package com.lester.carrentalsystem.Controller.Client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ContactsController {
    @FXML
    private ImageView contactImage;
    @FXML
    private Button closeButton;
    @FXML
    private Button minimizeButton;

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
    }
}
