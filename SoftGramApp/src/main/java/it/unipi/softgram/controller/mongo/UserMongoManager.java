package it.unipi.softgram.controller.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.softgram.entities.User;
import it.unipi.softgram.utilities.drivers.MongoDriver;
import it.unipi.softgram.utilities.enumerators.Role;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.exclude;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Updates.set;



public class UserMongoManager {
    private MongoDriver driver;

    public UserMongoManager(){
        try{
            driver = new MongoDriver();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    private static Consumer<Document> convertDocumentToUser() {
        User user = new User();
        return doc -> user.toUserDocument();
    }

    public void saveNewBirthday(User user){
        try {
            MongoCollection<Document> userColl = driver.getCollection("user");
            UpdateResult result = userColl.updateOne(eq("_id",user.getUsername()),
                    set("birthday",user.getBirthday()));
            if(result.getModifiedCount()==0)
                System.out.println("Requested user to update not found");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void saveNewCountry(User user){
        try {
            MongoCollection<Document> userColl = driver.getCollection("user");
            UpdateResult result = userColl.updateOne(eq("_id",user.getUsername()),
                    set("Country",user.getCountry()));
            if(result.getModifiedCount()==0)
                System.out.println("Requested user to update not found");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void saveNewEmail(User user){
        try {
            MongoCollection<Document> userColl = driver.getCollection("user");
            UpdateResult result = userColl.updateOne(eq("_id",user.getUsername()),
                    set("email",user.getEmail()));
            if(result.getModifiedCount()==0)
                System.out.println("Requested user to update not found");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void saveNewPassword(User user){
        try {
            MongoCollection<Document> userColl = driver.getCollection("user");
            UpdateResult result = userColl.updateOne(eq("_id",user.getUsername()),
                    set("password",user.getPassword()));
            if(result.getModifiedCount()==0)
                System.out.println("Requested user to update not found");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<User> searchUser(String username){
         return this.searchUser(username, 10);
    }

    private List<User> convertFromDocument(List<Document> usersDocument){
        List<User> users = new ArrayList<>();
        for(Document doc : usersDocument){
            User user = new User();
            users.add(user.fromUserDocument(doc));
        }
        return users;
    }

    public List<User> searchUser(String username, int limitNumber){
        try {
            MongoCollection<Document> userColl = driver.getCollection("user");
            Pattern pattern = Pattern.compile("^" + username + ".*$");
            Bson match = match(Filters.regex("_id", pattern));
            Bson project = project(fields(exclude("reviews","password")));
            Bson limit = limit(limitNumber);
            List<Document> output = userColl.aggregate(Arrays.asList(match,project,limit)).into(new ArrayList<>());
            return convertFromDocument(output);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public User showProfile(String username){
        return searchUser(username,1).remove(0);

    }

    public void becomeDeveloper(String username){
        try {
            MongoCollection<Document> userColl = driver.getCollection("user");
            UpdateResult result = userColl.updateOne(eq("_id",username),
                    set("role","Developer"));
            if(result.getModifiedCount()==0)
                System.out.println("Requested user to update not found");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

//developer
    public void becomeNormalUser(String username){
        try {
            MongoCollection<Document> userColl = driver.getCollection("user");
            UpdateResult result = userColl.updateOne(eq("_id", username),
                    set("role","Normal User"));
            if(result.getModifiedCount()==0){
                System.out.println("Requested user to update not found");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void saveNewWebsite(User user){
        try {
            MongoCollection<Document> userColl = driver.getCollection("user");
            UpdateResult result = userColl.updateOne(eq("_id",user.getUsername()),
                    set("website",user.getWebsite()));
            if(result.getModifiedCount()==0){
                System.out.println("Requested user to update not found");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

//admin

    public void changeUserRole(String username, Role.RoleValue role){
        try {
            String roleString = Role.getRoleString(role);
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

    public void addUser(User user){
        try {
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

    public void removeUser(String username){
        try {
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



