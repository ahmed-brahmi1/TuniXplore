package services;

import Models.Vol;
import com.example.vol.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceVol {

    private Connection connection;

    public ServiceVol() {
        // Initialize connection from DataBase singleton instance
        connection = DataBase.getInstance().getConnection();
    }

    // Add a new flight
    public void ajouter(Vol vol) {
        String query = "INSERT INTO flights (departure, destination, departure_date, arrival_date, capacity, available_seats, price) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, vol.getDeparture());
            preparedStatement.setString(2, vol.getDestination());
            preparedStatement.setString(3, vol.getDepartureDate());
            preparedStatement.setString(4, vol.getArrivalDate());
            preparedStatement.setInt(5, vol.getCapacity());
            preparedStatement.setInt(6, vol.getAvailableSeats());
            preparedStatement.setDouble(7, vol.getPrice());

            // Execute the query and commit the transaction
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                connection.commit(); // Commit the transaction
                System.out.println("Flight added successfully!");
            } else {
                System.out.println("No rows affected. Flight not added.");
            }
        } catch (SQLException e) {
            try {
                connection.rollback(); // Rollback in case of error
                System.out.println("Error while adding flight: " + e.getMessage());
            } catch (SQLException ex) {
                System.out.println("Error during rollback: " + ex.getMessage());
            }
        }
    }

    // Update a flight
    public void modifier(Vol vol) {
        String query = "UPDATE flights SET departure = ?, destination = ?, departure_date = ?, arrival_date = ?, capacity = ?, available_seats = ?, price = ? WHERE flight_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, vol.getDeparture());
            preparedStatement.setString(2, vol.getDestination());
            preparedStatement.setString(3, vol.getDepartureDate());
            preparedStatement.setString(4, vol.getArrivalDate());
            preparedStatement.setInt(5, vol.getCapacity());
            preparedStatement.setInt(6, vol.getAvailableSeats());
            preparedStatement.setDouble(7, vol.getPrice());
            preparedStatement.setInt(8, vol.getFlightId());

            // Execute the update and commit the transaction
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                connection.commit(); // Commit the transaction
                System.out.println("Flight updated successfully!");
            } else {
                System.out.println("No rows affected. Flight not updated.");
            }
        } catch (SQLException e) {
            try {
                connection.rollback(); // Rollback in case of error
                System.out.println("Error while updating flight: " + e.getMessage());
            } catch (SQLException ex) {
                System.out.println("Error during rollback: " + ex.getMessage());
            }
        }
    }

    // Delete a flight
    public void supprimer(int flightId) {
        String query = "DELETE FROM flights WHERE flight_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, flightId);

            // Execute the delete and commit the transaction
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                connection.commit(); // Commit the transaction
                System.out.println("Flight deleted successfully!");
            } else {
                System.out.println("No rows affected. Flight not deleted.");
            }
        } catch (SQLException e) {
            try {
                connection.rollback(); // Rollback in case of error
                System.out.println("Error while deleting flight: " + e.getMessage());
            } catch (SQLException ex) {
                System.out.println("Error during rollback: " + ex.getMessage());
            }
        }
    }

    // Display all flights
    public List<Vol> afficher() {
        List<Vol> flights = new ArrayList<>();
        String query = "SELECT * FROM flights";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                flights.add(new Vol(
                        resultSet.getInt("flight_id"),
                        resultSet.getString("departure"),
                        resultSet.getString("destination"),
                        resultSet.getString("departure_date"),
                        resultSet.getString("arrival_date"),
                        resultSet.getInt("capacity"),
                        resultSet.getInt("available_seats"),
                        resultSet.getDouble("price")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error while displaying flights: " + e.getMessage());
        }
        return flights;
    }

    // Find a flight by ID (optional)
    public Vol findVol(int flightId) {
        Vol vol = null;
        String query = "SELECT * FROM flights WHERE flight_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, flightId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                vol = new Vol(
                        resultSet.getInt("flight_id"),
                        resultSet.getString("departure"),
                        resultSet.getString("destination"),
                        resultSet.getString("departure_date"),
                        resultSet.getString("arrival_date"),
                        resultSet.getInt("capacity"),
                        resultSet.getInt("available_seats"),
                        resultSet.getDouble("price")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error while finding flight: " + e.getMessage());
        }
        return vol;
    }

}
