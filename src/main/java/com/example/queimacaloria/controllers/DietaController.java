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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
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

    // Botão de compartilhar
    @FXML private Button buttonCompartilhar;


    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        configurarTabelaUsuario();
        configurarTabelaPreDefinida();
        carregarDietasPreDefinidas();
        atualizarTabelaDietasUsuario();

        //Verifica se o botão compartilhar está presente antes de configurar o evento.
        if(buttonCompartilhar != null){
            buttonCompartilhar.setOnAction(event -> compartilharLista());
        }

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
            atualizarTabelaDietasUsuario();

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
                atualizarTabelaDietasUsuario();

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
                fachada.removerDieta(dietaSelecionada.getId());
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
            // Cria uma *cópia* da dieta pré-definida, para não modificar a original.
            Dieta novaDieta = new Dieta(
                    dietaSelecionada.getNome(),
                    dietaSelecionada.getObjetivo(),
                    dietaSelecionada.getCaloriasDiarias(),
                    null // Inicialmente, sem usuário
            );

            if (mainController != null && mainController.getUsuarioLogado() != null) {
                // Define o usuário *na nova dieta*.
                novaDieta.setUsuario(mainController.getUsuarioLogado());

                // Usa configurarDieta, que agora já associa o usuário.
                fachada.configurarDieta(novaDieta, novaDieta.getNome(), novaDieta.getObjetivo(),
                        novaDieta.getCaloriasDiarias(), novaDieta.getUsuario());

                //fachada.setDietaAtiva(mainController.getUsuarioLogado(), novaDieta); //Removido

                atualizarTabelaDietasUsuario();
                mensagemDieta.setText("Dieta adicionada.");

                // Atualiza o usuário no MainController
                if (mainController != null) {
                    Usuario usuarioAtualizado = fachada.buscarUsuarioPorId(mainController.getUsuarioLogado().getId());
                    mainController.setUsuarioLogado(usuarioAtualizado);
                    mainController.atualizarDadosTelaPrincipal(); // Atualiza a tela principal
                }

            } else {
                showAlert(Alert.AlertType.ERROR, "Erro", "Nenhum usuário logado", "Não foi possível adicionar a dieta.");
            }

        } catch (DietaNaoEncontradaException | UsuarioNaoEncontradoException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao adicionar dieta", e.getMessage());
            e.printStackTrace();
        }
    }

    public void atualizarTabelaDietasUsuario() {
        try {
            List<Dieta> listaDietas = fachada.listarDietas();
            if (mainController != null && mainController.getUsuarioLogado() != null) {
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

    @FXML
    public void compartilharLista() {
        if (mainController != null && mainController.getUsuarioLogado() != null) {
            Usuario usuarioLogado = mainController.getUsuarioLogado();
            //Agora que as dietas estão associadas, voltamos a filtrar
            List<Dieta> dietasDoUsuario = fachada.listarDietas().stream()
                    .filter(dieta -> dieta.getUsuario() != null && dieta.getUsuario().getId().equals(usuarioLogado.getId()))
                    .toList();

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salvar Relatório de Dietas em PDF"); // Título mais específico
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos PDF", "*.pdf"));
            Stage stage = (Stage) tabelaDietasUsuario.getScene().getWindow(); // Usando a tabela como referência.
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                try {
                    // Chama o método correto do GeradorPDF, passando a *lista de dietas*
                    GeradorPDF.gerarRelatorioDietas(dietasDoUsuario, file.getAbsolutePath());
                    showAlert(Alert.AlertType.INFORMATION, "Sucesso!", "Relatório Gerado",
                            "O relatório de dietas foi gerado com sucesso em: " + file.getAbsolutePath());

                } catch (Exception e) { // Apenas o catch genérico
                    showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao gerar relatório",
                            "Ocorreu um erro inesperado: " + e.getMessage());
                    e.printStackTrace(); // Stack trace completa no console
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Usuário Não Logado",
                    "É necessário estar logado para gerar o relatório.");
        }
    }

    public void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}