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
    //ADD
    private MainController mainController;

    public void setTreinoController(TreinoController treinoController) {
        this.treinoController = treinoController;
    }

    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }

    public void setTreino(Treino treino) {
        this.treino = treino;
        preencherCampos();
    }

    private void preencherCampos() {
        if (treino != null) {
            campoNome.setText(treino.getNome());
            campoTipoTreino.setText(treino.getTipoDeTreino());
            campoDuracao.setText(String.valueOf(treino.getDuracao()));
            campoNivelDificuldade.setText(String.valueOf(treino.getNivelDeDificuldade()));
        }
    }

    @FXML
    public void atualizarTreino() {
        try {
            // Obtém os valores dos campos.
            String nome = campoNome.getText();
            String tipoTreino = campoTipoTreino.getText();
            int duracao = Integer.parseInt(campoDuracao.getText());
            int nivelDificuldade = Integer.parseInt(campoNivelDificuldade.getText());


            fachada.configurarTreino(treino, nome, tipoTreino, duracao, nivelDificuldade);
            mensagemErro.setText("Treino atualizado com sucesso!");
            if (treinoController != null) {
                treinoController.initialize(); //  Chama initialize
            }
            if(mainController != null){
                mainController.atualizarDadosTelaPrincipal();
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