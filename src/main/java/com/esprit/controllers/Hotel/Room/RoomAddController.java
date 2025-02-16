package com.esprit.controllers.Hotel.Room;

import com.esprit.models.Room;
import com.esprit.services.Hotel.serviceHotel;
import com.esprit.services.Hotel.RoomService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomAddController {

    @FXML private ComboBox<String> locationTypeComboBox; // New ComboBox for location type
    @FXML private TextField locationValueTextField; // New TextField for location value
    @FXML private TextField basePriceTextField;
    @FXML private ComboBox<String> roomTypeComboBox;
    @FXML private TextField roomNumberTextField;
    @FXML private Label imagePathLabel; // New Label to display selected image path
    @FXML private CheckBox availableCheckBox;
    @FXML private CheckBox wifiCheckBox;
    @FXML private CheckBox acCheckBox;
    @FXML private CheckBox tvCheckBox;
    @FXML private CheckBox breakfastCheckBox;

    private RoomService roomService;
    private serviceHotel hotelService;
    private int hotelId;

    public RoomAddController() {
        roomService = new RoomService(); // assuming you have a RoomService class to handle DB operations
        hotelService = new serviceHotel(); // initialize the HotelService
    }

    @FXML
    public void initialize() {

        // Populate room types if necessary
        roomTypeComboBox.getItems().addAll("Single", "Double", "Suite", "Deluxe");
        // Initialize location type ComboBox
        locationTypeComboBox.setItems(FXCollections.observableArrayList("Floor", "Building"));


    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
        System.out.println(hotelId+".................");
    }


    @FXML
    public void addRoom() {
        try {
            // Validate inputs first
            if (!validateInputs()) return;

            // Create room object from inputs
            Room room = createRoomFromInputs();

            // Add to database
            roomService.addRoom(room);

            // Success feedback
            showAlert(Alert.AlertType.INFORMATION, "Success", "Room added successfully!");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionHotel/room-list.fxml"));
            Parent roomListView = loader.load();

            // Get the controller and set the hotel ID
            RoomListController controller = loader.getController();
            controller.setHotelId(hotelId); // Pass the hotel ID to the RoomListController

            // Get the current stage and set the new scene
            Stage stage = (Stage) ((Node) basePriceTextField).getScene().getWindow(); // Use any control to get the stage
            stage.setScene(new Scene(roomListView));
            stage.setTitle("Room List");
            stage.show();





            clearFields();

        } catch (SQLException e) {
            // Database error
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add room: " + e.getMessage());
        } catch (Exception e) {
            // Other errors
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
        }
    }

    // Helper method for input validation
    private boolean validateInputs() {
        if (basePriceTextField.getText() == null || basePriceTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a base price!");
            return false;
        }

        if (roomTypeComboBox.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Missing Data", "Please select a room type");
            return false;
        }
        if (locationValueTextField.getText() == null || locationValueTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a location value!");
            return false;
        }
        return true;
    }

    // Handle image browsing
    @FXML
    private void handleImageBrowse() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Room Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            imagePathLabel.setText(selectedFile.getAbsolutePath());
        }
    }

    // Get final location string
    private String getFullLocation() {
        return locationTypeComboBox.getValue() + " " + locationValueTextField.getText();
    }

    // Helper method to create Room object
    private Room createRoomFromInputs() throws NumberFormatException {
        return new Room(
                getFullLocation(), // Use the new method to get the full location
                Double.parseDouble(basePriceTextField.getText()),
                imagePathLabel.getText(), // Use the image path from the label
                hotelId,
                roomTypeComboBox.getValue(),
                Integer.parseInt(roomNumberTextField.getText()),
                availableCheckBox.isSelected(),
                getSelectedAmenities()
        );

    }

    // Helper method to get amenities
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

    @FXML
    public void cancel() {
        clearFields();
    }

    private void clearFields() {
        locationTypeComboBox.getSelectionModel().clearSelection();
        locationValueTextField.clear();
        basePriceTextField.clear();
        roomTypeComboBox.getSelectionModel().clearSelection();
        roomNumberTextField.clear();
        imagePathLabel.setText(""); // Clear the image path label
        availableCheckBox.setSelected(true);

    }
}