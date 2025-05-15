package com.lester.carrentalsystem.Controller.Client;

import com.lester.carrentalsystem.Infrastructure.Data.DBConnection;
import com.lester.carrentalsystem.Model.ClientSession;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CancelTransactionController {

    @FXML private ComboBox<String> cancelReasonComboBox;
    @FXML private TextArea messageTextArea;
    @FXML private Button confirmButton;
    @FXML private Button goBackButton;

    public void initialize() {
        cancelReasonComboBox.getItems().addAll("Change of Plans", "Found a Better Deal", "Vehicle Not Needed Anymore","Unexpected Travel Changes",
                "Personal Emergency","Payment Issues","Weather Conditions","Rental Policy Concerns","Vehicle Availability Issues","Other");
        confirmButton.setOnMouseEntered(e -> confirmButton.setStyle("-fx-background-color: #ff3535;"));
        confirmButton.setOnMouseExited(e -> confirmButton.setStyle("-fx-background-color: #c80000;"));
        confirmButton.setOnAction(event -> onConfirmButtonClicked());
        goBackButton.setOnAction(event -> {
            Stage stage = (Stage) goBackButton.getScene().getWindow();
            stage.close();
        });
    }

    public void onConfirmButtonClicked() {
        if (cancelReasonComboBox.getValue() == null || cancelReasonComboBox.getValue().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please select a reason for cancellation.");
            return;
        }

        if (messageTextArea.getText() == null || messageTextArea.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please provide additional details for cancellation.");
            return;
        }

        int transactionId = ClientSession.getSelectedTransactionId();

        if (transactionId == 0) {
            showAlert(Alert.AlertType.WARNING, "Error", "No transaction selected.");
            return;
        }

        try (Connection connection = DBConnection.getConnection()) {
            int carId = -1;
            int available = 0;

            try (PreparedStatement statement2 = connection.prepareStatement("SELECT c.CarId, c.Available FROM transaction t JOIN cars c ON t.CarId = c.CarId WHERE t.TransactionId = ?")) {
                statement2.setInt(1, transactionId);

                try (ResultSet resultSet2 = statement2.executeQuery()) {
                    if (resultSet2.next()) {
                        carId = resultSet2.getInt("CarId");
                        available = resultSet2.getInt("Available");
                    }
                }
            }

            if (carId != -1) {
                try (PreparedStatement statement3 = connection.prepareStatement("UPDATE cars SET Available = ? WHERE CarId = ?")) {
                    statement3.setInt(1, available + 1);
                    statement3.setInt(2, carId);
                    statement3.executeUpdate();
                }
            }

            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM transaction WHERE TransactionId = ?")) {
                statement.setInt(1, transactionId);
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    Image icon = new Image(getClass().getResource("/Images/Logo.png").toString());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Cancellation Approved");
                    alert.setHeaderText("Cancellation request approved. Transaction has been cancelled.");
                    alert.setContentText("Click <OK> to proceed.");
                    Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                    alertStage.getIcons().add(icon);

                    alert.setOnHidden(event -> {
                        Stage currentStage = (Stage) confirmButton.getScene().getWindow();
                        currentStage.close();
                    });

                    alert.showAndWait();
                } else {
                    showAlert(Alert.AlertType.WARNING, "Warning", "Transaction not found.");
                }
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to cancel transaction: " + e.getMessage());
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
