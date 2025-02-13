
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
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class MetaController {

    @FXML private TableView<Meta> tabelaMetasUsuario;
    @FXML private TableColumn<Meta, String> colunaDescricaoUsuario;
    @FXML private TableColumn<Meta, Meta.Tipo> colunaTipoUsuario;
    @FXML private TableColumn<Meta, Double> colunaProgressoUsuario;
    @FXML private TableColumn<Meta, LocalDate> colunaDataConclusaoUsuario;

    @FXML private TableView<Meta> tabelaMetasPreDefinidas;
    @FXML private TableColumn<Meta, String> colunaDescricaoPreDefinida;
    @FXML private TableColumn<Meta, Meta.Tipo> colunaTipoPreDefinida;
    @FXML private TableColumn<Meta, Double> colunaProgressoPreDefinida;
    @FXML private TableColumn<Meta, LocalDate> colunaDataConclusaoPreDefinida;

    @FXML private Label mensagemMeta;


    private Fachada fachada = Fachada.getInstanciaUnica();
    private MainController mainController;
    private ObservableList<Meta> metasPreDefinidas = FXCollections.observableArrayList();


    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        configurarTabelaUsuario();
        atualizarTabelaMetasUsuario();

        configurarTabelaPreDefinida();
        carregarMetasPreDefinidas();

        tabelaMetasUsuario.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                tabelaMetasPreDefinidas.getSelectionModel().clearSelection();
            }
        });

        tabelaMetasPreDefinidas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                tabelaMetasUsuario.getSelectionModel().clearSelection();
            }
        });
    }

    private void configurarTabelaUsuario() {
        colunaDescricaoUsuario.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colunaTipoUsuario.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colunaProgressoUsuario.setCellValueFactory(new PropertyValueFactory<>("progressoAtual"));
        colunaDataConclusaoUsuario.setCellValueFactory(new PropertyValueFactory<>("dataConclusao"));
    }
    private void configurarTabelaPreDefinida() {
        colunaDescricaoPreDefinida.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colunaTipoPreDefinida.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colunaProgressoPreDefinida.setCellValueFactory(new PropertyValueFactory<>("progressoAtual"));
        colunaDataConclusaoPreDefinida.setCellValueFactory(new PropertyValueFactory<>("dataConclusao"));
    }


    private void carregarMetasPreDefinidas() {
        try {
            List<Meta> metas = InicializadorDados.inicializarMetas(); // Método estático
            metasPreDefinidas.setAll(metas);
            tabelaMetasPreDefinidas.setItems(metasPreDefinidas);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar metas pré-definidas", e.getMessage());
        }
    }


    @FXML
    public void abrirTelaCriarMeta() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/criacao-meta-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Criar Nova Meta");
            stage.setScene(scene);

            CriacaoMetaController controller = loader.getController();
            controller.setMetaController(this);
            stage.showAndWait();
            atualizarTabelaMetasUsuario();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao abrir tela", e.getMessage());
        }
    }

    @FXML
    public void atualizarMeta() {
        Meta metaSelecionada = tabelaMetasUsuario.getSelectionModel().getSelectedItem();
        if (metaSelecionada != null) {
            try {
                fachada.configurarMeta(metaSelecionada, metaSelecionada.getDescricao(),
                        metaSelecionada.getTipo(), metaSelecionada.getValorAlvo(),
                        metaSelecionada.getProgressoAtual(), metaSelecionada.getDataConclusao());
                atualizarTabelaMetasUsuario();
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
        Meta metaSelecionada = tabelaMetasUsuario.getSelectionModel().getSelectedItem();
        if (metaSelecionada != null) {
            try {
                fachada.removerMeta(metaSelecionada.getId()); //  Chama remover da fachada
                atualizarTabelaMetasUsuario(); //  ATUALIZA A TABELA
                mensagemMeta.setText("Meta removida com sucesso!");
            } catch (MetaNaoEncontradaException e) { // Captura a exceção
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao remover meta", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhuma meta selecionada",
                    "Por favor, selecione uma meta para remover.");
        }
    }

    @FXML
    public void adicionarMetaPreDefinida() {
        Meta metaSelecionada = tabelaMetasPreDefinidas.getSelectionModel().getSelectedItem();
        if (metaSelecionada != null) {
            try {
                // Cria uma *NOVA* instância, copiando os valores.
                Meta novaMeta = new Meta(
                        metaSelecionada.getDescricao(),
                        metaSelecionada.getTipo(),
                        metaSelecionada.getValorAlvo(),
                        metaSelecionada.getProgressoAtual(),
                        metaSelecionada.getDataCriacao(),
                        metaSelecionada.getDataConclusao() != null ? metaSelecionada.getDataConclusao() : null // Mantém a data de conclusão se existir, senão, define como null.
                );
                fachada.configurarMeta(novaMeta, novaMeta.getDescricao(), novaMeta.getTipo(),
                        novaMeta.getValorAlvo(), novaMeta.getProgressoAtual(), novaMeta.getDataConclusao());
                atualizarTabelaMetasUsuario();
                mensagemMeta.setText("Meta adicionada com sucesso!");

            } catch (MetaNaoEncontradaException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao adicionar meta", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhuma meta selecionada",
                    "Por favor, selecione uma meta pré-definida para adicionar.");
        }
    }



    private void atualizarTabelaMetasUsuario() {
        try {
            List<Meta> listaMetas = fachada.listarMetas();
            tabelaMetasUsuario.setItems(FXCollections.observableArrayList(listaMetas)); //MUITO IMPORTANTE
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

    @FXML
    public void voltarParaTelaPrincipal() {
        if (mainController != null) {
            mainController.mostrarTelaPrincipal();
        } else {
            System.err.println("Erro: MainController não foi injetado!");
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro interno", "MainController não foi configurado corretamente.");
        }
    }
}