package com.example.queimacaloria.controllers;

import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
                    alert.showAndWait(); // Usa showAndWait() para mostrar e esperar

                    // Lembrete de água (agora ANTES de mostrar a tela principal)
                    Alert waterAlert = new Alert(Alert.AlertType.INFORMATION);
                    waterAlert.setTitle("Lembrete!");
                    waterAlert.setHeaderText(null); // Sem cabeçalho
                    waterAlert.setContentText("Lembre-se de beber água para se manter hidratado! Você pode registrar seu consumo na tela principal.");
                    waterAlert.showAndWait(); // Mostra o alerta e espera

                    authController.mostrarTelaPrincipal(getPrimaryStage(), usuario); // Mostra a tela principal APÓS o alerta.

                    return; // Importante:  Sai do método após o sucesso.
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
                authController.mostrarTelaAdmin(getPrimaryStage(), admin); //Novo

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