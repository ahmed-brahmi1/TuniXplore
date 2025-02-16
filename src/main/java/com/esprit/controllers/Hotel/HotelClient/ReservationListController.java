package com.esprit.controllers.Hotel.HotelClient;

import com.esprit.models.RoomReservation;
import com.esprit.services.Hotel.RoomReservationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class ReservationListController {

    @FXML
    private FlowPane reservationListView;
    @FXML
    private Button backButton;

    private RoomReservationService reservationService;
    @FXML
    private Label noReservationsLabel;

    public ReservationListController() {
        reservationService = new RoomReservationService();
    }

    @FXML
    public void initialize() {
        loadReservations();

    }

    private void loadReservations() {
        try {
            List<RoomReservation> reservations = reservationService.getAllReservations(); // Implement this method in your service
            reservationListView.getChildren().clear(); // Clear previous items

            if (reservations.isEmpty()) {
                noReservationsLabel.setVisible(true); // Show the no reservations label
            } else {
                noReservationsLabel.setVisible(false); // Hide the no reservations label
                for (RoomReservation reservation : reservations) {
                    ReservationListCell cell = new ReservationListCell();
                    cell.updateItem(reservation, false); // Update the cell with reservation data
                    reservationListView.getChildren().add(cell); // Add the cell to the FlowPane
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the error appropriately
        }
    }



    @FXML
    private void handleBackButtonAction() {
        try {
            // Load the hotel list view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionHotel/HotelClient.fxml"));
            Parent hotelListView = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(hotelListView));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
