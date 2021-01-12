package org.example;

import com.mongodb.client.MongoCollection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.bson.Document;
import utilities.MongoDriver;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import javax.swing.*;
import java.io.IOException;

import static org.example.App.setRoot;

public class login {

    @FXML private TextField username;
    @FXML private PasswordField passwordField;
    @FXML private Label msg;

    public void loginbtn(ActionEvent actionEvent) throws IOException {
        if(username.getText().isEmpty() || passwordField.getText().isEmpty()){
            msg.setText("Fields required");
        }else {
            msg.setText("");
            MongoDriver driver = new MongoDriver();
            MongoCollection<Document> collection = driver.getCollection("users");

            // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

            Document query = new Document();
            query.append("_id", username.getText().toString());
            query.append("password", passwordField.getText().toString());


            if(collection.find(query).iterator().hasNext()){
                setRoot("admin");
                msg.setText("");
            }else{
                msg.setText("Query Error");
                System.out.println("Error");
            }


        }

    }

    public void signup(ActionEvent actionEvent) throws IOException {
        setRoot("signup");
    }
}
