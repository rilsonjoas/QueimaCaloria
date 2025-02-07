package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.ExercicioNaoEncontradoException;
import com.example.queimacaloria.negocio.Exercicio;
import com.example.queimacaloria.negocio.Fachada;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class ExercicioController {

    @FXML
    private TableView<Exercicio> tabelaExercicios;
    @FXML
    private TableColumn<Exercicio, String> colunaNome;
    @FXML
    private TableColumn<Exercicio, Exercicio.TipoExercicio> colunaTipo;
    @FXML
    private TableColumn<Exercicio, Integer> colunaTempo;
    @FXML
    private TableColumn<Exercicio, Double> colunaCaloriasQueimadas;
    @FXML
    private Label mensagemExercicio;

    private Fachada fachada = Fachada.getInstanciaUnica();

    @FXML
    public void initialize() {
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colunaTempo.setCellValueFactory(new PropertyValueFactory<>("tempo"));
        colunaCaloriasQueimadas.setCellValueFactory(new PropertyValueFactory<>("caloriasQueimadas"));

        atualizarTabelaExercicios();
    }

    @FXML
    public void abrirTelaCriarExercicio() {
        // TODO: Implementar a abertura da tela de criação (diálogo, nova janela, etc.)
        System.out.println("Abrir tela de criação de exercício (NÃO IMPLEMENTADO)");
    }

    @FXML
    public void atualizarExercicio() {
        Exercicio exercicioSelecionado = tabelaExercicios.getSelectionModel().getSelectedItem();
        if (exercicioSelecionado != null) {
            try {
                fachada.configurarExercicio(exercicioSelecionado, exercicioSelecionado.getNome(),
                        exercicioSelecionado.getDescricao(), exercicioSelecionado.getTipo(),
                        exercicioSelecionado.getTempo(), exercicioSelecionado.getCaloriasQueimadasPorMinuto());
                atualizarTabelaExercicios();
                mensagemExercicio.setText("Exercício atualizado com sucesso!");
            } catch (ExercicioNaoEncontradoException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar exercício", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhum exercício selecionado",
                    "Por favor, selecione um exercício para atualizar.");
        }
    }

    @FXML
    public void removerExercicio() {
        Exercicio exercicioSelecionado = tabelaExercicios.getSelectionModel().getSelectedItem();
        if (exercicioSelecionado != null) {
            try {

                fachada.configurarExercicio(exercicioSelecionado, null, null, null, 0, 0); // Define campos como nulos
                                                                                           // ou zero para "remover"
                atualizarTabelaExercicios();
                mensagemExercicio.setText("Exercício removido com sucesso!");
            } catch (ExercicioNaoEncontradoException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao remover exercício", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhum exercício selecionado",
                    "Por favor, selecione um exercício para remover.");
        }
    }

    private void atualizarTabelaExercicios() {

        try {
            List<Exercicio> listaExercicios = fachada.listarExercicios(); // PRECISA IMPLEMENTAR listarExercicios() na
                                                                          // Fachada e controladores
            tabelaExercicios.setItems(FXCollections.observableArrayList(listaExercicios));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar exercicios", e.getMessage());
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