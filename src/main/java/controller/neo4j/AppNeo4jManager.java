package controller.neo4j;

import entities.App;
import entities.User;
import enumerators.Relation;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Record;
import org.neo4j.driver.TransactionWork;
import utilities.Neo4jDriver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class AppNeo4jManager {

    private final Neo4jDriver neo;
    private int queryLimit = 20;
    private int superNodeThreshold = 1000;

    public AppNeo4jManager(){
        neo = new Neo4jDriver();
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

    //follow unfollow suggested apps suggested user (List)
    public void addApp(App a, User u) {
        try (Session session = neo.getSession()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MERGE (a:App {id: $id, app_name: $name, category: $category}) ",
                        parameters("id", a.getId(),
                                "name", a.getName(),
                                "category", a.getCategory()));
                return null;
            });
            if (u != null) {
                followOrDevelopApp(u, a, Relation.RelationType.DEVELOP);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeApp(App a){
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


    private List<App> getApps(Result result) {
        ArrayList<App> Apps = new ArrayList<>();
        while (result.hasNext()){
            Record r = result.next();
            App a = new App();
            a.setId(r.get("id").asString());
            a.setName(r.get("name").asString());
            a.setCategory(r.get("category").asString());
            Apps.add(a);
        }
        return Apps;
    }

    public List<App> browseFollowedApps(User u){
        try (Session session = neo.getSession()){
            return session.readTransaction(tx ->{
                Result result = tx.run("MATCH (u:User)-[:FOLLOW]->(a:App) WHERE u.username = $username " +
                                "RETURN a ",
                        parameters("username",u.getUsername()));
                return getApps(result);
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public List<App> browseDevelopedApps(User u){
        try (Session session = neo.getSession()){
            return session.readTransaction(tx ->{
                Result result = tx.run("MATCH (u:User)-[:DEVELOP]->(a:App) WHERE u.username = $username " +
                                "RETURN a ",
                        parameters("username",u.getUsername()));
                return getApps(result);
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public List<App> browseAppsOfFollowers(User u){
        try (Session session = neo.getSession()){
            return session.readTransaction(tx ->{
                Result result = tx.run("MATCH (u:User)-[:FOLLOW]->(:User)-[*]->(a:App) WHERE u.username = $username " +
                                "RETURN a \n " + "LIMIT $queryLimit ",
                        parameters("username",u.getUsername(), "queryLimit", this.queryLimit));
                return getApps(result);
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<App> browseFavoriteCategory(User u){
        ArrayList<App> Apps = new ArrayList<>();
        try (Session session = neo.getSession()){
            return session.readTransaction(tx ->{
                Result result = tx.run("MATCH (u:User)-[:FOLLOW]->(a:App) WHERE u.username = $username " +
                                "With a.category As cat " +
                                "MATCH (a2:app) WHERE a2.category = cat "+
                                "Return a2 \n " + "LIMIT $queryLimit ",
                        parameters("username",u.getUsername(), "queryLimit", this.queryLimit));

                return getApps(result);

            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<App> browseCommonApps(){
        try (Session session = neo.getSession()){
            return session.readTransaction(tx ->{
                Result result = tx.run("MATCH (a:App) " +
                                "WHERE size(()-[:FOLLOW]->(a)) > $threshold " +
                                "Return a \n " + "LIMIT $queryLimit ",
                        parameters("threshold", this.superNodeThreshold,
                                "queryLimit", this.queryLimit));
                return getApps(result);
            });
        }
        catch (Exception e){
            e.printStackTrace();

        }
        return null;
    }

    //added new
    public List<String> browseSuggestedapps(String username, int limit){
        try (Session session = neo.getSession()){
            return session.readTransaction((TransactionWork<List<String>>) tx ->{
                Result result = tx.run("MATCH (u1:App {name: $nme})-[f:FOLLOW]->(u2:pp)<-[f:FOLLOW]-(u3:App)" +
                                "(u1)-[f:FOLLOW]->(a:User)<-[f:FOLLOW]-(u3)" +
                                "WHERE NOT (u1)-[:FOLLOW]->(u3)" +
                                "RETURN DISTINCT u3.username AND u3<>u1" +
                                "LIMIT $limit ",
                        parameters("name", username, "limit", limit));
                ArrayList<String> apps = new ArrayList<>();
                while (result.hasNext()){
                    Record r = result.next();
                    apps.add(r.get("u2.name").asString());
                }
                return apps;
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
