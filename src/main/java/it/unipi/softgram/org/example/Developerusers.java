package it.unipi.softgram.org.example;

import it.unipi.softgram.controller.neo4j.UserNeo4jManager;
import it.unipi.softgram.entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Developerusers extends CommonUserPages {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "True",
                        "False"
                );
        super.getUserid_col().setCellValueFactory(new PropertyValueFactory<>("username"));
        super.getRole_col().setCellValueFactory(new PropertyValueFactory<>("role"));
        super.getEmail_col().setCellValueFactory(new PropertyValueFactory<>("email"));


        //suggested users

        super.getUsername_col().setCellValueFactory(new PropertyValueFactory<>("username"));

        //why here?
        //suggestedusers();


        FollowUserButtonToTable();
        FollowButtonToTable();
        findUsers("");
        listrequest.setOnMouseClicked(event -> {
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(null, "follow this user?", "!!", dialogButton);

            if (dialogResult == JOptionPane.YES_OPTION) {
                // Saving code here

            }
        });
        listsuggest.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2) {
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog(null, "Follow this user", "!!", dialogButton);

                if (dialogResult == JOptionPane.YES_OPTION) {
                    // Saving code here
                    UserNeo4jManager user1 = new UserNeo4jManager();
                    User user = new User();

                    user.setUsername(super.getUserid().getText());
                    String followeduser = listsuggest.getSelectionModel().getSelectedItem();
                    String followeruser = super.getUserid().getText();
                    boolean request = true;
                    //  System.out.println("clicked on " + listrequest.getSelectionModel().getSelectedItem());
                    user1.addFollow(followeruser, followeduser, request);
                    JOptionPane.showMessageDialog(null, "You follow this user");
                    followedSearch(super.getUserid().getText());

                }
            }
        });
        user_table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event){
                if(event.getClickCount() == 2){

                    try {
                        //Load second scene
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("usermainpage.fxml"));
                        Parent root = loader.load();

                        //Get controller of scene2
                        Developerusers.super.getUserid1().setText(user_table.getSelectionModel().getSelectedItem().getUsername());

                        Usermainpage scene2Controller = loader.getController();
                        //Pass whatever data you want. You can have multiple method calls here
                        scene2Controller.transferMessage(Developerusers.super.getUserid().getText());
                        scene2Controller.transferMessage1(Developerusers.super.getUserid1().getText());

                        //Show scene 2 in new window
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.setTitle("User Mainpage");
                        stage.show();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        });
    }
    public void actual_search(ActionEvent actionEvent) {
        UserNeo4jManager user=new UserNeo4jManager();
        List<String> data= user.browseActualFollowers(super.getActualtxt().getText());
        ObservableList<String> items = FXCollections.observableArrayList();
        if(data.isEmpty()){
            items.add("No data");
        }
        items.addAll(data);
        listactual.setItems(items);
    }
    public void actualSearch(String username){
        UserNeo4jManager user=new UserNeo4jManager();
        List<String> data= user.browseActualFollowers(username);
        ObservableList<String> items = FXCollections.observableArrayList();
        if(data.isEmpty()){
            items.add("No data");
        }
        items.addAll(data);
        listactual.setItems(items);
    }
    public void statismainpage(ActionEvent actionEvent) {

    }
}
