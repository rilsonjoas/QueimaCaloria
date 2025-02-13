
package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.TreinoNaoEncontradoException;
import com.example.queimacaloria.negocio.Exercicio;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.InicializadorDados;
import com.example.queimacaloria.negocio.Treino;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TreinoController {

    @FXML private TableView<Treino> tabelaTreinosUsuario;
    @FXML private TableColumn<Treino, String> colunaNomeUsuario;
    @FXML private TableColumn<Treino, String> colunaTipoTreinoUsuario;
    @FXML private TableColumn<Treino, Integer> colunaDuracaoUsuario;
    @FXML private TableColumn<Treino, Integer> colunaNivelDificuldadeUsuario;
    @FXML private TableColumn<Treino, Double> colunaProgressoUsuario;

    @FXML private TableView<Treino> tabelaTreinosPreDefinidos;
    @FXML private TableColumn<Treino, String> colunaNomePreDefinido;
    @FXML private TableColumn<Treino, String> colunaTipoTreinoPreDefinido;
    @FXML private TableColumn<Treino, Integer> colunaDuracaoPreDefinido;
    @FXML private TableColumn<Treino, Integer> colunaNivelDificuldadePreDefinido;
    @FXML private TableColumn<Treino, Double> colunaProgressoPreDefinido; // Mesmo progresso

    @FXML private Label mensagemTreino;
    @FXML private Label labelNomeTreino;
    @FXML private Label labelTipoTreino;
    @FXML private Label labelDuracaoTreino;
    @FXML private Label labelDificuldadeTreino;
    @FXML private Label labelExerciciosTreino;

    private Fachada fachada = Fachada.getInstanciaUnica();
    private MainController mainController;
    private ObservableList<Treino> treinosPreDefinidos = FXCollections.observableArrayList();

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        configurarTabelaUsuario();
        atualizarTabelaTreinosUsuario();

        configurarTabelaPreDefinida();
        carregarTreinosPreDefinidos();

        tabelaTreinosUsuario.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                tabelaTreinosPreDefinidos.getSelectionModel().clearSelection();
                exibirDetalhesTreino(newSelection);
            }
        });

        tabelaTreinosPreDefinidos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                tabelaTreinosUsuario.getSelectionModel().clearSelection();
                exibirDetalhesTreino(newSelection);
            }
        });
    }

    private void configurarTabelaUsuario() {
        colunaNomeUsuario.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaTipoTreinoUsuario.setCellValueFactory(new PropertyValueFactory<>("tipoDeTreino"));
        colunaDuracaoUsuario.setCellValueFactory(new PropertyValueFactory<>("duracao"));
        colunaNivelDificuldadeUsuario.setCellValueFactory(new PropertyValueFactory<>("nivelDeDificuldade"));
        colunaProgressoUsuario.setCellValueFactory(new PropertyValueFactory<>("progresso"));
    }

    private void configurarTabelaPreDefinida() {
        colunaNomePreDefinido.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaTipoTreinoPreDefinido.setCellValueFactory(new PropertyValueFactory<>("tipoDeTreino"));
        colunaDuracaoPreDefinido.setCellValueFactory(new PropertyValueFactory<>("duracao"));
        colunaNivelDificuldadePreDefinido.setCellValueFactory(new PropertyValueFactory<>("nivelDeDificuldade"));
        colunaProgressoPreDefinido.setCellValueFactory(new PropertyValueFactory<>("progresso"));
    }

    private void carregarTreinosPreDefinidos() {
        try {
            List<Treino> treinos = InicializadorDados.inicializarTreinos();
            treinosPreDefinidos.setAll(treinos);
            tabelaTreinosPreDefinidos.setItems(treinosPreDefinidos);
        } catch (Exception e){
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar treinos pré-definidos", e.getMessage());
        }
    }

    @FXML
    public void abrirTelaCriarTreino() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/criacao-treino-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Criar Novo Treino");
            stage.setScene(scene);

            CriacaoTreinoController controller = loader.getController();
            controller.setTreinoController(this);

            stage.showAndWait();
            atualizarTabelaTreinosUsuario();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao abrir tela", e.getMessage());
        }
    }

    @FXML
    public void atualizarTreino() {
        Treino treinoSelecionado = tabelaTreinosUsuario.getSelectionModel().getSelectedItem();
        if (treinoSelecionado != null) {
            try {
                fachada.configurarTreino(treinoSelecionado, treinoSelecionado.getNome(),
                        treinoSelecionado.getTipoDeTreino(), treinoSelecionado.getDuracao(),
                        treinoSelecionado.getNivelDeDificuldade());
                atualizarTabelaTreinosUsuario();
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
        Treino treinoSelecionado = tabelaTreinosUsuario.getSelectionModel().getSelectedItem();
        if (treinoSelecionado != null) {
            try {
                fachada.removerTreino(treinoSelecionado.getId()); //  Chama remover da fachada
                atualizarTabelaTreinosUsuario(); //  ATUALIZA A TABELA
                mensagemTreino.setText("Treino removido com sucesso!");
            } catch (TreinoNaoEncontradoException e) { // Captura a exceção
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao remover treino", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhum treino selecionado",
                    "Por favor, selecione um treino para remover.");
        }
    }
    @FXML
    public void adicionarTreinoPreDefinido() {
        Treino treinoSelecionado = tabelaTreinosPreDefinidos.getSelectionModel().getSelectedItem();
        if (treinoSelecionado != null) {
            try {
                // Cria uma *NOVA* instância, copiando os valores.
                Treino novoTreino = new Treino(
                        treinoSelecionado.getNome(),
                        treinoSelecionado.getTipoDeTreino(),
                        treinoSelecionado.getDuracao(),
                        treinoSelecionado.getNivelDeDificuldade(),
                        new ArrayList<>(treinoSelecionado.getExercicios()), // Copia a lista de exercícios
                        treinoSelecionado.getCaloriasQueimadas(),
                        treinoSelecionado.getProgresso(),
                        treinoSelecionado.isConcluido()
                );
                fachada.configurarTreino(novoTreino, novoTreino.getNome(),
                        novoTreino.getTipoDeTreino(), novoTreino.getDuracao(),
                        novoTreino.getNivelDeDificuldade());
                atualizarTabelaTreinosUsuario();
                mensagemTreino.setText("Treino adicionado com sucesso!");
            } catch (TreinoNaoEncontradoException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao adicionar treino", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhum treino selecionado",
                    "Por favor, selecione um treino pré-definido para adicionar.");
        }
    }


    private void atualizarTabelaTreinosUsuario() {
        try {
            List<Treino> listaTreinos = fachada.listarTreinos();
            tabelaTreinosUsuario.setItems(FXCollections.observableArrayList(listaTreinos)); //MUITO IMPORTANTE
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar treinos", e.getMessage());
        }
    }

    private void exibirDetalhesTreino(Treino treino) {
        if (treino != null) {
            labelNomeTreino.setText("Nome: " + treino.getNome());
            labelTipoTreino.setText("Tipo: " + treino.getTipoDeTreino());
            labelDuracaoTreino.setText("Duração: " + treino.getDuracao() + " min");
            labelDificuldadeTreino.setText("Dificuldade: " + treino.getNivelDeDificuldade());

            // Lista os exercícios do treino (melhorado)
            if (treino.getExercicios() != null && !treino.getExercicios().isEmpty()) {
                String exerciciosStr = treino.getExercicios().stream()
                        .map(Exercicio::getNome)
                        .collect(Collectors.joining(", "));
                labelExerciciosTreino.setText("Exercícios: " + exerciciosStr);
            } else {
                labelExerciciosTreino.setText("Exercícios: Nenhum");
            }
        } else {
            labelNomeTreino.setText("Nome: ");
            labelTipoTreino.setText("Tipo: ");
            labelDuracaoTreino.setText("Duração: ");
            labelDificuldadeTreino.setText("Dificuldade: ");
            labelExerciciosTreino.setText("Exercícios: ");
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