package com.example.queimacaloria.controllers;

import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.List;

public class LoginController {

    @FXML private TextField campoEmail;
    @FXML private PasswordField campoSenha;
    @FXML private Label mensagemLogin;
    @FXML private AuthController authController;

    private Fachada fachada = Fachada.getInstanciaUnica();

    // Define o controlador de autenticação.
    public void setAuthController(AuthController authController) {
        this.authController = authController;
    }

    // Realiza o login do usuário.
    @FXML
    public void login() {
        String email = campoEmail.getText();
        String password = campoSenha.getText();

        try {
            List<Usuario> usuarios = fachada.listarUsuarios();

            for (Usuario usuario : usuarios) {
                if (usuario.getEmail().equals(email) && usuario.getSenha().equals(password)) {
                    mensagemLogin.setText("Login efetuado com sucesso");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Bem-vindo!");
                    alert.setHeaderText("Login realizado com sucesso");
                    alert.setContentText("Olá,! Seja bem-vindo ao sistema.");
                    alert.show();
                    authController.mostrarTelaPrincipal(getPrimaryStage(), usuario);
                    return;
                }
            }

            mensagemLogin.setText("Credenciais inválidas");

        } catch (Exception e) {
            mensagemLogin.setText("Erro ao fazer login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Navega para a tela de registro.
    @FXML
    public void irParaRegistro() {
        if (authController != null) {
            authController.mostrarTelaRegistro();
        } else {
            System.err.println("Erro: AuthController não foi injetado em LoginController!");
        }
    }

    // Obtém o palco principal da aplicação.
    private Stage getPrimaryStage() {
        if (authController != null) {
            return (Stage) campoEmail.getScene().getWindow();
        }
        return null;
    }
}