package it.unipi.softgram.entities;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import it.unipi.softgram.utilities.MongoDriver;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.exclude;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Updates.set;

public class User {
    private String username;
    private Date birthday;
    private String email;
    private String website;
    private String role;
    private String password;
    private String country;

    List<Review> reviews;

    List<User> myFollowersList;
    List<User> myFollowingList;

    List<App> myApps;

    public User(){}

    public User(String username){
        this.username = username;
    }

    private void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    private void setCountry(String country) {
        this.country = country;
    }

    private void setEmail(String email){
        this.email = email;
    }

    public void setPassword(String password){
        this.password = password;
    }

    private void setWebsite(String website){
        this.website = website;
    }

    public String getWebsite(){
        return website;
    }

    public String getUsername() {
        return username;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getCountry() {
        return country;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Document toUserDocument(){
        List<Document> reviewDocList = new ArrayList<>();
        for (Review review: this.reviews){
            Document reviewDoc = new Document("review", new Document("content", review.getContent())
                    .append("date", review.getDateOfReview())
                    .append("appId", review.getAppId()))
                    .append("score", review.getScore())
                    .append("scoreDate", review.getDateOfScore());
            reviewDocList.add(reviewDoc);
        }

        Document userDoc = new Document("_id", this.username)
                .append("birthday", this.birthday)
                .append("email", this.email)
                .append("website", this.website)
                .append("role", this.role)
                .append("password", this.password)
                .append("country", this.country)
                .append("reviews", reviewDocList);
        return userDoc;
    }

    public User fromUserDocument(Document r){
        this.username = (String) r.get("_id");
        this.birthday = (Date) r.get("birthday");
        this.email = (String) r.get("email");
        this.website = (String) r.get("website");
        this.role = (String) r.get("role");
        this.password = (String) r.get("password");
        this.country = (String) r.get("country");
        List<Document> reviewsDocList = (List<Document>) r.get("reviews");
        List<Review> reviews = new ArrayList<>();
        for (Document review: reviewsDocList){
            Review x = new Review();
            reviews.add(x.fromUserCollDocument(review, this.username));
        }
        this.reviews = reviews;
        return this;
    }


}

