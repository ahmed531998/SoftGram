package it.unipi.softgram;

import it.unipi.softgram.controller.mongo.AppMongoManager;
import it.unipi.softgram.controller.mongo.ReviewMongoManager;
import it.unipi.softgram.controller.mongo.UserMongoManager;
import it.unipi.softgram.controller.mongoneo4j.AppMongoNeo4jManager;
import it.unipi.softgram.controller.mongoneo4j.UserMongoNeo4jManager;
import it.unipi.softgram.controller.neo4j.AppNeo4jManager;
import it.unipi.softgram.controller.neo4j.UserNeo4jManager;
import it.unipi.softgram.entities.App;
import it.unipi.softgram.entities.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class a {

    public static void main(String[] args) throws ParseException {
        AppMongoManager AQ = new AppMongoManager();
        UserMongoManager UQ = new UserMongoManager();
        ReviewMongoManager RQ = new ReviewMongoManager();

        AppNeo4jManager ANQ = new AppNeo4jManager();
        UserNeo4jManager UNQ = new UserNeo4jManager();

        AppMongoNeo4jManager AMNQ = new AppMongoNeo4jManager();
        UserMongoNeo4jManager UMNQ = new UserMongoNeo4jManager();


//        System.out.println(AQ.getPopularApps(5));
//        System.out.println(AQ.getPopularAppsPerYear(2019, 5));
//        System.out.println(AQ.getPopularAppsPerCat("Finance", 5));
//
        List<User> us = UQ.searchUserByUsername("Richard L", 0);
        User u = us.get(0);

        System.out.println("common");
        List<App> as = ANQ.browseCommonApps();
        for (App a: as){
            System.out.println(a.toAppDocument());
        }
        System.out.println("fav");

        as = ANQ.browseFavoriteCategory(u);
        for (App a: as){
            System.out.println(a.toAppDocument());
        }
        System.out.println("foll");

        as = ANQ.browseAppsOfFollowers(u);
        for (App a: as){
            System.out.println(a.toAppDocument());
        }
        System.out.println("dev");

        as = ANQ.browseDevelopedApps(u);
        for (App a: as){
            System.out.println(a.toAppDocument());
        }
        System.out.println("follwed");

        as = ANQ.browseFollowedApps(u);
        for (App a: as){
            System.out.println(a.toAppDocument());
        }


        Date dos = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2019-02-22 17:21:47");
        Date dor = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2019-02-22 17:21:47");

    }
}
