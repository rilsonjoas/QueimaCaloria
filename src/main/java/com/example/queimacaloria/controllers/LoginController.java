package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Usuario;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.List;

public class LoginController {

    @FXML private TextField campoEmail;
    @FXML private PasswordField campoSenha;
    @FXML private Label mensagemLogin;
    @FXML private AuthController authController;
    @FXML private Button buttonLoginAdmin;

    private Fachada fachada = Fachada.getInstanciaUnica();
    private static final String ADMIN_EMAIL = "admin";
    private static final String ADMIN_SENHA = "admin";

    public void setAuthController(AuthController authController) {
        this.authController = authController;
    }

    @FXML
    public void initialize(){
        buttonLoginAdmin.setOnAction(event -> loginAdmin());
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

                    try {
                        Usuario usuarioCompleto = fachada.buscarUsuarioPorId(usuario.getId());
                        if(usuarioCompleto.getRestricoes() == null){
                            usuarioCompleto.setRestricoes(FXCollections.observableSet(new HashSet<>()));
                        }

                        PauseTransition delay = new PauseTransition(Duration.seconds(0.5)); // Pequena pausa
                        delay.setOnFinished(event -> {
                            authController.mostrarTelaPrincipal(getPrimaryStage(), usuarioCompleto);

                            // Lembrete de água
                            Alert waterAlert = new Alert(Alert.AlertType.INFORMATION);
                            waterAlert.setTitle("Lembrete!");
                            waterAlert.setHeaderText(null);
                            waterAlert.setContentText("Lembre-se de beber água para se manter hidratado!");

                            // Pausa para o lembrete de água
                            PauseTransition waterDelay = new PauseTransition(Duration.seconds(5));
                            waterDelay.setOnFinished(e -> waterAlert.close());
                            waterAlert.show();
                            waterDelay.play();
                        });
                        delay.play();

                        return;

                    } catch (UsuarioNaoEncontradoException e) {
                        mensagemLogin.setText("Erro ao carregar dados do usuário: " + e.getMessage());
                        return; // Sai do método login() se não encontrar o usuário.
                    }
                }
            }

            mensagemLogin.setText("Credenciais inválidas");

        } catch (Exception e) {
            mensagemLogin.setText("Erro ao fazer login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void loginAdmin() {
        String email = campoEmail.getText();
        String password = campoSenha.getText();

        if (email.equals(ADMIN_EMAIL) && password.equals(ADMIN_SENHA)) {
            try {
                Usuario admin = new Usuario();
                admin.setNome("Administrador");
                admin.setEmail(ADMIN_EMAIL);
                admin.setSenha(ADMIN_SENHA);
                admin.setTipo(String.valueOf(Usuario.TipoUsuario.ADMINISTRADOR));

                mensagemLogin.setText("Login de administrador efetuado com sucesso");
                authController.mostrarTelaAdmin(getPrimaryStage(), admin);

            } catch (Exception e) {
                mensagemLogin.setText("Erro ao fazer login de administrador: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            mensagemLogin.setText("Credenciais de administrador inválidas");
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