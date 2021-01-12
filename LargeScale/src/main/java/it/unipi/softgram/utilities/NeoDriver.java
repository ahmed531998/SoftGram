package it.unipi.softgram.utilities;

import org.neo4j.driver.*;
import org.neo4j.driver.internal.logging.JULogging;

import java.util.logging.Level;

public class NeoDriver {
    private Driver driver;
    private static NeoDriver graph = null;

    private NeoDriver(String uri, String username, String password){
        Config config = Config.builder().withLogging(new JULogging(Level.WARNING)).build();
        try {
            driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password), config);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static NeoDriver getInstance(String uri, String username, String password){
        if(graph == null){
            graph = new NeoDriver(uri, username, password);
        }
        return graph;
    }
    public Session getSession (){
        if(driver!=null)
            return driver.session();
        else throw new RuntimeException("Connection doesn't exist");
    }

    public Driver getDriver(){
        try{
            return graph.driver;
        } catch (RuntimeException e){
            e.printStackTrace();
        }
        return null;
    }

    public void close(){
        try {
            graph.driver.close();
        } catch (RuntimeException e){
            e.printStackTrace();
        }
    }
}

