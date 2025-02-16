package com.esprit.services.Hotel;

import com.esprit.models.Hotel;
import com.esprit.utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class serviceHotel {
    private final Connection con;

    public serviceHotel() {
        this.con = DataBase.getInstance().getConnection();
    }

    // Add a new hotel (no description anymore)
    public int addHotel(Hotel hotel) {
        String hotelQuery = "INSERT INTO hotels (name, location, rating, base_price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement hotelPst = con.prepareStatement(hotelQuery, Statement.RETURN_GENERATED_KEYS)) {
            con.setAutoCommit(false); // Start transaction

            // Insert hotel
            hotelPst.setString(1, hotel.getName());
            hotelPst.setString(2, hotel.getLocation());
            hotelPst.setDouble(3, hotel.getRating());
            hotelPst.setDouble(4, hotel.getBasePrice());
            hotelPst.executeUpdate();

            // Retrieve generated hotel ID
            ResultSet rs = hotelPst.getGeneratedKeys();
            if (rs.next()) {
                int hotelId = rs.getInt(1);
                hotel.setId(hotelId);
                con.commit(); // Commit only if successful
                return hotelId;
            }
        } catch (SQLException e) {
            try {
                con.rollback(); // Rollback on error
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                con.setAutoCommit(true); // Restore auto-commit mode
            } catch (SQLException autoCommitEx) {
                autoCommitEx.printStackTrace();
            }
        }
        return -1; // Return -1 if insertion failed
    }

    // Add features for a hotel
    public boolean addHotelFeatures(int hotelId, List<String> features) {
        String featureQuery = "INSERT INTO hotel_features (hotel_id, feature_name) VALUES (?, ?)";
        try (PreparedStatement featurePst = con.prepareStatement(featureQuery)) {
            con.setAutoCommit(false); // Start transaction

            for (String feature : features) {
                featurePst.setInt(1, hotelId);
                featurePst.setString(2, feature);
                featurePst.addBatch();
            }

            int[] result = featurePst.executeBatch();
            con.commit(); // Commit transaction
            return result.length == features.size(); // Check if all features were added
        } catch (SQLException e) {
            try {
                con.rollback(); // Rollback on error
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                con.setAutoCommit(true); // Restore auto-commit mode
            } catch (SQLException autoCommitEx) {
                autoCommitEx.printStackTrace();
            }
        }
        return false;
    }

    // Add multiple images for a hotel
    public boolean addHotelImages(int hotelId, List<String> imageUrls) {
        String imageQuery = "INSERT INTO hotel_images (hotel_id, image_url) VALUES (?, ?)";
        try (PreparedStatement imagePst = con.prepareStatement(imageQuery)) {
            con.setAutoCommit(false); // Start transaction

            for (String imageUrl : imageUrls) {
                imagePst.setInt(1, hotelId);
                imagePst.setString(2, imageUrl);
                imagePst.addBatch();
            }
            imagePst.executeBatch();

            con.commit(); // Commit transaction
            return true;
        } catch (SQLException e) {
            try {
                con.rollback(); // Rollback on error
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                con.setAutoCommit(true); // Restore auto-commit mode
            } catch (SQLException autoCommitEx) {
                autoCommitEx.printStackTrace();
            }
        }
        return false;
    }

    // Add a hotel with features and images in a single transaction
    public boolean addHotelWithFeaturesAndImages(Hotel hotel, List<String> features, List<String> imageUrls) {
        try {
            con.setAutoCommit(false); // Disable auto-commit to manage the transaction manually

            // Add the hotel
            int hotelId = addHotel(hotel);
            if (hotelId == -1) {
                con.rollback(); // Rollback if hotel insertion failed
                return false;
            }

            // Add features for the hotel
            boolean featuresAdded = addHotelFeatures(hotelId, features);
            if (!featuresAdded) {
                con.rollback(); // Rollback if adding features failed
                return false;
            }

            // Add images for the hotel
            boolean imagesAdded = addHotelImages(hotelId, imageUrls);
            if (!imagesAdded) {
                con.rollback(); // Rollback if adding images failed
                return false;
            }

            // Commit the transaction if all operations were successful
            con.commit();
            return true;

        } catch (SQLException e) {
            try {
                con.rollback(); // Rollback if any exception occurs
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            return false;

        } finally {
            try {
                con.setAutoCommit(true); // Restore auto-commit mode
            } catch (SQLException autoCommitEx) {
                autoCommitEx.printStackTrace();
            }
        }
    }

    // Get features for a hotel
    public List<String> getHotelFeatures(int hotelId) {
        List<String> features = new ArrayList<>();
        String query = "SELECT feature_name FROM hotel_features WHERE hotel_id = ?";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, hotelId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String featureName = rs.getString("feature_name");
                features.add(featureName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return features;
    }



    // Get hotel by ID
    public Hotel getHotelById(int hotelId) {
        String query = "SELECT * FROM hotels WHERE id = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, hotelId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String location = rs.getString("location");
                float rating = rs.getFloat("rating");
                double basePrice = rs.getDouble("base_price");

                // Fetch images for the hotel
                List<String> images = getHotelImages(hotelId);

                // Fetch features for the hotel
                List<String> features = getHotelFeatures(hotelId);

                Hotel hotel = new Hotel(name, location, rating, basePrice, images, features);
                hotel.setId(hotelId); // Set the ID
                return hotel;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean hasAvailableRooms(int hotelId) throws SQLException {
        String query = "SELECT COUNT(*) FROM rooms WHERE hotel_id = ? AND is_available = true";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, hotelId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Return true if there are available rooms
            }
        }
        return false; // No available rooms
    }

    public List<String> getDistinctLocations() {
        List<String> locations = new ArrayList<>();
        String query = "SELECT DISTINCT location FROM hotels"; // Adjust this query based on your actual table structure

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                locations.add(rs.getString("location"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return locations;
    }
    // Get images for a hotel
    public List<String> getHotelImages(int hotelId) {
        List<String> images = new ArrayList<>();
        String query = "SELECT image_url FROM hotel_images WHERE hotel_id = ?";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, hotelId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String imageUrl = rs.getString("image_url");
                images.add(imageUrl);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return images;
    }

    // Get hotels with one image for each hotel
    public List<Hotel> getHotelsWithOneImage() {
        List<Hotel> hotels = new ArrayList<>();
        String query = "SELECT h.id, h.name, h.location, h.rating, h.base_price, hi.image_url " +
                "FROM hotels h LEFT JOIN hotel_images hi ON h.id = hi.hotel_id";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int hotelId = rs.getInt("id");
                String name = rs.getString("name");
                String location = rs.getString("location");
                float rating = rs.getFloat("rating");
                double basePrice = rs.getDouble("base_price");
                String imageUrl = rs.getString("image_url");

                // Fetch the features for the hotel
                List<String> features = getHotelFeatures(hotelId);

                // If there is an image, use it. If not, use a default image (you could also handle it differently)
                List<String> images = imageUrl != null ? List.of(imageUrl) : List.of("default_image_url");

                Hotel hotel = new Hotel(name, location, rating, basePrice, images, features);
                hotel.setId(hotelId);
                hotels.add(hotel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hotels;
    }

    public List<Hotel> getAllHotels() {
        List<Hotel> hotels = new ArrayList<>();
        String query = "SELECT * FROM hotels";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int hotelId = rs.getInt("id");
                String name = rs.getString("name");
                String location = rs.getString("location");
                float rating = rs.getFloat("rating");
                double basePrice = rs.getDouble("base_price");

                // Fetch images for the hotel
                List<String> images = getHotelImages(hotelId);

                // Fetch features for the hotel
                List<String> features = getHotelFeatures(hotelId);

                Hotel hotel = new Hotel(name, location, rating, basePrice, images, features);
                hotel.setId(hotelId);
                hotels.add(hotel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hotels;
    }


    public void deleteHotel(int hotelId) {
        String deleteFeaturesQuery = "DELETE FROM hotel_features WHERE hotel_id = ?";
        String deleteImagesQuery = "DELETE FROM hotel_images WHERE hotel_id = ?";
        String deleteHotelQuery = "DELETE FROM hotels WHERE id = ?";

        try (PreparedStatement featurePst = con.prepareStatement(deleteFeaturesQuery);
             PreparedStatement imagePst = con.prepareStatement(deleteImagesQuery);
             PreparedStatement hotelPst = con.prepareStatement(deleteHotelQuery)) {

            con.setAutoCommit(false); // Start transaction

            // Delete features first (to maintain referential integrity)
            featurePst.setInt(1, hotelId);
            featurePst.executeUpdate();

            // Delete images
            imagePst.setInt(1, hotelId);
            imagePst.executeUpdate();

            // Delete the hotel
            hotelPst.setInt(1, hotelId);
            hotelPst.executeUpdate();

            con.commit(); // Commit transaction
            System.out.println("Hotel deleted successfully!");

        } catch (SQLException e) {
            try {
                con.rollback(); // Rollback on error
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                con.setAutoCommit(true); // Restore auto-commit mode
            } catch (SQLException autoCommitEx) {
                autoCommitEx.printStackTrace();
            }
        }
    }


    public boolean editHotel(Hotel hotel) {
        String hotelQuery = "UPDATE hotels SET name = ?, location = ?, rating = ?, base_price = ? WHERE id = ?";
        try (PreparedStatement hotelPst = con.prepareStatement(hotelQuery)) {
            con.setAutoCommit(false); // Start transaction

            // Update hotel details
            hotelPst.setString(1, hotel.getName());
            hotelPst.setString(2, hotel.getLocation());
            hotelPst.setFloat(3, hotel.getRating());
            hotelPst.setDouble(4, hotel.getBasePrice());
            hotelPst.setInt(5, hotel.getId());
            hotelPst.executeUpdate();

            // Update features
            if (!updateHotelFeatures(hotel.getId(), hotel.getFeatures())) {
                con.rollback();
                return false;
            }

            // Update images
            if (!updateHotelImages(hotel.getId(), hotel.getImages())) {
                con.rollback();
                return false;
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException autoCommitEx) {
                autoCommitEx.printStackTrace();
            }
        }
    }

    // Update hotel features
    public boolean updateHotelFeatures(int hotelId, List<String> features) {
        String deleteQuery = "DELETE FROM hotel_features WHERE hotel_id = ?";
        String insertQuery = "INSERT INTO hotel_features (hotel_id, feature_name) VALUES (?, ?)";

        try (PreparedStatement deletePst = con.prepareStatement(deleteQuery);
             PreparedStatement insertPst = con.prepareStatement(insertQuery)) {

            deletePst.setInt(1, hotelId);
            deletePst.executeUpdate();

            for (String feature : features) {
                insertPst.setInt(1, hotelId);
                insertPst.setString(2, feature);
                insertPst.addBatch();
            }
            insertPst.executeBatch();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update hotel images
    public boolean updateHotelImages(int hotelId, List<String> images) {
        String deleteQuery = "DELETE FROM hotel_images WHERE hotel_id = ?";
        String insertQuery = "INSERT INTO hotel_images (hotel_id, image_url) VALUES (?, ?)";

        try (PreparedStatement deletePst = con.prepareStatement(deleteQuery);
             PreparedStatement insertPst = con.prepareStatement(insertQuery)) {

            deletePst.setInt(1, hotelId);
            deletePst.executeUpdate();

            for (String imageUrl : images) {
                insertPst.setInt(1, hotelId);
                insertPst.setString(2, imageUrl);
                insertPst.addBatch();
            }
            insertPst.executeBatch();
            return true;

        } catch (SQLException e) {
            System.out.println("IMAGE ERROR "+e.getMessage());
            return false;
        }
    }
    public List<Integer> getAllHotelIds() {
        List<Integer> hotelIds = new ArrayList<>();
        String query = "SELECT id FROM hotels";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                hotelIds.add(rs.getInt("id"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching hotel IDs: " + e.getMessage());
        }

        return hotelIds;
    }
    public int getHotelIdByName(String hotelName) {
        String query = "SELECT id FROM hotels WHERE name = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, hotelName);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching hotel ID by name: " + e.getMessage());
        }
        return -1; // Return -1 if not found
    }
    public List<String> getAllHotelNames() {
        List<String> hotelNames = new ArrayList<>();
        String query = "SELECT name FROM hotels"; // Modify this if your table name/column is different

        try (Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                hotelNames.add(resultSet.getString("name"));
            }

        } catch (SQLException e) {
            System.out.println("HOTEL NAME ERROR "+e.getMessage());
        }

        return hotelNames;
    }
}
