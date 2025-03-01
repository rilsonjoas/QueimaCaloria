package com.example.queimacaloria.controllers;

import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Usuario;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Setter;

import java.time.LocalDate;

public class RegistroController {

    @FXML private TextField campoNome;
    @FXML private TextField campoEmail;
    @FXML private PasswordField campoSenha;
    @FXML private DatePicker campoDataNascimento;
    @FXML private ComboBox<Usuario.Sexo> campoSexo;
    @FXML private TextField campoPeso;
    @FXML private TextField campoAltura;
    @FXML private Label mensagemRegistro;

    private final Fachada fachada = Fachada.getInstanciaUnica();
    @FXML
    @Setter
    private AuthController authController;


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
        String pesoStr = campoPeso.getText();
        String alturaStr = campoAltura.getText();

        if (!validarFormulario(nome, email, password, dataNascimento, sexo, pesoStr, alturaStr)) {
            return; // Aborta se a validação falhar
        }

        try {
            float peso = Float.parseFloat(pesoStr);
            float altura = Float.parseFloat(alturaStr);

            Usuario novoUsuario = fachada.cadastrarUsuario(nome, email, password, dataNascimento, sexo, peso, altura);
            mensagemRegistro.setText("Usuário cadastrado com sucesso!");

            if (authController != null) {
                authController.mostrarTelaPrincipal(getPrimaryStage(), novoUsuario);
            } else {
                // Tratar erro
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao mostrar a tela principal", "AuthController não está definido.");
            }
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao cadastrar usuário", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro inesperado", e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para validar o formulário
    private boolean validarFormulario(String nome, String email, String password, LocalDate dataNascimento,
                                      Usuario.Sexo sexo, String pesoStr, String alturaStr) {
        if (nome == null || nome.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O nome não pode estar vazio.");
            return false;
        }

        if (email == null || email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O e-mail não pode estar vazio.");
            return false;
        }

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O e-mail não é válido.");
            return false;
        }

        if (password == null || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "A senha não pode estar vazia.");
            return false;
        }

        if (dataNascimento == null) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "A data de nascimento não pode ser nula.");
            return false;
        }

        if (sexo == null) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O sexo não pode ser nulo.");
            return false;
        }

        if (pesoStr == null || pesoStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O peso não pode estar vazio.");
            return false;
        }

        if (!isNumeric(pesoStr)) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O peso deve ser um número.");
            return false;
        }

        if (alturaStr == null || alturaStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "A altura não pode estar vazia.");
            return false;
        }

        if (!isNumeric(alturaStr)) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "A altura deve ser um número.");
            return false;
        }

        return true;
    }

    //Função auxiliar para validar se o email é válido
    private boolean isValidEmail(String email) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //Função auxiliar para validar se é um valor numérico
    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void irParaLogin() {
        if (authController != null) {
            authController.mostrarTelaLogin();
        } else {
            System.err.println("Erro: AuthController não foi injetado!");
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro interno", "AuthController não foi configurado corretamente.");
        }
    }

    private Stage getPrimaryStage() {
        if (authController != null) {
            return (Stage) campoEmail.getScene().getWindow();
        }
        return null;
    }
}