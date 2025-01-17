package it.unipi.softgram.entities;


import org.bson.Document;

import java.util.Date;

public class App {
    private String id;
    private Boolean adSupported;
    private Date released;
    private String name;
    private Double price;
    private String category;
    private String size;
    private String ageGroup;
    private String currency;
    private Date lastUpdated;
    //private User developer;
    private Boolean inAppPurchase;

    private Double average;
    private Integer reviewCount;
    private Integer yearOfInterest;

    public App() {

    }

    public App(String id, Boolean adSupported, Date released, String name, Double price,
               String category, String size,
               String ageGroup, String currency, Date lastUpdated,
               Boolean inAppPurchase, Double average, Integer reviewCount, Integer yearOfInterest){
        this.name = name;
        this.category = category;
        this.adSupported = adSupported;
        this.id = id;
        this.released = released;
        this.price = price;
        this.size = size;
        this.ageGroup = ageGroup;
        this.currency = currency;
        this.lastUpdated = lastUpdated;
        //this.developer = developer;
        this.inAppPurchase = inAppPurchase;
        this.average = average;
        this.reviewCount = reviewCount;
        this.yearOfInterest = yearOfInterest;
    }
    public boolean getInAppPurchase() { return inAppPurchase; }

    public Boolean getAdSupported() { return adSupported; }

    public String getId() { return id; }

    public String getName() { return name; }

    public Double getPrice() { return price; }

    public Date getReleased() { return released; }

    public String getAgeGroup() { return ageGroup; }

    public String getCurrency() { return currency; }

    public String getSize() { return size; }

    public Date getLastUpdated() { return lastUpdated; }

    public String getCategory() { return category; }

    public Double getAverage() {
        return average;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public Integer getYearOfInterest() {
        return yearOfInterest;
    }

    public void setId(String id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setInAppPurchase(boolean inAppPurchase) { this.inAppPurchase = inAppPurchase; }

    public void setReleased(Date released) { this.released = released; }

    public void setPrice(Double price) { this.price = price; }

    public void setLastUpdated(Date lastUpdated) { this.lastUpdated = lastUpdated; }

    public void setAdSupported(Boolean adSupported) { this.adSupported = adSupported; }

    public void setAgeGroup(String ageGroup) { this.ageGroup = ageGroup; }

    public void setCurrency(String currency) { this.currency = currency; }

    public void setSize(String size) { this.size = size; }

    public void setInAppPurchase(Boolean inAppPurchase) {
        this.inAppPurchase = inAppPurchase;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public void setYearOfInterest(Integer yearOfInterest) {
        this.yearOfInterest = yearOfInterest;
    }

    public Document toAppDocument(){
        Document appDoc = new Document("_id", this.getId());
        if(this.adSupported!=null)
            appDoc.append("adSupported", this.adSupported);
        if(this.released!=null)
            appDoc.append("released", this.released);
        if(this.name!=null)
            appDoc.append("name", this.name);
        if(this.price!=null)
            appDoc.append("price", this.price);

        if(this.size!=null)
            appDoc.append("size", this.size);
        if(this.ageGroup!=null)
            appDoc.append("ageGroup", this.ageGroup);
        if(this.currency!=null)
            appDoc.append("currency", this.currency);
        if(this.lastUpdated!=null)
            appDoc.append("lastUpdated", this.lastUpdated);

//        Document devDoc;
//        if(this.developer!=null) {
//            devDoc = new Document("developerId", this.getDeveloper().getUsername());
//            if (this.developer.getEmail() != null)
//                devDoc.append("developerEmail", this.developer.getEmail());
//            if (this.developer.getWebsite() != null)
//                devDoc.append("developerWebsite", this.developer.getWebsite());
//            appDoc.append("developer", devDoc);
//        }
        if (this.category!=null)
            appDoc.append("category", this.category);
        if (this.inAppPurchase != null)
          appDoc.append("inAppPurchase", this.inAppPurchase);

        return appDoc;
    }

    public App fromAppDocument(Document r){
        this.id = (String) r.get("_id");
        Boolean x = r.get("adSupported")!=null?
                ((Double)r.get("adSupported") == 0.0? false : true):null;
        this.adSupported = x;
        this.released = (Date) r.get("released");
        this.name = (String) r.get("name");
        this.price = (double) r.get("price");

        this.size = (String) r.get("size");
        this.ageGroup = (String) r.get("ageGroup");
        this.currency = (String) r.get("currency");
        this.lastUpdated = (Date) r.get("lastUpdated");
//        Document d = (Document) r.get("developer");
//        User x = new User();
//        this.developer = (x.fromUserDocument(d));
         x = r.get("inAppPurchase")!=null?
                ((Double)r.get("inAppPurchase") == 0.0? false : true):null;
        this.inAppPurchase = x;
        this.category = (String) r.get("category");
        return this;
    }

}

