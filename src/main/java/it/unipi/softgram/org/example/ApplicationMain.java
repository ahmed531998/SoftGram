package it.unipi.softgram.org.example;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import it.unipi.softgram.controller.mongo.AppMongoManager;
import it.unipi.softgram.controller.mongo.ReviewMongoManager;
import it.unipi.softgram.controller.mongoneo4j.AppMongoNeo4jManager;
import it.unipi.softgram.controller.neo4j.AppNeo4jManager;
import it.unipi.softgram.entities.App;
import it.unipi.softgram.entities.Review;
import it.unipi.softgram.entities.User;
import it.unipi.softgram.table_chooser.AppData;
import it.unipi.softgram.table_chooser.Userdata;
import it.unipi.softgram.table_chooser.reviewData;
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
import org.bson.BsonRegularExpression;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.neo4j.driver.exceptions.NoSuchRecordException;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;
import it.unipi.softgram.table_chooser.reviewData;

import static com.mongodb.client.model.Filters.eq;

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
    ObservableList<reviewData> reviewdataa = FXCollections.observableArrayList();

    @FXML TableView<reviewData> review_tble;
    @FXML private TableColumn<reviewData, String> id_Col;
    @FXML private TableColumn<reviewData, String> content_col;
    @FXML private TableColumn<reviewData, Double> score_col;
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

        // findApp(app_id);

       /* App app1=new App();
        User user=new User();
        app1.setId(appid.getText());
        user.setUsername(userid.getText());
        try {
            boolean result = app.relationFollowUserAppExists(user, app1);

            if (result == false) {
                follow.setText("Follow");
            } else {
                follow.setText("UnFollow");
            }
        }catch (NoSuchRecordException e){

        }*/



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
        MongoDriver driver = new MongoDriver();
        MongoCollection<Document> collection = driver.getCollection("app");

        // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

        Document query = new Document();
        query.append("_id", text);
        Consumer<Document> processBlock = document -> {

            String name = (String) document.get("name");
            Double price1 = (Double) document.get("price");
            String agegroup1 = (String) document.get("ageGroup");
            String category= (String) document.get("category");
            String released= String.valueOf(document.get("released"));
            String lastupdated= String.valueOf(document.get("lastUpdated"));
            String currency_data= (String)document.get("currency");
            String size_data= (String) document.get("size");


            Document dev= (Document) document.get("developer");
            String developerEmail= (String) dev.get("developerEmail");
            String developerWeb1= (String) dev.get("developerWebsite");
            String developerID= (String) dev.get("developerId");
            if(developerEmail == null)
                developerEmail="no";
            currency1.setText(currency_data);
            size.setText(size_data);
            name_txt.setText(name);
            price.setText(String.valueOf(price1));
            currency.setText(category);
            agegroup.setText(agegroup1);
            released_txt.setText(released);
            last_txt.setText(lastupdated);
            developeremail.setText(developerEmail);
            developerweb.setText(developerWeb1);
            developerid.setText(developerID);



        };

        collection.find(query).forEach(processBlock);
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
                AppMongoManager app=new AppMongoManager();
                App app1=new App();
                app1.setId(appid.getText());
                app1.setName(appname.getText());
                app1.setPrice(Double.parseDouble(price11.getText()));
                app1.setCategory(category11.getText());
                app1.setAgeGroup(agegroup.getText());
                app.updateApp(app1);
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
        ReviewMongoManager review=new ReviewMongoManager();
      //  List<Review> data= review.showReviewsOfApp(appid.getText().toString(), 0);
        ObservableList<String> items = FXCollections.observableArrayList();
        MongoDriver driver = new MongoDriver();
        MongoCollection<Document> collection = driver.getCollection("review");
        // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

        Consumer<Document> processBlock = new Consumer<Document>() {
            @Override
            public void accept(Document document) {
                items.add((String) document.get("content"));

                Object id= (Object) document.get("_id");
                String content= (String) document.get("content");
                Double score= (Double) document.get("score");
                String username= (String) document.get("username");
                String idapp= (String) document.get("appId");
                if(score== null)
                    score=0.0;
                reviewdataa.add(new reviewData(
                        id,
                        content,
                        score,
                        username,
                        idapp
                        ));
            }

        };
        review_tble.setItems(null);
        review_tble.setItems(reviewdataa);


        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$match", new Document()
                                .append("appId", text)
                        ),
                new Document()
                        .append("$skip", 0.0),
                new Document()
                        .append("$limit", 10.0)
        );

        collection.aggregate(pipeline)
                .allowDiskUse(false)
                .forEach(processBlock);
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
        TableColumn<reviewData, Void> colBtn = new TableColumn("Update");

        Callback<TableColumn<reviewData, Void>, TableCell<reviewData, Void>> cellFactory = new Callback<TableColumn<reviewData, Void>, TableCell<reviewData, Void>>() {
            @Override
            public TableCell<reviewData, Void> call(final TableColumn<reviewData, Void> param) {
                final TableCell<reviewData, Void> cell = new TableCell<reviewData, Void>() {

                    private final Button btn = new Button("Update");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            reviewData data = getTableView().getItems().get(getIndex());


                            Stage newStage = new Stage();
                            VBox comp = new VBox();
                            TextArea contenttxt = new TextArea(getTableView().getItems().get(getIndex()).getContent());

                            Button update = new Button("Update");
                            update.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {

                                    ReviewMongoManager reviewMongoManager=new ReviewMongoManager();
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
                            reviewData data = getTableView().getItems().get(getIndex());
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

        TableColumn<reviewData, Void> colBtn = new TableColumn("Delete");

        Callback<TableColumn<reviewData, Void>, TableCell<reviewData, Void>> cellFactory = new Callback<TableColumn<reviewData, Void>, TableCell<reviewData, Void>>() {
            @Override
            public TableCell<reviewData, Void> call(final TableColumn<reviewData, Void> param) {
                final TableCell<reviewData, Void> cell = new TableCell<reviewData, Void>() {
                    private final Button btn = new Button("Delete");

                    {

                        btn.setOnAction((ActionEvent event) -> {
                            reviewData data = getTableView().getItems().get(getIndex());
                            System.out.println("selectedData: " + data);

                            int dialogButton = JOptionPane.YES_NO_OPTION;
                            int dialogResult = JOptionPane.showConfirmDialog(null, "Would You Like to remove this review?", "Removing", dialogButton);
                            if (dialogResult == JOptionPane.YES_OPTION) {
                                // Saving code here
                                ReviewMongoManager review=new ReviewMongoManager();
                                Review r=new Review();
                                Object _id=getTableView().getItems().get(getIndex()).get_id();
                                r.set_id(_id);
                                //review.delete(r);
                                MongoDriver driver=new MongoDriver();
                                try {
                                    MongoCollection<Document> reviewCollection = driver.getCollection("review");
                                    DeleteResult result = reviewCollection.deleteOne(eq("_id", _id));
                                    if (result.getDeletedCount() == 0) {
                                        System.out.println("Review to delete not found");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
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
                            reviewData data = getTableView().getItems().get(getIndex());
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
        MongoDriver driver = new MongoDriver();
        MongoCollection<Document> collection = driver.getCollection("user");
        Document query1 = new Document();
        query1.append("_id", userid);
        String check = "";
        if (collection.find(query1).iterator().hasNext() && collection.find(query1).iterator().next().get("role").equals("Admin")) {
            return check = "Admin";
        } else if (collection.find(query1).iterator().hasNext() && collection.find(query1).iterator().next().get("role").equals("Developer")) {
            return check = "Developer";
        } else  {
            return check = "Normal";
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
