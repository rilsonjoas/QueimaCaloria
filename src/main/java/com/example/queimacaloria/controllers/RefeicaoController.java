package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.RefeicaoNaoEncontradaException;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.InicializadorDados;
import com.example.queimacaloria.negocio.Refeicao;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class RefeicaoController {

    @FXML private TableView<Refeicao> tabelaRefeicoesUsuario;
    @FXML private TableColumn<Refeicao, String> colunaNomeUsuario;
    @FXML private TableColumn<Refeicao, Integer> colunaCaloriasUsuario;
    @FXML private TableColumn<Refeicao, String> colunaMacronutrientesUsuario;

    @FXML private TableView<Refeicao> tabelaRefeicoesPreDefinidas;
    @FXML private TableColumn<Refeicao, String> colunaNomePreDefinida;
    @FXML private TableColumn<Refeicao, Integer> colunaCaloriasPreDefinida;
    @FXML private TableColumn<Refeicao, String> colunaMacronutrientesPreDefinida;

    @FXML private Label mensagemRefeicao;

    private Fachada fachada = Fachada.getInstanciaUnica();
    private MainController mainController;
    private ObservableList<Refeicao> refeicoesPreDefinidas = FXCollections.observableArrayList();

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        configurarTabelaUsuario();
        atualizarTabelaRefeicoesUsuario();
        configurarTabelaPreDefinida();
        carregarRefeicoesPreDefinidas();
    }

    private void configurarTabelaUsuario() {
        colunaNomeUsuario.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaCaloriasUsuario.setCellValueFactory(new PropertyValueFactory<>("calorias"));
        colunaMacronutrientesUsuario.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMacronutrientesFormatados()));
    }

    private void configurarTabelaPreDefinida() {
        colunaNomePreDefinida.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaCaloriasPreDefinida.setCellValueFactory(new PropertyValueFactory<>("calorias"));
        colunaMacronutrientesPreDefinida.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMacronutrientesFormatados()));
    }

    private void carregarRefeicoesPreDefinidas() {
        try {
            List<Refeicao> refeicoes = InicializadorDados.inicializarRefeicoes();
            refeicoesPreDefinidas.setAll(refeicoes);
            tabelaRefeicoesPreDefinidas.setItems(refeicoesPreDefinidas);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar refeições pré-definidas", e.getMessage());
        }
    }

    @FXML
    public void abrirTelaCriarRefeicao() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/criacao-refeicao-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Criar Nova Refeição");
            stage.setScene(scene);

            CriacaoRefeicaoController controller = loader.getController();
            controller.setRefeicaoController(this);
            controller.setMainController(mainController);

            stage.showAndWait();
            atualizarTabelaRefeicoesUsuario();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao abrir tela", e.getMessage());
        }
    }

    @FXML
    public void atualizarRefeicao() {
        Refeicao refeicaoSelecionada = tabelaRefeicoesUsuario.getSelectionModel().getSelectedItem();
        if (refeicaoSelecionada != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/edicao-refeicao-view.fxml"));
                Parent root = loader.load();

                EdicaoRefeicaoController controller = loader.getController();
                controller.setRefeicaoController(this);
                controller.setMainController(mainController);
                controller.setRefeicao(refeicaoSelecionada);

                Stage stage = new Stage();
                stage.setTitle("Editar Refeição");
                stage.setScene(new Scene(root));
                stage.showAndWait();
                atualizarTabelaRefeicoesUsuario();

            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao abrir tela de edição", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhuma refeição selecionada", "Selecione uma refeição para atualizar.");
        }
    }

    @FXML
    public void removerRefeicao() {
        Refeicao refeicaoSelecionada = tabelaRefeicoesUsuario.getSelectionModel().getSelectedItem();
        if (refeicaoSelecionada != null) {
            try {
                fachada.removerRefeicao(refeicaoSelecionada.getId());
                atualizarTabelaRefeicoesUsuario();
                mensagemRefeicao.setText("Refeição removida.");
            } catch (RefeicaoNaoEncontradaException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao remover refeição", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhuma refeição selecionada", "Selecione uma refeição para remover.");
        }
    }

    @FXML
    public void adicionarRefeicaoPreDefinida() {
        Refeicao refeicaoSelecionada = tabelaRefeicoesPreDefinidas.getSelectionModel().getSelectedItem();
        if (refeicaoSelecionada != null) {
            try {
                // Cria uma *NOVA* instância, copiando os valores, SEM usuário
                Refeicao novaRefeicao = new Refeicao(
                        refeicaoSelecionada.getNome(),
                        refeicaoSelecionada.getDescricao(),
                        refeicaoSelecionada.getCalorias(), //  Copia as calorias!
                        refeicaoSelecionada.getMacronutrientes() // Copia o Map
                );
                fachada.configurarRefeicao(novaRefeicao, novaRefeicao.getNome(),
                        novaRefeicao.getDescricao(), novaRefeicao.getMacronutrientes());
                atualizarTabelaRefeicoesUsuario(); //  ATUALIZA A TABELA *APÓS* ADICIONAR
                mensagemRefeicao.setText("Refeição adicionada com sucesso!");

            } catch (Exception e) { // Captura genérica, pois configurarRefeicao não lança exceções específicas
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao adicionar refeição", e.getMessage());
                e.printStackTrace(); // Imprime o stack trace para ajudar a debugar
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhuma refeição selecionada",
                    "Por favor, selecione uma refeição pré-definida para adicionar.");
        }
    }



    public void atualizarTabelaRefeicoesUsuario() {
        try {
            List<Refeicao> listaRefeicoes = fachada.listarRefeicoes();
            tabelaRefeicoesUsuario.setItems(FXCollections.observableArrayList(listaRefeicoes));
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

    @FXML
    public void voltarParaTelaPrincipal() {
        if (mainController != null) {
            mainController.mostrarTelaPrincipal();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro interno", "MainController não foi configurado.");
        }
    }
}