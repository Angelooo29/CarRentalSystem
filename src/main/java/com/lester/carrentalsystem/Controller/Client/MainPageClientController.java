package com.lester.carrentalsystem.Controller.Client;

import com.lester.carrentalsystem.App;
import com.lester.carrentalsystem.Infrastructure.Data.DBConnection;
import com.lester.carrentalsystem.Model.ClientSession;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;


public class MainPageClientController {

    @FXML
    public static Label welcomeLabel;
    @FXML
    public Label fullName;

    @FXML
    private AnchorPane contentArea;

    @FXML
    private Button homeButton;
    @FXML
    private Button vehiclesButton;
    @FXML
    private Button aboutButton;
    @FXML
    private Button contactsButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Button logoutButton;

    @FXML
    private FontAwesomeIcon homeIcon;
    @FXML
    private FontAwesomeIcon vehiclesIcon;
    @FXML
    private FontAwesomeIcon aboutIcon;
    @FXML
    private FontAwesomeIcon contactsIcon;
    @FXML
    private FontAwesomeIcon settingsIcon;
    @FXML
    private FontAwesomeIcon logoutIcon;

    private static final String DEFAULT_BUTTON_STYLE = "-fx-background-color: white; -fx-text-fill: #732bb5;";
    private static final String ACTIVE_BUTTON_STYLE = "-fx-background-color: #732bb5; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 21px;";

    private Pair<Button, FontAwesomeIcon> lastActive;

    public void setWelcomeUser() {
        int clientId = ClientSession.getInstance().getClientId();
        fullName.setText("");

        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT Fullname, Username, Password, ContactNumber, ProfilePicture, DriversLicense FROM users_client WHERE ClientId = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, clientId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String clientFullName = resultSet.getString("Fullname");

                if (clientFullName.length() > 20) {
                    fullName.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
                } else if (clientFullName.length() > 10) {
                    fullName.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
                } else {
                    fullName.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
                }

                fullName.setText(clientFullName);
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load client data: " + e.getMessage());
        }

        fullName.setAlignment(Pos.CENTER);
        fullName.setTextAlignment(TextAlignment.CENTER);
        fullName.setText(fullName.getText() + "!");
    }

    @FXML
    public void initialize() {
        onHomeButtonClicked(new ActionEvent());
        setWelcomeUser();
    }

    @FXML
    private void buttonAnimation(Button button) {
        homeButton.setOnMouseEntered(e -> homeButton.setStyle("-fx-background-color: #E8D6FF;"));
        homeButton.setOnMouseExited(e -> homeButton.setStyle(DEFAULT_BUTTON_STYLE));

        vehiclesButton.setOnMouseEntered(e -> vehiclesButton.setStyle("-fx-background-color: #E8D6FF;"));
        vehiclesButton.setOnMouseExited(e -> vehiclesButton.setStyle(DEFAULT_BUTTON_STYLE));

        aboutButton.setOnMouseEntered(e -> aboutButton.setStyle("-fx-background-color: #E8D6FF;"));
        aboutButton.setOnMouseExited(e -> aboutButton.setStyle(DEFAULT_BUTTON_STYLE));

        contactsButton.setOnMouseEntered(e -> contactsButton.setStyle("-fx-background-color: #E8D6FF;"));
        contactsButton.setOnMouseExited(e -> contactsButton.setStyle(DEFAULT_BUTTON_STYLE));

        settingsButton.setOnMouseEntered(e -> settingsButton.setStyle("-fx-background-color: #E8D6FF;"));
        settingsButton.setOnMouseExited(e -> settingsButton.setStyle(DEFAULT_BUTTON_STYLE));

        logoutButton.setOnMouseEntered(e -> logoutButton.setStyle("-fx-background-color: #E8D6FF;"));
        logoutButton.setOnMouseExited(e -> logoutButton.setStyle(DEFAULT_BUTTON_STYLE));

        button.setOnMouseEntered(e -> button.setStyle(ACTIVE_BUTTON_STYLE));
        button.setOnMouseExited(e -> button.setStyle(ACTIVE_BUTTON_STYLE));
    }

    @FXML
    void onHomeButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Client/Home.fxml"));
            Parent authenticationContent = loader.load();

            contentArea.getChildren().clear();
            contentArea.getChildren().add(authenticationContent);

            resetButtonStyles();
            buttonAnimation(homeButton);
            homeButton.setStyle(ACTIVE_BUTTON_STYLE);
            homeIcon.getStyleClass().clear();
            homeIcon.getStyleClass().add("icon-white");
            lastActive = new Pair<>(homeButton, homeIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    void onVehiclesButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Client/VehiclesList.fxml"));
            Parent authenticationContent = loader.load();

            contentArea.getChildren().clear();
            contentArea.getChildren().add(authenticationContent);

            resetButtonStyles();
            buttonAnimation(vehiclesButton);
            vehiclesButton.setStyle(ACTIVE_BUTTON_STYLE);
            vehiclesIcon.getStyleClass().clear();
            vehiclesIcon.getStyleClass().add("icon-white");
            lastActive = new Pair<>(vehiclesButton, vehiclesIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onAboutButtonClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Client/About.fxml"));
        Parent authenticationContent = loader.load();

        contentArea.getChildren().clear();
        contentArea.getChildren().add(authenticationContent);
        resetButtonStyles();
        buttonAnimation(aboutButton);
        aboutButton.setStyle(ACTIVE_BUTTON_STYLE);
        aboutIcon.getStyleClass().clear();
        aboutIcon.getStyleClass().add("icon-white");
        lastActive = new Pair<>(aboutButton, aboutIcon);
    }

    @FXML
    void onContactsButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Client/Contacts.fxml"));
            Parent authenticationContent = loader.load();

            contentArea.getChildren().clear();
            contentArea.getChildren().add(authenticationContent);

            resetButtonStyles();
            buttonAnimation(contactsButton);
            contactsButton.setStyle(ACTIVE_BUTTON_STYLE);
            contactsIcon.getStyleClass().clear();
            contactsIcon.getStyleClass().add("icon-white");
            lastActive = new Pair<>(contactsButton, contactsIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onSettingsButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Client/Settings.fxml"));
            Parent authenticationContent = loader.load();

            contentArea.getChildren().clear();
            contentArea.getChildren().add(authenticationContent);

            resetButtonStyles();
            buttonAnimation(settingsButton);
            settingsButton.setStyle(ACTIVE_BUTTON_STYLE);
            settingsIcon.getStyleClass().clear();
            settingsIcon.getStyleClass().add("icon-white");
            lastActive = new Pair<>(settingsButton, settingsIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onLogoutButtonClicked(ActionEvent event) {
        Pair<Button, FontAwesomeIcon> previousActive = lastActive;

        resetButtonStyles();
        buttonAnimation(logoutButton);
        logoutButton.setStyle(ACTIVE_BUTTON_STYLE);
        logoutIcon.getStyleClass().clear();
        logoutIcon.getStyleClass().add("icon-white");

        Image icon = new Image(getClass().getResource("/Images/Logo.png").toString());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();

        alertStage.getIcons().add(icon);
        alert.setTitle("Confirm Logout");
        alert.setHeaderText("Are you sure you want to log out?");
        alert.setContentText("Click <OK> to proceed.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage currentStage = (Stage) logoutButton.getScene().getWindow();
            currentStage.close();

            try {
                App app = new App();
                app.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to navigate back to the main application.");
            }

        } else {
            logoutButton.setStyle(DEFAULT_BUTTON_STYLE);
            logoutIcon.getStyleClass().clear();
            logoutIcon.getStyleClass().add("icon-default");

            if (previousActive != null) {
                Button lastButton = previousActive.getKey();
                FontAwesomeIcon lastIcon = previousActive.getValue();
                lastButton.setStyle(ACTIVE_BUTTON_STYLE);
                lastIcon.getStyleClass().clear();
                lastIcon.getStyleClass().add("icon-white");
                buttonAnimation(lastButton);
            }
        }
    }

    private void resetButtonStyles() {
        homeButton.setStyle(DEFAULT_BUTTON_STYLE);
        aboutButton.setStyle(DEFAULT_BUTTON_STYLE);
        vehiclesButton.setStyle(DEFAULT_BUTTON_STYLE);
        contactsButton.setStyle(DEFAULT_BUTTON_STYLE);
        settingsButton.setStyle(DEFAULT_BUTTON_STYLE);
        logoutButton.setStyle(DEFAULT_BUTTON_STYLE);

        homeIcon.getStyleClass().clear();
        homeIcon.getStyleClass().add("icon-default");
        aboutIcon.getStyleClass().clear();
        aboutIcon.getStyleClass().add("icon-default");
        vehiclesIcon.getStyleClass().clear();
        vehiclesIcon.getStyleClass().add("icon-default");
        contactsIcon.getStyleClass().clear();
        contactsIcon.getStyleClass().add("icon-default");
        settingsIcon.getStyleClass().clear();
        settingsIcon.getStyleClass().add("icon-default");
        logoutIcon.getStyleClass().clear();
        logoutIcon.getStyleClass().add("icon-default");
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
