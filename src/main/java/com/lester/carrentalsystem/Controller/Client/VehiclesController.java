package com.lester.carrentalsystem.Controller.Client;

import com.lester.carrentalsystem.Infrastructure.Data.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VehiclesController {

    @FXML
    private Label rushAvailabilityLabel;
    @FXML
    private Label mgAvailabilityLabel;
    @FXML
    private Label explorerAvailabilityLabel;

    @FXML
    private Button rushRentButton;
    @FXML
    private Button mgRentButton;
    @FXML
    private Button explorerRentButton;

    public void initialize() {
        refreshAvailability();
    }

    public void refreshAvailability() {
        checkAndDisplayAvailability("Toyota Rush", rushAvailabilityLabel, rushRentButton);
        checkAndDisplayAvailability("MG ZS", mgAvailabilityLabel, mgRentButton);
        checkAndDisplayAvailability("Ford Explorer", explorerAvailabilityLabel, explorerRentButton);
    }

    private void checkAndDisplayAvailability(String carName, Label label, Button rentButton) {
        try (Connection con = DBConnection.getConnection()) {
            String query = """
            SELECT * FROM cars WHERE CarName = ? AND CarId NOT IN (
                SELECT CarId FROM transaction WHERE RentEndDate >= CURRENT_DATE
            )
        """;
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, carName);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    label.setText("Available");
                    label.setStyle("-fx-text-fill: green;");
                    rentButton.setDisable(false);
                } else {
                    label.setText("Not Available");
                    label.setStyle("-fx-text-fill: red;");
                    rentButton.setDisable(true);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            label.setText("Error");
            label.setStyle("-fx-text-fill: orange;");
            rentButton.setDisable(true);
        }
    }

    @FXML
    void onRentButtonClicked(ActionEvent event) {
        Button source = (Button) event.getSource();
        String carName = "";
        BigDecimal price = BigDecimal.ZERO;

        switch (source.getId()) {
            case "rushRentButton":
                carName = "Toyota Rush";
                price = new BigDecimal("2000.00");
                break;
            case "mgRentButton":
                carName = "MG ZS";
                price = new BigDecimal("2500.00");
                break;
            case "explorerRentButton":
                carName = "Ford Explorer";
                price = new BigDecimal("3000.00");
                break;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Client/Transaction.fxml"));
            Parent root = loader.load();

            TransactionController controller = loader.getController();
            controller.setCarDetails(carName, price);
            controller.setVehiclesController(this);

            Stage stage = new Stage();
            stage.setTitle("Car Rental System - Transaction");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
