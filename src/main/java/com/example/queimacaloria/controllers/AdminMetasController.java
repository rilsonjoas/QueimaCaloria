package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.MetaNaoEncontradaException;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.InicializadorDados;
import com.example.queimacaloria.negocio.Meta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import com.example.queimacaloria.interfaces.IBaseAdmin;  // Importe a interface
import lombok.Setter;

public class AdminMetasController {

    @FXML private TableView<Meta> tabelaMetas;
    @FXML private TableColumn<Meta, String> colunaDescricao;
    @FXML private TableColumn<Meta, Meta.Tipo> colunaTipo;
    @FXML private TableColumn<Meta, Double> colunaValorAlvo;

    @FXML private TextField campoDescricao;
    @FXML private ChoiceBox<Meta.Tipo> campoTipo;
    @FXML private TextField campoValorAlvo;
    @FXML private Label mensagem;

    private Fachada fachada = Fachada.getInstanciaUnica();
    @Setter private IBaseAdmin mainController;
    private ObservableList<Meta> listaMetasPreDefinidas = FXCollections.observableArrayList();

    public void setMainController(AdminMainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        colunaDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colunaTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colunaValorAlvo.setCellValueFactory(new PropertyValueFactory<>("valorAlvo"));

        campoTipo.setItems(FXCollections.observableArrayList(Meta.Tipo.values()));

        carregarMetasPreDefinidas();
        atualizarTabelaMetas();
    }

    private void carregarMetasPreDefinidas(){
        List<Meta> metas = fachada.getMetasPreDefinidas();
        listaMetasPreDefinidas.addAll(metas);
        tabelaMetas.setItems(FXCollections.observableArrayList(listaMetasPreDefinidas));

    }

    @FXML
    public void criarMeta() {
        String descricao = campoDescricao.getText();
        Meta.Tipo tipo = campoTipo.getValue();
        String valorAlvoStr = campoValorAlvo.getText();

        try {
            double valorAlvo = Double.parseDouble(valorAlvoStr);

            Meta novaMeta = new Meta();
            fachada.configurarMeta(novaMeta, descricao, tipo, valorAlvo, 0.0, null);

            atualizarTabelaMetas();
            mensagem.setText("Meta criada com sucesso.");
        } catch (Exception e) {
            mensagem.setText("Erro ao criar meta: " + e.getMessage());
        }
    }

    @FXML
    public void atualizarMeta() {
        Meta metaSelecionada = tabelaMetas.getSelectionModel().getSelectedItem();
        if (metaSelecionada != null) {
            try {
                String descricao = campoDescricao.getText();
                Meta.Tipo tipo = campoTipo.getValue();
                String valorAlvoStr = campoValorAlvo.getText();
                double valorAlvo = Double.parseDouble(valorAlvoStr);

                fachada.configurarMeta(metaSelecionada, descricao, tipo, valorAlvo, 0.0, null);
                atualizarTabelaMetas();
                mensagem.setText("Meta atualizada com sucesso.");
            } catch (Exception e) {
                mensagem.setText("Erro ao atualizar meta: " + e.getMessage());
            }
        } else {
            mensagem.setText("Selecione uma meta para atualizar.");
        }
    }

    @FXML
    public void removerMeta() {
        Meta metaSelecionada = tabelaMetas.getSelectionModel().getSelectedItem();
        if (metaSelecionada != null) {
            try {
                fachada.removerMeta(metaSelecionada.getId());
                atualizarTabelaMetas();
                mensagem.setText("Meta removida com sucesso.");
            } catch (MetaNaoEncontradaException e) {
                mensagem.setText("Erro ao remover meta: " + e.getMessage());
            }
        } else {
            mensagem.setText("Selecione uma meta para remover.");
        }
    }

    private void atualizarTabelaMetas() {
        List<Meta> listaDeMetas = fachada.listarMetas();
        tabelaMetas.setItems(FXCollections.observableArrayList(listaDeMetas));
    }
}