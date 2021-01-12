package it.unipi.softgram.utilities.drivers;

import org.neo4j.driver.*;
import org.neo4j.driver.internal.logging.JULogging;


import java.util.logging.Level;


public class Neo4jDriver {
    private static Driver driver;

    public Neo4jDriver(){
        if(driver==null) {
            String uri = "bolt://localhost:7687";
            String user = "neo4j";
            String password = "root";
            Config config = Config.builder().withLogging(new JULogging(Level.WARNING)).build();
            try {
                driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password), config);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public Session getSession (){
        if(driver!=null)
            return driver.session();
        else throw new RuntimeException("Connection doesn't exist");
    }

    public void close(){
        if(driver!= null)
            driver.close();
        else throw new RuntimeException("Connection doesn't exist.");
    }
}
