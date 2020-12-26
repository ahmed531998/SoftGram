package it.unipi.softgram;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import com.mongodb.client.model.Updates;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;


import org.bson.conversions.Bson;


import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Aggregates.*;
import  static com.mongodb.client.model.Projections.*;


public class ReviewQueryManager{
    private final MongoCollection<Document> userCollection;
    private final MongoCollection<Document> appCollection;

    public ReviewQueryManager(MongoDriver driver, String userCollectionName, String appCollectionName){
        userCollection = driver.getDBCollection(userCollectionName);
        appCollection = driver.getDBCollection(appCollectionName);
    }

    public void create(Review review) {
        Document appDoc = getAppDoc(review);
        Document userDoc = getUserDoc(review);
        userCollection.updateOne(eq("_id", review.getUserId()), Updates.addToSet("reviews", userDoc));
        appCollection.updateOne(eq("_id", review.getAppId()), Updates.addToSet("reviews", appDoc));
    }


    public List<Review> read(String id, boolean user) {
        MongoCollection<Document> coll = user? userCollection:appCollection;

        Bson myMatch = match(eq("_id", id));
        Bson myProj = project(fields(include("reviews"), exclude("_id")));
        List<Document> output = coll.aggregate(Arrays.asList(myMatch, myProj)).into(new ArrayList<>());
        List<Document> results = output.get(0).getList("reviews", Document.class);
        List<Review> reviews = new ArrayList<>();

        System.out.println(results);

        for (Document result : results) {
            String appId = user ? (String) result.get("appId") : id;
            String username = user ? id : (String) result.get("username");
            String dateOfScore = (String) result.get("scoreDate");
            Document reviewDoc = (Document) result.get("review");
            String dateOfReview = (String) reviewDoc.get("date");
            String content = (String) reviewDoc.get("content");
            double score = (double) result.get("score");

            Review r = new Review(appId, username, dateOfScore, dateOfReview, content, score);
            reviews.add(r);
        }
        return reviews;
    }

    private Document getAppDoc(Review r){
        Document appDoc = new Document("username", r.getUserId())
                .append("score", r.getScore())
                .append("scoreDate", r.getDateOfScore())
                .append("review", new Document("content", r.getContent())
                        .append("date", r.getDateOfReview()));
        return appDoc;
    }

    private Document getUserDoc(Review r){
        Document userDoc = new Document("appId", r.getAppId())
                .append("score", r.getScore())
                .append("scoreDate", r.getDateOfScore())
                .append("review", new Document("content", r.getContent())
                        .append("date", r.getDateOfReview()));
        return userDoc;
    }



    //for now just the content is modified --> add date after fixing date issue
    //expand -- update only content or only score
    public void update(Review oldReview, Review newReview) {
        Document appDoc = getAppDoc(newReview);
        Document userDoc = getUserDoc(newReview);

        delete(oldReview);
        create(newReview);
    }

    public void delete(Review r) {
        Bson query = new Document().append("_id", r.getUserId());
        Bson fields = new Document().append("reviews",
                      new Document().append("appId", r.getAppId())
                                    .append("score", r.getScore())
                                    .append("scoreDate", r.getDateOfScore())
                                    .append("review",
                                            new Document("content", r.getContent())
                                                 .append("date", r.getDateOfReview())));

        Bson update = new Document("$pull",fields);
        userCollection.updateOne(query, update);

        query = new Document().append("_id", r.getAppId());
        fields = new Document().append("reviews",
                 new Document().append("username", r.getUserId())
                               .append("score", r.getScore())
                               .append("scoreDate", r.getDateOfScore())
                               .append("review",
                                      new Document("content", r.getContent())
                                           .append("date", r.getDateOfReview())));

        update = new Document("$pull",fields);
        appCollection.updateOne(query, update);
    }
}
