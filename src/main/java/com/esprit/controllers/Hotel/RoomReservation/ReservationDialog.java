package com.esprit.controllers.Hotel.RoomReservation;

import com.esprit.models.Room;
import com.esprit.models.RoomReservation;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.util.Optional;

public class ReservationDialog {
    private final Dialog<RoomReservation> dialog;
    private final DatePicker checkInDate;
    private final DatePicker checkOutDate;
    private final TextField guestName;
    private final Label priceLabel;
    private final Label roomDetailsLabel; // New label for room details

    public ReservationDialog(Room room, String hotelName) {
        dialog = new Dialog<>();
        dialog.setTitle("Room Reservation");

        // Create UI components
        checkInDate = new DatePicker();
        checkOutDate = new DatePicker();
        guestName = new TextField();
        priceLabel = new Label("Total Price: 0.00 TND"); // Initialize with default price
        roomDetailsLabel = new Label(); // Initialize room details label

        // Set minimum date to today
        LocalDate today = LocalDate.now();
        checkInDate.setValue(today);
        checkOutDate.setValue(today);
        checkInDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(today)); // Disable past dates
            }
        });
        checkOutDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(today)); // Disable past dates
            }
        });

        // Setup grid layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Check-in Date:"), checkInDate);
        grid.addRow(1, new Label("Check-out Date:"), checkOutDate);
        grid.addRow(2, new Label("Guest Name:"), guestName);
        grid.addRow(3, roomDetailsLabel); // Add room details label
        grid.addRow(4, priceLabel); // Add price label

        // Set room details
        roomDetailsLabel.setText("Room Number: " + room.getRoomNumber() + ", Type: " + room.getRoomType());

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Add listeners to date pickers for validation and price calculation
        checkInDate.valueProperty().addListener((obs, oldVal, newVal) -> updatePrice(room));
        checkOutDate.valueProperty().addListener((obs, oldVal, newVal) -> updatePrice(room));

        // Set result converter
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                if (validateForm()) {
                    return new RoomReservation(
                            room.getId(),
                            guestName.getText(),
                            checkInDate.getValue(),
                            checkOutDate.getValue(),
                            calculateTotalPrice(room, checkInDate.getValue(), checkOutDate.getValue()),
                            room.getRoomNumber(),
                            room.getRoomType(),
                            hotelName

                    );
                } else {
                    showErrorAlert("Please fill in all fields correctly.");
                    return null; // Prevent dialog from closing
                }
            }
            return null; // Close dialog on cancel
        });

        // Prevent dialog from closing on validation error
        dialog.getDialogPane().lookupButton(ButtonType.OK).addEventFilter(ActionEvent.ACTION, event -> {
            if (!validateForm()) {
                event.consume(); // Prevent the dialog from closing
                showErrorAlert("Please fill in all fields correctly.");
            }
        });
    }

    private void updatePrice(Room room) {
        LocalDate checkIn = checkInDate.getValue();
        LocalDate checkOut = checkOutDate.getValue();

        if (checkIn != null && checkOut != null) {
            priceLabel.setText(String.format("Total Price: %.2f TND", calculateTotalPrice(room, checkIn, checkOut)));
        } else {
            priceLabel.setText("Total Price: 0.00 TND"); // Reset price if dates are not selected
        }
    }

    private double calculateTotalPrice(Room room, LocalDate checkIn, LocalDate checkOut) {
        if (checkIn != null && checkOut != null) {
            long days = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
            return room.getBasePrice() * days;
        }
        return 0.0; // Return 0 if dates are not valid
    }

    private boolean validateForm() {
        LocalDate checkIn = checkInDate.getValue();
        LocalDate checkOut = checkOutDate.getValue();
        String guestNameInput = guestName.getText();

        // Check if all fields are filled correctly
        if (checkIn == null || checkOut == null || guestNameInput.isEmpty()) {
            return false;
        }

        // Check if check-in date is before check-out date and at least one day apart
        return !checkIn.isAfter(checkOut) && !checkIn.isEqual(checkOut);
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Optional<RoomReservation> showAndWait() {
        return dialog.showAndWait();
    }
}