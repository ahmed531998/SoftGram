package it.unipi.softgram.entities;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
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
            UpdateResult result = userColl.updateOne(eq("_id", this.getUsername()),
                    set("role","Normal User"));
            if(result.getModifiedCount()==0){
                System.out.println("Requested user to update not found");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void saveNewWebsiteForCurrentUser(){
        try {
            MongoDriver driver = new MongoDriver();
            MongoCollection<Document> userColl = driver.getCollection("user");
            UpdateResult result = userColl.updateOne(eq("_id",this.getUsername()),
                    set("website",this.website));
            if(result.getModifiedCount()==0){
                System.out.println("Requested user to update not found");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
