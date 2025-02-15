package com.esprit.services.services_voiture;

import com.esprit.models.Reservations;
import com.esprit.utils.DataBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReservationService {
    private Connection connection;

    public ReservationService() {
        connection = DataBase.getInstance().getConnection();
    }

    public ObservableList<Reservations> getReservationsByClient(int utilisateurId) {
        ObservableList<Reservations> reservationsList = FXCollections.observableArrayList();
        String query = "SELECT * FROM reservations WHERE utilisateur_id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, utilisateurId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Reservations reservation = new Reservations(
                        resultSet.getInt("id"),
                        resultSet.getInt("utilisateur_id"),
                        resultSet.getInt("voiture_id"),
                        resultSet.getDate("date_debut"),
                        resultSet.getDate("date_fin"),
                        resultSet.getString("statut"),
                        resultSet.getDouble("prix_finale"),
                        resultSet.getBoolean("conducteur_supplementaire") // ✅ Correction ajoutée ici
                );
                System.out.println("✅ Ajout d'une réservation : " + reservation); // 🔹 Debugging
                reservationsList.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservationsList;
    }

}
