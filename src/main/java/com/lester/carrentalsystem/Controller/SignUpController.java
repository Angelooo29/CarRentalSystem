package com.lester.carrentalsystem.Controller;

import com.lester.carrentalsystem.Infrastructure.Data.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignUpController {
    @FXML
    private Button closeButton;
    @FXML
    private Button minimizeButton;
    @FXML
    private CheckBox showPasswordCheckbox;
    @FXML
    private TextField visiblePasswordField;
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
        closeButton.setOnAction(event -> {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });

        closeButton.setOnMouseEntered(e -> closeButton.setStyle("-fx-background-color: #ff3535;"));
        closeButton.setOnMouseExited(e -> closeButton.setStyle("-fx-background-color:  #a10101;"));

        minimizeButton.setOnAction(event -> {
            Stage stage = (Stage) minimizeButton.getScene().getWindow();
            stage.setIconified(true);
        });

        signupButton.setOnMouseEntered(e -> signupButton.setStyle("-fx-background-color: #9145f5;"));
        signupButton.setOnMouseExited(e -> signupButton.setStyle("-fx-background-color:  #732bb5;"));

        showPasswordCheckbox.setOnAction(event -> {
            if (showPasswordCheckbox.isSelected()) {
                visiblePasswordField.setText(passwordField.getText());
                visiblePasswordField.setVisible(true);
                passwordField.setVisible(false);
            } else {
                passwordField.setText(visiblePasswordField.getText());
                passwordField.setVisible(true);
                visiblePasswordField.setVisible(false);
            }
        });

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!showPasswordCheckbox.isSelected()) {
                visiblePasswordField.setText(newValue);
            }
        });

        visiblePasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (showPasswordCheckbox.isSelected()) {
                passwordField.setText(newValue);
            }
        });

        visiblePasswordField.setVisible(false);
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
                showAlert(Alert.AlertType.INFORMATION, "Success", "Driver's License uploaded successfully!");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to upload Driver's License: " + e.getMessage());
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
                showAlert(Alert.AlertType.INFORMATION, "Success", "Profile Picture uploaded successfully!");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to upload Profile Picture: " + e.getMessage());
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
            showAlert(Alert.AlertType.ERROR, "Form Submission Error",
                    "All fields are mandatory. \n\nPlease provide all the required information, including your Profile Picture and Driver's License, to complete the sign-up process.");
            return;
        }

        try (Connection connection = DBConnection.getConnection()) {
            String sql = "INSERT INTO users_client (Fullname, Username, Password, ContactNumber, `DriversLicense`, `ProfilePicture`) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, fullName);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, contactNumber);
            preparedStatement.setBytes(5, driversLicenseData);
            preparedStatement.setBytes(6, profilePictureData);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Account Created Successfully!");
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Login.fxml"));
                    Parent root = loader.load();
                    Image icon = new Image(getClass().getResource("/Images/Logo.png").toString());

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.getIcons().add(icon);
                    stage.initStyle(StageStyle.UNDECORATED);
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
            Image icon = new Image(getClass().getResource("/Images/Logo.png").toString());

            Stage stage = new Stage();
            stage.setTitle("Car Rental System - Log In");
            stage.getIcons().add(icon);
            stage.initStyle(StageStyle.UNDECORATED);
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
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        Image icon = new Image(getClass().getResource("/Images/Logo.png").toString());

        alertStage.getIcons().add(icon);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
