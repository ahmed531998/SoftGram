package it.unipi.softgram.org.example;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class NormalUser extends CommonUser implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        suggestedapps();
        getCommonApps(true);

        super.getUserid().setVisible(false);
        FollowAppButtonToTable();
        FollowUserButtonToTable();

        super.getApp_id_col().setCellValueFactory(new PropertyValueFactory<>("_id"));
        super.getAppname_col().setCellValueFactory(new PropertyValueFactory<>("name"));
        super.getAdsupported_col().setCellValueFactory(new PropertyValueFactory<>("adSupported"));
        super.getPrice_col().setCellValueFactory(new PropertyValueFactory<>("price"));
        super.getReleased_col().setCellValueFactory(new PropertyValueFactory<>("released"));
        super.getCat_col().setCellValueFactory(new PropertyValueFactory<>("category"));
        super.getAge_col().setCellValueFactory(new PropertyValueFactory<>("ageGroup"));

        super.getLast_col().setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));

        super.getIs_purchasesCol().setCellValueFactory(new PropertyValueFactory<>("inAppPurchase"));

        //suggested apps (Common)
        super.getAppid_col().setCellValueFactory(new PropertyValueFactory<>("_id"));
        super.getAppname_col().setCellValueFactory(new PropertyValueFactory<>("name"));
        super.getCat_col().setCellValueFactory(new PropertyValueFactory<>("category"));

        //common
        super.getAppid_com_col().setCellValueFactory(new PropertyValueFactory<>("_id"));
        super.getAppname_com_col().setCellValueFactory(new PropertyValueFactory<>("name"));

        //suggested users
        super.getUsername_col().setCellValueFactory(new PropertyValueFactory<>("username"));

        super.getApp_id_col2().setCellValueFactory(new PropertyValueFactory<>("_id"));
        super.getAppname_col2().setCellValueFactory(new PropertyValueFactory<>("name"));
        super.getName_col2().setCellValueFactory(new PropertyValueFactory<>("name"));
        super.getNum_col2().setCellValueFactory(new PropertyValueFactory<>("numberOfReviews"));
        super.getAvg_col2().setCellValueFactory(new PropertyValueFactory<>("Avg"));

        super.getApp_id_col3().setCellValueFactory(new PropertyValueFactory<>("_id"));
        super.getName_col3().setCellValueFactory(new PropertyValueFactory<>("name"));
        super.getCate_col3().setCellValueFactory(new PropertyValueFactory<>("category"));
        super.getAvg_col3().setCellValueFactory(new PropertyValueFactory<>("Avg"));

        findApp("");
        getMostPopularApps();

        search_table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event){
                if(event.getClickCount() == 2){
                    try {
                        //Load second scene
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("ApplicationMain.fxml"));
                        Parent root = loader.load();

                        //Get controller of scene2
                        NormalUser.super.getAppid().setText(search_table.getSelectionModel().getSelectedItem().getId());
                        NormalUser.super.getUserid().setText(NormalUser.super.getUserid().getText());
                        ApplicationMain scene2Controller = loader.getController();
                        //Pass whatever data you want. You can have multiple method calls here
                        scene2Controller.transferMessage(NormalUser.super.getAppid().getText()); //appid
                        scene2Controller.transferMessage1(NormalUser.super.getUserid().getText()); //userid

                        //Show scene 2 in new window
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.setTitle("Second Window");
                        stage.show();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        });
        search_table2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event){
                if(event.getClickCount() == 2){
                    try {
                        //Load second scene
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("ApplicationMain.fxml"));
                        Parent root = loader.load();

                        //Get controller of scene2
                        NormalUser.super.getAppid().setText(search_table2.getSelectionModel().getSelectedItem().getId());
                        ApplicationMain scene2Controller = loader.getController();
                        //Pass whatever data you want. You can have multiple method calls here
                        scene2Controller.transferMessage(NormalUser.super.getAppid().getText());
                        scene2Controller.transferMessage1(NormalUser.super.getUserid().getText());

                        //Show scene 2 in new window
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.setTitle("Second Window");
                        stage.show();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        });
        search_table3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event){
                if(event.getClickCount() == 2){
                    try {
                        //Load second scene
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("ApplicationMain.fxml"));
                        Parent root = loader.load();

                        //Get controller of scene2
                        NormalUser.super.getAppid().setText(search_table3.getSelectionModel().getSelectedItem().getId());
                        ApplicationMain scene2Controller = loader.getController();
                        //Pass whatever data you want. You can have multiple method calls here
                        scene2Controller.transferMessage(NormalUser.super.getAppid().getText());
                        scene2Controller.transferMessage1(NormalUser.super.getUserid().getText());

                        //Show scene 2 in new window
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.setTitle("Second Window");
                        stage.show();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        });

    }


    public static void showStage() {
        Stage newStage = new Stage();
        VBox comp = new VBox();
        TextField nameField = new TextField("Name");
        TextField phoneNumber = new TextField("Phone Number");
        comp.getChildren().add(nameField);
        comp.getChildren().add(phoneNumber);
        Scene stageScene = new Scene(comp, 300, 300);
        newStage.setScene(stageScene);
        newStage.show();
    }


    public void statismainpage(ActionEvent actionEvent) {
    }


    public void usersmainpage(ActionEvent actionEvent) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("test.fxml"));
            Parent root = loader.load();

            Normaluserspage scene2Controller = loader.getController();
            scene2Controller.transferMessage(super.getUserid().getText());

            //Show scene 2 in new window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Normaluser Window");
            stage.show();
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void appmainpage(ActionEvent actionEvent) throws IOException {
        try {
            //Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("normaluser.fxml"));
            Parent root = loader.load();

            //Get controller of scene2

            NormalUser scene2Controller = loader.getController();
            //Pass whatever data you want. You can have multiple method calls here
            scene2Controller.transferMessage(super.getUserid().getText());

            //Show scene 2 in new window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Normaluser Window");
            stage.show();
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}