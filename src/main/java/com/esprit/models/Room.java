package com.esprit.models;

import java.util.List;

public class Room {
    private int id;

    private String location;  // Can be "Floor" or "Building"

    private double basePrice;
    private String image_url;
    private int hotelId;  // Foreign key relation with Hotel
    private String roomType;  // E.g., "Single", "Double", "Suite", etc.
    private int roomNumber;  // Floor on which the room is located
    private boolean isAvailable;  // Availability status of the room
    private List<String> amenities;  // E.g., "Wi-Fi", "Air Conditioning", "TV", etc.



    public Room(String location, double basePrice, String images, int hotelId, String roomType, int roomNumber, boolean isAvailable, List<String> amenities) {

        this.location = location;
        this.basePrice = basePrice;
        this.image_url = images;
        this.hotelId = hotelId;
        this.roomType = roomType;
        this.roomNumber = roomNumber;

        this.isAvailable = isAvailable;
        this.amenities = amenities;
    }

    public Room(int id, String location, double basePrice, String image_url, int hotelId, String roomType, int roomNumber, boolean isAvailable, List<String> amenities) {
        this.id = id;
        this.location = location;
        this.basePrice = basePrice;
        this.image_url = image_url;
        this.hotelId = hotelId;
        this.roomType = roomType;
        this.roomNumber = roomNumber;
        this.isAvailable = isAvailable;
        this.amenities = amenities;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    // Getter and Setter methods for all fields
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }






    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    // Getters and Setters for all fields
    public String getImageUrl() {
        return image_url;
    }

    public void setImageUrl(String imageUrl) {
        this.image_url = imageUrl;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }



    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }
}
