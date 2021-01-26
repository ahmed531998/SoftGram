package it.unipi.softgram.org.example;

import it.unipi.softgram.controller.mongo.UserMongoManager;
import it.unipi.softgram.controller.mongoneo4j.UserMongoNeo4jManager;
import it.unipi.softgram.controller.neo4j.UserNeo4jManager;
import it.unipi.softgram.entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
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
import java.util.ResourceBundle;

public class Users extends CommonUserPages {

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            ObservableList<String> options =
                    FXCollections.observableArrayList(
                            "True",
                            "False"
                    );
            super.getRequest().getItems().addAll(options);
            super.getUserid_col().setCellValueFactory(new PropertyValueFactory<>("username"));
            super.getRole_col().setCellValueFactory(new PropertyValueFactory<>("role"));
            super.getEmail_col().setCellValueFactory(new PropertyValueFactory<>("email"));

            //suggested users

            super.getUsername_col().setCellValueFactory(new PropertyValueFactory<>("username"));

            //why here?
            //suggestedusers();

            ChangeRoleButtonToTable();
            FollowUserButtonToTable();
            RemoveButtonToTable();
            UpdateButtonToTable();
            FollowButtonToTable();
            findUsers("");
            listrequest.setOnMouseClicked(event -> {
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog(null, "follow this user?", "!!", dialogButton);

                if (dialogResult == JOptionPane.YES_OPTION) {
                    // Saving code here

                }
            });
            listsuggest.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2) {
                    int dialogButton = JOptionPane.YES_NO_OPTION;
                    int dialogResult = JOptionPane.showConfirmDialog(null, "Follow this user", "!!", dialogButton);

                    if (dialogResult == JOptionPane.YES_OPTION) {
                        // Saving code here
                        UserNeo4jManager user1 = new UserNeo4jManager();
                        User user = new User();

                        user.setUsername(Users.super.getUserid().getText());
                        String followeduser = listsuggest.getSelectionModel().getSelectedItem();
                        String followeruser = Users.super.getUserid().getText();
                        boolean request = true;
                        //  System.out.println("clicked on " + listrequest.getSelectionModel().getSelectedItem());
                        user1.addFollow(followeruser, followeduser, request);
                        JOptionPane.showMessageDialog(null, "You follow this user");
                        followedSearch(Users.super.getUserid().getText());

                    }
                }
            });

            user_table.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event){
                    if(event.getClickCount() == 2){

                        try {
                            //Load second scene
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("usermainpage.fxml"));
                            Parent root = loader.load();

                            //Get controller of scene2
                            Users.super.getUserid1().setText(user_table.getSelectionModel().getSelectedItem().getUsername());

                            System.out.println( Users.super.getUserid1().getText());
                            Usermainpage scene2Controller = loader.getController();
                            //Pass whatever data you want. You can have multiple method calls here
                            scene2Controller.transferMessage( Users.super.getUserid().getText());
                            scene2Controller.transferMessage1( Users.super.getUserid1().getText());

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

        private void UpdateButtonToTable() {
            TableColumn<User, Void> colBtn = new TableColumn("Update");

            Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = new Callback<TableColumn<User, Void>, TableCell<User, Void>>() {
                @Override
                public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                    final TableCell<User, Void> cell = new TableCell<User, Void>() {
                        private final Button btn = new Button("Update");
                        {
                            btn.setOnAction((ActionEvent event) -> {
                                String _id = getTableView().getItems().get(getIndex()).getUsername();
                                String email = getTableView().getItems().get(getIndex()).getEmail();
                                String role = getTableView().getItems().get(getIndex()).getRole();
                                Stage newStage = new Stage();
                                VBox comp = new VBox();
                                TextField username = new TextField(_id);
                                TextField emailtxt = new TextField(email);
                                TextField roletxt = new TextField(role);
                                Button update = new Button("Update");
                                update.setOnAction(event1 -> {

                                    //why here?
                                    UserMongoManager user=new UserMongoManager();
                                    User user1=new User();
                                    user1.setUsername(username.getText());
                                    user1.setEmail(emailtxt.getText());
                                    user1.setRole(roletxt.getText());
                                    user.updateUser(user1);
                                    JOptionPane.showMessageDialog(null, "Updated Successfully");
                                    ClearTable(user_table);
                                    findUsers("");
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

        private void ChangeRoleButtonToTable() {
        TableColumn<User, Void> colBtn = new TableColumn("ChangeRole");

        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = new Callback<TableColumn<User, Void>, TableCell<User, Void>>() {
            @Override
            public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                final TableCell<User, Void> cell = new TableCell<User, Void>() {
                    private final Button btn = new Button("ChangeRole");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            UserMongoNeo4jManager usermongo=new UserMongoNeo4jManager();
                            if(getTableView().getItems().get(getIndex()).getRole().equals("Developer")){
                                int dialogButton = JOptionPane.YES_NO_OPTION;
                                int dialogResult1 = JOptionPane.showConfirmDialog(null, "Change Role to Normal", "Changing", dialogButton);
                                if (dialogResult1 == JOptionPane.YES_OPTION) {
                                    // Saving code here
                                    usermongo.becomeNormalUser(getTableView().getItems().get(getIndex()).getUsername());
                                    JOptionPane.showMessageDialog(null, "this user become normal user");
                                    ClearTable(user_table);
                                    findUsers("");
                                }
                            }else if(getTableView().getItems().get(getIndex()).getRole().equals("Normal User") || getTableView().getItems().get(getIndex()).getRole().equals("NormalUser") || getTableView().getItems().get(getIndex()).getRole().equals("Normal")){
                                int dialogButton = JOptionPane.YES_NO_OPTION;
                                int dialogResult = JOptionPane.showConfirmDialog(null, "Change Role to Developer", "Changing", dialogButton);
                                if (dialogResult == JOptionPane.YES_OPTION) {
                                    usermongo.becomeDeveloper(getTableView().getItems().get(getIndex()).getUsername());
                                    JOptionPane.showMessageDialog(null, "this user become a developer");
                                    ClearTable(user_table);
                                    findUsers("");

                                }
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

        private void RemoveButtonToTable() {
            TableColumn<User, Void> colBtn = new TableColumn("Remove");

            Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = new Callback<TableColumn<User, Void>, TableCell<User, Void>>() {
                @Override
                public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                    final TableCell<User, Void> cell = new TableCell<User, Void>() {

                        private final Button btn = new Button("Remove");

                        {
                            btn.setOnAction((ActionEvent event) -> {
                                User data = getTableView().getItems().get(getIndex());
                                System.out.println("selectedData: " + data);

                                int dialogButton = JOptionPane.YES_NO_OPTION;
                                int dialogResult = JOptionPane.showConfirmDialog(null, "Would You Like to remove this user?", "Removing", dialogButton);

                                if (dialogResult == JOptionPane.YES_OPTION) {
                                    // Saving code here
                                    UserMongoNeo4jManager usermongo=new UserMongoNeo4jManager();
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

        public void statismainpage(ActionEvent actionEvent) {
            try {
                //Load second scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("statistics.fxml"));
                Parent root = loader.load();

                //Get controller of scene2

                Statistics scene2Controller = loader.getController();
                //Pass whatever data you want. You can have multiple method calls here
                scene2Controller.transferMessage( Users.super.getUserid().getText());

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

        public void acceptfollow(ActionEvent actionEvent) {
    }
}
