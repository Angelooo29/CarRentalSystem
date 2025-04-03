package com.lester.carrentalsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/View/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 640);  // Updated dimensions to match FXML
        stage.setTitle("Car Rental System - Login");  // Updated title
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}