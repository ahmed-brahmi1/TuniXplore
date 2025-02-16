package com.esprit.controllers.Hotel.Room;

import com.esprit.models.Room;
import com.esprit.services.Hotel.RoomService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class RoomDetailsController {

    @FXML
    private Label amenitiesLabel;
    @FXML
    private Label availabilityLabel;
    @FXML
    private Label locationLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private ImageView roomImageView;
    @FXML
    private Label roomNumberLabel;
    @FXML
    private Label roomTypeLabel;
    @FXML
    private Button backButton;
    @FXML
    private Button editButton; // Add the edit button
    @FXML
    private Button deleteButton; // Add the delete button

    private Room currentRoom;
    private RoomService roomService; // Declare RoomService

    public RoomDetailsController() {
        roomService = new RoomService(); // Initialize RoomService
    }

    public void setRoom(Room room) {
        this.currentRoom = room;
        if (currentRoom == null) {
            System.out.println("Current room is null!");
        } else {
            System.out.println("Current room location: " + currentRoom.getLocation());
        }
        updateUI();
    }

    private void updateUI() {
        if (currentRoom != null) {
            locationLabel.setText("Location: " + currentRoom.getLocation());
            priceLabel.setText("Price: $" + String.format("%.2f", currentRoom.getBasePrice()));
            roomTypeLabel.setText("Type: " + currentRoom.getRoomType());
            roomNumberLabel.setText("Number: " + currentRoom.getRoomNumber());
            availabilityLabel.setText("Availability: " + (currentRoom.isAvailable() ? "Available" : "Not Available"));
            amenitiesLabel.setText("Amenities: " + String.join(", ", currentRoom.getAmenities()));

            loadRoomImage(currentRoom.getImageUrl());
        }
    }

    private void loadRoomImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            try {
                File imageFile = new File(imageUrl);
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toURL().toString());
                    roomImageView.setImage(image);
                    roomImageView.setFitHeight(400);
                    roomImageView.setFitWidth(700);
                    roomImageView.setPreserveRatio(true);
                } else {
                    System.out.println("Image file not found: " + imageUrl);
                }
            } catch (MalformedURLException e) {
                System.err.println("Invalid image URL: " + e.getMessage());
            }
        } else {
            System.out.println("No image URL provided.");
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        System.out.println("Back button clicked");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionHotel/room-list.fxml"));
            Parent root = loader.load();

            // Get the RoomListController instance and pass the hotel ID
            RoomListController controller = loader.getController();
            controller.setHotelId(currentRoom.getHotelId()); // Pass the hotel ID to the RoomListController

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 800));
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load room list: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEdit(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/esprit/demo1/room-edit.fxml"));
            Parent editRoomView = loader.load();

            EditRoomController controller = loader.getController();
            controller.setRoom(currentRoom); // Pass the current room to the EditRoomController

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(editRoomView));
            stage.setTitle("Edit Room");
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load edit room page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDelete.setTitle("Delete Room");
        confirmDelete.setHeaderText("Are you sure you want to delete this room?");
        confirmDelete.setContentText("This action cannot be undone.");

        if (confirmDelete.showAndWait().get().getButtonData().isDefaultButton()) {
            // Call the service to delete the room
            roomService.deleteRoom(currentRoom.getHotelId(),currentRoom.getRoomNumber()); // Assuming you have a method to delete a room by ID

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Room deleted successfully!");

            // Navigate back to the room list
            handleBack(event); // Reuse the handleBack method to navigate back
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}