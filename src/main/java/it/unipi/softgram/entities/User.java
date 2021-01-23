package it.unipi.softgram.entities;

import org.bson.Document;

import java.util.Date;

public class User {
    private String username;
    private Date birthday;
    private String country;
    private String password;
    private String email;
    private String role;
    private String website;

    public void setRole(String role) {
        this.role = role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setWebsite(String website){this.website = website;}

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

    public Document toUserDocument(){
        Document userDoc = new Document("_id", this.getUsername());
        if(this.getBirthday()!=null)
            userDoc.append("birthday", this.getBirthday());
        if(this.getEmail()!=null)
            userDoc.append("email", this.getEmail());

        userDoc.append("password", this.getPassword());
        userDoc.append("role", "Normal User");

        if(this.getCountry()!=null)
            userDoc.append("Country", this.getCountry());
        return userDoc;
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
