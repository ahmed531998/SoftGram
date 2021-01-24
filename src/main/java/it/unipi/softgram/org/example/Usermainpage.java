package it.unipi.softgram.org.example;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.softgram.controller.mongo.ReviewMongoManager;
import it.unipi.softgram.controller.mongo.UserMongoManager;
import it.unipi.softgram.controller.neo4j.UserNeo4jManager;
import it.unipi.softgram.entities.Review;
import it.unipi.softgram.entities.User;
import it.unipi.softgram.table_chooser.AppData;
import it.unipi.softgram.table_chooser.reviewData;
import it.unipi.softgram.utilities.drivers.MongoDriver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
import java.util.*;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.eq;

public class Usermainpage implements Initializable {

    //@FXML private Label AppID;
    @FXML private Text name_tfield, email_tfield, web_tfield, role_tfield;
    ObservableList<AppData> apps = FXCollections.observableArrayList();
    @FXML Label userid;
    @FXML Button followbtn,home,remove_btn,update_btn;
    User user=new User();
    User user2=new User();
    String username1="";
    ObservableList<reviewData> reviewdataa = FXCollections.observableArrayList();
    @FXML
    TableView<AppData> search_table;
    @FXML
    private TableColumn<AppData, String> app_id_col;
    @FXML
    private TableColumn<AppData, String> appname_col;
    @FXML
    private TableColumn<AppData, Boolean> adsupported_col;
    @FXML
    private TableColumn<AppData, Double> price_col;
    @FXML
    private TableColumn<AppData, Date> released_col;
    @FXML
    private TableColumn<AppData, String> cat_col;
    @FXML
    private TableColumn<AppData, Integer> age_col;
    @FXML
    private TableColumn<AppData, Date> last_col;
    @FXML
    private TableColumn<AppData, Boolean> is_purchasesCol;
    @FXML
    TableView<reviewData> review_tble;
    @FXML private TableColumn<reviewData, String> id_Col;
    @FXML private TableColumn<reviewData, String> content_col;
    @FXML private TableColumn<reviewData, Double> score_col;

    public void transferMessage(String message) {
       // AppID.setText(message);

        user.setUsername(message);
        userid.setText(message);

        String check=check(message);
        boolean check2=check2(message, name_tfield.getText());
        remove_btn.setVisible(false);
        update_btn.setVisible(false);
        if(check2==true){
            remove_btn.setVisible(true);
            update_btn.setVisible(true);
        }else{
            if(check.equals("Admin")){
                remove_btn.setVisible(true);
                update_btn.setVisible(true);
            }else{
                remove_btn.setVisible(false);
                update_btn.setVisible(false);
            }
        }


    }
    public void transferMessage1(String message) {
        // AppID.setText(message);
        user2.setUsername(message);
        username1=message;
        findUsers(message);
        returnapps(message);
    }
    public void findUsers(String text){
        MongoDriver driver = new MongoDriver();
        MongoCollection<Document> collection = driver.getCollection("user");

        // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

        Consumer<Document> processBlock = document -> {
            //System.out.println(document);
            String username = (String) document.get("_id");
            String website = (String) document.get("website");
            String email = (String) document.get("email");
            String role = (String) document.get("role");
            name_tfield.setText(username);
            email_tfield.setText(email);
            role_tfield.setText(role);
            web_tfield.setText(website);



        };

        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$match", new Document()
                                .append("$or", Collections.singletonList(
                                        new Document()
                                                .append("_id", new Document()
                                                        .append("$regex", new BsonRegularExpression(text))
                                                )
                                        )
                                )
                        ),
                new Document()
                        .append("$skip", 0.0),
                new Document()
                        .append("$limit", 100.0)
        );

        collection.aggregate(pipeline)
                .allowDiskUse(false)
                .forEach(processBlock);
    }


    public void homepage(ActionEvent actionEvent) {
        Stage stage = (Stage) home.getScene().getWindow();
        // do what you have to do
        stage.close();
        try {
            App.setRoot("users");
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updatefun(ActionEvent actionEvent) {



        String _id = name_tfield.getText();
        String email = email_tfield.getText();
        String role = role_tfield.getText();
        String website = web_tfield.getText();

        Stage newStage = new Stage();
        VBox comp = new VBox();
        javafx.scene.control.TextField username = new javafx.scene.control.TextField(_id);
        javafx.scene.control.TextField emailtxt = new javafx.scene.control.TextField(email);
        javafx.scene.control.TextField roletxt = new javafx.scene.control.TextField(role);
        javafx.scene.control.TextField websitetxt = new javafx.scene.control.TextField(website);
        javafx.scene.control.Button update = new Button("Update");
        update.setOnAction(event -> {
            UserMongoManager user=new UserMongoManager();
            User user1=new User();
            user1.setUsername(user2.getUsername());
            user1.setWebsite(websitetxt.getText());
            user1.setRole(roletxt.getText());
            user1.setEmail(emailtxt.getText());


            user.updateUser(user1);
            JOptionPane.showMessageDialog(null, "Updated");
            name_tfield.setText("");
            role_tfield.setText("");
            email_tfield.setText("");
            web_tfield.setText("");
            name_tfield.setText(username.getText());
            role_tfield.setText(roletxt.getText());
            email_tfield.setText(emailtxt.getText());
            web_tfield.setText(websitetxt.getText());

        });

        comp.getChildren().add(username);
        comp.getChildren().add(emailtxt);
        comp.getChildren().add(roletxt);
        comp.getChildren().add(websitetxt);
        comp.getChildren().add(update);
        Scene stageScene = new Scene(comp, 300, 300);
        newStage.setScene(stageScene);
        newStage.show();

    }

    public void removefun(ActionEvent actionEvent) {
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(null, "Would You Like to remove this user?", "Removing", dialogButton);

        if (dialogResult == JOptionPane.YES_OPTION) {
            // Saving code here
            UserMongoManager usermongo=new UserMongoManager();

            usermongo.removeUser(user.getUsername());
            JOptionPane.showMessageDialog(null, "User removed successfully");
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

        }
    }

    public void followfun(ActionEvent actionEvent) {

        UserNeo4jManager useneo=new UserNeo4jManager();
        useneo.addFollow(user2.getUsername(), user.getUsername(), false);
        JOptionPane.showMessageDialog(null, "You followed this user");
        followbtn.setText("UnFollow");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



       /* UserNeo4jManager userr=new UserNeo4jManager();
        try {
            boolean result = userr.relationUserUserExists(user, user2);

            if (result == false) {
                followbtn.setText("Follow");
            } else {
                followbtn.setText("UnFollow");
            }
        }catch (NoSuchRecordException e){

        }*/


        id_Col.setCellValueFactory(new PropertyValueFactory<>("_id"));
        content_col.setCellValueFactory(new PropertyValueFactory<>("content"));
        score_col.setCellValueFactory(new PropertyValueFactory<>("score"));
        app_id_col.setCellValueFactory(new PropertyValueFactory<>("_id"));
        appname_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        adsupported_col.setCellValueFactory(new PropertyValueFactory<>("adSupported"));
        price_col.setCellValueFactory(new PropertyValueFactory<>("price"));
        released_col.setCellValueFactory(new PropertyValueFactory<>("released"));
        cat_col.setCellValueFactory(new PropertyValueFactory<>("category"));
        age_col.setCellValueFactory(new PropertyValueFactory<>("ageGroup"));
        last_col.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));
        is_purchasesCol.setCellValueFactory(new PropertyValueFactory<>("inAppPurchase"));
        updateButtonToTable();
        deleteButtonToTable();
        showReviews(name_tfield.getText());
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
                                        reviewDoc.append("appId", getTableView().getItems().get(getIndex()).getAppid());
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
                                                    showReviews(name_tfield.getText());

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
                            if(name_tfield.getText().equals(getTableView().getItems().get(getIndex()).getUsername())) {
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
    public void ClearTable(TableView x) {
        /*
         * the table should be already created before
         * table should have information
         */

        for (int i = 0; i < x.getItems().size(); i++) {
            x.getItems().clear();
        }

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
                                showReviews(name_tfield.getText());
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
                            if(name_tfield.getText().equals(getTableView().getItems().get(getIndex()).getUsername())) {
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
                                .append("username", text)
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
    public void returnapps(String text){
        System.out.println("here");

        MongoDriver driver=new MongoDriver();
        MongoCollection<Document> collection = driver.getCollection("app");
        Document query = new Document();
        query.append("developer.developerId", name_tfield.getText());

        Consumer<Document> processBlock = new Consumer<Document>() {
            @Override
            public void accept(Document document) {
                String name = (String) document.get("name");
                String _id = (String) document.get("_id");
                // double adsupported = (double) document.get("adSupported");
                String lastupdated = (String) document.get("name");
                Double price = (Double) document.get("price");
                String category = (String) document.get("category");
                String agegroup = (String) document.get("ageGroup");
                String lastupdated1 = (String) document.get("name");
                boolean apppurchases = true;

                apps.add(new AppData(
                        name,
                        _id,
                        0.0,
                        lastupdated1,
                        price,
                        category,
                        agegroup,
                        lastupdated1,
                        apppurchases, 0, 0.0,""));
            }

        };
        search_table.setItems(null);
        search_table.setItems(apps);

        collection.find(query).forEach(processBlock);
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
