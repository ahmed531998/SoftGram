package org.example;

import com.mongodb.client.MongoCollection;
import entities.Apps;
import entities.Review;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.bson.BsonRegularExpression;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import table_chooser.AppData;
import table_chooser.MostPopCat;
import utilities.MongoDriver;


import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;

public class ApplicationMain  implements Initializable {
    @FXML
    private Text appname,appid,released, price, currency,agegroup,currency1,size;

    @FXML private Text developerid;
    @FXML
    private Text name_txt,released_txt,last_txt,developeremail,developerweb;
    @FXML private Button home;
    String app_id;
    @FXML
    ListView<String> list = new ListView<String>();
    ObservableList<Review> Reviews = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

       // findApp(app_id);




    }

    public void transferMessage(String message) {
        //Display the message
        appid.setText(message);
        app_id=message;
        findApp(message);
    }
    public void home_fun(ActionEvent actionEvent) {
        Stage stage = (Stage) home.getScene().getWindow();
        // do what you have to do
        stage.close();
        try {
            App.setRoot("admin");
        } catch (IOException e) {
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
                //System.out.println(document);
                String name = (String) document.get("name");
                System.out.println(name);
                Double price1 = (Double) document.get("price");
                String agegroup1 = (String) document.get("ageGroup");
                String category= (String) document.get("category");
                String released= String.valueOf(document.get("released"));
                String lastupdated= String.valueOf(document.get("lastUpdated"));
                String currency_data= (String)document.get("currency");
                String size_data= (String) document.get("size");

                Document dev= (Document) document.get("developer");
                String developerEmail= (String) dev.get("developerEmail");
                String developerWeb1= (String) dev.get("developerWebsite");
                String developerID= (String) dev.get("developerId");
                currency1.setText(currency_data);
                size.setText(size_data);
                name_txt.setText(name);
                price.setText(String.valueOf(price1));
                currency.setText(category);
                agegroup.setText(agegroup1);
                released_txt.setText(released);
                last_txt.setText(lastupdated);
                developeremail.setText(developerEmail);
                developerweb.setText(developerWeb1);
                developerid.setText(developerID);

                ArrayList review= (ArrayList) document.get("reviews");
                try {
                    ObservableList<String> items = FXCollections.observableArrayList();
                    if(review.isEmpty()) {
                        items.add("No Review");
                        list.setItems(items);
                    }else{
                        int j=0;

                        for (int i=0; i<review.size();i++)
                        {
                            review.get(0);
                            Document reviewdata= (Document)  review.get(0);
                            Document content= (Document) reviewdata.get("review");

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String review_date = sdf.format((Date) content.get("date"));

                            items.add(reviewdata.get("score") +"\n"+ (String) content.get("content") +"\n" + review_date +"\n" + reviewdata.get("username") );
                            if(j >10)  {
                                break;
                            }
                            j++;
                        }
                        list.setItems(items);


                      System.out.println("here");
                    }
                }catch (NullPointerException e){
                    ObservableList<String> items = FXCollections.observableArrayList(
                            "No review");
                    list.setItems(items);
                }

            }
        };

        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$match", new Document()
                                .append("$or", Arrays.asList(
                                        new Document()
                                                .append("_id", new Document()
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

    public void updateApp(ActionEvent actionEvent) {
        MongoDriver driver = new MongoDriver();
        MongoCollection<Document> collection = driver.getCollection("apps");

        double price1 = Double.parseDouble(price.getText().toString());
        String name = name_txt.getText().toString();
        String category1 = currency1.getText().toString();
        String ageGroup = agegroup.getText().toString();
        Stage newStage = new Stage();
        VBox comp = new VBox();
        TextField appname = new TextField(name);
        TextField price11 = new TextField("" + price1);
        TextField category11 = new TextField("" + category1);
        TextField agegroup = new TextField(ageGroup);
        Button update = new Button("Update");
        update.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Document query = new Document();
                query.append("_id", app_id);
                Document setData = new Document();
                setData.append("name", appname.getText().toString())
                        .append("price", Double.parseDouble(price11.getText().toString()))
                        .append("category", category11.getText().toString())
                        .append("ageGroup", agegroup.getText().toString())
                ;
                Document update = new Document();
                update.append("$set", setData);
                //To update single Document
                collection.updateOne(query, update);
                JOptionPane.showMessageDialog(null, "Updated Successfully");


            }
        });

        comp.getChildren().add(appname);
        comp.getChildren().add(price11);
        comp.getChildren().add(category11);
        comp.getChildren().add(agegroup);
        comp.getChildren().add(update);
        Scene stageScene = new Scene(comp, 300, 300);
        newStage.setScene(stageScene);
        newStage.show();


    }

    public void deleteApp(ActionEvent actionEvent) {

        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(null, "Would You Like to remove this application?", "Removing", dialogButton);
        MongoDriver driver = new MongoDriver();
        MongoCollection<Document> collection = driver.getCollection("apps");

        if (dialogResult == JOptionPane.YES_OPTION) {
            // Saving code here
            Document query = new Document();
            query.append("_id", app_id);
            collection.deleteOne(query);
            JOptionPane.showMessageDialog(null, "Removed Successfully");
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

        }
    }
}
