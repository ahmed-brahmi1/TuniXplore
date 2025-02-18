package com.esprit.services.Hotel;

import com.esprit.models.RoomReservation;
import com.esprit.utils.DataBase;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomReservationService {
    private final Connection connection;

    public RoomReservationService() {
        this.connection = DataBase.getInstance().getConnection();
    }

    public void saveReservation(RoomReservation reservation) throws SQLException {
        String query = "INSERT INTO room_reservations (room_id, guest_name, check_in, check_out, total_price, room_number, room_type, hotel_name, isConfirmed) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, reservation.getRoomId());
            pstmt.setString(2, reservation.getGuestName());
            pstmt.setDate(3, Date.valueOf(reservation.getCheckInDate()));
            pstmt.setDate(4, Date.valueOf(reservation.getCheckOutDate()));
            pstmt.setDouble(5, reservation.getTotalPrice());
            pstmt.setInt(6, reservation.getRoomNumber());
            pstmt.setString(7, reservation.getRoomType());
            pstmt.setString(8, reservation.getHotelName());
            pstmt.setBoolean(9, reservation.isConfirmed());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("ERROR SAVING RESERVATION " + e.getMessage());
            throw e; // Rethrow the exception after logging
        }
    }

    public boolean doesRoomExist(int roomId) throws SQLException {
        String query = "SELECT COUNT(*) FROM rooms WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Returns true if room exists
            }
        }
        return false;
    }

    public List<RoomReservation> getAllReservations() throws SQLException {
        List<RoomReservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM room_reservations"; // Adjust this query based on your actual table structure

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                RoomReservation reservation = new RoomReservation(
                        rs.getInt("room_id"),
                        rs.getString("guest_name"),
                        rs.getDate("check_in").toLocalDate(),
                        rs.getDate("check_out").toLocalDate(),
                        rs.getDouble("total_price"),
                        rs.getInt("room_number"),
                        rs.getString("room_type"),
                        rs.getString("hotel_name"),
                        rs.getBoolean("isConfirmed")
                );
                reservations.add(reservation);
            }
        }
        return reservations;
    }

    public void confirmReservation(int id, boolean confirmed) {
        String query = "UPDATE room_reservations SET isConfirmed = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setBoolean(1, confirmed);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}