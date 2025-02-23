package com.example.queimacaloria.controllers;

import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Usuario;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

public class LoginController {

    @FXML private TextField campoEmail;
    @FXML private PasswordField campoSenha;
    @FXML private Label mensagemLogin;
    @FXML private AuthController authController;

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

            for (Usuario usuario : usuarios) {
                if (usuario.getEmail().equals(email) && usuario.getSenha().equals(password)) {
                    mensagemLogin.setText("Login efetuado com sucesso");

                    // Alerta de boas-vindas
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Bem-vindo!");
                    alert.setHeaderText("Login realizado com sucesso");
                    alert.setContentText("Olá, " + usuario.getNome() + "! Seja bem-vindo ao YouFit.");
                    alert.show();


                    // Pausa ANTES de mostrar a tela principal e exibir o lembrete
                    PauseTransition delay = new PauseTransition(Duration.seconds(0.5)); // Pequena pausa
                    delay.setOnFinished(event -> {
                        authController.mostrarTelaPrincipal(getPrimaryStage(), usuario);

                        // Lembrete de água (depois de mostrar a tela principal)
                        Alert waterAlert = new Alert(Alert.AlertType.INFORMATION);
                        waterAlert.setTitle("Lembrete!");
                        waterAlert.setHeaderText(null); // Sem cabeçalho
                        waterAlert.setContentText("Lembre-se de beber água para se manter hidratado!");

                        // Pausa para o lembrete de água
                        PauseTransition waterDelay = new PauseTransition(Duration.seconds(5));
                        waterDelay.setOnFinished(e -> waterAlert.close());
                        waterAlert.show(); // Mostra o alerta de água
                        waterDelay.play(); // Inicia a contagem para fechar o alerta de água
                    });
                    delay.play(); // Inicia a primeira pausa

                    return;
                }
            }

            mensagemLogin.setText("Credenciais inválidas");

        } catch (Exception e) {
            mensagemLogin.setText("Erro ao fazer login: " + e.getMessage());
            e.printStackTrace();
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