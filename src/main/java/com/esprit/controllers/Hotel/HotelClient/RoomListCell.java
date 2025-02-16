package com.esprit.controllers.Hotel.HotelClient;

import com.esprit.models.Room;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.function.Consumer;

public class RoomListCell extends ListCell<Room> {
    private final Consumer<Room> onReserveClicked;

    public RoomListCell(Consumer<Room> onReserveClicked) {
        this.onReserveClicked = onReserveClicked;
    }

    @Override
    protected void updateItem(Room room, boolean empty) {
        super.updateItem(room, empty);

        if (empty || room == null) {
            setText(null);
            setGraphic(null);
        } else {
            HBox card = createCard(room);
            setGraphic(card);
        }
    }

    private HBox createCard(Room room) {
        HBox card = new HBox();
        card.setSpacing(10);
        card.setStyle("-fx-padding: 10; -fx-border-color: #e0e0e0; -fx-border-radius: 10; -fx-background-color: #ffffff;");

        // Room Image
        ImageView imageView = new ImageView();
        imageView.setFitHeight(100);
        imageView.setFitWidth(150);
        imageView.setPreserveRatio(true);
        imageView.setImage(new Image(formatImageUrl(room.getImageUrl()))); // Format the image URL

        // Room Details
        VBox details = new VBox(5);
        Label roomNameLabel = new Label("Room Number: " + room.getRoomNumber());
        roomNameLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        Label roomTypeLabel = new Label("Type: " + room.getRoomType());
        roomTypeLabel.setStyle("-fx-font-size: 14;");

        Label priceLabel = new Label(String.format("Price: %.2f TND", room.getBasePrice()));
        priceLabel.setStyle("-fx-font-size: 14;");

        // Availability
        Label availabilityLabel = new Label(room.isAvailable() ? "Available" : "Not Available");
        availabilityLabel.setTextFill(room.isAvailable() ? Color.GREEN : Color.RED);
        availabilityLabel.setStyle("-fx-font-size: 14;");

        // Amenities
        Label amenitiesLabel = new Label("Amenities: " + String.join(", ", room.getAmenities()));
        amenitiesLabel.setStyle("-fx-font-size: 12;");

        details.getChildren().addAll(
                roomNameLabel,
                roomTypeLabel,
                priceLabel,
                availabilityLabel,
                amenitiesLabel

        );

        // Reserve Button
        Button reserveButton = new Button("Reserve");
        reserveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        VBox buttonContainer = new VBox(reserveButton);
        buttonContainer.setAlignment(Pos.TOP_RIGHT);
        buttonContainer.setSpacing(5);
        buttonContainer.setStyle("-fx-padding: 5 10 0 0;"); // Padding: top 5, right 10
        HBox.setHgrow(details, Priority.ALWAYS);

        reserveButton.setOnAction(e -> {
            if (onReserveClicked != null) {
                onReserveClicked.accept(room);
            }
        });


        card.getChildren().addAll(imageView, details, buttonContainer);

        return card;
    }

    private String formatImageUrl(String imageUrl) {
        String formattedUrl = imageUrl.replace("\\", "/"); // Replace backslashes with forward slashes
        if (!formattedUrl.startsWith("file:/")) {
            formattedUrl = "file:///" + formattedUrl; // Add file protocol
        }
        return formattedUrl;
    }
}