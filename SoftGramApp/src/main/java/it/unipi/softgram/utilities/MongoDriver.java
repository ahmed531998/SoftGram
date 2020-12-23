package it.unipi.softgram.utilities;

import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDriver {
    private static MongoClient mongoClient = null;
    private MongoDatabase mongoDatabase;

    public void connectMongo(){
        if(mongoClient!=null){
            this.mongoClient = MongoClients.create("mongodb://localhost:27017");
            this.mongoDatabase = this.mongoClient.getDatabase("proj");
        }
    }

    public MongoCollection<Document> getCollection(String collection){
        if(this.mongoClient!= null)
            return this.mongoDatabase.getCollection(collection);
        else throw new RuntimeException("Connection doesn't exist.");
    }

    public void close(){
        if(mongoClient!= null)
            this.mongoClient.close();
        else throw new RuntimeException("Connection doesn't exist.");
    }

}
