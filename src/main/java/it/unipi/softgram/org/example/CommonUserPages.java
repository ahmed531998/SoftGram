package it.unipi.softgram.org.example;

import it.unipi.softgram.controller.mongo.UserMongoManager;
import it.unipi.softgram.controller.mongoneo4j.UserMongoNeo4jManager;
import it.unipi.softgram.controller.neo4j.UserNeo4jManager;
import it.unipi.softgram.entities.App;
import it.unipi.softgram.entities.Review;
import it.unipi.softgram.entities.User;
import it.unipi.softgram.gui.WelcomeScreen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

abstract class CommonUserPages implements Initializable {
    int page=0;
    @FXML
    private TextField yeartxt,followedtxt,actualtxt,usernametxt,username,country,email,role,website,username_re,followerUsername,followedUsername,searchtxt;
    @FXML private PasswordField pass_id;

    @FXML
    ListView<String> listsuggest = new ListView<>();
    @FXML
    ListView<String> listfollowed = new ListView<>();
    @FXML
    ListView<String> listrequest = new ListView<>();
    @FXML
    ListView<String> listactual = new ListView<>();
    ObservableList<Review> Reviews = FXCollections.observableArrayList();
    @FXML
    private DatePicker birth;
    @FXML
    private ComboBox request;
    @FXML private Label userid,userid1;
    ObservableList<User> userdata = FXCollections.observableArrayList();
    ObservableSet<User> suggestedusers = FXCollections.observableSet();
    User user_main=new User();

    @FXML
    TableView<User> user_table;
    @FXML
    private TableColumn<App, String> userid_col;
    @FXML
    private TableColumn<App, String> role_col;
    @FXML
    private TableColumn<App, String> email_col;

    @FXML TableView<User> suggesteduser_table;
    @FXML private TableColumn<User, String> username_col;

    String app_id;

    public ComboBox getRequest() {
        return request;
    }

    public TableColumn<App, String> getUserid_col() {
        return userid_col;
    }

    public TableColumn<App, String> getRole_col() {
        return role_col;
    }

    public TableColumn<App, String> getEmail_col() {
        return email_col;
    }

    public TableColumn<User, String> getUsername_col() {
        return username_col;
    }

    public TextField getActualtxt() {
        return actualtxt;
    }

    public Label getUserid() {
        return userid;
    }

    public Label getUserid1() {
        return userid1;
    }

    public void transferMessage(String message) {
        //Display the message
        userid.setText(message);
        user_main.setUsername(message);
        followedSearch(message);
        actualSearch(message);
        followrequest(message);
        followrequestfun(message);
        followcommon(message);
    }

    public void suggestedusers(String username){
        UserNeo4jManager user=new UserNeo4jManager();

        Set<String> data= user.browseSuggestedUsers(username,10);
        ObservableSet<String> items = FXCollections.observableSet();
        for (String datum : data) {
            items.add(datum);
            User x = new User();
            x.setUsername(datum);
            suggestedusers.add(x);
        }
        suggesteduser_table.setItems(null);
        suggesteduser_table.setItems((ObservableList<User>) suggestedusers);

    }

    public void usersmainpage(ActionEvent actionEvent) {
        try {
            //Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("users.fxml"));
            Parent root = loader.load();

            //Get controller of scene2

            CommonUserPages scene2Controller = loader.getController();
            //Pass whatever data you want. You can have multiple method calls here
            scene2Controller.transferMessage(userid.getText());

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

    public void appmainpage(ActionEvent actionEvent) throws IOException {
        try {
            //Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin.fxml"));
            Parent root = loader.load();

            //Get controller of scene2

            Admin scene2Controller = loader.getController();
            //Pass whatever data you want. You can have multiple method calls here
            scene2Controller.transferMessage(userid.getText());

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
    public void findUsers(String text) {
        user_table.setItems(null);
        user_table.setItems(userdata);
        UserMongoManager userFinder = new UserMongoManager();
        List<User> users = userFinder.searchUserByUsername(text, page*10);
        for (User u: users) {
            User x = new User();
            x.setUsername(u.getUsername()); x.setRole(u.getRole()); x.setEmail(u.getEmail());
            userdata.add(x);
        }
    }

    public void becomedeveloper(ActionEvent actionEvent) {
        UserMongoNeo4jManager user = new UserMongoNeo4jManager();
        if (username_re.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Field required");
        }else {
            user.becomeDeveloper(username_re.getText());
            JOptionPane.showMessageDialog(null, "User became a developer");
        }
    }

    public void becomenormal(ActionEvent actionEvent) {
        UserMongoNeo4jManager user = new UserMongoNeo4jManager();
        if (username_re.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Field required");
        }else {
            user.becomeNormalUser(username_re.getText());
            JOptionPane.showMessageDialog(null, "User became a normal user");
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

    public void searchfun(ActionEvent actionEvent) {
        ClearTable(user_table);
        findUsers(searchtxt.getText());
        System.out.println(searchtxt.getText());
    }

    public void nextfun(ActionEvent actionEvent) {
        ClearTable(user_table);
        page++;
        findUsers("");
    }

    public void backfun(ActionEvent actionEvent) {
        ClearTable(user_table);
        page--;
        findUsers("");
    }
    public void addusers(ActionEvent actionEvent) {

        String usernametxt= username.getText();
        if(username.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Field required");
        }else {
            UserMongoNeo4jManager user = new UserMongoNeo4jManager();
            User userobj=new User();
            userobj.setUsername(usernametxt);
            user.addUser(userobj);
            JOptionPane.showMessageDialog(null, "User added successfully");
        }
    }
    public void remove_user(ActionEvent actionEvent) {
        UserMongoNeo4jManager user = new UserMongoNeo4jManager();
        if (username_re.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Field required");
        }else {
            user.removeUser(username_re.getText());
            JOptionPane.showMessageDialog(null, "User added successfully");
        }
    }
    public void addfollow(ActionEvent actionEvent) {
        UserNeo4jManager user = new UserNeo4jManager();
        if (followedUsername.getText().isEmpty() || followerUsername.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Field required");
        }else {
            user.addFollow(followerUsername.getText(), followedUsername.getText(), Boolean.parseBoolean(request.getItems().toString()));
            JOptionPane.showMessageDialog(null, "Done");
        }
    }

    public void removedfollow(ActionEvent actionEvent) {
        UserNeo4jManager user = new UserNeo4jManager();
        if (followedUsername.getText().isEmpty() || followerUsername.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Field required");
        }else {
            user.removeFollow(followerUsername.getText(), followedUsername.getText());
            JOptionPane.showMessageDialog(null, "Removed");
        }
    }
    public void FollowButtonToTable() {
        TableColumn<User, Void> colBtn = new TableColumn("Follow");

        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = new Callback<TableColumn<User, Void>, TableCell<User, Void>>() {
            @Override
            public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                final TableCell<User, Void> cell = new TableCell<User, Void>() {

                    private final Button btn = new Button("Follow");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            UserNeo4jManager useneo=new UserNeo4jManager();
                            System.out.println(userid.getText());
                            useneo.addFollow(userid.getText(), getTableView().getItems().get(getIndex()).getUsername(), false);
                            JOptionPane.showMessageDialog(null, "You followed this user");
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

        user_table.getColumns().add(colBtn);

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


    public void follow_search(ActionEvent actionEvent) {
        UserNeo4jManager user=new UserNeo4jManager();
        List<String> data= user.browseFollowRequests(usernametxt.getText());
        ObservableList<String> items = FXCollections.observableArrayList();
        if(data.isEmpty()){
            items.add("No data");
        }
        items.addAll(data);
        listrequest.setItems(items);

    }

    public void followrequestfun(String username){
        UserNeo4jManager user=new UserNeo4jManager();
        List<String> data= user.browseFollowRequests(username);
        ObservableList<String> items = FXCollections.observableArrayList();
        if(data.isEmpty()){
            items.add("No data");
        }
        items.addAll(data);
        listrequest.setItems(items);
    }
    public void followcommon(String username){
        //common users
        UserNeo4jManager user=new UserNeo4jManager();

        Set<String> data= user.browseSuggestedUsers(username,10);
        ObservableList<String> items = FXCollections.observableArrayList();
        if(data.isEmpty()){
            items.add("No data");
        }
        items.addAll(data);
        listsuggest.setItems(items);
    }
    public void followrequest(String username){
        UserNeo4jManager user=new UserNeo4jManager();
        List<String> data= user.browseFollowRequests(username);
        ObservableList<String> items = FXCollections.observableArrayList();
        if(data.isEmpty()){
            items.add("No data");
        }
        items.addAll(data);
        listrequest.setItems(items);

    }

    public void suggest_search(ActionEvent actionEvent) {
        UserNeo4jManager user=new UserNeo4jManager();
        ArrayList<String> data=
                (ArrayList<String>) user.browseUsersWithMostFollowersInYear(Integer.parseInt(yeartxt.getText()),10);
        ObservableList<String> items = FXCollections.observableArrayList();
        items.addAll(data);
        listsuggest.setItems(items);

    }

    public void actual_search(ActionEvent actionEvent) {
        UserNeo4jManager user=new UserNeo4jManager();
        List<String> data= user.browseActualFollowers(actualtxt.getText());
        ObservableList<String> items = FXCollections.observableArrayList();
        if(data.isEmpty()){
            items.add("No data");
        }
        items.addAll(data);
        listactual.setItems(items);
    }
    public void actualSearch(String username){
        UserNeo4jManager user=new UserNeo4jManager();
        List<String> data= user.browseActualFollowers(username);
        ObservableList<String> items = FXCollections.observableArrayList();
        if(data.isEmpty()){
            items.add("No data");
        }
        items.addAll(data);
        listactual.setItems(items);
    }

    public void followed_search(ActionEvent actionEvent) {

        UserNeo4jManager user=new UserNeo4jManager();
        List<String> data= user.browseFollowedUsers(followedtxt.getText());
        ObservableList<String> items = FXCollections.observableArrayList();
        if(data.isEmpty()){
            items.add("No data");
        }
        items.addAll(data);
        listfollowed.setItems(items);
    }
    public void followedSearch(String usernme){

        UserNeo4jManager user=new UserNeo4jManager();
        List<String> data= user.browseFollowedUsers(usernme);
        ObservableList<String> items = FXCollections.observableArrayList();
        if(data.isEmpty()){
            items.add("No data");
        }
        items.addAll(data);
        listfollowed.setItems(items);
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
                            UserNeo4jManager user1=new UserNeo4jManager();
                            User user=new User();

                            user.setUsername(userid.getText());
                            String followeduser=getTableView().getItems().get(getIndex()).getUsername();
                            String followeruser=userid.getText();
                            boolean request=true;


                            if(btn.getText().equals("Follow")){
                                user1.addFollow(followeruser,followeduser, request);
                                JOptionPane.showMessageDialog(null, "You followed this user");
                                btn.setText("Unfollow");
                                ClearTable(suggesteduser_table);
                                suggestedusers(user.getUsername());}else{
                                user1.removeFollow(followeruser,followeduser);
                                JOptionPane.showMessageDialog(null, "You unfollowed this user");
                                btn.setText("Follow");
                                ClearTable(suggesteduser_table);
                                suggestedusers(user.getUsername());
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
