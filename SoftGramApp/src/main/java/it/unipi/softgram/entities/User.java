package it.unipi.softgram.entities;

import it.unipi.softgram.utilities.MongoDriver;
import java.util.*;
import java.util.function.Consumer;

import org.bson.Document;
import com.mongodb.client.MongoCollection;

import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

public class User {
    private String username;
    private Date date_of_birth;
    private String country;
    private String password;

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUsername() {
        return username;
    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public String getCountry() {
        return country;
    }

    private static Consumer<Document> printFormattedDocuments() {
        return doc -> System.out.println(doc.toJson(JsonWriterSettings.builder().indent(true).build()));
    }

    public void saveNewInformationsForCurrentUser(){
        MongoDriver driver = new MongoDriver();
        MongoCollection<Document> userColl = driver.getCollection("user");
        combine(set("date of birth",this.date_of_birth),set( "country",this.country));
        userColl.updateOne(eq("_id",this.username),
                combine(set("date of birth",this.date_of_birth),set( "country",this.country)));
    }

    public static void searchUser(String username){
        MongoDriver driver = new MongoDriver();
        List<Document> matchingUsers = new ArrayList<>();
        try {
            driver.connectMongo();
            MongoCollection<Document> userColl = driver.getCollection("user");
            Bson match = match(eq("_id", username));
            Bson project = project(fields(exclude("reviews"),computed("username","$_id")));
            Bson limit = limit(10);
            userColl.aggregate(Arrays.asList(match,project,limit)).forEach(printFormattedDocuments());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        User.searchUser("a");

    }
}
