package it.unipi.softgram.controller.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import it.unipi.softgram.entities.App;
import it.unipi.softgram.utilities.drivers.MongoDriver;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.or;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Updates.set;


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

    //size, price, last updated, currency, ad supported, age group, in app purchases, released
    public void updateApp(App a){
        try {
            MongoCollection<Document> appColl = driver.getCollection("app");
            BasicDBObject searchQuery = new BasicDBObject("_id", a.getId());
            BasicDBObject updateFields = new BasicDBObject();
            updateFields.append("name", a.getName());
            updateFields.append("size", a.getSize());
            updateFields.append("lastUpdated", a.getLastUpdated());
            updateFields.append("currency", a.getCurrency());
            updateFields.append("adSupported", a.getAdSupported());
            updateFields.append("ageGroup", a.getAgeGroup());
            updateFields.append("inAppPurchases", a.getInAppPurchase());
            updateFields.append("released", a.getReleased());
            updateFields.append("price", a.getPrice());
            BasicDBObject setQuery = new BasicDBObject();
            setQuery.append("$set", updateFields);
            appColl.updateOne(searchQuery, setQuery);
            System.out.println("updated");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateCategory(App a){
        try {
            MongoCollection<Document> userColl = driver.getCollection("app");
            UpdateResult result = userColl.updateOne(eq("_id", a.getId()),
                    set("category",a.getCategory()));
            if(result.getModifiedCount()==0){
                System.out.println("Requested app to update not found");
            }
        }
        catch (Exception e){
            throw new RuntimeException("write operation failed");
        }
    }

    public void updateName(App a){
    try {
        MongoCollection<Document> userColl = driver.getCollection("app");
        UpdateResult result = userColl.updateOne(eq("_id", a.getId()),
                set("name",a.getName()));
        if(result.getModifiedCount()==0){
            System.out.println("Requested app to update not found");
        }
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
            Pattern pattern = Pattern.compile(".*" + text + ".*$");
            Bson filter1 = Filters.regex("_id", pattern);
            Bson filter2 = Filters.regex("name", pattern);
            List<Document> output = collection.find(or(filter1,filter2))
                    .limit(100)
                    .into(new ArrayList<>());
            List<App> apps = new ArrayList<>();
            for (Document d: output){
                App app = new App();
                apps.add(app.fromAppDocument(d));
            }
            return apps;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public double getAverageRating(App a){
        try{
            MongoCollection<Document> collection = driver.getCollection("review");
            Bson myGroup = new Document("$group", new Document("_id", a.getId())
                    .append("average", new Document()
                            .append("$avg", "$score")));

            List <Document> output = collection.aggregate(
                    Arrays.asList(myGroup))
                    .into(new ArrayList<>());
            if (output.size() > 0) {
                Document x = output.get(0);
                return (Double) x.get("average");
            }
            else return 0.0;
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0.0;
    }

    public int getRatingCount(App a){
        try{
            MongoCollection<Document> collection = driver.getCollection("review");
            Bson myGroup = new Document("$group", new Document("_id", a.getId())
                    .append("count", new Document()
                            .append("$sum", 1)));

            List <Document> output = collection.aggregate(
                    Arrays.asList(myGroup))
                    .into(new ArrayList<>());
            if (output.size() > 0) {
                Document x = output.get(0);
                return (Integer) x.get("count");
            }
            else return 0;
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public List<Document> getPopularApps(int limit){
        try{
            MongoCollection<Document> collection = driver.getCollection("review");
            Bson myGroup = new Document("$group", new Document("_id", "$appId")
                    .append("average", new Document()
                            .append("$avg", "$score"))
                    .append("count", new Document()
                            .append("$sum", 1)));
            Bson mySort = sort(descending("average", "count"));
            Bson mySkip = skip(0);
            Bson myLimit = limit(limit);

            List <Document> output = collection.aggregate(
                    Arrays.asList(myGroup, mySort, mySkip, myLimit))
                    .into(new ArrayList<>());

            return output;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public List<Document> getPopularAppsPerCat(String cat, int limit){
        try {
            MongoCollection<Document> collection = driver.getCollection("review");

            Bson myMatch = match(eq("category",cat));
            Bson myGroup = new Document("$group", new Document("_id", "$appId")
                    .append("average", new Document()
                            .append("$avg", "$score"))
                    .append("count", new Document()
                            .append("$sum", 1)));
            Bson mySort = sort(descending("average", "count"));
            Bson mySkip = skip(0);
            Bson myLimit = limit(limit);
            List <Document> output = collection.aggregate(
                    Arrays.asList(myMatch, myGroup, mySort, mySkip, myLimit))
                    .into(new ArrayList<>());

            return output;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Document> getPopularAppsPerYear(int year, int limit){
        try{
            MongoCollection<Document> collection = driver.getCollection("review");

            Bson myGroup = new Document("$project", new Document("_id", "$appId")
                    .append("appName", "$appName")
                    .append("category", "$category")
                    .append("year", new Document()
                            .append("$year", "$date"))
                    .append("average", new Document()
                            .append("$avg", "$score"))
                    .append("count", new Document()
                            .append("$sum", 1)));
            Bson myMatch = match(eq("year", year));
            Bson mySort = sort(descending("average", "count"));
            Bson mySkip = skip(0);
            Bson myLimit = limit(limit);
            List <Document> output = collection.aggregate(
                    Arrays.asList(myGroup, myMatch, mySort, mySkip, myLimit))
                    .into(new ArrayList<>());

            return output;
    }catch(Exception e){
        e.printStackTrace();
    }
        return null;
    }
}