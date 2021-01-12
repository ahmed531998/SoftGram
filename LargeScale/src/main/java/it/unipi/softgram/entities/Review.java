package it.unipi.softgram.entities;

import org.bson.Document;

import java.util.Date;

public class Review {
    private String appId;
    private String userId;
    private Date dateOfScore;
    private Date dateOfReview;
    private String content;
    private double score;

    //constructors
    public Review(){
        this.appId = "";
        this.userId = "";
        this.dateOfScore = null;
        this.dateOfReview = null;
        this.content = "";
        this.score = 0;
    }
    public Review(String appId, String userId, Date dateOfScore, Date dateOfReview, String content,
                  double score){
        this.appId = appId;
        this.userId = userId;
        this.dateOfScore = dateOfScore;
        this.dateOfReview = dateOfReview;
        this.content = content;
        this.score = score;
    }

    //setters
    public void setAppId(String appId) { this.appId = appId; }
    public void setContent(String content) { this.content = content; }
    public void setDateOfReview(Date dateOfReview) {
        this.dateOfReview = dateOfReview;
    }
    public void setDateOfScore(Date dateOfScore) {
        this.dateOfScore = dateOfScore;
    }
    public void setScore(double score) {
        this.score = score;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    //getters
    public double getScore() { return score; }
    public String getAppId() {
        return appId;
    }
    public String getContent() { return content; }
    public Date getDateOfReview() {
        return dateOfReview;
    }
    public Date getDateOfScore() {
        return dateOfScore;
    }
    public String getUserId() {
        return userId;
    }

    public Document toAppCollDocument(){
        Document reviewAppDoc = new Document("username", this.userId)
                .append("score", this.score)
                .append("scoreDate", this.dateOfScore)
                .append("review", new Document("content", this.content)
                        .append("date", this.dateOfReview));
        return reviewAppDoc;
    }

    public Document toUserCollDocument(){
        Document reviewUserDoc = new Document("appId", this.appId)
                .append("score", this.score)
                .append("scoreDate", this.dateOfScore)
                .append("review", new Document("content", this.content)
                        .append("date", this.dateOfReview));
        return reviewUserDoc;
    }

    public Review fromUserCollDocument(Document r, String username){
        this.appId = (String) r.get("appId");
        this.userId = username;
        this.dateOfScore = (Date) r.get("scoreDate");
        Document reviewDoc = (Document) r.get("review");
        this.dateOfReview = (Date) reviewDoc.get("date");
        this.content = (String) reviewDoc.get("content");
        this.score = (double) r.get("score");
        return this;
    }

    public Review fromAppCollDocument(Document r, String appId){
        this.appId = appId;
        this.userId = (String) r.get("username");
        this.dateOfScore = (Date) r.get("scoreDate");
        Document reviewDoc = (Document) r.get("review");
        this.dateOfReview = (Date) reviewDoc.get("date");
        this.content = (String) reviewDoc.get("content");
        this.score = (double) r.get("score");
        return this;
    }
}
