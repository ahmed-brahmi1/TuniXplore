package com.esprit.controllers.User;
import com.esprit.models.Parrainage;
import com.esprit.services.User.ServiceParrainage;
import com.esprit.models.User;
import com.esprit.services.User.ServiceUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class AddUserByAdminController {

    @FXML
    private Button AddUserbtn;
    @FXML
    private Button UpdateUserbtn1;
    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField ageField;

    @FXML
    private ComboBox<String> g;

    @FXML
    private TextField telField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField mdpField;

    @FXML
    private TextField Code;

    @FXML
    private Button goBackAdmin;

    @FXML
    private ComboBox<String> Roles;

    private final ServiceUser userService = new ServiceUser();
    private final ServiceParrainage parrainageService = new ServiceParrainage();  // Service pour gérer le parrainage
    private User userToEdit; // Pour stocker l'utilisateur à modifier


    @FXML
    public void initialize() {
        // Ajouter des rôles dans le ComboBox
        Roles.getItems().addAll("Admin", "Voyageur", "Agent des voitures","Agent des hotels","Agent des vols");
            g.getItems().addAll("Homme","Femme");
        AddUserbtn.setVisible(true);
        UpdateUserbtn1.setVisible(false);

    }
    @FXML
    private void goBackAdmin() {
        try {
            // Charger le fichier FXML de l'interface login
            Parent root = FXMLLoader.load(getClass().getResource("/Dashboard.fxml"));
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir du bouton "Go Back"
            Stage stage = (Stage) goBackAdmin.getScene().getWindow();

            // Changer la scène
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void ajouterUtilisateur() {
        try {
            // Récupérer les données saisies
            String nomUser = nomField.getText().trim();
            String prenomUser = prenomField.getText().trim();
            String ageText = ageField.getText().trim();
            String genreUser = g.getValue();
            String telText = telField.getText().trim();
            String emailUser = emailField.getText().trim();
            String mdpUser = mdpField.getText().trim();
            String roleUser = Roles.getValue();
            String parrainCode = Code.getText().trim();

            // Liste pour accumuler les erreurs
            StringBuilder erreurs = new StringBuilder();

            // ✅ 1. Vérification des champs vides
            if (nomUser.isEmpty() || prenomUser.isEmpty() || ageText.isEmpty() ||
                   genreUser==null|| telText.isEmpty() || emailUser.isEmpty() || mdpUser.isEmpty() || roleUser == null) {
                erreurs.append("- Tous les champs obligatoires doivent être remplis.\n");
            }

            // ✅ 2. Vérification du nom et prénom (lettres uniquement)
            if (!nomUser.matches("[a-zA-Z]+")) {
                erreurs.append("- Le nom doit contenir uniquement des lettres.\n");
            }
            if (!prenomUser.matches("[a-zA-Z]+")) {
                erreurs.append("- Le prénom doit contenir uniquement des lettres.\n");
            }

            // ✅ 3. Vérification de l'email (forme correcte)
            if (!emailUser.matches("^[\\w.-]+@[\\w.-]+\\.com$")) {
                erreurs.append("- L'adresse email doit être valide (exemple : nom@domaine.com).\n");
            }

            // ✅ 4. Vérification du mot de passe (au moins 8 caractères)
            if (mdpUser.length() < 8) {
                erreurs.append("- Le mot de passe doit contenir au moins 8 caractères.\n");
            }

            // ✅ 5. Vérification de l'âge (nombre positif)
            int ageUser = 0;
            try {
                ageUser = Integer.parseInt(ageText);
                if (ageUser <= 0) {
                    erreurs.append("- L'âge doit être un nombre positif.\n");
                }
            } catch (NumberFormatException e) {
                erreurs.append("- L'âge doit être un nombre.\n");
            }

            // ✅ 6. Vérification du téléphone (exactement 8 chiffres)
            int telUser = 0;
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
                return; // Arrêter l’exécution si erreurs
            }

            // ✅ Création de l'utilisateur
            User user = new User(nomUser, prenomUser, ageUser, genreUser, emailUser, mdpUser, roleUser, telUser);
            userService.ajouterParAdmin(user);
            System.out.println("Utilisateur ajouté avec succès !");

            // ✅ Gestion du parrainage
            if (!parrainCode.isEmpty()) {
                Parrainage parrainage = new Parrainage(user.getId(), parrainCode);
                parrainageService.ajouter(parrainage);
                System.out.println("Parrainage ajouté avec succès !");
            }

            // ✅ Notification de succès
            showAlert(Alert.AlertType.INFORMATION, "Succès", "L'utilisateur a été ajouté avec succès !");

            // ✅ Redirection vers le Dashboard
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Dashboard");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ouverture du Dashboard.");
            }

            // ✅ Fermer la fenêtre actuelle
            ((Stage) AddUserbtn.getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'ajout de l'utilisateur.");
        }
    }
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }



    // Méthode pour remplir le formulaire avec les données de l'utilisateur
    public void setUserData(User user) {
        this.userToEdit = user;

        nomField.setText(user.getNom());
        prenomField.setText(user.getPrenom());
        ageField.setText(String.valueOf(user.getAge()));

        telField.setText(String.valueOf(user.getTel()));
        emailField.setText(user.getEmail());
        mdpField.setText(user.getMdp());
        Roles.setValue(user.getRole());

        AddUserbtn.setVisible(false);
        UpdateUserbtn1.setVisible(true);
    }


    @FXML
    private void handleUpdateUser() {
        try {
            // ✅ Cacher le champ du code de parrainage
            Code.setVisible(false);

            // ✅ Récupérer les données saisies
            String nomUser = nomField.getText().trim();
            String prenomUser = prenomField.getText().trim();
            String ageText = ageField.getText().trim();
            String genreUser = g.getValue();
            String telText = telField.getText().trim();
            String emailUser = emailField.getText().trim();
            String mdpUser = mdpField.getText().trim();
            String roleUser = Roles.getValue();

            // ✅ Liste pour accumuler les erreurs
            StringBuilder erreurs = new StringBuilder();

            // ✅ 1. Vérification des champs vides
            if (nomUser.isEmpty() || prenomUser.isEmpty() || ageText.isEmpty() ||
                    genreUser==null || telText.isEmpty() || emailUser.isEmpty() || mdpUser.isEmpty() || roleUser == null) {
                erreurs.append("- Tous les champs obligatoires doivent être remplis.\n");
            }

            // ✅ 2. Vérification du nom et prénom (lettres uniquement)
            if (!nomUser.matches("[a-zA-Z]+")) {
                erreurs.append("- Le nom doit contenir uniquement des lettres.\n");
            }
            if (!prenomUser.matches("[a-zA-Z]+")) {
                erreurs.append("- Le prénom doit contenir uniquement des lettres.\n");
            }

            // ✅ 3. Vérification de l'email (forme correcte)
            if (!emailUser.matches("^[\\w.-]+@[\\w.-]+\\.com$")) {
                erreurs.append("- L'adresse email doit être valide (exemple : nom@domaine.com).\n");
            }

            // ✅ 4. Vérification du mot de passe (au moins 8 caractères)
            if (mdpUser.length() < 8) {
                erreurs.append("- Le mot de passe doit contenir au moins 8 caractères.\n");
            }

            // ✅ 5. Vérification de l'âge (nombre positif)
            int ageUser = 0;
            try {
                ageUser = Integer.parseInt(ageText);
                if (ageUser <= 0) {
                    erreurs.append("- L'âge doit être un nombre positif.\n");
                }
            } catch (NumberFormatException e) {
                erreurs.append("- L'âge doit être un nombre valide.\n");
            }

            // ✅ 6. Vérification du téléphone (exactement 8 chiffres)
            int telUser = 0;
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
                return; // Arrêter l’exécution si erreurs
            }

            // ✅ Ajouter ou mettre à jour l'utilisateur selon la condition
            if (userToEdit == null) {
                // 📌 Ajout d'un nouvel utilisateur
                User user = new User(nomUser, prenomUser, ageUser, genreUser, emailUser, mdpUser, roleUser, telUser);
                userService.ajouterParAdmin(user);
                System.out.println("Nouvel utilisateur ajouté avec succès !");
            } else {
                // 🖊️ Modification de l'utilisateur existant
                userToEdit.setNom(nomUser);
                userToEdit.setPrenom(prenomUser);
                userToEdit.setAge(ageUser);
                userToEdit.setGenre(genreUser);
                userToEdit.setTel(telUser);
                userToEdit.setEmail(emailUser);
                userToEdit.setMdp(mdpUser);
                userToEdit.setRole(roleUser);

                userService.modifier(userToEdit);
                System.out.println("Utilisateur mis à jour avec succès !");
            }

            // ✅ Notification de succès
            showAlert(Alert.AlertType.INFORMATION, "Succès", "L'utilisateur a été enregistré avec succès !");

            // ✅ Redirection vers le Dashboard
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Dashboard");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ouverture du Dashboard.");
            }

            // ✅ Fermer la fenêtre actuelle
            ((Stage) AddUserbtn.getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'enregistrement de l'utilisateur.");
        }
    }
}
