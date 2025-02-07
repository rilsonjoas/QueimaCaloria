package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.MetaNaoEncontradaException;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Meta;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Date;
import java.util.List;

public class MetaController {

    @FXML
    private TableView<Meta> tabelaMetas;
    @FXML
    private TableColumn<Meta, String> colunaDescricao;
    @FXML
    private TableColumn<Meta, Meta.Tipo> colunaTipo;
    @FXML
    private TableColumn<Meta, Double> colunaProgresso;
    @FXML
    private TableColumn<Meta, Date> colunaDataConclusao;
    @FXML
    private Label mensagemMeta;

    private Fachada fachada = Fachada.getInstanciaUnica();

    @FXML
    public void initialize() {
        colunaDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colunaTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colunaProgresso.setCellValueFactory(new PropertyValueFactory<>("progressoAtual")); // Use progressoAtual, não um
                                                                                           // método
        colunaDataConclusao.setCellValueFactory(new PropertyValueFactory<>("dataConclusao"));
        atualizarTabelaMetas();
    }

    @FXML
    public void abrirTelaCriarMeta() {
        // TODO: Implementar abertura da tela de criação de meta.
        System.out.println("Abrir tela de criação de meta (NÃO IMPLEMENTADO)");
    }

    @FXML
    public void atualizarMeta() {
        Meta metaSelecionada = tabelaMetas.getSelectionModel().getSelectedItem();
        if (metaSelecionada != null) {
            try {
                fachada.configurarMeta(metaSelecionada, metaSelecionada.getDescricao(),
                        metaSelecionada.getTipo(), metaSelecionada.getValorAlvo(),
                        metaSelecionada.getProgressoAtual(), metaSelecionada.getDataConclusao());

                atualizarTabelaMetas();
                mensagemMeta.setText("Meta atualizada com sucesso!");
            } catch (MetaNaoEncontradaException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar meta", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhuma meta selecionada",
                    "Por favor, selecione uma meta para atualizar.");
        }
    }

    @FXML
    public void removerMeta() {
        Meta metaSelecionada = tabelaMetas.getSelectionModel().getSelectedItem();
        if (metaSelecionada != null) {
            try {
                fachada.configurarMeta(metaSelecionada, null, null, 0, 0, null);
                atualizarTabelaMetas();
                mensagemMeta.setText("Meta removida com sucesso!");
            } catch (MetaNaoEncontradaException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao remover meta", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhuma meta selecionada",
                    "Por favor, selecione uma meta para remover.");
        }
    }

    private void atualizarTabelaMetas() {

        try {
            List<Meta> listaMetas = fachada.listarMetas(); // PRECISA IMPLEMENTAR listarMetas() na Fachada e
                                                           // controladores
            tabelaMetas.setItems(FXCollections.observableArrayList(listaMetas));

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar metas", e.getMessage());
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
