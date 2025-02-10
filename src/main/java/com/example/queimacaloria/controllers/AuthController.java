package com.example.queimacaloria.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AuthController {

    @FXML
    private StackPane authContainer;

    private Parent telaLogin;
    private Parent telaRegistro;


    @FXML
    public void initialize(){
        try{
            telaLogin = carregarTela("/com/example/queimacaloria/views/login-view.fxml"); // Caminho correto?
            telaRegistro = carregarTela("/com/example/queimacaloria/views/registro-view.fxml");

            ((LoginController) getController(telaLogin)).setAuthController(this);
            ((RegistroController) getController(telaRegistro)).setAuthController(this);

            mostrarTelaLogin();


        } catch(IOException e){
            e.printStackTrace();
        }

    }


    private Object getController(Parent parent){
        return ((FXMLLoader) parent.getProperties().get("fxmlLoader")).getController();
    }

    private Parent carregarTela(String caminho) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
        Parent root = loader.load();
        root.getProperties().put("fxmlLoader", loader); // Armazena o loader para uso posterior
        return root;
    }

    public void mostrarTelaLogin() {
        authContainer.getChildren().setAll(telaLogin);
    }

    public void mostrarTelaRegistro() {
        authContainer.getChildren().setAll(telaRegistro);
    }

    // Adicione este método
    public void mostrarTelaPrincipal(Stage primaryStage) { // Recebe o primaryStage
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/main-view.fxml"));
            Parent telaPrincipal = loader.load();

            MainController mainController = loader.getController();
            mainController.setPrimaryStage(primaryStage); // Injete o stage principal no MainController

            Scene scene = new Scene(telaPrincipal);
            primaryStage.setScene(scene);
            primaryStage.setTitle("YouFit - Tela Principal");
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar a tela principal", e.getMessage());
        }
    }

    // Adicione este método showAlert, se você não tiver um método utilitário para isso
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}