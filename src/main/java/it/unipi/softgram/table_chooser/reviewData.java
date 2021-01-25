package it.unipi.softgram.table_chooser;

public class reviewData {
    Object _id;
    String content;
    double score;
    String username;
    String appid;

    public reviewData(Object _id, String content, double score, String username, String appid) {
        this._id = _id;
        this.content = content;
        this.score = score;
        this.username = username;
        this.appid = appid;
    }

    public Object get_id() {
        return _id;
    }

    public void set_id(Object _id) {
        this._id = _id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
}
