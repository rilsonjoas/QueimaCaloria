
package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;
import com.example.queimacaloria.negocio.Dieta;
import com.example.queimacaloria.negocio.Fachada;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class DietaController {

    @FXML
    private TableView<Dieta> tabelaDietas;
    @FXML
    private TableColumn<Dieta, String> colunaNome;
    @FXML
    private TableColumn<Dieta, Dieta.ObjetivoDieta> colunaObjetivo;
    @FXML
    private TableColumn<Dieta, Integer> colunaCalorias;
    @FXML
    private TableColumn<Dieta, Double> colunaProgresso;
    @FXML
    private Label mensagemDieta;

    private Fachada fachada = Fachada.getInstanciaUnica();

    @FXML
    public void initialize() {
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaObjetivo.setCellValueFactory(new PropertyValueFactory<>("objetivo"));
        colunaCalorias.setCellValueFactory(new PropertyValueFactory<>("caloriasDiarias"));
        colunaProgresso.setCellValueFactory(cellData -> cellData.getValue().progressoDaDietaProperty().asObject());
        atualizarTabelaDietas();
    }

    @FXML
    public void abrirTelaCriacaoDieta() {
        // TODO: Implementar a abertura da tela de criação (diálogo, nova janela, etc.)
        // Exemplo básico (precisa de um FXML e Controller para a criação)
        // showCriacaoDietaDialog();
        System.out.println("Abrir tela de criação de dieta (NÃO IMPLEMENTADO)");
    }

    @FXML
    public void realizarAtualizacaoDieta() {
        Dieta dietaSelecionada = tabelaDietas.getSelectionModel().getSelectedItem();
        if (dietaSelecionada != null) {
            try {
                fachada.configurarDieta(dietaSelecionada, dietaSelecionada.getNome().get(),
                        dietaSelecionada.getObjetivo().get(), dietaSelecionada.getCaloriasDiarias().get(),
                        dietaSelecionada.getUsuario().get());
                atualizarTabelaDietas();
                mensagemDieta.setText("Dieta atualizada com sucesso!");
            } catch (DietaNaoEncontradaException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar dieta", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhuma dieta selecionada",
                    "Por favor, selecione uma dieta para atualizar.");
        }
    }

    @FXML
    public void realizarRemocaoDieta() {
        Dieta dietaSelecionada = tabelaDietas.getSelectionModel().getSelectedItem();
        if (dietaSelecionada != null) {
            try {
                fachada.configurarDieta(dietaSelecionada, null, null, 0, null); // Define campos como nulos ou zero para
                                                                                // "remover"

                // fachada.excluirDieta(dietaSelecionada.getId()); // Ajustar fachada se for
                // excluir de verdade
                atualizarTabelaDietas();
                mensagemDieta.setText("Dieta removida com sucesso!");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao remover dieta", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhuma dieta selecionada",
                    "Por favor, selecione uma dieta para remover.");
        }
    }

    private void atualizarTabelaDietas() {
        try {
            List<Dieta> listaDietas = fachada.listarDietas(); // PRECISA IMPLEMENTAR listarDietas() na Fachada e
                                                              // controladores!
            tabelaDietas.setItems(FXCollections.observableArrayList(listaDietas));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar dietas", e.getMessage());
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