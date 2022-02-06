package rip.orbit.mars.util;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;

import rip.orbit.mars.Mars;

import org.bson.Document;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class MongoUtils {

    /**
     * UpdateOptions used to perform an upsert (see https://docs.mongodb.com/manual/reference/method/db.collection.update/)
     */
    public static final UpdateOptions UPSERT_OPTIONS = new UpdateOptions().upsert(true);

    /**
     * Convenience method to retrieve a MongoCollection object from singleton
     * MongoDatabase.
     *
     * @Parameter collectionId Id of collection to retrieve
     * @return MongoCollection for specified collection id.
     */
    public static MongoCollection<Document> getCollection(String collectionId) {
        return Mars.getInstance().getMongoDatabase().getCollection(collectionId);
    }

}