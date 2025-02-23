package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.TreinoNaoEncontradoException;
import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;
import com.example.queimacaloria.negocio.Exercicio;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.InicializadorDados;
import com.example.queimacaloria.negocio.Treino;
import com.example.queimacaloria.negocio.Usuario;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TreinoController {

    @FXML private TableView<Treino> tabelaTreinosUsuario;
    @FXML private TableColumn<Treino, String> colunaNomeUsuario;
    @FXML private TableColumn<Treino, String> colunaTipoTreinoUsuario;
    @FXML private TableColumn<Treino, Integer> colunaDuracaoUsuario;
    @FXML private TableColumn<Treino, Integer> colunaNivelDificuldadeUsuario;

    @FXML private TableView<Treino> tabelaTreinosPreDefinidos;
    @FXML private TableColumn<Treino, String> colunaNomePreDefinido;
    @FXML private TableColumn<Treino, String> colunaTipoTreinoPreDefinido;
    @FXML private TableColumn<Treino, Integer> colunaDuracaoPreDefinido;
    @FXML private TableColumn<Treino, Integer> colunaNivelDificuldadePreDefinido;

    @FXML private Label mensagemTreino;
    @FXML private Label labelNomeTreino;
    @FXML private Label labelTipoTreino;
    @FXML private Label labelDuracaoTreino;
    @FXML private Label labelDificuldadeTreino;
    @FXML private Label labelExerciciosTreino;

    private Fachada fachada = Fachada.getInstanciaUnica();
    private MainController mainController;
    private ObservableList<Treino> treinosPreDefinidos = FXCollections.observableArrayList();

    // Define o controlador principal.
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    // Inicializa o controlador, configurando as tabelas e listeners.
    @FXML
    public void initialize() {
        configurarTabelaUsuario();
        atualizarTabelaTreinosUsuario();
        configurarTabelaPreDefinida();
        carregarTreinosPreDefinidos();

        tabelaTreinosUsuario.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    exibirDetalhesTreino(newValue);
                }
        );
    }

    // Configura a tabela de treinos do usuário.
    private void configurarTabelaUsuario() {
        colunaNomeUsuario.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaTipoTreinoUsuario.setCellValueFactory(new PropertyValueFactory<>("tipoDeTreino"));
        colunaDuracaoUsuario.setCellValueFactory(new PropertyValueFactory<>("duracao"));
        colunaNivelDificuldadeUsuario.setCellValueFactory(new PropertyValueFactory<>("nivelDeDificuldade"));
    }

    // Configura a tabela de treinos pré-definidos.
    private void configurarTabelaPreDefinida() {
        colunaNomePreDefinido.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaTipoTreinoPreDefinido.setCellValueFactory(new PropertyValueFactory<>("tipoDeTreino"));
        colunaDuracaoPreDefinido.setCellValueFactory(new PropertyValueFactory<>("duracao"));
        colunaNivelDificuldadePreDefinido.setCellValueFactory(new PropertyValueFactory<>("nivelDeDificuldade"));
    }

    // Carrega os treinos pré-definidos.
    private void carregarTreinosPreDefinidos() {
        try {
            List<Treino> treinos = InicializadorDados.inicializarTreinos();
            treinosPreDefinidos.setAll(treinos);
            tabelaTreinosPreDefinidos.setItems(treinosPreDefinidos);
        } catch (Exception e){
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar treinos pré-definidos", e.getMessage());
        }
    }

    // Abre a tela de criação de treino.
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
            controller.setMainController(mainController);

            stage.showAndWait();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao abrir tela", e.getMessage());
        }
    }

    // Abre a tela de edição de treino.
    @FXML
    public void atualizarTreino() {
        Treino treinoSelecionado = tabelaTreinosUsuario.getSelectionModel().getSelectedItem();
        if (treinoSelecionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/edicao-treino-view.fxml"));
                Parent root = loader.load();

                EdicaoTreinoController controller = loader.getController();
                controller.setTreinoController(this);
                controller.setMainController(mainController);
                System.out.println("TreinoController.atualizarTreino(): Treino selecionado ID: " + treinoSelecionado.getId());
                controller.setTreino(treinoSelecionado);

                Stage stage = new Stage();
                stage.setTitle("Editar Treino");
                stage.setScene(new Scene(root));
                stage.showAndWait();

            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao abrir tela de edição", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhum treino selecionado",
                    "Por favor, selecione um treino para atualizar.");
        }
    }

    // Remove o treino selecionado.
    @FXML
    public void removerTreino() {
        Treino treinoSelecionado = tabelaTreinosUsuario.getSelectionModel().getSelectedItem();
        if (treinoSelecionado != null) {
            try {
                fachada.removerTreino(treinoSelecionado.getId());
                atualizarTabelaTreinosUsuario();
                mensagemTreino.setText("Treino removido com sucesso!");

                if (mainController != null && mainController.getUsuarioLogado() != null) {
                    try {
                        Usuario usuarioAtualizado = fachada.buscarUsuarioPorId(mainController.getUsuarioLogado().getId());
                        mainController.setUsuarioLogado(usuarioAtualizado);
                    } catch (UsuarioNaoEncontradoException e) {
                        showAlert(Alert.AlertType.ERROR, "Erro", "Usuário não encontrado.",
                                "O usuário logado não pôde ser encontrado após a remoção do treino.");
                    }
                }

            } catch (TreinoNaoEncontradoException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao remover treino", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhum treino selecionado",
                    "Por favor, selecione um treino para remover.");
        }
    }

    // Adiciona um treino pré-definido ao usuário.
    @FXML
    public void adicionarTreinoPreDefinido() {
        Treino treinoSelecionado = tabelaTreinosPreDefinidos.getSelectionModel().getSelectedItem();
        if (treinoSelecionado != null) {
            try {
                Treino novoTreino = new Treino(
                        treinoSelecionado.getNome(),
                        treinoSelecionado.getTipoDeTreino(),
                        treinoSelecionado.getDuracao(),
                        treinoSelecionado.getNivelDeDificuldade(),
                        new ArrayList<>(treinoSelecionado.getExercicios()),
                        treinoSelecionado.getCaloriasQueimadas(),
                        treinoSelecionado.getProgresso(),
                        treinoSelecionado.isConcluido()
                );
                fachada.configurarTreino(novoTreino, novoTreino.getNome(),
                        novoTreino.getTipoDeTreino(), novoTreino.getDuracao(),
                        novoTreino.getNivelDeDificuldade());
                atualizarTabelaTreinosUsuario();
                mensagemTreino.setText("Treino adicionado com sucesso!");

                if (mainController != null && mainController.getUsuarioLogado() != null) {
                    try {
                        Usuario usuarioAtualizado = fachada.buscarUsuarioPorId(mainController.getUsuarioLogado().getId());
                        mainController.setUsuarioLogado(usuarioAtualizado);
                    } catch (UsuarioNaoEncontradoException e) {
                        showAlert(Alert.AlertType.ERROR, "Erro", "Usuário não encontrado.",
                                "O usuário logado não pôde ser encontrado após a adição do treino.");
                    }
                }

            } catch (TreinoNaoEncontradoException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao adicionar treino", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhum treino selecionado",
                    "Por favor, selecione um treino pré-definido para adicionar.");
        }
    }

    // Atualiza a tabela de treinos do usuário.
    public void atualizarTabelaTreinosUsuario() {
        try {
            List<Treino> listaTreinos = fachada.listarTreinos();
            tabelaTreinosUsuario.setItems(FXCollections.observableArrayList(listaTreinos));
            tabelaTreinosUsuario.refresh();

            if (!tabelaTreinosUsuario.getItems().isEmpty()) {
                tabelaTreinosUsuario.getSelectionModel().select(0);
                exibirDetalhesTreino(tabelaTreinosUsuario.getItems().get(0));
            } else {
                exibirDetalhesTreino(null);
            }

        }

        catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar treinos", e.getMessage());
        }
    }

    // Exibe os detalhes de um treino selecionado.
    private void exibirDetalhesTreino(Treino treino) {
        if (treino != null) {
            if(labelNomeTreino != null) labelNomeTreino.setText("Nome: " + treino.getNome());
            if(labelTipoTreino != null) labelTipoTreino.setText("Tipo: " + treino.getTipoDeTreino());
            if(labelDuracaoTreino != null) labelDuracaoTreino.setText("Duração: " + treino.getDuracao() + " min");
            if(labelDificuldadeTreino != null) labelDificuldadeTreino.setText("Dificuldade: " + treino.getNivelDeDificuldade());

            if (treino.getExercicios() != null && !treino.getExercicios().isEmpty()) {
                String exerciciosStr = treino.getExercicios().stream()
                        .map(Exercicio::getNome)
                        .collect(Collectors.joining(", "));
                if(labelExerciciosTreino != null) labelExerciciosTreino.setText("Exercícios: " + exerciciosStr);
            } else {
                if(labelExerciciosTreino != null) labelExerciciosTreino.setText("Exercícios: Nenhum");
            }
        } else {
            if(labelNomeTreino != null) labelNomeTreino.setText("Nome: ");
            if(labelTipoTreino != null) labelTipoTreino.setText("Tipo: ");
            if(labelDuracaoTreino != null) labelDuracaoTreino.setText("Duração: ");
            if(labelDificuldadeTreino != null) labelDificuldadeTreino.setText("Dificuldade: ");
            if(labelExerciciosTreino != null) labelExerciciosTreino.setText("Exercícios: ");
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
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro interno", "MainController não foi configurado corretamente.");
        }
    }
}