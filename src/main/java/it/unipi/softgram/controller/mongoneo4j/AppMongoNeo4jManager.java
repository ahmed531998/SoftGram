package it.unipi.softgram.controller.mongoneo4j;

import it.unipi.softgram.controller.mongo.AppMongoManager;
import it.unipi.softgram.controller.neo4j.AppNeo4jManager;
import it.unipi.softgram.entities.App;
import it.unipi.softgram.entities.User;
import it.unipi.softgram.utilities.drivers.Neo4jDriver;
import it.unipi.softgram.utilities.enumerators.Relation;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

import static org.neo4j.driver.Values.parameters;


public class AppMongoNeo4jManager {
    private final Neo4jDriver neo4jDriver;
    private final AppMongoManager appMongoManager;


    public AppMongoNeo4jManager(){
        neo4jDriver = new Neo4jDriver();
        appMongoManager = new AppMongoManager();
    }

    public void addApp(App a, User u) {
        String name, category;
        if(a.getName() == null)
            name = "Unknown";
        else
            name = a.getName();
        if(a.getCategory()== null)
            category = "Unknown";
        else
            category = a.getCategory();
        try (Session session = neo4jDriver.getSession()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MERGE (a:App {id: $id, app_name: $name, category: $category}) ",
                        parameters("id", a.getId(),
                                "name", name,
                                "category", category));
                try {
                    appMongoManager.addApp(a);
                }
                catch (RuntimeException r ) {
                    if (r.getMessage().equals("write operation failed")) {
                        tx.rollback();
                    }
                    r.printStackTrace();
                    return null;
                }
                if (u != null) {
                    /*AppNeo4jManager neo = new AppNeo4jManager();
                    neo.followOrDevelopApp(u, a, Relation.RelationType.DEVELOP);*/
                }
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeApp(App a){
        try ( Session session = neo4jDriver.getSession() ) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MATCH (a:App {id: $id}) " +
                                "DETACH DELETE a",
                        parameters( "id", a.getId()) );
                try {
                    appMongoManager.deleteApp(a);
                }
                catch (RuntimeException r ) {
                    if (r.getMessage().equals("write operation failed")) {
                        tx.rollback();
                    }
                    r.printStackTrace();
                    return null;
                }
                return null;
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

// maybe separate function (updateName() and updateCategory()) in order to call these only when needed
    public void updateName(App a){
        try ( Session session = neo4jDriver.getSession() ) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MATCH (a:App {id: $id}) " +
                                "SET a.app_name = $name",
                        parameters("id", a.getId(),
                                "name", a.getName()));
                try {
                    appMongoManager.updateName(a);
                }
                catch (RuntimeException r ) {
                    if (r.getMessage().equals("write operation failed")) {
                        tx.rollback();
                    }
                    r.printStackTrace();
                    return null;
                }
                return null;
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateCategory(App a){
        try ( Session session = neo4jDriver.getSession() ) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MATCH (a:App {id: $id}) " +
                                "SET a.category = $category",
                        parameters("id", a.getId(),
                                "category", a.getCategory()));
                try {
                    appMongoManager.updateCategory(a);
                }
                catch (RuntimeException r ) {
                    if (r.getMessage().equals("write operation failed")) {
                        tx.rollback();
                    }
                    r.printStackTrace();
                    return null;
                }
                return null;
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
