package it.unipi.softgram.table_chooser;


import com.mongodb.client.MongoCollection;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import org.bson.Document;
import it.unipi.softgram.utilities.drivers.MongoDriver;

import javax.swing.*;
import java.awt.*;

public class AppData {
    private String name;
    private String _id;
    private double adSupported;
    private String released;
    private double price;
    private String category;
    private String ageGroup;
    private String lastUpdated;
    private boolean inAppPurchase;
    private double Avg;
    private int numberOfReviews;


    public AppData(String name, String _id, double adSupported, String released, double price, String category,
                   String ageGroup, String lastUpdated, boolean inAppPurchase, int numberOfReviews, double Avg) {
        this.name = name;
        this._id = _id;
        this.adSupported = adSupported;
        this.released = released;
        this.price = price;
        this.category = category;
        this.ageGroup = ageGroup;
        this.lastUpdated = lastUpdated;
        this.inAppPurchase = inAppPurchase;
        this.numberOfReviews=numberOfReviews;
        this.Avg=Avg;

    }

    public double getAvg() {
        return Avg;
    }

    public void setAvg(double avg) {
        Avg = avg;
    }

    public int getNumberOfReviews() {
        return numberOfReviews;
    }

    public void setNumberOfReviews(int numberOfReviews) {
        this.numberOfReviews = numberOfReviews;
    }

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

    public double getAdSupported() {
        return adSupported;
    }

    public void setAdSupported(double adSupported) {
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

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
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




}
