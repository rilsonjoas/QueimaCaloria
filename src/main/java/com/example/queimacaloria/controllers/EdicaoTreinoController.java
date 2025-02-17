package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.TreinoNaoEncontradoException;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Treino;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EdicaoTreinoController {

    @FXML private TextField campoNome;
    @FXML private TextField campoTipoTreino;
    @FXML private TextField campoDuracao;
    @FXML private TextField campoNivelDificuldade;
    @FXML private Label mensagemErro;

    private Fachada fachada = Fachada.getInstanciaUnica();
    private Treino treino; // O treino a ser editado
    private TreinoController treinoController;

    public void setTreinoController(TreinoController treinoController) {
        this.treinoController = treinoController;
    }

    public void setTreino(Treino treino) {
        this.treino = treino;
        preencherCampos();
    }

    private void preencherCampos() {
        if (treino != null) {
            campoNome.textProperty().bindBidirectional(treino.nomeProperty());
            campoTipoTreino.textProperty().bindBidirectional(treino.tipoDeTreinoProperty());
            campoDuracao.textProperty().bindBidirectional(treino.duracaoProperty(), new javafx.util.converter.NumberStringConverter());
            campoNivelDificuldade.textProperty().bindBidirectional(treino.nivelDeDificuldadeProperty(), new javafx.util.converter.NumberStringConverter());
        }
    }

    @FXML
    public void atualizarTreino() {
        try {
            fachada.configurarTreino(treino, treino.getNome(), treino.getTipoDeTreino(),
                    treino.getDuracao(), treino.getNivelDeDificuldade());
            mensagemErro.setText("Treino atualizado com sucesso!");
            if (treinoController != null) {
                treinoController.initialize();
            }
            fecharJanela();
        } catch (NumberFormatException e) {
            mensagemErro.setText("Erro: Duração e dificuldade devem ser números inteiros.");
        }
        catch (TreinoNaoEncontradoException e) {
            mensagemErro.setText("Erro: " + e.getMessage());
        } catch (Exception e) {
            mensagemErro.setText("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void fecharJanela() {
        Stage stage = (Stage) campoNome.getScene().getWindow();
        stage.close();
    }
}