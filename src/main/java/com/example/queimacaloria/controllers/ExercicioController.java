package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.ExercicioNaoEncontradoException;
import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;
import com.example.queimacaloria.negocio.Exercicio;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.InicializadorDados;
import com.example.queimacaloria.negocio.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.Setter;

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

    @Setter
    private ExercicioController exercicioController;

    // Define o controlador principal.
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    // Inicializa o controlador, configurando as tabelas.
    @FXML
    public void initialize() {
        configurarTabelaUsuario();
        configurarTabelaPreDefinida();
        carregarExerciciosPreDefinidos();
    }

    // Configura a tabela de exercícios do usuário.
    private void configurarTabelaUsuario() {
        colunaNomeUsuario.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaTipoUsuario.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colunaTempoUsuario.setCellValueFactory(new PropertyValueFactory<>("tempo"));
        colunaCaloriasQueimadasUsuario.setCellValueFactory(cellData -> cellData.getValue().caloriasQueimadasProperty().asObject());
    }

    // Configura a tabela de exercícios pré-definidos.
    private void configurarTabelaPreDefinida() {
        colunaNomePreDefinido.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaTipoPreDefinido.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colunaTempoPreDefinido.setCellValueFactory(new PropertyValueFactory<>("tempo"));
        colunaCaloriasQueimadasPreDefinido.setCellValueFactory(cellData -> cellData.getValue().caloriasQueimadasProperty().asObject());
    }

    // Carrega os exercícios pré-definidos.
    private void carregarExerciciosPreDefinidos() {
        try {
            List<Exercicio> exercicios = InicializadorDados.inicializarExercicios();
            exerciciosPreDefinidos.setAll(exercicios);
            tabelaExerciciosPreDefinidos.setItems(exerciciosPreDefinidos);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar exercicios pré-definidos", e.getMessage());
        }
    }

    // Abre a tela de criação de exercício.
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
            controller.setMainController(mainController);

            stage.showAndWait();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao abrir tela", e.getMessage());
        }
    }

    // Abre a tela de edição de exercício.
    @FXML
    public void atualizarExercicio() {
        Exercicio exercicioSelecionado = tabelaExerciciosUsuario.getSelectionModel().getSelectedItem();
        if (exercicioSelecionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/edicao-exercicio-view.fxml"));
                Parent root = loader.load();

                EdicaoExercicioController controller = loader.getController();
                controller.setExercicioController(this);
                controller.setExercicio(exercicioSelecionado);
                controller.setMainController(mainController);


                Stage stage = new Stage();
                stage.setTitle("Editar Exercício");
                stage.setScene(new Scene(root));
                stage.showAndWait();

            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao abrir tela de edição", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhum exercício selecionado",
                    "Por favor, selecione um exercício para atualizar.");
        }
    }

    // Remove o exercício selecionado.
    @FXML
    public void removerExercicio() {
        Exercicio exercicioSelecionado = tabelaExerciciosUsuario.getSelectionModel().getSelectedItem();
        if (exercicioSelecionado != null) {
            try {
                fachada.removerExercicio(exercicioSelecionado.getId());
                atualizarTabelaExerciciosUsuario();
                mensagemExercicio.setText("Exercício removido com sucesso!");
                if (mainController != null) {
                    mainController.atualizarDadosTelaPrincipal();
                }
            } catch (ExercicioNaoEncontradoException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao remover exercício", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhum exercício selecionado",
                    "Por favor, selecione um exercício para remover.");
        }
    }

    // Adiciona um exercício pré-definido ao usuário.
    @FXML
    public void adicionarExercicioPreDefinido() {
        Exercicio exercicioSelecionado = tabelaExerciciosPreDefinidos.getSelectionModel().getSelectedItem();
        if (exercicioSelecionado != null) {
            try {
                Exercicio novoExercicio = new Exercicio(
                        exercicioSelecionado.getNome(),
                        exercicioSelecionado.getDescricao(),
                        exercicioSelecionado.getMusculosTrabalhados() != null ? new ArrayList<>(exercicioSelecionado.getMusculosTrabalhados()) : new ArrayList<>(),
                        exercicioSelecionado.getTipo(),
                        exercicioSelecionado.getTempo(),
                        exercicioSelecionado.getCaloriasQueimadas(),
                        false
                );

                fachada.configurarExercicio(novoExercicio, novoExercicio.getNome(),
                        novoExercicio.getDescricao(), novoExercicio.getTipo(),
                        novoExercicio.getTempo(), novoExercicio.getCaloriasQueimadas());

                atualizarTabelaExerciciosUsuario();
                mensagemExercicio.setText("Exercício adicionado com sucesso!");

                if (mainController != null) {
                    mainController.atualizarDadosTelaPrincipal();
                }
            } catch (ExercicioNaoEncontradoException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao adicionar exercício", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhum exercício selecionado",
                    "Por favor, selecione um exercício pré-definido para adicionar.");
        }
    }

    // Atualiza a tabela de exercícios do usuário.
    public void atualizarTabelaExerciciosUsuario() {
        try {
            List<Exercicio> listaExercicios = fachada.listarExercicios();

            if(mainController != null){
                mainController.getAtividadesRecentes().clear();
            }

            for(Exercicio exercicio : listaExercicios){
                if(mainController != null){
                    mainController.adicionarExercicioRecente(exercicio);
                }
            }

            tabelaExerciciosUsuario.setItems(FXCollections.observableArrayList(listaExercicios));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar exercicios", e.getMessage());
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

    // Volta para a tela principal.
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