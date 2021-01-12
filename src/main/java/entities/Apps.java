package entities;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.BsonRegularExpression;
import org.bson.Document;
import org.bson.conversions.Bson;
import utilities.MongoDriver;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class Apps {
    private String name;
    private String _id;
    private boolean adSupported;
    private String released;
    private double price;
    private String category;
    private int ratingCount;
    private int installCount;
    private String size;
    private String ageGroup;
    private String currency;
    private String lastUpdated;
    private boolean inAppPurchase;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public boolean getAdSupported() {
        return adSupported;
    }

    public void setAdSupported(boolean adSupported) {
        this.adSupported = adSupported;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public int getInstallCount() {
        return installCount;
    }

    public void setInstallCount(int installCount) {
        this.installCount = installCount;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public boolean getInAppPurchase() {
        return inAppPurchase;
    }

    public void setInAppPurchase(boolean inAppPurchase) {
        this.inAppPurchase = inAppPurchase;
    }


    public void addApp() {
        try {
            MongoDriver driver = new MongoDriver();
            MongoCollection<Document> appColl = driver.getCollection("apps");
            Document d1 = new Document("_id", get_id());
            d1.append("category", getCategory());
            d1.append("released", getReleased());
            d1.append("ageGroup", getAgeGroup());
            d1.append("size", getSize());
            d1.append("installCount", getInstallCount());
            d1.append("adSupported", getAdSupported());
            d1.append("price", getPrice());
            d1.append("currency", getCurrency());
            d1.append("name", getName());
            d1.append("ratingCount", getRatingCount());
            d1.append("inAppPurchase", getInAppPurchase());
            d1.append("installCount", "101");
            appColl.insertOne(d1);
            System.out.println("added");
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }
    public void updateApp(){
        try {
            MongoDriver driver = new MongoDriver();
            MongoCollection<Document> appColl = driver.getCollection("apps");
            BasicDBObject searchQuery = new BasicDBObject("_id", get_id());

            BasicDBObject updateFields = new BasicDBObject();
            updateFields.append("name", getName());
            updateFields.append("category", getCategory());
            updateFields.append("installCount", getInstallCount());

            BasicDBObject setQuery = new BasicDBObject();
            setQuery.append("$set", updateFields);

            appColl.updateOne(searchQuery, setQuery);
            System.out.println("updated");
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public void deleteApp(){
        try {
            MongoDriver driver = new MongoDriver();
            MongoCollection<Document> appColl = driver.getCollection("apps");
            appColl.deleteOne(Filters.eq("_id", get_id()));
            System.out.println("deleted");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void findApp(String text){
        MongoDriver driver = new MongoDriver();
        MongoCollection<Document> collection = driver.getCollection("apps");

        // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

        Consumer<Document> processBlock = new Consumer<Document>() {
            @Override
            public void accept(Document document) {
                System.out.println(document);
            }
        };

        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$match", new Document()
                                .append("$or", Arrays.asList(
                                        new Document()
                                                .append("_id", new Document()
                                                        .append("$regex", new BsonRegularExpression(text))
                                                ),
                                        new Document()
                                                .append("name", new Document()
                                                        .append("$regex", new BsonRegularExpression(text))
                                                )
                                        )
                                )
                        ),
                new Document()
                .append("$skip", 0.0),
                new Document()
                        .append("$limit", 100.0)
        );

        collection.aggregate(pipeline)
                .allowDiskUse(false)
                .forEach(processBlock);
    }
    public void MostPopularApps(){
        MongoDriver driver = new MongoDriver();
        MongoCollection<Document> collection = driver.getCollection("apps");

        // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

        Consumer<Document> processBlock = new Consumer<Document>() {
            @Override
            public void accept(Document document) {
                System.out.println(document);
            }
        };

        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$project", new Document()
                                .append("_id", 1.0)
                                .append("name", 1.0)
                                .append("Avg", new Document()
                                        .append("$avg", "$reviews.score")
                                )
                                .append("numberOfReviews", new Document()
                                        .append("$cond", new Document()
                                                .append("if", new Document()
                                                        .append("$isArray", "$reviews")
                                                )
                                                .append("then", new Document()
                                                        .append("$size", "$reviews")
                                                )
                                                .append("else", 0.0)
                                        )
                                )
                        ),
                new Document()
                        .append("$sort", new Document()
                                .append("Avg", -1.0)
                                .append("numberOfReviews", -1.0)
                        ),
                new Document()
                        .append("$skip", 0.0),
                new Document()
                        .append("$limit", 100.0)
        );

        collection.aggregate(pipeline)
                .allowDiskUse(false)
                .forEach(processBlock);
    }
    public void MostPopularAppsInEachCat(String cat){
        MongoDriver driver = new MongoDriver();
        MongoCollection<Document> collection = driver.getCollection("apps");

        // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

        Consumer<Document> processBlock = new Consumer<Document>() {
            @Override
            public void accept(Document document) {
                System.out.println(document);
            }
        };

        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$match", new Document()
                                .append("category", cat)
                        ),
                new Document()
                        .append("$project", new Document()
                                .append("_id", 1.0)
                                .append("name", 1.0)
                                .append("category", 1.0)
                                .append("Avg", new Document()
                                        .append("$avg", "$reviews.score")
                                )
                                .append("numberOfReviews", new Document()
                                        .append("$cond", new Document()
                                                .append("if", new Document()
                                                        .append("$isArray", "$reviews")
                                                )
                                                .append("then", new Document()
                                                        .append("$size", "$reviews")
                                                )
                                                .append("else", 0.0)
                                        )
                                )
                        ),
                new Document()
                        .append("$sort", new Document()
                                .append("Avg", -1.0)
                                .append("numberOfReviews", -1.0)
                        ),
                new Document()
                        .append("$skip", 0.0),
                new Document()
                        .append("$limit", 100.0)
        );

        collection.aggregate(pipeline)
                .allowDiskUse(false)
                .forEach(processBlock);
    }
    public void MostPopularAppsInEachYear(int year){
        MongoDriver driver = new MongoDriver();
        MongoCollection<Document> collection = driver.getCollection("apps");

        // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

        Consumer<Document> processBlock = new Consumer<Document>() {
            @Override
            public void accept(Document document) {
                System.out.println(document);
            }
        };

        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$project", new Document()
                                .append("_id", 1.0)
                                .append("name", 1.0)
                                .append("category", 1.0)
                                .append("released", 1.0)
                                .append("Avg", new Document()
                                        .append("$avg", "$reviews.score")
                                )
                                .append("numberOfReviews", new Document()
                                        .append("$cond", new Document()
                                                .append("if", new Document()
                                                        .append("$isArray", "$reviews")
                                                )
                                                .append("then", new Document()
                                                        .append("$size", "$reviews")
                                                )
                                                .append("else", 0.0)
                                        )
                                )
                                .append("year", new Document()
                                        .append("$year", "$released")
                                )
                        ),
                new Document()
                        .append("$match", new Document()
                                .append("year", year)
                        ),
                new Document()
                        .append("$sort", new Document()
                                .append("Avg", -1.0)
                                .append("numberOfReviews", -1.0)
                        ),
                new Document()
                        .append("$skip", 0.0),
                new Document()
                        .append("$limit", 100.0)
        );

        collection.aggregate(pipeline)
                .allowDiskUse(false)
                .forEach(processBlock);
    }

}
