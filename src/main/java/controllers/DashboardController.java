package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class DashboardController {

    // Action for "Gestion des vols" button
    @FXML
    private void showGestionVols(ActionEvent event) {
        try {
            // Load the Vol.fxml (flight management page)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vol/Vol.fxml"));
            Parent root = loader.load();  // Load the layout for the flight management page

            // Get the current stage (window) and change the scene to the flight management page
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 600));  // Set the new scene
            stage.show();  // Show the window
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
