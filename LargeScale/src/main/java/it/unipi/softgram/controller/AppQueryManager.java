package it.unipi.softgram.controller;

import com.mongodb.client.MongoCollection;
import it.unipi.softgram.entities.App;
import it.unipi.softgram.entities.Relation;
import it.unipi.softgram.entities.Review;
import it.unipi.softgram.entities.User;
import it.unipi.softgram.utilities.MongoDriver;
import it.unipi.softgram.utilities.NeoDriver;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.eq;
import static org.neo4j.driver.Values.parameters;

public class AppQueryManager {
    private final MongoCollection<Document> appCollection;
    private final NeoDriver neo;
    private int queryLimit = 20;
    private int superNodeThreshold = 1000;

    public AppQueryManager(MongoDriver driver, NeoDriver neo, String userCollectionName, String appCollectionName){
        appCollection = driver.getDBCollection(appCollectionName);
        this.neo = neo;
    }

    public void setQueryLimit(int queryLimit) {
        this.queryLimit = queryLimit;
    }

    public int getQueryLimit() {
        return queryLimit;
    }

    public void setSuperNodeThreshold(int superNodeThreshold) {
        this.superNodeThreshold = superNodeThreshold;
    }

    public int getSuperNodeThreshold() {
        return superNodeThreshold;
    }

    //implement Mongo app queries here


    //Graph App Queries

    public void addAppToGraph(App a, User u){
        try (Session session = neo.getSession()){
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MERGE (a:App {id: $id, app_name: $name, category: $category}) ",
                        parameters( "id", a.getId(),
                                "name", a.getName(),
                                "category", a.getCategory()));
                return null;
            });
            if (u != null){
                followOrDevelopApp(u, a, Relation.RelationType.DEVELOP);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void addAppToMongo(App a){
        try{
            Document appDoc = a.toAppDocument();
            appCollection.insertOne(appDoc);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    

    public void removeAppFromGraph(App a){
        try ( Session session = neo.getSession() ) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MATCH (a:App {id: $id}) " +
                                "DETACH DELETE a",
                        parameters( "id", a.getId()) );
                return null;
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void removeAppFromMongo(App a){
        try {
            Document appDoc = a.toAppDocument();
            appCollection.deleteOne(appDoc);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
 
    
    public void followOrDevelopApp(User u, App a, Relation.RelationType type){
        try (Session session = neo.getSession() ) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss a");
            String currentDate = sdf.format(new Date());
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MATCH (u:User {username: $username}), " +
                                "(a:App {id: $id}) " +
                                "MERGE (u)-[f:"+Relation.getRelationString(type)+"]->(a) " +
                                "SET f.date = $currentDate ",
                        parameters( "username", u.getUsername(),
                                "id",a.getId(),
                                "currentDate", currentDate) );
                return null;
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void unfollowApp(User u, App a){
        try (Session session = neo.getSession() ) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MATCH ((u:User {username: $username})-[f:FOLLOW]->" +
                                "(a:App {id: $id})) " +
                                "DELETE f ",
                        parameters( "username", u.getUsername(),
                                "id",a.getId()));
                return null;
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<App> browseFollowedApps(User u){
        try (Session session = neo.getSession()){
            List<App> myApps = session.readTransaction((TransactionWork<List<App>>) tx ->{
                Result result = tx.run("MATCH (u:User)-[:FOLLOW]->(a:App) WHERE u.username = $username " +
                                "RETURN a ",
                        parameters("username",u.getUsername()));
                ArrayList<App> followedApps = new ArrayList<>();
                while (result.hasNext()){
                    Record r = result.next();
                    App a = new App();
                    a.setId(r.get("id").asString());
                    a.setName(r.get("name").asString());
                    a.setCategory(r.get("category").asString());
                    followedApps.add(a);
                }
                return followedApps;
            });
            return myApps;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<App> browseDevelopedApps(User u){
        try (Session session = neo.getSession()){
            List<App> myApps = session.readTransaction((TransactionWork<List<App>>) tx ->{
                Result result = tx.run("MATCH (u:User)-[:DEVELOP]->(a:App) WHERE u.username = $username " +
                                "RETURN a ",
                        parameters("username",u.getUsername()));
                ArrayList<App> followedApps = new ArrayList<>();
                while (result.hasNext()){
                    Record r = result.next();
                    App a = new App();
                    a.setId(r.get("id").asString());
                    a.setName(r.get("name").asString());
                    a.setCategory(r.get("category").asString());
                    followedApps.add(a);
                }
                return followedApps;
            });
            return myApps;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<App> browseAppsOfFollowers(User u){
        try (Session session = neo.getSession()){
            List<App> myApps = session.readTransaction((TransactionWork<List<App>>) tx ->{
                Result result = tx.run("MATCH (u:User)-[:FOLLOW]->(:User)-[*]->(a:App) WHERE u.username = $username " +
                                "RETURN a \n " + "LIMIT $queryLimit ",
                        parameters("username",u.getUsername(), "queryLimit", this.queryLimit));
                ArrayList<App> suggestedApps = new ArrayList<>();
                while (result.hasNext()){
                    Record r = result.next();
                    App a = new App();
                    a.setId(r.get("id").asString());
                    a.setName(r.get("name").asString());
                    a.setCategory(r.get("category").asString());
                    suggestedApps.add(a);
                }
                return suggestedApps;
            });
            return myApps;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<App> browseFavoriteCategory(User u){
        try (Session session = neo.getSession()){
            List<App> myApps = session.readTransaction((TransactionWork<List<App>>) tx ->{
                Result result = tx.run("MATCH (u:User)-[:FOLLOW]->(a:App) WHERE u.username = $username " +
                                "With a.category As cat " +
                                "MATCH (a2:app) WHERE a2.category = cat "+
                                "Return a2 \n " + "LIMIT $queryLimit ",
                        parameters("username",u.getUsername(), "queryLimit", this.queryLimit));
                ArrayList<App> suggestedApps = new ArrayList<>();
                while (result.hasNext()){
                    Record r = result.next();
                    App a = new App();
                    a.setId(r.get("id").asString());
                    a.setName(r.get("name").asString());
                    a.setCategory(r.get("category").asString());
                    suggestedApps.add(a);
                }
                return suggestedApps;
            });
            return myApps;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<App> browseCommonApps(){
        try (Session session = neo.getSession()){
            List<App> myApps = session.readTransaction((TransactionWork<List<App>>) tx ->{
                Result result = tx.run("MATCH (a:App) " +
                                "WHERE size(()-[:FOLLOW]->(a)) > $threshold " +
                                "Return a \n " + "LIMIT $queryLimit ",
                        parameters("threshold", this.superNodeThreshold,
                                                 "queryLimit", this.queryLimit));
                ArrayList<App> suggestedApps = new ArrayList<>();
                while (result.hasNext()){
                    Record r = result.next();
                    App a = new App();
                    a.setId(r.get("id").asString());
                    a.setName(r.get("name").asString());
                    a.setCategory(r.get("category").asString());
                    suggestedApps.add(a);
                }
                return suggestedApps;
            });
            return myApps;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }






}
