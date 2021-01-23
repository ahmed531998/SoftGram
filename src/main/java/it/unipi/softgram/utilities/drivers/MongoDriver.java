package it.unipi.softgram.utilities.drivers;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoDriver {
    private static MongoClient mongoClient;
    private static MongoDatabase mongoDatabase;

    public MongoDriver(){
        if(mongoClient==null){
            Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
            mongoLogger.setLevel(Level.WARNING);
            ConnectionString uri = new ConnectionString("mongodb://localhost:27017");
            try {
                mongoClient = MongoClients.create(uri);
                mongoDatabase = mongoClient.getDatabase("proj");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public MongoCollection<Document> getCollection(String collection){
        if(mongoClient!= null)
            return mongoDatabase.getCollection(collection);
        else throw new RuntimeException("Connection doesn't exist.");
    }

    public static void close(){
        if(mongoClient!= null)
            mongoClient.close();
        else throw new RuntimeException("Connection doesn't exist.");
    }

}
