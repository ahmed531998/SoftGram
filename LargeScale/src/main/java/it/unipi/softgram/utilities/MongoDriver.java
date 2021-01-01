package it.unipi.softgram.utilities;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;


public class MongoDriver {
    private static MongoDriver driver = null;
    private MongoClient client;
    private MongoDatabase db;

    private MongoDriver(String uri, String db){
        client = MongoClients.create(uri);
        this.db = client.getDatabase(db);
    }

    public static MongoDriver getMongoDriver(String uri, String db){
        if (driver == null) {
            driver = new MongoDriver(uri, db);
        }
        return driver;
    }

    public MongoCollection<Document> getDBCollection(String coll){
        try {
            return driver.db.getCollection(coll);
        } catch (RuntimeException e){
            e.printStackTrace();
        }
        return null;
    }

    public void close(){
        try {
            driver.client.close();
        } catch (RuntimeException e){
            e.printStackTrace();
        }
    }


}
