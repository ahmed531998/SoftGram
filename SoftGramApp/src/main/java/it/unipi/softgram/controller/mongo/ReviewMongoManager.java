package it.unipi.softgram.controller.mongo;

import com.mongodb.client.MongoCollection;
import it.unipi.softgram.entities.Review;
import it.unipi.softgram.utilities.drivers.MongoDriver;
import org.bson.Document;

import com.mongodb.client.model.Updates;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;


import org.bson.conversions.Bson;


import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Aggregates.*;
import  static com.mongodb.client.model.Projections.*;

public class ReviewMongoManager {
    private MongoDriver driver;
//before never used (by andrea)
    public enum DateQuery{
        Before,
        After,
        On
    }

    public ReviewMongoManager(){
       try{
           driver = new MongoDriver();
       }
       catch (Exception e){
           e.printStackTrace();
       }
    }

    public void postNewReview(Review review) {
        Document appDoc = review.toAppCollDocument();
        Document userDoc = review.toUserCollDocument();
        try {
            MongoCollection<Document> userCollection = driver.getCollection("user");
            MongoCollection<Document> appCollection = driver.getCollection("app");
            userCollection.updateOne(eq("_id", review.getUserId()),
                    Updates.addToSet("reviews", userDoc));
            appCollection.updateOne(eq("_id", review.getAppId()),
                    Updates.addToSet("reviews", appDoc));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<Review> searchByWord(String word, int limit) {

        Bson myUnwind = unwind("reviews");
        Bson myMatch = match(regex("reviews.review.content", ".*" + Pattern.quote(" " + word + " ") + ".*"));
        Bson myProj = project(fields(include("reviews"), include("_id")));
        Bson myLimit = limit(limit);
        try {
            MongoCollection<Document> userCollection = driver.getCollection("user");
            List<Document> output = userCollection.aggregate(Arrays.asList(myUnwind,
                    myMatch,
                    myProj,
                    myLimit))
                    .into(new ArrayList<>());
            List<Review> reviews = new ArrayList<>();
            System.out.println(output);


            return reviews;
        } catch (Exception e){
            e.printStackTrace();
        }

//        List<Document> results = output.get(0).getList("reviews", Document.class);
//        for (Document result : results) {
//            Review r = new Review();
//            r.fromUserCollDocument(result, (String) result.get("_id"));
//            reviews.add(r);
//        }
        return null;
    }

    public List<Review> searchByDate(Date myDate, DateQuery when, int limit) {

        Bson myUnwind = unwind("reviews");


        Bson myMatch = (when == DateQuery.On)? eq("reviews.review.date", myDate):
                (when == DateQuery.After)? gt("reviews.review.date", myDate):
                        lt("reviews.review.date", myDate);


        Bson myProj = project(fields(include("reviews"), include("_id")));
        Bson myLimit = limit(limit);

        try {
            MongoCollection<Document> userCollection = driver.getCollection("user");
            List<Document> output = userCollection.aggregate(Arrays.asList(myUnwind,
                    myMatch,
                    myProj,
                    myLimit))
                    .into(new ArrayList<>());


            System.out.println(output);
            //returning null (by andrea)
            List<Review> reviews = new ArrayList<>();
            return reviews;

        }catch (Exception e){
            e.printStackTrace();
        }

//        List<Document> results = output.get(0).getList("reviews", Document.class);
//        for (Document result : results) {
//            Review r = new Review();
//            if (user) {
//                r.fromUserCollDocument(result, id);
//            } else {
//                r.fromAppCollDocument(result, id);
//            }
//            reviews.add(r);
//        }
        return null;
    }

    public List<Review> searchById(String id, boolean user, int limit) {
        MongoCollection<Document> coll;
        if(user)
            coll= driver.getCollection("user");
        else
            coll = driver.getCollection("app");

        Bson myUnwind = unwind("reviews");
        Bson myMatch = match(eq("_id", id));
        Bson myProj = project(fields(include("reviews"), include("_id")));
        Bson myLimit = limit(limit);
        try {
            List<Document> output = coll.aggregate(Arrays.asList(myUnwind,
                    myMatch,
                    myProj,
                    myLimit))
                    .into(new ArrayList<>());
            System.out.println(output);
            //returning null (by andrea)
            List<Review> reviews = new ArrayList<>();
            return reviews;

        } catch (Exception e){
            e.printStackTrace();
        }
        //    List<Document> results = output.get(0).getList("reviews", Document.class);

//        for (Document result : results) {
//            Review r = new Review();
//            if (user) {
//                r.fromUserCollDocument(result, id);
//            } else {
//                r.fromAppCollDocument(result, id);
//            }
//            reviews.add(r);
//        }
        return null;
    }

    public void updateReviewScore(Review oldReview, double newScore) {
        try {
            delete(oldReview);
            oldReview.setScore(newScore);
            oldReview.setDateOfScore(new Date());
            postNewReview(oldReview);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateReviewContent(Review oldReview, String newContent) {
        try {
            delete(oldReview);
            oldReview.setContent(newContent);
            oldReview.setDateOfReview(new Date());
            postNewReview(oldReview);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void delete(Review r) {
        try {
            MongoCollection<Document> userCollection = driver.getCollection("user");
            MongoCollection<Document> appCollection = driver.getCollection("app");
            Bson query = new Document().append("_id", r.getUserId());
            Bson fields = new Document().append("reviews", r.toUserCollDocument());

            Bson update = new Document("$pull", fields);
            userCollection.updateOne(query, update);

            query = new Document().append("_id", r.getAppId());
            fields = new Document().append("reviews", r.toAppCollDocument());

            update = new Document("$pull", fields);
            appCollection.updateOne(query, update);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
