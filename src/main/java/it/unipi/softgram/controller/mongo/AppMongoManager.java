package it.unipi.softgram.controller.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import it.unipi.softgram.entities.App;
import it.unipi.softgram.entities.Review;
import it.unipi.softgram.utilities.drivers.MongoDriver;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;
import com.mongodb.client.model.Filters;
import org.bson.BsonRegularExpression;

import javax.print.Doc;
import java.util.List;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;


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
            MongoCollection<Document> appColl = driver.getCollection("app");
            Document d1 = a.toAppDocument();
            appColl.insertOne(d1);
            System.out.println("added");
        }
        catch (Exception e){
            throw new RuntimeException("write operation failed");
        }
    }

    public void updateApp(App a){
        try {
            MongoCollection<Document> appColl = driver.getCollection("app");
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
            throw new RuntimeException("write operation failed");
        }
    }

    public void deleteApp(App a){
        try {
            MongoCollection<Document> appColl = driver.getCollection("app");
            appColl.deleteOne(Filters.eq("_id", a.getId()));
            System.out.println("deleted");
        }
        catch (Exception e){
            throw new RuntimeException("write operation failed");
        }
    }

    public List<App> findApp(String text){
        try {
            MongoCollection<Document> collection = driver.getCollection("app");
            Pattern pattern = Pattern.compile("^" + text + ".*$");
            Bson filter1 = Filters.regex("_id", pattern);
            Bson filter2 = Filters.regex("name", pattern);
            List<Document> output = collection.find(or(filter1,filter2))
                    .limit(100)
                    .into(new ArrayList<>());
            List<App> apps = new ArrayList<>();
            App app = new App();
            for (Document d: output){
                apps.add(app.fromAppDocument(d));
            }
            return apps;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public void getMostPopularApps() {
        try {
            MongoCollection<Document> collection = driver.getCollection("review");

            Consumer<Document> processBlock = new Consumer<Document>() {
                @Override
                public void accept(Document document) {
                    System.out.println(document);
                }
            };

            List<? extends Bson> pipeline = Arrays.asList(
                    new Document()
                            .append("$project", new Document()
                                    .append("appId", 1.0)
                                    .append("appName", 1.0)
                                    .append("average", new Document()
                                            .append("$avg", "$score")
                                    )
                                    .append("numberOfReviews", new Document()
                                            .append("$sum", 1)
                                    )
                            ),
                    new Document()
                            .append("$sort", new Document()
                                    .append("average", -1.0)
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

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getMostPopularAppsInEachCat(String cat){
        try {
            MongoCollection<Document> collection = driver.getCollection("review");

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
                                    .append("appId", 1.0)
                                    .append("appName", 1.0)
                                    .append("category", 1.0)
                                    .append("average", new Document()
                                            .append("$avg", "$score")
                                    )
                                    .append("numberOfReviews", new Document()
                                            .append("$sum", 1)
                                    )
                            ),
                    new Document()
                            .append("$sort", new Document()
                                    .append("average", -1.0)
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getMostPopularAppsInEachYear(int year){
        MongoCollection<Document> collection = driver.getCollection("review");

        Consumer<Document> processBlock = new Consumer<Document>() {
            @Override
            public void accept(Document document) {
                System.out.println(document);
            }
        };

        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$project", new Document()
                                .append("appId", 1.0)
                                .append("appName", 1.0)
                                .append("category", 1.0)
                                .append("average", new Document()
                                        .append("$avg", "$score")
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
