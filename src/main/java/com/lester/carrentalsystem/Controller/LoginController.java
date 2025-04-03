package com.lester.carrentalsystem.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.lester.carrentalsystem.Model.DBConnection;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    public void initialize() {
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if ("admin".equals(username) && "123".equals(password)) {
            showAlert("Login Success", "Welcome, admin!");
            return;
        }

        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE Username = ? AND Password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                showAlert("Login Success", "Welcome, " + username + "!");
            } else {
                showAlert("Login Failed", "Invalid username or password.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Unable to connect to the database.");
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
