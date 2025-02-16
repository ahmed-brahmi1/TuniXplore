package com.esprit.models;

import java.util.List;

public class Hotel {
    private int id;
    private String name;
    private String location;
    private float rating;
    private double basePrice;
    private List<String> images; // List of image URLs
    private List<String> features; // Added list of features

    // Constructor with ID (used for retrieving data)
    public Hotel(int id, String name, String location, float rating, double basePrice, List<String> images, List<String> features) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.rating = rating;
        this.basePrice = basePrice;
        this.images = images;
        this.features = features;
    }

    // Constructor without ID (used for adding a new hotel)
    public Hotel(String name, String location, float rating, double basePrice, List<String> images, List<String> features) {
        this.name = name;
        this.location = location;

        this.rating = rating;
        this.basePrice = basePrice;
        this.images = images;
        this.features = features;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }





    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }
    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +

                ", rating=" + rating +
                ", basePrice=" + basePrice +
                ", images=" + images +
                '}';
    }

    public String getImageUrl() {
        return images.get(0);
    }
}
