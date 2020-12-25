package it.unipi.softgram.utilities;

import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoDriver {
    private static MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    public MongoDriver(){
        if(mongoClient==null){
            Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
            mongoLogger.setLevel(Level.SEVERE);
            mongoClient = MongoClients.create("mongodb://localhost:27017");
            this.mongoDatabase = mongoClient.getDatabase("proj");
        }
    }

    public MongoCollection<Document> getCollection(String collection){
        if(mongoClient!= null)
            return this.mongoDatabase.getCollection(collection);
        else throw new RuntimeException("Connection doesn't exist.");
    }

    public void close(){
        if(mongoClient!= null)
            mongoClient.close();
        else throw new RuntimeException("Connection doesn't exist.");
    }

}
