package com.esprit.controllers.Hotel.HotelClient;

import com.esprit.models.Hotel;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class HotelListCell extends ListCell<Hotel> {

    @Override
    protected void updateItem(Hotel hotel, boolean empty) {
        super.updateItem(hotel, empty);

        if (empty || hotel == null) {
            setText(null);
            setGraphic(null);
        } else {
            HBox card = createCard(hotel);
            setGraphic(card);
        }
    }

    private HBox createCard(Hotel hotel) {
        // Create an HBox for the card layout
        HBox card = new HBox();
        card.setSpacing(10);
        card.setStyle("-fx-padding: 10; -fx-border-color: #e0e0e0; -fx-border-radius: 10; -fx-background-color: #ffffff;");

        // Image
        ImageView imageView = new ImageView();
        imageView.setFitHeight(200);
        imageView.setFitWidth(270);
        imageView.setPreserveRatio(true);

        // Load the image
        if (hotel.getImages() != null && !hotel.getImages().isEmpty()) {
            String imageUrl = hotel.getImages().get(0);

            try {
                // Convert the file path to a valid URL
                String formattedUrl = imageUrl.replace("\\", "/"); // Replace backslashes with forward slashes
                if (!formattedUrl.startsWith("file:/")) {
                    formattedUrl = "file:///" + formattedUrl; // Add file protocol
                }
                imageView.setImage(new Image(formattedUrl, true));
            } catch (Exception e) {
                System.out.println("Error loading image: " + e.getMessage());
                // Optionally set a default image
                imageView.setImage(new Image("file:///path/to/default/image.jpg"));
            }
        } else {
            // Optionally set a default image
            imageView.setImage(new Image("file:///path/to/default/image.jpg"));
        }

        // Create a VBox for hotel details
        VBox details = new VBox(5);
        details.setStyle("-fx-padding: 10;");

        // Hotel Name
        Label nameLabel = new Label(hotel.getName());
        nameLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #1E90FF;");

        // Location
        Label locationLabel = new Label(hotel.getLocation());
        locationLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #666;");

        // Rating
        Label ratingLabel = new Label(String.format("★%.1f", hotel.getRating()));
        ratingLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #FFD700;");

        // Features
        VBox featuresBox = new VBox(5);
        featuresBox.setStyle("-fx-text-fill: green;");

        List<String> features = hotel.getFeatures();
        for (int i = 0; i < Math.min(features.size(), 3); i++) {
            String feature = features.get(i);
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

        // Create a VBox for price and button
        VBox priceAndButton = new VBox(5);
        priceAndButton.setStyle("-fx-alignment: center-right;"); // Align to the right

        // Price
        Label priceLabel = new Label(String.format("%.2f TND", hotel.getBasePrice()));
        priceLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        // Availability Button
        Button availabilityButton = new Button("See Availability");
        availabilityButton.setStyle("-fx-background-color: #1E90FF; -fx-text-fill: white; -fx-font-weight: bold;");
        availabilityButton.setPrefWidth(150);

        availabilityButton.setOnAction(event -> {
            // Get the current hotel
            Hotel currentHotel = getItem();
            if (currentHotel != null) {
                // Navigate to the hotel details view
                navigateToHotelDetails(currentHotel);
            }
        });

        // Add icons to the button (optional)
        FontAwesomeIcon icon = new FontAwesomeIcon();
        icon.setGlyphName("CHECK");
        icon.setFill(Color.WHITE);
        icon.setSize("1.3em");
        availabilityButton.setGraphic(icon);

        // Add price and button to the VBox
        priceAndButton.getChildren().addAll( availabilityButton,priceLabel);

        // Add all components to the details VBox
        details.getChildren().addAll(nameLabel, locationLabel, ratingLabel, featuresBox);

        // Add the image, details, and price/button VBox to the card HBox
        card.getChildren().addAll(imageView, details);

        // Add the priceAndButton VBox to the right side of the card
        HBox.setHgrow(priceAndButton, javafx.scene.layout.Priority.ALWAYS); // Allow it to grow
        card.getChildren().add(priceAndButton);

        return card;
    }

    private void navigateToHotelDetails(Hotel hotel) {
        try {
            // Load the hotel details FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionHotel/HotelDetailsClient.fxml"));
            Parent hotelDetailsView = loader.load();

            // Get the controller for the hotel details
            HotelDetailsClientController controller = loader.getController();
            controller.setHotel(hotel); // Pass the selected hotel to the controller

            // Get the current stage and set the new scene
            Stage stage = (Stage) getScene().getWindow();
            stage.setScene(new Scene(hotelDetailsView));
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading hotel details: " + e.getMessage());
        }
    }
}