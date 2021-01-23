package it.unipi.softgram.controller.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.softgram.entities.Review;
import it.unipi.softgram.utilities.drivers.MongoDriver;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Updates.set;

//test this

public class ReviewMongoManager {
    private MongoDriver driver;

    public enum DateQuery{
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
        Document reviewDoc = review.toReviewDocument();
        try {
            MongoCollection<Document> reviewCollection = driver.getCollection("review");
            reviewCollection.insertOne(reviewDoc);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<Review> searchByWordInUserReviews(String word, String username, int skip){
        Bson entityFilter = eq("username",username);
        Pattern pattern = Pattern.compile("^" + word + ".*$");
        Bson wordFilter = Filters.regex("content",pattern);
        return searchBy(wordFilter, entityFilter, skip);
    }

    public List<Review> searchByWordInAppReviews(String word, String appId, int skip){
        Bson entityFilter = eq("appId", appId);
        Pattern pattern = Pattern.compile("^" + word + ".*$");
        Bson wordFilter = Filters.regex("content",pattern);
        return searchBy(wordFilter, entityFilter, skip);
    }

    public List<Review> searchByDateInUserReviews(Date myDate, DateQuery when, String username, int skip){
        Bson dateFilter = (when == DateQuery.On)? eq("date", myDate):
                (when == DateQuery.After)? gt("date", myDate):
                        lt("date", myDate);

        Bson entityFilter = eq("username",username);

        return searchBy(dateFilter, entityFilter, skip);
    }

    public List<Review> searchByDateInAppReviews(Date myDate, DateQuery when, String appId, int skip){
        Bson dateFilter = (when == DateQuery.On)? eq("date", myDate):
                (when == DateQuery.After)? gt("date", myDate):
                        lt("date", myDate);

        Bson entityFilter = eq("appId",appId);

        return searchBy(dateFilter, entityFilter, skip);
    }

    public List<Review> searchBy(Bson filter1,  Bson entityFilter, int skip) {
        Bson sort = sort(descending("date"));
        try {
            MongoCollection<Document> reviewCollection = driver.getCollection("review");
            List<Document> output = reviewCollection.find(and(filter1,entityFilter))
                    .sort(sort).skip(skip).limit(50)
                    .into(new ArrayList<>());
            List<Review> reviews = new ArrayList<>();
            Review review = new Review();
            for(Document reviewDoc : output)
                reviews.add(review.fromReviewDocument(reviewDoc));
            System.out.println(output);
            return reviews;
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public List<Review> showReviewsOfUser(String username, int skip){
        Bson entityFilter = eq("username", username);
        return showReviews(entityFilter, skip);
    }

    public List<Review> showReviewsOfApp(String appId, int skip){
        Bson entityFilter = eq("appId", appId);
        return showReviews(entityFilter, skip);
    }

    public List<Review> showReviews(Bson entityFilter, int skip) {
        Bson sort = sort(descending("_id"));
        try {
            MongoCollection<Document> reviewCollection = driver.getCollection("review");
            List<Document> output = reviewCollection.find(and(entityFilter))
                    .skip(skip).limit(10)
                    .into(new ArrayList<>());
            List<Review> reviews = new ArrayList<>();
            Review review = new Review();
            for(Document reviewDoc : output)
                reviews.add(review.fromReviewDocument(reviewDoc));
            System.out.println(output);
            return reviews;
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public Review showScore(String username, String appId){
        try {
            MongoCollection<Document> reviewCollection = driver.getCollection("review");
            Document reviewDoc = reviewCollection.find(and(
                    eq("username",username)
                    ,eq("appId",appId),exists("score"))).first();
            Review reviewScore = new Review();
            if(reviewDoc == null)
                return null;
            reviewScore.fromReviewDocument(reviewDoc);
            return reviewScore;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void updateReviewScore(Review oldReview, double newScore) {
        try {
            MongoCollection<Document> reviewCollection = driver.getCollection("review");
            UpdateResult result = reviewCollection.updateOne(eq("_id",oldReview.get_id()),
                    set("score",newScore));
            if(result.getModifiedCount()==0)
                System.out.println("Requested review to update not found");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateReviewContent(Review oldReview, String newContent) {
        try {
            MongoCollection<Document> reviewCollection = driver.getCollection("review");
            UpdateResult result = reviewCollection.updateOne(eq("_id",oldReview.get_id()),
                    set("content",newContent));
            if(result.getModifiedCount()==0)
                System.out.println("Requested review to update not found");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void delete(Review r) {
        try {
            MongoCollection<Document> reviewCollection = driver.getCollection("review");
            DeleteResult result = reviewCollection.deleteOne(eq("_id",r.get_id()));
            if(result.getDeletedCount()==0){
                System.out.println("Review to delete not found");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //users commented greatest number of apps in Month
    public List<Document> Top10UsersReviewersInMonth(Date monthYear){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String monthYearString = sdf.format(monthYear);
        Pattern pattern = Pattern.compile("^" + monthYearString + ".*$");
        Bson myMatch = eq(regex("date",pattern));
        Bson myGroup1 = new Document("$group",
                new Document("_id", new Document("username", "$username")
                        .append("appId", "$appId")));
        Bson myGroup2 = group("$username", Accumulators.sum("reviewedAppCount",1));
        Bson mySort = sort(descending("reviewedAppCount"));
        Bson myLimit = limit(10);
        Bson myProjection = project(fields(excludeId(),include("username","reviewedAppCount")));
        try{
            MongoCollection<Document> reviewColl = driver.getCollection("review");
            return reviewColl.aggregate(
                    Arrays.asList(myMatch, myGroup1, myGroup2,mySort,myLimit,myProjection))
                    .into(new ArrayList<>());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
