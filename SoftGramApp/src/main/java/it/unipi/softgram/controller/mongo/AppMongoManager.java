package it.unipi.softgram.controller.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import it.unipi.softgram.entities.App;
import it.unipi.softgram.utilities.drivers.MongoDriver;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Arrays;
import java.util.function.Consumer;
import com.mongodb.client.model.Filters;
import org.bson.BsonRegularExpression;

import javax.print.Doc;
import java.util.List;


public class AppMongoManager {
    private MongoDriver driver;

    public AppMongoManager(){
        try{
            driver = new MongoDriver();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addApp(App a) {
        try {
            MongoCollection<Document> appColl = driver.getCollection("apps");
            Document d1 = a.toAppDocument();
            appColl.insertOne(d1);
            System.out.println("added");
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }
    public void updateApp(App a){
        try {
            MongoCollection<Document> appColl = driver.getCollection("apps");
            BasicDBObject searchQuery = new BasicDBObject("_id", a.getId());
            BasicDBObject updateFields = new BasicDBObject();
            updateFields.append("name", a.getName());
            updateFields.append("category", a.getCategory());
            updateFields.append("installCount", a.getInstallCount());
            BasicDBObject setQuery = new BasicDBObject();
            setQuery.append("$set", updateFields);
            appColl.updateOne(searchQuery, setQuery);
            System.out.println("updated");
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }



    public void deleteApp(String id){
        try {
            MongoDriver driver = new MongoDriver();
            MongoCollection<Document> appColl = driver.getCollection("apps");
            appColl.deleteOne(Filters.eq("_id", id));
            System.out.println("deleted");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }




    public void findApp(String text){
        //misses the try catch (by andrea)
        MongoCollection<Document> collection = driver.getCollection("apps");

        // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/
//maybe replaced with lambda (by andrea)
        Consumer<Document> processBlock = new Consumer<Document>() {
            @Override
            public void accept(Document document) {
                System.out.println(document);
            }
        };
        //watch the warning at Arrays.asList() (by andrea)
        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$match", new Document()
                                .append("$or", Arrays.asList(
                                        new Document()
                                                .append("_id", new Document()
                                                        .append("$regex", new BsonRegularExpression(text))
                                                ),
                                        new Document()
                                                .append("name", new Document()
                                                        .append("$regex", new BsonRegularExpression(text))
                                                )
                                        )
                                )
                        )
        );

        collection.aggregate(pipeline)
                .allowDiskUse(false)
                .forEach(processBlock);
    }
    public void MostPopularApps(){
        // misses the try catch (by andrea)
        MongoCollection<Document> collection = driver.getCollection("apps");

        // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

        Consumer<Document> processBlock = new Consumer<Document>() {
            @Override
            public void accept(Document document) {
                System.out.println(document);
            }
        };

        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$project", new Document()
                                .append("_id", 1.0)
                                .append("name", 1.0)
                                .append("Avg", new Document()
                                        .append("$avg", "$reviews.score")
                                )
                                .append("numberOfReviews", new Document()
                                        .append("$cond", new Document()
                                                .append("if", new Document()
                                                        .append("$isArray", "$reviews")
                                                )
                                                .append("then", new Document()
                                                        .append("$size", "$reviews")
                                                )
                                                .append("else", 0.0)
                                        )
                                )
                        ),
                new Document()
                        .append("$sort", new Document()
                                .append("Avg", -1.0)
                                .append("numberOfReviews", -1.0)
                        ),
                new Document()
                        .append("$skip", 0.0),
                new Document()
                        .append("$limit", 100.0)
        );

        collection.aggregate(pipeline)
                .allowDiskUse(false)
                .forEach(processBlock);
    }
    public void MostPopularAppsInEachCat(String cat){
        // misses the try catch (by andrea)
        MongoCollection<Document> collection = driver.getCollection("apps");

        // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/
        //maybe replace with lambda (by andrea)
        Consumer<Document> processBlock = new Consumer<Document>() {
            @Override
            public void accept(Document document) {
                System.out.println(document);
            }
        };

        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$match", new Document()
                                .append("category", cat)
                        ),
                new Document()
                        .append("$project", new Document()
                                .append("_id", 1.0)
                                .append("name", 1.0)
                                .append("category", 1.0)
                                .append("Avg", new Document()
                                        .append("$avg", "$reviews.score")
                                )
                                .append("numberOfReviews", new Document()
                                        .append("$cond", new Document()
                                                .append("if", new Document()
                                                        .append("$isArray", "$reviews")
                                                )
                                                .append("then", new Document()
                                                        .append("$size", "$reviews")
                                                )
                                                .append("else", 0.0)
                                        )
                                )
                        ),
                new Document()
                        .append("$sort", new Document()
                                .append("Avg", -1.0)
                                .append("numberOfReviews", -1.0)
                        ),
                new Document()
                        .append("$skip", 0.0),
                new Document()
                        .append("$limit", 100.0)
        );

        collection.aggregate(pipeline)
                .allowDiskUse(false)
                .forEach(processBlock);
    }
    public void MostPopularAppsInEachYear(int year){
        // misses the try catch (by andrea)
        MongoCollection<Document> collection = driver.getCollection("apps");

        // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/
        //maybe replace with lambda (by andrea)
        Consumer<Document> processBlock = new Consumer<Document>() {
            @Override
            public void accept(Document document) {
                System.out.println(document);
            }
        };

        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$project", new Document()
                                .append("_id", 1.0)
                                .append("name", 1.0)
                                .append("category", 1.0)
                                .append("released", 1.0)
                                .append("Avg", new Document()
                                        .append("$avg", "$reviews.score")
                                )
                                .append("numberOfReviews", new Document()
                                        .append("$cond", new Document()
                                                .append("if", new Document()
                                                        .append("$isArray", "$reviews")
                                                )
                                                .append("then", new Document()
                                                        .append("$size", "$reviews")
                                                )
                                                .append("else", 0.0)
                                        )
                                )
                                .append("year", new Document()
                                        .append("$year", "$released")
                                )
                        ),
                new Document()
                        .append("$match", new Document()
                                .append("year", year)
                        ),
                new Document()
                        .append("$sort", new Document()
                                .append("Avg", -1.0)
                                .append("numberOfReviews", -1.0)
                        ),
                new Document()
                        .append("$skip", 0.0),
                new Document()
                        .append("$limit", 100.0)
        );

        collection.aggregate(pipeline)
                .allowDiskUse(false)
                .forEach(processBlock);
    }
}
