package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.MetaNaoEncontradaException;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Meta;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;


public class EdicaoMetaController {

    @FXML
    private TextField campoDescricao;
    @FXML
    private ChoiceBox<Meta.Tipo> campoTipo;
    @FXML
    private TextField campoValorAlvo;
    @FXML
    private TextField campoProgressoAtual;
    @FXML
    private Label labelDataConclusao; // Mostra a data, mas não é editável.
    @FXML
    private Button buttonConcluirMeta; // Botão para concluir
    @FXML
    private Label mensagemErro;

    private Fachada fachada = Fachada.getInstanciaUnica();

    private Meta meta;
    private MetaController metaController;

    //ADD
    private MainController mainController;

    public void setMetaController(MetaController metaController) {
        this.metaController = metaController;
    }

    //ADD
    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
        preencherCampos();
    }

    @FXML
    public void initialize() {
        campoTipo.setItems(FXCollections.observableArrayList(Meta.Tipo.values()));
    }

    private void preencherCampos() {
        if (meta != null) {
            campoDescricao.setText(meta.getDescricao());
            campoTipo.setValue(meta.getTipo());
            campoValorAlvo.setText(String.valueOf(meta.getValorAlvo()));
            campoProgressoAtual.setText(String.valueOf(meta.getProgressoAtual()));

            // Data de conclusão: formata e exibe, mas não é editável
            if (meta.getDataConclusao() != null) {
                labelDataConclusao.setText("Concluída em: " + meta.getDataConclusao().toString());
            } else {
                labelDataConclusao.setText("Meta não concluída");
            }

            // Desabilitar o botão se já estiver concluída
            if (meta.getDataConclusao() != null) {
                buttonConcluirMeta.setDisable(true);
            }
        }
    }

    @FXML
    public void atualizarMeta() {
        try {
            // Atualiza os campos que podem ser editados
            String descricao = campoDescricao.getText();
            Meta.Tipo tipo = campoTipo.getValue();
            double valorAlvo = Double.parseDouble(campoValorAlvo.getText());
            double progressoAtual = Double.parseDouble(campoProgressoAtual.getText());

            fachada.configurarMeta(meta, descricao, tipo, valorAlvo, progressoAtual, meta.getDataConclusao()); // Mantém a data original ou null
            mensagemErro.setText("Meta atualizada com sucesso!");

            if(metaController != null){
                metaController.initialize(); //  Chama initialize
            }

            //ADD
            if(mainController != null){
                mainController.atualizarDadosTelaPrincipal();
            }

            fecharJanela();

        }  catch (NumberFormatException e) {
            mensagemErro.setText("Erro: Valores numéricos inválidos.");
        } catch (MetaNaoEncontradaException e) {
            mensagemErro.setText("Erro ao atualizar meta: " + e.getMessage());
        }
        catch (Exception e) {
            mensagemErro.setText("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void concluirMeta() {
        try {
            meta.setDataConclusao(LocalDate.now()); // Define a data de conclusão
            fachada.configurarMeta(meta, meta.getDescricao(), meta.getTipo(), meta.getValorAlvo(), meta.getProgressoAtual(), meta.getDataConclusao());// Salva
            labelDataConclusao.setText("Concluída em: " + meta.getDataConclusao().toString()); // Atualiza a exibição
            buttonConcluirMeta.setDisable(true); // Desabilita o botão

            if(metaController != null){
                metaController.initialize(); //  Chama initialize
            }
            if(mainController != null){
                mainController.atualizarDadosTelaPrincipal();
            }

            mensagemErro.setText("Meta concluída com sucesso!");
        } catch (MetaNaoEncontradaException e) {
            mensagemErro.setText("Erro ao concluir meta: " + e.getMessage());

        }
    }

    @FXML
    private void fecharJanela() {
        Stage stage = (Stage) campoDescricao.getScene().getWindow();
        stage.close();
    }
}