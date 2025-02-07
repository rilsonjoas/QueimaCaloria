package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.TreinoNaoEncontradoException;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Treino;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import java.util.List;

// Controller para a tela de gerenciamento de treinos.
public class TreinoController {

    @FXML private TableView<Treino> tabelaTreinos;
    @FXML private TableColumn<Treino, String> colunaNome;
    @FXML private TableColumn<Treino, String> colunaTipoTreino;
    @FXML private TableColumn<Treino, Integer> colunaDuracao;
    @FXML private TableColumn<Treino, Integer> colunaNivelDificuldade;
    @FXML private TableColumn<Treino, Double> colunaProgresso;
    @FXML private Label mensagemTreino;

    private Fachada fachada = Fachada.getInstanciaUnica();
    private MainController mainController;  // Referência ao MainController

    // Injeta o MainController para permitir a volta à tela principal.
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    // Inicializa a tabela de treinos.
    public void initialize() {
        // Configura as colunas da tabela para exibir os dados do Treino.
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaTipoTreino.setCellValueFactory(new PropertyValueFactory<>("tipoDeTreino"));
        colunaDuracao.setCellValueFactory(new PropertyValueFactory<>("duracao"));
        colunaNivelDificuldade.setCellValueFactory(new PropertyValueFactory<>("nivelDeDificuldade"));
        colunaProgresso.setCellValueFactory(new PropertyValueFactory<>("progresso"));

        atualizarTabelaTreinos(); // Carrega os treinos na tabela
    }

    @FXML
    // Abre a tela para criar um novo treino.
    public void abrirTelaCriarTreino() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/criacao-treino-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Criar Novo Treino");
            stage.setScene(scene);

            CriacaoTreinoController controller = loader.getController();
            controller.setTreinoController(this); // Passa a referência deste controller para o controller de criação.


            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao abrir tela", e.getMessage());
        }
    }

    @FXML
    // Atualiza um treino existente.
    public void atualizarTreino() {
        Treino treinoSelecionado = tabelaTreinos.getSelectionModel().getSelectedItem();
        if (treinoSelecionado != null) {
            try {

                fachada.configurarTreino(treinoSelecionado, treinoSelecionado.getNome(),
                        treinoSelecionado.getTipoDeTreino(), treinoSelecionado.getDuracao(),
                        treinoSelecionado.getNivelDeDificuldade());
                atualizarTabelaTreinos(); // Atualiza a tabela após a modificação.
                mensagemTreino.setText("Treino atualizado com sucesso!");
            } catch (TreinoNaoEncontradoException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar treino", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhum treino selecionado",
                    "Por favor, selecione um treino para atualizar.");
        }
    }

    @FXML
    // Remove um treino existente.
    public void removerTreino() {
        Treino treinoSelecionado = tabelaTreinos.getSelectionModel().getSelectedItem();
        if (treinoSelecionado != null) {
            try {
                fachada.configurarTreino(treinoSelecionado, null, null, 0, 0); // Remove o treino.
                atualizarTabelaTreinos(); // Atualiza a tabela após a remoção.
                mensagemTreino.setText("Treino removido com sucesso!");
            } catch (TreinoNaoEncontradoException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao remover treino", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhum treino selecionado",
                    "Por favor, selecione um treino para remover.");
        }
    }

    private void atualizarTabelaTreinos() {
        try {
            List<Treino> listaTreinos = fachada.listarTreinos();
            tabelaTreinos.setItems(FXCollections.observableArrayList(listaTreinos));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar treinos", e.getMessage());
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
