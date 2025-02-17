
package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.MetaNaoEncontradaException;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Meta;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;


public class CriacaoMetaController {

    @FXML
    private TextField campoDescricao;
    @FXML
    private ChoiceBox<Meta.Tipo> campoTipo;
    @FXML
    private TextField campoValorAlvo;
    @FXML
    private TextField campoProgressoAtual;
    @FXML
    private DatePicker campoDataConclusao;
    @FXML
    private Label mensagemErro;

    private Fachada fachada = Fachada.getInstanciaUnica();

    private MetaController metaController;

    private MainController mainController;

    public void setMetaController(MetaController metaController) {
        this.metaController = metaController;

    }

    //ADD
    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        campoTipo.setItems(FXCollections.observableArrayList(Meta.Tipo.values()));
    }

    @FXML
    // Método chamado ao clicar no botão "Criar Meta"
    public void criarMeta() {
        try {
            String descricao = campoDescricao.getText();
            Meta.Tipo tipo = campoTipo.getValue();
            double valorAlvo = Double.parseDouble(campoValorAlvo.getText());
            double progressoAtual = Double.parseDouble(campoProgressoAtual.getText());
            LocalDate dataConclusao = campoDataConclusao.getValue();

            Meta novaMeta = new Meta();
            fachada.configurarMeta(novaMeta, descricao, tipo, valorAlvo, progressoAtual, dataConclusao);

            mensagemErro.setText("Meta criada com sucesso!");

            // Atualiza a tabela de metas no MetaController
            if (metaController != null) {
                metaController.initialize();
            }
            //ADD
            if(mainController != null){
                mainController.atualizarDadosTelaPrincipal();
            }

            fecharJanela(); // Fecha a janela atual

        } catch (NumberFormatException e) {
            mensagemErro.setText("Erro: Valores numéricos inválidos.");
        } catch (MetaNaoEncontradaException e) {
            mensagemErro.setText("Erro ao criar meta: " + e.getMessage());
        } catch (Exception e) {
            mensagemErro.setText("Erro inesperado: " + e.getMessage());
        }
    }

    @FXML
    // Fecha a janela atual
    private void fecharJanela() {
        Stage stage = (Stage) campoDescricao.getScene().getWindow();
        stage.close();
    }
}