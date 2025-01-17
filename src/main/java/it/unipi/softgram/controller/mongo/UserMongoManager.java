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
import java.util.List;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;



public class UserMongoManager {
    private final MongoDriver driver;

    public UserMongoManager(){
        driver = new MongoDriver();
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


    public List<User> searchUserByUsername(String username, int skip){
        Pattern pattern = Pattern.compile("^" + username + ".*$");
        Bson query = Filters.regex("_id",pattern);
        return this.searchUserBy(query,skip);
    }

    public List<User> searchUserByEmail(String email, int skip){
        Pattern pattern = Pattern.compile("^" + email + ".*$");
        Bson query = Filters.regex("email",pattern);
        return this.searchUserBy(query,skip);
    }


    public List<User> searchUserBy(Bson query, int skip){
        try{
            MongoCollection<Document> userColl = driver.getCollection("user");
            List<Document> output = userColl.find(query).skip(skip).limit(10)
                   .into(new ArrayList<>());
            return convertFromDocument(output);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



    private List<User> convertFromDocument(List<Document> usersDocument){
        List<User> users = new ArrayList<>();
        for(Document doc : usersDocument){
            User user = new User();
            users.add(user.fromUserDocument(doc));
        }
        return users;
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
            throw new RuntimeException("write operation failed");
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
            throw new RuntimeException("write operation failed");
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
            throw new RuntimeException("write operation failed");
        }
    }

    public void addUser(User user){
        try {
            Document userDoc = user.toUserDocument();
            MongoCollection<Document> userColl = driver.getCollection("user");
            userColl.insertOne(userDoc);
        }
        catch (Exception e){
            throw new RuntimeException("write operation failed");
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
            throw new RuntimeException("write operation failed");
        }
    }

    public void updateUser(User user){
        try {
            MongoDriver driver = new MongoDriver();
            MongoCollection<Document> collection = driver.getCollection("user");
            Document query = new Document();
            query.append("_id", user.getUsername());
            Document setData = new Document();
            setData.append("website", user.getWebsite())
                    .append("role", user.getRole())
                    .append("email", user.getEmail())
            ;
            Document update = new Document();
            update.append("$set", setData);
            //To update single Document
            UpdateResult result= collection.updateOne(query, update);
            if(result.getModifiedCount()==0){
                System.out.println("Requested user to update not found");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}



