package com.lester.carrentalsystem.Controller.Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.math.BigDecimal;

public class Vehicles3rdPageController {
    @FXML
    private Button tHiAceRentButton;
    @FXML
    private Button hH1RentButton;
    @FXML
    private Button nNUrvanRentButton;

    @FXML
    void onRentButtonClicked(ActionEvent event) {
        Button source = (Button) event.getSource();
        String carName = "";
        BigDecimal price = BigDecimal.ZERO;

        switch (source.getId()) {
            case "tHiAceRentButton":
                carName = "Toyota HiAce";
                price = new BigDecimal("3500.00");
                break;
            case "hH1RentButton":
                carName = "Hyundai H-1";
                price = new BigDecimal("3000.00");
                break;
            case "nNUrvanRentButton":
                carName = "Nissan NV350 Urvan";
                price = new BigDecimal("3500.00");
                break;
        }
    }
}

