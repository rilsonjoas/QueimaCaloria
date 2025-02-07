package com.example.queimacaloria.controllers;

import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Refeicao;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import java.util.List;
import java.util.Map;

// Controller para a tela de gerenciamento de refeições.
public class RefeicaoController {

    @FXML private TableView<Refeicao> tabelaRefeicoes;
    @FXML private TableColumn<Refeicao, String> colunaNome;
    @FXML private TableColumn<Refeicao, Integer> colunaCalorias;
    @FXML private TableColumn<Refeicao, Map<String, Double>> colunaMacronutrientes;
    @FXML private Label mensagemRefeicao;

    private Fachada fachada = Fachada.getInstanciaUnica();
    private MainController mainController;  // Referência ao MainController

    // Injeta o MainController para permitir a volta à tela principal.
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    // Inicializa a tabela de refeições e configura os listeners.
    public void initialize() {
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaCalorias.setCellValueFactory(new PropertyValueFactory<>("calorias"));
        colunaMacronutrientes.setCellValueFactory(new PropertyValueFactory<>("macronutrientes"));
        atualizarTabelaRefeicoes(); // Carrega as refeições na tabela.
    }

    @FXML
    // Abre a tela para criar uma nova refeição.
    public void abrirTelaCriarRefeicao() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/criacao-refeicao-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Criar Nova Refeição");
            stage.setScene(scene);

            // Injeta este controller (RefeicaoController) no controller de criação.
            CriacaoRefeicaoController controller = loader.getController();
            controller.setRefeicaoController(this);

            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao abrir tela", e.getMessage());
        }
    }

    @FXML
    // Atualiza uma refeição existente.
    public void atualizarRefeicao() {
        Refeicao refeicaoSelecionada = tabelaRefeicoes.getSelectionModel().getSelectedItem();
        if (refeicaoSelecionada != null) {
            try {
                fachada.configurarRefeicao(refeicaoSelecionada, refeicaoSelecionada.getNome(),
                        refeicaoSelecionada.getDescricao(), refeicaoSelecionada.getMacronutrientes());
                atualizarTabelaRefeicoes(); // Atualiza a tabela após a modificação.
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
    // Remove uma refeição existente.
    public void removerRefeicao() {
        Refeicao refeicaoSelecionada = tabelaRefeicoes.getSelectionModel().getSelectedItem();
        if (refeicaoSelecionada != null) {
            try {
                fachada.configurarRefeicao(refeicaoSelecionada, null, null, null); // "Remove" a refeição.
                atualizarTabelaRefeicoes(); // Atualiza a tabela após a remoção.
                mensagemRefeicao.setText("Refeição removida com sucesso!");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao remover refeição", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhuma refeição selecionada",
                    "Por favor, selecione uma refeição para remover.");
        }
    }

    // Atualiza a tabela de refeições com os dados do repositório.
    private void atualizarTabelaRefeicoes() {
        try {
            List<Refeicao> listaRefeicoes = fachada.listarRefeicoes();
            tabelaRefeicoes.setItems(FXCollections.observableArrayList(listaRefeicoes));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar refeições", e.getMessage());
        }
    }

    // Exibe um alerta na tela.
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


    @FXML
    // Volta para a tela principal
    public void voltarParaTelaPrincipal() {
        if (mainController != null) {
            mainController.mostrarTelaPrincipal();
        } else {
            System.err.println("Erro: MainController não foi injetado!");
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro interno", "MainController não foi configurado corretamente.");
        }
    }
}
