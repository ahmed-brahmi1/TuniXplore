package com.esprit.test;


import com.esprit.utils.DataBase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        URL fxmlUrl = getClass().getResource("/Dashboard.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);


        Scene scene = new Scene(fxmlLoader.load(), 900, 800);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}