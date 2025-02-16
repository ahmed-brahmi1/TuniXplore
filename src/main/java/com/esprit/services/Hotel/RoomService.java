package com.esprit.services.Hotel;

import com.esprit.models.Room;
import com.esprit.utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomService {
    private final Connection con;

    public RoomService() {
        this.con = DataBase.getInstance().getConnection();
    }

    public void addRoom(Room room) throws SQLException{
        String query = "INSERT INTO rooms (location, base_price, hotel_id, room_type, room_number, is_available, image_url) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {


            preparedStatement.setString(1, room.getLocation());
            preparedStatement.setDouble(2, room.getBasePrice());
            preparedStatement.setInt(3, room.getHotelId());
            preparedStatement.setString(4, room.getRoomType());
            preparedStatement.setInt(5, room.getRoomNumber());
            preparedStatement.setBoolean(6, room.isAvailable());
            preparedStatement.setString(7, room.getImageUrl());


            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Failed to add room");
            }

            // Retrieve the generated room ID
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int roomId = generatedKeys.getInt(1);
                    insertAmenities(roomId, room.getAmenities());
                } else {
                    throw new SQLException("Creating room failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.out.println("FAILED ADDING ROOM "+e.getMessage());
        }
    }

    private void insertAmenities(int roomId, List<String> amenities) throws SQLException {
        String amenityQuery = "INSERT INTO room_amenities (room_id, amenity) VALUES (?, ?)";
        try (PreparedStatement amenityPst = con.prepareStatement(amenityQuery)) {
            for (String amenity : amenities) {
                amenityPst.setInt(1, roomId);
                amenityPst.setString(2, amenity);
                amenityPst.addBatch();
            }
            amenityPst.executeBatch();
        }
    }

    public List<Room> getAllRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM rooms";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Room room = new Room(
                        rs.getString("location"),
                        rs.getDouble("base_price"),
                        rs.getString("image_url"),
                        rs.getInt("hotel_id"),
                        rs.getString("room_type"),
                        rs.getInt("room_number"),
                        rs.getBoolean("is_available"),
                        fetchAmenities(rs.getInt("id")) // Fetch amenities for the room
                );
                rooms.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception for handling in the controller
        }

        return rooms;
    }

    public List<Room> getRoomsByHotel(int hotelId) throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM rooms WHERE hotel_id = ? AND is_available = true"; // Filter for available rooms

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, hotelId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Room room = new Room(
                        rs.getInt("id"),
                        rs.getString("location"),
                        rs.getDouble("base_price"),
                        rs.getString("image_url"),
                        rs.getInt("hotel_id"),
                        rs.getString("room_type"),
                        rs.getInt("room_number"),
                        rs.getBoolean("is_available"),
                        fetchAmenities(rs.getInt("id")) // Fetch amenities for the room
                );
                rooms.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception for handling in the controller
        }

        return rooms;
    }

    // Method to update room availability
    public void updateRoomAvailability(int roomId, boolean isAvailable) throws SQLException {
        String query = "UPDATE rooms SET is_available = ? WHERE id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setBoolean(1, isAvailable);
            pstmt.setInt(2, roomId);
            pstmt.executeUpdate();
        }
    }

    public Room getRoomByNumber(int hotelId, int roomNumber) {
        Room room = null;
        String query = "SELECT * FROM rooms WHERE hotel_id = ? AND room_number = ?";

        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, hotelId);
            preparedStatement.setInt(2, roomNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int roomId = resultSet.getInt("id"); // Fetch room ID
                room = new Room(

                        resultSet.getString("location"),
                        resultSet.getDouble("base_price"),
                        resultSet.getString("image_url"),
                        resultSet.getInt("hotel_id"),
                        resultSet.getString("room_type"),
                        resultSet.getInt("room_number"),
                        resultSet.getBoolean("is_available"),
                        fetchAmenities(roomId)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return room;
    }

    private List<String> fetchAmenities(int roomId) {
        List<String> amenities = new ArrayList<>();
        String query = "SELECT amenity FROM room_amenities WHERE room_id = ?";

        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, roomId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                amenities.add(resultSet.getString("amenity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return amenities;
    }

    public void updateRoom(int hotelId, int roomNumber, Room updatedRoom) {
        String query = "UPDATE rooms SET location = ?, base_price = ?, room_type = ?, image_url = ?, is_available = ? " +
                "WHERE hotel_id = ? AND room_number = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setString(1, updatedRoom.getLocation());
            preparedStatement.setDouble(2, updatedRoom.getBasePrice());
            preparedStatement.setString(3, updatedRoom.getRoomType());
            preparedStatement.setString(4, updatedRoom.getImageUrl());
            preparedStatement.setBoolean(5, updatedRoom.isAvailable());
            preparedStatement.setInt(6, hotelId);
            preparedStatement.setInt(7, roomNumber);
            preparedStatement.executeUpdate();

            // Update amenities
            int roomId = getRoomId(hotelId, roomNumber);
            updateRoomAmenities(roomId, updatedRoom.getAmenities());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getRoomId(int hotelId, int roomNumber) throws SQLException {
        String query = "SELECT id FROM rooms WHERE hotel_id = ? AND room_number = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, hotelId);
            preparedStatement.setInt(2, roomNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        }
        throw new SQLException("Room not found");
    }

    private void updateRoomAmenities(int roomId, List<String> newAmenities) throws SQLException {
        String deleteQuery = "DELETE FROM room_amenities WHERE room_id = ?";
        try (PreparedStatement deleteStmt = con.prepareStatement(deleteQuery)) {
            deleteStmt.setInt(1, roomId);
            deleteStmt.executeUpdate();
        }

        insertAmenities(roomId, newAmenities);
    }

    public void deleteRoom(int hotelId, int roomNumber) {
        try {
            int roomId = getRoomId(hotelId, roomNumber);

            String deleteAmenitiesQuery = "DELETE FROM room_amenities WHERE room_id = ?";
            try (PreparedStatement deleteStmt = con.prepareStatement(deleteAmenitiesQuery)) {
                deleteStmt.setInt(1, roomId);
                deleteStmt.executeUpdate();
            }

            String deleteRoomQuery = "DELETE FROM rooms WHERE id = ?";
            try (PreparedStatement preparedStatement = con.prepareStatement(deleteRoomQuery)) {
                preparedStatement.setInt(1, roomId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
