package it.unipi.softgram.org.example;

import it.unipi.softgram.controller.mongo.UserMongoManager;
import it.unipi.softgram.controller.mongoneo4j.UserMongoNeo4jManager;
import it.unipi.softgram.controller.neo4j.UserNeo4jManager;
import it.unipi.softgram.entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static it.unipi.softgram.org.example.App.setRoot;

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
        try {
            //Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/login.fxml"));
            Parent root = loader.load();

            //Get controller of scene2
            login scene2Controller = loader.getController();
            //Pass whatever data you want. You can have multiple method calls here
            //scene2Controller.transferMessage("");

            //Show scene 2 in new window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("login Window");
            stage.show();
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public void signup(ActionEvent actionEvent) throws IOException {

        if(username.getText().isEmpty() || email.getText().isEmpty() || website.getText().isEmpty() || password.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Fields required");
        }else{
            UserMongoNeo4jManager user=new UserMongoNeo4jManager();
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
