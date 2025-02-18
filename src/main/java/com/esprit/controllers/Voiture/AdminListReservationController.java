package com.esprit.controllers.Voiture;

import com.esprit.models.Reservations;
import com.esprit.services.services_voiture.ReservationService;
import com.esprit.utils.DataBase;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminListReservationController {

    @FXML
    private TableView<Reservations> reservationsTable;
    @FXML
    private TableColumn<Reservations, Integer> colId;
    @FXML
    private TableColumn<Reservations, Integer> colVoitureId;
    @FXML
    private TableColumn<Reservations, String> colDateDebut;
    @FXML
    private TableColumn<Reservations, String> colDateFin;
    @FXML
    private TableColumn<Reservations, String> colStatut;
    @FXML
    private TableColumn<Reservations, Double> colPrixFinale;
    @FXML
    private TableColumn<Reservations, Void> colAnnuler;
    @FXML
    private TableColumn<Reservations, Void> colAccepter;

    private ReservationService reservationService;
    private Connection connection;

    public AdminListReservationController() {
        connection = DataBase.getInstance().getConnection();
    }

    @FXML
    public void initialize() {
        reservationService = new ReservationService();

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colVoitureId.setCellValueFactory(new PropertyValueFactory<>("voiture_id"));
        colDateDebut.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDate_debut().toString())
        );
        colDateFin.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDate_fin().toString())
        );
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colPrixFinale.setCellValueFactory(new PropertyValueFactory<>("prix_finale"));

        colAnnuler.setCellFactory(param -> new TableCell<>() {
            private final Button btnSupprimer = new Button("🗑 Annuler");

            {
                btnSupprimer.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 12px;");
                btnSupprimer.setOnAction(event -> {
                    Reservations reservation = getTableView().getItems().get(getIndex());
                    supprimerReservation(reservation);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnSupprimer);
                }
            }
        });

        colAccepter.setCellFactory(param -> new TableCell<>() {
            private final Button btnAccepter = new Button("✅ Accepter");
            private final Label lblConfirmee = new Label("✔ Confirmée");

            {
                btnAccepter.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-size: 12px;");
                btnAccepter.setOnAction(event -> {
                    Reservations reservation = getTableView().getItems().get(getIndex());

                    if (reservation.getStatut() != null && reservation.getStatut().trim().equalsIgnoreCase("En attente")) {
                        accepterReservation(reservation);
                    } else {
                        showAlert("Information", "Cette réservation est déjà confirmée.");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Reservations reservation = getTableView().getItems().get(getIndex());

                    if ("confirmée".equalsIgnoreCase(reservation.getStatut().trim())) {
                        setGraphic(lblConfirmee);
                    } else {
                        setGraphic(btnAccepter);
                    }
                }
            }
        });

        loadReservations(1);
    }

    private void loadReservations(int utilisateurId) {
        ObservableList<Reservations> reservationsList = reservationService.getReservationsByClient(utilisateurId);
        reservationsTable.setItems(reservationsList);
    }

    private void supprimerReservation(Reservations reservation) {
        try {
            String query = "DELETE FROM reservations WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, reservation.getId());
            int deletedRows = preparedStatement.executeUpdate();
            if (deletedRows > 0) {
                showAlert("Succès", "Réservation supprimée avec succès !");
                reservationsTable.getItems().remove(reservation);
            } else {
                showAlert("Erreur", "Échec de la suppression !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur SQL", "Une erreur est survenue lors de la suppression !");
        }
    }

    private void accepterReservation(Reservations reservation) {
        try {
            String query = "UPDATE reservations SET statut = 'confirmée' WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, reservation.getId());
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows > 0) {
                showAlert("Succès", "Réservation confirmée avec succès !");
                ObservableList<Reservations> updatedList = reservationService.getReservationsByClient(1);
                reservationsTable.setItems(updatedList);
                reservationsTable.refresh();
            } else {
                showAlert("Erreur", "Échec de la confirmation !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur SQL", "Une erreur est survenue lors de la mise à jour !");
        }
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
