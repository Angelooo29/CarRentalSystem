package com.lester.carrentalsystem.Controller;

import com.lester.carrentalsystem.Controller.Client.MainPageClientController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.lester.carrentalsystem.Model.DBConnection;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    public void initialize() {loginButton.setDefaultButton(true);}

    @FXML
    private void onLoginButtonClicked(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT * FROM users_client WHERE Username = ? AND Password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String fullName = resultSet.getString("Fullname");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Client/MainPageClient.fxml"));
                Parent root = loader.load();

                MainPageClientController mainController = loader.getController();
                mainController.setWelcomeData(fullName);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();


                Stage currentStage = (Stage) loginButton.getScene().getWindow();
                currentStage.close();
            } else {
                showAlert("Login Failed", "Invalid username or password.");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while processing your request.");
        }
    }


    @FXML
    private void onCreateAccountClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/SignUp.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

        Stage currentStage = (Stage) loginButton.getScene().getWindow();
        currentStage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
