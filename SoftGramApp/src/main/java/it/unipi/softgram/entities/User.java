package it.unipi.softgram.entities;

import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import it.unipi.softgram.utilities.MongoDriver;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import org.bson.Document;
import com.mongodb.client.MongoCollection;

import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

public class User {
    private final String username;
    private String birthday;
    private String country;
    private String password;
    private String email;
    private String role;

    public User(String username) {
        this.username = username;
    }

    private void setBirthday(String birthday) {
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

    public String getUsername() {
        return username;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getCountry() {
        return country;
    }

    public String getEmail(){
        return email;
    }

    private static Consumer<Document> printFormattedDocuments() {
        return doc -> System.out.println(doc.toJson(JsonWriterSettings.builder().indent(true).build())
                .replace("_id","username"));
    }

    private void saveNewBirthdayForCurrentUser(){
        try {
            MongoDriver driver = new MongoDriver();
            MongoCollection<Document> userColl = driver.getCollection("user");
            UpdateResult result = userColl.updateOne(eq("_id",this.username),
                    set("birthday",this.birthday));
            if(result.getModifiedCount()==0)
                System.out.println("Requested user to update not found");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void saveNewCountryForCurrentUser(){
        try {
            MongoDriver driver = new MongoDriver();
            MongoCollection<Document> userColl = driver.getCollection("user");
            UpdateResult result = userColl.updateOne(eq("_id",this.username),
                    set("Country",this.country));
            if(result.getModifiedCount()==0)
                System.out.println("Requested user to update not found");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void saveNewEmailForCurrentUser(){
        try {
            MongoDriver driver = new MongoDriver();
            MongoCollection<Document> userColl = driver.getCollection("user");
            UpdateResult result = userColl.updateOne(eq("_id",this.username),
                    set("email",this.email));
            if(result.getModifiedCount()==0)
                System.out.println("Requested user to update not found");
            }
            catch (Exception e){
                e.printStackTrace();
            }
    }

    private void saveNewPasswordForCurrentUser(){
        try {
            MongoDriver driver = new MongoDriver();
            MongoCollection<Document> userColl = driver.getCollection("user");
            UpdateResult result = userColl.updateOne(eq("_id",this.username),
                    set("password",this.password));
            if(result.getModifiedCount()==0)
                System.out.println("Requested user to update not found");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void searchUser(String username){
        this.searchUser(username, 10);
    }

    private void searchUser(String username, int limitNumber){
        try {
            MongoDriver driver = new MongoDriver();
            MongoCollection<Document> userColl = driver.getCollection("user");
            Pattern pattern = Pattern.compile("^" + username + ".*$");
            Bson match = match(Filters.regex("_id", pattern));
            Bson project = project(fields(exclude("reviews","password")));
            Bson limit = limit(limitNumber);
            userColl.aggregate(Arrays.asList(match,project,limit)).forEach(printFormattedDocuments());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showOwnProfile(){
        searchUser(this.username,1);
    }

    public void becomeDeveloper(){
        try {
            MongoDriver driver = new MongoDriver();
            MongoCollection<Document> userColl = driver.getCollection("user");
            UpdateResult result = userColl.updateOne(eq("_id",this.username),
                    set("role","Developer"));
            if(result.getModifiedCount()==0)
                System.out.println("Requested user to update not found");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


}
