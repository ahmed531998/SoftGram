import it.unipi.softgram.controller.neo4j.UserNeo4jManager;
import it.unipi.softgram.entities.User;


import java.util.List;
import java.util.Scanner;

public class andrea {
    public String viewUser(List<User> foundUsers){
        int i = 0;
        if(foundUsers == null) {
            System.out.println("No user found");
            return null;
        }
        for (User u: foundUsers){
            System.out.println(i + " " + u.getUsername());
            i++;
        }
        Scanner sc = new Scanner(System.in);
        String ans;
        do{
            System.out.println("Write the user number to show that user profile // write beak to go back");
            ans = sc.nextLine();
            try {
                if(ans.equals("break"))
                    return null;
                int userIndex = Integer.parseInt(ans);
                if(userIndex < 0 || userIndex >= foundUsers.size()) {
                    throw new NumberFormatException();
                }
                else {
                    foundUsers.get(userIndex).printUserInformation();
                    return foundUsers.get(userIndex).getUsername();
                }
            }
            catch (NumberFormatException n){
                System.out.println("Please insert the correct number");
            }
        }while(true);
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
}
