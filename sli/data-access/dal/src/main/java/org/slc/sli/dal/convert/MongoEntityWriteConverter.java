package org.slc.sli.dal.convert;

import com.mongodb.DBObject;

import org.springframework.core.convert.converter.Converter;

import org.slc.sli.domain.MongoEntity;

public class MongoEntityWriteConverter implements Converter<MongoEntity, DBObject> {
    
    @Override
    public DBObject convert(MongoEntity e) {
        return e.toDBObject();
    }
    
}
