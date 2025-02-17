package com.example.queimacaloria.controllers;

import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Usuario;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration; // Importante

import java.time.LocalDate;
import java.util.Optional;

public class RegistroController {

    @FXML private TextField campoNome;
    @FXML private TextField campoEmail;
    @FXML private PasswordField campoSenha;
    @FXML private DatePicker campoDataNascimento;
    @FXML private ComboBox<Usuario.Sexo> campoSexo;
    @FXML private TextField campoPeso;
    @FXML private TextField campoAltura;
    @FXML private Label mensagemRegistro;

    private Fachada fachada = Fachada.getInstanciaUnica();
    @FXML private AuthController authController;

    public void setAuthController(AuthController authController) {
        this.authController = authController;
    }

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
        float peso = 0.0f;
        float altura = 0.0f;


        try {
            peso = Float.parseFloat(campoPeso.getText());
            altura = Float.parseFloat(campoAltura.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Dados inválidos", "Peso e altura devem ser números válidos.");
            return; // Retorna se houver erro de conversão.
        }


        try {
            // Cadastra e já obtém o usuário criado.
            Usuario novoUsuario = fachada.cadastrarUsuario(nome, email, password, dataNascimento, sexo, peso, altura);
            mensagemRegistro.setText("Usuário cadastrado com sucesso!");

            // Adiciona um pequeno atraso antes de redirecionar (ajuda aos dados carregarem completamente)
            PauseTransition delay = new PauseTransition(Duration.seconds(0.3)); // Atraso de menos de meio segundo
            delay.setOnFinished(event -> {
                if (authController != null) {
                    authController.mostrarTelaPrincipal(getPrimaryStage(), novoUsuario); // Passa o novo usuário!
                } else {
                    System.err.println("Erro: AuthController é nulo em RegistroController!");
                    showAlert(Alert.AlertType.ERROR, "Erro", "Erro interno", "AuthController não foi configurado corretamente.");
                }
            });
            delay.play(); // Inicia o atraso.


        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao cadastrar usuário", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro inesperado", e.getMessage());
            e.printStackTrace();
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
            // Limpeza dos campos
            campoNome.clear();
            campoSenha.clear();
            campoAltura.clear();
            campoEmail.clear();
            campoPeso.clear();
            campoSexo.setValue(null);
            campoDataNascimento.setValue(null);
            mensagemRegistro.setText(null);
        } else {
            System.err.println("Erro: AuthController não foi injetado!"); // Boa prática
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro interno", "AuthController não foi configurado corretamente.");
        }
    }


    private Stage getPrimaryStage() {
        if (authController != null) {
            // Forma correta de obter o Stage a partir de um componente da cena.
            return (Stage) campoEmail.getScene().getWindow();
        }
        return null;
    }
}