package com.esprit.controllers.Hotel.HotelAdmin;

import com.esprit.controllers.Hotel.Room.RoomListController;
import com.esprit.models.Hotel;
import com.esprit.services.Hotel.serviceHotel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class HotelDetailsController {
    @FXML private ImageView mainImageView;
    @FXML private HBox thumbnailBox;
    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Label nameLabel;
    @FXML private Label locationLabel;
    @FXML private Label priceLabel;
    @FXML private VBox featuresBox;

    private List<String> imagePaths = new ArrayList<>();
    private int currentImageIndex = 0;
    private serviceHotel hotelService = new serviceHotel();
    private Hotel currentHotel ;

    @FXML
    public void initialize() {
        // Initialize carousel controls
        prevButton.setOnAction(e -> showPreviousImage());
        nextButton.setOnAction(e -> showNextImage());
        System.out.println("HotelDetailsController initialized");
        System.out.println("featuresBox: " + featuresBox);
        // Style the buttons
        String buttonStyle = """
            -fx-background-color: rgba(0, 0, 0, 0.5);
            -fx-text-fill: white;
            -fx-cursor: hand;
            """;
        prevButton.setStyle(buttonStyle);
        nextButton.setStyle(buttonStyle);


    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            // Charger la page de la liste des hôtels
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionHotel/hotel-list.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle et la remplacer
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Hôtels");
            stage.setWidth(1100);  // Set desired width
            stage.setHeight(800);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Gérer l'erreur d'affichage de la nouvelle page
        }
    }

    public void setHotel(Hotel hotel) {
        this.currentHotel  = hotel;

        System.out.println(currentHotel);

        // Set hotel information
        nameLabel.setText(hotel.getName());
        locationLabel.setText("📍 " + hotel.getLocation());
        // Set hotel features
        setHotelFeatures(hotel.getFeatures());
        priceLabel.setText("💰 " + String.format("%.2f TND", hotel.getBasePrice()));

        // Load images
        imagePaths = hotelService.getHotelImages(hotel.getId());
        if (!imagePaths.isEmpty()) {
            setupImageCarousel();
        }
    }

    private void setHotelFeatures(List<String> features) {
        featuresBox.getChildren().clear(); // Clear old features

        for (String feature : features) {
            Label featureLabel = new Label("✔️" + feature);
            featureLabel.setStyle("-fx-font-size: 14; -fx-padding: 5;");
            featuresBox.getChildren().add(featureLabel);
        }
    }


    private void setupImageCarousel() {
        // Clear existing thumbnails
        thumbnailBox.getChildren().clear();

        // Setup main image
        showImage(0);

        // Create thumbnails
        for (int i = 0; i < imagePaths.size(); i++) {
            ImageView thumbnail = createThumbnail(imagePaths.get(i), i);
            thumbnailBox.getChildren().add(thumbnail);
        }

        // Update button visibility
        updateNavigationButtons();
    }

    private ImageView createThumbnail(String imagePath, int index) {
        ImageView thumbnail = new ImageView(loadImage(imagePath));
        thumbnail.setFitHeight(60);
        thumbnail.setFitWidth(90);
        thumbnail.setPreserveRatio(true);

        // Style thumbnail
        thumbnail.setStyle("""
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);
            -fx-cursor: hand;
            """);

        // Add click handler
        thumbnail.setOnMouseClicked(e -> {
            currentImageIndex = index;
            showImage(currentImageIndex);
            updateNavigationButtons();
        });

        return thumbnail;
    }

    private void showImage(int index) {
        if (index >= 0 && index < imagePaths.size()) {
            Image image = loadImage(imagePaths.get(index));
            mainImageView.setImage(image);
            currentImageIndex = index;

            // Highlight current thumbnail
            thumbnailBox.getChildren().forEach(node -> node.setStyle(node.getStyle().replace("-fx-border-color: #2196F3;", "")));
            if (index < thumbnailBox.getChildren().size()) {
                thumbnailBox.getChildren().get(index).setStyle(
                        thumbnailBox.getChildren().get(index).getStyle() + "-fx-border-color: #2196F3;"
                );
            }
        }
    }

    private void showNextImage() {
        if (currentImageIndex < imagePaths.size() - 1) {
            showImage(currentImageIndex + 1);
            updateNavigationButtons();
        }
    }

    private void showPreviousImage() {
        if (currentImageIndex > 0) {
            showImage(currentImageIndex - 1);
            updateNavigationButtons();
        }
    }

    private void updateNavigationButtons() {
        prevButton.setDisable(currentImageIndex == 0);
        nextButton.setDisable(currentImageIndex == imagePaths.size() - 1);
    }

    private Image loadImage(String imageUrl) {
        try {
            File file = new File(imageUrl);
            if (!file.exists()) {
                System.err.println("Image file does not exist: " + imageUrl);
                return new Image("file:default_hotel.jpg");
            }
            return new Image(file.toURI().toString());
        } catch (Exception e) {
            System.err.println("Failed to load image: " + imageUrl + " - " + e.getMessage());
            return new Image("file:default_hotel.jpg");
        }
    }

    @FXML
    private void handleDelete() {
        Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDelete.setTitle("Delete Hotel");
        confirmDelete.setHeaderText("Are you sure you want to delete this hotel?");
        confirmDelete.setContentText("This action cannot be undone.");

        if (confirmDelete.showAndWait().get().getButtonData().isDefaultButton()) {
            hotelService.deleteHotel(currentHotel.getId());

            // Navigate back to the hotel list
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionHotel/hotel-list.fxml"));
                Parent hotelListView = loader.load();

                // Get the stage from the current scene
                Stage stage = (Stage) nameLabel.getScene().getWindow();
                stage.setScene(new Scene(hotelListView));
                stage.setWidth(900);  // Set desired width
                stage.setHeight(800);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleEdit(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionHotel/edit-hotel.fxml"));
            Parent editView = loader.load();

            EditHotelController controller = loader.getController();
            controller.setHotel(currentHotel); // Ensure this method exists to pass hotel details

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(editView));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleListRooms(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionHotel/room-list.fxml"));
            Parent roomListView = loader.load();

            RoomListController controller = loader.getController();
            if (currentHotel != null && currentHotel.getId() > 0) {
                controller.setHotelId(currentHotel.getId()); // Pass the hotel ID to the RoomListController
            } else {
                System.out.println("No hotel selected, showing all rooms.");
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(roomListView));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}