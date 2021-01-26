package it.unipi.softgram.org.example;

import com.mongodb.client.MongoCollection;
import it.unipi.softgram.controller.mongo.AppMongoManager;
import it.unipi.softgram.controller.mongo.ReviewMongoManager;
import it.unipi.softgram.controller.mongo.UserMongoManager;
import it.unipi.softgram.controller.mongoneo4j.AppMongoNeo4jManager;
import it.unipi.softgram.controller.neo4j.AppNeo4jManager;
import it.unipi.softgram.entities.App;
import it.unipi.softgram.entities.Review;
import it.unipi.softgram.entities.User;
import it.unipi.softgram.utilities.drivers.MongoDriver;
import it.unipi.softgram.utilities.enumerators.Relation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.bson.Document;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

public class ApplicationMain  implements Initializable {
    @FXML
    private Text appname,appid,released, price, currency,agegroup,currency1,size,name_tfield;

    @FXML
    TextArea content_txt;
    @FXML
    private ComboBox reviewScore;
    @FXML Button follow;
    @FXML private Text developerid;

    @FXML
    private Text name_txt,released_txt,last_txt,developeremail,developerweb;
    @FXML private Button home,delete_btn,updte_btn;
    @FXML
    Label userid;
    String app_id;
    ObservableList<Review> reviewdataa = FXCollections.observableArrayList();

    @FXML TableView<Review> review_tble;
    @FXML private TableColumn<Review, String> id_Col;
    @FXML private TableColumn<Review, String> content_col;
    @FXML private TableColumn<Review, Double> score_col;
    AppNeo4jManager app=new AppNeo4jManager();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "1",
                        "2",
                        "3",
                        "4",
                        "5"
                );
        reviewScore.getItems().addAll(options);
        //common
        id_Col.setCellValueFactory(new PropertyValueFactory<>("_id"));
        content_col.setCellValueFactory(new PropertyValueFactory<>("content"));
        score_col.setCellValueFactory(new PropertyValueFactory<>("score"));

        updateButtonToTable();
        deleteButtonToTable();
    }

    public void transferMessage(String message) {
        //Display the message
        appid.setText(message);
        app_id=message;
        System.out.println(message);
        findApp(message);
        showReviews(message);

    }

    public void transferMessage1(String message) {
        //Display the message
        userid.setText(message);
        String check=check(message);
        boolean check2=check2(message, developerid.getText());
        delete_btn.setVisible(false);
        updte_btn.setVisible(false);
        if(check2==true){
            delete_btn.setVisible(true);
            updte_btn.setVisible(true);
        }else{
            if(check.equals("Admin")){
                delete_btn.setVisible(true);
                updte_btn.setVisible(false);
            }else{
                delete_btn.setVisible(false);
                updte_btn.setVisible(false);
            }
        }


    }

    public void home_fun(ActionEvent actionEvent) {
        String check=check(userid.getText());
        try {
            //Load second scene
            if(check.equals("Admin")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin.fxml"));
                Parent root = loader.load();
                Admin scene2Controller = loader.getController();
                scene2Controller.transferMessage(appid.getText());
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Admin Window");
                stage.show();
                ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
            }else if (check.equals("Developer")){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("developer.fxml"));
                Parent root = loader.load();
                Developer scene2Controller = loader.getController();
                scene2Controller.transferMessage(appid.getText());
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Admin Window");
                stage.show();
            }else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("normaluser.fxml"));
                Parent root = loader.load();
                NormalUser scene2Controller = loader.getController();
                scene2Controller.transferMessage(appid.getText());
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Admin Window");
                stage.show();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void findApp(String text){
        AppMongoManager appFinder = new AppMongoManager();
        List<App> apps = appFinder.findApp(text, 1, 0);
        App a = apps.get(0);
        currency1.setText(a.getCurrency());
        size.setText(a.getSize());
        name_txt.setText(a.getName());
        price.setText(String.valueOf(a.getPrice()));
        currency.setText(a.getCategory());
        agegroup.setText(a.getAgeGroup());
        released_txt.setText(String.valueOf(a.getReleased()));
        last_txt.setText(String.valueOf(a.getLastUpdated()));
        //dev
    }

    public void updateApp(ActionEvent actionEvent) {
        double price1 = Double.parseDouble(price.getText());
        String name = name_txt.getText();
        String category1 = currency1.getText();
        String ageGroup = agegroup.getText();
        String category = currency.getText();
        Stage newStage = new Stage();
        VBox comp = new VBox();
        TextField appname = new TextField(name);
        appname.setPromptText("App name");
        TextField price11 = new TextField("" + price1);
        price11.setPromptText("Price");
        TextField category11 = new TextField("" + category);
        category11.setPromptText("Category");
        TextField agegroup = new TextField(ageGroup);
        agegroup.setPromptText("Age group");
        Button update = new Button("Update");
        update.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AppMongoNeo4jManager app=new AppMongoNeo4jManager();
                AppMongoManager appFinder=new AppMongoManager();
                App app1=new App();
                app1.setId(appid.getText());
                app1.setName(appname.getText());
                app1.setPrice(Double.parseDouble(price11.getText()));
                app1.setCategory(category11.getText());
                app1.setAgeGroup(agegroup.getText());
                app.updateName(app1);
                app.updateCategory(app1);
                appFinder.updateApp(app1);
                JOptionPane.showMessageDialog(null, "Updated Successfully");
            }
        });

        comp.getChildren().add(appname);
        comp.getChildren().add(price11);
        comp.getChildren().add(category11);
        comp.getChildren().add(agegroup);
        comp.getChildren().add(update);
        Scene stageScene = new Scene(comp, 300, 300);
        newStage.setScene(stageScene);
        newStage.show();
    }

    public void deleteApp(ActionEvent actionEvent) {
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(null, "Would You Like to remove this application?", "Removing", dialogButton);
        ;
        if (dialogResult == JOptionPane.YES_OPTION) {
            // Saving code here
            AppMongoNeo4jManager app=new AppMongoNeo4jManager();
            App app_idd=new App();
            app_idd.setId(appid.getText());
            app.removeApp(app_idd);
            JOptionPane.showMessageDialog(null, "Removed Successfully");
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        }
    }

    public void FollowApp(ActionEvent actionEvent) {
        AppNeo4jManager app=new AppNeo4jManager();
        User user=new User();
        App app1=new App();
        user.setUsername(appid.getText());
        app1.setId(appid.getText());

           if(follow.getText().equals("Follow")) {
               app.followOrDevelopApp(user, app1, Relation.RelationType.FOLLOW);
               JOptionPane.showMessageDialog(null, "You followed this app");
               follow.setText("Unfollow");
           }else {
               app.unfollowApp(user,app1);
               JOptionPane.showMessageDialog(null, "You unfollowed this app");
               follow.setText("Follow");
           }

    }

    public  void showReviews(String text){
        review_tble.setItems(null);
        review_tble.setItems(reviewdataa);
        ReviewMongoManager reviewFinder = new ReviewMongoManager();
        List<Review> reviews = reviewFinder.showReviewsOfApp(text, 0);
        for (Review r: reviews){
            reviewdataa.add(r);
        }
    }

    public void ClearTable(TableView x) {
        /*
         * the table should be already created before
         * table should have information
         */

        for (int i = 0; i < x.getItems().size(); i++) {
            x.getItems().clear();
        }

    }

    public void AddReview(ActionEvent actionEvent) {
        ReviewMongoManager reviewm=new ReviewMongoManager();
        MongoDriver driver=new MongoDriver();
        Review review=new Review();
        try {
            MongoCollection<Document> reviewCollection = driver.getCollection("review");
            Document reviewDoc = new Document();
            reviewDoc.append("appId", appid.getText());
            reviewDoc.append("username", userid.getText());
            reviewDoc.append("score", new Document()
                    .append("$exists", true)
            );
            int limit = 1;
            Review reviewScoree = new Review();
            Consumer<Document> processBlock = new Consumer<Document>() {
                @Override
                public void accept(Document document) {

                        //insert both
                        reviewScoree.fromReviewDocument(document);
                        review.setAppId(appid.getText());
                        review.setAppName(name_txt.getText());
                        review.setCategory(currency.getText());
                        review.setUsername(userid.getText());
                        review.setContent(content_txt.getText());
                        review.setDate(new Date());
                        reviewm.postNewReview(review);
                        reviewm.updateReviewScore(reviewScoree.fromReviewDocument(document),Double.parseDouble(reviewScore.getSelectionModel().getSelectedItem().toString()));
                        JOptionPane.showMessageDialog(null, "Review Added");
                        ClearTable(review_tble);
                        showReviews(appid.getText());




                }
            };
            if(!reviewCollection.find(reviewDoc).limit(limit).iterator().hasNext()){
                review.setAppId(appid.getText());
                review.setAppName(name_txt.getText());
                review.setCategory(currency.getText());
                review.setUsername(userid.getText());
                review.setScore(Double.parseDouble(reviewScore.getSelectionModel().getSelectedItem().toString()));
                review.setContent(content_txt.getText());
                reviewm.postNewReview(review);
                review.setDate(new Date());
                JOptionPane.showMessageDialog(null, "Review Added");
                ClearTable(review_tble);
                showReviews(appid.getText());
            }else{
                reviewCollection.find(reviewDoc).limit(limit).forEach(processBlock);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void updateButtonToTable() {
        TableColumn<Review, Void> colBtn = new TableColumn("Update");

        Callback<TableColumn<Review, Void>, TableCell<Review, Void>> cellFactory = new Callback<TableColumn<Review, Void>, TableCell<Review, Void>>() {
            @Override
            public TableCell<Review, Void> call(final TableColumn<Review, Void> param) {
                final TableCell<Review, Void> cell = new TableCell<Review, Void>() {

                    private final Button btn = new Button("Update");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Review data = getTableView().getItems().get(getIndex());

                            Stage newStage = new Stage();
                            VBox comp = new VBox();
                            TextArea contenttxt = new TextArea(getTableView().getItems().get(getIndex()).getContent());

                            Button update = new Button("Update");
                            update.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    ReviewMongoManager reviewm=new ReviewMongoManager();
                                    MongoDriver driver=new MongoDriver();
                                    Review review=new Review();

                                    try {
                                        MongoCollection<Document> reviewCollection = driver.getCollection("review");
                                        Document reviewDoc = new Document();
                                        reviewDoc.append("appId", appid.getText());
                                        reviewDoc.append("username", userid.getText());
                                        reviewDoc.append("content", new Document()
                                                .append("$exists", true)
                                        );
                                        int limit = 1;
                                        Review reviewScoree = new Review();

                                        Consumer<Document> processBlock = new Consumer<Document>() {
                                            @Override
                                            public void accept(Document document) {

                                                if (!reviewDoc.isEmpty()) {
                                                    //insert both
                                                    reviewm.updateReviewContent(reviewScoree.fromReviewDocument(document),contenttxt.getText());
                                                    JOptionPane.showMessageDialog(null, "Review updated");
                                                    ClearTable(review_tble);
                                                    showReviews(appid.getText());

                                                }else{

                                                }
                                            }
                                        };
                                        reviewCollection.find(reviewDoc).limit(limit).forEach(processBlock);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            });

                            comp.getChildren().add(contenttxt);
                            comp.getChildren().add(update);
                            Scene stageScene = new Scene(comp, 300, 300);
                            newStage.setScene(stageScene);
                            newStage.show();


                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Review data = getTableView().getItems().get(getIndex());
                            if(userid.getText().equals(getTableView().getItems().get(getIndex()).getUsername())) {
                                setGraphic(btn);
                            }else{
                                setGraphic(null);
                            }
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);

        review_tble.getColumns().add(colBtn);

    }


    private void deleteButtonToTable() {

        TableColumn<Review, Void> colBtn = new TableColumn("Delete");

        Callback<TableColumn<Review, Void>, TableCell<Review, Void>> cellFactory = new Callback<TableColumn<Review, Void>, TableCell<Review, Void>>() {
            @Override
            public TableCell<Review, Void> call(final TableColumn<Review, Void> param) {
                final TableCell<Review, Void> cell = new TableCell<Review, Void>() {
                    private final Button btn = new Button("Delete");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Review data = getTableView().getItems().get(getIndex());
                            System.out.println("selectedData: " + data);
                            int dialogButton = JOptionPane.YES_NO_OPTION;
                            int dialogResult = JOptionPane.showConfirmDialog(null, "Would You Like to remove this review?", "Removing", dialogButton);
                            if (dialogResult == JOptionPane.YES_OPTION) {
                                // Saving code here
                                ReviewMongoManager review=new ReviewMongoManager();
                                Review r=new Review();
                                Object _id=getTableView().getItems().get(getIndex()).get_id();
                                r.set_id(_id);
                                review.delete(r);
                                JOptionPane.showMessageDialog(null, "Removed Successfully");
                                ClearTable(review_tble);
                                showReviews(appid.getText());
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Review data = getTableView().getItems().get(getIndex());
                            if(userid.getText().equals(getTableView().getItems().get(getIndex()).getUsername())) {
                                setGraphic(btn);
                            }else{
                                setGraphic(null);
                            }

                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);

        review_tble.getColumns().add(colBtn);

    }
    public String check(String userid) {
        UserMongoManager user = new UserMongoManager();
        List<User> users = user.searchUserByUsername(userid, 0);
        String check = "";
        if (users.size() > 0){
            User myUser = users.get(0);
            if (myUser.getRole().equals("Admin")) {
                return check = "Admin";
            } else if (myUser.getRole().equals("Developer")) {
                return check = "Developer";
            } else  {
                return check = "Normal";
            }
        }
        else{
            return check;
        }
    }
    public boolean check2(String userid, String username){

      if(userid.equals(username)){
          return true;
      }else {
          return false;
      }
    }

}
