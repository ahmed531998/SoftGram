package controller.mongoneo4j;


import controller.mongo.AppMongoManager;
import controller.neo4j.AppNeo4jManager;
import entities.User;
import enumerators.Relation;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;
import utilities.Neo4jDriver;
import entities.App;
import static org.neo4j.driver.Values.parameters;

//test only the update app but change it

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
                    System.out.println(u.getUsername());
                    System.out.println(a.getId());
                    AppNeo4jManager neo = new AppNeo4jManager();
                    neo.followOrDevelopApp(u, a, Relation.RelationType.DEVELOP);
                    System.out.println("test2");
                }else {
                    System.out.println("here");
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
    public void updateApp(App a){
        String name, category;
        if(a.getName() == null)
            name = "Unknown";
        else
            name = a.getName();
        if(a.getCategory()== null)
            category = "Unknown";
        else
            category = a.getCategory();

        try ( Session session = neo4jDriver.getSession() ) {
            session.writeTransaction((TransactionWork<Void>) tx -> {

                tx.run( "MATCH (a:App {id:$id}) " +
                                "SET a.app_name= $name, a.category= $category",
                        parameters("id", a.getId(),
                                "name", name,
                                "category", category));
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
