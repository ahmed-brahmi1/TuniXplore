package com.esprit.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

            // Vérification si le fichier FXML existe
            URL fxmlUrl = getClass().getResource("/ViewVoiture/voiture/liste_voitures.fxml");

            FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
            Scene scene = new Scene(fxmlLoader.load(), 1200, 700);

            stage.setTitle("Liste Voiture!");
            stage.setScene(scene);
            stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
