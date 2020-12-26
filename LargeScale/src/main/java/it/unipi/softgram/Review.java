package it.unipi.softgram;

public class Review {
    private String appId;
    private String userId;
    private String dateOfScore;
    private String dateOfReview;
    private String content;
    private double score;

    //constructors
    public Review(){
        this.appId = "";
        this.userId = "";
        this.dateOfScore = "";
        this.dateOfReview = "";
        this.content = "";
        this.score = 0;
    }
    public Review(String appId, String userId, String dateOfScore, String dateOfReview, String content,
                  double score){
        this.appId = appId;
        this.userId = userId;
        this.dateOfScore = dateOfScore;
        this.dateOfReview = dateOfReview;
        this.content = content;
        this.score = score;
    }

    //setters
    public void setAppId(String appId) {
        this.appId = appId;
    }
    public void setContent(String content) { this.content = content; }
    public void setDateOfReview(String dateOfReview) {
        this.dateOfReview = dateOfReview;
    }
    public void setDateOfScore(String dateOfScore) {
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
    public String getDateOfReview() {
        return dateOfReview;
    }
    public String getDateOfScore() {
        return dateOfScore;
    }
    public String getUserId() {
        return userId;
    }

}
