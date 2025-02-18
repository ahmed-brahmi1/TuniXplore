package com.esprit.controllers.User;

import com.esprit.models.Parrainage;
import com.esprit.models.User;
import com.esprit.services.User.ServiceUser;
import com.esprit.services.User.ServiceParrainage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.List;

public class AddParrainageByAdminController {

    @FXML
    private Button AddParrainagebtn;

    @FXML
    private TextField Code;

    @FXML
    private ComboBox<User> Voyageurs; // Changez le type à String

    @FXML
    private Button UpdateUserbtn1;

    @FXML
    private Button goBackAdmin;

    private User selectedUser;

    private ServiceUser userService = new ServiceUser();
    private final ServiceParrainage parrainageService = new ServiceParrainage();
    private Parrainage ParraianageToEdit;

    @FXML
    public void initialize() {
        loadUsers();
        UpdateUserbtn1.setVisible(false);
        loadUsers();
    }










    // Charger tous les utilisateurs dans la ComboBox
    private void loadUsers() {
        List<User> userList = userService.getAllUsers();
        ObservableList<User> observableUsers = FXCollections.observableArrayList(userList);

        // Associer le prénom et nom comme affichage dans la ComboBox
        Voyageurs.setItems(observableUsers);
        Voyageurs.setConverter(new StringConverter<>() {
            @Override
            public String toString(User user) {
                return user == null ? "" : user.getPrenom() + " " + user.getNom();
            }

            @Override
            public User fromString(String string) {
                return null;
            }
        });
    }

    public void ajouterParrainage() {
        try {
            // ✅ 1. Vérifier si l'utilisateur est sélectionné
            User selectedUser = Voyageurs.getValue();
            String parrainCode = Code.getText().trim(); // Supprimer les espaces

            StringBuilder erreurs = new StringBuilder(); // Liste d'erreurs

            // Vérification des champs
            if (selectedUser == null) {
                erreurs.append("- Aucun utilisateur sélectionné.\n");
            }

            if (parrainCode.isEmpty()) {
                erreurs.append("- Le champ Code doit être rempli.\n");
            }

            // ✅ Si erreurs, afficher une alerte et arrêter l’exécution
            if (erreurs.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Erreur de saisie");
                alert.setHeaderText("Champs manquants");
                alert.setContentText(erreurs.toString());
                alert.showAndWait();
                return; // ⛔ Arrêter ici si erreurs
            }

            // ✅ 2. Créer l'objet Parrainage et ajouter dans la base
            int userId = selectedUser.getId();
            Parrainage parrainage = new Parrainage(userId, parrainCode);
            parrainageService.ajouter(parrainage);

            // ✅ 3. Afficher un message de succès
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Succès");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Le parrainage a été ajouté avec succès !");
            successAlert.showAndWait();
            System.out.println("Parrainage ajouté avec succès !");
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ajout : " + e.getMessage());
            e.printStackTrace();
        }

        // ✅ 4. Ouvrir le Dashboard
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashboardParrainage.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // ✅ 5. Fermer la fenêtre actuelle
        ((Stage) AddParrainagebtn.getScene().getWindow()).close();
    }




    private void setupComboBoxListener() {
        Voyageurs.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                selectedUser = newValue;

            }
        });
    }


    public void setParrainageData(Parrainage selectedParrainage) {
        this.ParraianageToEdit = selectedParrainage;

        Code.setText(selectedParrainage.getCode());



        AddParrainagebtn.setVisible(false);
        UpdateUserbtn1.setVisible(true);
    }
    @FXML
    private void handleUpdateParrainage() {
        AddParrainagebtn.setVisible(false);

        // ✅ Récupérer les données
        String c = Code.getText().trim();
        User selectedUser = Voyageurs.getValue();

        // ✅ Liste pour les erreurs
        StringBuilder erreurs = new StringBuilder();

        // ✅ Vérifications
        if (selectedUser == null) {
            erreurs.append("- Veuillez sélectionner un utilisateur.\n");
        }
        if (c.isEmpty()) {
            erreurs.append("- Le champ Code est obligatoire.\n");
        }

        // ✅ Si erreurs, afficher une alerte et arrêter l’exécution
        if (erreurs.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText("Champs manquants");
            alert.setContentText(erreurs.toString());
            alert.showAndWait();
            return; // ⛔ Stopper l’exécution
        }

        // ✅ Ajouter ou modifier selon le contexte
        if (ParraianageToEdit == null) {
            // ➕ Ajouter un nouveau parrainage
            Parrainage p = new Parrainage(selectedUser.getId(), c);
            parrainageService.ajouterParAdmin(p);
        } else {
            // ✏️ Modifier un parrainage existant
            ParraianageToEdit.setCode(c);
            ParraianageToEdit.setId_sender(selectedUser.getId());
            parrainageService.modifier(ParraianageToEdit);
        }

        // ✅ Afficher un message de succès
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Succès");
        successAlert.setHeaderText(null);
        successAlert.setContentText("Parrainage traité avec succès !");
        successAlert.showAndWait();

        // ✅ Charger et afficher le DashboardParrainage
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashboardParrainage.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard Parrainage");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // ✅ Fermer la fenêtre actuelle
        ((Stage) AddParrainagebtn.getScene().getWindow()).close();
    }



    @FXML
    private void GoBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashboardParrainage.fxml"));
            Parent root = loader.load();

            // Afficher la nouvelle interface
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestion de parrainages");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        ((Stage) goBackAdmin.getScene().getWindow()).close();
    }
}



