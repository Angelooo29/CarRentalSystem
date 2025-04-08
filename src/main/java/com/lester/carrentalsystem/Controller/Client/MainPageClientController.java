package com.lester.carrentalsystem.Controller.Client;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Optional;


public class MainPageClientController {

    @FXML
    private AnchorPane contentArea;

    @FXML
    private Button homeButton;
    @FXML
    private Button vehiclesButton;
    @FXML
    private Button aboutButton;
    @FXML
    private Button logoutButton;

    @FXML
    private FontAwesomeIcon homeIcon;
    @FXML
    private FontAwesomeIcon aboutIcon;
    @FXML
    private FontAwesomeIcon logoutIcon;

    private static final String DEFAULT_BUTTON_STYLE = "-fx-background-color: white; -fx-text-fill: black;";
    private static final String ACTIVE_BUTTON_STYLE = "-fx-background-color: #732bb5; -fx-text-fill: white;";

    @FXML
    public void initialize() {
        onHomeButtonClicked(new ActionEvent());
    }

    @FXML
    void onHomeButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Client/Home.fxml"));
            Parent authenticationContent = loader.load();

            contentArea.getChildren().clear();
            contentArea.getChildren().add(authenticationContent);

            resetButtonStyles();
            homeButton.setStyle(ACTIVE_BUTTON_STYLE);
            homeIcon.getStyleClass().clear();
            homeIcon.getStyleClass().add("icon-white");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onVehiclesButtonClicked(ActionEvent event) {

    }

    @FXML
    void onAboutButtonClicked(ActionEvent event) {
        resetButtonStyles();
        aboutButton.setStyle(ACTIVE_BUTTON_STYLE);
        aboutIcon.getStyleClass().clear();
        aboutIcon.getStyleClass().add("icon-white");
    }

    @FXML
    void onLogoutButtonClicked(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("You're about to logout!");
        alert.setContentText("Are you sure you want to logout?");

        logoutButton.setStyle(ACTIVE_BUTTON_STYLE);
        logoutIcon.getStyleClass().clear();
        logoutIcon.getStyleClass().add("icon-white");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Login.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();

                Stage thisStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                thisStage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            logoutButton.setStyle(DEFAULT_BUTTON_STYLE);
            logoutIcon.getStyleClass().clear();
            logoutIcon.getStyleClass().add("icon-default");
        }
    }

    private void resetButtonStyles() {
        homeButton.setStyle(DEFAULT_BUTTON_STYLE);
        aboutButton.setStyle(DEFAULT_BUTTON_STYLE);



        homeIcon.getStyleClass().clear();
        homeIcon.getStyleClass().add("icon-default");
        aboutIcon.getStyleClass().clear();
        aboutIcon.getStyleClass().add("icon-default");
    }
}
