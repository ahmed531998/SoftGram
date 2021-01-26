package it.unipi.softgram.entities;

import org.bson.Document;

import java.util.Date;

public class Review {
    private String appId;
    private String appName;
    private String category;
    private String username;
    private Date date;
    private String content;
    private Double score;
    private Object _id;

    public Review(){
    }

    public Review(String appId, String category, String username, Date date, String content){
        this.appId = appId;
        this.category = category;
        this.username = username;
        this.date = date;
        this.content = content;
        this.score = null;
    }

    public Review(String appId, String category, String username, String content,
                  Double score){
        this.appId = appId;
        this.category = category;
        this.username = username;
        this.content = content;
        this.score = score;
    }
    public Review(String appId, String category, String username, Date date, String content,
                  Double score){
        this.appId = appId;
        this.category = category;
        this.username = username;
        this.date = date;
        this.content = content;
        this.score = score;
    }

    //setters
    public void setAppId(String appId) { this.appId = appId; }
    public void setCategory(String category){ this.category = category; }
    public void setContent(String content) { this.content = content; }
    public void setDate(Date date) {
        this.date = date;
    }
    public void setScore(Double score) {
        this.score = score;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setAppName(String appName) { this.appName = appName;    }

    public void set_id(Object _id) {
        this._id = _id;
    }

    //getters
    public double getScore() { return score; }
    public String getAppId() {
        return appId;
    }
    public String getCategory() { return category; }
    public String getContent() { return content; }
    public Date getDate() {
        return date;
    }
    public String getUsername() {
        return username;
    }
    public Object get_id(){ return  _id;}
    public String getAppName() {return appName; }

    public Document toReviewDocument(){
        Document review = new Document("username", this.username)
                .append("date", this.date)
                .append("content", this.content)
                .append("appId", this.appId)
                .append("appName", this.appName);
        if(this.score != null)
            review.append("score", this.score);
        if(this.category!=null)
            review.append("category", this.category);

        return review;
    }


    public Review fromReviewDocument(Document r){
        this.appId = (String) r.get("appId");
        this.username = (String) r.get("username");
        this.date = (Date) r.get("date");
        this.content = (String) r.get("content");
        this.score = (Double) r.get("score");
        this.category = (String) r.get("category");
        this.appName = (String) r.get("appName");
        this._id = (Object) r.get("_id");
        return this;
    }
}