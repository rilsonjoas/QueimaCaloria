package com.example.queimacaloria.controllers;

import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Refeicao;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Map;

public class RefeicaoController {

    @FXML
    private TableView<Refeicao> tabelaRefeicoes;
    @FXML
    private TableColumn<Refeicao, String> colunaNome;
    @FXML
    private TableColumn<Refeicao, Integer> colunaCalorias;
    @FXML
    private TableColumn<Refeicao, Map<String, Double>> colunaMacronutrientes;

    @FXML
    private Label mensagemRefeicao;

    private Fachada fachada = Fachada.getInstanciaUnica();;

    @FXML
    public void initialize() {
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaCalorias.setCellValueFactory(new PropertyValueFactory<>("calorias"));
        colunaMacronutrientes.setCellValueFactory(new PropertyValueFactory<>("macronutrientes"));
        atualizarTabelaRefeicoes();
    }

    @FXML
    public void abrirTelaCriarRefeicao() {
        // TODO: Implementar abertura da tela de criação de refeição.
        System.out.println("Abrir tela de criação de refeição (NÃO IMPLEMENTADO)");
    }

    @FXML
    public void atualizarRefeicao() {
        Refeicao refeicaoSelecionada = tabelaRefeicoes.getSelectionModel().getSelectedItem();
        if (refeicaoSelecionada != null) {
            try {

                fachada.configurarRefeicao(refeicaoSelecionada, refeicaoSelecionada.getNome(),
                        refeicaoSelecionada.getDescricao(), refeicaoSelecionada.getMacronutrientes());
                atualizarTabelaRefeicoes();
                mensagemRefeicao.setText("Refeição atualizada com sucesso!");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar refeição", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhuma refeição selecionada",
                    "Por favor, selecione uma refeição para atualizar.");
        }
    }

    @FXML
    public void removerRefeicao() {
        Refeicao refeicaoSelecionada = tabelaRefeicoes.getSelectionModel().getSelectedItem();
        if (refeicaoSelecionada != null) {
            try {
                fachada.configurarRefeicao(refeicaoSelecionada, null, null, null);
                atualizarTabelaRefeicoes();
                mensagemRefeicao.setText("Refeição removida com sucesso!");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao remover refeição", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhuma refeição selecionada",
                    "Por favor, selecione uma refeição para remover.");
        }
    }

    private void atualizarTabelaRefeicoes() {
        try {
            List<Refeicao> listaRefeicoes = fachada.listarRefeicoes(); // PRECISA IMPLEMENTAR na fachada e controladores
            tabelaRefeicoes.setItems(FXCollections.observableArrayList(listaRefeicoes));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar refeições", e.getMessage());
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
