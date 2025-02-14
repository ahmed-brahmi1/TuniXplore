package com.esprit.controllers.User;

import com.esprit.models.User;
import com.esprit.services.User.ServiceUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ajoutUserController {

    @FXML
    private TextField age;

    @FXML
    private TextField code;

    @FXML
    private TextField email;

    @FXML
    private TextField g;

    @FXML
    private Button goBack;

    @FXML
    private TextField mdp;

    @FXML
    private TextField nom;

    @FXML
    private TextField num_tel;

    @FXML
    private TextField prenom;

    @FXML
    private Button btn_inscrire; // Bouton pour s'inscrire

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
    private void handleInscription() {
        try {
            // Récupérer les données saisies par l'utilisateur
            String nomUser = nom.getText();
            String prenomUser = prenom.getText();
            int ageUser = Integer.parseInt(age.getText());
            String genreUser = g.getText();
            int telUser = Integer.parseInt(num_tel.getText());
            String emailUser = email.getText();
            String mdpUser = mdp.getText();

            // Créer un nouvel utilisateur
            User newUser = new User(nomUser, prenomUser, ageUser, genreUser, emailUser, mdpUser, "voyageur", telUser);

            // Ajouter l'utilisateur à la base de données
            ServiceUser serviceUser = new ServiceUser();
            serviceUser.ajouter(newUser);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null); // Pas de texte d'en-tête
            alert.setContentText("L'utilisateur a été ajouté avec succès !");
            alert.showAndWait();

            System.out.println("Utilisateur ajouté avec succès !");


        } catch (NumberFormatException e) {
            System.out.println("Erreur de format : Veuillez vérifier les champs numériques (âge, téléphone).");
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }
}