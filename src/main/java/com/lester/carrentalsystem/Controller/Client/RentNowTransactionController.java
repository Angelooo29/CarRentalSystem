package com.lester.carrentalsystem.Controller.Client;

import com.lester.carrentalsystem.Infrastructure.Data.DBConnection;
import com.lester.carrentalsystem.Model.ClientSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.*;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class RentNowTransactionController implements Initializable {

    @FXML private Label calculationLabel;
    @FXML private Label totalRentAmountLabel;
    @FXML private ImageView vehicleImage;
    @FXML private Label vehicleNameLabel;
    @FXML private Label rentPriceLabel;
    @FXML private Label seatingCapacityLabel;
    @FXML private DatePicker rentStartDatePicker;
    @FXML private DatePicker rentEndDatePicker;
    @FXML private TextField pickOffLocationField;
    @FXML private ComboBox<String> colorComboBox;
    @FXML private ComboBox<String> paymentMethodComboBox;
    @FXML private Button confirmRentButton;
    @FXML private Button cancelButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paymentMethodComboBox.getItems().addAll("Cash on Delivery", "Credit/Debit Card", "Online Banking","e-Wallet");
        colorComboBox.getItems().addAll("White", "Black", "Grey");
        confirmRentButton.setOnMouseEntered(e -> confirmRentButton.setStyle("-fx-background-color: #9145f5"));
        confirmRentButton.setOnMouseExited(e -> confirmRentButton.setStyle("-fx-background-color:  #732bb5"));
        rentStartDatePicker.setOnAction(event -> calculateTotalRent());
        rentEndDatePicker.setOnAction(event -> calculateTotalRent());
        calculationLabel.setText("(Calculating...)");
        cancelButton.setOnAction(event -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });

        confirmRentButton.setOnAction(event -> onConfirmRentButtonClicked());

    }

    private VehiclesInfo selectedVehicle;

    public void setCarDetails(VehiclesInfo vehicle) {
        this.selectedVehicle = vehicle;
        DecimalFormat df = new DecimalFormat("#,###.00");
        String formattedPrice = df.format(vehicle.getPrice());

        vehicleImage.setImage(new Image(vehicle.getImagePath()));
        vehicleNameLabel.setText(vehicle.getMake() + " " + vehicle.getModel());
        rentPriceLabel.setText("₱ " + formattedPrice);
        seatingCapacityLabel.setText(vehicle.getSeating() + " seats");

    }

    double totalRent = 0;
    private void calculateTotalRent() {
        LocalDate startDate = rentStartDatePicker.getValue();
        LocalDate endDate = rentEndDatePicker.getValue();

        if (startDate != null && endDate != null && !endDate.isBefore(startDate) && startDate != endDate) {
            long days = ChronoUnit.DAYS.between(startDate, endDate);
            totalRent = days * selectedVehicle.getPrice();
            if (totalRent == 0) {
                DecimalFormat df = new DecimalFormat("#,###.00");
                String formattedRentPrice = df.format(selectedVehicle.getPrice());
                totalRentAmountLabel.setText("₱ " + formattedRentPrice);
                calculationLabel.setText("(" + formattedRentPrice + " x 1 day)");
            } else {
                DecimalFormat df = new DecimalFormat("#,###.00");
                String formattedRentPrice = df.format(selectedVehicle.getPrice());
                String formattedTotal = df.format(totalRent);
                totalRentAmountLabel.setText("₱ " + formattedTotal);
                calculationLabel.setText("(" + formattedRentPrice + " x " + days + " days)");
            }
        } else {
            totalRentAmountLabel.setText("Calculating...");
            calculationLabel.setText("(Calculating...)");
        }
    }

    private void onConfirmRentButtonClicked() {
        int clientId = ClientSession.getInstance().getClientId();
        String carModel = selectedVehicle.getModel();
        String carColor = colorComboBox.getSelectionModel().getSelectedItem();
        LocalDate rentStartDate = rentStartDatePicker.getValue();
        LocalDate rentEndDate = rentEndDatePicker.getValue();
        String pickOffLocation = pickOffLocationField.getText();
        String paymentMethod = paymentMethodComboBox.getSelectionModel().getSelectedItem();
        double price = 0;
        if (totalRent == 0) {
            price = selectedVehicle.getPrice();
        } else {
            price = totalRent;
        }

        if (carColor.isEmpty() || rentStartDate == null || rentEndDate == null ||
                pickOffLocation.isEmpty() || paymentMethod == null) {
            showAlert(Alert.AlertType.ERROR, "Error","Please fill out all fields.");
            return;
        }

        if (rentStartDate.isAfter(rentEndDate)) {
            showAlert(Alert.AlertType.ERROR, "Error","Rent Start Date must be before Rent End Date.");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            String getCarIdQuery = "SELECT CarId FROM cars WHERE CarModel = ?";
            try (PreparedStatement carStmt = con.prepareStatement(getCarIdQuery)) {
                carStmt.setString(1, carModel);
                ResultSet carRs = carStmt.executeQuery();

                if (carRs.next()) {
                    int carId = carRs.getInt("CarId");

                    String insertTransactionQuery = """
                    INSERT INTO transaction (ClientId, CarId, CarColor, RentStartDate, RentEndDate, PickOffLocation, PaymentMethod, Price)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

                    try (PreparedStatement insertStmt = con.prepareStatement(insertTransactionQuery)) {
                        insertStmt.setInt(1, clientId);
                        insertStmt.setInt(2, carId);
                        insertStmt.setString(3, carColor);
                        insertStmt.setDate(4, Date.valueOf(rentStartDate));
                        insertStmt.setDate(5, Date.valueOf(rentEndDate));
                        insertStmt.setString(6, pickOffLocation);
                        insertStmt.setString(7, paymentMethod);
                        insertStmt.setDouble(8, price);

                        int rowsInserted = insertStmt.executeUpdate();
                        if (rowsInserted > 0) {
                            showAlert(Alert.AlertType.INFORMATION, "Successful","Car rented successfully!");

                            try (Connection connection1 = DBConnection.getConnection()) {
                                String sql1 = "SELECT Available FROM cars WHERE CarId = ?";
                                PreparedStatement statement1 = connection1.prepareStatement(sql1);
                                statement1.setInt(1, carId);
                                ResultSet resultSet1 = statement1.executeQuery();

                                if (resultSet1.next()) {
                                    int availableCount = resultSet1.getInt("Available");

                                    String sql2 = "UPDATE cars SET Available = ? WHERE CarId = ?";
                                    PreparedStatement statement2 = connection1.prepareStatement(sql2);
                                    statement2.setInt(1, availableCount-1);
                                    statement2.setInt(2, carId);
                                    statement2.executeUpdate();
                                } else {
                                    showAlert(Alert.AlertType.ERROR, "Error","Car not found.");
                                }
                            } catch (SQLException e) {
                                showAlert(Alert.AlertType.ERROR,"Error", "Failed to update vehicle information: " + e.getMessage());
                            }

                            Stage stage = (Stage) cancelButton.getScene().getWindow();
                            stage.close();
                        }
                        else {
                            showAlert(Alert.AlertType.ERROR, "Failed","Failed to create transaction.");
                        }
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error","Car not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error","Database error: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        Image icon = new Image(getClass().getResource("/Images/Logo.png").toString());

        alertStage.getIcons().add(icon);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
