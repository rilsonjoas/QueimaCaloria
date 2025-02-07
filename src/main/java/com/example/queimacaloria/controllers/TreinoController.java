package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.TreinoNaoEncontradoException;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Treino;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class TreinoController {

    @FXML
    private TableView<Treino> tabelaTreinos;
    @FXML
    private TableColumn<Treino, String> colunaNome;
    @FXML
    private TableColumn<Treino, String> colunaTipoTreino;
    @FXML
    private TableColumn<Treino, Integer> colunaDuracao;
    @FXML
    private TableColumn<Treino, Integer> colunaNivelDificuldade;
    @FXML
    private TableColumn<Treino, Double> colunaProgresso;
    @FXML
    private Label mensagemTreino;

    private Fachada fachada = Fachada.getInstanciaUnica();

    @FXML
    public void initialize() {
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaTipoTreino.setCellValueFactory(new PropertyValueFactory<>("tipoDeTreino"));
        colunaDuracao.setCellValueFactory(new PropertyValueFactory<>("duracao"));
        colunaNivelDificuldade.setCellValueFactory(new PropertyValueFactory<>("nivelDeDificuldade"));
        colunaProgresso.setCellValueFactory(new PropertyValueFactory<>("progresso")); // Use progresso, não um método
        atualizarTabelaTreinos();
    }

    @FXML
    public void abrirTelaCriarTreino() {
        // TODO: Implementar abertura da tela de criação de treino.
        System.out.println("Abrir tela de criação de treino (NÃO IMPLEMENTADO)");
    }

    @FXML
    public void atualizarTreino() {
        Treino treinoSelecionado = tabelaTreinos.getSelectionModel().getSelectedItem();
        if (treinoSelecionado != null) {
            try {

                fachada.configurarTreino(treinoSelecionado, treinoSelecionado.getNome(),
                        treinoSelecionado.getTipoDeTreino(), treinoSelecionado.getDuracao(),
                        treinoSelecionado.getNivelDeDificuldade());
                atualizarTabelaTreinos();
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
    public void removerTreino() {
        Treino treinoSelecionado = tabelaTreinos.getSelectionModel().getSelectedItem();
        if (treinoSelecionado != null) {
            try {
                fachada.configurarTreino(treinoSelecionado, null, null, 0, 0); // Define campos como nulos ou zero para
                                                                               // "remover"
                atualizarTabelaTreinos();
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
            List<Treino> listaTreinos = fachada.listarTreinos(); // PRECISA IMPLEMENTAR listarTreinos() na fachada e
                                                                 // controladores
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
}