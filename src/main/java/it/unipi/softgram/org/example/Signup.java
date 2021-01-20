package org.example;

import controller.mongo.UserMongoManager;
import controller.neo4j.UserNeo4jManager;
import entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.example.App.setRoot;

public class Signup implements Initializable {

    @FXML private TextField username, email, website;
    @FXML private PasswordField password;
    @FXML private ComboBox country;
    @FXML private DatePicker birthday;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Italy",
                        "Iraq",
                        "UK",
                        "Chine",
                        "India",
                        "Egypt",
                        "US"
                );
        country.getItems().addAll(options);


    }

    public void loginbtn(ActionEvent actionEvent) throws IOException {
        setRoot("login");
    }

    public void signup(ActionEvent actionEvent) throws IOException {

        if(username.getText().isEmpty() || email.getText().isEmpty() || website.getText().isEmpty() || password.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Fields required");
        }else{
            UserNeo4jManager user=new UserNeo4jManager();
            User userobj=new User();
            try {
                userobj.setUsername(username.getText().toString());
                userobj.setWebsite(website.getText().toString());
                userobj.setPassword(password.getText().toString());
                userobj.setCountry(country.getItems().toString());
                userobj.setEmail(email.getText().toString());
                user.addUser(userobj);
                JOptionPane.showMessageDialog(null, "Added successfully");
                setRoot("admin");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}