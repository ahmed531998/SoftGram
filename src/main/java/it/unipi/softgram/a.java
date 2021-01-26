package it.unipi.softgram;

import com.mongodb.client.MongoClients;
import it.unipi.softgram.controller.mongo.AppMongoManager;
import it.unipi.softgram.controller.mongo.ReviewMongoManager;
import it.unipi.softgram.controller.mongo.StatisticsMongoManager;
import it.unipi.softgram.controller.mongo.UserMongoManager;
import it.unipi.softgram.controller.mongoneo4j.AppMongoNeo4jManager;
import it.unipi.softgram.controller.mongoneo4j.UserMongoNeo4jManager;
import it.unipi.softgram.controller.neo4j.AppNeo4jManager;
import it.unipi.softgram.controller.neo4j.UserNeo4jManager;
import it.unipi.softgram.entities.App;
import it.unipi.softgram.entities.Review;
import it.unipi.softgram.entities.User;
import it.unipi.softgram.utilities.drivers.MongoDriver;
import it.unipi.softgram.utilities.drivers.Neo4jDriver;
import it.unipi.softgram.utilities.enumerators.Role;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class a {

    public void signup() throws ParseException {
        UserMongoNeo4jManager userRegisterer = new UserMongoNeo4jManager();
        UserMongoManager userFinder = new UserMongoManager();
        Scanner sc = new Scanner(System.in);
        User u = new User();
        String username;
        boolean found;
        //get unique username
        do{
            System.out.println("Please enter a username");
            username = sc.nextLine();
            found = userFinder.searchUserByUsername(username, 0).size()>0;
        }while(username.isEmpty() || found);
        String password;
        //get password
        do{
            System.out.println("Please enter a password");
            password = sc.nextLine();
        }while(password.isEmpty());

        String country, email, website, answer, dateString;
        Date date;
        do{
            System.out.println("Do you want to add country? (y/n)");
            answer = sc.nextLine();
            switch(answer){
                case "y":
                    do{
                        System.out.println("Please enter a country");
                        country = sc.nextLine();
                    }while(country.isEmpty());
                    u.setCountry(country);
                    break;
                case "n":
                    System.out.println("No country will be added");
                    break;
                default:
                    break;
            }
        }while(answer.isEmpty());

        do{
            System.out.println("Do you want to add email? (y/n)");

            answer = sc.nextLine();
            switch(answer){
                case "y":
                    do{
                        System.out.println("Please enter an email");
                        email = sc.nextLine();
                    }while(email.isEmpty());
                    u.setEmail(email);
                    break;
                case "n":
                    System.out.println("No Email will be added");
                    break;
                default:
                    break;
            }
        }while(answer.isEmpty());

        do{
            System.out.println("Do you want to add website? (y/n)");

            answer = sc.nextLine();
            switch(answer){
                case "y":
                    do{
                        System.out.println("Please enter a website");
                        website = sc.nextLine();
                    }while(website.isEmpty());
                    u.setWebsite(website);
                    break;
                case "n":
                    System.out.println("No website will be added");
                    break;
                default:
                    break;
            }
        }while(answer.isEmpty());
        do{
            System.out.println("Do you want to add birthday? (y/n)");

            answer = sc.nextLine();
            switch(answer){
                case "y":
                    do{
                        System.out.println("Please enter a birthday (yyyy-mm-dd)");
                        dateString = sc.nextLine();
                    }while(dateString.isEmpty());
                    String pattern = "yyyy-MM-dd";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                    date = simpleDateFormat.parse(dateString);
                    u.setBirthday(date);
                    break;
                case "n":
                    System.out.println("No birthday will be added");
                    break;
                default:
                    break;
            }
        }while(answer.isEmpty());
        u.setUsername(username);
        u.setPassword(password);
        u.setRole("Normal User");
        userRegisterer.addUser(u);
    }

    public User login(){
        UserMongoManager userFinder = new UserMongoManager();
        Scanner sc = new Scanner(System.in);
        User u = new User();
        String username;
        List<User> foundUsers;
        //get unique username
        do{
            System.out.println("Please enter your username");
            username = sc.nextLine();
            foundUsers = userFinder.searchUserByUsername(username, 0);
        }while(username.isEmpty() || foundUsers.size()!=1);
        System.out.println("Please enter a password");
        String password;
        //get password
        do{
            System.out.println("Please enter the correct password");
            password = sc.nextLine();
        }while(password.isEmpty() || !foundUsers.get(0).getPassword().equals(password));
        return foundUsers.get(0);
    }

    public void modifyProfile(User u){
        UserMongoNeo4jManager userModifier = new UserMongoNeo4jManager();
        Scanner sc = new Scanner(System.in);
        String ans;
        do{
            System.out.println("Would you like to become developer? (y/n)");
            ans = sc.nextLine();
            switch (ans){
                case "y":
                    userModifier.becomeDeveloper(u.getUsername());
                case"n":
                    break;
                default:
                    break;
            }
        }while(ans.isEmpty());
        userModifier.changeUserRole(u.getUsername(), Role.getRoleFromString(u.getRole()));
    }

    public void browseFollowers(User u){
        UserNeo4jManager followerFinder = new UserNeo4jManager();
        List<String> followers = followerFinder.browseActualFollowers(u.getUsername());
        System.out.println(followers);
    }

    public void browseSuggestedUsers(User u){
        UserNeo4jManager recommender = new UserNeo4jManager();
        Set<String> suggested = recommender.browseSuggestedUsers(u.getUsername(), 10);
        System.out.println(suggested);
    }

    public void browseFollowedUsers(User u){
        UserNeo4jManager friendsFinder = new UserNeo4jManager();
        List<String> friends = friendsFinder.browseFollowedUsers(u.getUsername());
        System.out.println(friends);
    }

    public void searchUsers(){
        UserMongoManager userFinder = new UserMongoManager();
        List<User>foundUsers = null;
        Scanner sc = new Scanner(System.in);
        String ans, email, username;
        do{
            System.out.println("Search by email or username? (0/1)");
            ans = sc.nextLine();
            switch (ans){
                case "0":
                    do{
                        email = sc.nextLine();
                    }while (email.isEmpty());
                    foundUsers = userFinder.searchUserByEmail(email, 0);
                    break;
                case"1":
                    do{
                        username = sc.nextLine();
                    }while (username.isEmpty());
                    foundUsers = userFinder.searchUserByUsername(username, 0);
                    break;
                default:
                    break;
            }
        }while(ans.isEmpty());

        for (User u: foundUsers){
            System.out.format("%s\t%s\t%s\t%s\t%s\t\n",
                    u.getUsername(), u.getEmail(), u.getWebsite(), u.getCountry(),u.getRole());
        }
    }

    public void searchReview() throws ParseException {
        ReviewMongoManager reviewFinder = new ReviewMongoManager();
        List<Review>foundReviews = null;
        Scanner sc = new Scanner(System.in);
        String ans, word, dateString;
        do{
            System.out.println("Search by date or word? (0/1)");
            ans = sc.nextLine();
            switch (ans){
                case "0":
                    do{
                        dateString = sc.nextLine();
                    }while (dateString.isEmpty());
                    String pattern = "yyyy-MM-dd";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                    Date date = simpleDateFormat.parse(dateString);
                    foundReviews = reviewFinder.searchByDate(date, ReviewMongoManager.DateQuery.After, 0);
                    break;
                case"1":
                    do{
                        word = sc.nextLine();
                    }while (word.isEmpty());
                    foundReviews = reviewFinder.searchByWord(word, 0);
                    break;
                default:
                    break;
            }
        }while(ans.isEmpty());

        for (Review r: foundReviews){
            System.out.format("%s\t%f\t%s\t%s\t%s\t\n",
                   r.getContent(), r.getScore(), r.getUsername(), r.getAppId(), r.getAppName());
        }
    }

    public void browsePopularApps(){
        AppMongoManager appFinder = new AppMongoManager();
        List<App>foundApps = null;
        Scanner sc = new Scanner(System.in);
        String ans, category;
        int year;
        do{
            System.out.println("Search by category or year or in general? (0/1/2)");
            ans = sc.nextLine();
            switch (ans){
                case "0":
                    do{
                        category = sc.nextLine();
                    }while (category.isEmpty());
                    foundApps = appFinder.getPopularAppsPerCat(category, 50, 0);
                    break;
                case"1":
                    year = sc.nextInt();
                    foundApps = appFinder.getPopularAppsPerYear(year, 50, 0);
                    break;
                case "2":
                    foundApps = appFinder.getPopularApps(50, 0);
                    break;
                default:
                    break;
            }
        }while(ans.isEmpty());

        for (App a: foundApps){
            System.out.format("%s\t%s\t%s\t%f\t%f\t",
                    a.getId(), a.getName(), a.getCategory(), a.getAverage(), a.getReviewCount());
        }
    }

    public static void main(String[] args) throws ParseException {
        AppMongoManager AQ = new AppMongoManager();
        UserMongoManager UQ = new UserMongoManager();
        ReviewMongoManager RQ = new ReviewMongoManager();
        AppNeo4jManager ANQ = new AppNeo4jManager();
        UserNeo4jManager UNQ = new UserNeo4jManager();
        StatisticsMongoManager SQ = new StatisticsMongoManager();

        AppMongoNeo4jManager AMNQ = new AppMongoNeo4jManager();
        UserMongoNeo4jManager UMNQ = new UserMongoNeo4jManager();

        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                User myUser = new User();
                System.out.println("Welcome to softgram");
                System.out.println("To sign up press 1");
                System.out.println("To log in press 2");

                int answer = sc.nextInt();
                if (answer == 1) {
                    User u = new User();
                    System.out.println("Please enter a username");

                    System.out.println("Please enter a password");

                }
                if (answer == 2) {
                    System.out.println("Please Enter your username");
                    String username = sc.nextLine();
                    System.out.println("Please Enter your password");
                    String password = sc.nextLine();
                    User u = new User();
                    u.setUsername(username);
                    u.setUsername(password);

                    List<User> users = UQ.searchUserByUsername(username, 0);
                    if (users.size() == 1) {
                        if (users.get(0).getPassword().equals(password)) {
                            System.out.println("Welcome to softgram");
                        } else {
                            System.out.println("Wrong credentials");
                            continue;
                        }
                    }
                }


            }
        }
    }
}
