package com.example.queimacaloria.controllers;

import com.example.queimacaloria.dados.RepositorioUsuariosArray;
import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;
import com.example.queimacaloria.negocio.ControladorUsuario;
import com.example.queimacaloria.negocio.Usuario;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;

public class RegisterController {

    @FXML private TextField nameField;

    @FXML private TextField emailField;

    @FXML private PasswordField passwordField;

    @FXML private DatePicker birthDateField;

    @FXML private ChoiceBox<Usuario.Sexo> genderChoice;

    @FXML private TextField weightField;
    @FXML private TextField heightField;

    @FXML
    private Label registerMessage;


    private ControladorUsuario controladorUsuario = new ControladorUsuario();


    @FXML
    public void initialize() {
        genderChoice.setItems(FXCollections.observableArrayList(Usuario.Sexo.values()));
    }

    @FXML
    public void register() {
        String nome = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        LocalDate dataNascimento = birthDateField.getValue();
        Usuario.Sexo sexo = genderChoice.getValue();
        float peso = Float.parseFloat(weightField.getText());
        float altura = Float.parseFloat(heightField.getText());


        try {
            Usuario novoUsuario = new Usuario();
            controladorUsuario.atualizarDados(novoUsuario, nome, email, dataNascimento, sexo, peso, altura);
            RepositorioUsuariosArray.getInstanciaUnica().adicionar(novoUsuario);
            registerMessage.setText("Usuário cadastrado com sucesso!");

        } catch (IllegalArgumentException e) {
            registerMessage.setText("Erro ao cadastrar usuário: " + e.getMessage());
        } catch (UsuarioNaoEncontradoException e) {
            throw new RuntimeException(e);
        }
    }
}