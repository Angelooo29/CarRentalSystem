package com.lester.carrentalsystem.Controller.Client;

public class VehiclesInfo {
    private String imagePath;
    private String make;
    private String model;
    private String type;
    private double price;
    private int seating;
    private int available;

    public VehiclesInfo(String imagePath, String make, String model, String type, double price, int seating, int available) {
        this.imagePath = imagePath;
        this.make = make;
        this.model = model;
        this.type = type;
        this.price = price;
        this.seating = seating;
        this.available = available;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public int getSeating() {
        return seating;
    }

    public int getAvailable() {
        return available;
    }

    public String isAvailable() {
        return available > 0 ? "Available" : "Not Available";
    }
}