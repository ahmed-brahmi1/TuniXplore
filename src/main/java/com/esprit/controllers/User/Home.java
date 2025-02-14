package com.esprit.controllers.User;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;








public class Home extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("home.fxml"));
        Parent root= FXMLLoader.load(getClass().getResource("/login.fxml"));
        Scene scene=new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}
