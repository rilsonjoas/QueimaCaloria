package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.MetaNaoEncontradaException;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Meta;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import java.time.LocalDate;
import java.util.List;

// Controller para a tela de gerenciamento de metas
public class MetaController {

    @FXML private TableView<Meta> tabelaMetas;
    @FXML private TableColumn<Meta, String> colunaDescricao;
    @FXML private TableColumn<Meta, Meta.Tipo> colunaTipo;
    @FXML private TableColumn<Meta, Double> colunaProgresso;
    @FXML private TableColumn<Meta, LocalDate> colunaDataConclusao;
    @FXML private Label mensagemMeta;

    private Fachada fachada = Fachada.getInstanciaUnica();
    private MainController mainController; // Referência para o controller principal


    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    // Inicializa a tabela de metas
    public void initialize() {
        // Configura as colunas para exibir os dados das metas.
        colunaDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colunaTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colunaProgresso.setCellValueFactory(new PropertyValueFactory<>("progressoAtual"));
        colunaDataConclusao.setCellValueFactory(new PropertyValueFactory<>("dataConclusao"));

        atualizarTabelaMetas(); // Carrega as metas na tabela
    }

    @FXML
    // Abre a tela para criar uma nova meta
    public void abrirTelaCriarMeta() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/criacao-meta-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Criar Nova Meta");
            stage.setScene(scene);

            // Define o controller da tela de criação e passa uma referência deste controller.
            CriacaoMetaController controller = loader.getController();
            controller.setMetaController(this);

            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao abrir tela", e.getMessage());
        }
    }


    @FXML
    // Atualiza uma meta existente
    public void atualizarMeta() {
        Meta metaSelecionada = tabelaMetas.getSelectionModel().getSelectedItem();

        if (metaSelecionada != null) {
            try {
                fachada.configurarMeta(metaSelecionada, metaSelecionada.getDescricao(),
                        metaSelecionada.getTipo(), metaSelecionada.getValorAlvo(),
                        metaSelecionada.getProgressoAtual(), metaSelecionada.getDataConclusao());

                atualizarTabelaMetas(); // Atualiza a tabela após a modificação
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
    // Remove uma meta existente.
    public void removerMeta() {
        Meta metaSelecionada = tabelaMetas.getSelectionModel().getSelectedItem();

        if (metaSelecionada != null) {
            try {
                fachada.configurarMeta(metaSelecionada, null, null, 0, 0, null);
                atualizarTabelaMetas(); // Atualiza a tabela após a remoção.
                mensagemMeta.setText("Meta removida com sucesso!");
            } catch (MetaNaoEncontradaException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao remover meta", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhuma meta selecionada",
                    "Por favor, selecione uma meta para remover.");
        }
    }

    // Atualiza os dados da tabela de metas.
    private void atualizarTabelaMetas() {
        try {
            List<Meta> listaMetas = fachada.listarMetas();
            tabelaMetas.setItems(FXCollections.observableArrayList(listaMetas));

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar metas", e.getMessage());
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