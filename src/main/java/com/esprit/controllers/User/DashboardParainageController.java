package com.esprit.controllers.User;

import com.esprit.models.Parrainage;
import com.esprit.models.User;
import com.esprit.services.User.ServiceParrainage;
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

public class DashboardParainageController {


    @FXML
    private TableColumn<Parrainage, Integer> IdColumn;

    @FXML
    private Button Updatebtn;

    @FXML
    private TableColumn<Parrainage, String> code;

    @FXML
    private Button delete;

    @FXML
    private TableColumn<Parrainage, Integer> id_sender;

    @FXML
    private Button log_out;

    @FXML
    private Button naviguer_versAjouter;

    @FXML
    private Button retourner;

    @FXML
    private TableView<Parrainage> tableViewParrainage;
    @FXML
    private Parrainage ParrainageToEdit; // Pour stocker l'utilisateur à modifier

    private final ServiceParrainage serviceParrainage = new ServiceParrainage();
    public void initialize() {
        afficherParrainages();
    }

    private void afficherParrainages() {
        List<Parrainage> p = serviceParrainage.afficher();
        ObservableList<Parrainage> ParrainageList = FXCollections.observableArrayList(p);

        // Associer les colonnes aux attributs de l'objet User
        IdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        code.setCellValueFactory(new PropertyValueFactory<>("id_sender"));
       id_sender.setCellValueFactory(new PropertyValueFactory<>("code"));



        // Mettre la liste des utilisateurs dans la TableView
        tableViewParrainage.setItems(ParrainageList);

        // Rafraîchir la table au démarrage
        refreshTable();
    }
    private void refreshTable() {
        // Récupérer les données de la base de données
        List<Parrainage> parrainages = serviceParrainage.afficher();

        // Convertir les données en ObservableList
        ObservableList<Parrainage> userList = FXCollections.observableArrayList(parrainages);

        // Mettre à jour la TableView
        tableViewParrainage.setItems(userList);
    }
    @FXML
    private void handleSupprimer() {
        Parrainage selectedParrainage = tableViewParrainage.getSelectionModel().getSelectedItem();

        if (selectedParrainage == null) {
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
        confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer cet parrainage ?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Supprimer l'utilisateur
            serviceParrainage.supprimer(selectedParrainage.getId());
            tableViewParrainage.getItems().remove(selectedParrainage);

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Suppression réussie");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Le parrainage a été supprimé avec succès.");
            successAlert.showAndWait();
        }
    }
    @FXML
    private void naviguerVersAjouter() {
        // Naviguer vers l'interface AddUserByAdmin
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddParrainageByAdmin.fxml"));
            Parent root = loader.load();

            // Afficher la nouvelle interface
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("ajouter un parrainage");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        ((Stage) naviguer_versAjouter.getScene().getWindow()).close();
    }
    @FXML
    private void handleModifier() {
        // Récupérer l'utilisateur sélectionné
        Parrainage selectedParrainage = tableViewParrainage.getSelectionModel().getSelectedItem();

        if (selectedParrainage == null) {
            // Afficher un message d'erreur si aucune ligne n'est sélectionnée
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un Parrainage à modifier.");
            alert.showAndWait();
            return;
        }

        // Naviguer vers l'interface AddUserByAdmin
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddParrainageByAdmin.fxml"));
            Parent root = loader.load();

            // Passer les données de l'utilisateur sélectionné au contrôleur AddUserByAdmin
            AddParrainageByAdminController controller = loader.getController();
            controller.setParrainageData(selectedParrainage);

            // Afficher la nouvelle interface
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter le parrainage");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        ((Stage) Updatebtn.getScene().getWindow()).close();
    }



    @FXML
    private void GoBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard.fxml"));
            Parent root = loader.load();

            // Afficher la nouvelle interface
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestion des utilisateurs");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        ((Stage) retourner.getScene().getWindow()).close();
    }
}
