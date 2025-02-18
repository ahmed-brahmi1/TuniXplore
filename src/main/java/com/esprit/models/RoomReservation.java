package com.esprit.models;

import javafx.beans.property.*;

import java.time.LocalDate;

public class RoomReservation {
    private final IntegerProperty id = new SimpleIntegerProperty(this, "id");
    private final IntegerProperty roomId = new SimpleIntegerProperty(this, "roomId");
    private final StringProperty guestName = new SimpleStringProperty(this, "guestName");
    private final ObjectProperty<LocalDate> checkInDate = new SimpleObjectProperty<>(this, "checkInDate");
    private final ObjectProperty<LocalDate> checkOutDate = new SimpleObjectProperty<>(this, "checkOutDate");
    private final DoubleProperty totalPrice = new SimpleDoubleProperty(this, "totalPrice");
    private final IntegerProperty roomNumber = new SimpleIntegerProperty(this, "roomNumber");
    private final StringProperty roomType = new SimpleStringProperty(this, "roomType");
    private final StringProperty hotelName = new SimpleStringProperty(this, "hotelName");
    private final BooleanProperty isConfirmed = new SimpleBooleanProperty(this, "isConfirmed");

    public RoomReservation(int roomId, String guestName, LocalDate checkInDate, LocalDate checkOutDate, double totalPrice, int roomNumber, String roomType, String hotelName, boolean isConfirmed) {
        this.roomId.set(roomId);
        this.guestName.set(guestName);
        this.checkInDate.set(checkInDate);
        this.checkOutDate.set(checkOutDate);
        this.totalPrice.set(totalPrice);
        this.roomNumber.set(roomNumber);
        this.roomType.set(roomType);
        this.hotelName.set(hotelName);
        this.isConfirmed.set(isConfirmed);
    }

    // Getters and Setters for properties
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public int getRoomId() {
        return roomId.get();
    }

    public void setRoomId(int roomId) {
        this.roomId.set(roomId);
    }

    public IntegerProperty roomIdProperty() {
        return roomId;
    }

    public String getGuestName() {
        return guestName.get();
    }

    public void setGuestName(String guestName) {
        this.guestName.set(guestName);
    }

    public StringProperty guestNameProperty() {
        return guestName;
    }

    public LocalDate getCheckInDate() {
        return checkInDate.get();
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate.set(checkInDate);
    }

    public ObjectProperty<LocalDate> checkInDateProperty() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate.get();
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate.set(checkOutDate);
    }

    public ObjectProperty<LocalDate> checkOutDateProperty() {
        return checkOutDate;
    }

    public double getTotalPrice() {
        return totalPrice.get();
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice.set(totalPrice);
    }

    public DoubleProperty totalPriceProperty() {
        return totalPrice;
    }

    public int getRoomNumber() {
        return roomNumber.get();
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber.set(roomNumber);
    }

    public IntegerProperty roomNumberProperty() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType.get();
    }

    public void setRoomType(String roomType) {
        this.roomType.set(roomType);
    }

    public StringProperty roomTypeProperty() {
        return roomType;
    }

    public String getHotelName() {
        return hotelName.get();
    }

    public void setHotelName(String hotelName) {
        this.hotelName.set(hotelName);
    }

    public StringProperty hotelNameProperty() {
        return hotelName;
    }

    public boolean isConfirmed() {
        return isConfirmed.get();
    }

    public void setConfirmed(boolean confirmed) {
        this.isConfirmed.set(confirmed);
    }

    public BooleanProperty isConfirmedProperty() {
        return isConfirmed;
    }
}