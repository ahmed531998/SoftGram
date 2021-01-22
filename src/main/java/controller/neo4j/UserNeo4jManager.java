package controller.neo4j;


import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;
import utilities.Neo4jDriver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

//test this

public class UserNeo4jManager {
    private final Neo4jDriver neo4jDriver;

    public UserNeo4jManager(){
        neo4jDriver = new Neo4jDriver();
    }

    //if false accept follow, if true follow request
    public void addFollow(String followerUsername, String followedUsername, boolean request){
        String partOfQuery = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());
        if(request)
            partOfQuery = "SET f.accepted = false ";
        else
            partOfQuery = "REMOVE f.accepted ";
        final String partOfQueryFinal = partOfQuery;
        try ( Session session = neo4jDriver.getSession() ) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MATCH (u:User {username: $followerUsername}),(u2:User {username: $followedUsername}) " +
                                "MERGE (u)-[f:FOLLOW]->(u2) " +
                                partOfQueryFinal +
                                "SET f.date = date($currentDate) ",
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
                tx.run( "MATCH ((u:User {username: $followerUsername})-[f:FOLLOW]->(u2:User {username: $followedUsername})) " +
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



    public List<String> browseFollowedUsers(String username){
        try (Session session = neo4jDriver.getSession()){
          return session.readTransaction((TransactionWork<List<String>>) tx ->{
                Result result = tx.run("MATCH (u:User)-[f:FOLLOW]->(u2:User) " +
                                            "WHERE u.username = $name AND NOT EXISTS(f.accepted) " +
                                            "RETURN u2.username "+
                                            "ORDER BY f.date DESC ",
                                        parameters("name",username));
                ArrayList<String> followedUsers = new ArrayList<>();
                while (result.hasNext()){
                    Record r = result.next();
                    followedUsers.add(r.get("u2.username").asString());
                }
                return followedUsers;
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<String> browseActualFollowers(String username){
        String partOfQuery = "AND NOT EXISTS(f.accepted) ";
        return browseFollowers(username,partOfQuery);
    }

    public List<String> browseFollowRequests(String username){
        String partOfQuery = "AND f.accepted = false ";
        return browseFollowers(username, partOfQuery);
    }

    public List<String> browseFollowers(String username, final String partOfQuery){
        try (Session session = neo4jDriver.getSession()){
            return session.readTransaction((TransactionWork<List<String>>) tx ->{
                Result result = tx.run("MATCH (u:User)-[f:FOLLOW]->(u2:User) " +
                                "WHERE u2.username = $name "+ partOfQuery +
                                "RETURN u.username " +
                                "ORDER BY f.date DESC ",
                        parameters("name",username));
                ArrayList<String> followers = new ArrayList<>();
                while (result.hasNext()){
                    Record r = result.next();
                    followers.add(r.get("u.username").asString());
                }
                return followers;
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<String> browseTop10UsersWithMostFollowersInYear(int year){
        return browseUsersWithMostFollowersInYear(year, 10);
    }

    public List<String> browseUsersWithMostFollowersInYear(int year, int limit){
        try (Session session = neo4jDriver.getSession()){
            return session.readTransaction((TransactionWork<List<String>>) tx ->{
                Result result = tx.run("MATCH (u1:User)-[f:FOLLOW]->(u2:User) " +
                                "WHERE " +
                                "date({year: $nextYear, month: 1, day: 1}) > f.date >= date({year: $currentYear, month: 1, day: 1}) " +
                                "AND NOT EXISTS(f.accepted)  " +
                                "RETURN u2.username, count(u1) AS followers " +
                                "ORDER BY followers DESC " +
                                "LIMIT $limit ",
                        parameters("limit", limit, "currentYear", year, "nextYear",year+1));
                ArrayList<String> followers = new ArrayList<>();
                while (result.hasNext()){
                    Record r = result.next();
                    followers.add(r.get("u2.username").asString() + " " + r.get("followers").asInt() + " " + "followers");
                }
                return followers;
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //example with Young Kim
    public List<String> browseSuggestedUsers(String username, int limit){
        try (Session session = neo4jDriver.getSession()){
            return session.readTransaction((TransactionWork<List<String>>) tx ->{
                Result result = tx.run("MATCH (u1:User {username: $username})-[:FOLLOW]->(u2:User)-[:FOLLOW]->(u3:User), " +
                                "(u1)-[:FOLLOW]->(a:App)<-[:FOLLOW]-(u3) " +
                                "WHERE NOT (u1)-[:FOLLOW]->(u3) AND u3<>u1 " +
                                "RETURN DISTINCT u3.username " +
                                "LIMIT $limit ",
                        parameters("username", username, "limit", limit));
                ArrayList<String> users = new ArrayList<>();
                while (result.hasNext()){
                    Record r = result.next();
                    users.add(r.get("u3.username").asString());
                }
                return users;
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
