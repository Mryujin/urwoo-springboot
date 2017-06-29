package com.urwoo.mongodb.repository.mongodb;

import com.mongodb.DBCollection;
import com.mongodb.WriteConcern;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.gridfs.GridFS;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Repository
public class MongoRepository {

    public static final String MONGO_ID = DBCollection.ID_FIELD_NAME;

    protected MongoDatabase database;

    protected GridFS gridFS;

    @Autowired(required = false)
    @Qualifier("mongoDatabase")
    public void setMongoDatabaseDescriptor(MongoDatabase database) {
        this.database = database;
    }

    @Autowired(required = false)
    public void setGridFSDescriptor(GridFS gridFS) {
        this.gridFS = gridFS;
    }

    public long count(String collection, Document filter) {
        return database.getCollection(collection).count(filter);
    }

    public void insert(String collection, Document insert) {
        database.getCollection(collection)
                .withWriteConcern(WriteConcern.W1).insertOne(insert);
    }

    public long update(String collection, Document filter, Document replace) {
        return database.getCollection(collection)
                .replaceOne(filter, replace).getModifiedCount();
    }

    public long deleteAll(String collection, Document filter) {
        return database.getCollection(collection).deleteMany(filter).getDeletedCount();
    }

    public long delete(String collection, Document filter) {
        return database.getCollection(collection).deleteOne(filter).getDeletedCount();
    }

    public <T> T get(String collection, Document filter, Function<Document, T> decoder) {
        FindIterable<Document> iterable = database.getCollection(collection).find(filter);
        try (MongoCursor<Document> cursor = iterable.iterator()) {
            Document document = cursor.tryNext();
            if (cursor.hasNext()) {
                throw new RuntimeException("查询对象不唯一");
            }
            return decoder.apply(document);
        }
    }

    public <T> List<T> list(String collection, Document filter, Function<Document, T> decoder,
                            Bson bson, Integer start, Integer limit) {
        FindIterable<Document> iterable = null;
        if (null == filter) {
            iterable = database.getCollection(collection).find();
        } else {
            iterable = database.getCollection(collection).find(filter);
        }
        if (bson != null) {
            iterable = iterable.sort(bson);
        }

        if (Objects.nonNull(start) && Objects.nonNull(limit)) {
            iterable.skip(start);
            iterable.limit(limit);
        }
        List<T> list = new ArrayList<>();
        try (MongoCursor<Document> cursor = iterable.iterator()) {
            for (; cursor.hasNext();) {
                list.add(decoder.apply(cursor.next()));
            }
            return list;
        }
    }
}
