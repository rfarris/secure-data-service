/*
 * Copyright 2012 Shared Learning Collaborative, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.slc.sli.search.process.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slc.sli.search.config.IndexConfig;
import org.slc.sli.search.config.IndexConfigStore;
import org.slc.sli.search.connector.SearchEngineConnector;
import org.slc.sli.search.entity.IndexEntity;
import org.slc.sli.search.entity.IndexEntity.Action;
import org.slc.sli.search.process.Indexer;
import org.slc.sli.search.util.IndexEntityUtil;
import org.slc.sli.search.util.NestedMapUtil;
import org.slc.sli.search.util.SearchIndexerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

/**
 * Indexer is responsible for building elastic search index requests and
 * sending them to the elastic search server for processing.
 * 
 * @author dwu
 * 
 */
public class IndexerImpl implements Indexer {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private static final int DEFAULT_BULK_SIZE = 3000;
    private static final int MAX_AGGREGATE_PERIOD = 500;
    
    private static final int INDEX_WORKER_POOL_SIZE = 4;
    
    private int bulkSize = DEFAULT_BULK_SIZE;
    // queue of indexrequests limited to bulkSize
    private LinkedBlockingQueue<IndexEntity> indexRequests;
    
    private IndexConfigStore indexConfigStore;

    private int indexerWorkerPoolSize = INDEX_WORKER_POOL_SIZE;
    
    private ScheduledExecutorService queueWatcherExecutor;

    private long aggregatePeriodInMillis = MAX_AGGREGATE_PERIOD;
    
    private SearchEngineConnector connector;
    
    // this is helpful to avoid adding index mappings for each index operation. The map does not have to be accurate
    // and no harm will be done if re-mapping is issued
    private final ConcurrentHashMap<String, Boolean> knownIndexesMap = new ConcurrentHashMap<String, Boolean>();

    
    public void init() {
        indexRequests = new LinkedBlockingQueue<IndexEntity>(bulkSize * indexerWorkerPoolSize);
        queueWatcherExecutor = Executors.newScheduledThreadPool(indexerWorkerPoolSize);
        logger.info("Indexer started");
        queueWatcherExecutor.scheduleAtFixedRate(new IndexQueueMonitor(), aggregatePeriodInMillis, aggregatePeriodInMillis, TimeUnit.MILLISECONDS);
    }
    
    public void destroy() {
        queueWatcherExecutor.shutdown();
    }
    
    /**
     * Issue bulk index request if reached the size or not empty and last update past tolerance period
     *
     */
    private class IndexQueueMonitor implements Runnable {
        public void run() {
            try {
                if (!indexRequests.isEmpty()) {
                    queueWatcherExecutor.execute(new Runnable() {
                        
                        public void run() {
                            flushIndexQueue();
                        }
                    }); 
                }
            } catch (Throwable t) {
                logger.info("Unable to index with elasticsearch", t);
            }
        }
    }
    
    /**
     * flush/index accumulated index records
     */
    public void flushIndexQueue() {
        final List<IndexEntity> col = new ArrayList<IndexEntity>();
        indexRequests.drainTo(col, bulkSize);
        if (!col.isEmpty())
            executeBulk(col);
    }
    
    
    /* (non-Javadoc)
     * @see org.slc.sli.search.process.Indexer#index(org.slc.sli.search.entity.IndexEntity)
     */
    public void index(IndexEntity ie) {
        try {
            if (ie != null) {
                addIndexMappingIfNeeded(ie.getIndex());
                indexRequests.put(ie);
            }
        } catch (InterruptedException e) {
            throw new SearchIndexerException("Shutting down...");
        } 
    }
    
    /**
     * Takes a collection of index requests, builds a bulk http message to send to elastic search
     * 
     * @param indexRequests
     */
    @SuppressWarnings("unchecked")
    public void executeBulkGetUpdate(List<IndexEntity> updates) {
        logger.info("Preparing _mget request with " + updates.size() + " records");
        if (updates.isEmpty())
            return;
        Map<String, IndexEntity> indexUpdateMap = new HashMap<String, IndexEntity>();
        for (IndexEntity ie : updates) {
            indexUpdateMap.put(ie.getId(), ie);
        }
        try {
            String request = IndexEntityUtil.getBulkGetJson(updates);
            logger.info("Sending _mget request with " + updates.size() + " records");
            String body = connector.executePost(connector.getMGetUri(), request);
            Map<String, Object> orig;
            List<Map<String, Object>> docs = (List<Map<String, Object>>)IndexEntityUtil.getEntity(body).get("docs");
            IndexEntity ie;
            final List<IndexEntity> reindex = new ArrayList<IndexEntity>();
            for (Map<String, Object> entity : docs) {
                if (entity != null && entity.containsKey("exists") && (Boolean)entity.get("exists")) {
                    orig = (Map<String, Object>) entity.get("_source");
                    try {
                        ie = indexUpdateMap.remove(IndexEntityUtil.getIndexEntity(entity).getId());
                        
                        if (ie != null) {
                            // if an update happened, re-index, if no update, skip the insert
                            if (NestedMapUtil.merge(orig, ie.getBody()))
                                reindex.add(new IndexEntity(ie.getIndex(), ie.getType(), ie.getId(), orig));
                        } else {
                            logger.error("Unable to match response from get " + entity.get("_id"));
                        }
                    } catch (Exception e) {
                        logger.error("Unable to process entry from ES for re-index " + entity.get("_id"));
                    }
                } else { // if doesn't exist, add
                    ie = indexUpdateMap.remove(entity.get("_id"));
                    if (ie != null)
                        reindex.add(ie);
                }
            }
            executeBulkIndex(reindex);
        } catch (Exception re) {
            logger.error("Error on mget.", re);
        }        
    }
    
    public void executeBulk(List<IndexEntity> docs) {
        ListMultimap<Action, IndexEntity> docMap =  ArrayListMultimap.create();
        for (IndexEntity ie : docs) {
            docMap.put(ie.getAction(), ie);
        }
        if (docMap.containsKey(Action.INDEX)) {
            executeBulkIndex(docMap.get(Action.INDEX));
        }
        if (docMap.containsKey(Action.UPDATE)) {
            executeBulkGetUpdate(docMap.get(Action.UPDATE));
        }
        if (docMap.containsKey(Action.QUICK_UPDATE)) {
            executeUpdate(docMap.get(Action.QUICK_UPDATE));
        }
        if (docMap.containsKey(Action.DELETE)) {
            executeBulkDelete(docMap.get(Action.DELETE));
        }
    }
    
    /**
     * Takes a collection of index requests, builds a bulk http message to send to elastic search
     * 
     * @param indexRequests
     */
    public void executeBulkIndex(List<IndexEntity> docs) {
        if (docs.isEmpty()) {
            return;
        }
        logger.info("Preparing _bulk request with " + docs.size() + " records");
        // create bulk http message
        /*
         * format of message data
         * { "index" : { "_index" : "test", "_type" : "type1", "_id" : "1" } }
         * { "field1" : "value1" }
         */
        
        // add each index request to the message
        String message = IndexEntityUtil.getBulkIndexJson(docs);
        logger.info("Sending _bulk request with " + docs.size() + " records");
         // send the message
        connector.executePost(connector.getBulkUri(), message);
        logger.info("Bulk index response: OK");
    }
    
    /**
     * Takes a collection of delete requests, send a bulk delete to elastic search
     * @param docs
     */
    public void executeBulkDelete(List<IndexEntity> docs) {
        if (docs.isEmpty()) {
            return;
        }
        logger.info("Preparing _bulk delete request with " + docs.size() + " records");

        String message = IndexEntityUtil.getBulkDeleteJson(docs);
        logger.info("Sending _bulk delete request with " + docs.size() + " records");
        // send the message
        connector.executePost(connector.getBulkUri(), message);
        logger.info("Bulk delete response: OK");
    }
    
    /**
     * Takes a collection of index requests, builds a bulk http message to send to elastic search
     * 
     * @param indexRequests
     */
    public void executeUpdate(List<IndexEntity> docs) {
        logger.info("Sending update requests for " + docs.size() + " records");
        StringBuilder sb = new StringBuilder();
        logger.info(IndexEntityUtil.toUpdateJson(docs.get(0)));
        for (IndexEntity ie : docs) {
            try {
            sb.append(connector.executePost(
                    connector.getUpdateUri(), IndexEntityUtil.toUpdateJson(ie), ie.getIndex(), ie.getType(), ie.getId()).toString());
            }
            catch (Exception e) {
                logger.error("Unable to update entry for " + ie, e);
            }
        }
        logger.info(sb.toString());
        // TODO: do we need to check the response status of each part of the bulk request?
        
    }
    
    /**
     * Add index mapping if no index is found in the list of known indexes. Can be repeated.
     * @param index
     */
    public void addIndexMappingIfNeeded(String index) {
        synchronized(knownIndexesMap) {
            if (!knownIndexesMap.containsKey(index)) {
                logger.info("Updating mappings for " + index);
                try {
                    if (connector.executeHead(connector.getIndexUri(), index) != HttpStatus.OK) {
                        logger.info("Creating new index " + index);
                        connector.executePost(connector.getIndexUri(), null, index);
                    }
                    HttpStatus response;
                    for (IndexConfig config : indexConfigStore.getConfigs()) {
                        if (!config.isChildDoc()) {
                            response = connector.executePut(
                                    connector.getIndexTypeUri() + "/_mapping?ignore_conflicts=true", 
                                    IndexEntityUtil.getBodyForIndex(config.getMapping()),
                                    index, config.getCollectionName());
                            logger.info(String.format("Mapping response: %s ", response));
                        }
                    }
                    
                } catch (Exception e) {
                    logger.info("Index " + index + " already exists");
                }
                knownIndexesMap.put(index, Boolean.TRUE);
            }
        }
    }
    
    public void setBulkSize(int bulkSize) {
        this.bulkSize = bulkSize;
    }
    
    public void setAggregatePeriod(long aggregatePeriodInMillis) {
        this.aggregatePeriodInMillis  = aggregatePeriodInMillis;
    }
    
    public void setIndexerWorkerPoolSize(int indexerWorkerPoolSize) {
        this.indexerWorkerPoolSize  = indexerWorkerPoolSize;
    }
    
    public void setIndexConfigStore(IndexConfigStore indexConfigStore) {
        this.indexConfigStore = indexConfigStore;
    }
    
    public void setSearchEngineConnector(SearchEngineConnector searchEngineConnector) {
        this.connector = searchEngineConnector;
    }
    
    public String getHealth() {
        ThreadPoolExecutor tpe = (ThreadPoolExecutor)queueWatcherExecutor;
        return getClass() + ": {indexRequests size:" + indexRequests.size() + ", active count:" + tpe.getActiveCount() +
            ", completed count:" + tpe.getCompletedTaskCount() + "}";
    }
}
