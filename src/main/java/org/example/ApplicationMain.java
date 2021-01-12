package org.example;

import com.mongodb.client.MongoCollection;
import entities.Apps;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.bson.BsonRegularExpression;
import org.bson.Document;
import org.bson.conversions.Bson;
import utilities.MongoDriver;


import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class ApplicationMain  implements Initializable {
    @FXML
    private Label appname,appid,released, price, currency,agegroup,currency1,size;

    @FXML private Text developerid;
    @FXML
    private TextArea name_txt,released_txt,last_txt,developeremail,developerweb;
    @FXML private Button home;
    String app_id;
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
                System.out.println(document);
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
                System.out.println(developeremail);
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
}
