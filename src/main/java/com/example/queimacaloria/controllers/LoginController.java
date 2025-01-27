package com.example.queimacaloria.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.example.queimacaloria.negocio.Usuario;
import java.util.ArrayList;
import java.time.LocalDate;

public class LoginController {

    @FXML private TextField emailField;

    @FXML private PasswordField passwordField;

    @FXML
    private Label loginMessage;

    @FXML
    public void login() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.equals("admin") && password.equals("admin")) {
            loginMessage.setText("Login efetuado com sucesso");
        }else {
            loginMessage.setText("Credenciais inválidas");
        }
    }
}