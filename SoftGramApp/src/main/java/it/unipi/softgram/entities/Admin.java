package it.unipi.softgram.entities;

import com.mongodb.client.MongoCollection;
import it.unipi.softgram.utilities.MongoDriver;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

enum Role{NORMAL_USER, DEVELOPER, ADMIN}

public class Admin extends Developer {

    public Admin(String username) {
        super(username);
    }

    private void changeUserRole(String username, Role role){
        String roleString = null;
        switch (role){
            case ADMIN:
                roleString = "Admin";
                break;
            case DEVELOPER:
                roleString = "Developer";
                break;
            case NORMAL_USER:
                roleString = "Normal User";
                break;
        }
        if(roleString == null){
            throw new RuntimeException("Role not set");
        }
        try {
            MongoDriver driver = new MongoDriver();
            MongoCollection<Document> userColl = driver.getCollection("user");
            userColl.updateOne(eq("_id",username),
                    set("role",roleString));
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
            userColl.deleteOne(eq("_id",username));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
