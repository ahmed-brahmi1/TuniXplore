package com.esprit.controllers.Hotel.Room;

import com.esprit.models.Room;
import com.esprit.services.Hotel.RoomService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomListController {

    @FXML private ComboBox<String> hotelFilterComboBox;
    @FXML private GridPane roomCardsPane;
    @FXML private AnchorPane rootPane;

    private RoomService roomService;
    private List<Room> rooms = new ArrayList<>();

    @FXML
    private ListView<String> roomListView;

    private static int hotelId = 0; // Static variable to persist hotel selection

    public RoomListController() {
        roomService = new RoomService();
    }

    public void setHotelId(int id) {
        hotelId = id;
        loadRooms(); // Reload rooms when hotel is set
    }

    @FXML
    public void initialize() {
        if (hotelId > 0) {
            loadRoomsForHotel();
        } else {
            System.out.println("No hotel ID provided, loading all rooms.");
            loadAllRooms();
        }
    }

    private void loadRooms() {
        roomCardsPane.getChildren().clear();
        try {
            List<Room> rooms = (hotelId > 0) ? roomService.getRoomsByHotel(hotelId) : roomService.getAllRooms();
            updateRoomCards(rooms);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load rooms: " + e.getMessage());
        }
    }

    private void loadAllRooms() {
        try {
            List<Room> rooms = roomService.getAllRooms();
            updateRoomCards(rooms);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load rooms: " + e.getMessage());
        }
    }

    private void loadRoomsForHotel() {
        try {
            List<Room> rooms = roomService.getRoomsByHotel(hotelId);
            updateRoomCards(rooms);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load rooms: " + e.getMessage());
        }
    }

    private void updateRoomCards(List<Room> rooms) {
        roomCardsPane.getChildren().clear();
        for (int i = 0; i < rooms.size(); i++) {
            VBox card = createRoomCard(rooms.get(i));
            roomCardsPane.add(card, i % 3, i / 3); // Grid positioning
        }
    }

    private VBox createRoomCard(Room room) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 15;");

        ImageView imageView = new ImageView();
        if (room.getImageUrl() != null && !room.getImageUrl().isEmpty()) {
            try {
                Image image = new Image(new File(room.getImageUrl()).toURI().toString());
                imageView.setImage(image);
                imageView.setFitHeight(150);
                imageView.setFitWidth(200);
                imageView.setPreserveRatio(true);
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Image Error", "Failed to load image: " + e.getMessage());
            }
        }

        Label locationLabel = new Label("Location: " + room.getLocation());
        Label priceLabel = new Label("Price: $" + room.getBasePrice());
        Label typeLabel = new Label("Type: " + room.getRoomType());
        Label availabilityLabel = new Label(room.isAvailable() ? "Available" : "Not Available");

        card.getChildren().addAll(imageView, locationLabel, priceLabel, typeLabel, availabilityLabel);
        card.setOnMouseClicked(event -> roomCardClicked(room));
        return card;
    }

    @FXML
    private void roomCardClicked(Room room) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionHotel/room-details.fxml"));
            Parent roomDetailView = loader.load();

            RoomDetailsController controller = loader.getController();
            controller.setRoom(room);

            Stage stage = (Stage) roomCardsPane.getScene().getWindow();
            stage.setScene(new Scene(roomDetailView));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayRooms() {
        roomListView.getItems().clear();
        for (Room room : rooms) {
            roomListView.getItems().add(room.getRoomNumber() + " - " + room.getRoomType());
        }
    }

    @FXML
    public void addRoom() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/esprit/demo1/room-add.fxml"));
            Parent root = fxmlLoader.load();
            RoomAddController controller = fxmlLoader.getController();
            controller.setHotelId(hotelId);
            rootPane.getChildren().setAll(root);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot open the room addition page: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void setFilteredRooms(List<Room> filteredRooms) {
        this.rooms = filteredRooms;
        displayRooms();
    }
}
