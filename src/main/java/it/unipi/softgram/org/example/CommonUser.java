package it.unipi.softgram.org.example;

import it.unipi.softgram.controller.mongo.AppMongoManager;
import it.unipi.softgram.controller.mongoneo4j.AppMongoNeo4jManager;
import it.unipi.softgram.controller.neo4j.AppNeo4jManager;
import it.unipi.softgram.controller.neo4j.UserNeo4jManager;
import it.unipi.softgram.entities.App;
import it.unipi.softgram.entities.User;
import it.unipi.softgram.gui.WelcomeScreen;
import it.unipi.softgram.utilities.enumerators.Relation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
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

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import static java.lang.Integer.parseInt;

public class CommonUser implements Initializable {
    @FXML
    TextField followertxt1,followertxt;
    @FXML ListView userlist, listcommon,listfav,listfav11,listfav1;
    public Button search_btn_pop;
    @FXML
    private ComboBox app_purchase, ad_supported;
    @FXML
    private DatePicker released, last_update;
    @FXML
    TextField dev_email, dev_web, _id, favtxt,appname, price, category, ratingcount, installscount, size, age_group, currency, searchtxt, text_pop, text_year;
    ObservableList<App> app_data_array = FXCollections.observableArrayList();
    ObservableList<App> app_data_array2 = FXCollections.observableArrayList();
    ObservableList<App> app_data_array1 = FXCollections.observableArrayList();
    ObservableList<App> app_data_array3 = FXCollections.observableArrayList();

    ObservableSet<App> suggestedapp = FXCollections.observableSet();
    ObservableList<App> commonapp = FXCollections.observableArrayList();
    ObservableSet<User> suggestedusers = FXCollections.observableSet();

    @FXML TableView<User> suggesteduser_table;
    @FXML private TableColumn<User, String> username_col;

    @FXML
    TableView<App> search_table,suggestedapps_table,commonapp_table;
    @FXML
    TableView<App> search_table2;
    @FXML
    TableView<App> search_table3;
    @FXML
    private TableColumn<App, String> app_id_col, app_id_col2,appid_col,appid_com_col;
    @FXML
    private TableColumn<App, String> appname_col, appname_col2,name_col2,apppname_col,appname_com_col;
    @FXML
    private TableColumn<App, Boolean> adsupported_col;
    @FXML
    private TableColumn<App, Double> price_col;
    @FXML
    private TableColumn<App, Date> released_col;
    @FXML
    private TableColumn<App, String> cat_col,category_col;
    @FXML
    private TableColumn<App, String> cate_col3;
    @FXML
    private TableColumn<App, Integer> age_col;
    @FXML
    private TableColumn<App, Date> last_col;
    @FXML
    private TableColumn<App, Boolean> is_purchasesCol;
    @FXML
    private TableColumn<App, Integer> num_col2;
    @FXML
    private TableColumn<App, Integer> avg_col2;
    @FXML
    private TableColumn<App, Double> avg_col3;
    @FXML
    private TableColumn<App, String> app_id_col3;
    @FXML
    private TableColumn<App, String> name_col3;

    @FXML private Label userid,appid;
    int page = 0;
    int p = 0;
    int p1=0;
    ObservableList<App> app_data = FXCollections.observableArrayList();



    public Label getUserid(){return userid;}
    public TableColumn<App, String> getName_col2() {
        return name_col2;
    }
    public TableColumn<App, String> getCate_col3() {
        return cate_col3;
    }

    public DatePicker getReleased() {
        return released;
    }

    public TableColumn<App, Boolean> getAdsupported_col() {
        return adsupported_col;
    }

    public TableColumn<App, Boolean> getIs_purchasesCol() {
        return is_purchasesCol;
    }

    public TableColumn<App, Date> getLast_col() {
        return last_col;
    }

    public TableColumn<App, Date> getReleased_col() {
        return released_col;
    }

    public TableColumn<App, Double> getAvg_col3() {
        return avg_col3;
    }

    public TableColumn<App, Double> getPrice_col() {
        return price_col;
    }

    public TableColumn<App, Integer> getAge_col() {
        return age_col;
    }

    public TableColumn<App, Integer> getAvg_col2() {
        return avg_col2;
    }

    public TableColumn<App, Integer> getNum_col2() {
        return num_col2;
    }

    public Label getAppid() {
        return appid;
    }

    public TableColumn<App, String> getApp_id_col() {
        return app_id_col;
    }

    public TableColumn<App, String> getName_col3() {
        return name_col3;
    }

    public TableColumn<App, String> getAppname_com_col() {
        return appname_com_col;
    }

    public TableColumn<App, String> getApp_id_col2() {
        return app_id_col2;
    }

    public TableColumn<App, String> getApp_id_col3() {
        return app_id_col3;
    }

    public TableColumn<App, String> getAppid_col() {
        return appid_col;
    }

    public TableColumn<App, String> getAppid_com_col() {
        return appid_com_col;
    }

    public TableColumn<App, String> getAppname_col() {
        return appname_col;
    }

    public TableColumn<App, String> getAppname_col2() {
        return appname_col2;
    }

    public TextField getCategory() {
        return category;
    }

    public TableColumn<User, String> getUsername_col() {
        return username_col;
    }

    public TableColumn<App, String> getCat_col() {
        return cat_col;
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
        suggestedapps();
        getCommonApps(true);
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
        deleteButtonToTable();
        updateButtonToTable();
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
                        appid.setText(search_table.getSelectionModel().getSelectedItem().getId());
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
                    App app=new App();
                    try {
                        //Load second scene
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("ApplicationMain.fxml"));
                        Parent root = loader.load();

                        //Get controller of scene2
                        appid.setText(search_table2.getSelectionModel().getSelectedItem().getId());
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
                    try {
                        //Load second scene
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("ApplicationMain.fxml"));
                        Parent root = loader.load();

                        //Get controller of scene2
                        appid.setText(search_table3.getSelectionModel().getSelectedItem().getId());
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
    private void deleteButtonToTable() {
        TableColumn<App, Void> colBtn = new TableColumn("Delete");
        Callback<TableColumn<App, Void>, TableCell<App, Void>> cellFactory = new Callback<TableColumn<App, Void>, TableCell<App, Void>>() {
            @Override
            public TableCell<App, Void> call(final TableColumn<App, Void> param) {
                final TableCell<App, Void> cell = new TableCell<App, Void>() {
                    private final Button btn = new Button("Delete");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            App data = getTableView().getItems().get(getIndex());
                            System.out.println("selectedData: " + data);
                            int dialogButton = JOptionPane.YES_NO_OPTION;
                            int dialogResult = JOptionPane.showConfirmDialog(null, "Would You Like to remove this application?", "Removing", dialogButton);
                            if (dialogResult == JOptionPane.YES_OPTION) {
                                // Saving code here
                                AppMongoNeo4jManager app=new AppMongoNeo4jManager();
                                it.unipi.softgram.entities.App app_id=new it.unipi.softgram.entities.App();
                                app_id.setId(getTableView().getItems().get(getIndex()).getId());
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
                            //ask Mohammed

                            /*if(userid.getText().equals("")) {
                                setGraphic(btn);
                            }else{
                                setGraphic(null);
                            }*/
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
    private void updateButtonToTable() {
        TableColumn<App, Void> colBtn = new TableColumn("Update");
        Callback<TableColumn<App, Void>, TableCell<App, Void>> cellFactory = new Callback<TableColumn<App, Void>, TableCell<App, Void>>() {
            @Override
            public TableCell<App, Void> call(final TableColumn<App, Void> param) {
                final TableCell<App, Void> cell = new TableCell<App, Void>() {
                    private final Button btn = new Button("Update");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            App data = getTableView().getItems().get(getIndex());
                            System.out.println("selectedData: " + data);
                            double price = getTableView().getItems().get(getIndex()).getPrice();
                            String name = getTableView().getItems().get(getIndex()).getName();
                            String category = getTableView().getItems().get(getIndex()).getCategory();
                            String ageGroup = getTableView().getItems().get(getIndex()).getAgeGroup();
                            Stage newStage = new Stage();
                            VBox comp = new VBox();
                            TextField appname = new TextField(name);
                            //Ask Mohammed - not in developer
                            appname.setPromptText("App name");
                            TextField price1 = new TextField("" + price);
                            //Ask Mohammed - not in developer
                            price1.setPromptText("Price");
                            TextField category1 = new TextField("" + category);
                            //Ask Mohammed - not in developer
                            category1.setPromptText("Category");
                            TextField agegroup = new TextField(ageGroup);
                            //Ask Mohammed - not in developer
                            agegroup.setPromptText("Age group");
                            Button update = new Button("Update");
                            update.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    AppMongoNeo4jManager app=new AppMongoNeo4jManager();
                                    AppMongoManager appmongo=new AppMongoManager();
                                    App app1=new App();
                                    app1.setId(getTableView().getItems().get(getIndex()).getId());
                                    app1.setName(appname.getText());
                                    app1.setCategory(category1.getText());
                                    app1.setPrice(Double.parseDouble(price1.getText()));
                                    app1.setAgeGroup(agegroup.getText());
                                    if (app1.getCategory() != null)
                                        app.updateCategory(app1);
                                    if(app1.getName() != null)
                                        app.updateName(app1);
                                    appmongo.updateApp(app1);
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
                            //ask Mohammed

                            /*if(userid.getText().equals("")) {
                                setGraphic(btn);
                            }else{
                                setGraphic(null);
                            }*/
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


    public void Add_apps(ActionEvent actionEvent) {
        if (_id.getText().isEmpty() || appname.getText().isEmpty() || category.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Fields required");
        } else {
            // Apps app = new Apps();
            it.unipi.softgram.entities.App app = new App();
            User user = new User();
            user.setUsername(userid.getText());

            if (!price.getText().isEmpty()) {
                app.setPrice(Double.parseDouble(price.getText()));
            }
            if (!ad_supported.getSelectionModel().isEmpty()) {
                app.setAdSupported(Boolean.parseBoolean(ad_supported.getSelectionModel().getSelectedItem().toString()));
            }
            if (!app_purchase.getSelectionModel().isEmpty()) {
                app.setInAppPurchase(Boolean.parseBoolean(app_purchase.getSelectionModel().getSelectedItem().toString()));
            }

            if (!age_group.getText().isEmpty()) {
                app.setAgeGroup(age_group.getText());
            }

            if (!currency.getText().isEmpty()) {
                app.setCurrency(currency.getText());
            }

            if (!dev_email.getText().isEmpty()) {
                user.setEmail(dev_email.getText());
            }
            if (!dev_web.getText().isEmpty()) {
                user.setWebsite(dev_web.getText());
            }

            app.setId(_id.getText());
            app.setCategory(category.getText());
            app.setName(appname.getText());
            AppMongoNeo4jManager app1 = new AppMongoNeo4jManager();
            app1.addApp(app, user);
            //this function
            AppNeo4jManager neo = new AppNeo4jManager();
            neo.followOrDevelopApp(user, app, Relation.RelationType.DEVELOP);

            JOptionPane.showMessageDialog(null, "Added Successfully");
        }
    }

    public void transferMessage(String message) {
        userid.setText(message);
        suggestFavoriteCategory(message);
        appoffollowers(message);
        followerappsfun(message);
        suggestUsers(message);
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


    public void findApp(String text) {
        search_table.setItems(null);
        search_table.setItems(app_data_array);
        AppMongoManager appFinder=new AppMongoManager();
        List<App> foundApps = appFinder.findApp(text, 10, page*10);
        app_data_array.addAll(foundApps);
    }

    public void FollowAppButtonToTable() {
        TableColumn<App, Void> colBtn = new TableColumn("Follow");

        Callback<TableColumn<App, Void>, TableCell<App, Void>> cellFactory = new Callback<TableColumn<App, Void>, TableCell<App, Void>>() {
            @Override
            public TableCell<App, Void> call(final TableColumn<App, Void> param) {
                final TableCell<App, Void> cell = new TableCell<App, Void>() {

                    private final Button btn = new Button("Follow");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            AppNeo4jManager app=new AppNeo4jManager();
                            User user=new User();
                            App app1=new App();
                            user.setUsername(userid.getText());
                            app1.setId(getTableView().getItems().get(getIndex()).getId());
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










    public void signout_fun(ActionEvent actionEvent) throws IOException {
        try {
            //Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("WelcomeScreen.fxml"));
            Parent root = loader.load();

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

    public void back_fun(ActionEvent actionEvent) {
        ClearTable(search_table2);
        p--;
    getMostPopularApps();
    }

    public void next_fun(ActionEvent actionEvent) {
        ClearTable(search_table2);
        p++;
        getMostPopularApps();
    }

    public void getMostPopularApps() {
        AppMongoManager appFinder = new AppMongoManager();
        search_table2.setItems(null);
        search_table2.setItems(app_data_array2);
        List<App> foundApps = appFinder.getPopularApps(10, page*10);
        app_data_array2.addAll(foundApps);
    }
    public void getBestAppsPerCategory(ActionEvent actionEvent) {
        if (text_pop.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Field required");
        } else {
            ClearTable(search_table3);
            AppMongoManager appFinder = new AppMongoManager();
            search_table3.setItems(null);
            search_table3.setItems(app_data_array1);
            List<App> foundApps = appFinder.getPopularAppsPerCat(text_pop.getText(), 10, p1*10);
            app_data_array1.addAll(foundApps);
        }
    }
    public void getBestAppsPerYear(ActionEvent actionEvent) {
        if (text_year.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Field required");
        } else {
            ClearTable(search_table3);
            AppMongoManager appFinder = new AppMongoManager();
            search_table3.setItems(null);
            search_table3.setItems(app_data_array3);
            List<App> foundApps = appFinder.getPopularAppsPerYear(parseInt(text_year.getText()), 10, p1 * 10);
            app_data_array3.addAll(foundApps);
        }
    }

    public void getCommonApps(Boolean common){
        AppNeo4jManager app=new AppNeo4jManager();
        Set<App> data= app.browseCommonApps();
        ObservableList<String> items = FXCollections.observableArrayList();
        for (App a: data){
            items.add(a.toAppDocument().get("_id") +" "+ a.toAppDocument().get("name"));
            String _id= (String) a.toAppDocument().get("_id");
            String name= (String) a.toAppDocument().get("name");
            String category= (String) a.toAppDocument().get("category");
            App x = new App(); x.setId(_id); x.setName(name); x.setCategory(category);
            if (common) {
                commonapp.add(x);
            } else {
                suggestedapp.add(x);
            }
        }
    }
    public void suggestCommonApps(){
        getCommonApps(true);
        commonapp_table.setItems(null);
        commonapp_table.setItems(commonapp);
    }
    public  void suggestedapps(){
        getCommonApps(false);
        suggestedapps_table.setItems(null);
        ObservableList<App> solver = FXCollections.observableArrayList();
        for(App a: suggestedapp){
            solver.add(a);
        }

        suggestedapps_table.setItems(solver);
    }
    public void suggestUsers(String username){
        UserNeo4jManager user=new UserNeo4jManager();
        Set<String> data= user.browseSuggestedUsers(username,10);
        ObservableList<String> items = FXCollections.observableArrayList();
        for (String datum : data) {
            items.add(datum);
            User u = new User();
            u.setUsername(datum);
            suggestedusers.add(u);
        }
        suggesteduser_table.setItems(null);
        ObservableList<User> solver = FXCollections.observableArrayList();
        for(User a: suggestedusers){
            solver.add(a);
        }
        suggesteduser_table.setItems(solver);
    }
    public void suggestFavoriteCategory(String username){
        AppNeo4jManager app=new AppNeo4jManager();
        User u=new User();
        u.setUsername(username);
        Set<App> data= app.browseFavoriteCategory(u);
        ObservableList<String> items = FXCollections.observableArrayList();
        if(data.isEmpty()){
            items.add("No data");
        }
        for (App a: data){
            items.add((String) a.toAppDocument().get("_id"));
        }
        listfav.setItems(items);
    }
    public void fav_cat(ActionEvent actionEvent) {
        AppNeo4jManager app=new AppNeo4jManager();
        User u=new User();
        u.setUsername(favtxt.getText());
        Set<App> data= app.browseFavoriteCategory(u);
        ObservableList<String> items = FXCollections.observableArrayList();
        if(data.isEmpty()){
            items.add("No data");
        }
        for (App a: data){
            items.add((String) a.toAppDocument().get("_id"));
        }
        listfav.setItems(items);
    }
    public void appoffollowers(String username){
        AppNeo4jManager app=new AppNeo4jManager();
        User u=new User();
        u.setUsername(username);
        Set<App> data= app.browseAppsOfFollowers(u);
        ObservableList<String> items = FXCollections.observableArrayList();
        if(data.isEmpty()){
            items.add("No data");
        }
        for (App a: data){
            items.add((String) a.toAppDocument().get("_id"));
        }
        listfav1.setItems(items);
    }
    public void app_of_followers(ActionEvent actionEvent) {
        AppNeo4jManager app=new AppNeo4jManager();
        User u=new User();
        u.setUsername(followertxt.getText());
        Set<App> data= app.browseAppsOfFollowers(u);
        ObservableList<String> items = FXCollections.observableArrayList();
        if(data.isEmpty()){
            items.add("No data");
        }
        for (App a: data){
            items.add((String) a.toAppDocument().get("_id"));

        }
        listfav1.setItems(items);
    }
    public void followerappsfun(String message){
        AppNeo4jManager app=new AppNeo4jManager();
        User u=new User();
        u.setUsername(message);
        Set<App> data= app.browseFollowedApps(u);
        ObservableList<String> items = FXCollections.observableArrayList();
        if(data.isEmpty()){
            items.add("No data");
        }
        for (App a: data){
            items.add((String) a.toAppDocument().get("_id"));

        }

        listfav11.setItems(items);
    }
    public void Followedapps(ActionEvent actionEvent) {
        AppNeo4jManager app=new AppNeo4jManager();
        User u=new User();
        u.setUsername(followertxt1.getText());
        Set<App> data= app.browseFollowedApps(u);
        ObservableList<String> items = FXCollections.observableArrayList();
        if(data.isEmpty()){
            items.add("No data");
        }
        for (App a: data){
            items.add((String) a.toAppDocument().get("_id"));
        }

        listfav11.setItems(items);
    }
    public void FollowUserButtonToTable() {
        TableColumn<User, Void> colBtn = new TableColumn("Follow");

        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = new Callback<TableColumn<User, Void>, TableCell<User, Void>>() {
            @Override
            public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                final TableCell<User, Void> cell = new TableCell<User, Void>() {

                    private final Button btn = new Button("Follow");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            AppNeo4jManager app=new AppNeo4jManager();
                            User user=new User();
                            App app1=new App();
                            user.setUsername(userid.getText());
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

    public void ClearTable(TableView x) {
        for (int i = 0; i < x.getItems().size(); i++) {
            x.getItems().clear();
        }
    }



}