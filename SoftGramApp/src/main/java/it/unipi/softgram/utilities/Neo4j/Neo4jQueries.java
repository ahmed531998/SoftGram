package it.unipi.softgram.utilities.Neo4j;

import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import it.unipi.softgram.entities.Role;

import static org.neo4j.driver.Values.ofLocalDate;
import static org.neo4j.driver.Values.parameters;

public class Neo4jQueries {
    public final Neo4jDriver neo4jDriver;

    public Neo4jQueries(){
        neo4jDriver = new Neo4jDriver();
    }


    public void addUser( final String username, Role.RoleValue roleEnum){
        String roleString = Role.getRoleString(roleEnum);
        final String role = roleString.replaceAll("\\s","");
        try ( Session session = neo4jDriver.getSession() ) {
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
        try ( Session session = neo4jDriver.getSession() ) {
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
        try ( Session session = neo4jDriver.getSession() ) {
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
        try ( Session session = neo4jDriver.getSession() ) {
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
        try ( Session session = neo4jDriver.getSession() ) {
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
        try ( Session session = neo4jDriver.getSession() ) {
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
        try (Session session = neo4jDriver.getSession()){
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
        try (Session session = neo4jDriver.getSession()){
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


    public static void main(String[] args){
        Neo4jQueries queries = new Neo4jQueries();
        queries.removeUser("andrea gerratana");
        queries.neo4jDriver.close();
    }
}
