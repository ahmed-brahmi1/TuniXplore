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
        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'utilisateur n'est pas connecté.");
            return;
        }

        // Récupérer les données saisies
        String nomUser = name.getText().trim();
        String prenomUser = lastName.getText().trim();
        String ageText = Age.getText().trim();
        String telText = tel.getText().trim();
        String emailUser = email.getText().trim();

        // Liste pour accumuler les erreurs
        StringBuilder erreurs = new StringBuilder();

        // ✅ Vérification des champs vides
        if (nomUser.isEmpty() || prenomUser.isEmpty() || ageText.isEmpty() ||
                telText.isEmpty() || emailUser.isEmpty()) {
            erreurs.append("- Tous les champs doivent être remplis.\n");
        }

        // ✅ Vérification du nom et prénom (lettres uniquement)
        if (!nomUser.matches("[a-zA-Z]+")) {
            erreurs.append("- Le nom doit contenir uniquement des lettres.\n");
        }
        if (!prenomUser.matches("[a-zA-Z]+")) {
            erreurs.append("- Le prénom doit contenir uniquement des lettres.\n");
        }

        // ✅ Vérification de l'email (forme correcte)
        if (!emailUser.matches("^[\\w.-]+@[\\w.-]+\\.com$")) {
            erreurs.append("- L'adresse email doit être valide (exemple : nom@domaine.com).\n");
        }

        // ✅ Vérification des champs numériques (âge, téléphone)
        int ageUser = 0;
        int telUser = 0;
        try {
            ageUser = Integer.parseInt(ageText);
            if (ageUser <= 0) {
                erreurs.append("- L'âge doit être un nombre positif.\n");
            }
        } catch (NumberFormatException e) {
            erreurs.append("- L'âge doit être un nombre.\n");
        }

        // ✅ Vérification du téléphone (exactement 8 chiffres)
        if (!telText.matches("\\d{8}")) {
            erreurs.append("- Le numéro de téléphone doit contenir exactement 8 chiffres.\n");
        } else {
            try {
                telUser = Integer.parseInt(telText);
            } catch (NumberFormatException e) {
                erreurs.append("- Le numéro de téléphone doit être un nombre valide.\n");
            }
        }

        // ✅ Afficher toutes les erreurs ensemble
        if (erreurs.length() > 0) {
            showAlert(Alert.AlertType.WARNING, "Erreurs de saisie", erreurs.toString());
            return; // Arrêter si erreurs
        }

        // ✅ Mise à jour de l'utilisateur
        currentUser.setNom(nomUser);
        currentUser.setPrenom(prenomUser);
        currentUser.setAge(ageUser);
        currentUser.setTel(telUser);
        currentUser.setEmail(emailUser);

        // ✅ Mettre à jour la session
        Session.setCurrentUser(currentUser);

        // ✅ Enregistrer les modifications dans la base
        ServiceUser serviceUser = new ServiceUser();
        try {
            System.out.println("Tentative de mise à jour pour l'utilisateur: " + currentUser.getNom());
            serviceUser.modifier(currentUser);
            System.out.println("Utilisateur mis à jour avec succès.");

            // ✅ Affichage d'une notification de succès
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Données mises à jour avec succès !");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la mise à jour des données.");
            e.printStackTrace();
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
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}