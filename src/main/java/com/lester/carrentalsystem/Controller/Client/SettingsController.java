package com.lester.carrentalsystem.Controller.Client;

import com.lester.carrentalsystem.App;
import com.lester.carrentalsystem.Infrastructure.Data.DBConnection;
import com.lester.carrentalsystem.Model.ClientSession;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class SettingsController {

    @FXML
    public ListView listView;

    @FXML
    public ImageView vehicleImage;

    @FXML
    public Label vehicleNameLabel;

    @FXML
    public Label priceLabel;

    @FXML
    public Label startDateLabel;

    @FXML
    public Label EndDateLabel;

    @FXML
    public Label locationLabel;

    @FXML
    public Label paymentLabel;

    @FXML
    private TextField clientIdField;

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField contactNumberField;

    @FXML
    private ImageView profilePictureView;

    @FXML
    private ImageView driversLicenseView;

    @FXML
    private Button saveButton;

    @FXML
    public Button editButton;

    private byte[] profilePictureData;
    private byte[] driversLicenseData;

    public void initialize() {
        loadClientData();
        rentalHistory();
        saveButton.setOnMouseEntered(e -> saveButton.setStyle("-fx-background-color: #9145f5;"));
        saveButton.setOnMouseExited(e -> saveButton.setStyle("-fx-background-color:  #732bb5;"));
        editButton.setOnMouseEntered(e -> editButton.setStyle("-fx-background-color: #9145f5;"));
        editButton.setOnMouseExited(e -> editButton.setStyle("-fx-background-color:  #732bb5;"));
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
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load client data: " + e.getMessage());
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
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to upload profile picture: " + e.getMessage());
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
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to upload driver's license: " + e.getMessage());
            }
        }
    }

    @FXML
    private void onSaveChangesClicked() {
        int clientId = ClientSession.getInstance().getClientId();

        try (Connection connection = DBConnection.getConnection()) {
            String sql = "UPDATE users_client SET ProfilePicture = ?, DriversLicense = ? WHERE ClientId = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setBytes(1, profilePictureData);
            statement.setBytes(2, driversLicenseData);
            statement.setInt(3, clientId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Information updated successfully!");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR,"Error", "Failed to update client information: " + e.getMessage());
        }
    }

    @FXML
    private void onEditButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Client/EditUserProfile.fxml"));
            Parent root = loader.load();
            Image icon = new Image(getClass().getResource("/Images/Logo.png").toString());

            Stage stage = new Stage();
            stage.setTitle("Car Rental System - Edit User Profile");
            stage.setScene(new Scene(root));
            stage.getIcons().add(icon);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void rentalHistory() {
        int clientId = ClientSession.getInstance().getClientId();
        ObservableList<String> rentHistory = FXCollections.observableArrayList();
        Map<String, Integer> rentDateToCarIdMap = new HashMap<>();

        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT CarId, RentStartDate, RentEndDate, PickOffLocation, PaymentMethod, Price, TransactionDate FROM transaction WHERE ClientId = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, clientId);
            ResultSet resultSet = statement.executeQuery();

            SimpleDateFormat formatter = new SimpleDateFormat("MMM. dd, yyyy");
            while (resultSet.next()) {
                int carId = resultSet.getInt("CarId");
                String formattedDate = formatter.format(resultSet.getDate("RentStartDate"));

                rentHistory.add(formattedDate);
                rentDateToCarIdMap.put(formattedDate, carId);
            }

            listView.setItems(rentHistory);
            listView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                if (newValue != null) {
                    int selectedCarId = rentDateToCarIdMap.get(newValue);
                    String imagePath = "/images/car_" + selectedCarId + ".png";
                    Image image = new Image(getClass().getResource(imagePath).toExternalForm());
                    vehicleImage.setImage(image);
                }
            });

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load client data: " + e.getMessage());
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
