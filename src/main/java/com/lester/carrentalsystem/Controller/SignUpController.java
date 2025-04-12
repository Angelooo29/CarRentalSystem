package com.lester.carrentalsystem.Controller;

import com.lester.carrentalsystem.Infrastructure.Data.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignUpController {
    @FXML
    private TextField fullNameField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField contactNumberField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button signupButton;

    @FXML
    private ImageView driversLicenseImageView;
    @FXML
    private ImageView profilePictureImageView;

    private byte[] profilePictureData;
    private byte[] driversLicenseData;

    public void initialize() {
        signupButton.setOnMouseEntered(e -> signupButton.setStyle("-fx-background-color: #9145f5;"));
        signupButton.setOnMouseExited(e -> signupButton.setStyle("-fx-background-color:  #732bb5;"));
    }

    @FXML
    void onUploadDriversLicenseClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                driversLicenseData = Files.readAllBytes(selectedFile.toPath());
                driversLicenseImageView.setImage(new Image(selectedFile.toURI().toString()));
                showAlert(Alert.AlertType.INFORMATION, "Success", "Driver's license uploaded successfully!");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to upload driver's license: " + e.getMessage());
            }
        }
    }

    @FXML
    void onUploadProfilePictureClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                profilePictureData = Files.readAllBytes(selectedFile.toPath());
                profilePictureImageView.setImage(new Image(selectedFile.toURI().toString()));
                showAlert(Alert.AlertType.INFORMATION, "Success", "Profile picture uploaded successfully!");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to upload profile picture: " + e.getMessage());
            }
        }
    }

    @FXML
    void onSignupButtonClicked(ActionEvent event) {
        String fullName = fullNameField.getText();
        String contactNumber = contactNumberField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || contactNumber.isEmpty() || driversLicenseData == null || profilePictureData == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled out, including uploading both files!");
            return;
        }

        try (Connection connection = DBConnection.getConnection()) {
            String sql = "INSERT INTO users_client (Fullname, Username, Password, ContactNumber, `DriversLicense`, `Profile Picture`) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, fullName);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, contactNumber);
            preparedStatement.setBytes(5, driversLicenseData); // Store driver's license binary data
            preparedStatement.setBytes(6, profilePictureData); // Store profile picture binary data

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Sign-up successful!");
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Login.fxml"));
                    Parent root = loader.load();

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();

                    Stage currentStage = (Stage) signupButton.getScene().getWindow();
                    currentStage.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred: " + e.getMessage());
        }
    }

    @FXML
    void onBackHyperlinkClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Login.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Car Rental System - Log In");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) signupButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
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
