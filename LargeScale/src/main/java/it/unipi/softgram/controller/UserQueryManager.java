package it.unipi.softgram.controller;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.softgram.entities.Review;
import it.unipi.softgram.entities.Role;
import it.unipi.softgram.entities.User;
import it.unipi.softgram.utilities.MongoDriver;
import it.unipi.softgram.utilities.NeoDriver;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Updates.set;
import static org.neo4j.driver.Values.parameters;

public class UserQueryManager {
    private final MongoCollection<Document> userCollection;
    public final NeoDriver neo;

    public UserQueryManager(MongoDriver driver, NeoDriver neo, String userCollectionName, String appCollectionName){
        userCollection = driver.getDBCollection(userCollectionName);
        this.neo = neo;
    }

    private static Consumer<? super Document> printFormattedDocuments() {
        return doc -> System.out.println(doc.toJson(JsonWriterSettings.builder().indent(true).build())
                .replace("_id","username"));
    }

    private void saveNewBirthdayForCurrentUser(User u){
        try {
            UpdateResult result = userCollection.updateOne(eq("_id",u.getUsername()),
                    set("birthday",u.getBirthday()));
            if(result.getModifiedCount()==0)
                System.out.println("Requested user to update not found");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void saveNewCountryForCurrentUser(User u){
        try {
            UpdateResult result = userCollection.updateOne(eq("_id",u.getUsername()),
                    set("Country",u.getCountry()));
            if(result.getModifiedCount()==0)
                System.out.println("Requested user to update not found");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void saveNewEmailForCurrentUser(User u){
        try {
            UpdateResult result = userCollection.updateOne(eq("_id",u.getUsername()),
                    set("email",u.getEmail()));
            if(result.getModifiedCount()==0)
                System.out.println("Requested user to update not found");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void saveNewPasswordForCurrentUser(User u){
        try {
            UpdateResult result = userCollection.updateOne(eq("_id",u.getUsername()),
                    set("password",u.getPassword()));
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
            Pattern pattern = Pattern.compile("^" + username + ".*$");
            Bson match = match(Filters.regex("_id", pattern));
            Bson project = project(fields(exclude("reviews","password")));
            Bson limit = limit(limitNumber);
            userCollection.aggregate(Arrays.asList(match,project,limit)).forEach(printFormattedDocuments());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showOwnProfile(User u){
        searchUser(u.getUsername(),1);
    }

    public void becomeDeveloper(User u){
        try {
            UpdateResult result = userCollection.updateOne(eq("_id",u.getUsername()),
                    set("role","Developer"));
            if(result.getModifiedCount()==0)
                System.out.println("Requested user to update not found");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void becomeNormalUser(User u){
        try {
            UpdateResult result = userCollection.updateOne(eq("_id", u.getUsername()),
                    set("role","Normal User"));
            if(result.getModifiedCount()==0){
                System.out.println("Requested user to update not found");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void saveNewWebsiteForCurrentUser(User u){
        try {
            UpdateResult result = userCollection.updateOne(eq("_id",u.getUsername()),
                    set("website",u.getWebsite()));
            if(result.getModifiedCount()==0){
                System.out.println("Requested user to update not found");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    private void changeUserRole(String username, Role.RoleValue role){
        try {
            String roleString = Role.getRoleString(role);
            UpdateResult result = userCollection.updateOne(eq("_id",username),
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
            Document userDoc = new Document("_id", user.getUsername());
            if(user.getBirthday()!=null)
                userDoc.append("birthday", user.getBirthday());
            if(user.getEmail()!=null)
                userDoc.append("email", user.getEmail());
            userDoc.append("role", "Normal User");
            if(user.getCountry()!=null)
                userDoc.append("Country", user.getCountry());
            userCollection.insertOne(userDoc);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void removeUserFromDB(String username){
        try {
            DeleteResult result = userCollection.deleteOne(eq("_id",username));
            if(result.getDeletedCount()==0){
                System.out.println("User to delete not found");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    //graph user queries

    public void addUser( final String username, Role.RoleValue roleEnum){
        String roleString = Role.getRoleString(roleEnum);
        final String role = roleString.replaceAll("\\s","");
        try ( Session session = neo.getSession() ) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MERGE (u:User {username: $username})" +
                                "SET u:" + role,
                        parameters( "username", username) );
                return null;
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void removeUser(String username){
        try ( Session session = neo.getSession() ) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MATCH (u:User {username: $username})" +
                                "DETACH DELETE u",
                        parameters( "username", username) );
                return null;
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void becomeDeveloper(String username){
        try ( Session session = neo.getSession() ) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MATCH (u:User {username: $username})" +
                                "REMOVE u:NormalUser" +
                                "SET u:Developer",
                        parameters( "username", username) );
                return null;
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void becomeNormalUser(String username){
        try ( Session session = neo.getSession() ) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MATCH (u:User {username: $username})" +
                                "REMOVE u:Developer" +
                                "SET u:NormalUser",
                        parameters( "username", username) );
                return null;
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addFollow(String followerUsername, String followedUsername){
        try ( Session session = neo.getSession() ) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss a");
            String currentDate = sdf.format(new Date());
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MATCH (u:User {username: $followerUsername}),(u2:User {username: $followedUsername})" +
                                "MERGE (u)-[f:FOLLOW]->(u2)" +
                                "SET f.date = $currentDate",
                        parameters( "followerUsername", followerUsername,
                                "followedUsername",followedUsername,
                                "currentDate", currentDate) );
                return null;
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void removeFollow(String followerUsername, String followedUsername){
        try ( Session session = neo.getSession() ) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MATCH ((u:User {username: $followerUsername})-[f:FOLLOW]->(u2:User {username: $followedUsername}))" +
                                "DELETE f",
                        parameters( "followerUsername", followerUsername,
                                "followedUsername",followedUsername ) );
                return null;
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void browseFollowedUsers(String username){
        try (Session session = neo.getSession()){
            List<String> followedUsernames = session.readTransaction((TransactionWork<List<String>>) tx ->{
                Result result = tx.run("MATCH (u:User)-[:FOLLOW]->(f:User) WHERE u.username = $name " +
                                "RETURN f.username",
                        parameters("name",username));
                ArrayList<String> followedUsers = new ArrayList<>();
                while (result.hasNext()){
                    Record r = result.next();
                    followedUsers.add(r.get("username").asString());
                }
                return followedUsers;
            });
            System.out.println(followedUsernames);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void browseFollowers(String username){
        try (Session session = neo.getSession()){
            List<String> followersUsernames = session.readTransaction((TransactionWork<List<String>>) tx ->{
                Result result = tx.run("MATCH (u:User)-[:FOLLOW]->(f:User) WHERE f.username = $name " +
                                "RETURN u.username",
                        parameters("name",username));
                ArrayList<String> followers = new ArrayList<>();
                while (result.hasNext()){
                    Record r = result.next();
                    followers.add(r.get("username").asString());
                }
                return followers;
            });
            System.out.println(followersUsernames);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



}
