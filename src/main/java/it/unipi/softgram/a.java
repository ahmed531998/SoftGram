package it.unipi.softgram;

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
import it.unipi.softgram.utilities.enumerators.Relation;
import it.unipi.softgram.utilities.enumerators.Role;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class a {

    public static void signup() throws ParseException {
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
            found = userFinder.searchUserByExactUsername(username, 0).size()>0;
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
        System.out.println("Sign Up Successful, you can log in");
    }

    public static User login(){
        UserMongoManager userFinder = new UserMongoManager();
        Scanner sc = new Scanner(System.in);
        String username;
        List<User> foundUsers;
        //get unique username
        do{
            System.out.println("Please enter your username");
            username = sc.nextLine();
            foundUsers = userFinder.searchUserByExactUsername(username, 0);

        }while(username.isEmpty() || foundUsers.size()!=1);
        System.out.println("Please enter a password");
        String password;
        //get password
        do{
            System.out.println("Please enter the correct password");
            password = sc.nextLine();
        }while(password.isEmpty() || !foundUsers.get(0).getPassword().equals(password));

        System.out.println("Welcome " + foundUsers.get(0).getUsername());
        return foundUsers.get(0);
    }

    public static void modifyProfile(User u) throws ParseException {
        UserMongoNeo4jManager userModifier = new UserMongoNeo4jManager();
        UserMongoManager userModifierMongo = new UserMongoManager();
        Scanner sc = new Scanner(System.in);
        String ans;

        do {
            System.out.println("Would you like to update your birthday? (y/n)");
            ans = sc.nextLine();
            if ("y".equals(ans)) {
                String dateString;
                do{
                    System.out.println("Please enter a birthday (yyyy-mm-dd)");
                    dateString = sc.nextLine();
                }while(dateString.isEmpty());
                String pattern = "yyyy-MM-dd";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                Date date = simpleDateFormat.parse(dateString);
                u.setBirthday(date);
                userModifierMongo.saveNewBirthday(u);
            }
        } while (ans.isEmpty());


        do {
            System.out.println("Would you like to update your country? (y/n)");
            ans = sc.nextLine();
            if ("y".equals(ans)) {
                String country;
                do{
                    System.out.println("Please enter a country");
                    country = sc.nextLine();
                }while(country.isEmpty());
                u.setCountry(country);
                userModifierMongo.saveNewCountry(u);
            }
        } while (ans.isEmpty());


        do {
            System.out.println("Would you like to update your email? (y/n)");
            ans = sc.nextLine();
            if ("y".equals(ans)) {
                String email;
                do{
                    System.out.println("Please enter an email");
                    email = sc.nextLine();
                }while(email.isEmpty());
                u.setEmail(email);
                userModifierMongo.saveNewEmail(u);
            }
        } while (ans.isEmpty());


        do {
            System.out.println("Would you like to update your password? (y/n)");
            ans = sc.nextLine();
            if ("y".equals(ans)) {
                String password;
                //get password
                do{
                    System.out.println("Please enter a password");
                    password = sc.nextLine();
                }while(password.isEmpty());
                u.setPassword(password);
                userModifierMongo.saveNewPassword(u);
            }
        } while (ans.isEmpty());


        do {
            System.out.println("Would you like to update your website? (y/n)");
            ans = sc.nextLine();
            if ("y".equals(ans)) {
                String website;
                do{
                    System.out.println("Please enter a website");
                    website = sc.nextLine();
                }while(website.isEmpty());
                u.setWebsite(website);
                userModifierMongo.saveNewWebsite(u);
            }
        } while (ans.isEmpty());


        if (u.getRole().equals("Normal User")) {
            do {
                System.out.println("Would you like to become developer? (y/n)");
                ans = sc.nextLine();
                if ("y".equals(ans)) {
                    userModifier.becomeDeveloper(u.getUsername());
                }
            } while (ans.isEmpty());
        } else if (u.getRole().equals("Developer")){
            do {
                System.out.println("Would you like to become Normal User? (y/n)");
                ans = sc.nextLine();
                if ("y".equals(ans)) {
                    userModifier.becomeNormalUser(u.getUsername());
                }
            } while (ans.isEmpty());
        }
    }

    public static void browseFollowers(User u){
        UserNeo4jManager followerFinder = new UserNeo4jManager();
        List<String> followers = followerFinder.browseActualFollowers(u.getUsername());
        List<User> f = null;
        for(String s: followers){
            User x = new User(); x.setUsername(s);
            assert false;
            Objects.requireNonNull(f).add(x);
        }
        System.out.println(followers);
        viewUser(u,f);
    }

    public static void browseSuggestedUsers(User u){
        UserNeo4jManager recommender = new UserNeo4jManager();
        Set<String> suggested = recommender.browseSuggestedUsers(u.getUsername(), 10);
        System.out.println(suggested);
        List<User> f = null;
        for(String s: suggested){
            User x = new User(); x.setUsername(s);
            assert false;
            f.add(x);
        }
        viewUser(u, f);
    }

    public static List<String> browseFollowedUsers(User u){
        UserNeo4jManager friendsFinder = new UserNeo4jManager();
        List<String> friends = friendsFinder.browseFollowedUsers(u.getUsername());
        System.out.println(friends);
        List<User> f = null;
        for(String s: friends){
            User x = new User(); x.setUsername(s);
            assert false;
            f.add(x);
        }
        viewUser(u, f);
        return friends;
    }

    public static void searchUsers(User myUser){
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

        assert foundUsers != null;
        for (User u: foundUsers){
            u.printUserInformation();
        }
        viewUser(myUser, foundUsers);
    }

    public static void searchReview(User myUser) throws ParseException {
        ReviewMongoManager reviewFinder = new ReviewMongoManager();
        List<Review>foundReviews = null;
        Scanner sc = new Scanner(System.in);
        String ans, word, dateString, appId, category, username;
        do{
            System.out.println("Search by date or word or userid or appId or category or score? (0/1/2/3/4/5)");
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
                        System.out.println("With word");
                        word = sc.nextLine();
                    }while (word.isEmpty());
                    foundReviews = reviewFinder.searchByWord(word, 0);
                    break;
                case"2":
                    do{
                        System.out.println("With username");
                        username = sc.nextLine();
                    }while (username.isEmpty());
                    foundReviews = searchUserReviews(username);
                    break;
                case"3":
                    do{
                        System.out.println("With appId");
                        appId = sc.nextLine();
                    }while (appId.isEmpty());
                    foundReviews = searchAppReviews(appId);
                    break;
                case"4":
                    do{
                        System.out.println("With category");
                        category = sc.nextLine();
                    }while (category.isEmpty());
                    foundReviews = reviewFinder.showReviewsOfCat(category, 0);
                    break;
                case"5":
                    System.out.println("With score");
                    Double score = sc.nextDouble();
                    foundReviews = reviewFinder.searchByScore(score, 0);
                    break;
                default:
                    break;
            }
        }while(ans.isEmpty());

        assert foundReviews != null;
        for (Review r: foundReviews){
            r.printReviewInformation();
        }
        viewReview(myUser, foundReviews);
    }

    public static List<Review>searchUserReviews(String username) throws ParseException {
        Scanner sc = new Scanner(System.in);
        ReviewMongoManager reviewFinder = new ReviewMongoManager();
        List<Review> foundReviews;
        System.out.println("By word(0), By date(1), all(anything)");
        String answer;
        do {
            answer = sc.nextLine();
            String word, dateString;
            switch (answer) {
                case "0":
                    do {
                        System.out.println("With word");
                        word = sc.nextLine();
                    } while (word.isEmpty());
                    foundReviews = reviewFinder.searchByWordInUserReviews(word, username, 0);
                    break;
                case "1":
                    do {
                        dateString = sc.nextLine();
                    } while (dateString.isEmpty());
                    String pattern2 = "yyyy-MM-dd";
                    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(pattern2);
                    Date date2 = simpleDateFormat2.parse(dateString);
                    foundReviews = reviewFinder.searchByDateInUserReviews(date2, ReviewMongoManager.DateQuery.After, username, 0);
                    break;
                default:
                    foundReviews = reviewFinder.showReviewsOfUser(username, 0);
                    break;
            }
        }while (answer.isEmpty());
        assert foundReviews != null;
        for (Review r: foundReviews){
            r.printReviewInformation();
        }
        return foundReviews;
    }

    public static List<Review>searchAppReviews(String appId) throws ParseException{
        Scanner sc = new Scanner(System.in);
        ReviewMongoManager reviewFinder = new ReviewMongoManager();
        List<Review> foundReviews;
        System.out.println("By word(0), By date(1), all(anything)");
        String answer;
        do{
            answer = sc.nextLine();
            String word, dateString;
            switch(answer){
                case"0":
                    do{
                        System.out.println("With word");
                        word = sc.nextLine();
                    }while (word.isEmpty());
                    foundReviews = reviewFinder.searchByWordInAppReviews(word, appId, 0);
                    break;
                case"1":
                    do{
                        dateString = sc.nextLine();
                    }while (dateString.isEmpty());
                    String pattern1 = "yyyy-MM-dd";
                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(pattern1);
                    Date date1 = simpleDateFormat1.parse(dateString);
                    foundReviews = reviewFinder.searchByDateInAppReviews(date1, ReviewMongoManager.DateQuery.After,appId, 0);
                    break;
                default:
                    foundReviews = reviewFinder.showReviewsOfApp(appId, 0);
                    break;
            }
        }while (answer.isEmpty());
        assert foundReviews != null;
        for (Review r: foundReviews){
            r.printReviewInformation();
        }
        return foundReviews;
    }

    public static void browsePopularApps(User myUser){
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

        assert foundApps != null;
        for (App a: foundApps){
            a.printAppInformation();
        }
        viewApp(myUser, foundApps);
    }

    public static Set<App> browseFollowedApps(User u){
        AppNeo4jManager appFinder = new AppNeo4jManager();
        System.out.println("Followed Apps:");
        Set<App> apps = appFinder.browseFollowedApps(u);
        List<App> as = null;
        for (App a: apps){
            a.printAppInformation();
            apps.add(a);
        }
        viewApp(u,  as);

        if (u.getRole().equals("Developer")){
            Set<App> devApps = appFinder.browseDevelopedApps(u);
            System.out.println("Developed Apps:");
            List<App> as1 = null;
            for (App a: apps){
                a.printAppInformation();
                apps.add(a);
            }
            viewApp(u,  as1);
        }
        return apps;
    }

    public static void browseSuggestedApps(User u){
        AppNeo4jManager appFinder = new AppNeo4jManager();
        Set<App>apps = appFinder.browseAppsOfFollowers(u);
        apps.addAll(appFinder.browseCommonApps());
        apps.addAll(appFinder.browseFavoriteCategory(u));
        List<App> as = null;
        for (App a: apps){
            a.printAppInformation();
            apps.add(a);
        }
        viewApp(u,  as);
    }

    public static void searchApps(User myUser){
        AppMongoManager appFinder = new AppMongoManager();
        List<App>foundApps = null;
        Scanner sc = new Scanner(System.in);
        String ans, nameId, category, size;
        double price;
        int release;
        boolean ad;
        do{
            System.out.println("Search by nameId or category or price or release or size or ad? (0/1/2/3/4/5)");
            ans = sc.nextLine();
            switch (ans){
                case "0":
                    do{
                        nameId = sc.nextLine();
                    }while (nameId.isEmpty());
                    foundApps = appFinder.findApp(nameId, 50, 0);
                    break;
                case"1":
                    do{
                        category = sc.nextLine();
                    }while (category.isEmpty());
                    foundApps = appFinder.findAppByCategory(category, 50, 0);
                    break;
                case"2":
                    price = sc.nextDouble();
                    foundApps = appFinder.findAppByPrice(price, 50, 0);
                    break;
                case"3":
                    release = sc.nextInt();
                    foundApps = appFinder.findAppByRelease(release, 50, 0);
                    break;
                case"4":
                    do{
                        size = sc.nextLine();
                    }while (size.isEmpty());
                    foundApps = appFinder.findAppBySize(size, 50, 0);
                    break;
                case"5":
                    ad = sc.nextBoolean();
                    foundApps = appFinder.findAppByAd(ad, 50, 0);
                    break;
                default:
                    break;
            }
        }while(ans.isEmpty());

        assert foundApps != null;
        for (App a: foundApps){
          a.printAppInformation();
        }

        viewApp(myUser, foundApps);
    }

    public void downGradeDeveloper(User admin, String developerId){
        if (admin.getRole().equals("Admin")){
            UserMongoNeo4jManager downGrader = new UserMongoNeo4jManager();
            downGrader.becomeNormalUser(developerId);
        }
        else{
            System.out.println("Sorry admin functionality");
        }
    }

    public static void addApp(User u) throws ParseException {
        App a = new App();
        AppMongoNeo4jManager appRegisterer = new AppMongoNeo4jManager();
        AppMongoManager appFinder = new AppMongoManager();
        if (u.getRole().equals("Developer") || u.getRole().equals("Admin")) {
            Scanner sc = new Scanner(System.in);
            String id, name, size, category, ageGroup, currency;
            boolean adSupported, inAppPurchase;
            Date released, lastUpdated;
            double price;
            boolean found;
            //get unique username
            do {
                System.out.println("Please enter an app Id");
                id = sc.nextLine();
                found = appFinder.findAppById(id, 10, 0).size() > 0;
            } while (id.isEmpty() || found);
            //get name
            do {
                System.out.println("Please enter a name");
                name = sc.nextLine();
            } while (name.isEmpty());
            //getCategory
            do {
                System.out.println("Please enter a category");
                category = sc.nextLine();
            } while (category.isEmpty());
            //getReleased
            String dateString;
            do {
                System.out.println("Please enter Released (yyyy-mm-dd)");
                dateString = sc.nextLine();
            } while (dateString.isEmpty());
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            released = simpleDateFormat.parse(dateString);

            String answer;
            //get lastUpdated
            do {
                System.out.println("Do you want to add lastUpdated? (y/n)");
                answer = sc.nextLine();
                switch (answer) {
                    case "y":
                        do {
                            System.out.println("Please enter a lastUpdated (yyyy-mm-dd)");
                            dateString = sc.nextLine();
                        } while (dateString.isEmpty());
                        pattern = "yyyy-MM-dd";
                        simpleDateFormat = new SimpleDateFormat(pattern);
                        lastUpdated = simpleDateFormat.parse(dateString);
                        a.setLastUpdated(lastUpdated);
                        break;
                    case "n":
                        System.out.println("No lastUpdated will be added");
                        break;
                    default:
                        break;
                }
            } while (answer.isEmpty());

            //get ageGroup
            do {
                System.out.println("Do you want to add ageGroup? (y/n)");

                answer = sc.nextLine();
                switch (answer) {
                    case "y":
                        do {
                            System.out.println("Please enter an ageGroup");
                            ageGroup = sc.nextLine();
                        } while (ageGroup.isEmpty());
                        a.setAgeGroup(ageGroup);
                        break;
                    case "n":
                        System.out.println("No ageGroup will be added");
                        break;
                    default:
                        break;
                }
            } while (answer.isEmpty());

            //get size
            do {
                System.out.println("Do you want to add size? (y/n)");
                answer = sc.nextLine();
                switch (answer) {
                    case "y":
                        do {
                            System.out.println("Please enter a size");
                            size = sc.nextLine();
                        } while (size.isEmpty());
                        a.setSize(size);
                        break;
                    case "n":
                        System.out.println("No size will be added");
                        break;
                    default:
                        break;
                }
            } while (answer.isEmpty());

            //get currency
            do {
                System.out.println("Do you want to add currency? (y/n)");

                answer = sc.nextLine();
                switch (answer) {
                    case "y":
                        do {
                            System.out.println("Please enter a currency");
                            currency = sc.nextLine();
                        } while (currency.isEmpty());
                        a.setCurrency(currency);
                        break;
                    case "n":
                        System.out.println("No currency will be added");
                        break;
                    default:
                        break;
                }
            } while (answer.isEmpty());

            //get adSupported
            do {
                System.out.println("Do you want to add adSupported? (y/n)");

                answer = sc.nextLine();
                switch (answer) {
                    case "y":
                        System.out.println("Please enter an adsupported");
                        adSupported = sc.nextBoolean();
                        a.setAdSupported(adSupported);
                        break;
                    case "n":
                        System.out.println("No adSupported will be added");
                        break;
                    default:
                        break;
                }
            } while (answer.isEmpty());

            //get inAppPurchase
            do {
                System.out.println("Do you want to add inAppPurchase? (y/n)");

                answer = sc.nextLine();
                switch (answer) {
                    case "y":
                        System.out.println("Please enter an inAppPurchase");
                        inAppPurchase = sc.nextBoolean();
                        a.setInAppPurchase(inAppPurchase);
                        break;
                    case "n":
                        System.out.println("No inAppPurchase will be added");
                        break;
                    default:
                        break;
                }
            } while (answer.isEmpty());

            //get price
            do {
                System.out.println("Do you want to add price? (y/n)");

                answer = sc.nextLine();
                switch (answer) {
                    case "y":
                        System.out.println("Please enter a price");
                        price = sc.nextDouble();
                        a.setPrice(price);
                        break;
                    case "n":
                        System.out.println("No price will be added");
                        break;
                    default:
                        break;
                }
            } while (answer.isEmpty());
            a.setId(id);
            a.setName(name);
            a.setCategory(category);
            a.setReleased(released);

            appRegisterer.addApp(a, u);
        }
        else{
            System.out.println("You have to be a developer to do this");
        }
    }

    public static void monitorApp(User u){
        Scanner sc = new Scanner(System.in);
        if(u.getRole().equals("Admin")){
            System.out.println("AppsWithAverageAboveValue(0), userActivity(1)" +
                    "yearlyActivity(2), monthlyActivity(3), userYearlyProfile(4)" +
                    "bestAppEachYear(5), bestAppPerCategoryEachYear(6)" +
                    "yearlyAverageCategoryRate(7)");
            String ans;
            StatisticsMongoManager SQ = new StatisticsMongoManager();
            ans = sc.nextLine();
            switch (ans){
                case "0":
                    System.out.println("Enter average threshold");
                    int average = sc.nextInt();
                    SQ.getAppsWithAverageAbove(average, 50);
                    break;
                case"1":
                    System.out.println("Enter activity threshold");
                    int active = sc.nextInt();
                    SQ.getUserActivity(active, 50);
                    break;
                case"2":
                    SQ.monitorYearlyActivity();
                    break;
                case"3":
                    System.out.println("Enter year of interest");
                    int year = sc.nextInt();
                    SQ.monitorMonthlyActivity(year);
                    break;
                case"4":
                    System.out.println("Enter username of interest");
                    String username = sc.nextLine();
                    User x = new User(); x.setUsername(username);
                   SQ.getYearlyUserActivityProfile(x);
                    break;
                case"5":
                    SQ.getBestAppEachYear();
                    break;
                case"6":
                    SQ.getBestAppInEachCategoryEachYear();
                    break;
                case"7":
                    SQ.getYearlyAverageCategoryRate();
                    break;
                default:
                    break;
            }
        }
        else {
            System.out.println("Sorry Admin Functionality");
        }
    }

    public static void removeReview(User u, Review review){
        ReviewMongoManager reviewKiller = new ReviewMongoManager();
        if(u.getRole().equals("Admin"))
            reviewKiller.delete(review);
        else{
            if(u.getUsername().equals(review.getUsername()))
                reviewKiller.delete(review);
            else{
                System.out.println("Unauthorized operation");
            }
        }


    }

    public static void modifyReview(User u, Review review){
        ReviewMongoManager reviewUpdater = new ReviewMongoManager();
        if(u.getUsername().equals(review.getUsername())){
            System.out.println("Content(0), Score(1), both(2)");
            Scanner sc = new Scanner(System.in);
            String ans;
            ans = sc.nextLine();
            switch (ans){
                case"0":
                    System.out.println("Enter new content");
                    String content = sc.nextLine();
                    reviewUpdater.updateReviewContent(review, content);
                    break;
                case"1":
                    System.out.println("Enter new score");
                    double score = sc.nextDouble();
                    reviewUpdater.updateReviewScore(review, score);
                    break;
                case"2":
                    System.out.println("Enter new content");
                    String newcontent = sc.nextLine();
                    reviewUpdater.updateReviewContent(review, newcontent);
                    System.out.println("Enter new score");
                    double newscore = sc.nextDouble();
                    reviewUpdater.updateReviewScore(review, newscore);
                    break;
            }
        }
    }


    public static void viewReview(User u, List<Review> foundReviews){
        int i = 0;
        if(foundReviews == null) {
            System.out.println("No Reviews found");
            return;
        }
        for (Review r: foundReviews){
            System.out.println(i + " " + r.get_id() + " " + r.getAppId() + " " + r.getUsername());
            i++;
        }
        Scanner sc = new Scanner(System.in);
        String ans;
        do{
            System.out.println("Write the review number you want to view // write break to go back");
            ans = sc.nextLine();
            try {
                if(ans.equals("break"))
                    return;
                int userIndex = Integer.parseInt(ans);
                if(userIndex < 0 || userIndex >= foundReviews.size()) {
                    throw new NumberFormatException();
                }
                else {
                    Review selectedReview = foundReviews.get(userIndex);
                    selectedReview.printReviewInformation();
                    System.out.println("To remove review(1), To modify review(2)");

                    ans = sc.nextLine();
                    if(ans.equals("1"))
                        removeReview(u, selectedReview);
                    else if(ans.equals("2"))
                        modifyReview(u, selectedReview);

                }
            }
            catch (NumberFormatException n){
                System.out.println("Please insert the correct number");
            }
        }while(true);
    }

    public static void deleteApp(User u, App a){
        AppMongoNeo4jManager appKiller = new AppMongoNeo4jManager();
        if(u.getRole().equals("Admin"))
            appKiller.removeApp(a);
        else if (u.getRole().equals("Developer")){
            AppNeo4jManager checker = new AppNeo4jManager();
            Set<App> devApps = checker.browseDevelopedApps(u);
            if(devApps.contains(a)){
                appKiller.removeApp(a);
            }
        }
        else{
            System.out.println("Unauthorized");
        }
    }

    public static void updateApp(User u, App a){
        AppMongoNeo4jManager appRefresher = new AppMongoNeo4jManager();
        if (u.getRole().equals("Developer")) {
            AppNeo4jManager checker = new AppNeo4jManager();
            Set<App> devApps = checker.browseDevelopedApps(u);
            if (devApps.contains(a)) {
                System.out.println("Update name(0), update category(1), both(2)");

                Scanner sc = new Scanner(System.in);
                String ans;
                ans = sc.nextLine();
                switch (ans){
                    case"0":
                        System.out.println("Enter new name");
                        String name = sc.nextLine();
                        a.setName(name);
                        appRefresher.updateName(a);
                        break;
                    case"1":
                        System.out.println("Enter new category");
                        String category = sc.nextLine();
                        a.setCategory(category);
                        appRefresher.updateCategory(a);
                        break;
                    case"2":
                        System.out.println("Enter new name");
                        String newname = sc.nextLine();
                        a.setName(newname);
                        appRefresher.updateName(a);
                        System.out.println("Enter new category");
                        String newcategory = sc.nextLine();
                        a.setCategory(newcategory);
                        appRefresher.updateCategory(a);
                        break;
                }
            }
        }
        else{
            System.out.println("Unauthorized");
        }
    }



    public static void viewUser(User myUser, List<User> foundUsers){
        int i = 0;
        if(foundUsers == null) {
            System.out.println("No user found");
            return;
        }
        for (User u: foundUsers){
            System.out.println(i + " " + u.getUsername());
            i++;
        }
        Scanner sc = new Scanner(System.in);
        String ans;
        do{
            System.out.println("Write the user number to show user profile // write break to go back");
            ans = sc.nextLine();
            try {
                if(ans.equals("break"))
                    return;
                int userIndex = Integer.parseInt(ans);
                if(userIndex < 0 || userIndex >= foundUsers.size()) {
                    throw new NumberFormatException();
                }
                else {
                    User selectedUser = foundUsers.get(userIndex);
                    selectedUser.printUserInformation();
                    System.out.println("To remove User(1), To follow/unfollow user(2), To change user role(3)");
                    ans = sc.nextLine();
                    if(ans.equals("1"))
                        removeUser(selectedUser, myUser);
                    else if(ans.equals("2"))
                        askToFollowUnfollowOrBrowsePosts(myUser, foundUsers.get(userIndex));
                    else if(ans.equals("3"))
                        changeUserRole(myUser, selectedUser);

                }
            }
            catch (NumberFormatException | ParseException n){
                System.out.println("Please insert the correct number");
            }
        }while(true);
    }

    public static void askToFollowUnfollowOrBrowsePosts(User myUser, User user) throws ParseException {
        String print;
        List<String> followedUsers = browseFollowedUsers(myUser);
        if(followedUsers.contains(user.getUsername()))
            print = "Write 0 to browse posts, 1 to unfollow 2 to go back";
        else
            print =  "Write 0 to browse posts, 1 to follow 2 to go back";
        System.out.println(print);
        Scanner sc = new Scanner(System.in);
        String ans = sc.nextLine();
        switch (ans){
            case "0":
                searchUserReviews(user.getUsername());
                break;
            case "1":
                UserNeo4jManager neo = new UserNeo4jManager();
                if(followedUsers.contains(user.getUsername()))
                    neo.removeFollow(myUser.getUsername(),user.getUsername());
                else
                    neo.addFollow(myUser.getUsername(), user.getUsername(), true);
                break;
            default:
                break;
        }
    }

    public static void acceptRejectFollows(User myUser, List<String> followRequests){
        int i = 0;
        if(followRequests == null) {
            System.out.println("No follow requests");
            return;
        }
        for (String u: followRequests){
            System.out.println(i + " " + u);
            i++;
        }
        Scanner sc = new Scanner(System.in);
        String ans;
        do{
            System.out.println("Write the user number to accept/reject his follow // write beak to go back");
            ans = sc.nextLine();
            try {
                if(ans.equals("break"))
                    return;
                int userIndex = Integer.parseInt(ans);
                if(userIndex < 0 || userIndex >= followRequests.size()) {
                    throw new NumberFormatException();
                }
                else {
                    System.out.println("Write 0 to reject follow, write 1 to accept follow");
                    UserNeo4jManager neo = new UserNeo4jManager();
                    ans = sc.nextLine();
                    switch (ans){
                        case "0":
                            neo.removeFollow(followRequests.get(userIndex), myUser.getUsername());
                            System.out.println("Follow rejected");
                            break;
                        case "1":
                            neo.addFollow(followRequests.get(userIndex), myUser.getUsername(), false);
                            System.out.println("Follow accepted");
                            break;
                        default:
                            break;
                    }
                }
            }
            catch (NumberFormatException n){
                System.out.println("Please insert the correct number");
            }
        }while(true);
    }

    public static void removeUser(User removed, User remover){
        UserMongoNeo4jManager umn = new UserMongoNeo4jManager();
        if(remover.getRole().equals("Admin"))
            umn.removeUser(removed.getUsername());
    }

    public static void changeUserRole(User admin, User changed){
        if(admin.getRole().equals("Admin")) {
            Scanner sc = new Scanner(System.in);
            UserMongoNeo4jManager umn = new UserMongoNeo4jManager();
            System.out.println("Please enter the new role between {Normal User,Developer,Admin}");
            Role.RoleValue role;
            try {
                role = Role.getRoleFromString(sc.nextLine());
            } catch (RuntimeException r) {
                System.out.println("Insert one of this {Normal User,Developer,Admin}");
                return;
            }
            umn.changeUserRole(changed.getUsername(), role);
        }
    }

    public static void viewApp(User myUser, List<App> foundApps){
        int i = 0;
        if(foundApps == null) {
            System.out.println("No app found");
            return;
        }
        for (App a: foundApps){
            System.out.println(i + " " + a.getName());
            i++;
        }
        Scanner sc = new Scanner(System.in);
        String ans;
        do{
            System.out.println("Write the app number to show app // write break to go back");
            ans = sc.nextLine();
            try {
                if(ans.equals("break"))
                    return;
                int appIndex = Integer.parseInt(ans);
                if(appIndex < 0 || appIndex >= foundApps.size()) {
                    throw new NumberFormatException();
                }
                else {
                    App selectedApp = foundApps.get(appIndex);
                    selectedApp.printAppInformation();
                    System.out.println("To Follow App(1), To modify App(2), To delete app(3)");
                    ans = sc.nextLine();
                    if(ans.equals("1"))
                        askToFollowUnfollowBrowsePostsOrWriteReview(myUser, selectedApp);
                    else if(ans.equals("2"))
                        updateApp(myUser, selectedApp);
                    else if(ans.equals("3"))
                        deleteApp(myUser, selectedApp);
                }
            }
            catch (NumberFormatException | ParseException n){
                System.out.println("Please insert the correct number");
            }
        }while(true);
    }


    public static void askToFollowUnfollowBrowsePostsOrWriteReview(User myUser, App app) throws ParseException {
        String print;
        Set<App> followedApp = browseFollowedApps(myUser);
        Set<String> appIds = null;
        for (App a: followedApp){
            assert false;
            appIds.add(a.getId());
        }


        assert false;
        if(appIds.contains(app.getId()))
            print = "Write 0 to browse posts, 1 to unfollow 2 to write review 3 to edit/add a score 4 to go back";
        else
            print =  "Write 0 to browse posts, 1 to follow 2 to write review 3 to edit/add a score 4 to go back";
        System.out.println(print);
        Scanner sc = new Scanner(System.in);
        String ans = sc.nextLine();
        switch (ans){
            case "0":
                searchAppReviews(app.getId());
                break;
            case "1":
                AppNeo4jManager neo = new AppNeo4jManager();
                if(followedApp.contains(app))
                    neo.unfollowApp(myUser, app);
                else
                    neo.followOrDevelopApp(myUser, app, Relation.RelationType.FOLLOW);
                break;
            case "2":
                writeReview(myUser, app);
            case"3":
                addEditScore(myUser, app);
            default:
                break;
        }
    }
    public static void writeReview(User myUser, App app){
        System.out.println("Write the review content");
        Scanner sc = new Scanner(System.in);
        Review review = new Review();
        review.setUsername(myUser.getUsername());
        review.setAppId(app.getId());
        String content = sc.nextLine();
        review.setContent(content);
        review.setDate(new Date());
        ReviewMongoManager rm = new ReviewMongoManager();
        rm.postNewReview(review);
    }

    public static void addEditScore(User myUser, App app){
        ReviewMongoManager rm = new ReviewMongoManager();
        Review reviewScore = rm.showScore(myUser.getUsername(), app.getId());
        if(reviewScore == null){
            System.out.println("You never scored this app, add a score");
            Scanner sc = new Scanner(System.in);
            Review review = new Review();
            review.setUsername(myUser.getUsername());
            review.setAppId(app.getId());
            System.out.println("Write the review content");
            String content = sc.nextLine();
            review.setContent(content);
            System.out.println("Write the review score");
            double score = writeScore();
            review.setScore(score);
            review.setDate(new Date());
            rm.postNewReview(review);
        }
        else{
            System.out.println("Your review when you scored this app is:");
            reviewScore.printReviewInformation();
            System.out.println("Do you want to edit your past score? y/n");
            Scanner sc = new Scanner(System.in);
            String ans = sc.nextLine();
            while(true){
                if(ans.equals("n"))
                    return;
                else if(ans.equals("y")){
                    System.out.println("Write your new score");
                    double score = writeScore();
                    rm.updateReviewScore(reviewScore, score);
                    System.out.println("Do you want to edit your past content? y/n");
                    ans = sc.nextLine();
                    if(ans.equals("n"))
                        return;
                    else if(ans.equals("y")){
                        System.out.println("Write your new content");
                        String content = sc.nextLine();
                        rm.updateReviewContent(reviewScore, content);
                        return;
                    }
                    else{
                        System.out.println("invalid input");
                    }
                }
                else{
                    System.out.println("invalid input");
                }
            }
        }

    }

    public static double writeScore(){
        Scanner sc = new Scanner(System.in);
        double score;
        while(true){
            String scoreString = sc.nextLine();
            try {
                score = Double.parseDouble(scoreString);
                if(score<0 || score > 5)
                    throw new NumberFormatException();
            }
            catch (NumberFormatException n){
                System.out.println("Please insert a valid score");
                continue;
            }
            return score;
        }
    }

    public static User logout(){
        return null;
    }


    public static void main(String[] args) throws ParseException {

        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                User myUser = new User();
                System.out.println("Welcome to softgram");
                System.out.println("To sign up press 1");
                System.out.println("To log in press 2");
                System.out.println("To quit press 3");

                String answer = sc.nextLine();
                switch (answer){
                    case"1":
                        signup();
                        break;
                    case "2":
                        myUser = login();
                        break;
                    default:
                        break;
                }
                while (myUser!=null){
                    UserNeo4jManager getRequests = new UserNeo4jManager();
                    System.out.println("To search for an app, press 1");
                    System.out.println("To browse suggested apps, press 2");
                    System.out.println("To browse followed apps, press 3");
                    System.out.println("To browse popular apps, press 4");
                    System.out.println("To search for a review, press 5");
                    System.out.println("To search for a user, press 6");
                    System.out.println("To browse followed users, press 7");
                    System.out.println("To browse suggested users, press 8");
                    System.out.println("To browse followers, press 9");
                    System.out.println("To modify your profile, press 0");

                    System.out.println("To add an app, press 11");
                    System.out.println("To monitor the application, press 12");
                    System.out.println("To check requests, press 13");

                    System.out.println("To logout, press #");

                    String ans = sc.nextLine();
                    switch (ans){
                        case"1":
                            searchApps(myUser);
                            break;
                        case "2":
                            browseSuggestedApps(myUser);
                            break;
                        case"3":
                            browseFollowedApps(myUser);
                            break;
                        case "4":
                            browsePopularApps(myUser);
                            break;
                        case"5":
                            searchReview(myUser);
                            break;
                        case "6":
                            searchUsers(myUser);
                            break;
                        case"7":
                            browseFollowedUsers(myUser);
                            break;
                        case "8":
                            browseSuggestedUsers(myUser);
                            break;
                        case"9":
                            browseFollowers(myUser);
                            break;
                        case "0":
                            modifyProfile(myUser);
                            break;
                        case"11":
                            addApp(myUser);
                            break;
                        case"12":
                            monitorApp(myUser);
                            break;
                        case"13":
                            acceptRejectFollows(myUser, getRequests.browseFollowRequests(myUser.getUsername()));
                            break;
                        case"#":
                            myUser = logout();
                            break;
                        default:
                            break;
                    }
                }



                if (answer.equals("3")){
                    Neo4jDriver.close();
                    MongoDriver.close();
                    break;
                }
            }
        }
    }
}
