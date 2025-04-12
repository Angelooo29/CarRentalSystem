package com.lester.carrentalsystem.Controller.Client;

import com.lester.carrentalsystem.Model.ClientSession;
import com.lester.carrentalsystem.Infrastructure.Data.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class TransactionController implements Initializable {

    @FXML
    private TextField carNameField;

    @FXML
    private DatePicker rentStartDatePicker;

    @FXML
    private DatePicker rentEndDatePicker;

    @FXML
    private TextField pickOffLocationField;

    @FXML
    private ComboBox<String> paymentMethodComboBox;

    @FXML
    private TextField rentPriceField;

    private VehiclesController vehiclesController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        paymentMethodComboBox.getItems().addAll("Cash", "Credit Card", "Debit Card");
    }

    public void setCarDetails(String carName, BigDecimal price) {
        carNameField.setText(carName);
        rentPriceField.setText(price.toString());
    }

    @FXML
    private void onConfirmRentClicked(ActionEvent event) {
        String carName = carNameField.getText();
        LocalDate rentStartDate = rentStartDatePicker.getValue();
        LocalDate rentEndDate = rentEndDatePicker.getValue();
        String pickOffLocation = pickOffLocationField.getText();
        String paymentMethod = paymentMethodComboBox.getSelectionModel().getSelectedItem();

        BigDecimal price;
        try {
            price = new BigDecimal(rentPriceField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid price value!");
            return;
        }

        if (carName.isEmpty() || rentStartDate == null || rentEndDate == null ||
                pickOffLocation.isEmpty() || paymentMethod == null) {
            showAlert(Alert.AlertType.ERROR, "Please fill all fields.");
            return;
        }

        if (rentStartDate.isAfter(rentEndDate)) {
            showAlert(Alert.AlertType.ERROR, "Rent start date must be before rent end date.");
            return;
        }

        int clientId = ClientSession.getInstance().getClientId();

        try (Connection con = DBConnection.getConnection()) {
            String getCarIdQuery = "SELECT CarId FROM cars WHERE CarName = ?";
            try (PreparedStatement carStmt = con.prepareStatement(getCarIdQuery)) {
                carStmt.setString(1, carName);
                ResultSet carRs = carStmt.executeQuery();

                if (carRs.next()) {
                    int carId = carRs.getInt("CarId");

                    String insertTransactionQuery = """
                    INSERT INTO transaction (ClientId, CarId, RentStartDate, RentEndDate, PickOffLocation, PaymentMethod, Price)
                    VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

                    try (PreparedStatement insertStmt = con.prepareStatement(insertTransactionQuery)) {
                        insertStmt.setInt(1, clientId);
                        insertStmt.setInt(2, carId);
                        insertStmt.setDate(3, Date.valueOf(rentStartDate));
                        insertStmt.setDate(4, Date.valueOf(rentEndDate));
                        insertStmt.setString(5, pickOffLocation);
                        insertStmt.setString(6, paymentMethod);
                        insertStmt.setBigDecimal(7, price);

                        int rowsInserted = insertStmt.executeUpdate();
                        if (rowsInserted > 0) {
                            showAlert(Alert.AlertType.INFORMATION, "Car rented successfully!");

                            vehiclesController.refreshAvailability();

                            Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                            currentStage.close();
                        }
                        else {
                            showAlert(Alert.AlertType.ERROR, "Failed to create transaction.");
                        }
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Car not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database error: " + e.getMessage());
        }
    }

    public void setVehiclesController(VehiclesController controller) {
        this.vehiclesController = controller;
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Transaction Status");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
