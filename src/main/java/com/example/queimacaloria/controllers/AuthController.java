package com.example.queimacaloria.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import com.example.queimacaloria.negocio.Usuario;

//Este controller administra as telas de login e cadastro. Ele faz com que elas operem separados da tela principal.

public class AuthController {

    @FXML
    private StackPane authContainer;

    private Parent telaLogin;
    private Parent telaRegistro;

    // Inicializa o controlador, carregando as telas de login e registro.
    @FXML
    public void initialize(){
        try{
            telaLogin = carregarTela("/com/example/queimacaloria/views/login-view.fxml");
            telaRegistro = carregarTela("/com/example/queimacaloria/views/registro-view.fxml");

            ((LoginController) getController(telaLogin)).setAuthController(this);
            ((RegistroController) getController(telaRegistro)).setAuthController(this);

            mostrarTelaLogin();

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    // Obtém o controlador de uma tela a partir do seu Parent.
    private Object getController(Parent parent){
        return ((FXMLLoader) parent.getProperties().get("fxmlLoader")).getController();
    }

    // Carrega uma tela FXML a partir do caminho especificado.
    private Parent carregarTela(String caminho) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
        Parent root = loader.load();
        root.getProperties().put("fxmlLoader", loader); // Armazena o loader.
        return root;
    }

    // Exibe a tela de login no container principal.
    public void mostrarTelaLogin() {
        authContainer.getChildren().setAll(telaLogin);
    }

    // Exibe a tela de registro no container principal.
    public void mostrarTelaRegistro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/registro-view.fxml"));
            Parent telaRegistro = loader.load();

            RegistroController registroController = loader.getController();
            registroController.setAuthController(this);

            authContainer.getChildren().setAll(telaRegistro);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Exibe a tela principal, definindo o usuário logado.
    public void mostrarTelaPrincipal(Stage primaryStage, Usuario usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/main-view.fxml"));
            Parent telaPrincipal = loader.load();

            MainController mainController = loader.getController();
            mainController.setPrimaryStage(primaryStage);
            mainController.setUsuarioLogado(usuario);

            Scene scene = new Scene(telaPrincipal);
            primaryStage.setScene(scene);
            primaryStage.setTitle("YouFit - Tela Principal");
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar a tela principal", e.getMessage());
        }
    }

    // Exibe um alerta na tela.
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}