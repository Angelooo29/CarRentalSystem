/*package com.lester.carrentalsystem.Controller.Client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class VehicleCardController {

    @FXML
    private ImageView vehicleImage;

    @FXML
    private Label priceLabel;

    @FXML
    private Label availabilityLabel;

    @FXML
    private Button rentButton;

    public void setVehicleData(VehiclesInfo vehicle) {
        vehicleImage.setImage(new Image(vehicle.getImagePath())); // Load image
        priceLabel.setText("Price: $" + vehicle.getPrice());
        availabilityLabel.setText(vehicle.isAvailable() ? "Available" : "Not Available");

        rentButton.setOnAction(e -> rentVehicle(vehicle));
    }

    private void rentVehicle(VehiclesInfo vehicle) {
        System.out.println("Renting vehicle: " + vehicle.getModel());
    }
}*/