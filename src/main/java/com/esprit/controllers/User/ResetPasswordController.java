package com.esprit.controllers.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Random;

public class ResetPasswordController {

    @FXML
    private Button btn_reset;

    @FXML
    private Button verifier;

    @FXML
    private Button goBack;

    @FXML
    private TextField reset_email;

    @FXML
    private TextField codeField;

    private String codeGenere;

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

    @FXML
    private void envoyerCode() {
        btn_reset.setVisible(false);
        verifier.setVisible(true);
        String email = reset_email.getText().trim();

        if (email.isEmpty() || !email.matches("^[\\w.-]+@[\\w.-]+\\.tn$")) {
            showAlert(Alert.AlertType.WARNING, "Erreur", "Veuillez entrer une adresse e-mail valide.");
            return;
        }

        // Générer un code à 4 chiffres et l'afficher
        codeGenere = String.format("%04d", new Random().nextInt(10000));


        // Envoyer le code par email
        EmailSender.envoyerEmail(email, codeGenere);
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Un code de vérification a été envoyé à votre e-mail.");

        // Afficher le champ de code
        codeField.setVisible(true);
    }
    @FXML
    private void verifierCode() {
        String codeSaisi = codeField.getText().trim();

        if (codeSaisi.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Erreur", "Veuillez entrer le code de vérification.");
            return;
        }

        // Comparer le code saisi avec le code généré
        if (codeSaisi.equals(codeGenere)) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le code est correct. Vous pouvez réinitialiser votre mot de passe.");
            // Ajoutez ici la logique pour réinitialiser le mot de passe
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le code est incorrect. Veuillez réessayer.");
        }
    }
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public String getCodeVerification() {
        return codeField.getText();
    }
}
