package com.example.queimacaloria.controllers;

import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Usuario;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class RegistroController {

    @FXML
    private TextField campoNome;
    @FXML
    private TextField campoEmail;
    @FXML
    private PasswordField campoSenha;
    @FXML
    private DatePicker campoDataNascimento;
    @FXML
    private ChoiceBox<Usuario.Sexo> campoSexo;
    @FXML
    private TextField campoPeso;
    @FXML
    private TextField campoAltura;
    @FXML
    private Label mensagemRegistro;

    private Fachada fachada = Fachada.getInstanciaUnica();

    @FXML
    public void initialize() {
        campoSexo.setItems(FXCollections.observableArrayList(Usuario.Sexo.values()));
    }

    @FXML
    public void registrar() {
        String nome = campoNome.getText();
        String email = campoEmail.getText();
        String password = campoSenha.getText();
        LocalDate dataNascimento = campoDataNascimento.getValue();
        Usuario.Sexo sexo = campoSexo.getValue();

        float peso = 0;
        float altura = 0;
        try {
            peso = Float.parseFloat(campoPeso.getText());
            altura = Float.parseFloat(campoAltura.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Dados inválidos", "Peso e altura devem ser números válidos.");
            return;
        }

        try {
            Usuario novoUsuario = new Usuario(); // Cria um novo usuário
            fachada.atualizarDadosUsuario(novoUsuario, nome, email, password, dataNascimento, sexo, peso, altura);
            mensagemRegistro.setText("Usuário cadastrado com sucesso!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao cadastrar usuário", e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}