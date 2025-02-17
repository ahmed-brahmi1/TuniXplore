package com.esprit.controllers.User;

import com.esprit.models.User;
import com.esprit.services.User.ServiceUser;
import com.esprit.utils.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class ProfileController {

    @FXML
    private TextField name;

    @FXML
    private TextField lastName;

    @FXML
    private TextField Age;

    @FXML
    private TextField tel;

    @FXML
    private TextField email;

    private User currentUser; // Déclaration de currentUser au niveau de la classe

    public void initialize() {
        // Charger automatiquement depuis la session
        currentUser = Session.getCurrentUser(); // Utiliser l'attribut de classe, pas une variable locale

        if (currentUser != null) {
            name.setText(currentUser.getNom());
            lastName.setText(currentUser.getPrenom());
            Age.setText(String.valueOf(currentUser.getAge()));
            tel.setText(String.valueOf(currentUser.getTel()));
            email.setText(currentUser.getEmail());
        } else {
            // Si l'utilisateur n'est pas connecté, afficher un message d'erreur ou rediriger
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Vous devez être connecté pour accéder à cette page.");
            alert.showAndWait();
            // Ajouter ici la logique de redirection vers la page de connexion si nécessaire
        }
    }

    public void initUser(User user) {
        // ✅ Affiche les données reçues directement
        name.setText(user.getNom());
        lastName.setText(user.getPrenom());
        Age.setText(String.valueOf(user.getAge()));
        tel.setText(String.valueOf(user.getTel()));
        email.setText(user.getEmail());
    }

    public void saveChanges() {
        // Si currentUser n'est pas null, on met à jour ses données
        if (currentUser != null) {
            currentUser.setNom(name.getText());
            currentUser.setPrenom(lastName.getText());
            currentUser.setAge(Integer.parseInt(Age.getText()));
            currentUser.setTel(Integer.parseInt(tel.getText()));
            currentUser.setEmail(email.getText());

            // Mettre à jour la session avec le nouvel utilisateur
            Session.setCurrentUser(currentUser);

            // Enregistrer les modifications dans la base de données
            ServiceUser serviceUser = new ServiceUser();
            System.out.println("Tentative de mise à jour pour l'utilisateur: " + currentUser.getNom());
            serviceUser.modifier(currentUser);
            System.out.println("Utilisateur mis à jour avec succès.");

            // Affichage d'une notification de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Données mises à jour avec succès !");
            alert.showAndWait();
        } else {
            System.out.println("Erreur : l'utilisateur n'est pas connecté.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur : l'utilisateur n'est pas connecté.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleEditProfile() {
        try {
            // Appeler la méthode saveChanges pour enregistrer les modifications
            saveChanges();
        } catch (Exception e) {
            System.out.println("Une erreur s'est produite lors de la mise à jour du profil : " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur s'est produite lors de la mise à jour du profil.");
            alert.showAndWait();
        }
    }
}