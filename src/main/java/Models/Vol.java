package Models;

public class Vol {
    private int flightId;
    private String departure;
    private String destination;
    private String departureDate;
    private String arrivalDate;
    private int capacity;
    private int availableSeats;
    private double price;

    // Constructor to initialize all fields
    public Vol(int flightId, String departure, String destination, String departureDate, String arrivalDate, int capacity, int availableSeats, double price) {
        this.flightId = flightId;
        this.departure = departure;
        this.destination = destination;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.capacity = capacity;
        this.availableSeats = availableSeats;
        this.price = price;
    }

    // Getters and Setters
    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // toString method to display flight details
    @Override
    public String toString() {
        return "Vol{" +
                "flightId=" + flightId +
                ", departure='" + departure + '\'' +
                ", destination='" + destination + '\'' +
                ", departureDate='" + departureDate + '\'' +
                ", arrivalDate='" + arrivalDate + '\'' +
                ", capacity=" + capacity +
                ", availableSeats=" + availableSeats +
                ", price=" + price +
                '}';
    }
}
