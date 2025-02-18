package controllers;



import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import services.ServiceVol;  // Assuming ServiceVol fetches the flight data
import Models.Vol;          // Assuming Vol is the flight model

import java.io.IOException;
import java.util.List;

public class ListeVolController {   // Changed the class name from LiisteVolController to ListeVolController
    @FXML private TableView<Vol> flightTable;
    @FXML private TableColumn<Vol, Integer> flightIdColumn;
    @FXML private TableColumn<Vol, String> departureColumn;
    @FXML private TableColumn<Vol, String> destinationColumn;
    @FXML private TableColumn<Vol, String> departureDateColumn;
    @FXML private TableColumn<Vol, String> arrivalDateColumn;
    @FXML private TableColumn<Vol, Integer> capacityColumn;
    @FXML private TableColumn<Vol, Integer> availableSeatsColumn;
    @FXML private TableColumn<Vol, Double> priceColumn;
    @FXML private Button backButton;

    private ServiceVol serviceVol;  // Service to fetch the flight data

    // Constructor to initialize the service
    public ListeVolController() {  // Fixed the constructor to use the correct class name
        serviceVol = new ServiceVol();
    }

    // Initialize method called after FXML is loaded
    @FXML
    public void initialize() {
        // Initialize the columns in the TableView
        flightIdColumn.setCellValueFactory(new PropertyValueFactory<>("flightId"));
        departureColumn.setCellValueFactory(new PropertyValueFactory<>("departure"));
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
        departureDateColumn.setCellValueFactory(new PropertyValueFactory<>("departureDate"));
        arrivalDateColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalDate"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        availableSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Load the flight data into the table
        loadFlightData();
    }

    // Load the flight data from the ServiceVol class and populate the TableView
    private void loadFlightData() {
        List<Vol> flights = serviceVol.afficher();
        if (flights.isEmpty()) {
            System.out.println("No flights available to display.");
        }
        flightTable.getItems().setAll(flights);
    }


    // Handle the back button click, which will close this window
    @FXML
    private void goBack(ActionEvent event) {
        try {
            // Load the previous scene (e.g., vol.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard.fxml"));
            Parent root = loader.load();
            StackPane rootPane = (StackPane) flightTable.getScene().lookup("#contentArea");

            // Get the current stage (window)
            Stage stage = (Stage) backButton.getScene().getWindow(); // Fix: Ensure button is initialized
            stage.setScene(new Scene(root, 800, 500)); // Set new scene
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
