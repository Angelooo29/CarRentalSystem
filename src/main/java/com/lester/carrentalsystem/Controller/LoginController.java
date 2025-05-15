package com.lester.carrentalsystem.Controller;

import com.lester.carrentalsystem.Model.ClientSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginController {

    @FXML
    private Button closeButton;
    @FXML
    private Button minimizeButton;
    @FXML
    private CheckBox showPasswordCheckbox;
    @FXML
    private TextField visiblePasswordField;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private VBox vbox;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;

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

        usernameField.setOnKeyPressed(this::handleKeyPress);
        passwordField.setOnKeyPressed(this::handleKeyPress);

        loginButton.setOnMouseEntered(e -> loginButton.setStyle("-fx-background-color: #9145f5;"));
        loginButton.setOnMouseExited(e -> loginButton.setStyle("-fx-background-color: #732bb5;"));

        vbox.translateXProperty().bind(anchorPane.widthProperty().subtract(vbox.widthProperty()).divide(2));
        vbox.translateYProperty().bind(anchorPane.heightProperty().subtract(vbox.heightProperty()).divide(2));

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
            stage.initStyle(StageStyle.UNDECORATED);
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
        stage.initStyle(StageStyle.UNDECORATED);
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
