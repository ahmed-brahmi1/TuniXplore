package com.esprit.controllers.Hotel.HotelAdmin;

import com.esprit.models.Hotel;
import com.esprit.services.Hotel.serviceHotel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class EditHotelController {
    @FXML private TextField nameField;
    @FXML private TextField locationField;
    @FXML private TextField ratingField;
    @FXML private TextField priceField;
    @FXML private TextField imageUrlField;
    @FXML private ListView<String> imageListView;
    @FXML private AnchorPane rootPane;

    // New CheckBox references
    @FXML private CheckBox parkingCheck;
    @FXML private CheckBox poolCheck;
    @FXML private CheckBox wifiCheck;
    @FXML private CheckBox restaurantCheck;
    @FXML private CheckBox spaCheck;
    @FXML private CheckBox gymCheck;
    @FXML private CheckBox barCheck;
    @FXML private CheckBox roomServiceCheck;
    @FXML private CheckBox petsCheck;
    @FXML private CheckBox beachCheck;

    @FXML private Button selectImageButton;

    @FXML private ImageView selectedImageView;

    private File selectedFile;
    private Hotel hotel;
    private serviceHotel hotelService = new serviceHotel();
    private ObservableList<String> imageList = FXCollections.observableArrayList();

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
        nameField.setText(hotel.getName());
        locationField.setText(hotel.getLocation());
        ratingField.setText(String.valueOf(hotel.getRating()));
        priceField.setText(String.valueOf(hotel.getBasePrice()));

        // Load existing features into checkboxes
        List<String> features = hotelService.getHotelFeatures(hotel.getId());
        parkingCheck.setSelected(features.contains("Free Parking"));
        poolCheck.setSelected(features.contains("Swimming Pool"));
        wifiCheck.setSelected(features.contains("Free WiFi"));
        restaurantCheck.setSelected(features.contains("Restaurant"));
        spaCheck.setSelected(features.contains("Spa Services"));
        gymCheck.setSelected(features.contains("Fitness Center"));
        barCheck.setSelected(features.contains("Bar/Lounge"));
        roomServiceCheck.setSelected(features.contains("Room Service"));
        petsCheck.setSelected(features.contains("Pet Friendly"));
        beachCheck.setSelected(features.contains("Beach Access"));

        // Load existing images
        imageList.setAll(hotelService.getHotelImages(hotel.getId()));
        imageListView.setItems(imageList);
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (hotel == null) {
            System.out.println("Error: No hotel to update.");
            return;
        }

        String newName = nameField.getText();
        String newLocation = locationField.getText();
        float newRating, newPrice;

        try {
            newRating = Float.parseFloat(ratingField.getText());
            newPrice = Float.parseFloat(priceField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Invalid rating or price input.");
            return;
        }

        // Update hotel object
        hotel.setName(newName);
        hotel.setLocation(newLocation);
        hotel.setRating(newRating);
        hotel.setBasePrice(newPrice);

        // Save changes to database
        hotelService.editHotel(hotel);

        // Update features in DB
        List<String> selectedFeatures = getSelectedFeatures();
        hotelService.updateHotelFeatures(hotel.getId(), selectedFeatures);

        // Fetch existing images from the database
        List<String> imageUrls = hotelService.getHotelImages(hotel.getId()); // Get existing images from DB

        // Append the newly selected image (if any)
        if (selectedFile != null) {
            String imageUrl = "C:/hotel_images/" + selectedFile.getName(); // Save relative path
            imageUrls.add(imageUrl); // Add new image to the list
        }

        hotelService.updateHotelImages(hotel.getId(), imageUrls);



        // Navigate back
        navigateToHotelDetails();
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        navigateToHotelDetails();
    }

    private void navigateToHotelDetails() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionHotel/hotel-details.fxml"));
            Parent hotelDetailsView = loader.load();

            HotelDetailsController controller = loader.getController();
            controller.setHotel(hotel);

            rootPane.getChildren().setAll(hotelDetailsView);
            // Get the current stage (window)
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setWidth(1000);  // Set desired width
            stage.setHeight(800); // Set desired height

        } catch (IOException e) {
            System.out.println("Error navigating back: " + e.getMessage());
        }
    }

    @FXML
    private void handleSelectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image de l'hôtel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers Images", "*.png", "*.jpg", "*.jpeg"));

        selectedFile = fileChooser.showOpenDialog(selectImageButton.getScene().getWindow());

        if (selectedFile != null) {
            // Get the file name and create a destination path
            String destinationPath = "C:/hotel_images/" + selectedFile.getName();
            File destFile = new File(destinationPath);

            // Move the selected file to the destination folder
            try {
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Image image = new Image(destFile.toURI().toString()); // Show the image in UI
                selectedImageView.setImage(image);
            } catch (IOException e) {
                System.out.println("Error moving image file: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleRemoveImage(ActionEvent event) {
        String selectedImage = imageListView.getSelectionModel().getSelectedItem();
        if (selectedImage != null) {
            imageList.remove(selectedImage);
        }
    }

    // Helper method to get selected features from checkboxes
    private List<String> getSelectedFeatures() {
        List<String> selectedFeatures = new ArrayList<>();
        if (parkingCheck.isSelected()) selectedFeatures.add("Free Parking");
        if (poolCheck.isSelected()) selectedFeatures.add("Swimming Pool");
        if (wifiCheck.isSelected()) selectedFeatures.add("Free WiFi");
        if (restaurantCheck.isSelected()) selectedFeatures.add("Restaurant");
        if (spaCheck.isSelected()) selectedFeatures.add("Spa Services");
        if (gymCheck.isSelected()) selectedFeatures.add("Fitness Center");
        if (barCheck.isSelected()) selectedFeatures.add("Bar/Lounge");
        if (roomServiceCheck.isSelected()) selectedFeatures.add("Room Service");
        if (petsCheck.isSelected()) selectedFeatures.add("Pet Friendly");
        if (beachCheck.isSelected()) selectedFeatures.add("Beach Access");
        return selectedFeatures;
    }
}