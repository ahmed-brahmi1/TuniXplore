package com.example.vol;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.event.ActionEvent;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML file and connect to the controller
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("vol.fxml"));
        AnchorPane root = fxmlLoader.load();  // Load the FXML layout

        // Create the scene and set it on the stage
        Scene scene = new Scene(root);
        stage.setTitle("Flight Management");
        stage.setScene(scene);
        stage.show();  // Show the window
    }




    public static void main(String[] args) {
        launch(args);  // Launch JavaFX application
    }
}
