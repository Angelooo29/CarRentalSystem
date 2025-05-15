package com.lester.carrentalsystem.Controller.Client;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class HomeController {

    @FXML
    private Button closeButton;
    @FXML
    private Button minimizeButton;
    @FXML
    private ImageView imageView;

    @FXML
    private Button prevButton, nextButton;

    @FXML
    private HBox dotsContainer;

    private List<Image> images = new ArrayList<>();
    private List<Circle> dots = new ArrayList<>();
    private int currentIndex = 0;
    private Timeline autoSlide;

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

        imageView.setFitWidth(830);
        imageView.setFitHeight(720);
        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);

        images.add(new Image(getClass().getResource("/Images/MainPhoto.png").toExternalForm()));
        images.add(new Image(getClass().getResource("/Images/SecondPhoto.png").toExternalForm()));
        images.add(new Image(getClass().getResource("/Images/ThirdPhoto.png").toExternalForm()));

        createDots();
        updateImage();

        prevButton.setOnAction(e -> {
            showPreviousImage();
            restartAutoSlide();
        });

        nextButton.setOnAction(e -> {
            showNextImage();
            restartAutoSlide();
        });

        autoSlide = new Timeline(
                new KeyFrame(Duration.seconds(3), e -> showNextImageAuto())
        );
        autoSlide.setCycleCount(Animation.INDEFINITE);
        autoSlide.play();
    }

    private void createDots() {
        for (int i = 0; i < images.size(); i++) {
            Circle dot = new Circle(6, Color.GRAY);
            dots.add(dot);
            dotsContainer.getChildren().add(dot);
        }
    }

    private void showPreviousImage() {
        if (currentIndex > 0) {
            currentIndex--;
            animateSlide(-1);
        }
    }

    private void showNextImage() {
        if (currentIndex < images.size() - 1) {
            currentIndex++;
            animateSlide(1);
        }
    }

    private void showNextImageAuto() {
        currentIndex = (currentIndex + 1) % images.size();
        animateSlide(1);
    }

    private void updateImage() {
        imageView.setImage(images.get(currentIndex));

        for (int i = 0; i < dots.size(); i++) {
            if (i == currentIndex) {
                dots.get(i).setFill(Color.WHITE);
            } else {
                dots.get(i).setFill(Color.GRAY);
            }
        }

        prevButton.setDisable(currentIndex == 0);
        nextButton.setDisable(currentIndex == images.size() - 1);
    }

    private void animateSlide(int direction) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(300), imageView);
        tt.setFromX(direction * imageView.getFitWidth());
        tt.setToX(0);
        tt.setOnFinished(e -> updateImage());
        imageView.setImage(images.get(currentIndex));
        tt.play();
    }

    private void restartAutoSlide() {
        autoSlide.stop();
        autoSlide.playFromStart();
    }
}
