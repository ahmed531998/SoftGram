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
        try (Session session = neo4jDriver.getSession()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MERGE (a:App {id: $id, app_name: $name, category: $category}) ",
                        parameters("id", a.getId(),
                                "name", a.getName(),
                                "category", a.getCategory()));
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
                    AppNeo4jManager neo = new AppNeo4jManager();
                    neo.followOrDevelopApp(u, a, Relation.RelationType.DEVELOP);
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
                    appMongoManager.deleteApp(a.getId());
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

    // maybe add function for change category or app_name or in the app manager leave
    // update app and add fields to update
    public void updateApp(App a){
        try ( Session session = neo4jDriver.getSession() ) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MATCH (a:App {id: $id}) " +
                                "SET a.app_name: $name, a.category: $category",
                        parameters("id", a.getId(),
                                "name", a.getName(),
                                "category", a.getCategory()));
                try {
                    appMongoManager.updateApp(a);
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
