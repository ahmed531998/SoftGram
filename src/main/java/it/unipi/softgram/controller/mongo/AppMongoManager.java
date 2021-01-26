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
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
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

    public List<App> findApp(String text, int limit, int skip){
        try {
            MongoCollection<Document> collection = driver.getCollection("app");
            Pattern pattern = Pattern.compile(".*" + text + ".*$");
            Bson filter1 = Filters.regex("_id", pattern);
            Bson filter2 = Filters.regex("name", pattern);
            List<Document> output = collection.find(or(filter1,filter2))
                    .skip(skip)
                    .limit(limit)
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

    public List<App> findAppByCategory(String cat, int limit, int skip){
        try {
            MongoCollection<Document> collection = driver.getCollection("app");
            Pattern pattern = Pattern.compile(".*" + cat + ".*$");
            Bson filter1 = Filters.regex("category", pattern);
            List<Document> output = collection.find(filter1)
                    .skip(skip)
                    .limit(limit)
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

    public List<App> findAppBySize(Double size, int limit, int skip){
        try {
            MongoCollection<Document> collection = driver.getCollection("app");
            Bson filter1 = match(gte("size", size));
            List<Document> output = collection.find(filter1)
                    .skip(skip)
                    .limit(limit)
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
    public List<App> findAppByPrice(Double price, int limit, int skip){
        try {
            MongoCollection<Document> collection = driver.getCollection("app");
            Bson filter1 = match(gte("price", price));
            List<Document> output = collection.find(filter1)
                    .skip(skip)
                    .limit(limit)
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
    public List<App> findAppByAd(Boolean adSupported, int limit, int skip){
        try {
            MongoCollection<Document> collection = driver.getCollection("app");
            Bson filter1 = match(gte("adSupported", adSupported));
            List<Document> output = collection.find(filter1)
                    .skip(skip)
                    .limit(limit)
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

    public List<App> findAppByRelease(int year, int limit, int skip){
        try {
            MongoCollection<Document> collection = driver.getCollection("app");
            Bson myGroup = new Document("$project", new Document("_id", "$_id")
                    .append("name", "name")
                    .append("category", "$category")
                    .append("year", new Document()
                            .append("$year", "released")));
            Bson filter1 = match(eq("year", year));
            List<Document> output = collection.aggregate( Arrays.asList(myGroup, filter1, skip(skip), limit(limit)))
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
                    Collections.singletonList(myGroup))
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
                    Collections.singletonList(myGroup))
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

    public List<App> getPopularApps(int limit, int skip){
        try{
            MongoCollection<Document> collection = driver.getCollection("review");
            Bson myGroup = new Document("$group", new Document("_id", new Document("appId", "$appId").append("name", "$appName"))
                    .append("average", new Document()
                            .append("$avg", "$score"))
                    .append("count", new Document()
                            .append("$sum", 1)));
            Bson mySort = sort(descending("average", "count"));
            Bson mySkip = skip(skip);
            Bson myLimit = limit(limit);

            List <Document> output = collection.aggregate(
                    Arrays.asList(myGroup, mySort, mySkip, myLimit))
                    .into(new ArrayList<>());

            List <App> result = new ArrayList<>();
            for (Document d: output){
                App a = new App();
                Document id = (Document) d.get("_id");
                String appId = (String) id.get("appId");
                String appName = (String) id.get("name");
                Integer reviewCount = (Integer) d.get("count");
                Double average = (Double) d.get("average");
                a.setName(appName);
                a.setId(appId);
                a.setAverage(average);
                a.setReviewCount(reviewCount);
                result.add(a);
            }
            return result;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public List<App> getPopularAppsPerCat(String cat, int limit, int skip){
        try {
            MongoCollection<Document> collection = driver.getCollection("review");

            Bson myMatch = match(eq("category",cat));
            Bson myGroup = new Document("$group", new Document("_id", new Document("appId", "$appId").append("name", "$appName"))
                    .append("average", new Document()
                            .append("$avg", "$score"))
                    .append("count", new Document()
                            .append("$sum", 1)));
            Bson mySort = sort(descending("average", "count"));
            Bson mySkip = skip(skip);
            Bson myLimit = limit(limit);
            List <Document> output = collection.aggregate(
                    Arrays.asList(myMatch, myGroup, mySort, mySkip, myLimit))
                    .into(new ArrayList<>());

            List <App> result = new ArrayList<>();
            for (Document d: output){
                App a = new App();
                Document id = (Document) d.get("_id");
                String appId = (String) id.get("appId");
                String appName = (String) id.get("name");
                Integer reviewCount = (Integer) d.get("count");
                Double average = (Double) d.get("average");
                a.setName(appName);
                a.setId(appId);
                a.setAverage(average);
                a.setReviewCount(reviewCount);
                a.setCategory(cat);
                result.add(a);
            }
            return result;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<App> getPopularAppsPerYear(int year, int limit, int skip){
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
            Bson mySkip = skip(skip);
            Bson myLimit = limit(limit);
            List <Document> output = collection.aggregate(
                    Arrays.asList(myGroup, myMatch, mySort, mySkip, myLimit))
                    .into(new ArrayList<>());

            List <App> result = new ArrayList<>();
            for (Document d: output){
                App a = new App();
                String id = (String) d.get("_id");
                String name = (String) d.get("appName");
                Integer reviewCount = (Integer) d.get("count");
                Double average = (Double) d.get("average");
                String category = (String) d.get("category");
                a.setName(name);
                a.setId(id);
                a.setAverage(average);
                a.setReviewCount(reviewCount);
                a.setYearOfInterest(year);
                a.setCategory(category);
                result.add(a);
            }
            return result;
    }catch(Exception e){
        e.printStackTrace();
    }
        return null;
    }
}