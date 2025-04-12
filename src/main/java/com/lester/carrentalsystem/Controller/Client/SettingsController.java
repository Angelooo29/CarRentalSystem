package com.lester.carrentalsystem.Controller.Client;

import com.lester.carrentalsystem.Infrastructure.Data.DBConnection;
import com.lester.carrentalsystem.Model.ClientSession;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SettingsController {

    @FXML
    private TextField clientIdField;

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField contactNumberField;

    @FXML
    private ImageView profilePictureView;

    @FXML
    private ImageView driversLicenseView;

    private byte[] profilePictureData;
    private byte[] driversLicenseData;

    public void initialize() {
        loadClientData();
    }

    private void loadClientData() {
        int clientId = ClientSession.getInstance().getClientId();
        clientIdField.setText(String.valueOf(clientId));

        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT Fullname, Username, Password, ContactNumber, ProfilePicture, DriversLicense FROM users_client WHERE ClientId = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, clientId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                fullNameField.setText(resultSet.getString("Fullname"));
                usernameField.setText(resultSet.getString("Username"));
                passwordField.setText(resultSet.getString("Password"));
                contactNumberField.setText(resultSet.getString("ContactNumber"));

                byte[] profilePicture = resultSet.getBytes("ProfilePicture");
                byte[] driversLicense = resultSet.getBytes("DriversLicense");

                if (profilePicture != null) {
                    profilePictureView.setImage(new Image(new java.io.ByteArrayInputStream(profilePicture)));
                }
                if (driversLicense != null) {
                    driversLicenseView.setImage(new Image(new java.io.ByteArrayInputStream(driversLicense)));
                }
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load client data: " + e.getMessage());
        }
    }

    @FXML
    private void onChangeProfilePictureClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                profilePictureData = Files.readAllBytes(selectedFile.toPath());
                profilePictureView.setImage(new Image(selectedFile.toURI().toString()));
            } catch (Exception e) {
                showAlert("Error", "Failed to upload profile picture: " + e.getMessage());
            }
        }
    }

    @FXML
    private void onChangeDriversLicenseClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                driversLicenseData = Files.readAllBytes(selectedFile.toPath());
                driversLicenseView.setImage(new Image(selectedFile.toURI().toString()));
            } catch (Exception e) {
                showAlert("Error", "Failed to upload driver's license: " + e.getMessage());
            }
        }
    }

    @FXML
    private void onSaveChangesClicked() {
        String fullName = fullNameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String contactNumber = contactNumberField.getText();

        int clientId = ClientSession.getInstance().getClientId();

        try (Connection connection = DBConnection.getConnection()) {
            String sql = "UPDATE users_client SET Fullname = ?, Username = ?, Password = ?, ContactNumber = ?, ProfilePicture = ?, DriversLicense = ? WHERE ClientId = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, fullName);
            statement.setString(2, username);
            statement.setString(3, password);
            statement.setString(4, contactNumber);
            statement.setBytes(5, profilePictureData);
            statement.setBytes(6, driversLicenseData);
            statement.setInt(7, clientId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                showAlert("Success", "Information updated successfully!");
            }
        } catch (SQLException e) {
            showAlert("Error", "Failed to update client information: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
