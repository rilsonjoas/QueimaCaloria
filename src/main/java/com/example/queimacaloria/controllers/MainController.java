package com.example.queimacaloria.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import javafx.scene.layout.Region;


public class MainController {

    @FXML
    private StackPane contentArea;


    // Método para mostrar a tela de login
    @FXML
    public void showLoginScreen() {
        loadScreen("login-view.fxml");
    }


    // Método para mostrar a tela de registro
    @FXML
    public void showRegisterScreen() {
        loadScreen("register-view.fxml");
    }

    // Método para mostrar a tela de dashboard
    @FXML
    public void showUserDashboard() {
        loadScreen("user-dashboard-view.fxml");
    }

    // Método para mostrar a tela de dietas
    @FXML
    public void showDietScreen() {
        loadScreen("diet-view.fxml");
    }

    // Método para mostrar a tela de exercícios
    @FXML
    public void showExerciseScreen() {
        loadScreen("exercise-view.fxml");
    }

    // Método para mostrar a tela de metas
    @FXML
    public void showGoalScreen() {
        loadScreen("goal-view.fxml");
    }


    // Método para mostrar a tela de refeições
    @FXML
    public void showMealScreen() {
        loadScreen("meal-view.fxml");
    }


    // Método para mostrar a tela de treinos
    @FXML
    public void showWorkoutScreen() {
        loadScreen("workout-view.fxml");
    }

    // Método para fazer logout
    @FXML
    public void logout() {
        contentArea.getChildren().clear();
    }

    // Método para carregar telas dinamicamente
    private void loadScreen(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/" + fxmlFileName));
            Parent root = loader.load();

            // Ajustar o tamanho da tela
            Region region = (Region) root;
            region.setPrefWidth(600);
            region.setPrefHeight(400);


            contentArea.getChildren().clear();
            contentArea.getChildren().add(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}