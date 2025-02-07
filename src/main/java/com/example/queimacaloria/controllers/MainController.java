package com.example.queimacaloria.controllers;

import com.example.queimacaloria.negocio.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import javafx.scene.layout.Region;

public class MainController {

    @FXML
    private StackPane areaConteudo;

    // Método para mostrar a tela de login
    @FXML
    public void mostrarTelaLogin() { // Nomes em português
        carregarTela("login-view.fxml");
    }

    // Método para mostrar a tela de registro
    @FXML
    public void mostrarTelaRegistro() { // Nomes em português
        carregarTela("registro-view.fxml");
    }

    // Método para mostrar a tela de dietas
    @FXML
    public void mostrarTelaDieta() { // Nomes em português
        carregarTela("dieta-view.fxml");
    }

    // Método para mostrar a tela de exercícios
    @FXML
    public void mostrarTelaExercicio() { // Nomes em português
        carregarTela("exercicio-view.fxml");
    }

    // Método para mostrar a tela de metas
    @FXML
    public void mostrarTelaMeta() { // Nomes em português
        carregarTela("meta-view.fxml");
    }

    // Método para mostrar a tela de refeições
    @FXML
    public void mostrarTelaRefeicao() { // Nomes em português
        carregarTela("refeicao-view.fxml");
    }

    // Método para mostrar a tela de treinos
    @FXML
    public void mostrarTelaTreino() { // Nomes em português
        carregarTela("treino-view.fxml");
    }

    // Método para fazer logout
    @FXML
    public void logout() {
        areaConteudo.getChildren().clear();
    }

    // Método para carregar telas dinamicamente
    private void carregarTela(String nomeArquivoFXML) { // Nomes em português
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/queimacaloria/views/" + nomeArquivoFXML));
            Parent root = loader.load();

            // Ajustar o tamanho da tela
            Region region = (Region) root;
            region.setPrefWidth(600);
            region.setPrefHeight(400);

            areaConteudo.getChildren().clear();
            areaConteudo.getChildren().add(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}