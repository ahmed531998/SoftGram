package it.unipi.softgram;

import it.unipi.softgram.controller.mongo.ReviewMongoManager;
import it.unipi.softgram.entities.Review;
import it.unipi.softgram.utilities.drivers.MongoDriver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class a {

    public static void main(String[] args) throws ParseException {
        ReviewMongoManager RQ = new ReviewMongoManager();
        Date dos = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2019-02-22 17:21:47");
        Date dor = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2019-02-22 17:21:47");

        Review r = new Review("com.commsource.beautyplus",
                "Richard L",
                dos,
                dor,
                "Great app for accessing tickets but the search function is faulty - I search for the exact name of an event i have tickets for and hundreds of other events come up but not the one i search for. Also, why can't i favourite the event i have tickets for under the tickets tab?",
                3);

        //RQ.postNewReview(r);
//        System.out.println(RQ.searchById("Richard L", true, 10));
//        System.out.println(RQ.searchByWord("bla", 10));
//        System.out.println(RQ.searchByDate(dor, ReviewMongoManager.DateQuery.On, 10));
//        //RQ.updateReviewContent(r, "bla");
        //r.setContent("bla");
        //RQ.updateReviewScore(r, 5);
        RQ.delete(r);
    }
}
