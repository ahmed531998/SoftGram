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
import javafx.geometry.Orientation;
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
import java.util.*;
import java.util.function.Consumer;


public class Admin implements Initializable {

    String app_id;

    @FXML TextField followertxt1,followertxt;
    @FXML ListView userlist, listcommon,listfav,listfav11,listfav1;
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

    @FXML private Label appid;
    int page = 0;
    int p = 0;
    int p1=0;
    ObservableList<App> app_data = FXCollections.observableArrayList();


    public void Add_apps(ActionEvent actionEvent) {

        if (_id.getText().isEmpty() || appname.getText().isEmpty() || price.getText().isEmpty() || dev_email.getText().isEmpty() || dev_web.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Fields required");
        } else {
            // Apps app = new Apps();
            App app=new App();
            User user=new User();
            user.setUsername(appid.getText());
            user.setEmail(dev_email.getText());
            user.setWebsite(dev_web.getText());

            app.setId(_id.getText());
            app.setCategory(category.getText());
            app.setAdSupported(Boolean.parseBoolean(ad_supported.getItems().toString()));
            app.setInAppPurchase(Boolean.parseBoolean(app_purchase.getItems().toString()));
            app.setAgeGroup(age_group.getText());
            app.setInstallCount(Integer.parseInt(installscount.getText()));
            app.setName(appname.getText());
            app.setCurrency(currency.getText());
            app.setPrice(Double.parseDouble(price.getText()));
            app.setRatingCount(Integer.parseInt(ratingcount.getText()));
            app.setDeveloper(user);
            AppMongoNeo4jManager app1=new AppMongoNeo4jManager();
            app1.addApp(app, user);
            //this function
            AppNeo4jManager neo = new AppNeo4jManager();
            neo.followOrDevelopApp(user, app, Relation.RelationType.DEVELOP);

            JOptionPane.showMessageDialog(null, "Added Successfully");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "True",
                        "False"
                );
        app_purchase.getItems().addAll(options);
        ad_supported.getItems().addAll(options);
       // commonapps();
        suggestedusers();
        suggestedapps();
        commonapps();
        fav_catfun();
        appoffollowers();
        appid.setVisible(false);
        FollowAppButtonToTable();
        FollowUserButtonToTable();
        followerappsfun();


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
        deleteButtonToTable();
        updateButtonToTable();
        MostPopularApps();

        search_table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event){
                if(event.getClickCount() == 2){
                    App app=new App();
                    try {
                        //Load second scene
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("ApplicationMain.fxml"));
                        Parent root = loader.load();

                        //Get controller of scene2
                        appid.setText(search_table.getSelectionModel().getSelectedItem().get_id());
                        System.out.println(appid.getText());
                        ApplicationMain scene2Controller = loader.getController();
                        //Pass whatever data you want. You can have multiple method calls here
                        scene2Controller.transferMessage(appid.getText());

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
                    App app=new App();
                    try {
                        //Load second scene
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("ApplicationMain.fxml"));
                        Parent root = loader.load();

                        //Get controller of scene2
                        appid.setText(search_table2.getSelectionModel().getSelectedItem().get_id());
                        ApplicationMain scene2Controller = loader.getController();
                        //Pass whatever data you want. You can have multiple method calls here
                        scene2Controller.transferMessage(appid.getText());

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
                    App app=new App();
                    try {
                        //Load second scene
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("ApplicationMain.fxml"));
                        Parent root = loader.load();

                        //Get controller of scene2
                        appid.setText(search_table3.getSelectionModel().getSelectedItem().get_id());
                        ApplicationMain scene2Controller = loader.getController();
                        //Pass whatever data you want. You can have multiple method calls here
                        scene2Controller.transferMessage(appid.getText());

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
        appid.setText(message);
        app_id=message;

       System.out.println(appid.getText());
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
                        apppurchases, 0, 0.0));
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


    private void deleteButtonToTable() {
        TableColumn<AppData, Void> colBtn = new TableColumn("Delete");

        Callback<TableColumn<AppData, Void>, TableCell<AppData, Void>> cellFactory = new Callback<TableColumn<AppData, Void>, TableCell<AppData, Void>>() {
            @Override
            public TableCell<AppData, Void> call(final TableColumn<AppData, Void> param) {
                final TableCell<AppData, Void> cell = new TableCell<AppData, Void>() {

                    private final Button btn = new Button("Delete");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            AppData data = getTableView().getItems().get(getIndex());
                            System.out.println("selectedData: " + data);

                            int dialogButton = JOptionPane.YES_NO_OPTION;
                            int dialogResult = JOptionPane.showConfirmDialog(null, "Would You Like to remove this application?", "Removing", dialogButton);
                            if (dialogResult == JOptionPane.YES_OPTION) {
                                // Saving code here
                                AppMongoNeo4jManager app=new AppMongoNeo4jManager();
                                it.unipi.softgram.entities.App app_id=new it.unipi.softgram.entities.App();
                                app_id.setId(getTableView().getItems().get(getIndex()).get_id());
                                app.removeApp(app_id);
                                JOptionPane.showMessageDialog(null, "Removed Successfully");
                                ClearTable(search_table);
                                findApp("");
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

        search_table.getColumns().add(colBtn);

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

    private void updateButtonToTable() {
        TableColumn<AppData, Void> colBtn = new TableColumn("Update");

        Callback<TableColumn<AppData, Void>, TableCell<AppData, Void>> cellFactory = new Callback<TableColumn<AppData, Void>, TableCell<AppData, Void>>() {
            @Override
            public TableCell<AppData, Void> call(final TableColumn<AppData, Void> param) {
                final TableCell<AppData, Void> cell = new TableCell<AppData, Void>() {

                    private final Button btn = new Button("Update");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            AppData data = getTableView().getItems().get(getIndex());
                            System.out.println("selectedData: " + data);

                            MongoDriver driver = new MongoDriver();
                            MongoCollection<Document> collection = driver.getCollection("app");

                            double price = getTableView().getItems().get(getIndex()).getPrice();
                            String name = getTableView().getItems().get(getIndex()).getName();
                            String category = getTableView().getItems().get(getIndex()).getCategory();
                            String ageGroup = getTableView().getItems().get(getIndex()).getAgeGroup();
                            Stage newStage = new Stage();
                            VBox comp = new VBox();
                            TextField appname = new TextField(name);
                            TextField price1 = new TextField("" + price);
                            TextField category1 = new TextField("" + category);
                            TextField agegroup = new TextField(ageGroup);
                            Button update = new Button("Update");
                            update.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {

                                    AppMongoNeo4jManager app=new AppMongoNeo4jManager();
                                    App app1=new App();
                                    app1.setId(getTableView().getItems().get(getIndex()).get_id());
                                    app1.setName(appname.getText().toString());
                                    app1.setCategory(category1.getText().toString());
                                    app1.setPrice(Double.parseDouble(price1.getText().toString()));
                                    app1.setAgeGroup(agegroup.getText().toString());
                                    if (app1.getCategory() != null)
                                        app.updateCategory(app1);
                                    if(app1.getName() != null)
                                        app.updateName(app1);

                                    /*Document query = new Document();
                                    query.append("_id", getTableView().getItems().get(getIndex()).get_id());
                                    Document setData = new Document();
                                    setData.append("name", appname.getText().toString())
                                            .append("price", Double.parseDouble(price1.getText().toString()))
                                            .append("category", category1.getText().toString())
                                            .append("ageGroup", agegroup.getText().toString())
                                    ;
                                    Document update = new Document();
                                    update.append("$set", setData);
                                    //To update single Document
                                    collection.updateOne(query, update);*/
                                    JOptionPane.showMessageDialog(null, "Updated Successfully");
                                    ClearTable(search_table);
                                    findApp("");
                                }
                            });

                            comp.getChildren().add(appname);
                            comp.getChildren().add(price1);
                            comp.getChildren().add(category1);
                            comp.getChildren().add(agegroup);
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
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);

        search_table.getColumns().add(colBtn);

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
                        false, numberOfReviews, avg));
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
        //App.setRoot("users");
        //((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    public void appmainpage(ActionEvent actionEvent) throws IOException {
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
          List<App> data= app.browseCommonApps();
          ObservableList<String> items = FXCollections.observableArrayList();
          String _id="";
          String name = "";
          String category="";
          for (App a: data){
              items.add(a.toAppDocument().get("_id") +" "+ a.toAppDocument().get("name"));
               _id= (String) a.toAppDocument().get("_id");
               name= (String) a.toAppDocument().get("name");
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
                      true, 0, 0.0));
          }



          suggestedapps_table.setItems(null);
          suggestedapps_table.setItems(suggestedapp);
    }

    public void suggestedusers(){
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
        List<App> data= app.browseCommonApps();
        ObservableList<String> items = FXCollections.observableArrayList();
        String _id="";
        String name = "";
        String category="";
        for (App a: data){
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
                    true, 0, 0.0));
        }



        commonapp_table.setItems(null);
        commonapp_table.setItems(commonapp);
    }
    //neo4j
    public void Followedapps(ActionEvent actionEvent) {

        AppNeo4jManager app=new AppNeo4jManager();
        User u=new User();
        u.setUsername(followertxt1.getText().toString());
        List<App> data= app.browseFollowedApps(u);
        ObservableList<String> items = FXCollections.observableArrayList();
        if(data.isEmpty()){
            items.add("No data");
        }
        for (App a: data){
            items.add((String) a.toAppDocument().get("_id"));

        }

        listfav11.setItems(items);
    }
    public void followerappsfun(){
        AppNeo4jManager app=new AppNeo4jManager();
        User u=new User();
        u.setUsername(appid.getText().toString());
        List<App> data= app.browseFollowedApps(u);
        ObservableList<String> items = FXCollections.observableArrayList();
        if(data.isEmpty()){
            items.add("No data");
        }
        for (App a: data){
            items.add((String) a.toAppDocument().get("_id"));

        }

        listfav11.setItems(items);
    }
            //neo4j
    public void app_of_followers(ActionEvent actionEvent) {

        AppNeo4jManager app=new AppNeo4jManager();
        User u=new User();
        u.setUsername(followertxt.getText().toString());
        List<App> data= app.browseAppsOfFollowers(u);
        ObservableList<String> items = FXCollections.observableArrayList();
        if(data.isEmpty()){
            items.add("No data");
        }
        for (App a: data){
            items.add((String) a.toAppDocument().get("_id"));

        }
        listfav1.setItems(items);
    }
    public void appoffollowers(){
        AppNeo4jManager app=new AppNeo4jManager();
        User u=new User();
        u.setUsername(appid.getText().toString());
        List<App> data= app.browseAppsOfFollowers(u);
        ObservableList<String> items = FXCollections.observableArrayList();
        if(data.isEmpty()){
            items.add("No data");
        }
        for (App a: data){
            items.add((String) a.toAppDocument().get("_id"));

        }
        listfav1.setItems(items);
    }
    //neo4j
    public void fav_cat(ActionEvent actionEvent) {
        AppNeo4jManager app=new AppNeo4jManager();
        User u=new User();
        u.setUsername(favtxt.getText().toString());
        List<App> data= app.browseFavoriteCategory(u);
        ObservableList<String> items = FXCollections.observableArrayList();
        if(data.isEmpty()){
            items.add("No data");
        }
        for (App a: data){
            items.add((String) a.toAppDocument().get("_id"));

        }

        listfav.setItems(items);
    }
    public void fav_catfun(){
        AppNeo4jManager app=new AppNeo4jManager();
        User u=new User();
        u.setUsername(appid.getText());
        List<App> data= app.browseFavoriteCategory(u);
        ObservableList<String> items = FXCollections.observableArrayList();
        if(data.isEmpty()){
            items.add("No data");
        }
        for (App a: data){
            items.add(String.valueOf(data.get(0)));

        }
        listfav.setItems(items);
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
                            App app1=new App();
                            user.setUsername(appid.getText().toString());
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
                            App app1=new App();
                            user.setUsername(appid.getText().toString());
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

