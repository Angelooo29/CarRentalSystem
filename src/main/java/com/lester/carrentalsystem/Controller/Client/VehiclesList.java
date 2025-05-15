package com.lester.carrentalsystem.Controller.Client;

import com.lester.carrentalsystem.Infrastructure.Data.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VehiclesList {

    @FXML
    private Button closeButton;
    @FXML
    private Button minimizeButton;
    @FXML
    private Rectangle overlayEffects;
    @FXML
    private RadioButton allRadio;

    @FXML
    private RadioButton availableRadio;

    @FXML
    private RadioButton notAvailableRadio;

    @FXML
    private ToggleGroup available;

    @FXML
    private ComboBox<String> filterType;

    @FXML
    private ComboBox<String> filterItem;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private GridPane gridPane;

    private List<VehiclesInfo> vehicleList = new ArrayList<>();
    private final int MAX_COLUMNS = 3;

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

        available = new ToggleGroup();
        allRadio.setToggleGroup(available);
        availableRadio.setToggleGroup(available);
        notAvailableRadio.setToggleGroup(available);
        allRadio.setSelected(true);

        loadVehicles();
        updateFilter();
    }

    private void loadVehicles() {
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT CarId, CarMake, CarModel, CarType, Seating, Available, Price FROM cars";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int carId = resultSet.getInt("CarId");
                String carMake = resultSet.getString("CarMake");
                String carModel = resultSet.getString("CarModel");
                String carType = resultSet.getString("CarType");
                int seating = resultSet.getInt("Seating");
                int available = resultSet.getInt("Available");
                double price = resultSet.getDouble("Price");

                vehicleList.add(new VehiclesInfo("/images/car_" + carId + ".png",carMake, carModel, carType, price, seating, available));
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load client data: " + e.getMessage());
        }
        System.out.println("Vehicle Size: " + vehicleList.size());
        applyFilters();
    }

    private void applyFilters() {
        List<VehiclesInfo> filteredList = vehicleList.stream()
                .filter(v -> filterType.getValue() == null || matchesFilter(v))
                .filter(v -> selectedAvailability().equals("All") || v.getAvailable() > 0 == selectedAvailability().equals("Available"))
                .collect(Collectors.toList());

        displayVehicles(filteredList);
        scrollPane.setVvalue(0.0);
    }

    private boolean matchesFilter(VehiclesInfo v) {
        String selectedFilter = filterType.getValue();
        String selectedValue = filterItem.getValue();

        if (selectedFilter == null || selectedValue == null) return true;

        return switch (selectedFilter) {
            case "Brand" -> v.getMake().equals(selectedValue);
            case "Car Type" -> v.getType().equals(selectedValue);
            case "Seating" -> v.getSeating() == Integer.parseInt(selectedValue);
            case "Price" -> checkPriceRange(v.getPrice(), selectedValue);
            default -> true;
        };
    }

    private boolean checkPriceRange(double price, String selectedValue) {
        if (selectedValue.startsWith(">")) {
            return price > Integer.parseInt(selectedValue.substring(1));
        } else if (selectedValue.contains("-")) {
            String[] range = selectedValue.split("-");
            int min = Integer.parseInt(range[0].trim());
            int max = Integer.parseInt(range[1].trim());
            return price >= min && price <= max;
        }
        return false;
    }

    private void displayVehicles(List<VehiclesInfo> vehicles) {
        gridPane.getChildren().clear();

        int rowIndex = 0;
        int colIndex = 0;

        for (VehiclesInfo v : vehicles) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Client/VehicleCards.fxml"));
                AnchorPane vehicleCard = loader.load();

                VehicleCardsController controller = loader.getController();
                controller.setVehicleData(v);

                gridPane.add(vehicleCard, colIndex, rowIndex);

                colIndex++;
                if (colIndex == MAX_COLUMNS) {
                    colIndex = 0;
                    rowIndex++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateFilter() {
        ObservableList<String> filterOptions = FXCollections.observableArrayList("All", "Brand", "Car Type", "Seating", "Price");
        filterType.setItems(filterOptions);
        filterType.setOnAction(event -> updateFilterItem());

        available.selectedToggleProperty().addListener((observable, oldValue, newValue) -> applyFilters());
    }

    private void updateFilterItem() {
        filterItem.getItems().clear();
        String selectedFilter = filterType.getValue();
        String column = switch (selectedFilter) {
            case "Brand" -> "CarMake";
            case "Car Type" -> "CarType";
            case "Seating" -> "Seating";
            case "Price" -> "Price";
            default -> "";
        };

        if (!column.isEmpty() && !column.equals("Price")) {
            try (Connection connection = DBConnection.getConnection()) {
                String sql = "SELECT DISTINCT " + column + " FROM cars";
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    filterItem.getItems().add(resultSet.getString(1));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (!column.isEmpty() && column.equals("Price")) {
            filterItem.getItems().add(">500");
            filterItem.getItems().add("500 - 2000");
            filterItem.getItems().add("2000 - 4000");
            filterItem.getItems().add("4000 - 6000");
        }

        filterItem.setOnAction(event -> applyFilters());
    }

    public String selectedAvailability() {
        Toggle selectedToggle = available.getSelectedToggle();
        return selectedToggle != null ? ((RadioButton) selectedToggle).getText() : "All";
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        Image icon = new Image(getClass().getResource("/Images/Logo.png").toString());

        alertStage.getIcons().add(icon);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
