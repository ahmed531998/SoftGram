import it.unipi.softgram.controller.mongo.ReviewMongoManager;
import it.unipi.softgram.controller.mongoneo4j.UserMongoNeo4jManager;
import it.unipi.softgram.controller.neo4j.AppNeo4jManager;
import it.unipi.softgram.controller.neo4j.UserNeo4jManager;
import it.unipi.softgram.entities.App;
import it.unipi.softgram.entities.Review;
import it.unipi.softgram.entities.User;
import it.unipi.softgram.utilities.drivers.Neo4jDriver;
import it.unipi.softgram.utilities.enumerators.Relation;
import it.unipi.softgram.utilities.enumerators.Role;


import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class andrea {
    public void viewUser(List<User> foundUsers){
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
                    foundUsers.get(userIndex).printUserInformation();
                    askToFollowUnfollowOrBrowsePosts(foundUsers.get(userIndex));
                }
            }
            catch (NumberFormatException n){
                System.out.println("Please insert the correct number");
            }
        }while(true);
    }

    public void askToFollowUnfollowOrBrowsePosts(User user){
        String print;
        if(followedUsers.contains(user))
            print = "Write 0 to browse posts, 1 to unfollow 2 to go back";
        else
            print =  "Write 0 to browse posts, 1 to follow 2 to go back"
        System.out.println(print);
        Scanner sc = new Scanner(System.in);
        String ans = sc.nextLine();
        switch (ans){
            case "0":
                browsePosts(user);
                break;
            case "1":
                UserNeo4jManager neo = new UserNeo4jManager();
                if(followedUsers.contains(user))
                    neo.removeFollow(this.username,user.getUsername());
                else
                    neo.addFollow(this.username, user.getUsername(), true);
                break;
            default:
                break;
        }
    }

    public void acceptRejectFollows(List<User> followRequests){
        int i = 0;
        if(followRequests == null) {
            System.out.println("No follow requests");
            return;
        }
        for (User u: followRequests){
            System.out.println(i + " " + u.getUsername());
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
                           neo.removeFollow(followRequests.get(userIndex).getUsername(),this.username);
                           System.out.println("Follow rejected");
                           break;
                       case "1":
                           neo.addFollow(followRequests.get(userIndex).getUsername(),this.username,false);
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

    public void removeUser(){
        System.out.println("Please write the username of the user to remove");
        Scanner sc = new Scanner(System.in);
        String username = sc.nextLine();
        UserMongoNeo4jManager umn = new UserMongoNeo4jManager();
        umn.removeUser(username);
    }

    public void changeUserRole(){
        System.out.println("Please write the username of the user to change his role");
        Scanner sc = new Scanner(System.in);
        String username = sc.nextLine();
        UserMongoNeo4jManager umn = new UserMongoNeo4jManager();
        System.out.println("Please enter the new role between {Normal User,Developer,Admin}");
        try {
            Role.RoleValue role = Role.getRoleFromString(sc.nextLine());
        }
        catch (RuntimeException r) {
            System.out.println("Insert one of this {Normal User,Developer,Admin}");
            return;
        }
        umn.changeUserRole(username, role);
    }

    public void viewApp(List<App> foundApps){
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
                    foundApps.get(appIndex).printAppInformation();
                    askToFollowUnfollowBrowsePostsOrWriteReview(foundApps.get(appIndex));
                }
            }
            catch (NumberFormatException n){
                System.out.println("Please insert the correct number");
            }
        }while(true);
    }


    public void askToFollowUnfollowBrowsePostsOrWriteReview(App app){
        String print;
        if(followedApp.contains(app))
            print = "Write 0 to browse posts, 1 to unfollow 2 to write review 3 to edit/add a score 4 to go back";
        else
            print =  "Write 0 to browse posts, 1 to follow 2 to write review 3 to edit/add a score 4 to go back";
        System.out.println(print);
        Scanner sc = new Scanner(System.in);
        String ans = sc.nextLine();
        switch (ans){
            case "0":
                browsePosts(app);
                break;
            case "1":
                AppNeo4jManager neo = new AppNeo4jManager();
                if(followedApp.contains(app))
                    neo.unfollowApp(this.username, app);
                else
                    neo.followOrDevelopApp(this.username, app, Relation.RelationType.FOLLOW);
                break;
            case "2":
                writeReview(app);
            case"3":
                addEditScore(app);
            default:
                break;
        }
    }
    public void writeReview(App app){
        System.out.println("Write the review content");
        Scanner sc = new Scanner(System.in);
        Review review = new Review();
        review.setUsername(this.username);
        review.setAppId(app.getId());
        String content = sc.nextLine();
        review.setContent(content);
        review.setDate(new Date());
        ReviewMongoManager rm = new ReviewMongoManager();
        rm.postNewReview(review);
    }

    public void addEditScore(App app){
        ReviewMongoManager rm = new ReviewMongoManager();
        Review reviewScore = rm.showScore(this.username, app.getId());
        if(reviewScore == null){
            System.out.println("You never scored this app, add a score");
            Scanner sc = new Scanner(System.in);
            Review review = new Review();
            review.setUsername(this.username);
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

    public double writeScore(){
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
}

