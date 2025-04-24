package com.lester.carrentalsystem.Controller.Client;

import com.lester.carrentalsystem.Infrastructure.Data.DBConnection;
import com.lester.carrentalsystem.Model.ClientSession;
import com.sun.tools.javac.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditUserProfileController {

    @FXML
    private TextField clientIdEdit;

    @FXML
    private TextField fullNameEdit;

    @FXML
    private TextField usernameEdit;

    @FXML
    private TextField passwordEdit;

    @FXML
    private TextField contactNumberEdit;

    @FXML
    private Button saveChangesEdit;

    @FXML
    private void initialize() {
        loadUserData();
        saveChangesEdit.setOnMouseEntered(e -> saveChangesEdit.setStyle("-fx-background-color: #9145f5;"));
        saveChangesEdit.setOnMouseExited(e -> saveChangesEdit.setStyle("-fx-background-color:  #732bb6;"));
    }

    @FXML
    private void loadUserData() {
        int clientId = ClientSession.getInstance().getClientId();
        clientIdEdit.setText(String.valueOf(clientId));

        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT Fullname, Username, Password, ContactNumber, ProfilePicture, DriversLicense FROM users_client WHERE ClientId = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, clientId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                fullNameEdit.setText(resultSet.getString("Fullname"));
                usernameEdit.setText(resultSet.getString("Username"));
                passwordEdit.setText(resultSet.getString("Password"));
                contactNumberEdit.setText(resultSet.getString("ContactNumber"));
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load client data: " + e.getMessage());
        }
    }

    @FXML
    private void onSaveChangesEditClicked(ActionEvent actionEvent) {
        String fullName = fullNameEdit.getText();
        String username = usernameEdit.getText();
        String password = passwordEdit.getText();
        String contactNumber = contactNumberEdit.getText();

        int clientId = ClientSession.getInstance().getClientId();

        try (Connection connection = DBConnection.getConnection()) {
            String sql = "UPDATE users_client SET Fullname = ?, Username = ?, Password = ?, ContactNumber = ? WHERE ClientId = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, fullName);
            statement.setString(2, username);
            statement.setString(3, password);
            statement.setString(4, contactNumber);
            statement.setInt(5, clientId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "User Profile updated successfully!\n\nPlease log in again to apply the changes to your account.\n\n");
                Stage currentStage = (Stage) saveChangesEdit.getScene().getWindow();
                currentStage.close();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update client information: " + e.getMessage());
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
