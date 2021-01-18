package it.unipi.softgram.controller.mongoneo4j;

import it.unipi.softgram.controller.mongo.UserMongoManager;
import it.unipi.softgram.entities.User;
import it.unipi.softgram.utilities.drivers.Neo4jDriver;
import it.unipi.softgram.utilities.enumerators.Role;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

import static org.neo4j.driver.Values.parameters;

//test this

public class UserMongoNeo4jManager {
    private final Neo4jDriver neo4jDriver;
    private final UserMongoManager userMongoManager;



    public UserMongoNeo4jManager(){
        neo4jDriver = new Neo4jDriver();
        userMongoManager = new UserMongoManager();
    }
    //User
    public void addUser( User user){
        try ( Session session = neo4jDriver.getSession() ) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MERGE (u:User {username: $username})" +
                                "SET u:NormalUser",
                        parameters( "username", user.getUsername()) );
                try {
                    userMongoManager.addUser(user);
                }
                catch (RuntimeException r ) {
                    if (r.getMessage().equals("write operation failed")) {
                        tx.rollback();
                    }
                    r.printStackTrace();
                }
                return null;
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        User user = new User();
        user.setUsername("andreagerra");
        UserMongoNeo4jManager neo = new UserMongoNeo4jManager();
        neo.addUser(user);
        //neo.removeUser(user.getUsername());
        neo.becomeDeveloper(user.getUsername());
        //neo.becomeNormalUser(user.getUsername());
        //neo.changeUserRole(user.getUsername(), Role.RoleValue.ADMIN);
    }

    public void removeUser(String username){
        try ( Session session = neo4jDriver.getSession() ) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MATCH (u:User {username: $username})" +
                                "DETACH DELETE u",
                        parameters( "username", username) );
                try {
                    userMongoManager.removeUser(username);
                }
                catch (RuntimeException r ) {
                    if (r.getMessage().equals("write operation failed")) {
                        tx.rollback();
                    }
                    r.printStackTrace();
                }
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
                try {
                    userMongoManager.becomeDeveloper(username);
                }
                catch (RuntimeException r ) {
                    if (r.getMessage().equals("write operation failed")) {
                        tx.rollback();
                    }
                    r.printStackTrace();
                }
                return null;
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //Developer
    public void becomeNormalUser(String username){
        try ( Session session = neo4jDriver.getSession() ) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MATCH (u:User {username: $username})" +
                                "REMOVE u:Developer" +
                                "SET u:NormalUser",
                        parameters( "username", username) );
                try {
                    userMongoManager.becomeNormalUser(username);
                }
                catch (RuntimeException r ) {
                    if (r.getMessage().equals("write operation failed")) {
                        tx.rollback();
                    }
                    r.printStackTrace();
                }
                return null;
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //Admin
    public void changeUserRole(String username, Role.RoleValue role){
        String roleString = Role.getRoleString(role).replace("\\s","");
        try ( Session session = neo4jDriver.getSession() ) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MATCH (u:User {username: $username})" +
                                "REMOVE u:Developer:NormalUser:Admin" +
                                "SET u:" + Role.getRoleString(role),
                        parameters( "username", username) );
                try {
                    userMongoManager.changeUserRole(username, role);
                }
                catch (RuntimeException r ) {
                    if (r.getMessage().equals("write operation failed")) {
                        tx.rollback();
                    }
                    r.printStackTrace();
                }
                return null;
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
