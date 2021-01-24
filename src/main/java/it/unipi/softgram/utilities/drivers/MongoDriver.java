package it.unipi.softgram.utilities.drivers;


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
            try {
                mongoClient = MongoClients.create("mongodb://172.16.3.103:27020,172.16.3.104:27020,172.16.3.105:27020/"+
                        "retryWrites=true&w=majority&wtimeout=5000&readConcernLevel=majority");
                mongoDatabase = mongoClient.getDatabase("softgram");
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
