package org.example;

import class_controller.UserNeo4jManager;
import enumerators.Role;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.example.App.setRoot;

public class Users implements Initializable {

    @FXML private TextField username,country,email,role,website,username_re,followerUsername,followedUsername;
    @FXML private PasswordField pass_id;
    @FXML
    private DatePicker birth;
    @FXML
    private ComboBox request;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "True",
                        "False"
                );
        request.getItems().addAll(options);
    }
    public void usersmainpage(ActionEvent actionEvent) throws IOException {
        App.setRoot("users");
    }

    public void appmainpage(ActionEvent actionEvent) throws IOException {
        App.setRoot("admin");
    }

    public void addusers(ActionEvent actionEvent) {

        Role.RoleValue roleValue= Role.RoleValue.valueOf(role.getText().toString());
        String usernametxt=username.getText().toString();
        String roleString = Role.getRoleString(roleValue);
        final String role = roleString.replaceAll("\\s","");
        if(username.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Field required");
        }else {
            UserNeo4jManager user = new UserNeo4jManager();
            user.addUser(usernametxt, roleValue);
            JOptionPane.showMessageDialog(null, "User added successfully");
        }
    }

    public void remove_user(ActionEvent actionEvent) {
        UserNeo4jManager user = new UserNeo4jManager();
        if (username_re.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Field required");
        }else {
            user.removeUser(username_re.getText().toString());
            JOptionPane.showMessageDialog(null, "User added successfully");
        }
    }

    public void becomedeveloper(ActionEvent actionEvent) {
        UserNeo4jManager user = new UserNeo4jManager();
        if (username_re.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Field required");
        }else {
            user.becomeDeveloper(username_re.getText().toString());
            JOptionPane.showMessageDialog(null, "User became a developer");
        }
    }

    public void becomenormal(ActionEvent actionEvent) {
        UserNeo4jManager user = new UserNeo4jManager();
        if (username_re.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Field required");
        }else {
            user.becomeNormalUser(username_re.getText().toString());
            JOptionPane.showMessageDialog(null, "User became a normal user");
        }
    }

    public void addfollow(ActionEvent actionEvent) {
        UserNeo4jManager user = new UserNeo4jManager();
        if (followedUsername.getText().isEmpty() || followerUsername.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Field required");
        }else {
            user.addFollow(followerUsername.getText().toString(),followedUsername.getText().toString(), Boolean.parseBoolean(request.getItems().toString()));
            JOptionPane.showMessageDialog(null, "Done");
        }
    }

    public void acceptfollow(ActionEvent actionEvent) {
        UserNeo4jManager user = new UserNeo4jManager();
        if (followedUsername.getText().isEmpty() || followerUsername.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Field required");
        }else {
            user.acceptFollow(followerUsername.getText().toString(),followedUsername.getText().toString());
            JOptionPane.showMessageDialog(null, "Accepted");
        }
    }

    public void removedfollow(ActionEvent actionEvent) {
        UserNeo4jManager user = new UserNeo4jManager();
        if (followedUsername.getText().isEmpty() || followerUsername.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Field required");
        }else {
            user.removeFollow(followerUsername.getText().toString(),followedUsername.getText().toString());
            JOptionPane.showMessageDialog(null, "Removed");
        }
    }
    public void signout_fun(ActionEvent actionEvent) throws IOException {
        setRoot("login");
    }
}
