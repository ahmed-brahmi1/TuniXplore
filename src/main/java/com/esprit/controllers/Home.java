package com.esprit.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;


public class Home extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        URL fxmlUrl = getClass().getResource("/GestionHotel/hotel-list.fxml");

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
        stage.setScene(scene);
        stage.show();

    }
}