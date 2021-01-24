package it.unipi.softgram.org.example;

import com.mongodb.client.MongoCollection;
import it.unipi.softgram.controller.mongo.UserMongoManager;
import it.unipi.softgram.entities.App;
import it.unipi.softgram.entities.User;
import it.unipi.softgram.utilities.drivers.MongoDriver;
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
import java.util.function.Consumer;

import java.io.IOException;
import java.util.List;


public class login {

    @FXML private TextField username;
    @FXML private PasswordField passwordField;
    @FXML private Label msg;
    @FXML private Label userid;

    public void loginbtn(ActionEvent actionEvent) {

        if(username.getText().isEmpty() || passwordField.getText().isEmpty()){
            msg.setText("Fields required");
        }else {
            msg.setText("");
            MongoDriver driver = new MongoDriver();
            MongoCollection<Document> collection = driver.getCollection("user");

            // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

            Document query = new Document();
            query.append("_id", username.getText());
            query.append("password", passwordField.getText());
            Document query1 = new Document();
            query1.append("_id", username.getText());




            String role= (String) collection.find(query1).iterator().next().get("role");

            if(collection.find(query).iterator().hasNext() && collection.find(query1).iterator().next().get("role").equals("Admin")){


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
                    ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                msg.setText("");
            }else if(collection.find(query).iterator().hasNext() && collection.find(query1).iterator().next().get("role").equals("Developer")){
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
                    ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                msg.setText("");
            }else if(collection.find(query).iterator().hasNext() && collection.find(query1).iterator().next().get("role").equals("NormalUser") || collection.find(query1).iterator().next().get("role").equals("Normal User")){
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
                    ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                msg.setText("");
            }else{
                msg.setText("Query Error");
                System.out.println("Error");
            }


        }

    }

    public void signup(ActionEvent actionEvent) {
        try {
            //Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("signup.fxml"));
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
            ex.printStackTrace();
        }
    }
}