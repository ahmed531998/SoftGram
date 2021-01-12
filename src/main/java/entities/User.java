package entities;

import java.util.*;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import utilities.MongoDriver;

public class User {
    private String username;
    private Date birthday;
    private String country;
    private String password;
    private String email;
    private String role;
    private String website;

    List<Review> reviews;

    List<String> followersList;
    List<String> followingList;

    List<String> developedApps;

    public void setUsername(String username) {
        this.username = username;
    }

    private void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    private void setCountry(String country) {
        this.country = country;
    }

    private void setEmail(String email){
        this.email = email;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setWebsite(String website){}

    public String getUsername() {
        return username;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getCountry() {
        return country;
    }

    public String getEmail(){
        return email;
    }

    public String getWebsite(){
        return website;
    }

    public String getPassword() {
        return password;
    }


    public Document toUserDocument(){
        List<Document> reviewDocList = new ArrayList<>();
        for (Review review: this.reviews){
            Document reviewDoc = new Document("review", new Document("content", review.getContent())
                    .append("date", review.getDateOfReview())
                    .append("appId", review.getAppId()))
                    .append("score", review.getScore())
                    .append("scoreDate", review.getDateOfScore());
            reviewDocList.add(reviewDoc);
        }

        return new Document("_id", this.username)
                .append("birthday", this.birthday)
                .append("email", this.email)
                .append("website", this.website)
                .append("role", this.role)
                .append("password", this.password)
                .append("country", this.country)
                .append("reviews", reviewDocList);
    }

    public User fromUserDocument(Document r){
        this.username = (String) r.get("_id");
        this.birthday = (Date) r.get("birthday");
        this.email = (String) r.get("email");
        this.website = (String) r.get("website");
        this.role = (String) r.get("role");
        this.password = (String) r.get("password");
        this.country = (String) r.get("country");
        //check this warning (by andrea)
        List<Document> reviewsDocList = (List<Document>) r.get("reviews");
        List<Review> reviews = new ArrayList<>();
        for (Document review: reviewsDocList){
            Review x = new Review();
            reviews.add(x.fromUserCollDocument(review, this.username));
        }
        this.reviews = reviews;
        return this;
    }

    public void addUser(String username, String email, String password, String website, String role, String country) {
        try {
            MongoDriver driver = new MongoDriver();
            MongoCollection<Document> appColl = driver.getCollection("users");
            Document d1 = new Document("_id", getUsername());
            d1.append("_id", username);
            d1.append("email", email);
            d1.append("password",password);
            d1.append("website", website);
            d1.append("role", role);
            d1.append("country", country);
            //d1.append("birthdate", birth);
            appColl.insertOne(d1);
            System.out.println("added");
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

}
