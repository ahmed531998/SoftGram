package it.unipi.softgram.org.example;

import com.mongodb.client.MongoCollection;
import it.unipi.softgram.controller.mongo.UserMongoManager;
import it.unipi.softgram.controller.mongoneo4j.UserMongoNeo4jManager;
import it.unipi.softgram.controller.neo4j.UserNeo4jManager;
import it.unipi.softgram.entities.Review;
import it.unipi.softgram.entities.User;
import it.unipi.softgram.table_chooser.AppData;
import it.unipi.softgram.table_chooser.Userdata;
import it.unipi.softgram.utilities.drivers.MongoDriver;
import it.unipi.softgram.utilities.enumerators.Role;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class Users implements Initializable {
        int page=0;
        @FXML private TextField yeartxt,followedtxt,actualtxt,usernametxt,username,country,email,role,website,username_re,followerUsername,followedUsername,searchtxt;
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
        @FXML private Label appid;
        ObservableList<Userdata> userdata = FXCollections.observableArrayList();
        @FXML
        TableView<Userdata> user_table;
        @FXML
        private TableColumn<AppData, String> userid_col;
        @FXML
        private TableColumn<AppData, String> role_col;
        @FXML
        private TableColumn<AppData, String> email_col;

        String app_id;
        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            ObservableList<String> options =
                    FXCollections.observableArrayList(
                            "True",
                            "False"
                    );
            request.getItems().addAll(options);
            userid_col.setCellValueFactory(new PropertyValueFactory<>("username"));
            role_col.setCellValueFactory(new PropertyValueFactory<>("role"));
            email_col.setCellValueFactory(new PropertyValueFactory<>("email"));


            BecomeDeveloperButtonToTable();
            BecomeNormalButtonToTable();
            RemoveButtonToTable();
            UpdateButtonToTable();
            FollowButtonToTable();
            findUsers("");
            user_table.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event){
                    if(event.getClickCount() == 2){

                        try {
                            //Load second scene
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("usermainpage.fxml"));
                            Parent root = loader.load();

                            //Get controller of scene2
                            appid.setText(user_table.getSelectionModel().getSelectedItem().getUsername());
                            Usermainpage scene2Controller = loader.getController();
                            //Pass whatever data you want. You can have multiple method calls here
                            scene2Controller.transferMessage(appid.getText());

                            //Show scene 2 in new window
                            Stage stage = new Stage();
                            stage.setScene(new Scene(root));
                            stage.setTitle("User Mainpage");
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
        public void findUsers(String text) {
            MongoDriver driver = new MongoDriver();
            MongoCollection<Document> collection = driver.getCollection("users");

            // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

            Consumer<Document> processBlock = document -> {
                // System.out.println(document);
                String _id = (String) document.get("_id");
                String email = (String) document.get("email");
                String role = (String) document.get("role");

                userdata.add(new Userdata(
                        _id,
                        "",
                        "",email,role,""));
            };
            user_table.setItems(null);
            user_table.setItems(userdata);

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


        public void addusers(ActionEvent actionEvent) {

            Role.RoleValue roleValue= Role.RoleValue.valueOf(role.getText());
            String usernametxt= username.getText();
            String roleString = Role.getRoleString(roleValue);
            final String role = roleString.replaceAll("\\s","");
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

        public void addfollow(ActionEvent actionEvent) {
            UserNeo4jManager user = new UserNeo4jManager();
            if (followedUsername.getText().isEmpty() || followerUsername.getText().isEmpty()){
                JOptionPane.showMessageDialog(null, "Field required");
            }else {
                user.addFollow(followerUsername.getText(), followedUsername.getText(), Boolean.parseBoolean(request.getItems().toString()));
                JOptionPane.showMessageDialog(null, "Done");
            }
        }

        public void acceptfollow(ActionEvent actionEvent) {
       /* UserNeo4jManager user = new UserNeo4jManager();
        if (followedUsername.getText().isEmpty() || followerUsername.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Field required");
        }else {
            user.acceptFollow(followerUsername.getText().toString(),followedUsername.getText().toString());
            JOptionPane.showMessageDialog(null, "Accepted");
        }*/
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
        private void UpdateButtonToTable() {
            TableColumn<Userdata, Void> colBtn = new TableColumn("Update");

            Callback<TableColumn<Userdata, Void>, TableCell<Userdata, Void>> cellFactory = new Callback<TableColumn<Userdata, Void>, TableCell<Userdata, Void>>() {
                @Override
                public TableCell<Userdata, Void> call(final TableColumn<Userdata, Void> param) {
                    final TableCell<Userdata, Void> cell = new TableCell<Userdata, Void>() {
                        private final Button btn = new Button("Update");
                        {
                            btn.setOnAction((ActionEvent event) -> {
                                Userdata data = getTableView().getItems().get(getIndex());


                                MongoDriver driver = new MongoDriver();
                                MongoCollection<Document> collection = driver.getCollection("users");
                                String _id = getTableView().getItems().get(getIndex()).getUsername();
                                String email = getTableView().getItems().get(getIndex()).getEmail();
                                String role = getTableView().getItems().get(getIndex()).getRole();
                                Stage newStage = new Stage();
                                VBox comp = new VBox();
                                TextField username = new TextField(_id);
                                TextField emailtxt = new TextField(email);
                                TextField roletxt = new TextField(role);
                                Button update = new Button("Update");
                                update.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {

                                   /* Document query = new Document();
                                    query.append("_id", getTableView().getItems().get(getIndex()).getUsername());
                                    Document setData = new Document();
                                    setData.append("_id", username.getText().toString())
                                            .append("role", roletxt.getText().toString())
                                            .append("email", emailtxt.getText().toString())
                                    ;
                                    Document update = new Document();
                                    update.append("$set", setData);
                                    //To update single Document
                                    collection.updateOne(query, update);*/
                                        UserMongoManager user=new UserMongoManager();
                                        User user1=new User();
                                        user1.setUsername(username.getText());
                                        user1.setEmail(emailtxt.getText());
                                        user1.setRole(roletxt.getText());
                                        user.updateUser(user1);
                                        JOptionPane.showMessageDialog(null, "Updated Successfully");
                                        ClearTable(user_table);
                                        findUsers("");
                                    }
                                });

                                comp.getChildren().add(username);
                                comp.getChildren().add(emailtxt);
                                comp.getChildren().add(roletxt);
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

            user_table.getColumns().add(colBtn);

        }
        private void BecomeDeveloperButtonToTable() {
            TableColumn<Userdata, Void> colBtn = new TableColumn("BecomeDeveloper");

            Callback<TableColumn<Userdata, Void>, TableCell<Userdata, Void>> cellFactory = new Callback<TableColumn<Userdata, Void>, TableCell<Userdata, Void>>() {
                @Override
                public TableCell<Userdata, Void> call(final TableColumn<Userdata, Void> param) {
                    final TableCell<Userdata, Void> cell = new TableCell<Userdata, Void>() {
                        private final Button btn = new Button("BecomeDeveloper");
                        {
                            btn.setOnAction((ActionEvent event) -> {
                                Userdata data = getTableView().getItems().get(getIndex());
                                UserMongoNeo4jManager usermongo=new UserMongoNeo4jManager();
                            /*neo4j
                            UserNeo4jManager userneo=new UserNeo4jManager();
                            userneo.becomeDeveloper(getTableView().getItems().get(getIndex()).getUsername());*/
                                usermongo.becomeDeveloper(getTableView().getItems().get(getIndex()).getUsername());
                                JOptionPane.showMessageDialog(null, "this user become a developer");
                                ClearTable(user_table);
                                findUsers("");
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
        private void BecomeNormalButtonToTable() {
            TableColumn<Userdata, Void> colBtn = new TableColumn("BecomeNormal");

            Callback<TableColumn<Userdata, Void>, TableCell<Userdata, Void>> cellFactory = new Callback<TableColumn<Userdata, Void>, TableCell<Userdata, Void>>() {
                @Override
                public TableCell<Userdata, Void> call(final TableColumn<Userdata, Void> param) {
                    final TableCell<Userdata, Void> cell = new TableCell<Userdata, Void>() {

                        private final Button btn = new Button("BecomeNormal");

                        {
                            btn.setOnAction((ActionEvent event) -> {
                                Userdata data = getTableView().getItems().get(getIndex());
                                UserMongoNeo4jManager usermongo=new UserMongoNeo4jManager();
                                usermongo.becomeNormalUser(getTableView().getItems().get(getIndex()).getUsername());
                                JOptionPane.showMessageDialog(null, "this user become normal user");
                                ClearTable(user_table);
                                findUsers("");
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
        private void RemoveButtonToTable() {
            TableColumn<Userdata, Void> colBtn = new TableColumn("Remove");

            Callback<TableColumn<Userdata, Void>, TableCell<Userdata, Void>> cellFactory = new Callback<TableColumn<Userdata, Void>, TableCell<Userdata, Void>>() {
                @Override
                public TableCell<Userdata, Void> call(final TableColumn<Userdata, Void> param) {
                    final TableCell<Userdata, Void> cell = new TableCell<Userdata, Void>() {

                        private final Button btn = new Button("Remove");

                        {
                            btn.setOnAction((ActionEvent event) -> {
                                Userdata data = getTableView().getItems().get(getIndex());
                                System.out.println("selectedData: " + data);

                                int dialogButton = JOptionPane.YES_NO_OPTION;
                                int dialogResult = JOptionPane.showConfirmDialog(null, "Would You Like to remove this user?", "Removing", dialogButton);

                                if (dialogResult == JOptionPane.YES_OPTION) {
                                    // Saving code here
                                    UserMongoManager usermongo=new UserMongoManager();
                                    usermongo.removeUser(getTableView().getItems().get(getIndex()).getUsername());
                                    JOptionPane.showMessageDialog(null, "User removed successfully");
                                    ClearTable(user_table);
                                    findUsers("");
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

            user_table.getColumns().add(colBtn);

        }
        private void FollowButtonToTable() {
            TableColumn<Userdata, Void> colBtn = new TableColumn("Follow");

            Callback<TableColumn<Userdata, Void>, TableCell<Userdata, Void>> cellFactory = new Callback<TableColumn<Userdata, Void>, TableCell<Userdata, Void>>() {
                @Override
                public TableCell<Userdata, Void> call(final TableColumn<Userdata, Void> param) {
                    final TableCell<Userdata, Void> cell = new TableCell<Userdata, Void>() {

                        private final Button btn = new Button("Follow");

                        {
                            btn.setOnAction((ActionEvent event) -> {
                                Userdata data = getTableView().getItems().get(getIndex());
                                System.out.println("selectedData: " + data);
                                UserNeo4jManager useneo=new UserNeo4jManager();
                                useneo.addFollow(appid.getText(), getTableView().getItems().get(getIndex()).getUsername(), false);
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

        public void follow_serch(ActionEvent actionEvent) {
            UserNeo4jManager user=new UserNeo4jManager();
            ArrayList<String> data=
                    (ArrayList<String>) user.browseFollowRequests(usernametxt.getText());
            ObservableList<String> items = FXCollections.observableArrayList();
            for (int i=0; i<data.size();i++)
            {
                items.add(data.get(0));
            }
            listsuggest.setItems(items);

        }

        public void suggest_search(ActionEvent actionEvent) {
            UserNeo4jManager user=new UserNeo4jManager();
            ArrayList<String> data=
                    (ArrayList<String>) user.browseUsersWithMostFollowersInYear(Integer.parseInt(yeartxt.getText()),10);
            ObservableList<String> items = FXCollections.observableArrayList();
            for (int i=0; i<data.size();i++)
            {
                items.add(data.get(0));
            }
            listsuggest.setItems(items);

        }

        public void actual_search(ActionEvent actionEvent) {
            UserNeo4jManager user=new UserNeo4jManager();
            ArrayList<String> data=
                    (ArrayList<String>) user.browseActualFollowers(actualtxt.getText());
            ObservableList<String> items = FXCollections.observableArrayList();
            for (int i=0; i<data.size();i++)
            {
                items.add(data.get(0));
            }
            listactual.setItems(items);
        }

        public void followed_search(ActionEvent actionEvent) {

            UserNeo4jManager user=new UserNeo4jManager();
            ArrayList<String> data=
                    (ArrayList<String>) user.browseFollowedUsers(followedtxt.getText());
            ObservableList<String> items = FXCollections.observableArrayList();
            for (int i=0; i<data.size();i++)
            {
                items.add(data.get(0));
            }
            listfollowed.setItems(items);
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
}
