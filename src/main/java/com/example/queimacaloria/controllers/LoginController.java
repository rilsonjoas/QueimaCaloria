package com.example.queimacaloria.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.example.queimacaloria.negocio.Usuario;  // Certifique-se de que Usuario está importado
import java.util.ArrayList;
import java.time.LocalDate;

public class LoginController {

    @FXML
    private TextField campoEmail; // Alterado para português

    @FXML
    private PasswordField campoSenha; // Alterado para português

    @FXML
    private Label mensagemLogin; // Alterado para português

    @FXML
    public void login() {
        String email = campoEmail.getText();
        String password = campoSenha.getText();

        if (email.equals("admin") && password.equals("admin")) {
            mensagemLogin.setText("Login efetuado com sucesso");
        }  else {
            mensagemLogin.setText("Credenciais inválidas");
        }
    }
}