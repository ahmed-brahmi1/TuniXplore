package com.esprit.controllers.User;

import com.esprit.models.User;
import com.esprit.services.User.ServiceUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.esprit.utils.Session; // Assurez-vous que vous avez importé la classe Session

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
    private Label Reset;

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

        // Ajouter un gestionnaire d'événements au label Reset
        Reset.setOnMouseClicked(event -> {
            try {
                // Charger le fichier FXML de la nouvelle interface
                Parent root = FXMLLoader.load(getClass().getResource("/ResetPassword.fxml"));
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

        // Gestionnaire pour le bouton de connexion
        btn_login.setOnAction(event -> {
            String email = login_email.getText();
            String password = login_password.getText();

            // Vérifier les informations de connexion
            ServiceUser serviceUser = new ServiceUser();
            User user = serviceUser.authenticate(email, password);

            if (user != null) {
                // Connexion réussie

                // Enregistrer l'utilisateur dans la session
                Session.setCurrentUser(user); // Ajout de l'utilisateur dans la session

                if ("Admin".equals(user.getRole())) {
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("/Dashboard.fxml"));
                        Scene scene = new Scene(root);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();
                    } catch (Exception e) {
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement du tableau de bord.");
                    }
                } else if ("Voyageur".equals(user.getRole())) {
                    try {
                        // Charger le fichier FXML du profil
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Profile.fxml"));
                        Parent root = loader.load();

                        // Récupérer le contrôleur du profil
                        ProfileController profileController = loader.getController();

                        // Passer l'utilisateur connecté au contrôleur du profil
                        profileController.initUser(user);

                        // Changer de scène
                        Scene scene = new Scene(root);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();
                    } catch (Exception e) {
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement du profil.");
                    }
                }
            } else {
                // Afficher un message d'erreur
                showAlert(Alert.AlertType.WARNING, "Erreur de connexion", "Email ou mot de passe incorrect.");
            }
        });
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null); // Pas de texte d'en-tête
        alert.setContentText(message);
        alert.showAndWait();
    }
}
