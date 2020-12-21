package it.unipi.softgram.entities;

import java.util.Date;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import it.unipi.softgram.driver.MongoDriver;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Accumulators.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;
public class User {
    private String username;
    private Date date_of_birth;
    private String country;
    private String password;

    public void setUsername(String username) {
        this.username = username;
    }

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

    public void saveNewInformationsForCurrentUser(){
        MongoDriver driver = new MongoDriver();
        MongoCollection<Document> userColl = driver.getCollection("user");
        combine(set("date of birth",this.date_of_birth),set( "country",this.country));
        userColl.updateOne(eq("_id",this.username),
                combine(set("date of birth",this.date_of_birth),set( "country",this.country)));
    }
}
