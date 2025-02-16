package com.esprit.controllers.Hotel.HotelClient;

import com.esprit.models.RoomReservation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ReservationListCell extends ListCell<RoomReservation> {

    @Override
    protected void updateItem(RoomReservation reservation, boolean empty) {
        super.updateItem(reservation, empty);

        if (empty || reservation == null) {
            setText(null);
            setGraphic(null);
        } else {
            HBox card = createCard(reservation);
            setGraphic(card);
        }
    }

    private HBox createCard(RoomReservation reservation) {
        HBox card = new HBox();
        card.setSpacing(20);
        card.setMinWidth(200); // Set minimum width
        card.setPrefWidth(300); //
        card.setStyle("-fx-padding: 10; -fx-border-color: #e0e0e0; -fx-border-radius: 10; -fx-background-color: #ffffff;");

        // Create a VBox for reservation details
        VBox details = new VBox(5);
        details.setStyle("-fx-padding: 10;");

        // Guest Name
        Label guestNameLabel = new Label("Guest: " + reservation.getGuestName());
        guestNameLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        // Room Number
        Label roomNumberLabel = new Label("Room Number: " + reservation.getRoomNumber());
        roomNumberLabel.setStyle("-fx-font-size: 14;");

        // Room Type
        Label roomTypeLabel = new Label("Room Type: " + reservation.getRoomType());
        roomTypeLabel.setStyle("-fx-font-size: 14;");

        // Hotel Name
        Label hotelNameLabel = new Label("Hotel: " + reservation.getHotelName());
        hotelNameLabel.setStyle("-fx-font-size: 14;");

        // Check-in and Check-out Dates
        Label checkInLabel = new Label("Check-in: " + reservation.getCheckInDate());
        checkInLabel.setStyle("-fx-font-size: 14;");

        Label checkOutLabel = new Label("Check-out: " + reservation.getCheckOutDate());
        checkOutLabel.setStyle("-fx-font-size: 14;");

        // Total Price
        Label priceLabel = new Label(String.format("Total Price: %.2f TND", reservation.getTotalPrice()));
        priceLabel.setStyle("-fx-font-size: 14;");

        // Cancel Button
        Button cancelButton = new Button("Annuler");
        cancelButton.setStyle("-fx-background-color: #FF6347; -fx-text-fill: white; -fx-font-weight: bold;"); // Red color
        cancelButton.setOnAction(event -> {
            // Implement the action for the cancel button
            System.out.println("Cancel button clicked for reservation: " + reservation.getId());
            // You can add logic to handle cancellation here
        });

        // Add all components to the details VBox
        details.getChildren().addAll(guestNameLabel, hotelNameLabel, roomNumberLabel, roomTypeLabel, checkInLabel, checkOutLabel, priceLabel, cancelButton);

        // Add the details VBox to the card HBox
        card.getChildren().add(details);

        return card;
    }
}