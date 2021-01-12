package org.example;

import com.mongodb.client.MongoCollection;
import entities.Apps;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.bson.BsonRegularExpression;
import org.bson.Document;
import org.bson.conversions.Bson;
import table_chooser.AppData;
import table_chooser.MostPopCat;
import table_chooser.Mostpopyear;
import utilities.MongoDriver;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.eq;
import static org.example.App.setRoot;

public class Admin implements Initializable {
    public Button search_btn_pop;
    @FXML
    private ComboBox app_purchase, ad_supported;
    @FXML
    private DatePicker released, last_update;
    @FXML
    TextField _id, appname, price, category, ratingcount, installscount, size, age_group, currency, searchtxt, text_pop, text_year;
    ObservableList<AppData> app_data_array = FXCollections.observableArrayList();
    ObservableList<AppData> app_data_array2 = FXCollections.observableArrayList();
    ObservableList<MostPopCat> app_data_array1 = FXCollections.observableArrayList();
    ObservableList<MostPopCat> app_data_array3 = FXCollections.observableArrayList();


    @FXML
    TableView<AppData> search_table;
    @FXML
    TableView<AppData> search_table2;
    @FXML
    TableView<MostPopCat> search_table3;
    @FXML
    private TableColumn<AppData, String> app_id_col, app_id_col2;
    @FXML
    private TableColumn<AppData, String> appname_col, appname_col2;
    @FXML
    private TableColumn<AppData, Boolean> adsupported_col;
    @FXML
    private TableColumn<AppData, Double> price_col;
    @FXML
    private TableColumn<AppData, Date> released_col;
    @FXML
    private TableColumn<AppData, String> cat_col;
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
    ObservableList<Apps> app_data = FXCollections.observableArrayList();


    public void Add_apps(ActionEvent actionEvent) {

        if (_id.getText().isEmpty() || appname.getText().isEmpty() || price.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Fields required");
        } else {
            Apps app = new Apps();
            app.set_id(_id.getText().toString());
            app.setCategory(category.getText().toString());
            app.setAdSupported(Boolean.parseBoolean(ad_supported.getItems().toString()));
            app.setInAppPurchase(Boolean.parseBoolean(app_purchase.getItems().toString()));
            app.setAgeGroup(age_group.getText().toString());
            app.setInstallCount(Integer.parseInt(installscount.getText().toString()));
            app.setName(appname.getText().toString());
            app.setCurrency(currency.getText().toString());
            app.setPrice(Double.parseDouble(price.getText().toString()));
            app.setRatingCount(Integer.parseInt(ratingcount.getText().toString()));
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

        search_table.setEditable(true);
        app_id_col.setCellValueFactory(new PropertyValueFactory<>("_id"));
        app_id_col.setCellFactory(TextFieldTableCell.forTableColumn());
        app_id_col.setOnEditCommit(
                new EventHandler<CellEditEvent<AppData, String>>() {
                    @Override
                    public void handle(CellEditEvent<AppData, String> t) {
                        ((AppData) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).set_id(t.getNewValue());
                    }
                }
        );
        appname_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        appname_col.setCellFactory(TextFieldTableCell.forTableColumn());
        appname_col.setOnEditCommit(
                new EventHandler<CellEditEvent<AppData, String>>() {
                    @Override
                    public void handle(CellEditEvent<AppData, String> t) {
                        ((AppData) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setName(t.getNewValue());
                    }
                }
        );
        adsupported_col.setCellValueFactory(new PropertyValueFactory<>("adSupported"));
        price_col.setCellValueFactory(new PropertyValueFactory<>("price"));
        released_col.setCellValueFactory(new PropertyValueFactory<>("released"));
        cat_col.setCellValueFactory(new PropertyValueFactory<>("category"));
        cat_col.setCellFactory(TextFieldTableCell.forTableColumn());
        cat_col.setOnEditCommit(
                new EventHandler<CellEditEvent<AppData, String>>() {
                    @Override
                    public void handle(CellEditEvent<AppData, String> t) {
                        ((AppData) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setCategory(t.getNewValue());
                    }
                }
        );
        age_col.setCellValueFactory(new PropertyValueFactory<>("ageGroup"));

        last_col.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));

        is_purchasesCol.setCellValueFactory(new PropertyValueFactory<>("inAppPurchase"));


        app_id_col2.setCellValueFactory(new PropertyValueFactory<>("_id"));
        appname_col2.setCellValueFactory(new PropertyValueFactory<>("name"));
        num_col2.setCellValueFactory(new PropertyValueFactory<>("numberOfReviews"));
        avg_col2.setCellValueFactory(new PropertyValueFactory<>("Avg"));

        app_id_col3.setCellValueFactory(new PropertyValueFactory<>("_id"));
        name_col3.setCellValueFactory(new PropertyValueFactory<>("name"));
        cate_col3.setCellValueFactory(new PropertyValueFactory<>("category"));
        avg_col3.setCellValueFactory(new PropertyValueFactory<>("Avg"));


        findApp("");
        deleteButtonToTable();
        updateButtonToTable();
        setTableEditable();
        MostPopularApps();

        search_table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event){
                if(event.getClickCount() == 2){
                    Apps app=new Apps();
                    try {
                        //Load second scene
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("ApplicationMain.fxml"));
                        Parent root = loader.load();

                        //Get controller of scene2
                        appid.setText(search_table.getSelectionModel().getSelectedItem().get_id());
                        ApplicationMain scene2Controller = loader.getController();
                        //Pass whatever data you want. You can have multiple method calls here
                        scene2Controller.transferMessage(appid.getText());

                        //Show scene 2 in new window
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.setTitle("Second Window");
                        stage.show();
                    } catch (IOException ex) {
                        System.err.println(ex);
                    }
                }

            }
        });


    }

    public void add_function(ActionEvent actionEvent) {
        ClearTable(search_table);
        findApp(searchtxt.getText().toString());
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
        MongoCollection<Document> collection = driver.getCollection("apps");

        // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

        Consumer<Document> processBlock = new Consumer<Document>() {
            @Override
            public void accept(Document document) {
                // System.out.println(document);
                String name = (String) document.get("name");
                String _id = (String) document.get("_id");
                double adsupported = (double) document.get("adSupported");
                String lastupdated = (String) document.get("name");
                Double price = (Double) document.get("price");
                String category = (String) document.get("category");
                String agegroup = (String) document.get("ageGroup");
                String lastupdated1 = (String) document.get("name");
                boolean apppurchases = true;
                app_data_array.add(new AppData(
                        name,
                        _id,
                        adsupported,
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
                            MongoDriver driver = new MongoDriver();
                            MongoCollection<Document> collection = driver.getCollection("apps");

                            if (dialogResult == JOptionPane.YES_OPTION) {
                                // Saving code here
                                Document query = new Document();
                                query.append("_id", getTableView().getItems().get(getIndex()).get_id());
                                collection.deleteOne(query);
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
                            MongoCollection<Document> collection = driver.getCollection("apps");

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

                                    Document query = new Document();
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
                                    collection.updateOne(query, update);
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

    private void setTableEditable() {
        search_table.setEditable(true);
        // allows the individual cells to be selected
        search_table.getSelectionModel().cellSelectionEnabledProperty().set(true);
        // when character or numbers pressed it will start edit in editable
        // fields
        search_table.setOnKeyPressed(event -> {
            if (event.getCode().isLetterKey() || event.getCode().isDigitKey()) {
                editFocusedCell();
            } else if (event.getCode() == KeyCode.RIGHT
                    || event.getCode() == KeyCode.TAB) {
                search_table.getSelectionModel().selectNext();
                event.consume();
            } else if (event.getCode() == KeyCode.LEFT) {
                // work around due to
                // TableView.getSelectionModel().selectPrevious() due to a bug
                // stopping it from working on
                // the first column in the last row of the table
                selectPrevious();
                event.consume();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void editFocusedCell() {
        final TablePosition<AppData, ?> focusedCell = search_table
                .focusModelProperty().get().focusedCellProperty().get();
        search_table.edit(focusedCell.getRow(), focusedCell.getTableColumn());
    }

    @SuppressWarnings("unchecked")
    private void selectPrevious() {
        if (search_table.getSelectionModel().isCellSelectionEnabled()) {
            // in cell selection mode, we have to wrap around, going from
            // right-to-left, and then wrapping to the end of the previous line
            TablePosition<AppData, ?> pos = search_table.getFocusModel()
                    .getFocusedCell();
            if (pos.getColumn() - 1 >= 0) {
                // go to previous row
                search_table.getSelectionModel().select(pos.getRow(),
                        getTableColumn(pos.getTableColumn(), -1));
            } else if (pos.getRow() < search_table.getItems().size()) {
                // wrap to end of previous row
                search_table.getSelectionModel().select(pos.getRow() - 1,
                        search_table.getVisibleLeafColumn(
                                search_table.getVisibleLeafColumns().size() - 1));
            }
        } else {
            int focusIndex = search_table.getFocusModel().getFocusedIndex();
            if (focusIndex == -1) {
                search_table.getSelectionModel().select(search_table.getItems().size() - 1);
            } else if (focusIndex > 0) {
                search_table.getSelectionModel().select(focusIndex - 1);
            }
        }
    }

    private TableColumn<AppData, ?> getTableColumn(
            final TableColumn<AppData, ?> column, int offset) {
        int columnIndex = search_table.getVisibleLeafIndex(column);
        int newColumnIndex = columnIndex + offset;
        return search_table.getVisibleLeafColumn(newColumnIndex);
    }

    public void MostPopularApps() {
        MongoDriver driver = new MongoDriver();
        MongoCollection<Document> collection = driver.getCollection("apps");

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
                        0.0,
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
            MongoCollection<Document> collection = driver.getCollection("apps");

            // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

            Consumer<Document> processBlock = new Consumer<Document>() {
                @Override
                public void accept(Document document) {

                    String name = (String) document.get("name");
                    String _id = (String) document.get("_id");
                    String category = (String) document.get("category");
                    double avg = (Double) document.get("Avg");


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
            MongoCollection<Document> collection = driver.getCollection("apps");

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
                                    .append("year", Integer.parseInt(text_year.getText().toString()))
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
        App.setRoot("users");
    }

    public void appmainpage(ActionEvent actionEvent) throws IOException {
        App.setRoot("admin");
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
        setRoot("login");
    }
}

