package com.esprit.controllers.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class loginController {

    @FXML
    private Button btn_login;

    @FXML
    private TextField login_email;

    @FXML
    private PasswordField login_password;

    @FXML
    private Label vers_ajout;

    @FXML
    public void initialize() {
        // Ajouter un gestionnaire d'événements au label vers_ajout
        vers_ajout.setOnMouseClicked(event -> {
            try {
                // Charger le fichier FXML de la nouvelle interface
                Parent root = FXMLLoader.load(getClass().getResource("/ajoutUser.fxml"));
                Scene scene = new Scene(root);

                // Obtenir la scène actuelle à partir de l'événement
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                // Changer la scène
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}