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
    private Button btn;

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
            int ageUser = Integer.parseInt(age.getText().trim());
            String genreUser = g.getText().trim();
            int telUser = Integer.parseInt(num_tel.getText().trim());
            String emailUser = email.getText().trim();
            String mdpUser = mdp.getText().trim();
            String codeParrainage = code.getText().trim(); // Récupérer le code de parrainage

            // Vérification des champs (optionnel)
            if (nomUser.isEmpty() || prenomUser.isEmpty() || emailUser.isEmpty() || mdpUser.isEmpty() || codeParrainage.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs.");
                return;
            }

            // Créer l'utilisateur
            User newUser = new User(nomUser, prenomUser, ageUser, genreUser, emailUser, mdpUser, "Voyageur", telUser);

            // Ajouter l'utilisateur dans la base
            ServiceUser serviceUser = new ServiceUser();
            serviceUser.ajouter(newUser);

            // ✅ Stocker l'utilisateur dans la session
            Session.setCurrentUser(newUser);



            // Ajouter un parrainage avec l'ID de l'utilisateur et le code saisi
            ServiceParrainage serviceParrainage = new ServiceParrainage();
            Parrainage parrainage = new Parrainage(newUser.getId(), codeParrainage); // Utilisation du code saisi
            serviceParrainage.ajouter(parrainage);

            // ✅ Rediriger vers la page de profil
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Profile.fxml"));
                Parent root = loader.load();

                // ✅ Passer l'utilisateur au contrôleur du profil
                ProfileController profileController = loader.getController();
                profileController.initUser(newUser);

                Stage stage = (Stage) btn.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la redirection vers le profil.");
                e.printStackTrace();
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Erreur de format", "Vérifiez les champs numériques (âge, téléphone).");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'inscription.");
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
