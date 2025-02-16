package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import services.ServiceVol;
import Models.Vol;
import javafx.scene.control.Label;

import java.awt.event.ActionEvent;
import java.io.IOException;


public class AjouterVolsController {

    @FXML
    private TextField departureField;
    @FXML
    private TextField destinationField;
    @FXML
    private DatePicker departureDateField; // Corrected to DatePicker
    @FXML
    private DatePicker arrivalDateField;   // Corrected to DatePicker
    @FXML
    private TextField capacityField;
    @FXML
    private TextField availableSeatsField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField flightIdField;  // For flightId (update/delete)
    @FXML
    private TextField searchFlightIdField;  // Search field for flight ID
    @FXML
    private TextArea logArea;


    @FXML
    private Label errorLabelDeparture;
    @FXML
    private Label errorLabelDestination;
    @FXML
    private Label errorLabelDepartureDate;
    @FXML
    private Label errorLabelArrivalDate;
    @FXML
    private Label errorLabelCapacity;
    @FXML
    private Label errorLabelAvailableSeats;
    @FXML
    private Label errorLabelPrice;

    private ServiceVol serviceVol;

    public AjouterVolsController() {
        serviceVol = new ServiceVol(); // Initialize the service
    }

    // Add a new flight
    @FXML

    private void ajouterVol() {
        try {
            if (isInputValid()) {
                String departure = departureField.getText();
                String destination = destinationField.getText();
                String departureDate = departureDateField.getValue() != null ? departureDateField.getValue().toString() : "";
                String arrivalDate = arrivalDateField.getValue() != null ? arrivalDateField.getValue().toString() : "";
                int capacity = Integer.parseInt(capacityField.getText());
                int availableSeats = Integer.parseInt(availableSeatsField.getText());
                double price = Double.parseDouble(priceField.getText());

                Vol newVol = new Vol(0, departure, destination, departureDate, arrivalDate, capacity, availableSeats, price);
                serviceVol.ajouter(newVol);
                showAlert(AlertType.INFORMATION, "Success", "Flight added successfully!");
                resetFields();
            } else {
                showAlert(AlertType.ERROR, "Error", "Please fill all fields correctly.");
            }
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Error", "Invalid input, please ensure all fields are correctly filled.");
        }
    }


    // Update a flight
    @FXML
    private void modiferVol() {
        try {
            // Validate input fields
            if (isInputValid()) {
                // Get the data from the text fields
                String departure = departureField.getText();
                String destination = destinationField.getText();
                String departureDate = departureDateField.getValue().toString(); // Corrected for DatePicker
                String arrivalDate = arrivalDateField.getValue().toString();   // Corrected for DatePicker
                int capacity = Integer.parseInt(capacityField.getText());
                int availableSeats = Integer.parseInt(availableSeatsField.getText());
                double price = Double.parseDouble(priceField.getText());

                // Get the flightId from the field to update the specific flight
                int flightId = Integer.parseInt(flightIdField.getText());

                // Create updated Vol object
                Vol updatedVol = new Vol(flightId, departure, destination, departureDate, arrivalDate, capacity, availableSeats, price);

                // Call the service method to update the flight
                serviceVol.modifier(updatedVol);

                // Show success message
                showAlert(AlertType.INFORMATION, "Success", "Flight updated successfully!");
                resetFields();  // Reset fields after successful operation
            } else {
                showAlert(AlertType.ERROR, "Error", "Please fill all fields correctly.");
            }
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Error", "Invalid input, please ensure all fields are correctly filled.");
        }
    }

    // Delete a flight
    @FXML
    private void supprimerVol() {
        try {
            // Get the flightId from the field to delete the specific flight
            int flightId = Integer.parseInt(flightIdField.getText());

            // Call the service method to delete the flight
            serviceVol.supprimer(flightId);

            // Show success message
            showAlert(AlertType.INFORMATION, "Success", "Flight deleted successfully!");
            resetFields();  // Reset fields after successful operation
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Error", "Please provide a valid Flight ID to delete.");
        }
    }

    // Show all flights
    @FXML
    private void afficherVol() {
        serviceVol.afficher().forEach(vol -> System.out.println(vol)); // Just print them to the console
        showAlert(AlertType.INFORMATION, "Flights List", "Flights are displayed in the console.");
    }

    // Helper method to show alert dialogs
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void highlightInvalidField(TextField field) {
        field.setStyle("-fx-border-color: red;");
    }

    private void resetFieldHighlighting() {
        departureField.setStyle(null);
        destinationField.setStyle(null);
        capacityField.setStyle(null);
        availableSeatsField.setStyle(null);
        priceField.setStyle(null);
    }


    // Helper method to validate input fields
    private boolean isInputValid() {
        boolean isValid = true;
        resetErrorMessages(); // Reset the error messages first

        // Validate Departure
        if (departureField.getText().isEmpty()) {
            errorLabelDeparture.setText("Please enter a departure.");
            errorLabelDeparture.setVisible(true); // Make the error visible
            isValid = false;
        }

        // Validate Destination
        if (destinationField.getText().isEmpty()) {
            errorLabelDestination.setText("Please enter a destination.");
            errorLabelDestination.setVisible(true); // Make the error visible
            isValid = false;
        }

        // Validate Dates
        if (departureDateField.getValue() == null) {
            errorLabelDepartureDate.setText("Please enter a valid departure date.");
            errorLabelDepartureDate.setVisible(true); // Make the error visible
            isValid = false;
        }

        if (arrivalDateField.getValue() == null) {
            errorLabelArrivalDate.setText("Please enter a valid arrival date.");
            errorLabelArrivalDate.setVisible(true); // Make the error visible
            isValid = false;
        }

        // Validate Capacity
        try {
            int capacity = Integer.parseInt(capacityField.getText());
            if (capacity <= 0) {
                errorLabelCapacity.setText("Capacity must be greater than 0.");
                errorLabelCapacity.setVisible(true); // Make the error visible
                isValid = false;
            }
        } catch (NumberFormatException e) {
            errorLabelCapacity.setText("Invalid number for capacity.");
            errorLabelCapacity.setVisible(true); // Make the error visible
            isValid = false;
        }

        // Validate Available Seats
        try {
            int availableSeats = Integer.parseInt(availableSeatsField.getText());
            if (availableSeats < 0) {
                errorLabelAvailableSeats.setText("Available seats cannot be negative.");
                errorLabelAvailableSeats.setVisible(true); // Make the error visible
                isValid = false;
            }
        } catch (NumberFormatException e) {
            errorLabelAvailableSeats.setText("Invalid number for available seats.");
            errorLabelAvailableSeats.setVisible(true); // Make the error visible
            isValid = false;
        }

        // Validate Price
        try {
            double price = Double.parseDouble(priceField.getText());
            if (price <= 0) {
                errorLabelPrice.setText("Price must be greater than 0.");
                errorLabelPrice.setVisible(true); // Make the error visible
                isValid = false;
            }
        } catch (NumberFormatException e) {
            errorLabelPrice.setText("Invalid number for price.");
            errorLabelPrice.setVisible(true); // Make the error visible
            isValid = false;
        }

        return isValid;
    }

    // Reset the fields after a successful operation
    private void resetFields() {
        departureField.clear();
        destinationField.clear();
        departureDateField.setValue(null);  // Reset the DatePicker
        arrivalDateField.setValue(null);    // Reset the DatePicker
        capacityField.clear();
        availableSeatsField.clear();
        priceField.clear();
        flightIdField.clear();  // Clear the flightId field as well
    }


    public Label getErrorLabelDeparture() {
        return errorLabelDeparture;
    }

    public void setErrorLabelDeparture(Label errorLabelDeparture) {
        this.errorLabelDeparture = errorLabelDeparture;
    }

    public Label getErrorLabelDestination() {
        return errorLabelDestination;
    }

    public void setErrorLabelDestination(Label errorLabelDestination) {
        this.errorLabelDestination = errorLabelDestination;
    }

    public Label getErrorLabelDepartureDate() {
        return errorLabelDepartureDate;
    }

    public void setErrorLabelDepartureDate(Label errorLabelDepartureDate) {
        this.errorLabelDepartureDate = errorLabelDepartureDate;
    }

    public Label getErrorLabelArrivalDate() {
        return errorLabelArrivalDate;
    }

    public void setErrorLabelArrivalDate(Label errorLabelArrivalDate) {
        this.errorLabelArrivalDate = errorLabelArrivalDate;
    }

    public Label getErrorLabelCapacity() {
        return errorLabelCapacity;
    }

    public void setErrorLabelCapacity(Label errorLabelCapacity) {
        this.errorLabelCapacity = errorLabelCapacity;
    }

    public Label getErrorLabelAvailableSeats() {
        return errorLabelAvailableSeats;
    }

    public void setErrorLabelAvailableSeats(Label errorLabelAvailableSeats) {
        this.errorLabelAvailableSeats = errorLabelAvailableSeats;
    }

    public Label getErrorLabelPrice() {
        return errorLabelPrice;
    }

    public void setErrorLabelPrice(Label errorLabelPrice) {
        this.errorLabelPrice = errorLabelPrice;
    }

    private void resetErrorMessages() {
        // Hide all error messages and reset them
        errorLabelDeparture.setText("");
        errorLabelDestination.setText("");
        errorLabelDepartureDate.setText("");
        errorLabelArrivalDate.setText("");
        errorLabelCapacity.setText("");
        errorLabelAvailableSeats.setText("");
        errorLabelPrice.setText("");

        errorLabelDeparture.setVisible(false);
        errorLabelDestination.setVisible(false);
        errorLabelDepartureDate.setVisible(false);
        errorLabelArrivalDate.setVisible(false);
        errorLabelCapacity.setVisible(false);
        errorLabelAvailableSeats.setVisible(false);
        errorLabelPrice.setVisible(false);
    }

    @FXML




    private void showFlightDetails(javafx.event.ActionEvent event) {
        try {
            // Load the ListeVol FXML file for the flight details window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vol/listeVol.fxml"));

            Parent root = loader.load();  // Load the layout for the flight details window

            // Create a new stage (window) for the flight details
            Stage stage = new Stage();
            stage.setTitle("Flight Details");  // Title for the new window
            stage.setScene(new Scene(root, 800, 500));  // Set the scene size for the new window
            stage.show();  // Display the new window

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

