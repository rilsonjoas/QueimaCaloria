package com.example.queimacaloria.controllers;

import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Usuario;
import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.beans.binding.Bindings; // Importante!

public class PerfilController {

    // Labels para os valores ATUAIS
    @FXML private Label labelNomeAtual;
    @FXML private Label labelEmailAtual;
    @FXML private Label labelPesoAtual;
    @FXML private Label labelAlturaAtual;
    @FXML private Label labelIMCatual;


    // Campos de texto (continuam os mesmos)
    @FXML private TextField campoNome;
    @FXML private TextField campoEmail;
    @FXML private TextField campoPeso;
    @FXML private TextField campoAltura;
    @FXML private Label mensagemPerfil;

    private Fachada fachada = Fachada.getInstanciaUnica();
    private MainController mainController;
    private Usuario usuarioLogado;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
        bindLabels(); //Chama o método que configura os bindings.
    }

    private void bindLabels() {
        if (usuarioLogado != null) {
            // Usando apenas String.valueOf para nome e email, já que são Strings.
            labelNomeAtual.textProperty().bind(usuarioLogado.nomeProperty());
            labelEmailAtual.textProperty().bind(usuarioLogado.emailProperty());

            // Usando createStringBinding e String.format para formatar os números,
            // mas SEM texto adicional.
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


            // Mantém os bindings bidirecionais para os campos de texto.
            campoNome.textProperty().bindBidirectional(usuarioLogado.nomeProperty());
            campoEmail.textProperty().bindBidirectional(usuarioLogado.emailProperty());
            campoPeso.textProperty().bindBidirectional(usuarioLogado.pesoProperty(), new javafx.util.converter.NumberStringConverter());
            campoAltura.textProperty().bindBidirectional(usuarioLogado.alturaProperty(), new javafx.util.converter.NumberStringConverter());

        }
    }


    @FXML
    public void atualizarPerfil() {
        if (usuarioLogado == null) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Usuário não logado", "Não há um usuário logado para atualizar.");
            return;
        }

        try {
            //O nome e email já foram atualizados devido aos bindings.
            //float peso = Float.parseFloat(campoPeso.getText()); //Não precisa mais, o binding já fez
            //float altura = Float.parseFloat(campoAltura.getText()); //Não precisa mais

            //A fachada só precisa ser chamada para persistir as mudanças, o que *já foi feito*.
            fachada.atualizarDadosUsuario(usuarioLogado, usuarioLogado.getNome(), usuarioLogado.getEmail(), null, null, null, usuarioLogado.getPeso(), usuarioLogado.getAltura());
            // atualizarLabels(); // Não precisa mais! Os bindings fazem a atualização.
            mensagemPerfil.setText("Perfil atualizado com sucesso!");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Dados inválidos", "Peso e altura devem ser números válidos.");
        } catch (UsuarioNaoEncontradoException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Usuário não encontrado", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar perfil", e.getMessage());
            e.printStackTrace(); // Boa prática: imprimir o stack trace em caso de erro inesperado.
        }
    }

    @FXML
    public void voltarParaTelaPrincipal() {
        if (mainController != null) {
            mainController.mostrarTelaPrincipal();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro interno", "MainController não foi configurado corretamente.");
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