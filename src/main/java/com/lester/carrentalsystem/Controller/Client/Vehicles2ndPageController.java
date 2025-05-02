package com.lester.carrentalsystem.Controller.Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.math.BigDecimal;

public class Vehicles2ndPageController {
    @FXML
    private Button hcRentButton;
    @FXML
    private Button skRentButton;
    @FXML
    private Button mG4RentButton;

    @FXML
    void onRentButtonClicked(ActionEvent event) {
        Button source = (Button) event.getSource();
        String carName = "";
        BigDecimal price = BigDecimal.ZERO;

        switch (source.getId()) {
            case "hcRentButton":
                carName = "Honda Civic";
                price = new BigDecimal("2000.00");
                break;
            case "skRentButton":
                carName = "Suzuki Kizashi";
                price = new BigDecimal("2500.00");
                break;
            case "mG4RentButton":
                carName = "Mirage G4";
                price = new BigDecimal("3000.00");
                break;
        }
    }
}
