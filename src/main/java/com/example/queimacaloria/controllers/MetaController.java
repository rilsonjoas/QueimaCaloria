package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.MetaNaoEncontradaException;
import com.example.queimacaloria.negocio.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors; // Importante para o filtro!
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;



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

    // Botão de compartilhar
    @FXML
    private Button buttonCompartilhar;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        configurarTabelaUsuario();
        atualizarTabelaMetasUsuario();
        configurarTabelaPreDefinida();
        carregarMetasPreDefinidas();

        //Verifica se o botão compartilhar está presente antes de configurar o evento.
        if(buttonCompartilhar != null){
            buttonCompartilhar.setOnAction(event -> compartilharLista());
        }
    }

    private void configurarTabelaUsuario() {
        colunaDescricaoUsuario.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colunaTipoUsuario.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        // Cálculo CORRETO do progresso (agora como um Double)
        colunaProgressoUsuario.setCellValueFactory(cellData -> {
            Meta meta = cellData.getValue();
            double progresso = 0.0;
            if (meta.getValorAlvo() > 0) { // Evita divisão por zero!
                progresso = (meta.getProgressoAtual() / meta.getValorAlvo()) * 100.0;
            }
            return new SimpleDoubleProperty(progresso).asObject(); // Retorna como Double
        });

        colunaProgressoUsuario.setCellFactory(column -> {
            return new TableCell<Meta, Double>() {
                @Override
                protected void updateItem(Double progresso, boolean empty) {
                    super.updateItem(progresso, empty);
                    if (empty || progresso == null) {
                        setText(null);
                    } else {
                        setText(String.format("%.1f%%", progresso)); // Formata a porcentagem
                    }
                }
            };
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
    }


    private void configurarTabelaPreDefinida() {
        colunaDescricaoPreDefinida.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colunaTipoPreDefinida.setCellValueFactory(new PropertyValueFactory<>("tipo"));
    }

    private void carregarMetasPreDefinidas() {
        try {
            List<Meta> metas = InicializadorDados.inicializarMetas();
            metasPreDefinidas.setAll(metas);
            tabelaMetasPreDefinidas.setItems(metasPreDefinidas);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar metas pré-definidas", e.getMessage());
        }
    }

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

                // Recomendação de dieta
                Dieta dietaRecomendada = fachada.getDietaAleatoria(novaMeta.getTipo());
                if (dietaRecomendada != null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Recomendação de Dieta");
                    alert.setHeaderText("Com base na sua nova meta, recomendamos a seguinte dieta:");
                    alert.setContentText(String.format("Nome: %s\nObjetivo: %s\nCalorias: %d",
                            dietaRecomendada.getNome(),
                            dietaRecomendada.getObjetivo().getDescricao(),
                            dietaRecomendada.getCaloriasDiarias()));
                    alert.showAndWait();
                }

            } catch (MetaNaoEncontradaException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao adicionar meta", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhuma meta selecionada",
                    "Por favor, selecione uma meta pré-definida para adicionar.");
        }
    }

    public void atualizarTabelaMetasUsuario() {
        System.out.println("MetaController.atualizarTabelaMetasUsuario() chamado.");
        try {
            if (mainController != null && mainController.getUsuarioLogado() != null) {
                Usuario usuarioLogado = mainController.getUsuarioLogado();
                List<Meta> listaMetas = mainController.getUsuarioLogado().getMetas();

                // FILTRO: Mostra apenas as metas do usuário logado
                listaMetas = listaMetas.stream()
                        .filter(meta -> meta != null && meta.getUsuario() != null && meta.getUsuario().getId().equals(usuarioLogado.getId()))
                        .collect(Collectors.toList());

                tabelaMetasUsuario.setItems(FXCollections.observableArrayList(listaMetas));

                // Atualiza listeners (importante para reatividade, mas pode não ser a causa da duplicação)
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
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar metas", e.getMessage());
            e.printStackTrace(); // Sempre imprima stack traces para debugging!
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

    @FXML
    public void compartilharLista() {
        if (mainController != null && mainController.getUsuarioLogado() != null) {
            List<Meta> metasDoUsuario = mainController.getUsuarioLogado().getMetas();
            String nomeUsuario = mainController.getUsuarioLogado().getNome(); // Obtém o nome do usuário

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salvar Relatório de Metas em PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos PDF", "*.pdf"));
            Stage stage = (Stage) tabelaMetasUsuario.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                try {
                    // Passa o nome do usuário
                    GeradorPDF.gerarRelatorioMetas(metasDoUsuario, file.getAbsolutePath(), nomeUsuario);
                    showAlert(Alert.AlertType.INFORMATION, "Sucesso!", "Relatório Gerado",
                            "O relatório de metas foi gerado com sucesso em: " + file.getAbsolutePath());

                } catch (IOException e) { // Captura IOException
                    showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao gerar relatório", "Erro de I/O: " + e.getMessage());
                    e.printStackTrace();
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao gerar relatório", "Erro inesperado: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Usuário Não Logado", "É necessário estar logado para gerar o relatório.");
        }
    }
}