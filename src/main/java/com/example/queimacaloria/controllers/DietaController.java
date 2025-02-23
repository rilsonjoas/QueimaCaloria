package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;
import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;
import com.example.queimacaloria.negocio.*;
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
import java.util.List;

public class DietaController {

    @FXML private TableView<Dieta> tabelaDietasUsuario;
    @FXML private TableColumn<Dieta, String> colunaNomeUsuario;
    @FXML private TableColumn<Dieta, Dieta.ObjetivoDieta> colunaObjetivoUsuario;
    @FXML private TableColumn<Dieta, Integer> colunaCaloriasUsuario;

    @FXML private TableView<Dieta> tabelaDietasPreDefinidas;
    @FXML private TableColumn<Dieta, String> colunaNomePreDefinida;
    @FXML private TableColumn<Dieta, Dieta.ObjetivoDieta> colunaObjetivoPreDefinida;
    @FXML private TableColumn<Dieta, Integer> colunaCaloriasPreDefinida;

    @FXML private Label mensagemDieta;

    private Fachada fachada = Fachada.getInstanciaUnica();
    private MainController mainController;
    private ObservableList<Dieta> dietasPreDefinidas = FXCollections.observableArrayList();

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        configurarTabelaUsuario();
        configurarTabelaPreDefinida();
        carregarDietasPreDefinidas();
        atualizarTabelaDietasUsuario(); // Importante para carregar ao iniciar
    }

    private void configurarTabelaUsuario() {
        colunaNomeUsuario.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaObjetivoUsuario.setCellValueFactory(new PropertyValueFactory<>("objetivo"));
        colunaCaloriasUsuario.setCellValueFactory(new PropertyValueFactory<>("caloriasDiarias"));
    }

    private void configurarTabelaPreDefinida() {
        colunaNomePreDefinida.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaObjetivoPreDefinida.setCellValueFactory(new PropertyValueFactory<>("objetivo"));
        colunaCaloriasPreDefinida.setCellValueFactory(new PropertyValueFactory<>("caloriasDiarias"));
    }

    private void carregarDietasPreDefinidas() {
        try {
            List<Dieta> dietas = InicializadorDados.inicializarDietas();
            dietasPreDefinidas.setAll(dietas);
            tabelaDietasPreDefinidas.setItems(dietasPreDefinidas);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar dietas pré-definidas", e.getMessage());
        }
    }

    @FXML
    public void abrirTelaCriacaoDieta() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/criacao-dieta-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Criar Nova Dieta");
            stage.setScene(scene);

            CriacaoDietaController controller = loader.getController();
            controller.setDietaController(this);
            controller.setMainController(mainController);
            stage.showAndWait();
            atualizarTabelaDietasUsuario(); // Atualiza após criar

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao abrir tela", e.getMessage());
        }
    }

    @FXML
    public void realizarAtualizacaoDieta() {
        Dieta dietaSelecionada = tabelaDietasUsuario.getSelectionModel().getSelectedItem();
        if (dietaSelecionada != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/edicao-dieta-view.fxml"));
                Parent root = loader.load();
                EdicaoDietaController controller = loader.getController();
                controller.setDietaController(this);
                controller.setMainController(mainController);
                controller.setDieta(dietaSelecionada);
                Stage stage = new Stage();
                stage.setTitle("Editar Dieta");
                stage.setScene(new Scene(root));
                stage.showAndWait();
                atualizarTabelaDietasUsuario(); // Atualiza após editar

            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao abrir tela de edição", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhuma dieta selecionada", "Por favor, selecione uma dieta para editar.");
        }
    }

    @FXML
    public void realizarRemocaoDieta() {
        Dieta dietaSelecionada = tabelaDietasUsuario.getSelectionModel().getSelectedItem();
        if (dietaSelecionada != null) {
            try {
                fachada.removerDieta(dietaSelecionada.getId()); // Remove pela fachada
                atualizarTabelaDietasUsuario();
                mensagemDieta.setText("Dieta removida.");
            } catch (DietaNaoEncontradaException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao remover dieta", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhuma dieta selecionada", "Selecione uma dieta para remover.");
        }
    }

    @FXML
    public void adicionarDietaPreDefinida() {
        Dieta dietaSelecionada = tabelaDietasPreDefinidas.getSelectionModel().getSelectedItem();
        if (dietaSelecionada == null) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhuma dieta selecionada", "Selecione uma dieta pré-definida.");
            return;
        }

        try {
            // Cria uma *NOVA* instância, copiando os valores.  NÃO MODIFICA A ORIGINAL.
            Dieta novaDieta = new Dieta(
                    dietaSelecionada.getNome(),
                    dietaSelecionada.getObjetivo(),
                    dietaSelecionada.getCaloriasDiarias(),
                    null // Sem refeições!
            );

            if (mainController != null && mainController.getUsuarioLogado() != null) {
                novaDieta.setUsuario(mainController.getUsuarioLogado());
                fachada.configurarDieta(novaDieta, novaDieta.getNome(), novaDieta.getObjetivo(),
                        novaDieta.getCaloriasDiarias(), novaDieta.getUsuario());
                atualizarTabelaDietasUsuario();
                mensagemDieta.setText("Dieta adicionada.");

                // ATUALIZA O USUÁRIO LOGADO - CORRETO E COMPLETO
                if (mainController != null) {
                    try {
                        Usuario usuarioAtualizado = fachada.buscarUsuarioPorId(mainController.getUsuarioLogado().getId());
                        mainController.setUsuarioLogado(usuarioAtualizado);
                    } catch (UsuarioNaoEncontradoException e) {
                        showAlert(Alert.AlertType.ERROR, "Erro", "Usuário não encontrado.", "O usuário logado não foi encontrado.");
                    }
                }

            } else {
                showAlert(Alert.AlertType.ERROR, "Erro", "Nenhum usuário logado", "Não foi possível adicionar a dieta.");
            }

        } catch (DietaNaoEncontradaException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao adicionar dieta", e.getMessage());
        }
    }

    public void atualizarTabelaDietasUsuario() {
        try {
            List<Dieta> listaDietas = fachada.listarDietas();
            if (mainController != null && mainController.getUsuarioLogado() != null) {
                // Filtra as dietas pelo usuário logado:
                Usuario usuarioLogado = mainController.getUsuarioLogado();
                listaDietas = listaDietas.stream()
                        .filter(dieta -> dieta.getUsuario() != null && dieta.getUsuario().getId().equals(usuarioLogado.getId()))
                        .toList();
            }
            tabelaDietasUsuario.setItems(FXCollections.observableArrayList(listaDietas));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar dietas", e.getMessage());
        }
    }

    @FXML
    public void voltarParaTelaPrincipal() {
        if (mainController != null) {
            mainController.mostrarTelaPrincipal();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro interno", "MainController não foi configurado.");
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