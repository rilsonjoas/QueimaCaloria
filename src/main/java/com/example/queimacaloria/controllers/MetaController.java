package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.MetaNaoEncontradaException;
import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.InicializadorDados;
import com.example.queimacaloria.negocio.Meta;
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
import java.time.LocalDate;
import java.util.List;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


public class MetaController {

    @FXML private TableView<Meta> tabelaMetasUsuario;
    @FXML private TableColumn<Meta, String> colunaDescricaoUsuario;
    @FXML private TableColumn<Meta, Meta.Tipo> colunaTipoUsuario;
    @FXML private TableColumn<Meta, Double> colunaProgressoUsuario;
    @FXML private TableColumn<Meta, LocalDate> colunaDataConclusaoUsuario;

    @FXML private TableView<Meta> tabelaMetasPreDefinidas;
    @FXML private TableColumn<Meta, String> colunaDescricaoPreDefinida;
    @FXML private TableColumn<Meta, Meta.Tipo> colunaTipoPreDefinida;

    @FXML private Label mensagemMeta;


    private Fachada fachada = Fachada.getInstanciaUnica();
    private MainController mainController;
    private ObservableList<Meta> metasPreDefinidas = FXCollections.observableArrayList();

    // Define o controlador principal.
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    // Inicializa o controlador, configurando as tabelas.
    @FXML
    public void initialize() {
        configurarTabelaUsuario();
        atualizarTabelaMetasUsuario();

        configurarTabelaPreDefinida();
        carregarMetasPreDefinidas();
    }

    // Configura a tabela de metas do usuário.
    private void configurarTabelaUsuario() {
        colunaDescricaoUsuario.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colunaTipoUsuario.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        colunaProgressoUsuario.setCellValueFactory(cellData -> {
            Meta meta = cellData.getValue();
            double progresso = (meta.getValorAlvo() > 0) ? (meta.getProgressoAtual()/meta.getValorAlvo())*100 : 0.0;
            return new SimpleDoubleProperty(progresso).asObject();
        });

        colunaDataConclusaoUsuario.setCellValueFactory(cellData -> {
            Meta meta = cellData.getValue();
            return new ReadOnlyObjectWrapper<>(meta.getDataConclusao());
        });

        colunaDataConclusaoUsuario.setCellFactory(column -> {
            return new TableCell<Meta, LocalDate>() {
                @Override
                protected void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);

                    if (empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        Meta meta = getTableView().getItems().get(getIndex());
                        if (meta != null && meta.getDataConclusao() != null) {
                            setText(meta.getDataConclusao().toString());
                            setStyle("-fx-alignment: CENTER;");
                        } else {
                            setText("Continue avançando!");
                            setStyle("-fx-alignment: CENTER;");
                        }

                    }
                }
            };
        });

        tabelaMetasUsuario.getItems().addListener((javafx.collections.ListChangeListener<Meta>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Meta meta : change.getAddedSubList()) {
                        meta.dataConclusaoProperty().addListener((obs, oldVal, newVal) -> {
                            tabelaMetasUsuario.refresh();
                        });
                    }
                }
            }
        });
    }

    // Configura a tabela de metas pré-definidas.
    private void configurarTabelaPreDefinida() {
        colunaDescricaoPreDefinida.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colunaTipoPreDefinida.setCellValueFactory(new PropertyValueFactory<>("tipo"));
    }

    // Carrega as metas pré-definidas.
    private void carregarMetasPreDefinidas() {
        try {
            List<Meta> metas = InicializadorDados.inicializarMetas();
            metasPreDefinidas.setAll(metas);
            tabelaMetasPreDefinidas.setItems(metasPreDefinidas);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar metas pré-definidas", e.getMessage());
        }
    }

    // Abre a tela de criação de meta.
    @FXML
    public void abrirTelaCriarMeta() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/criacao-meta-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Criar Nova Meta");
            stage.setScene(scene);

            CriacaoMetaController controller = loader.getController();
            controller.setMetaController(this);
            controller.setMainController(mainController);
            stage.showAndWait();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao abrir tela", e.getMessage());
        }
    }

    // Abre a tela de edição de meta.
    @FXML
    public void atualizarMeta() {
        Meta metaSelecionada = tabelaMetasUsuario.getSelectionModel().getSelectedItem();
        if (metaSelecionada != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/edicao-meta-view.fxml"));
                Parent root = loader.load();

                EdicaoMetaController controller = loader.getController();
                controller.setMetaController(this);
                controller.setMainController(mainController);

                controller.setMeta(metaSelecionada);

                Stage stage = new Stage();
                stage.setTitle("Editar Meta");
                stage.setScene(new Scene(root));
                stage.showAndWait();

                atualizarTabelaMetasUsuario();

            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao abrir tela de edição", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhuma meta selecionada",
                    "Por favor, selecione uma meta para atualizar.");
        }
    }

    // Remove a meta selecionada.
    @FXML
    public void removerMeta() {
        Meta metaSelecionada = tabelaMetasUsuario.getSelectionModel().getSelectedItem();
        if (metaSelecionada != null) {
            try {
                fachada.removerMeta(metaSelecionada.getId());
                atualizarTabelaMetasUsuario();
                mensagemMeta.setText("Meta removida com sucesso!");

            } catch (MetaNaoEncontradaException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao remover meta", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhuma meta selecionada",
                    "Por favor, selecione uma meta para remover.");
        }
    }

    // Adiciona uma meta pré-definida ao usuário.
    @FXML
    public void adicionarMetaPreDefinida() {
        Meta metaSelecionada = tabelaMetasPreDefinidas.getSelectionModel().getSelectedItem();
        if (metaSelecionada != null) {
            try {
                Meta novaMeta = new Meta(
                        metaSelecionada.getDescricao(),
                        metaSelecionada.getTipo(),
                        metaSelecionada.getValorAlvo(),
                        0.0,
                        metaSelecionada.getDataCriacao(),
                        metaSelecionada.getDataConclusao() != null ? metaSelecionada.getDataConclusao() : null
                );
                fachada.configurarMeta(novaMeta, novaMeta.getDescricao(), novaMeta.getTipo(),
                        novaMeta.getValorAlvo(), novaMeta.getProgressoAtual(), novaMeta.getDataConclusao());

                atualizarTabelaMetasUsuario();
                mensagemMeta.setText("Meta adicionada com sucesso!");

            } catch (MetaNaoEncontradaException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao adicionar meta", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhuma meta selecionada",
                    "Por favor, selecione uma meta pré-definida para adicionar.");
        }
    }

    // Atualiza a tabela de metas do usuário.
    public void atualizarTabelaMetasUsuario() {
        System.out.println("MetaController.atualizarTabelaMetasUsuario() chamado.");
        try {
            List<Meta> listaMetas = fachada.listarMetas();
            tabelaMetasUsuario.setItems(FXCollections.observableArrayList(listaMetas));

            for (Meta meta : tabelaMetasUsuario.getItems()) {
                meta.dataConclusaoProperty().addListener((obs, oldVal, newVal) -> {
                    System.out.println("Listener de dataConclusaoProperty disparado! Meta ID: " + meta.getId());
                    tabelaMetasUsuario.refresh();
                });

                meta.progressoAtualProperty().addListener((obs, oldVal, newVal) -> {
                    System.out.println("Listener de progressoAtualProperty disparado! Meta ID: " + meta.getId());
                    tabelaMetasUsuario.refresh();
                });
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar metas", e.getMessage());
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