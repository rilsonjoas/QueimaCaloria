package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.TreinoNaoEncontradoException;
import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Treino;
import com.example.queimacaloria.negocio.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Alert;

public class CriacaoTreinoController {

    @FXML
    private TextField campoNome;
    @FXML
    private TextField campoTipoTreino;
    @FXML
    private TextField campoDuracao;
    @FXML
    private TextField campoNivelDificuldade;
    @FXML
    private Label mensagemErro;

    @FXML
    private CheckBox checkboxConcluido;

    private Fachada fachada = Fachada.getInstanciaUnica();

    private TreinoController treinoController;

    private MainController mainController;

    // Define o controlador da tela de treinos.
    public void setTreinoController(TreinoController treinoController) {
        this.treinoController = treinoController;
    }

    // Define o controlador principal.
    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }

    // Cria um novo treino.
    @FXML
    public void criarTreino() {
        try {
            String nome = campoNome.getText();
            String tipoTreino = campoTipoTreino.getText();
            int duracao = Integer.parseInt(campoDuracao.getText());
            int nivelDificuldade = Integer.parseInt(campoNivelDificuldade.getText());
            boolean concluido = checkboxConcluido.isSelected();

            Treino novoTreino = new Treino();
            fachada.configurarTreino(novoTreino, nome, tipoTreino, duracao, nivelDificuldade);
            novoTreino.setConcluido(concluido);

            if (mainController != null && mainController.getUsuarioLogado() != null) {
                try {
                    Usuario usuarioAtualizado = fachada.buscarUsuarioPorId(mainController.getUsuarioLogado().getId());
                    fachada.adicionarTreinoAoUsuario(usuarioAtualizado, novoTreino);
                    mainController.setUsuarioLogado(usuarioAtualizado);

                    if (concluido) {
                        mainController.getUsuarioLogado().adicionarPontuacao(nivelDificuldade);
                        showAlert(Alert.AlertType.INFORMATION,"Parabéns!", "Treino Concluído", "Você concluiu o treino e ganhou " + nivelDificuldade + " pontos!");
                    }

                } catch (UsuarioNaoEncontradoException e) {
                    System.err.println("Erro ao buscar usuário: " + e.getMessage());
                }
            }

            if (treinoController != null) {
                treinoController.atualizarTabelaTreinosUsuario();
            }

            if(mainController != null){
                mainController.atualizarDadosTelaPrincipal();
            }
            mensagemErro.setText("Treino criado com sucesso!");
            fecharJanela();

        } catch (NumberFormatException | TreinoNaoEncontradoException  e) {
            mensagemErro.setText("Erro: " + e.getMessage());
        } catch (Exception e) {
            mensagemErro.setText("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Fecha a janela atual.
    @FXML
    private void fecharJanela() {
        Stage stage = (Stage) campoNome.getScene().getWindow();
        stage.close();
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