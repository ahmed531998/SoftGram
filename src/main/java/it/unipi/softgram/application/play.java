package it.unipi.softgram.application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class play extends Application {
    Button button;
    @Override
    public void start(Stage window) throws Exception {
        Scene welcomeScene, loggedScene, signupScene;

        Label welcome = new Label("Welcome to SoftGram");
        Label home = new Label("Welcome to your homepage");
        Label form = new Label("Welcome to signup form");

        Button login = new Button("login");
        Button signup = new Button("signup");
        Button logout = new Button("logout");
        Button confirm = new Button("confirm");
        Button back = new Button("back");

        HBox welcomeLayout = new HBox(20);
        welcomeLayout.getChildren().addAll(welcome, login, signup);
        welcomeScene = new Scene(welcomeLayout, 540, 300);

        HBox loggedLayout = new HBox(20);
        loggedLayout.getChildren().addAll(home, logout);
        loggedScene = new Scene(loggedLayout, 540, 300);

        HBox newLayout = new HBox(20);
        newLayout.getChildren().addAll(form, back, confirm);
        signupScene = new Scene(newLayout, 540, 300);

        login.setOnAction(e->window.setScene(loggedScene));
        signup.setOnAction(e->window.setScene(signupScene));
        logout.setOnAction(e->window.setScene(welcomeScene));
        confirm.setOnAction(e->window.setScene(loggedScene));
        back.setOnAction(e->window.setScene(welcomeScene));

        window.setScene(welcomeScene);
        window.setTitle("SoftGram");
        window.show();

    }

    public static void main(String[] args){
        launch(args);
    }

}
