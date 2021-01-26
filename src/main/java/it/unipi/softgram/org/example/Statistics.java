package it.unipi.softgram.org.example;

import it.unipi.softgram.controller.mongo.StatisticsMongoManager;
import it.unipi.softgram.gui.WelcomeScreen;
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
    PieChart piechart,piechart1,piechartactive;
    int a=10;
    int b=100;
    int c=90;
    @FXML
    LineChart<?,?> linechart;

    @FXML
    TextField threshhold,limit,threshhold1, limit1,year;
    HashMap<String, Double> hash = new HashMap<String, Double>();
    StatisticsMongoManager statisticsMongoManager=new StatisticsMongoManager();
    @FXML
    private BarChart<?, ?> barchart,barchartt;
    @FXML
    private CategoryAxis x;
    @FXML
    private NumberAxis y;
    @FXML
    private BarChart<?, ?> barchart1;

    private final ObservableList<PieChart.Data> details= FXCollections.observableArrayList();
    private final ObservableList<PieChart.Data> details1= FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        getAppsWithAverageAbove();
        getusersActivity();
        monitoryear();
       // bestapp();
        //getusersActivity1();
        XYChart.Series set1 = null;
        XYChart.Series set2 = null;
        XYChart.Series set3= null;
        set1=new XYChart.Series<>();
        set1.getData().add(new XYChart.Data("com.loud.id", 2018));
        set1.getData().add(new XYChart.Data("com.loud.id", 2018));
        set1.getData().add(new XYChart.Data("com.loud.id", 2018));
        set1.getData().add(new XYChart.Data("com.loud.id", 2018));
        set1.getData().add(new XYChart.Data("com.loud.id", 2018));
        barchartt.getData().addAll(set1);

        set2=new XYChart.Series<>();
        set3=new XYChart.Series<>();
        set2.getData().add(new XYChart.Data("com.seo.id", 2019));
        set2.getData().add(new XYChart.Data("com.seo.id", 2019));
        set2.getData().add(new XYChart.Data("com.seo.id", 2019));
        set3.getData().add(new XYChart.Data("com.jhyuifd.id", 2020));
        set3.getData().add(new XYChart.Data("com.jhyuifd.id", 2020));
        barchartt.getData().addAll(set2);
        barchartt.getData().addAll(set3);


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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("WelcomeScreen.fxml"));
            Parent root = loader.load();

            //Get controller of scene2
            WelcomeScreen scene2Controller = loader.getController();
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
        HashMap<String, Integer> hash= (HashMap<String, Integer>) statisticsMongoManager.getUserActivity(Double.parseDouble(threshhold1.getText()),Integer.parseInt(limit1.getText()));
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
    public void monitoryear1(int year){
        piechart.getData().clear();
        Map<Integer, Integer> hash= (Map<Integer, Integer>) statisticsMongoManager.monitorMonthlyActivity(year);
        for ( Map.Entry<Integer, Integer> entry : hash.entrySet()) {
            int key = entry.getKey();
            int tab = entry.getValue();
            // do something with key and/or tab
            details.addAll(new PieChart.Data(String.valueOf(key), tab)
            );
        }
        piechart.setData(details);
    }

    public void run_yearbtn(ActionEvent actionEvent) {

        monitoryear1(Integer.parseInt(year.getText()));
    }


    public void bestapp(){
       /* try {
            Map<String, Integer> hash = (Map<String, Integer>) statisticsMongoManager.getBestAppEachYear();
            for (Map.Entry<String, Integer> entry : hash.entrySet()) {

                String key = entry.getKey();
                int value = entry.getValue();
                if(entry.equals("") || hash.entrySet().isEmpty())
                    continue;

                //System.out.println(key + ""+ value);
                // do something with key and/or tab
                details1.addAll(new PieChart.Data(key, value));
            }
            piechart1.setData(details1);
        }catch (NullPointerException e){
            e.printStackTrace();
        }*/
    }

    public void getusersActivity1(){

        XYChart.Series set1 = null;
        XYChart.Series set2 = null;
        XYChart.Series set3= null;

       /* HashMap<Integer, String> hash= (HashMap<Integer, String>) statisticsMongoManager.getBestAppEachYear();
        for ( Map.Entry<Integer, String > entry : hash.entrySet()) {
            int key = entry.getKey();
            String tab = entry.getValue();
            // do something with key and/or tab
            set1=new XYChart.Series<>();
            set1.getData().add(new XYChart.Data(key, tab));
            barchartt.getData().addAll(set1);
        }*/



    }


}
