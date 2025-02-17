package com.esprit.controllers.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ResetPasswordController {

    @FXML
    private Button btn_reset;

    @FXML
    private Button goBack;

    @FXML
    private TextField reset_email;

    @FXML
    private void goBack() {
        try {
            // Charger le fichier FXML de l'interface login
            Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir du bouton "Go Back"
            Stage stage = (Stage) goBack.getScene().getWindow();

            // Changer la scène
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
