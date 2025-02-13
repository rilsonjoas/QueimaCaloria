package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.ExercicioNaoEncontradoException;
import com.example.queimacaloria.negocio.Exercicio;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.InicializadorDados;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExercicioController {

    @FXML private TableView<Exercicio> tabelaExerciciosUsuario;
    @FXML private TableColumn<Exercicio, String> colunaNomeUsuario;
    @FXML private TableColumn<Exercicio, Exercicio.TipoExercicio> colunaTipoUsuario;
    @FXML private TableColumn<Exercicio, Integer> colunaTempoUsuario;
    @FXML private TableColumn<Exercicio, Double> colunaCaloriasQueimadasUsuario;

    @FXML private TableView<Exercicio> tabelaExerciciosPreDefinidos;
    @FXML private TableColumn<Exercicio, String> colunaNomePreDefinido;
    @FXML private TableColumn<Exercicio, Exercicio.TipoExercicio> colunaTipoPreDefinido;
    @FXML private TableColumn<Exercicio, Integer> colunaTempoPreDefinido;
    @FXML private TableColumn<Exercicio, Double> colunaCaloriasQueimadasPreDefinido;


    @FXML private Label mensagemExercicio;



    private Fachada fachada = Fachada.getInstanciaUnica();
    private MainController mainController;
    private ObservableList<Exercicio> exerciciosPreDefinidos = FXCollections.observableArrayList();

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        configurarTabelaUsuario();
        atualizarTabelaExerciciosUsuario();

        configurarTabelaPreDefinida();
        carregarExerciciosPreDefinidos();

        tabelaExerciciosUsuario.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                tabelaExerciciosPreDefinidos.getSelectionModel().clearSelection();
            }
        });

        tabelaExerciciosPreDefinidos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                tabelaExerciciosUsuario.getSelectionModel().clearSelection();
            }
        });
    }

    private void configurarTabelaUsuario() {
        colunaNomeUsuario.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaTipoUsuario.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colunaTempoUsuario.setCellValueFactory(new PropertyValueFactory<>("tempo"));
        colunaCaloriasQueimadasUsuario.setCellValueFactory(new PropertyValueFactory<>("caloriasQueimadas"));
    }

    private void configurarTabelaPreDefinida() {
        colunaNomePreDefinido.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaTipoPreDefinido.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colunaTempoPreDefinido.setCellValueFactory(new PropertyValueFactory<>("tempo"));
        colunaCaloriasQueimadasPreDefinido.setCellValueFactory(new PropertyValueFactory<>("caloriasQueimadas"));
    }

    private void carregarExerciciosPreDefinidos() {
        try {
            List<Exercicio> exercicios = InicializadorDados.inicializarExercicios(); // Método estático
            exerciciosPreDefinidos.setAll(exercicios);
            tabelaExerciciosPreDefinidos.setItems(exerciciosPreDefinidos);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar exercicios pré-definidos", e.getMessage());
        }
    }

    @FXML
    public void abrirTelaCriarExercicio() {
        try {
            String fxmlPath = "/com/example/queimacaloria/views/criacao-exercicio-view.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Criar Novo Exercício");
            stage.setScene(scene);

            CriacaoExercicioController controller = loader.getController();
            controller.setExercicioController(this);

            stage.showAndWait(); //Bloqueia a tela principal
            atualizarTabelaExerciciosUsuario(); //Atualiza após fechamento

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao abrir tela", e.getMessage());
        }
    }


    @FXML
    public void atualizarExercicio() {
        Exercicio exercicioSelecionado = tabelaExerciciosUsuario.getSelectionModel().getSelectedItem();
        if (exercicioSelecionado != null) {
            try {
                fachada.configurarExercicio(exercicioSelecionado, exercicioSelecionado.getNome(),
                        exercicioSelecionado.getDescricao(), exercicioSelecionado.getTipo(),
                        exercicioSelecionado.getTempo(), exercicioSelecionado.getCaloriasQueimadasPorMinuto());
                atualizarTabelaExerciciosUsuario();
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
        Exercicio exercicioSelecionado = tabelaExerciciosUsuario.getSelectionModel().getSelectedItem();
        if (exercicioSelecionado != null) {
            try {
                fachada.removerExercicio(exercicioSelecionado.getId()); //  Chama remover da fachada
                atualizarTabelaExerciciosUsuario(); //  ATUALIZA A TABELA
                mensagemExercicio.setText("Exercício removido com sucesso!");
            } catch (ExercicioNaoEncontradoException e) { // Captura a exceção
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao remover exercício", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhum exercício selecionado",
                    "Por favor, selecione um exercício para remover.");
        }
    }

    @FXML
    public void adicionarExercicioPreDefinido() {
        Exercicio exercicioSelecionado = tabelaExerciciosPreDefinidos.getSelectionModel().getSelectedItem();
        if (exercicioSelecionado != null) {
            try {
                // Cria uma *NOVA* instância, copiando os valores.
                // Usa construtor que trata corretamente o caso de lista nula.
                Exercicio novoExercicio = new Exercicio(
                        exercicioSelecionado.getNome(),
                        exercicioSelecionado.getDescricao(),
                        exercicioSelecionado.getMusculosTrabalhados() != null ? new ArrayList<>(exercicioSelecionado.getMusculosTrabalhados()) : new ArrayList<>(), // Copia ou cria nova.
                        exercicioSelecionado.getTipo(),
                        exercicioSelecionado.getTempo(),
                        exercicioSelecionado.getCaloriasQueimadasPorMinuto(),
                        false,
                        0.0
                );

                fachada.configurarExercicio(novoExercicio, novoExercicio.getNome(),
                        novoExercicio.getDescricao(), novoExercicio.getTipo(),
                        novoExercicio.getTempo(), novoExercicio.getCaloriasQueimadasPorMinuto());
                atualizarTabelaExerciciosUsuario(); // <--- ATUALIZA A TABELA
                mensagemExercicio.setText("Exercício adicionado com sucesso!");
            } catch (ExercicioNaoEncontradoException e){
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao adicionar exercício", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhum exercício selecionado",
                    "Por favor, selecione um exercício pré-definido para adicionar.");
        }
    }


    private void atualizarTabelaExerciciosUsuario() {
        try {
            List<Exercicio> listaExercicios = fachada.listarExercicios();
            tabelaExerciciosUsuario.setItems(FXCollections.observableArrayList(listaExercicios));
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