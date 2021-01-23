package it.unipi.softgram.org.example;

import com.mongodb.client.MongoCollection;
import it.unipi.softgram.controller.mongo.UserMongoManager;
import it.unipi.softgram.controller.neo4j.UserNeo4jManager;
import it.unipi.softgram.entities.User;
import it.unipi.softgram.utilities.drivers.MongoDriver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.bson.BsonRegularExpression;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
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

        Consumer<Document> processBlock = document -> {
            //System.out.println(document);
            String username = (String) document.get("_id");
            String website = (String) document.get("website");
            String email = (String) document.get("email");
            String role = (String) document.get("role");
            name_tfield.setText(username);
            email_tfield.setText(email);
            role_tfield.setText(role);
            web_tfield.setText(website);



        };

        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$match", new Document()
                                .append("$or", Collections.singletonList(
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



        String _id = name_tfield.getText();
        String email = email_tfield.getText();
        String role = role_tfield.getText();
        String website = web_tfield.getText();

        Stage newStage = new Stage();
        VBox comp = new VBox();
        javafx.scene.control.TextField username = new javafx.scene.control.TextField(_id);
        javafx.scene.control.TextField emailtxt = new javafx.scene.control.TextField(email);
        javafx.scene.control.TextField roletxt = new javafx.scene.control.TextField(role);
        javafx.scene.control.TextField websitetxt = new javafx.scene.control.TextField(website);
        javafx.scene.control.Button update = new Button("Update");
        update.setOnAction(event -> {
            UserMongoManager user=new UserMongoManager();
            User user1=new User();
            user1.setUsername(username.getText());
            user1.setEmail(emailtxt.getText());
            user1.setRole(roletxt.getText());
            user1.setRole(websitetxt.getText());
            user.updateUser(user1);
            JOptionPane.showMessageDialog(null, "Updated Successfully");


            name_tfield.setText("");
            role_tfield.setText("");
            email_tfield.setText("");
            web_tfield.setText("");
            name_tfield.setText(username.getText());
            role_tfield.setText(websitetxt.getText());
            email_tfield.setText(emailtxt.getText());
            web_tfield.setText(websitetxt.getText());

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
        UserNeo4jManager useneo=new UserNeo4jManager();
        useneo.addFollow(app_id, user.getUsername(), false);
        JOptionPane.showMessageDialog(null, "You followed this user");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
