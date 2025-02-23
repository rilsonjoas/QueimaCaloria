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
                    () -> String.format("%.2f", usuarioLogado.getAltura()),
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

        try {
            fachada.atualizarDadosUsuario(usuarioLogado, usuarioLogado.getNome(), usuarioLogado.getEmail(), null, null, null, usuarioLogado.getPeso(), usuarioLogado.getAltura());
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