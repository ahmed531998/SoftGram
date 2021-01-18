package it.unipi.softgram.entities;

import java.util.*;
import org.bson.Document;

public class User {
    private String username;
    private Date birthday;
    private String country;
    private String password;
    private String email;
    private String role;
    private String website;


    public List<String> followersList;
    public List<String> followingList;

    public List<String> developedApps;

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

    public String getRole(){
        return role;
    }

    public User fromUserDocument(Document r){
        this.username = (String) r.get("_id");
        this.birthday = (Date) r.get("birthday");
        this.email = (String) r.get("email");
        this.website = (String) r.get("website");
        this.role = (String) r.get("role");
        this.password = (String) r.get("password");
        this.country = (String) r.get("country");
        return this;
    }

}
