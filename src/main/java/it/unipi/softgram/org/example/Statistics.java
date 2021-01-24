package it.unipi.softgram.org.example;

import it.unipi.softgram.controller.mongo.StatisticsMongoManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


public class Statistics implements Initializable {
    @FXML
    private Label appid;
    @FXML
    PieChart piechart;
    int a=10;
    int b=100;
    int c=90;

    @FXML
    TextField threshhold,limit,threshhold1, limit1;
    HashMap<String, Double> hash = new HashMap<String, Double>();
    StatisticsMongoManager statisticsMongoManager=new StatisticsMongoManager();
    @FXML
    private BarChart<?, ?> barchart;
    @FXML
    private CategoryAxis x;
    @FXML
    private NumberAxis y;
    @FXML
    private BarChart<?, ?> barchart1;

    private final ObservableList<PieChart.Data> details= FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        getAppsWithAverageAbove();
        getusersActivity();
        monitoryear();
        // TODO Auto-generated method stub


       /* XYChart.Series set1=new XYChart.Series<>();
        set1.getData().add(new XYChart.Data("A"+"("+a+")", a));
        XYChart.Series set2=new XYChart.Series<>();
        set2.getData().add(new XYChart.Data("B"+"("+b+")", b));
        XYChart.Series set3=new XYChart.Series<>();
        set2.getData().add(new XYChart.Data("C"+"("+c+")", c));
        barchart.getData().addAll(set1);
        barchart.getData().addAll(set2);
        barchart.getData().addAll(set3);*/

    }
    public void transferMessage(String message) {
        //Display the message
        appid.setText(message);


    }

    public void usersmainpage(ActionEvent actionEvent) {
        try {
            //Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("users.fxml"));
            Parent root = loader.load();

            //Get controller of scene2

            Users scene2Controller = loader.getController();
            //Pass whatever data you want. You can have multiple method calls here
            scene2Controller.transferMessage(appid.getText());

            //Show scene 2 in new window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Users Window");
            stage.show();
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

  public void appmainpage(ActionEvent actionEvent) {
        try {
            //Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin.fxml"));
            Parent root = loader.load();

            //Get controller of scene2

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
            ex.printStackTrace();
        }
    }

    public void signout_fun(ActionEvent actionEvent) {
        try {
            //Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
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
            ex.printStackTrace();
        }
    }

    public void statismainpage(ActionEvent actionEvent) {
        try {
            //Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("statistics.fxml"));
            Parent root = loader.load();

            //Get controller of scene2

            Statistics scene2Controller = loader.getController();
            //Pass whatever data you want. You can have multiple method calls here
            scene2Controller.transferMessage(appid.getText());

            //Show scene 2 in new window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Statistics Window");
            stage.show();
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void getAppsWithAverageAbove(){

        XYChart.Series set1 = null;

        HashMap<String, Double> hash= (HashMap<String, Double>) statisticsMongoManager.getAppsWithAverageAbove(2,5);
        for ( Map.Entry<String, Double> entry : hash.entrySet()) {
            String key = entry.getKey();
            double tab = entry.getValue();
            // do something with key and/or tab
            set1=new XYChart.Series<>();
            set1.getData().add(new XYChart.Data(key, tab));
            barchart.getData().addAll(set1);
        }


    }
    public void getusersActivity(){

        XYChart.Series set1 = null;

        HashMap<String, Integer> hash= (HashMap<String, Integer>) statisticsMongoManager.getUserActivity(2,5);
        for ( Map.Entry<String, Integer> entry : hash.entrySet()) {
            String key = entry.getKey();
            double tab = entry.getValue();
            // do something with key and/or tab
            set1=new XYChart.Series<>();
            set1.getData().add(new XYChart.Data(key, tab));
            barchart1.getData().addAll(set1);
        }


    }

    public void run_btn(ActionEvent actionEvent) {
        barchart.getData().clear();
        XYChart.Series set1 = null;
        HashMap<String, Double> hash= (HashMap<String, Double>) statisticsMongoManager.getAppsWithAverageAbove(Double.parseDouble(threshhold.getText()),Integer.parseInt(limit.getText()));
        for ( Map.Entry<String, Double> entry : hash.entrySet()) {
            String key = entry.getKey();
            double tab = entry.getValue();
            // do something with key and/or tab
            set1=new XYChart.Series<>();
            set1.getData().add(new XYChart.Data(key, tab));
            barchart.getData().addAll(set1);
        }
    }

    public void run_btn1(ActionEvent actionEvent) {
        barchart1.getData().clear();
        XYChart.Series set1 = null;
        HashMap<String, Integer> hash= (HashMap<String, Integer>) statisticsMongoManager.getUserActivity(Double.parseDouble(threshhold1.getText()),Integer.parseInt(limit.getText()));
        for ( Map.Entry<String, Integer> entry : hash.entrySet()) {
            String key = entry.getKey();
            double tab = entry.getValue();
            // do something with key and/or tab
            set1=new XYChart.Series<>();
            set1.getData().add(new XYChart.Data(key, tab));
            barchart1.getData().addAll(set1);
        }
    }
    public void monitoryear(){
        HashMap<Integer, Integer> hash= (HashMap<Integer, Integer>) statisticsMongoManager.monitorYearlyActivity();
        for ( Map.Entry<Integer, Integer> entry : hash.entrySet()) {
            int key = entry.getKey();
            int tab = entry.getValue();
            // do something with key and/or tab
            details.addAll(new PieChart.Data(String.valueOf(key), tab)
            );
        }
        piechart.setData(details);
    }
}
