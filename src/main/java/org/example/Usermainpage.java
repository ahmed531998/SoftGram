package org.example;

import com.mongodb.client.MongoCollection;
import controller.mongo.UserMongoManager;
import controller.neo4j.UserNeo4jManager;
import entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.bson.BsonRegularExpression;
import org.bson.Document;
import org.bson.conversions.Bson;
import table_chooser.Userdata;
import utilities.MongoDriver;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public class Usermainpage implements Initializable {

    //@FXML private Label AppID;
    @FXML private Text name_tfield, email_tfield, web_tfield, role_tfield;
    String app_id;
    @FXML Button followbtn,home;
    User user=new User();

    public void transferMessage(String message) {
       // AppID.setText(message);

        user.setUsername(message);
        app_id=message;
        findUsers(message);
    }
    public void findUsers(String text){
        MongoDriver driver = new MongoDriver();
        MongoCollection<Document> collection = driver.getCollection("users");

        // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

        Consumer<Document> processBlock = new Consumer<Document>() {
            @Override
            public void accept(Document document) {
                //System.out.println(document);
                String username = (String) document.get("_id");
                String website = (String) document.get("website");
                String email = (String) document.get("email");
                String role = (String) document.get("role");
                name_tfield.setText(username);
                email_tfield.setText(email);
                role_tfield.setText(role);
                web_tfield.setText(website);



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


    public void homepage(ActionEvent actionEvent) {
        Stage stage = (Stage) home.getScene().getWindow();
        // do what you have to do
        stage.close();
        try {
            App.setRoot("users");
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updatefun(ActionEvent actionEvent) {


        MongoDriver driver = new MongoDriver();
        MongoCollection<Document> collection = driver.getCollection("users");
        String _id = name_tfield.getText().toString();
        String email = email_tfield.getText().toString();
        String role = role_tfield.getText().toString();
        String website = web_tfield.getText().toString();

        Stage newStage = new Stage();
        VBox comp = new VBox();
        javafx.scene.control.TextField username = new javafx.scene.control.TextField(_id);
        javafx.scene.control.TextField emailtxt = new javafx.scene.control.TextField(email);
        javafx.scene.control.TextField roletxt = new javafx.scene.control.TextField(role);
        javafx.scene.control.TextField websitetxt = new javafx.scene.control.TextField(website);
        javafx.scene.control.Button update = new Button("Update");
        update.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Document query = new Document();

                query.append("_id", user.getUsername());
                Document setData = new Document();
                setData.append("_id", username.getText().toString())
                        .append("role", roletxt.getText().toString())
                        .append("email", emailtxt.getText().toString())
                        .append("website", websitetxt.getText().toString())
                ;
                Document update = new Document();
                update.append("$set", setData);
                //To update single Document
                collection.updateOne(query, update);
                JOptionPane.showMessageDialog(null, "Updated Successfully");

                name_tfield.setText("");
                role_tfield.setText("");
                email_tfield.setText("");
                web_tfield.setText("");
                name_tfield.setText(username.getText().toString());
                role_tfield.setText(websitetxt.getText().toString());
                email_tfield.setText(emailtxt.getText().toString());
                web_tfield.setText(websitetxt.getText().toString());

            }
        });

        comp.getChildren().add(username);
        comp.getChildren().add(emailtxt);
        comp.getChildren().add(roletxt);
        comp.getChildren().add(websitetxt);
        comp.getChildren().add(update);
        Scene stageScene = new Scene(comp, 300, 300);
        newStage.setScene(stageScene);
        newStage.show();

    }

    public void removefun(ActionEvent actionEvent) {
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(null, "Would You Like to remove this user?", "Removing", dialogButton);

        if (dialogResult == JOptionPane.YES_OPTION) {
            // Saving code here
            UserMongoManager usermongo=new UserMongoManager();

            usermongo.removeUser(user.getUsername());
            JOptionPane.showMessageDialog(null, "User removed successfully");
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

        }
    }

    public void followfun(ActionEvent actionEvent) {
        System.out.println(user.getUsername());
        //neo4j
        /*UserNeo4jManager useneo=new UserNeo4jManager();
        useneo.addFollow(user.getUsername(), user.getUsername(), false);
        JOptionPane.showMessageDialog(null, "You followed this user");*/
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
