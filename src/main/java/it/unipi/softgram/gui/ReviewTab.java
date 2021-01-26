package it.unipi.softgram.gui;

import it.unipi.softgram.controller.mongo.ReviewMongoManager;
import it.unipi.softgram.entities.Review;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import static java.lang.Double.parseDouble;

public class ReviewTab implements Initializable {

    public ToggleGroup toggleGroup;
    public TableView reviewTable;
    public TableColumn contentCol;
    public TableColumn scoreCol;
    public TableColumn userCol;
    public TableColumn nameCol;
    public TableColumn dateCol;
    public TextField searchBox;
    public RadioButton idTick;
    public RadioButton catTick;
    public RadioButton userTick;
    public RadioButton scoreTick;
    public RadioButton wordTick;
    public Button appButton;
    public RadioButton dateTick;
    public Button userButton;
    public Button reviewButton;
    public Button signout;
    public Button searchButton;
    public TableColumn appId;
    public TableColumn appName;
    public TableColumn appCategory;
    public TableColumn appSize;
    public TableColumn appPrice;
    public TableColumn appRelease;
    private String chosenTick;
    public  ObservableList<Review> reviewArray = FXCollections.observableArrayList();


    public void appTab(ActionEvent actionEvent) throws IOException {
        try {
            //Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("appTab.fxml"));
            Parent root = loader.load();

            //Get controller of scene2

           // NormalUser scene2Controller = loader.getController();
            //Pass whatever data you want. You can have multiple method calls here
            //scene2Controller.transferMessage(userid.getText());

            //Show scene 2 in new window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("App Tab");
            stage.show();
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void userTab(ActionEvent actionEvent) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userTab.fxml"));
            Parent root = loader.load();

            //Normaluserspage scene2Controller = loader.getController();
            //scene2Controller.transferMessage(userid.getText());

            //Show scene 2 in new window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("User Tab");
            stage.show();
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void reviewTab(ActionEvent actionEvent){
        try {
            //Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("reviewTab.fxml"));
            Parent root = loader.load();

            //Get controller of scene2

           // ReviewTab scene2Controller = loader.getController();
            //Pass whatever data you want. You can have multiple method calls here
            //scene2Controller.transferMessage(userid.getText());

            //Show scene 2 in new window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Review Tab");
            stage.show();
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void reviewSearch(ActionEvent actionEvent) throws ParseException {
        ReviewMongoManager reviewFinder = new ReviewMongoManager();
        List<Review> reviews = null;
        if(chosenTick!=null){
            System.out.println("I am here " + chosenTick);
            switch (chosenTick){
                case "idTick":
                    reviews = reviewFinder.showReviewsOfApp(searchBox.getText(), 0);
                    break;
                case "wordTick":
                    reviews = reviewFinder.searchByWord(searchBox.getText(), 0);
                    break;
                case "userTick":
                    reviews = reviewFinder.showReviewsOfUser(searchBox.getText(), 0);
                    break;
                case "scoreTick":
                    reviews = reviewFinder.searchByScore(parseDouble(searchBox.getText()), 0);
                    break;
                case "catTick":
                    reviews = reviewFinder.showReviewsOfCat(searchBox.getText(),0);
                    break;
                case "dateTick":
                    String pattern = "yyyy-MM-dd";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                    Date date = simpleDateFormat.parse(searchBox.getText());
                    reviews = reviewFinder.searchByDate(date, ReviewMongoManager.DateQuery.On, 0);
                    break;
                default:
                    break;
            }
            ClearTable(reviewTable);
            reviewTable.setItems(null);
            reviewTable.setItems(reviewArray);
            reviewArray.addAll(reviews);
        }
    }


    public void setSearchParam(ActionEvent actionEvent) {
        toggleGroup.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (toggleGroup.getSelectedToggle() != null) {
                RadioButton selected = (RadioButton) toggleGroup.getSelectedToggle();
                System.out.println(selected.getText());
                chosenTick = selected.getText();
            }
        });
    }
    public void signout(ActionEvent actionEvent) {
        try {
            //Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("WelcomeScreen.fxml"));
            Parent root = loader.load();
            //Get controller of scene2
            WelcomeScreen scene2Controller = loader.getController();
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        toggleGroup = new ToggleGroup();
        idTick.setToggleGroup(toggleGroup);
        catTick.setToggleGroup(toggleGroup);
        dateTick.setToggleGroup(toggleGroup);
        wordTick.setToggleGroup(toggleGroup);
        userTick.setToggleGroup(toggleGroup);
        scoreTick.setToggleGroup(toggleGroup);

        contentCol.setCellValueFactory(
                new PropertyValueFactory<Review,String>("content")
        );
        dateCol.setCellValueFactory(
                new PropertyValueFactory<Review,String>("date")
        );
        scoreCol.setCellValueFactory(
                new PropertyValueFactory<Review,String>("score")
        );

        nameCol.setCellValueFactory(
                new PropertyValueFactory<Review,String>("appName")
        );

        userCol.setCellValueFactory(
                new PropertyValueFactory<Review,String>("username")
        );


    }
    public void ClearTable(TableView x) {
        for (int i = 0; i < x.getItems().size(); i++) {
            x.getItems().clear();
        }
    }
}

