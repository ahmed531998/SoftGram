package it.unipi.softgram.entities;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.softgram.utilities.MongoDriver;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class Admin extends Developer {

    public Admin(String username) {
        super(username);
    }

    private void changeUserRole(String username, Role.RoleValue role){
        try {
            String roleString = Role.getRoleString(role);
            MongoDriver driver = new MongoDriver();
            MongoCollection<Document> userColl = driver.getCollection("user");
            UpdateResult result = userColl.updateOne(eq("_id",username),
                    set("role",roleString));
            if(result.getModifiedCount()==0){
                System.out.println("Requested user to update not found");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addUserToDB(User user){
        try {
            MongoDriver driver = new MongoDriver();
            Document userDoc = new Document("_id", user.getUsername());
            if(user.getBirthday()!=null)
                userDoc.append("birthday", user.getBirthday());
            if(user.getEmail()!=null)
                userDoc.append("email", user.getEmail());
            userDoc.append("role", "Normal User");
            if(user.getCountry()!=null)
                userDoc.append("Country", user.getCountry());
            MongoCollection<Document> userColl = driver.getCollection("user");
            userColl.insertOne(userDoc);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void removeUserFromDB(String username){
        try {
            MongoDriver driver = new MongoDriver();
            MongoCollection<Document> userColl = driver.getCollection("user");
            DeleteResult result = userColl.deleteOne(eq("_id",username));
            if(result.getDeletedCount()==0){
                System.out.println("User to delete not found");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
