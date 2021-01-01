package it.unipi.softgram.entities;

import org.bson.Document;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class App {
    private String id;
    private Boolean adSupported;
    private Date released;
    private String name;
    private Double price;
    private String category;
    private int ratingCount;
    private int installCount;
    private String size;
    private String ageGroup;
    private String currency;
    private Date lastUpdated;
    private User developer;
    private boolean inAppPurchase;

    private List<Review> reviews;
    private List<User>followers;

    public App() {

    }

    public App(String id, Boolean adSupported, Date released, String name, Double price,
               String category, int ratingCount, int installCount, String size,
               String ageGroup, String currency, Date lastUpdated, User developer,
               Boolean inAppPurchase, List<Review> reviews){
        this.name = name;
        this.category = category;
        this.adSupported = adSupported;
        this.id = id;
        this.released = released;
        this.price = price;
        this.ratingCount = ratingCount;
        this.installCount = installCount;
        this.size = size;
        this.ageGroup = ageGroup;
        this.currency = currency;
        this.lastUpdated = lastUpdated;
        this.developer = developer;
        this.inAppPurchase = inAppPurchase;
        this.reviews = reviews;
    }
    public boolean isInAppPurchase() { return inAppPurchase; }

    public Boolean getAdSupported() { return adSupported; }

    public String getId() { return id; }

    public String getName() { return name; }

    public Double getPrice() { return price; }

    public int getInstallCount() { return installCount; }

    public int getRatingCount() { return ratingCount; }

    public Date getReleased() { return released; }

    public String getAgeGroup() { return ageGroup; }

    public String getCurrency() { return currency; }

    public String getSize() { return size; }

    public Date getLastUpdated() { return lastUpdated; }

    public List<Review> getReviews() { return reviews; }

    public String getCategory() { return category; }

    public User getDeveloper() { return developer; }

    public void setId(String id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public void setInstallCount(int installCount) { this.installCount = installCount; }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setInAppPurchase(boolean inAppPurchase) { this.inAppPurchase = inAppPurchase; }

    public void setReviews(List<Review> reviews) { this.reviews = reviews; }

    public void setReleased(Date released) { this.released = released; }

    public void setPrice(Double price) { this.price = price; }

    public void setRatingCount(int ratingCount) { this.ratingCount = ratingCount; }

    public void setLastUpdated(Date lastUpdated) { this.lastUpdated = lastUpdated; }

    public void setAdSupported(Boolean adSupported) { this.adSupported = adSupported; }

    public void setAgeGroup(String ageGroup) { this.ageGroup = ageGroup; }

    public void setCurrency(String currency) { this.currency = currency; }

    public void setSize(String size) { this.size = size; }

    public void setDeveloper(User developer) { this.developer = developer; }

    public Document toAppDocument(){
        List<Document> reviewDocList = new ArrayList<>();
        for (Review review: this.reviews){
            reviewDocList.add(review.toAppCollDocument());
        }

        Document appDoc = new Document("_id", this.id)
                .append("adSupported", this.adSupported)
                .append("released", this.released)
                .append("name", this.name)
                .append("price", this.price)
                .append("ratingCount", this.ratingCount)
                .append("installCount", this.installCount)
                .append("size", this.size)
                .append("ageGroup", this.ageGroup)
                .append("currency", this.currency)
                .append("lastUpdated", this.lastUpdated)
                .append("developer", new Document("developerId", this.developer.getUsername())
                        .append("developerEmail", this.developer.getEmail())
                        .append("developerWebsite", this.developer.getWebsite()))
                .append("inAppPurchase", this.inAppPurchase)
                .append("reviews", reviewDocList);
        return appDoc;
    }

    public App fromAppDocument(Document r){
        this.id = (String) r.get("_id");
        this.adSupported = (Boolean) r.get("adSupported");
        this.released = (Date) r.get("released");
        this.name = (String) r.get("name");
        this.price = (double) r.get("price");
        this.ratingCount = (int) r.get("ratingCount");
        this.installCount = (int) r.get("installCount");
        this.size = (String) r.get("size");
        this.ageGroup = (String) r.get("ageGroup");
        this.currency = (String) r.get("currency");
        this.lastUpdated = (Date) r.get("lastUpdated");
        Document d = (Document) r.get("developer");
        User x = new User();
        this.developer = (x.fromUserDocument(d));
        this.inAppPurchase = (Boolean) r.get("inAppPurchase");

        List<Document> reviewsDocList = (List<Document>) r.get("reviews");
        List<Review> reviews = new ArrayList<>();
        for (Document review: reviewsDocList){
            Review y = new Review();
            reviews.add(y.fromAppCollDocument(review, this.id));
        }
        this.reviews = reviews;
        return this;
    }

}
