package com.example.queimacaloria.controllers;

import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

public class LoginController {

    @FXML
    private TextField campoEmail;
    @FXML
    private PasswordField campoSenha;
    @FXML
    private Label mensagemLogin; // O nome DEVE ser exatamente "mensagemLogin"
    @FXML
    private AuthController authController;

    private Fachada fachada = Fachada.getInstanciaUnica();

    public void setAuthController(AuthController authController) {
        this.authController = authController;
    }

    @FXML
    public void login() {
        String email = campoEmail.getText();
        String password = campoSenha.getText();

        try {
            List<Usuario> usuarios = fachada.listarUsuarios();
            boolean loginSucesso = false;

            for (Usuario usuario : usuarios) {
                if (usuario.getEmail().equals(email) && usuario.getSenha().equals(password)) {
                    mensagemLogin.setText("Login efetuado com sucesso"); // ESTA LINHA ESTAVA CAUSANDO O ERRO
                    loginSucesso = true;
                    authController.mostrarTelaPrincipal(getPrimaryStage());
                    break; // Importante:  sair do loop após encontrar
                }
            }

            if (!loginSucesso) {
                mensagemLogin.setText("Credenciais inválidas"); // E ESTA
            }

        } catch (Exception e) {
            mensagemLogin.setText("Erro ao fazer login: " + e.getMessage());
        }
    }

    @FXML
    public void irParaRegistro() {
        if (authController != null) {
            authController.mostrarTelaRegistro();
        } else {
            System.err.println("Erro: AuthController não foi injetado em LoginController!");
        }
    }

    private Stage getPrimaryStage() {
        if (authController != null) {
            return (Stage) campoEmail.getScene().getWindow();
        }
        return null;
    }
}