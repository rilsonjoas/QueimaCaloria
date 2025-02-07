package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.TreinoNaoEncontradoException;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Treino;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

// Controller para a tela de criação de treino.
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

    private Fachada fachada = Fachada.getInstanciaUnica();

    private TreinoController treinoController;

    // Injeta o TreinoController para que a tabela seja atualizada após criar um novo treino.
    public void setTreinoController(TreinoController treinoController) {
        this.treinoController = treinoController;
    }

    @FXML
    public void criarTreino() {
        try {
            String nome = campoNome.getText();
            String tipoTreino = campoTipoTreino.getText();
            int duracao = Integer.parseInt(campoDuracao.getText());
            int nivelDificuldade = Integer.parseInt(campoNivelDificuldade.getText());

            Treino novoTreino = new Treino();
            fachada.configurarTreino(novoTreino, nome, tipoTreino, duracao, nivelDificuldade);

            mensagemErro.setText("Treino criado com sucesso!");

            if (treinoController != null) {
                treinoController.initialize();
            }

            fecharJanela();
        } catch (NumberFormatException e) {
            mensagemErro.setText("Erro: Duração e dificuldade devem ser números inteiros.");
        } catch (TreinoNaoEncontradoException e) {
            mensagemErro.setText("Erro: " + e.getMessage());
        } catch (Exception e) {
            mensagemErro.setText("Erro inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void fecharJanela() {
        Stage stage = (Stage) campoNome.getScene().getWindow();
        stage.close();
    }
}