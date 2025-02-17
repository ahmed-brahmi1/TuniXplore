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
        List<User> userList = userService.getAllUsers(); // Récupérer liste complète
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
            // Obtenir l'objet User sélectionné
            User selectedUser = Voyageurs.getValue();
            if (selectedUser == null) {
                System.out.println("Erreur : Aucun utilisateur sélectionné !");
                return;
            }

            int userId = selectedUser.getId(); // Récupérer l’ID
            String parrainCode = Code.getText();

            // Créer l'objet Parrainage avec l'ID du parrain
            Parrainage parrainage = new Parrainage(userId, parrainCode);
            parrainageService.ajouter(parrainage);

            // Afficher un message de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Le parrainage a été ajouté avec succès !");
            alert.showAndWait();
            System.out.println("Parrainage ajouté avec succès !");
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ajout : " + e.getMessage());
            e.printStackTrace();
        }

        // Afficher le Dashboard
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

        // Fermer la fenêtre actuelle
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
        if (ParraianageToEdit == null) {
            // Ajouter un nouvel utilisateur
            String c = Code.getText();

            int v = Voyageurs.getValue().getId();

            Parrainage p=new Parrainage(v, c);
            parrainageService.ajouterParAdmin(p);

        } else {
            // Modifier l'utilisateur existant
            ParraianageToEdit.setCode(Code.getText());
            ParraianageToEdit.setId_sender(Voyageurs.getValue().getId());

            parrainageService.modifier(ParraianageToEdit);
        }





        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashboardParrainage.fxml"));
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



