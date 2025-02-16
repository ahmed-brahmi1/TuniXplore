package com.esprit.controllers.Hotel.HotelClient;

import com.esprit.controllers.Hotel.RoomReservation.ReservationDialog;
import com.esprit.models.Hotel;
import com.esprit.models.Room;
import com.esprit.models.RoomReservation;
import com.esprit.services.Hotel.RoomReservationService;
import com.esprit.services.Hotel.RoomService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class HotelDetailsClientController {

    @FXML
    private Label hotelNameLabel;
    @FXML
    private Label hotelLocationLabel;
    @FXML
    private Label hotelRatingLabel;
    @FXML
    private VBox featuresBox;
    @FXML
    private ListView<Room> roomsListView;
    @FXML
    private ImageView mainImageView; // Main image view for the carousel
    @FXML
    private HBox thumbnailBox; // HBox for thumbnail images


    @FXML
    private Button backButton; // Back button// Next button

    private RoomService roomService = new RoomService();
    private Hotel selectedHotel;

    private final RoomReservationService reservationService = new RoomReservationService();

    public void initialize() {
        // Pass the reservation handler to the cell factory
        roomsListView.setCellFactory(param -> new RoomListCell(this::handleReservation));
    }
    private void handleReservation(Room room) {
        System.out.println("Room ID: " + room.getId()); // Debug statement
        String hotelName = selectedHotel.getName();
        ReservationDialog dialog = new ReservationDialog(room,hotelName);
        Optional<RoomReservation> result = dialog.showAndWait();

        result.ifPresent(reservation -> {
            try {
                reservationService.saveReservation(reservation);
                // Update room availability to false
                roomService.updateRoomAvailability(room.getId(), false);
                showSuccessAlert("Reservation successful for " + reservation.getGuestName() + "!");

                // Refresh the room list to exclude the reserved room
                populateHotelDetails(); // Call this method to refresh the room list
            } catch (SQLException e) {
                showErrorAlert("Reservation failed: " + e.getMessage());
            }
        });
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setHotel(Hotel hotel) {
        this.selectedHotel = hotel;
        populateHotelDetails();
    }


    private void populateHotelDetails() {
        System.out.println("Selected Hotel: " + selectedHotel);

        hotelNameLabel.setText(selectedHotel.getName());
        hotelLocationLabel.setText(selectedHotel.getLocation());
        hotelRatingLabel.setText(String.format("★%.1f", selectedHotel.getRating()));

        // Populate features
        List<String> features = selectedHotel.getFeatures();
        featuresBox.getChildren().clear();
        for (String feature : features) {
            HBox featureHBox = new HBox(5);
            FontAwesomeIcon checkIcon = new FontAwesomeIcon();
            checkIcon.setGlyphName("CHECK");
            checkIcon.setFill(Color.GREEN);
            checkIcon.setSize("1.2em");
            Label featureLabel = new Label(feature);
            featureLabel.setStyle("-fx-font-size: 12; -fx-text-fill: green;");
            featureHBox.getChildren().addAll(checkIcon, featureLabel);
            featuresBox.getChildren().add(featureHBox);
        }

        // Populate rooms
        List<Room> rooms = null;
        try {
            rooms = roomService.getRoomsByHotel(selectedHotel.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        roomsListView.getItems().clear();
        roomsListView.setCellFactory(param -> new RoomListCell(this::handleReservation)); // Set custom cell factory
        roomsListView.getItems().addAll(rooms);


        List<String> imageUrls = selectedHotel.getImages();
        if (!imageUrls.isEmpty()) {
            mainImageView.setImage(new Image(formatImageUrl(imageUrls.get(0))));
            for (String imageUrl : imageUrls) {
                ImageView thumbnail = new ImageView();
                thumbnail.setFitHeight(90);
                thumbnail.setFitWidth(120);
                thumbnail.setImage(new Image(formatImageUrl(imageUrl)));
                thumbnail.setOnMouseClicked(event -> {
                    mainImageView.setImage(new Image(formatImageUrl(imageUrl)));
                });
                thumbnailBox.getChildren().add(thumbnail);
            }
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

    private String formatImageUrl(String imageUrl) {
        String formattedUrl = imageUrl.replace("\\", "/");
        if (!formattedUrl.startsWith("file:/")) {
            formattedUrl = "file:///" + formattedUrl;
        }
        return formattedUrl;
    }
}