package it.unipi.softgram.org.example;

import com.mongodb.client.MongoCollection;
import it.unipi.softgram.controller.mongo.AppMongoManager;
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
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.bson.BsonRegularExpression;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;

public class ApplicationMain  implements Initializable {
    @FXML
    private Text appname,appid,released, price, currency,agegroup,currency1,size;

    @FXML Button follow;
    @FXML private Text developerid;
    @FXML
    private Text name_txt,released_txt,last_txt,developeremail,developerweb;
    @FXML private Button home;
    String app_id;
    @FXML
    ListView<String> list = new ListView<>();
    ObservableList<Review> Reviews = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

       // findApp(app_id);




    }

    public void transferMessage(String message) {
        //Display the message
        appid.setText(message);
        app_id=message;
        findApp(message);
    }
    public void home_fun(ActionEvent actionEvent) {
        try {
            //Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin.fxml"));
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
    public void findApp(String text){
        MongoDriver driver = new MongoDriver();
        MongoCollection<Document> collection = driver.getCollection("app");

        // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

        Consumer<Document> processBlock = document -> {
            System.out.println(document);
            String name = (String) document.get("name");
            System.out.println(name);
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

            ArrayList review= (ArrayList) document.get("reviews");
            try {
                ObservableList<String> items = FXCollections.observableArrayList();
                if(review.isEmpty()) {
                    items.add("No Review");
                    list.setItems(items);
                }else{
                    int j=0;

                    for (int i=0; i<review.size();i++)
                    {
                        review.get(0);
                        Document reviewdata= (Document)  review.get(0);
                        Document content= (Document) reviewdata.get("review");

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String review_date = sdf.format((Date) content.get("date"));

                        items.add(reviewdata.get("score") +"\n"+ content.get("content") +"\n" + review_date +"\n" + reviewdata.get("username") );
                        if(j >10)  {
                            break;
                        }
                        j++;
                    }
                    list.setItems(items);


                  System.out.println("here");
                }
            }catch (NullPointerException e){
                ObservableList<String> items = FXCollections.observableArrayList(
                        "No review");
                list.setItems(items);
                e.printStackTrace();
            }

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

    public void updateApp(ActionEvent actionEvent) {
        double price1 = Double.parseDouble(price.getText());
        String name = name_txt.getText();
        String category1 = currency1.getText();
        String ageGroup = agegroup.getText();
        Stage newStage = new Stage();
        VBox comp = new VBox();
        TextField appname = new TextField(name);
        TextField price11 = new TextField("" + price1);
        TextField category11 = new TextField("" + category1);
        TextField agegroup = new TextField(ageGroup);
        Button update = new Button("Update");
        update.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AppMongoManager app=new AppMongoManager();
                App app1=new App();
                app1.setId(app_id);
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
            app_idd.setId(app_id);
            app.removeApp(app_idd);
            JOptionPane.showMessageDialog(null, "Removed Successfully");
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

        }
    }

    public void FollowApp(ActionEvent actionEvent) {
        AppNeo4jManager app=new AppNeo4jManager();
        User user=new User();
        App app1=new App();
        user.setUsername(appid.getText().toString());
        app1.setId(app_id);

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
}
