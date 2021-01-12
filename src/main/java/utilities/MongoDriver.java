package utilities;

import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoDriver {
    private static MongoClient mongoClient;
    private static MongoDatabase mongoDatabase;

    public MongoDriver(){
        if(mongoClient==null){
            Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
            mongoLogger.setLevel(Level.WARNING);
            String uri = "mongodb://localhost:27017";
            mongoClient = MongoClients.create(uri);
            mongoDatabase = mongoClient.getDatabase("softgram");
        }
    }

    public MongoCollection<Document> getCollection(String collection){
        if(mongoClient!= null)
            return mongoDatabase.getCollection(collection);
        else throw new RuntimeException("Connection doesn't exist.");
    }

    public void close(){
        if(mongoClient!= null)
            mongoClient.close();
        else throw new RuntimeException("Connection doesn't exist.");
    }


}