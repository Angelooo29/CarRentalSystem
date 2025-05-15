package com.lester.carrentalsystem.Controller.Client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.text.DecimalFormat;

public class VehicleCardsController {

    @FXML
    private ImageView vehicleImage;

    @FXML
    private Label vehicleNameLabel;

    @FXML
    private Button rentNowButton;

    @FXML
    private Label priceLabel;

    @FXML
    private Label availableLabel;

    public void initialize () {
        rentNowButton.setOnMouseEntered(e -> rentNowButton.setStyle("-fx-background-color: #9145f5;"));
        rentNowButton.setOnMouseExited(e -> rentNowButton.setStyle("-fx-background-color:  #732bb5;"));
    }

    public void setVehicleData(VehiclesInfo vehicle) {
        String available = "";
        if (vehicle.getAvailable() > 0) {
            available = "Available";
            availableLabel.setStyle("-fx-text-fill: green;");
        } else if (vehicle.getAvailable() == 0) {
            available = "Not Available";
            availableLabel.setStyle("-fx-text-fill: red;");
            rentNowButton.setDisable(true);
        }

        DecimalFormat df = new DecimalFormat("#,###.00");
        String formattedPrice = df.format(vehicle.getPrice());

        vehicleImage.setImage(new Image(vehicle.getImagePath()));
        vehicleNameLabel.setText(vehicle.getMake() + " " + vehicle.getModel());
        priceLabel.setText("₱ " + formattedPrice);
        availableLabel.setText(available);


        rentNowButton.setOnAction(e -> rentVehicle(vehicle));
    }

    private void rentVehicle(VehiclesInfo vehicle) {
        System.out.println("Renting vehicle: " + vehicle.getModel());

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Client/RentNowTransaction.fxml"));
            Parent root = loader.load();
            Image icon = new Image(getClass().getResource("/Images/Logo.png").toString());

            RentNowTransactionController controller = loader.getController();
            controller.setCarDetails(vehicle);

            Stage stage = new Stage();
            stage.setTitle("Car Rental System - Rent Transaction");
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.getIcons().add(icon);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
