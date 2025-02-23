package com.example.queimacaloria.controllers;

import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Usuario;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

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
        // ... (código de validação e criação do usuário, como você já tem) ...
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
            return;
        }


        try {
            Usuario novoUsuario = fachada.cadastrarUsuario(nome, email, password, dataNascimento, sexo, peso, altura);
            mensagemRegistro.setText("Usuário cadastrado com sucesso!");

            // Pausa ANTES de mostrar a tela principal e exibir o lembrete.
            PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
            delay.setOnFinished(event -> {
                if (authController != null) {
                    authController.mostrarTelaPrincipal(getPrimaryStage(), novoUsuario);

                    // Lembrete de água (depois de mostrar a tela principal).
                    Alert waterAlert = new Alert(Alert.AlertType.INFORMATION);
                    waterAlert.setTitle("Lembrete!");
                    waterAlert.setHeaderText(null);  // Sem cabeçalho para ser mais direto.
                    waterAlert.setContentText("Lembre-se de beber água para se manter hidratado!");

                    // Pausa para o lembrete da água.
                    PauseTransition waterDelay = new PauseTransition(Duration.seconds(5));
                    waterDelay.setOnFinished(e -> waterAlert.close());  // Fecha o alerta após 5 segundos.
                    waterAlert.show();  // Mostra o alerta.
                    waterDelay.play(); // Inicia a contagem regressiva.

                } else {
                    //erro
                }
            });
            delay.play(); // Inicia a primeira pausa.

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
            // Limpar os campos, se desejar.
            campoNome.clear();
            campoSenha.clear();
            campoAltura.clear();
            campoEmail.clear();
            campoPeso.clear();
            campoSexo.setValue(null);
            campoDataNascimento.setValue(null);
            mensagemRegistro.setText(null);


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