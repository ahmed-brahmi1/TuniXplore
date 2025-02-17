package com.esprit.controllers;

import com.esprit.controllers.User.AddUserByAdminController;
import com.esprit.models.User;
import com.esprit.services.User.ServiceUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class DashboardController {

    @FXML
    private TableView<User> tableViewUsers; // Assure-toi d'ajouter fx:id dans FXML si manquant

    @FXML
    private TableColumn<User, Integer> IdColumn;

    @FXML
    private TableColumn<User, String> IdNom;

    @FXML
    private TableColumn<User, String> Idprenom;

    @FXML
    private TableColumn<User, Integer> IdAge;

    @FXML
    private TableColumn<User, String> IdGenre;

    @FXML
    private TableColumn<User, Integer> IdNum;

    @FXML
    private TableColumn<User, String> IdEmail;

    @FXML
    private TableColumn<User, String> IdMdp;

    @FXML
    private TableColumn<User, String> IdRole;
    @FXML
    private Button naviguer_vers_parrainage;

    @FXML
    private Button Updatebtn;
    @FXML
    private Button naviguer_versAjout;


    private User userToEdit; // Pour stocker l'utilisateur à modifier

    private final ServiceUser serviceUser = new ServiceUser();

    @FXML
    public void initialize() {
        afficherUtilisateurs();
    }

    private void afficherUtilisateurs() {
        List<User> users = serviceUser.afficher();
        ObservableList<User> userList = FXCollections.observableArrayList(users);

        // Associer les colonnes aux attributs de l'objet User
        IdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        IdNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        Idprenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        IdAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        IdGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        IdNum.setCellValueFactory(new PropertyValueFactory<>("tel"));
        IdEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        IdMdp.setCellValueFactory(new PropertyValueFactory<>("mdp"));
        IdRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Mettre la liste des utilisateurs dans la TableView
        tableViewUsers.setItems(userList);

        // Rafraîchir la table au démarrage
        refreshTable();
    }
    private void refreshTable() {
        // Récupérer les données de la base de données
        List<User> users = serviceUser.afficher();

        // Convertir les données en ObservableList
        ObservableList<User> userList = FXCollections.observableArrayList(users);

        // Mettre à jour la TableView
        tableViewUsers.setItems(userList);
    }
    @FXML
    private void handleSupprimer() {
        User selectedUser = tableViewUsers.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un utilisateur à supprimer.");
            alert.showAndWait();
            return;
        }

        // Boîte de dialogue de confirmation
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmer la suppression");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer cet utilisateur ?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Supprimer l'utilisateur
            serviceUser.supprimer(selectedUser.getId());
            tableViewUsers.getItems().remove(selectedUser);

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Suppression réussie");
            successAlert.setHeaderText(null);
            successAlert.setContentText("L'utilisateur a été supprimé avec succès.");
            successAlert.showAndWait();
        }
    }
    @FXML
    private void handleModifier() {
        // Récupérer l'utilisateur sélectionné
        User selectedUser = tableViewUsers.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            // Afficher un message d'erreur si aucune ligne n'est sélectionnée
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un utilisateur à modifier.");
            alert.showAndWait();
            return;
        }

        // Naviguer vers l'interface AddUserByAdmin
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddUserByAdmin.fxml"));
            Parent root = loader.load();

            // Passer les données de l'utilisateur sélectionné au contrôleur AddUserByAdmin
            AddUserByAdminController controller = loader.getController();
            controller.setUserData(selectedUser);

            // Afficher la nouvelle interface
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter l'utilisateur");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        ((Stage) Updatebtn.getScene().getWindow()).close();
    }
    @FXML
    private void naviguerVersAjout() {
        // Naviguer vers l'interface AddUserByAdmin
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddUserByAdmin.fxml"));
            Parent root = loader.load();

            // Afficher la nouvelle interface
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier l'utilisateur");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        ((Stage) naviguer_versAjout.getScene().getWindow()).close();
    }
    @FXML
    public void log_out(){
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmer la suppression");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer cet utilisateur ?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Supprimer l'utilisateur
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();

            // Afficher la nouvelle interface
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("login");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        ((Stage) naviguer_versAjout.getScene().getWindow()).close();
    }}
    @FXML
    private void naviguerVersParrainages() {
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
        ((Stage) naviguer_vers_parrainage.getScene().getWindow()).close();
    }
    }





