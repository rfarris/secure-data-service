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

package org.slc.sli.bulk.extract.lea;

import java.util.Iterator;
import java.util.Set;

import org.slc.sli.bulk.extract.extractor.EntityExtractor;
import org.slc.sli.common.constants.ParameterConstants;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.domain.Repository;

public class YearlyTranscriptExtractor implements EntityExtract {
    private EntityExtractor extractor;
    private LEAExtractFileMap map;
    private Repository<Entity> repo;
    
    public YearlyTranscriptExtractor(EntityExtractor extractor, LEAExtractFileMap map, Repository<Entity> repo) {
        this.extractor = extractor;
        this.map = map;
        this.repo = repo;
    }

    @Override
    public void extractEntities(EntityToLeaCache entityToEdorgCache) {
        Iterator<Entity> yearlyTranscripts = repo.findEach("yearlyTranscript", new NeutralQuery());
        
        while (yearlyTranscripts.hasNext()) {
            Entity yearlyTranscript = yearlyTranscripts.next();
            String studentId = (String) yearlyTranscript.getBody().get(ParameterConstants.STUDENT_ID);
            Set<String> studentLeas = entityToEdorgCache.getEntriesById(studentId);
            
            for (String lea : studentLeas) {
                extractor.extractEntity(yearlyTranscript, map.getExtractFileForLea(lea), "yearlyTranscript");
            }
        }
        
    }
    
}
