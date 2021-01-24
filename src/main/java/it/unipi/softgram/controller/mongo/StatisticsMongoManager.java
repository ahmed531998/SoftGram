package it.unipi.softgram.controller.mongo;

import com.mongodb.client.MongoCollection;
import it.unipi.softgram.utilities.drivers.MongoDriver;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;

import static com.mongodb.client.model.Aggregates.*;
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
    public Map<String, Double> getAppsWithAverageAbove(double threshold,int limit){
        try {
            Map<String, Double> toPlot = new LinkedHashMap<>();
            setThreshold(threshold);
            MongoCollection<Document> collection = driver.getCollection("review");
            Bson myGroup = new Document("$group", new Document("_id", "$appId")
                    .append("average", new Document().append("$avg", "$score")));
            Bson myMatch = match(gte("average", threshold));
            Bson mySort = sort(descending("average"));
            Bson myLimit = limit(limit);
            List<Document> output = collection.aggregate(
                    Arrays.asList(myGroup, myMatch, mySort,myLimit))
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
    public Map<String, Integer> getUserActivity(double threshold, int limit){
        try {
            Map<String, Integer> toPlot = new LinkedHashMap<>();
            setThreshold(threshold);
            MongoCollection<Document> collection = driver.getCollection("review");
            Bson myGroup = new Document("$group", new Document("_id", "$username")
                    .append("count", new Document().append("$sum", 1)));
            Bson myMatch = match(gte("count", threshold));
            Bson mySort = sort(descending("count"));
            Bson myLimit = limit(limit);
            List<Document> output = collection.aggregate(
                    Arrays.asList(myGroup, myMatch, mySort,myLimit))
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
            Map<Integer, Integer> toPlot = new LinkedHashMap<>();
            setThreshold(threshold);
            MongoCollection<Document> collection = driver.getCollection("review");

            Bson myGroup = new Document("$group", new Document("_id",  new Document("year", new Document().append("$year", "$date")))
                    .append("count", new Document().append("$sum", 1)));
            Bson mySort = sort(descending("count"));
            Bson myLimit = limit(5);
            List<Document> output = collection.aggregate(
                    Arrays.asList(myGroup, mySort,myLimit))
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


}
