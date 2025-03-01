package com.example.queimacaloria.controllers;

import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Usuario;
import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.beans.binding.Bindings;

public class PerfilController {

    @FXML private Label labelNomeAtual;
    @FXML private Label labelEmailAtual;
    @FXML private Label labelPesoAtual;
    @FXML private Label labelAlturaAtual;
    @FXML private Label labelIMCatual;

    @FXML private TextField campoNome;
    @FXML private TextField campoEmail;
    @FXML private TextField campoPeso;
    @FXML private TextField campoAltura;
    @FXML private Label mensagemPerfil;

    private Fachada fachada = Fachada.getInstanciaUnica();
    private MainController mainController;
    private Usuario usuarioLogado;

    // Define o controlador principal.
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    // Define o usuário logado e configura os bindings.
    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
        bindLabels();
    }

    // Configura os bindings dos labels e campos de texto.
    private void bindLabels() {
        if (usuarioLogado != null) {
            labelNomeAtual.textProperty().bind(usuarioLogado.nomeProperty());
            labelEmailAtual.textProperty().bind(usuarioLogado.emailProperty());

            labelPesoAtual.textProperty().bind(Bindings.createStringBinding(
                    () -> String.format("%.2f", usuarioLogado.getPeso()),
                    usuarioLogado.pesoProperty()
            ));
            labelAlturaAtual.textProperty().bind(Bindings.createStringBinding(
                    () -> String.format("%.0f", usuarioLogado.getAltura()),
                    usuarioLogado.alturaProperty()
            ));
            labelIMCatual.textProperty().bind(Bindings.createStringBinding(
                    () -> String.format("%.2f", usuarioLogado.getImc()),
                    usuarioLogado.imcProperty()
            ));

            campoNome.textProperty().bindBidirectional(usuarioLogado.nomeProperty());
            campoEmail.textProperty().bindBidirectional(usuarioLogado.emailProperty());
            campoPeso.textProperty().bindBidirectional(usuarioLogado.pesoProperty(), new javafx.util.converter.NumberStringConverter());
            campoAltura.textProperty().bindBidirectional(usuarioLogado.alturaProperty(), new javafx.util.converter.NumberStringConverter());

        }
    }

    // Atualiza os dados do perfil do usuário.
    @FXML
    public void atualizarPerfil() {
        if (usuarioLogado == null) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Usuário não logado", "Não há um usuário logado para atualizar.");
            return;
        }

        String nome = campoNome.getText();
        String email = campoEmail.getText();
        String pesoStr = campoPeso.getText();
        String alturaStr = campoAltura.getText();

        if (!validarFormulario(nome, email, pesoStr, alturaStr)) {
            return; // Aborta se a validação falhar
        }

        try {
            float peso = Float.parseFloat(pesoStr);
            float altura = Float.parseFloat(alturaStr);
            //Agora chamar o método correto passando todos os parâmetros.
            fachada.atualizarDadosUsuario(usuarioLogado, nome, email, null, null, null, peso, altura, usuarioLogado.getTipo());
            mensagemPerfil.setText("Perfil atualizado com sucesso!");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Dados inválidos", "Peso e altura devem ser números válidos.");
        } catch (UsuarioNaoEncontradoException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Usuário não encontrado", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar perfil", e.getMessage());
            e.printStackTrace();
        }
    }

    // Validar o formulario
    private boolean validarFormulario(String nome, String email, String pesoStr, String alturaStr) {
        if (nome == null || nome.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O nome não pode estar vazio.");
            return false;
        }

        if (email == null || email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O email não pode estar vazio.");
            return false;
        }

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O email não é válido.");
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

    //Validar se o email é válido.
    private boolean isValidEmail(String email) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //Validar se é um valor numérico
    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Volta para a tela principal.
    @FXML
    public void voltarParaTelaPrincipal() {
        if (mainController != null) {
            mainController.mostrarTelaPrincipal();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro interno", "MainController não foi configurado corretamente.");
        }
    }

    // Exibe um alerta na tela.
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}