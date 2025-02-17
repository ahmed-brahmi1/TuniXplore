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
    private TextField genreField;

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
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            int age = Integer.parseInt(ageField.getText());
            String genre = genreField.getText();
            int tel = Integer.parseInt(telField.getText());
            String email = emailField.getText();
            String mdp = mdpField.getText();
            String role = Roles.getValue();
            String parrainCode = Code.getText();

            User user = new User(nom, prenom, age, genre, email, mdp, role, tel);
            userService.ajouterParAdmin(user);

            if (parrainCode != null && !parrainCode.isEmpty()) {
                // Récupérer le parrainage en fonction du code
                Parrainage parrainage = new Parrainage(user.getId(), parrainCode);  // Créer un objet parrainage avec l'ID de l'utilisateur

                // Ajouter le parrainage dans la base de données
                parrainageService.ajouter(parrainage);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null); // Pas de texte d'en-tête
            alert.setContentText("L'utilisateur a été ajouté avec succès !");
            alert.showAndWait();
            System.out.println("Utilisateur ajouté avec succès !");
        } catch (NumberFormatException e) {
            System.out.println("Erreur : L'âge doit être un nombre !");
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ajout : " + e.getMessage());
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard.fxml"));
            Parent root = loader.load();


            // Afficher la nouvelle interface
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Fermer la fenêtre après l'ajout ou la modification
        ((Stage) AddUserbtn.getScene().getWindow()).close();


    }

    // Méthode pour remplir le formulaire avec les données de l'utilisateur
    public void setUserData(User user) {
        this.userToEdit = user;

        nomField.setText(user.getNom());
        prenomField.setText(user.getPrenom());
        ageField.setText(String.valueOf(user.getAge()));
        genreField.setText(user.getGenre());
        telField.setText(String.valueOf(user.getTel()));
        emailField.setText(user.getEmail());
        mdpField.setText(user.getMdp());
        Roles.setValue(user.getRole());

        AddUserbtn.setVisible(false);
        UpdateUserbtn1.setVisible(true);
    }


    @FXML
    private void handleUpdateUser() {
        Code.setVisible(false);
        if (userToEdit == null) {
            // Ajouter un nouvel utilisateur
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            int age = Integer.parseInt(ageField.getText());
            String genre = genreField.getText();
            int tel = Integer.parseInt(telField.getText());
            String email = emailField.getText();
            String mdp = mdpField.getText();
            String role = Roles.getValue();

            User user = new User(nom, prenom, age, genre, email, mdp, role, tel);
            userService.ajouterParAdmin(user);

        } else {
            // Modifier l'utilisateur existant
            userToEdit.setNom(nomField.getText());
            userToEdit.setPrenom(prenomField.getText());
            userToEdit.setAge(Integer.parseInt(ageField.getText()));
            userToEdit.setGenre(genreField.getText());
            userToEdit.setTel(Integer.parseInt(telField.getText()));
            userToEdit.setEmail(emailField.getText());
            userToEdit.setMdp(mdpField.getText());
            userToEdit.setRole(Roles.getValue());

            userService.modifier(userToEdit);
        }





        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard.fxml"));
            Parent root = loader.load();


            // Afficher la nouvelle interface
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Fermer la fenêtre après l'ajout ou la modification
        ((Stage) AddUserbtn.getScene().getWindow()).close();
    }
}
