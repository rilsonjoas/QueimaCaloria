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
//Importe ReadOnlyObjectWrapper
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


    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        configurarTabelaUsuario();
        atualizarTabelaMetasUsuario();

        configurarTabelaPreDefinida();
        carregarMetasPreDefinidas();
    }

    private void configurarTabelaUsuario() {
        colunaDescricaoUsuario.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colunaTipoUsuario.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        colunaProgressoUsuario.setCellValueFactory(cellData -> {
            Meta meta = cellData.getValue();
            double progresso = (meta.getValorAlvo() > 0) ? (meta.getProgressoAtual()/meta.getValorAlvo())*100 : 0.0;
            return new SimpleDoubleProperty(progresso).asObject();
        });


        // CORREÇÃO: Usar ReadOnlyObjectWrapper + Listener
        colunaDataConclusaoUsuario.setCellValueFactory(cellData -> {
            Meta meta = cellData.getValue();
            return new ReadOnlyObjectWrapper<>(meta.getDataConclusao());
        });

        // Listener para atualizar a exibição da data de conclusão
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
                        // Verifica se a meta não é nula e se a data de conclusão não é nula
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

        // Adiciona um listener para *cada meta* na tabela.  ESSENCIAL!
        tabelaMetasUsuario.getItems().addListener((javafx.collections.ListChangeListener<Meta>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Meta meta : change.getAddedSubList()) {
                        meta.dataConclusaoProperty().addListener((obs, oldVal, newVal) -> {
                            tabelaMetasUsuario.refresh(); // Atualiza a tabela!
                        });
                    }
                }
            }
        });
    }
    private void configurarTabelaPreDefinida() {
        colunaDescricaoPreDefinida.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colunaTipoPreDefinida.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        //Removido os setCellValueFactory das colunas que foram removidas.
    }


    private void carregarMetasPreDefinidas() {
        try {
            List<Meta> metas = InicializadorDados.inicializarMetas(); // Método estático
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
                // Carrega a tela de edição
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/edicao-meta-view.fxml"));
                Parent root = loader.load();

                // Obtém o controlador da tela de edição
                EdicaoMetaController controller = loader.getController();
                controller.setMetaController(this);
                controller.setMainController(mainController);

                // Passa a meta selecionada para o controlador
                controller.setMeta(metaSelecionada);

                // Exibe a tela de edição
                Stage stage = new Stage();
                stage.setTitle("Editar Meta");
                stage.setScene(new Scene(root));
                stage.showAndWait(); // Exibe como um diálogo modal

                atualizarTabelaMetasUsuario(); // Atualiza a tabela *após* a edição

                // Atualiza o usuário logado *após* a edição: Removido, a fachada agora cuida disso
                /*
                if (mainController != null && mainController.getUsuarioLogado() != null) {
                    try {
                        Usuario usuarioAtualizado = fachada.buscarUsuarioPorId(mainController.getUsuarioLogado().getId());
                        mainController.setUsuarioLogado(usuarioAtualizado);
                    } catch (UsuarioNaoEncontradoException e) {
                        showAlert(Alert.AlertType.ERROR, "Erro", "Usuário não encontrado.",
                                "O usuário logado não pôde ser encontrado após a edição da meta.");
                    }
                }

                 */

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
                fachada.removerMeta(metaSelecionada.getId()); //  Chama remover da fachada
                atualizarTabelaMetasUsuario(); //  Atualiza a tabela
                mensagemMeta.setText("Meta removida com sucesso!");

               /* Removido: A fachada agora cuida de atualizar o usuário
                // Atualiza o usuário logado *após* a remoção:
                if (mainController != null && mainController.getUsuarioLogado() != null) {
                    try {
                        Usuario usuarioAtualizado = fachada.buscarUsuarioPorId(mainController.getUsuarioLogado().getId());
                        mainController.setUsuarioLogado(usuarioAtualizado);
                    } catch (UsuarioNaoEncontradoException e) {
                        showAlert(Alert.AlertType.ERROR, "Erro", "Usuário não encontrado.",
                                "O usuário logado não pôde ser encontrado após a remoção da meta.");
                    }
                }
                */


            } catch (MetaNaoEncontradaException e) { // Captura a exceção
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
                // Cria uma *NOVA* instância, copiando os valores.
                Meta novaMeta = new Meta(
                        metaSelecionada.getDescricao(),
                        metaSelecionada.getTipo(),
                        metaSelecionada.getValorAlvo(),
                        0.0,  //  <-  Progresso inicial DEVE ser 0.0
                        metaSelecionada.getDataCriacao(),
                        metaSelecionada.getDataConclusao() != null ? metaSelecionada.getDataConclusao() : null
                        // Mantém a data de conclusão se existir, senão, define como null.
                );
                fachada.configurarMeta(novaMeta, novaMeta.getDescricao(), novaMeta.getTipo(),
                        novaMeta.getValorAlvo(), novaMeta.getProgressoAtual(), novaMeta.getDataConclusao());

                atualizarTabelaMetasUsuario(); //  ATUALIZA A TABELA *APÓS* ADICIONAR, E REMOVIDO A ATUALIZAÇÃO DO USUÁRIO
                mensagemMeta.setText("Meta adicionada com sucesso!");



            } catch (MetaNaoEncontradaException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao adicionar meta", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhuma meta selecionada",
                    "Por favor, selecione uma meta pré-definida para adicionar.");
        }
    }


    public void atualizarTabelaMetasUsuario() {
        System.out.println("MetaController.atualizarTabelaMetasUsuario() chamado."); // PRINT
        try {
            List<Meta> listaMetas = fachada.listarMetas();
            tabelaMetasUsuario.setItems(FXCollections.observableArrayList(listaMetas));

            // Adiciona o listener DENTRO de atualizarTabelaMetasUsuario
            for (Meta meta : tabelaMetasUsuario.getItems()) {
                // Listener para dataConclusaoProperty
                meta.dataConclusaoProperty().addListener((obs, oldVal, newVal) -> {
                    System.out.println("Listener de dataConclusaoProperty disparado! Meta ID: " + meta.getId()); //PRINT
                    tabelaMetasUsuario.refresh(); // Atualiza a tabela quando a data de conclusão mudar.
                });

                // Listener para progressoAtualProperty  --  ADICIONAR AQUI!
                meta.progressoAtualProperty().addListener((obs, oldVal, newVal) -> {
                    System.out.println("Listener de progressoAtualProperty disparado! Meta ID: " + meta.getId()); //PRINT
                    tabelaMetasUsuario.refresh();
                });
            }


        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar metas", e.getMessage());
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