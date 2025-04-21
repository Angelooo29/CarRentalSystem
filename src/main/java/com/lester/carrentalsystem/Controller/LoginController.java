package com.lester.carrentalsystem.Controller;

import com.lester.carrentalsystem.Model.ClientSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.lester.carrentalsystem.Infrastructure.Data.DBConnection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    public void initialize() {
        usernameField.setOnKeyPressed(this::handleKeyPress);
        passwordField.setOnKeyPressed(this::handleKeyPress);

        loginButton.setOnMouseEntered(e -> loginButton.setStyle("-fx-background-color: #9145f5;"));
        loginButton.setOnMouseExited(e -> loginButton.setStyle("-fx-background-color:  #732bb5;"));
    }

    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            onLoginButtonClicked(new ActionEvent(loginButton, null));
        }
    }

    @FXML
    private void onLoginButtonClicked(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT ClientId, Fullname, Username, ContactNumber FROM users_client WHERE Username = ? AND Password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int userId = resultSet.getInt("ClientId");
                String fullName = resultSet.getString("Fullname");
                String contactNumber = resultSet.getString("ContactNumber");

                ClientSession.getInstance().setClientData(userId, fullName, username, contactNumber);
                loadMainPage();
            } else {
                showAlert("Login Failed", "Invalid Username or Password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Unable to connect to the database.");
        }

    }

    private void loadMainPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Client/MainPageClient.fxml"));
            Parent root = loader.load();
            Image icon = new Image(getClass().getResource("/Images/Logo.png").toString());

            Stage stage = new Stage();
            stage.setTitle("Car Rental System");
            stage.getIcons().add(icon);
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Unable to load the main page.");
        }
    }

    @FXML
    private void onCreateAccountClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/SignUp.fxml"));
        Parent root = loader.load();
        Image icon = new Image(getClass().getResource("/Images/Logo.png").toString());

        Stage stage = new Stage();
        stage.setTitle("Car Rental System - Sign Up");
        stage.getIcons().add(icon);
        stage.setScene(new Scene(root));
        stage.show();

        Stage currentStage = (Stage) loginButton.getScene().getWindow();
        currentStage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        Image icon = new Image(getClass().getResource("/Images/Logo.png").toString());

        alertStage.getIcons().add(icon);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
