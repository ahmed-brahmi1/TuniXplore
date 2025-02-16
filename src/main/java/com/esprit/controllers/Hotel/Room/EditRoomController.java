package com.esprit.controllers.Hotel.Room;

import com.esprit.models.Room;
import com.esprit.services.Hotel.RoomService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditRoomController {

    @FXML
    private Label roomNumberLabel;
    @FXML
    private Label locationLabel;
    @FXML
    private TextField priceTextField; // Change to TextField for price
    @FXML
    private ComboBox<String> roomTypeComboBox;
    @FXML
    private ComboBox<String> availabilityComboBox;
    @FXML
    private CheckBox wifiCheckBox;
    @FXML
    private CheckBox acCheckBox;
    @FXML
    private CheckBox tvCheckBox;
    @FXML
    private CheckBox breakfastCheckBox;

    private Room currentRoom;
    private RoomService roomService;

    public EditRoomController() {
        roomService = new RoomService(); // Initialize RoomService
    }

    public void setRoom(Room room) {
        this.currentRoom = room;
        loadRoomData();
        populateRoomTypes(); // Populate room types when setting the room
    }

    private void loadRoomData() {
        if (currentRoom != null) {
            roomNumberLabel.setText(String.valueOf(currentRoom.getRoomNumber()));
            locationLabel.setText(currentRoom.getLocation());
            priceTextField.setText(String.valueOf(currentRoom.getBasePrice())); // Set price in TextField
            roomTypeComboBox.setValue(currentRoom.getRoomType());
            availabilityComboBox.setValue(currentRoom.isAvailable() ? "Available" : "Not Available");

            // Set amenities checkboxes based on current room amenities
            wifiCheckBox.setSelected(currentRoom.getAmenities().contains("Wi-Fi"));
            acCheckBox.setSelected(currentRoom.getAmenities().contains("Air Conditioning"));
            tvCheckBox.setSelected(currentRoom.getAmenities().contains("TV"));
            breakfastCheckBox.setSelected(currentRoom.getAmenities().contains("Breakfast"));
        }
    }

    private void populateRoomTypes() {
        // Populate the room type ComboBox with available room types
        roomTypeComboBox.getItems().addAll("Single", "Double", "Suite", "Deluxe"); // Add your room types here
    }

    @FXML
    private void handleSave(ActionEvent event) {
        try {
            // Update the current room with new values
            currentRoom.setRoomType(roomTypeComboBox.getValue());
            currentRoom.setAvailable(availabilityComboBox.getValue().equals("Available"));
            currentRoom.setBasePrice(Double.parseDouble(priceTextField.getText())); // Update the price from TextField
            currentRoom.setAmenities(getSelectedAmenities());

            // Call the service to update the room
            roomService.updateRoom(currentRoom.getHotelId(), currentRoom.getRoomNumber(), currentRoom);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Room details updated successfully!");
            navigateToRoomDetails(); // Navigate back to room details after saving
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid price format: " + e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update room: " + e.getMessage());
        }
    }

    private void navigateToRoomDetails() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionHotel/room-details.fxml"));
            Parent root = loader.load();

            // Get the controller for RoomDetailsController
            RoomDetailsController roomDetailsController = loader.getController();
            roomDetailsController.setRoom(currentRoom); // Pass the updated room details

            // Get the current stage and set the new scene
            Stage stage = (Stage) roomNumberLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to navigate to room details: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        // Logic to navigate back to the previous screen
        // This can be similar to the handleBack method in RoomDetailsController
    }

    private List<String> getSelectedAmenities() {
        List<String> amenities = new ArrayList<>();
        if (wifiCheckBox.isSelected()) amenities.add("Wi-Fi");
        if (acCheckBox.isSelected()) amenities.add("Air Conditioning");
        if (tvCheckBox.isSelected()) amenities.add("TV");
        if (breakfastCheckBox.isSelected()) amenities.add("Breakfast");
        return amenities;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}