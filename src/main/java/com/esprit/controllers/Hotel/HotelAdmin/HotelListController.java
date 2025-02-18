package com.esprit.controllers.Hotel.HotelAdmin;

import com.esprit.models.Hotel;
import com.esprit.services.Hotel.serviceHotel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class HotelListController {
    @FXML private GridPane hotelGrid;

    private final serviceHotel hotelService = new serviceHotel();



    @FXML private Slider priceSlider;
    @FXML private Label priceLabel;


    @FXML
    private TextField searchField;

    private List<Hotel> allHotels;

    @FXML
    private AnchorPane rootPane;
    @FXML
    public void initialize() {
        loadHotels();
        priceSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            priceLabel.setText(String.format("%.0f TND", newValue));
        });
    }

    private void loadHotels() {
        allHotels = hotelService.getAllHotels(); // Fetch all hotels from the database
        displayHotels(allHotels);
    }

    private void displayHotels(List<Hotel> hotels) {
        hotelGrid.getChildren().clear(); // Clear existing hotels

        for (int i = 0; i < hotels.size(); i++) {
            Hotel hotel = hotels.get(i);
            Pane hotelCard = createHotelCard(hotel); // Assume you have a method to create hotel cards
            hotelGrid.add(hotelCard, i % 3, i / 3); // Arrange in a grid
        }
    }

    @FXML
    private void filterHotels() {
        String searchText = searchField.getText().toLowerCase();

        List<Hotel> filteredHotels = allHotels.stream()
                .filter(hotel -> hotel.getName().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        displayHotels(filteredHotels);
    }

    @FXML
    private void filterHotelsByPrice() {
        double maxPrice = priceSlider.getValue();

        List<Hotel> filteredHotels = allHotels.stream()
                .filter(hotel -> hotel.getBasePrice() <= maxPrice)
                .collect(Collectors.toList());

        displayHotels(filteredHotels);
    }


    @FXML
    private void navigateToAddHotel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionHotel/add-hotel.fxml"));
            Parent ajouterHotelView = loader.load();
            rootPane.getChildren().setAll(ajouterHotelView);
        } catch (IOException e) {
            System.out.println("Error loading Add Hotel page: " + e.getMessage());
        }
    }

    private VBox createHotelCard(Hotel hotel) {
        VBox card = new VBox();
        card.setStyle("-fx-border-color: #ddd; -fx-padding: 10px; -fx-background-color: white; -fx-spacing: 5; -fx-alignment: center; -fx-border-radius: 10; -fx-background-radius: 10;");

        ImageView imageView = new ImageView();
        if (!hotel.getImages().isEmpty()) {
            String imageUrl = hotel.getImages().get(0);
            imageView.setImage(loadImage(imageUrl));
        } else {
            imageView.setImage(loadImage("file:default_hotel.jpg")); // Placeholder image
        }
        imageView.setFitWidth(220);
        imageView.setFitHeight(190);
        imageView.setPreserveRatio(true);

        Label nameLabel = new Label(hotel.getName());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label locationLabel = new Label("📍 " + hotel.getLocation());
        Label priceLabel = new Label("💰 " + hotel.getBasePrice() + " TND");

        card.getChildren().addAll(imageView, nameLabel, locationLabel, priceLabel);

        card.setOnMouseClicked(event -> showHotelDetails(hotel));

        return card;
    }

    // Helper method to load image with proper URL handling
    private Image loadImage(String imageUrl) {
        try {
            // Check if the URL is a valid file path or web URL
            if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
                return new Image(imageUrl); // Handle web URLs
            } else {
                File file = new File(imageUrl);
                if (file.exists()) {
                    return new Image(file.toURI().toString()); // Use File's toURI() method
                } else {
                    return new Image("file:default_hotel.jpg"); // Fallback to placeholder if file not found
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
            return new Image("file:default_hotel.jpg"); // Fallback to placeholder if error occurs
        }
    }

    private void showHotelDetails(Hotel selectedHotel) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionHotel/hotel-details.fxml"));
            Parent root = loader.load();

            HotelDetailsController controller = loader.getController();
            controller.setHotel(selectedHotel); // Pass the selected hotel to the details controller

            rootPane.getChildren().setAll(root);
        } catch (IOException e) {
            System.out.println("Error loading hotel details: " + e.getMessage());
        }
    }

    public void handleGestionReservations(ActionEvent actionEvent) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionHotel/reservationList.fxml"));
                Parent reservationListView = loader.load();

                // Get the current stage and set the new scene
                Stage stage = (Stage) hotelGrid.getScene().getWindow();
                stage.setScene(new Scene(reservationListView));
                stage.setTitle("Reservation List");
                stage.show();
            } catch (IOException e) {
                System.out.println("Error Failed to load reservation list: " + e.getMessage());
            }
        }


    }

