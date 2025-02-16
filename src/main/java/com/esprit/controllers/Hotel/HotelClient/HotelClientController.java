package com.esprit.controllers.Hotel.HotelClient;

import com.esprit.models.Hotel;
import com.esprit.services.Hotel.serviceHotel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HotelClientController {

    @FXML
    private ListView<Hotel> hotelListView;
    @FXML
    private ComboBox<String> locationComboBox; // ComboBox for location filter
    @FXML
    private Slider priceSlider; // Slider for price filter
    @FXML
    private Label priceLabel; // Label to display selected price
    @FXML
    private Button reservationsButton; // Button for reservations

    private serviceHotel hotelService;

    public HotelClientController() {
        hotelService = new serviceHotel();
    }

    @FXML
    public void initialize() {
        List<Hotel> hotels = hotelService.getAllHotels();
        List<Hotel> availableHotels = new ArrayList<>();
        // Filter hotels to include only those with available rooms
        for (Hotel hotel : hotels) {
            try {
                if (hotelService.hasAvailableRooms(hotel.getId())) {
                    availableHotels.add(hotel);
                }
            } catch (SQLException e) {
                System.out.println();
            }
        }
        // Set the items in the ListView
        hotelListView.getItems().addAll(availableHotels);
        List<String> locations = hotelService.getDistinctLocations();
        locations.add(0,"All Locations"); // Add "All Locations" as the first option
        locationComboBox.setItems(FXCollections.observableArrayList(locations));
        locationComboBox.getSelectionModel().select(0);
        // Set the custom cell factory
        hotelListView.setCellFactory(new Callback<ListView<Hotel>, ListCell<Hotel>>() {
            @Override
            public ListCell<Hotel> call(ListView<Hotel> listView) {
                return new HotelListCell();
            }
        });


        // Set up price slider listener
        priceSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            priceLabel.setText("Max Price: " + String.format("%.2f TND", newValue.doubleValue()));
            filterHotels();
        });

        // Set up location combo box listener
        locationComboBox.setOnAction(event -> filterHotels());
    }

    private void filterHotels() {
        String selectedLocation = locationComboBox.getValue();
        double maxPrice = priceSlider.getValue();

        // Clear the current list
        hotelListView.getItems().clear();

        // Fetch all hotels and filter based on selected criteria
        List<Hotel> allHotels = hotelService.getAllHotels();
        for (Hotel hotel : allHotels) {
            boolean matchesLocation = selectedLocation.equals("All Locations") || hotel.getLocation().equals(selectedLocation);
            boolean matchesPrice = hotel.getBasePrice() <= maxPrice;

            // Check if the hotel has available rooms
            boolean hasAvailableRooms = false;
            try {
                hasAvailableRooms = hotelService.hasAvailableRooms(hotel.getId());
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }

            // Only add the hotel if it matches the filters and has available rooms
            if (matchesLocation && matchesPrice && hasAvailableRooms) {
                hotelListView.getItems().add(hotel);
            }
        }
    }

    @FXML
    private void handleReservationsButtonAction() {
        try {
            // Load the reservation list view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionHotel/reservation_list_client.fxml"));
            Parent reservationListView = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) reservationsButton.getScene().getWindow();
            stage.setScene(new Scene(reservationListView));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error appropriately
        }
    }


}