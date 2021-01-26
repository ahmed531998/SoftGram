package it.unipi.softgram.org.example;

import it.unipi.softgram.controller.mongo.UserMongoManager;
import it.unipi.softgram.entities.User;
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

import java.io.IOException;
import java.util.List;


public class WelcomeScreen {

    @FXML private TextField username;
    @FXML private PasswordField passwordField;
    @FXML private Label msg;
    @FXML private Label userid;

    public void loginAction(ActionEvent actionEvent) {
        if(username.getText().isEmpty() || passwordField.getText().isEmpty()){
            msg.setText("Fields required");
        }else {
            msg.setText("");
            UserMongoManager userFinder = new UserMongoManager();
            List<User> users = userFinder.searchUserByUsername(username.getText(), 0);
            User myUser = users.get(0);

            if(passwordField.getText().equals(myUser.getPassword())){
                String role = myUser.getRole();
                switch (role) {
                    case "Admin":
                        try {
                            //Load second scene
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin.fxml"));
                            Parent root = loader.load();

                            //Get controller of scene2
                            userid.setText(username.getText());
                            Admin scene2Controller = loader.getController();
                            //Pass whatever data you want. You can have multiple method calls here
                            scene2Controller.transferMessage(userid.getText());

                            //Show scene 2 in new window
                            Stage stage = new Stage();
                            stage.setScene(new Scene(root));
                            stage.setTitle("Admin Window");
                            stage.show();
                            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        msg.setText("");
                        break;
                    case "Developer":
                        try {
                            //Load second scene
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("developer.fxml"));
                            Parent root = loader.load();

                            //Get controller of scene2
                            userid.setText(username.getText());
                            Developer scene2Controller = loader.getController();
                            //Pass whatever data you want. You can have multiple method calls here
                            scene2Controller.transferMessage(userid.getText());
                            System.out.println(userid.getText());

                            //Show scene 2 in new window
                            Stage stage = new Stage();
                            stage.setScene(new Scene(root));
                            stage.setTitle("Developer Window");
                            stage.show();
                            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        msg.setText("");
                        break;
                    case "NormalUser":
                    case "Normal User":
                        try {
                            //Load second scene
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("normaluser.fxml"));
                            Parent root = loader.load();

                            //Get controller of scene2
                            userid.setText(username.getText());
                            NormalUser scene2Controller = loader.getController();
                            //Pass whatever data you want. You can have multiple method calls here
                            scene2Controller.transferMessage(userid.getText());
                            System.out.println(userid.getText());

                            //Show scene 2 in new window
                            Stage stage = new Stage();
                            stage.setScene(new Scene(root));
                            stage.setTitle("NormalUser Window");
                            stage.show();
                            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        msg.setText("");
                        break;
                }
            }
            else{
                msg.setText("Query Error");
                System.out.println("Error");
            }
        }
    }

    public void signupAction(ActionEvent actionEvent) {
        try {
            //Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("signup.fxml"));
            Parent root = loader.load();

            //Show scene 2 in new window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("signup Window");
            stage.show();
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}