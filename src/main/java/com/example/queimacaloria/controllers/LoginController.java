package com.example.queimacaloria.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class LoginController {

    @FXML
    private TextField campoEmail;

    @FXML
    private PasswordField campoSenha;

    @FXML
    private Label mensagemLogin;


    @FXML
    public void login() {
        String email = campoEmail.getText();
        String password = campoSenha.getText();

        // Lógica de login (apenas um exemplo)
        if (email.equals("admin") && password.equals("admin")) {
            mensagemLogin.setText("Login efetuado com sucesso");
        }  else {
            mensagemLogin.setText("Credenciais inválidas");
        }
    }
}