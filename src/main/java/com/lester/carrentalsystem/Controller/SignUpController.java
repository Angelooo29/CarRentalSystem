package com.lester.carrentalsystem.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpController {
    @FXML
    private TextField fullNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button signupButton;

    @FXML
    private Hyperlink loginLink;

    @FXML
    public void initialize() {
        // Initialize UI components if needed
    }

    @FXML
    public void handleSignup(ActionEvent event) {
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validation
        if (fullName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Signup Error", "Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Signup Error", "Passwords do not match.");
            return;
        }

        // Here you would add code to save the user to your database
        // For this example, we'll just show a success message

        showAlert(Alert.AlertType.INFORMATION, "Signup Successful",
                "Your account has been created successfully. You can now login.");

        // Navigate back to login screen
        navigateToLogin();
    }

    @FXML
    public void handleLoginLink(ActionEvent event) {
        navigateToLogin();
    }

    private void navigateToLogin() {
        try {
            // Load the login view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Login.fxml"));
            Parent loginView = loader.load();

            // Get stage from login link
            Stage stage = (Stage) loginLink.getScene().getWindow();

            // Set the new scene
            stage.setScene(new Scene(loginView));
            stage.setTitle("Car Rental System - Login");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load login view.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
