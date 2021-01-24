package it.unipi.softgram.controller.mongo;

import com.mongodb.client.MongoCollection;
import it.unipi.softgram.entities.User;
import it.unipi.softgram.utilities.drivers.MongoDriver;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.print.Doc;
import java.util.*;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Sorts.descending;

public class StatisticsMongoManager {
    private double threshold;
    private final MongoDriver driver;

    private void setThreshold(double threshold){
        this.threshold = threshold;
    }

    public StatisticsMongoManager(){
        this.driver = new MongoDriver();
    }
    public Map<String, Double> getAppsWithAverageAbove(double threshold){
        try {
            Map<String, Double> toPlot = new LinkedHashMap<>();
            setThreshold(threshold);
            MongoCollection<Document> collection = driver.getCollection("review");
            Bson myGroup = new Document("$group", new Document("_id", "$appId")
                    .append("average", new Document().append("$avg", "$score")));
            Bson myMatch = match(gte("average", threshold));
            Bson mySort = sort(descending("average"));
            List<Document> output = collection.aggregate(
                    Arrays.asList(myGroup, myMatch, mySort))
                    .into(new ArrayList<>());
            for (Document d : output) {
                toPlot.put((String) d.get("_id"), (Double) d.get("average"));
            }
            System.out.println(toPlot);
            return toPlot;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public Map<String, Integer> getUserActivity(double threshold){
        try {
            Map<String, Integer> toPlot = new LinkedHashMap<>();
            setThreshold(threshold);
            MongoCollection<Document> collection = driver.getCollection("review");
            Bson myGroup = new Document("$group", new Document("_id", "$username")
                    .append("count", new Document().append("$sum", 1)));
            Bson myMatch = match(gte("count", threshold));
            Bson mySort = sort(descending("count"));
            List<Document> output = collection.aggregate(
                    Arrays.asList(myGroup, myMatch, mySort))
                    .into(new ArrayList<>());
            for (Document d : output) {
                toPlot.put((String) d.get("_id"), (Integer) d.get("count"));
            }
            System.out.println(toPlot);
            return toPlot;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public Map<Integer, Integer> monitorYearlyActivity(){
        try {
            Map<Integer, Integer> toPlot = new TreeMap<>();
            MongoCollection<Document> collection = driver.getCollection("review");

            Bson myGroup = new Document("$group", new Document("_id",  new Document("year", new Document().append("$year", "$date")))
                    .append("count", new Document().append("$sum", 1)));
            List<Document> output = collection.aggregate(
                    Arrays.asList(myGroup))
                    .into(new ArrayList<>());
            for (Document d : output) {
                Document y = (Document) d.get("_id");
                toPlot.put((Integer) y.get("year"), (Integer) d.get("count"));
            }
            System.out.println(toPlot);
            return toPlot;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public Map<Integer, Integer> monitorMonthlyActivity(int year){
        try {
            Map<Integer, Integer> toPlot = new TreeMap<>();
            MongoCollection<Document> collection = driver.getCollection("review");
            Bson myProject = new Document("$project", new Document("year", new Document().append("$year", "$date"))
                    .append("month", new Document().append("$month", "$date")));
            Bson myMatch = match(eq("year", year));
            Bson myGroup = new Document("$group", new Document("_id",  new Document("month", "$month"))
                    .append("count", new Document().append("$sum", 1)));
            List<Document> output = collection.aggregate(
                    Arrays.asList(myProject, myMatch, myGroup))
                    .into(new ArrayList<>());
            for (Document d : output) {
                Document y = (Document) d.get("_id");
                toPlot.put((Integer) y.get("month"), (Integer) d.get("count"));
            }
            System.out.println(toPlot);
            return toPlot;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public Map<Integer, Integer> getYearlyUserActivityProfile(User u){
        try {
            Map<Integer, Integer> toPlot = new TreeMap<>();
            MongoCollection<Document> collection = driver.getCollection("review");

            Bson myMatch = match(eq("username", u.getUsername()));
            Bson myGroup = new Document("$group", new Document("_id",  new Document("year", new Document().append("$year", "$date")))
                    .append("count", new Document().append("$sum", 1)));
            List<Document> output = collection.aggregate(
                    Arrays.asList(myMatch, myGroup))
                    .into(new ArrayList<>());
            for (Document d : output) {
                Document y = (Document) d.get("_id");
                toPlot.put((Integer) y.get("year"), (Integer) d.get("count"));
            }
            System.out.println(toPlot);
            return toPlot;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public Map<Integer, String> getBestAppEachYear(){
        try {
            Map<Integer, String> toPlot = new TreeMap<>();
            setThreshold(threshold);
            MongoCollection<Document> collection = driver.getCollection("review");
            Bson myProject = new Document("$project", new Document("year", new Document().append("$year", "$date"))
                    .append("appId", "$appId")
                    .append("score", "$score"));
            Bson myGroup = new Document("$group", new Document("_id", new Document("appId", "$appId").append("year", "$year") )
                    .append("average", new Document().append("$avg", "$score")));

            Bson mySort = sort(descending("_id.year", "average"));

            Bson myGroup2 = new Document("$group", new Document("_id", "$_id.year")
                    .append("best", new Document().append("$first", "$_id.appId")));

            List<Document> output = collection.aggregate(
                    Arrays.asList(myProject, myGroup, mySort, myGroup2))
                    .into(new ArrayList<>());
            for (Document d : output) {
                toPlot.put((Integer) d.get("_id"), (String) d.get("best"));
            }
            System.out.println(toPlot);
            return toPlot;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public Map<Integer, List<Map<String, String>>> getBestAppInEachCategoryEachYear(){
        try {
            Map<Integer, List<Map<String, String>>> toPlot = new TreeMap<>();
            MongoCollection<Document> collection = driver.getCollection("review");
            Bson myProject = new Document("$project", new Document("year", new Document().append("$year", "$date"))
                    .append("appId", "$appId")
                    .append("score", "$score")
                    .append("category", "$category"));

            Bson myGroup = new Document("$group", new Document("_id", new Document("appId", "$appId").append("year", "$year").append("category", "$category") )
                    .append("average", new Document().append("$avg", "$score")));

            Bson mySort = sort(descending("_id.year", "_id.category", "average"));

            Bson myGroup2 = new Document("$group", new Document("_id", new Document("year", "$_id.year").append("category", "$_id.category"))
                    .append("best", new Document().append("$first", "$_id.appId")));

            List<Document> output = collection.aggregate(
                    Arrays.asList(myProject, myGroup, mySort, myGroup2))
                    .into(new ArrayList<>());

            for (Document d : output) {
                List<Map<String, String>> container = new ArrayList<>();
                Map<String, String> x = new LinkedHashMap<>();
                Document id = (Document) d.get("_id");
                x.put((String) id.get("category"), (String) d.get("best"));
                if (toPlot.containsKey(id.get("year"))) {
                    container = toPlot.get(id.get("year"));
                    container.add(x);
                    toPlot.put((Integer) id.get("year"), container);
                } else {
                    container.add(x);
                    toPlot.put((Integer) id.get("year"), container);
                }
            }
            System.out.println(toPlot);
            return toPlot;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, List<Map<Integer, Double>>> getYearlyAverageCategoryRate(){
        try {
            Map<String, List<Map<Integer, Double>>> toPlot = new TreeMap<>();
            MongoCollection<Document> collection = driver.getCollection("review");
            Bson myProject = new Document("$project", new Document("year", new Document().append("$year", "$date"))
                    .append("appId", "$appId")
                    .append("score", "$score")
                    .append("category", "$category"));

            Bson myGroup = new Document("$group", new Document("_id", new Document().append("year", "$year").append("category", "$category") )
                    .append("average", new Document().append("$avg", "$score")));

            List<Document> output = collection.aggregate(
                    Arrays.asList(myProject, myGroup))
                    .into(new ArrayList<>());

            for (Document d : output) {
                List<Map<Integer, Double>> container = new ArrayList<>();
                Map<Integer, Double> x = new TreeMap<>();
                Document id = (Document) d.get("_id");
                x.put((Integer) id.get("year"), (Double) d.get("average"));
                if (toPlot.containsKey(id.get("category"))) {
                    container = toPlot.get(id.get("category"));
                    container.add(x);
                    toPlot.put((String) id.get("category"), container);
                } else {
                    container.add(x);
                    toPlot.put((String) id.get("category"), container);
                }
            }
            System.out.println(toPlot);
            return toPlot;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
