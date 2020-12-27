package it.unipi.softgram.entities;

import com.mongodb.client.MongoCollection;
import it.unipi.softgram.utilities.MongoDriver;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class Developer extends User{
    private String website;

    public Developer(String username) {
        super(username);
    }

    private void setWebsite(String website){
        this.website = website;
    }

    public String getWebsite(){
        return website;
    }

    public void becomeNormalUser(){
        try {
            MongoDriver driver = new MongoDriver();
            MongoCollection<Document> userColl = driver.getCollection("user");
            userColl.updateOne(eq("_id", this.getUsername()),
                    set("role","Normal User"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void saveNewWebsiteForCurrentUser(){
        try {
            MongoDriver driver = new MongoDriver();
            MongoCollection<Document> userColl = driver.getCollection("user");
            userColl.updateOne(eq("_id",this.getUsername()),
                    set("website",this.website));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
