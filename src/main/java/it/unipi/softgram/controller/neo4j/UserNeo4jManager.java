package it.unipi.softgram.controller.neo4j;

import it.unipi.softgram.utilities.drivers.Neo4jDriver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.unipi.softgram.utilities.enumerators.Role;

import static org.neo4j.driver.Values.parameters;

//return lists? (by andrea)
public class UserNeo4jManager {
    private final Neo4jDriver neo4jDriver;

    public UserNeo4jManager(){
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

    public void addFollow(String followerUsername, String followedUsername, boolean request){
        String partOfQuery = "";
        if(request)
            partOfQuery = "SET f.accepted = false";
        final String partOfQueryFinal = partOfQuery;
        try ( Session session = neo4jDriver.getSession() ) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = sdf.format(new Date());
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MATCH (u:User {username: $followerUsername}),(u2:User {username: $followedUsername})" +
                                "MERGE (u)-[f:FOLLOW]->(u2)" +
                                partOfQueryFinal +
                                "SET f.date = date($currentDate)",
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

    public void acceptFollow(String followerUsername, String followedUsername){
        try ( Session session = neo4jDriver.getSession() ) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MATCH ((u:User {username: $followerUsername})-[f:FOLLOW]->(u2:User {username: $followedUsername}))" +
                                "REMOVE f.accepted",
                        parameters( "followerUsername", followerUsername,
                                "followedUsername",followedUsername ) );
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
                Result result = tx.run("MATCH (u:User)-[f:FOLLOW]->(f:User) " +
                                            "WHERE u.username = $name AND f.accepted = null " +
                                            "RETURN f.username"+
                                            "ORDER BY f.date DESC",
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

    public void browseFollowers(String username, boolean requests){
        String partOfQuery = "AND f.accepted = null";
        if(requests)
            partOfQuery = "AND f.accepted = false";
        final String partOfQueryFinal = partOfQuery;
        try (Session session = neo4jDriver.getSession()){
            List<String> followersUsernames = session.readTransaction((TransactionWork<List<String>>) tx ->{
                Result result = tx.run("MATCH (u:User)-[f:FOLLOW]->(f:User)" +
                                "WHERE f.username = $name"+ partOfQueryFinal +
                                "RETURN u.username" +
                                "ORDER BY f.date DESC",
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

    public void browseUsersWithMostFollowersInYear(String year){
        browseUsersWithMostFollowersInYear(year, 10);
    }

    public void browseUsersWithMostFollowersInYear(String year, int limit){
        try (Session session = neo4jDriver.getSession()){
            List<String> usernameAndFollowers = session.readTransaction((TransactionWork<List<String>>) tx ->{
                Result result = tx.run("MATCH (u1:User)-[f:FOLLOW]->(u2:User) " +
                                          "WHERE f.date = date({year: $year}) AND f.accepted = null" +
                                          "RETURN u2.username, count(u1) AS followers " +
                                          "ORDER BY followers DESC " +
                                          "LIMIT $limit ",
                        parameters("limit", limit, "currentYear", year));
                ArrayList<String> followers = new ArrayList<>();
                while (result.hasNext()){
                    Record r = result.next();
                    followers.add(r.get("u2.username").asString() + " " + r.get("followers").asInt() + " " + "followers");
                }
                return followers;
            });
            System.out.println(usernameAndFollowers);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void browseSuggestedUsers(String username, int limit){
        try (Session session = neo4jDriver.getSession()){
            List<String> usernames = session.readTransaction((TransactionWork<List<String>>) tx ->{
                Result result = tx.run("MATCH (u1:User {username: $username})-[f:FOLLOW]->(u2:User)<-[f:FOLLOW]-(u3:User)" +
                                "(u1)-[f:FOLLOW]->(a:App)<-[f:FOLLOW]-(u3)" +
                                "WHERE NOT (u1)-[:FOLLOW]->(u3)" +
                                "RETURN DISTINCT u3.username AND u3<>u1" +
                                "LIMIT $limit ",
                        parameters("username", username, "limit", limit));
                ArrayList<String> users = new ArrayList<>();
                while (result.hasNext()){
                    Record r = result.next();
                    users.add(r.get("u2.username").asString());
                }
                return users;
            });
            System.out.println(usernames);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
