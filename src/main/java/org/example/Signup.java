package org.example;

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
    @FXML private ComboBox country, role;
    @FXML private DatePicker birthday;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Italy",
                        "Iraq",
                        "UK",
                        "Chine",
                        "India"
                );
        country.getItems().addAll(options);

        ObservableList<String> options1 =
                FXCollections.observableArrayList(
                        "DEVELOPER",
                        "NORMAL_USER",
                        "ADMIN"
                );
        role.getItems().addAll(options1);
    }

    public void loginbtn(ActionEvent actionEvent) throws IOException {
        setRoot("login");
    }

    public void signup(ActionEvent actionEvent) throws IOException {

        if(username.getText().isEmpty() || email.getText().isEmpty() || website.getText().isEmpty() || password.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Fields required");
        }else{
            User user=new User();
            user.addUser(username.getText().toString(), email.getText().toString(), password.getText().toString(),website.getText().toString(),role.getItems().toString(), country.getItems().toString());
            JOptionPane.showMessageDialog(null, "Added successfully");
            setRoot("admin");
        }
    }
}
