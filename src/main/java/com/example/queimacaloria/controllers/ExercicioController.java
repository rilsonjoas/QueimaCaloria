package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.ExercicioNaoEncontradoException;
import com.example.queimacaloria.negocio.Exercicio;
import com.example.queimacaloria.negocio.Fachada;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

// Controller para a tela de gerenciamento de exercícios
public class ExercicioController {

    @FXML private TableView<Exercicio> tabelaExercicios;
    @FXML private TableColumn<Exercicio, String> colunaNome;
    @FXML private TableColumn<Exercicio, Exercicio.TipoExercicio> colunaTipo;
    @FXML private TableColumn<Exercicio, Integer> colunaTempo;
    @FXML private TableColumn<Exercicio, Double> colunaCaloriasQueimadas;
    @FXML private Label mensagemExercicio;

    private Fachada fachada = Fachada.getInstanciaUnica();
    private MainController mainController; // Referência para o controller principal

    // Injeta o MainController para que se possa voltar à tela principal
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    // Inicializa a tabela de exercícios
    public void initialize() {
        // Configura as colunas da tabela para exibir os dados do Exercício
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colunaTempo.setCellValueFactory(new PropertyValueFactory<>("tempo"));
        colunaCaloriasQueimadas.setCellValueFactory(new PropertyValueFactory<>("caloriasQueimadas"));

        atualizarTabelaExercicios(); // Carrega os exercícios na tabela
    }

    @FXML
    // Abre a tela para criar um novo exercício
    public void abrirTelaCriarExercicio() {
        try {
            String fxmlPath = "/com/example/queimacaloria/views/criacao-exercicio-view.fxml";
            System.out.println("Caminho do FXML: " + getClass().getResource(fxmlPath)); // Depuração

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Criar Novo Exercício");
            stage.setScene(scene);

            CriacaoExercicioController controller = loader.getController();
            controller.setExercicioController(this); // Injeta a referência a este controller

            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao abrir tela", e.getMessage());
        }
    }

    @FXML
    // Atualiza um exercício existente
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
    // Remove um exercício existente
    public void removerExercicio() {
        Exercicio exercicioSelecionado = tabelaExercicios.getSelectionModel().getSelectedItem();
        if (exercicioSelecionado != null) {
            try {

                fachada.configurarExercicio(exercicioSelecionado, null, null, null, 0, 0);
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

    // Atualiza a tabela de exercícios com os dados do repositório
    private void atualizarTabelaExercicios() {

        try {
            List<Exercicio> listaExercicios = fachada.listarExercicios();
            tabelaExercicios.setItems(FXCollections.observableArrayList(listaExercicios));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar exercicios", e.getMessage());
        }

    }

    // Exibe um alerta na tela
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