package com.esprit.controllers.User;

import com.esprit.models.Parrainage;
import com.esprit.models.User;
import com.esprit.services.User.ServiceParrainage;
import com.esprit.services.User.ServiceUser;
import com.esprit.utils.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ajoutUserController {

    @FXML
    private TextField age;

    @FXML
    private TextField code;

    @FXML
    private TextField email;

    @FXML
    private ComboBox<String> g;

    @FXML
    private Button goBack;

    @FXML
    private TextField mdp;

    @FXML
    private PasswordField confirmer;

    @FXML
    private TextField nom;

    @FXML
    private TextField num_tel;

    @FXML
    private TextField prenom;

    @FXML
    private Button btn;

    @FXML
    public void initialize() {
        // Ajouter des rôles dans le ComboBox
        g.getItems().addAll("Homme","Femme");

    }

    @FXML
    private void goBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
            Stage stage = (Stage) goBack.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleInscription() {
        try {
            // Récupérer les données saisies
            String nomUser = nom.getText().trim();
            String prenomUser = prenom.getText().trim();
            String ageText = age.getText().trim();
            String genreUser = g.getValue();
            String telText = num_tel.getText().trim();
            String emailUser = email.getText().trim();
            String mdpUser = mdp.getText().trim();
            String codeParrainage = code.getText().trim();

            // Liste pour accumuler les erreurs
            StringBuilder erreurs = new StringBuilder();

            // ✅ Vérification des champs vides
            if (nomUser.isEmpty() || prenomUser.isEmpty() || ageText.isEmpty() ||
                    genreUser==null || telText.isEmpty() || emailUser.isEmpty() ||
                    mdpUser.isEmpty() || codeParrainage.isEmpty()) {
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

            // ✅ Vérification du mot de passe (au moins 8 caractères)
            if (mdpUser.length() < 8) {
                erreurs.append("- Le mot de passe doit contenir au moins 8 caractères.\n");
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
            try {
                ageUser = Integer.parseInt(ageText);
                if (ageUser > 90||ageUser<10) {
                    erreurs.append("- L'âge doit être Valide.\n");
                }
            } catch (NumberFormatException e) {
                erreurs.append("- L'âge doit être un nombre.\n");
            }

            try {
                telUser = Integer.parseInt(telText);
                if (telUser <= 0) {
                    erreurs.append("- Le numéro de téléphone doit être un nombre positif.\n");
                }
            } catch (NumberFormatException e) {
                erreurs.append("- Le numéro de téléphone doit être un nombre.\n");
            }
            // ✅ Vérification du téléphone (exactement 8 chiffres)
            if (!telText.matches("\\d{8}")) {
                erreurs.append("- Le numéro de téléphone doit contenir exactement 8 chiffres.\n");
            }
            // ✅ Vérification de la correspondance des mots de passe
            String mdpConfirmation = confirmer.getText().trim();
            if (!mdpUser.equals(mdpConfirmation)) {
                erreurs.append("- Les mots de passe ne correspondent pas.\n");
            }else {
                try {
                    telUser = Integer.parseInt(telText);
                } catch (NumberFormatException e) {
                    erreurs.append("- Le numéro de téléphone doit être un nombre valide.\n");
                }}
            // Vérifier si l'email existe déjà dans la base
            ServiceUser serviceUserEmail = new ServiceUser();
            if (serviceUserEmail.emailExiste(emailUser)) {
                erreurs.append("- Cet email est déjà utilisé. Veuillez en choisir un autre.\n");
            }

            // ✅ Afficher toutes les erreurs ensemble
            if (erreurs.length() > 0) {
                showAlert(Alert.AlertType.WARNING, "Erreurs de saisie", erreurs.toString());
                return; // Arrêter si erreurs
            }

            // ✅ Créer l'utilisateur
            User newUser = new User(nomUser, prenomUser, ageUser, genreUser, emailUser, mdpUser, "Voyageur", telUser);

            // ✅ Ajouter l'utilisateur dans la base
            ServiceUser serviceUser = new ServiceUser();
            serviceUser.ajouter(newUser);

            // ✅ Stocker l'utilisateur dans la session
            Session.setCurrentUser(newUser);

            // ✅ Ajouter le parrainage
            ServiceParrainage serviceParrainage = new ServiceParrainage();
            Parrainage parrainage = new Parrainage(newUser.getId(), codeParrainage);
            serviceParrainage.ajouter(parrainage);

            // ✅ Rediriger vers la page de profil
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Profile.fxml"));
                Parent root = loader.load();

                ProfileController profileController = loader.getController();
                profileController.initUser(newUser);

                Stage stage = (Stage) btn.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la redirection vers le profil.");
                e.printStackTrace();
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur inattendue lors de l'inscription.");
            e.printStackTrace();
        }
    }


    /**
     * Affiche une boîte de dialogue d'alerte.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
