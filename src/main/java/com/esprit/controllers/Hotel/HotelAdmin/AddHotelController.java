package com.esprit.controllers.Hotel.HotelAdmin;

import com.esprit.models.Hotel;
import com.esprit.services.Hotel.serviceHotel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddHotelController {
    @FXML private TextField nameField;
    @FXML private TextField locationField;
    @FXML private TextField priceField;
    @FXML private ComboBox<Double> ratingComboBox;
    @FXML private ListView<String> imageListView;
    @FXML
    private CheckBox parkingCheck, poolCheck, wifiCheck, restaurantCheck, spaCheck, gymCheck,
            barCheck, roomServiceCheck, petsCheck, beachCheck;


    private final ObservableList<String> imagePaths = FXCollections.observableArrayList();
    private List<String> selectedImages = new ArrayList<>();
    private final serviceHotel hotelService = new serviceHotel();

    @FXML
    public void initialize() {
        // Initialize rating combo box
        ratingComboBox.getItems().addAll(1.0, 2.0, 3.0, 4.0, 5.0);
        ratingComboBox.setValue(3.0);
    }

    // Method to choose multiple images
    @FXML
    private void chooseImages(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);

        if (selectedFiles != null) {
            for (File file : selectedFiles) {
                selectedImages.add(file.getAbsolutePath());
                imagePaths.add(file.getAbsolutePath());
            }
            imageListView.setItems(imagePaths);
        }
    }

    @FXML
    private void removeSelectedImage() {
        int selectedIndex = imageListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            selectedImages.remove(selectedIndex);
            imageListView.getItems().remove(selectedIndex);
        }
    }

    // Method to add a new hotel
    @FXML
    private void addHotel(ActionEvent event) {
        String name = nameField.getText();
        String location = locationField.getText();
        String priceText = priceField.getText();
        Double rating = ratingComboBox.getValue();  // Use the ComboBox value for rating
        List<String> features = getSelectedFeatures();


        // Validate Hotel Name
        if (name.isEmpty() || !name.matches("^[A-Za-zÀ-ÿ\\s]+$")) {
            showAlert("Erreur", "Veuillez entrer un nom valide (lettres uniquement).");
            return;
        }

        // Validate Location
        if (location.isEmpty() || !location.matches("^[A-Za-zÀ-ÿ\\s]+$")) {
            showAlert("Erreur", "Veuillez entrer une localisation valide (lettres uniquement).");
            return;
        }

        // Validate Price
        double price;
        try {
            price = Double.parseDouble(priceText);
            if (price <= 0) {
                showAlert("Erreur", "Le prix doit être un nombre positif.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le prix doit être un nombre valide.");
            return;
        }

        // Validate Rating
        if (rating == null) {
            showAlert("Erreur", "Veuillez sélectionner une note pour l'hôtel.");
            return;
        }

        // Validate Features
        if (features.isEmpty()) {
            showAlert("Erreur", "Veuillez sélectionner au moins une fonctionnalité.");
            return;
        }

        // Validate Images
        if (imagePaths.isEmpty()) {
            showAlert("Erreur", "Veuillez ajouter au moins une image de l'hôtel.");
            return;
        }



        Hotel hotel = new Hotel(name, location, rating.floatValue(), price, new ArrayList<>(imagePaths),features);
        int hotelId = hotelService.addHotel(hotel);

        if (hotelId > 0) {
            hotelService.addHotelImages(hotelId, new ArrayList<>(imagePaths));
            // Add features after the hotel is created
            boolean featuresAdded = hotelService.addHotelFeatures(hotelId, features);
            if (featuresAdded) {
                showAlert("Succès", "L'hôtel a été ajouté avec succès, y compris les fonctionnalités !");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionHotel/hotel-list.fxml"));


                Parent addView = null;
                try {
                    addView = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(addView));
                stage.show();
            } else {
                showAlert("Erreur", "Échec de l'ajout des fonctionnalités.");
            }

            clearFields();
        } else {
            showAlert("Erreur", "Échec de l'ajout de l'hôtel.");
        }
    }
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

    @FXML
    private void goBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/esprit/demo1/hotel-list.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    // Clear form fields
    private void clearFields() {
        nameField.clear();
        locationField.clear();
        priceField.clear();
        ratingComboBox.getSelectionModel().clearSelection(); // Clear selection in ComboBox

        imagePaths.clear();
        imageListView.getItems().clear();
    }

    // Show alert popup
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Close window
    @FXML
    private void closeWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/esprit/demo1/hotel-list.fxml"));
            Parent hotelListView = loader.load();

            // Get the current window and set the new scene
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(hotelListView));
            stage.show();
        } catch (IOException e) {

            showAlert("Erreur", "Impossible de charger la liste des hôtels.");
        }
    }
}
