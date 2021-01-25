package it.unipi.softgram.org.example;

import com.mongodb.client.MongoCollection;
import it.unipi.softgram.controller.mongoneo4j.AppMongoNeo4jManager;
import it.unipi.softgram.controller.neo4j.AppNeo4jManager;
import it.unipi.softgram.controller.neo4j.UserNeo4jManager;
import it.unipi.softgram.entities.App;
import it.unipi.softgram.entities.User;
import it.unipi.softgram.table_chooser.AppData;
import it.unipi.softgram.table_chooser.MostPopCat;
import it.unipi.softgram.table_chooser.Userdata;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.bson.BsonRegularExpression;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class NormalUser implements Initializable {
    @FXML
    TextField followertxt1,followertxt;
    @FXML
    ListView userlist, listcommon,listfav,listfav11,listfav1;
    public Button search_btn_pop;
    @FXML
    private ComboBox app_purchase, ad_supported;
    @FXML
    private DatePicker released, last_update;
    @FXML
    TextField dev_email, dev_web, _id, favtxt,appname, price, category, ratingcount, installscount, size, age_group, currency, searchtxt, text_pop, text_year;
    ObservableList<AppData> app_data_array = FXCollections.observableArrayList();
    ObservableList<AppData> app_data_array2 = FXCollections.observableArrayList();
    ObservableList<MostPopCat> app_data_array1 = FXCollections.observableArrayList();
    ObservableList<MostPopCat> app_data_array3 = FXCollections.observableArrayList();

    ObservableList<AppData> suggestedapp = FXCollections.observableArrayList();
    ObservableList<AppData> commonapp = FXCollections.observableArrayList();
    ObservableList<Userdata> suggestedusers = FXCollections.observableArrayList();

    @FXML TableView<Userdata> suggesteduser_table;
    @FXML private TableColumn<Userdata, String> username_col;

    @FXML
    TableView<AppData> search_table,suggestedapps_table,commonapp_table;
    @FXML
    TableView<AppData> search_table2;
    @FXML
    TableView<MostPopCat> search_table3;
    @FXML
    private TableColumn<AppData, String> app_id_col, app_id_col2,appid_col,appid_com_col;
    @FXML
    private TableColumn<AppData, String> appname_col, appname_col2,name_col2,apppname_col,appname_com_col;
    @FXML
    private TableColumn<AppData, Boolean> adsupported_col;
    @FXML
    private TableColumn<AppData, Double> price_col;
    @FXML
    private TableColumn<AppData, Date> released_col;
    @FXML
    private TableColumn<AppData, String> cat_col,category_col;
    @FXML
    private TableColumn<MostPopCat, String> cate_col3;
    @FXML
    private TableColumn<AppData, Integer> age_col;
    @FXML
    private TableColumn<AppData, Date> last_col;
    @FXML
    private TableColumn<AppData, Boolean> is_purchasesCol;
    @FXML
    private TableColumn<AppData, Integer> num_col2;
    @FXML
    private TableColumn<AppData, Integer> avg_col2;
    @FXML
    private TableColumn<MostPopCat, Double> avg_col3;
    @FXML
    private TableColumn<MostPopCat, String> app_id_col3;
    @FXML
    private TableColumn<MostPopCat, String> name_col3;


    @FXML private Label userid,appid;
    int page = 0;
    int p = 0;
    int p1=0;
    ObservableList<App> app_data = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // commonapps();
        suggestedapps();
        commonapps();
        userid.setVisible(false);
        FollowAppButtonToTable();
        FollowUserButtonToTable();



        app_id_col.setCellValueFactory(new PropertyValueFactory<>("_id"));
        appname_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        adsupported_col.setCellValueFactory(new PropertyValueFactory<>("adSupported"));
        price_col.setCellValueFactory(new PropertyValueFactory<>("price"));
        released_col.setCellValueFactory(new PropertyValueFactory<>("released"));
        cat_col.setCellValueFactory(new PropertyValueFactory<>("category"));
        age_col.setCellValueFactory(new PropertyValueFactory<>("ageGroup"));

        last_col.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));

        is_purchasesCol.setCellValueFactory(new PropertyValueFactory<>("inAppPurchase"));

        //suggested apps (Common)
        appid_col.setCellValueFactory(new PropertyValueFactory<>("_id"));
        apppname_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        category_col.setCellValueFactory(new PropertyValueFactory<>("category"));

        //common
        appid_com_col.setCellValueFactory(new PropertyValueFactory<>("_id"));
        appname_com_col.setCellValueFactory(new PropertyValueFactory<>("name"));

        //suggested users
        username_col.setCellValueFactory(new PropertyValueFactory<>("username"));

        app_id_col2.setCellValueFactory(new PropertyValueFactory<>("_id"));
        appname_col2.setCellValueFactory(new PropertyValueFactory<>("name"));
        name_col2.setCellValueFactory(new PropertyValueFactory<>("name"));
        num_col2.setCellValueFactory(new PropertyValueFactory<>("numberOfReviews"));
        avg_col2.setCellValueFactory(new PropertyValueFactory<>("Avg"));

        app_id_col3.setCellValueFactory(new PropertyValueFactory<>("_id"));
        name_col3.setCellValueFactory(new PropertyValueFactory<>("name"));
        cate_col3.setCellValueFactory(new PropertyValueFactory<>("category"));
        avg_col3.setCellValueFactory(new PropertyValueFactory<>("Avg"));

        findApp("");
        MostPopularApps();

        search_table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event){
                if(event.getClickCount() == 2){
                    it.unipi.softgram.entities.App app=new it.unipi.softgram.entities.App();
                    try {
                        //Load second scene
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("ApplicationMain.fxml"));
                        Parent root = loader.load();

                        //Get controller of scene2
                        appid.setText(search_table.getSelectionModel().getSelectedItem().get_id());
                        userid.setText(userid.getText());
                        ApplicationMain scene2Controller = loader.getController();
                        //Pass whatever data you want. You can have multiple method calls here
                        scene2Controller.transferMessage(appid.getText()); //appid
                        scene2Controller.transferMessage1(userid.getText()); //userid

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
                    it.unipi.softgram.entities.App app=new it.unipi.softgram.entities.App();
                    try {
                        //Load second scene
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("ApplicationMain.fxml"));
                        Parent root = loader.load();

                        //Get controller of scene2
                        appid.setText(search_table2.getSelectionModel().getSelectedItem().get_id());
                        ApplicationMain scene2Controller = loader.getController();
                        //Pass whatever data you want. You can have multiple method calls here
                        scene2Controller.transferMessage(appid.getText());
                        scene2Controller.transferMessage1(userid.getText());

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
                    it.unipi.softgram.entities.App app=new it.unipi.softgram.entities.App();
                    try {
                        //Load second scene
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("ApplicationMain.fxml"));
                        Parent root = loader.load();

                        //Get controller of scene2
                        appid.setText(search_table3.getSelectionModel().getSelectedItem().get_id());
                        ApplicationMain scene2Controller = loader.getController();
                        //Pass whatever data you want. You can have multiple method calls here
                        scene2Controller.transferMessage(appid.getText());
                        scene2Controller.transferMessage1(userid.getText());

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
    public void transferMessage(String message) {
        //Display the message
        userid.setText(message);

        fav_catfun(message);
        appoffollowers(message);
        followerappsfun(message);
        suggestedusers(message);
    }

    public void add_function(ActionEvent actionEvent) {
        ClearTable(search_table);
        findApp(searchtxt.getText());
    }

    public void next_function(ActionEvent actionEvent) {
        ClearTable(search_table);
        page++;
        findApp("");
    }

    public void back_function(ActionEvent actionEvent) {
        ClearTable(search_table);
        page--;
        findApp("");
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

    public void findApp(String text) {
        MongoDriver driver = new MongoDriver();
        MongoCollection<Document> collection = driver.getCollection("app");

        // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

        Consumer<Document> processBlock = new Consumer<Document>() {
            @Override
            public void accept(Document document) {
                // System.out.println(document);
                String name = (String) document.get("name");
                String _id = (String) document.get("_id");
                // double adsupported = (double) document.get("adSupported");
                String lastupdated = (String) document.get("name");
                Double price = (Double) document.get("price");
                String category = (String) document.get("category");
                String agegroup = (String) document.get("ageGroup");
                String lastupdated1 = (String) document.get("name");
                boolean apppurchases = true;
                app_data_array.add(new AppData(
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
        search_table.setItems(app_data_array);

        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$match", new Document()
                                .append("$or", Arrays.asList(
                                        new Document()
                                                .append("_id", new Document()
                                                        .append("$regex", new BsonRegularExpression(text))
                                                ),
                                        new Document()
                                                .append("name", new Document()
                                                        .append("$regex", new BsonRegularExpression(text))
                                                )
                                        )
                                )
                        ),
                new Document()
                        .append("$skip", page * 10),
                new Document()
                        .append("$limit", 10)
        );

        collection.aggregate(pipeline)
                .allowDiskUse(false)
                .forEach(processBlock);
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





    public void MostPopularApps() {
        MongoDriver driver = new MongoDriver();
        MongoCollection<Document> collection = driver.getCollection("app");

        // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

        Consumer<Document> processBlock = new Consumer<Document>() {
            @Override
            public void accept(Document document) {
                // System.out.println(document);
                String name = (String) document.get("name");
                String _id = (String) document.get("_id");
                double avg = (Double) document.get("Avg");
                int numberOfReviews = (Integer) document.get("numberOfReviews");

                boolean apppurchases = true;
                app_data_array2.add(new AppData(
                        name,
                        _id,
                        1.0,
                        "",
                        0,
                        "",
                        "",
                        "",
                        false, numberOfReviews, avg,""));
            }

        };
        search_table2.setItems(null);
        search_table2.setItems(app_data_array2);


        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$project", new Document()
                                .append("_id", 1.0)
                                .append("name", 1.0)
                                .append("Avg", new Document()
                                        .append("$avg", "$reviews.score")
                                )
                                .append("numberOfReviews", new Document()
                                        .append("$cond", new Document()
                                                .append("if", new Document()
                                                        .append("$isArray", "$reviews")
                                                )
                                                .append("then", new Document()
                                                        .append("$size", "$reviews")
                                                )
                                                .append("else", 0.0)
                                        )
                                )
                        ),
                new Document()
                        .append("$sort", new Document()
                                .append("Avg", -1.0)
                                .append("numberOfReviews", -1.0)
                        ),
                new Document()
                        .append("$skip", p * 10),
                new Document()
                        .append("$limit", 10.0)
        );

        collection.aggregate(pipeline)
                .allowDiskUse(false)
                .forEach(processBlock);
    }

    public void back_fun(ActionEvent actionEvent) {
        ClearTable(search_table2);
        p--;
        MostPopularApps();
    }

    public void next_fun(ActionEvent actionEvent) {
        ClearTable(search_table2);
        p++;
        MostPopularApps();
    }

    public void search_popular_cate(ActionEvent actionEvent) {
        if (text_pop.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Field required");
        } else {
            ClearTable(search_table3);
            MongoDriver driver = new MongoDriver();
            MongoCollection<Document> collection = driver.getCollection("app");

            // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

            Consumer<Document> processBlock = new Consumer<Document>() {
                @Override
                public void accept(Document document) {

                    String name = (String) document.get("name");
                    String _id = (String) document.get("_id");
                    String category = (String) document.get("category");
                    double avg = (Double) document.get("Avg");

                    if(avg == 0){
                        avg=0.0;
                    }

                    boolean apppurchases = true;
                    app_data_array1.add(new MostPopCat(
                            _id,
                            name,
                            category, avg, ""));
                }

            };
            search_table3.setItems(null);
            search_table3.setItems(app_data_array1);
            List<? extends Bson> pipeline = Arrays.asList(
                    new Document()
                            .append("$match", new Document()
                                    .append("category", text_pop.getText().toString())
                            ),
                    new Document()
                            .append("$project", new Document()
                                    .append("_id", 1.0)
                                    .append("name", 1.0)
                                    .append("category", 1.0)
                                    .append("Avg", new Document()
                                            .append("$avg", "$reviews.score")
                                    )
                                    .append("numberOfReviews", new Document()
                                            .append("$cond", new Document()
                                                    .append("if", new Document()
                                                            .append("$isArray", "$reviews")
                                                    )
                                                    .append("then", new Document()
                                                            .append("$size", "$reviews")
                                                    )
                                                    .append("else", 0.0)
                                            )
                                    )
                            ),
                    new Document()
                            .append("$sort", new Document()
                                    .append("Avg", -1.0)
                                    .append("numberOfReviews", -1.0)
                            ),
                    new Document()
                            .append("$skip", p1*10),
                    new Document()
                            .append("$limit", 10)
            );

            collection.aggregate(pipeline)
                    .allowDiskUse(false)
                    .forEach(processBlock);
        }
    }

    public void seatch_function_year(ActionEvent actionEvent) {
        if (text_year.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Field required");
        } else {
            ClearTable(search_table3);
            MongoDriver driver = new MongoDriver();
            MongoCollection<Document> collection = driver.getCollection("app");

            // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

            Consumer<Document> processBlock = new Consumer<Document>() {
                @Override
                public void accept(Document document) {
                    String name = (String) document.get("name");
                    String _id = (String) document.get("_id");
                    String category = (String) document.get("category");
                    String released = "";


                    boolean apppurchases = true;
                    app_data_array3.add(new MostPopCat(
                            _id,
                            name,
                            category, 0.0, ""));
                }

            };
            ClearTable(search_table3);
            search_table3.setItems(null);
            search_table3.setItems(app_data_array3);


            List<? extends Bson> pipeline = Arrays.asList(
                    new Document()
                            .append("$project", new Document()
                                    .append("_id", 1.0)
                                    .append("name", 1.0)
                                    .append("category", 1.0)
                                    .append("released", 1.0)
                                    .append("Avg", new Document()
                                            .append("$avg", "$reviews.score")
                                    )
                                    .append("numberOfReviews", new Document()
                                            .append("$cond", new Document()
                                                    .append("if", new Document()
                                                            .append("$isArray", "$reviews")
                                                    )
                                                    .append("then", new Document()
                                                            .append("$size", "$reviews")
                                                    )
                                                    .append("else", 0.0)
                                            )
                                    )
                                    .append("year", new Document()
                                            .append("$year", "$released")
                                    )
                            ),
                    new Document()
                            .append("$match", new Document()
                                    .append("year", Integer.parseInt(text_year.getText()))
                            ),
                    new Document()
                            .append("$sort", new Document()
                                    .append("Avg", -1.0)
                                    .append("numberOfReviews", -1.0)
                            ),
                    new Document()
                            .append("$skip", p1*10),
                    new Document()
                            .append("$limit", 10)
            );

            collection.aggregate(pipeline)
                    .allowDiskUse(false)
                    .forEach(processBlock);

        }
    }

    public void usersmainpage(ActionEvent actionEvent) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("test.fxml"));
            Parent root = loader.load();

            Normaluserspage scene2Controller = loader.getController();
            scene2Controller.transferMessage(userid.getText());

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
            scene2Controller.transferMessage(userid.getText());

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

    public void nextfun(ActionEvent actionEvent) {
        ClearTable(search_table3);
        p++;
        // findApp("");
    }

    public void backfun(ActionEvent actionEvent) {
        ClearTable(search_table3);
        p--;
        //findApp("");
    }

    public void signout_fun(ActionEvent actionEvent) throws IOException {
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

    public  void suggestedapps(){
        AppNeo4jManager app=new AppNeo4jManager();
        List<it.unipi.softgram.entities.App> data= app.browseCommonApps();
        ObservableList<String> items = FXCollections.observableArrayList();
        String _id="";
        String name = "";
        String category="";
        for (it.unipi.softgram.entities.App a: data){
            items.add(a.toAppDocument().get("_id") +" "+ a.toAppDocument().get("AppName"));
            _id= (String) a.toAppDocument().get("_id");
            name= (String) a.toAppDocument().get("AppName");
            category= (String) a.toAppDocument().get("category");
            suggestedapp.add(new AppData(
                    name,
                    _id,
                    0.0,
                    "",
                    0.0,
                    category,
                    "",
                    "",
                    true, 0, 0.0,""));
        }



        suggestedapps_table.setItems(null);
        suggestedapps_table.setItems(suggestedapp);
    }

    public void suggestedusers(String username){
        UserNeo4jManager user=new UserNeo4jManager();

        List<String> data= user.browseSuggestedUsers("Young Kim",10);
        ObservableList<String> items = FXCollections.observableArrayList();
        for (int i=0; i<data.size();i++)
        {
            items.add(data.get(i));
            suggestedusers.add(new Userdata(
                    data.get(i),"","","","",""
            ));
        }
        suggesteduser_table.setItems(null);
        suggesteduser_table.setItems(suggestedusers);

    }


    public void commonapps(){
        AppNeo4jManager app=new AppNeo4jManager();
        List<it.unipi.softgram.entities.App> data= app.browseCommonApps();
        ObservableList<String> items = FXCollections.observableArrayList();
        String _id="";
        String name = "";
        String category="";
        for (it.unipi.softgram.entities.App a: data){
            items.add(a.toAppDocument().get("_id") +" "+ a.toAppDocument().get("name"));
            _id= (String) a.toAppDocument().get("_id");
            name= (String) a.toAppDocument().get("name");
            category= (String) a.toAppDocument().get("category");
            commonapp.add(new AppData(
                    name,
                    _id,
                    0.0,
                    "",
                    0.0,
                    category,
                    "",
                    "",
                    true, 0, 0.0,""));
        }



        commonapp_table.setItems(null);
        commonapp_table.setItems(commonapp);
    }
    //neo4j
    public void Followedapps(ActionEvent actionEvent) {

        AppNeo4jManager app=new AppNeo4jManager();
        User u=new User();
        u.setUsername(followertxt1.getText().toString());
        List<it.unipi.softgram.entities.App> data= app.browseFollowedApps(u);
        ObservableList<String> items = FXCollections.observableArrayList();
        if(data.isEmpty()){
            items.add("No data");
        }
        for (it.unipi.softgram.entities.App a: data){
            items.add((String) a.toAppDocument().get("_id"));

        }

        listfav11.setItems(items);
    }

    public void followerappsfun(String message){
        AppNeo4jManager app=new AppNeo4jManager();
        User u=new User();
        u.setUsername(message);
        List<it.unipi.softgram.entities.App> data= app.browseFollowedApps(u);
        ObservableList<String> items = FXCollections.observableArrayList();
        if(data.isEmpty()){
            items.add("No data");
        }
        for (it.unipi.softgram.entities.App a: data){
            items.add((String) a.toAppDocument().get("_id"));

        }

        listfav11.setItems(items);
    }
    //neo4j
    public void app_of_followers(ActionEvent actionEvent) {

        AppNeo4jManager app=new AppNeo4jManager();
        User u=new User();
        u.setUsername(followertxt.getText().toString());
        List<it.unipi.softgram.entities.App> data= app.browseAppsOfFollowers(u);
        ObservableList<String> items = FXCollections.observableArrayList();
        if(data.isEmpty()){
            items.add("No data");
        }
        for (it.unipi.softgram.entities.App a: data){
            items.add((String) a.toAppDocument().get("_id"));

        }
        listfav1.setItems(items);
    }

    public void appoffollowers(String username){
        AppNeo4jManager app=new AppNeo4jManager();
        User u=new User();
        u.setUsername(username);
        List<it.unipi.softgram.entities.App> data= app.browseAppsOfFollowers(u);
        ObservableList<String> items = FXCollections.observableArrayList();
        if(data.isEmpty()){
            items.add("No data");
        }
        for (it.unipi.softgram.entities.App a: data){
            items.add((String) a.toAppDocument().get("_id"));

        }
        listfav1.setItems(items);
    }
    //neo4j
    public void fav_cat(ActionEvent actionEvent) {
        AppNeo4jManager app=new AppNeo4jManager();
        User u=new User();
        u.setUsername(favtxt.getText().toString());
        List<it.unipi.softgram.entities.App> data= app.browseFavoriteCategory(u);
        ObservableList<String> items = FXCollections.observableArrayList();
        if(data.isEmpty()){
            items.add("No data");
        }
        for (it.unipi.softgram.entities.App a: data){
            items.add((String) a.toAppDocument().get("_id"));

        }

        listfav.setItems(items);
    }

    public void fav_catfun(String username){
        AppNeo4jManager app=new AppNeo4jManager();
        User u=new User();
        u.setUsername(username);
        List<it.unipi.softgram.entities.App> data= app.browseFavoriteCategory(u);
        ObservableList<String> items = FXCollections.observableArrayList();
        if(data.isEmpty()){
            items.add("No data");
        }
        for (it.unipi.softgram.entities.App a: data){
            items.add((String) a.toAppDocument().get("_id"));

        }
        listfav.setItems(items);
    }

    public void statismainpage(ActionEvent actionEvent) {

    }
    private void FollowAppButtonToTable() {
        TableColumn<AppData, Void> colBtn = new TableColumn("Follow");

        Callback<TableColumn<AppData, Void>, TableCell<AppData, Void>> cellFactory = new Callback<TableColumn<AppData, Void>, TableCell<AppData, Void>>() {
            @Override
            public TableCell<AppData, Void> call(final TableColumn<AppData, Void> param) {
                final TableCell<AppData, Void> cell = new TableCell<AppData, Void>() {

                    private final Button btn = new Button("Follow");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            AppNeo4jManager app=new AppNeo4jManager();
                            User user=new User();
                            it.unipi.softgram.entities.App app1=new it.unipi.softgram.entities.App();
                            user.setUsername(userid.getText().toString());
                            app1.setId(getTableView().getItems().get(getIndex()).get_id());
                            if(btn.getText().equals("Follow")){
                                app.followOrDevelopApp(user,app1, Relation.RelationType.FOLLOW);
                                JOptionPane.showMessageDialog(null, "You followed this app");
                                btn.setText("Unfollow");
                                ClearTable(suggestedapps_table);
                                suggestedapps();}else{
                                app.unfollowApp(user,app1);
                                JOptionPane.showMessageDialog(null, "You unfollowed this app");
                                btn.setText("Follow");
                                ClearTable(suggestedapps_table);
                                suggestedapps();
                            }

                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);

        suggestedapps_table.getColumns().add(colBtn);

    }



    private void FollowUserButtonToTable() {
        TableColumn<Userdata, Void> colBtn = new TableColumn("Follow");

        Callback<TableColumn<Userdata, Void>, TableCell<Userdata, Void>> cellFactory = new Callback<TableColumn<Userdata, Void>, TableCell<Userdata, Void>>() {
            @Override
            public TableCell<Userdata, Void> call(final TableColumn<Userdata, Void> param) {
                final TableCell<Userdata, Void> cell = new TableCell<Userdata, Void>() {

                    private final Button btn = new Button("Follow");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            AppNeo4jManager app=new AppNeo4jManager();
                            User user=new User();
                            it.unipi.softgram.entities.App app1=new App();
                            user.setUsername(userid.getText().toString());
                            app1.setId(getTableView().getItems().get(getIndex()).getUsername());
                            if(btn.getText().equals("Follow")){
                                app.followOrDevelopApp(user,app1, Relation.RelationType.FOLLOW);
                                JOptionPane.showMessageDialog(null, "You followed this app");
                                btn.setText("Unfollow");
                                ClearTable(suggestedapps_table);
                                suggestedapps();}else{
                                app.unfollowApp(user,app1);
                                JOptionPane.showMessageDialog(null, "You unfollowed this app");
                                btn.setText("Follow");
                                ClearTable(suggestedapps_table);
                                suggestedapps();
                            }

                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);

        suggesteduser_table.getColumns().add(colBtn);

    }




}