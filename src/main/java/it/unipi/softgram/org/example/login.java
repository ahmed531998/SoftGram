package it.unipi.softgram.org.example;

import com.mongodb.client.MongoCollection;
import it.unipi.softgram.entities.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.bson.Document;
import it.unipi.softgram.utilities.drivers.MongoDriver;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import javax.swing.*;
import java.io.IOException;

import static it.unipi.softgram.org.example.App.setRoot;

public class login {

    @FXML private TextField username;
    @FXML private PasswordField passwordField;
    @FXML private Label msg;
    @FXML private Label appid;

    public void loginbtn(ActionEvent actionEvent) throws IOException {
        if(username.getText().isEmpty() || passwordField.getText().isEmpty()){
            msg.setText("Fields required");
        }else {
            msg.setText("");
            MongoDriver driver = new MongoDriver();
            MongoCollection<Document> collection = driver.getCollection("user");

            // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

            Document query = new Document();
            query.append("_id", username.getText().toString());
            query.append("password", passwordField.getText().toString());


            if(collection.find(query).iterator().hasNext()){
                App app=new App();

                try {
                    //Load second scene
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/admin.fxml"));
                    Parent root = loader.load();

                    //Get controller of scene2
                    appid.setText(username.getText().toString());
                    Admin scene2Controller = loader.getController();
                    //Pass whatever data you want. You can have multiple method calls here
                    scene2Controller.transferMessage(appid.getText());

                    //Show scene 2 in new window
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Admin Window");
                    stage.show();
                    ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
                } catch (IOException ex) {
                    System.err.println(ex);
                }

                msg.setText("");
            }else{
                msg.setText("Query Error");
                System.out.println("Error");
            }


        }

    }

    public void signup(ActionEvent actionEvent) throws IOException {
        try {
            //Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/signup.fxml"));
            Parent root = loader.load();

            //Get controller of scene2
            Signup scene2Controller = loader.getController();
            //Pass whatever data you want. You can have multiple method calls here
            //scene2Controller.transferMessage("");

            //Show scene 2 in new window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("signup Window");
            stage.show();
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}