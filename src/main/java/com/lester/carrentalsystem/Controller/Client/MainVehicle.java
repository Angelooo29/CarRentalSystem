/*package com.lester.carrentalsystem.Controller.Client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.CheckBox;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainVehicle {

    @FXML private GridPane gridPane;
    @FXML private ScrollPane scrollPane;
    @FXML private ComboBox<String> filterComboBox;
    @FXML private Slider priceSlider;
    @FXML private CheckBox availabilityCheckbox;

    private List<VehiclesInfo> vehicleList = new ArrayList<>();
    private final int maxColumns = 3;

    public void initialize() {
        loadVehicles(); // Populate vehicles when scene loads

        // Apply filter when options change
        filterComboBox.setOnAction(e -> applyFilters());
        priceSlider.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        availabilityCheckbox.setOnAction(e -> applyFilters());

    }

    private void loadVehicles() {
        vehicleList.add(new VehiclesInfo("Toyota Camry", "images/NissanRouge.png", 50, true));
        vehicleList.add(new VehiclesInfo("Ford Mustang", "images/NissanRouge.png", 75, false));
        vehicleList.add(new VehiclesInfo("Honda Civic", "images/car_1.png", 40, true));
        vehicleList.add(new VehiclesInfo("Honda Civic", "images/car_2.png", 80, true));
        vehicleList.add(new VehiclesInfo("Honda Civic", "images/car_3.png", 90, true));
        vehicleList.add(new VehiclesInfo("Honda Civic", "images/NissanRouge.png", 100, true));
        vehicleList.add(new VehiclesInfo("Honda Civic", "images/NissanRouge.png", 110, true));
        vehicleList.add(new VehiclesInfo("Honda Civic", "images/NissanRouge.png", 120, true));
        vehicleList.add(new VehiclesInfo("Honda Civic", "images/NissanRouge.png", 130, true));
        vehicleList.add(new VehiclesInfo("Honda Civic", "images/NissanRouge.png", 140, true));

        applyFilters(); // Load vehicles initially
    }

    private void applyFilters() {
        //gridPane.getChildren().clear(); // Clear previous content

        List<VehiclesInfo> filteredList = vehicleList.stream()
                .filter(v -> v.getPrice() <= priceSlider.getValue()) // Filter by price
                .filter(v -> !availabilityCheckbox.isSelected() || v.isAvailable()) // Availability filter
                .collect(Collectors.toList());

        displayVehicles(filteredList); // Update UI
    }

    private void displayVehicles(List<VehiclesInfo> vehicles) {
        int rowIndex = 0, colIndex = 0;

        for (VehiclesInfo v : vehicles) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Client/VehicleCard.fxml"));
                VBox vehicleCard = loader.load();

                VehicleCardController controller = loader.getController();
                controller.setVehicleData(v); // Pass data to card

                gridPane.add(vehicleCard, colIndex, rowIndex);

                colIndex++;
                if (colIndex == maxColumns) {
                    colIndex = 0;
                    rowIndex++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public void setScrollPane(ScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }
}*/