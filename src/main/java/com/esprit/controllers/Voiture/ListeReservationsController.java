package com.esprit.controllers.Voiture;

import com.esprit.models.Reservations;
import com.esprit.services.services_voiture.ReservationService;
import com.esprit.utils.DataBase;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ListeReservationsController {
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

    private ReservationService reservationService;
    private Connection connection;

    public ListeReservationsController() {
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
            private final Label lblConfirme = new Label("✔ Confirmée");

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
                    Reservations reservation = getTableView().getItems().get(getIndex());
                    if ("confirmée".equals(reservation.getStatut().toLowerCase())) {
                        setGraphic(lblConfirme);
                    } else {
                        setGraphic(btnSupprimer);
                    }
                }
            }
        });

        // Charger les réservations
        loadReservations(1);

        // Double-clic pour afficher la facture
        reservationsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Reservations selectedReservation = reservationsTable.getSelectionModel().getSelectedItem();
                if (selectedReservation != null) {
                    showInvoice(selectedReservation);
                }
            }
        });
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

    private void showInvoice(Reservations reservation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ViewVoiture/voiture/EspaceClientVoiture/Invoice.fxml"));
            Parent root = loader.load();

            InvoiceController controller = loader.getController();
            if (controller != null) {
                controller.setReservationDetails(reservation);
            } else {
                System.out.println("❌ Erreur : Impossible de charger InvoiceController !");
                return;
            }

            Stage stage = new Stage();
            stage.setTitle("Facture de Réservation");
            stage.setScene(new Scene(root));
            stage.show();

            System.out.println("✅ Facture affichée pour la réservation ID : " + reservation.getId());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("❌ Erreur : Impossible d’ouvrir Invoice.fxml !");
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
