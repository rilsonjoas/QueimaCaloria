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
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DietaController {

    @FXML private TableView<Dieta> tabelaDietasUsuario;
    @FXML private TableColumn<Dieta, String> colunaNomeUsuario;
    @FXML private TableColumn<Dieta, Meta.Tipo> colunaObjetivoUsuario;
    @FXML private TableColumn<Dieta, Integer> colunaCaloriasUsuario;

    @FXML private TableView<Dieta> tabelaDietasPreDefinidas;
    @FXML private TableColumn<Dieta, String> colunaNomePreDefinida;
    @FXML private TableColumn<Dieta, Meta.Tipo> colunaObjetivoPreDefinida;
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
            atualizarTabelaDietasUsuario(); // Atualiza a tabela após criar uma dieta

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
                controller.setDietaController(this);  // Define o DietaController
                controller.setMainController(mainController);  // Define o MainController
                controller.setDieta(dietaSelecionada); // Define a dieta a ser editada
                Stage stage = new Stage();
                stage.setTitle("Editar Dieta");
                stage.setScene(new Scene(root));
                stage.showAndWait(); // Importante: Espera a janela de edição fechar
                atualizarTabelaDietasUsuario();  // Atualiza a tabela após a edição

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
                atualizarTabelaDietasUsuario(); // Atualiza a tabela após remover.
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

        // REVISÃO: Simplificando a lógica de adição
        if (mainController == null || mainController.getUsuarioLogado() == null) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Nenhum usuário logado", "Não foi possível adicionar a dieta.");
            return;
        }

        try {
            // Cria uma *cópia* da dieta pré-definida, já com o usuário.
            Dieta novaDieta = new Dieta(
                    dietaSelecionada.getNome(),
                    dietaSelecionada.getObjetivo(),
                    dietaSelecionada.getCaloriasDiarias(),
                    mainController.getUsuarioLogado() // Usuário definido aqui!
            );

            fachada.configurarDieta(novaDieta, novaDieta.getNome(), novaDieta.getObjetivo(),
                    novaDieta.getCaloriasDiarias(), novaDieta.getUsuario());

            // **********  MUDANÇA AQUI ***********
            // ADICIONA A NOVA DIETA À LISTA DO USUÁRIO
            mainController.getUsuarioLogado().getDietas().add(novaDieta); // <-- Adiciona à lista do usuário!

            atualizarTabelaDietasUsuario();  //REMOVA, O LISTENER JÁ ATUALIZA A UI
            mensagemDieta.setText("Dieta adicionada com sucesso!");

        } catch (DietaNaoEncontradaException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao adicionar dieta", e.getMessage());
            e.printStackTrace();
        }
    }


    public void atualizarTabelaDietasUsuario() {
        try {
            if (mainController != null && mainController.getUsuarioLogado() != null) {
                Usuario usuarioLogado = mainController.getUsuarioLogado();
                List<Dieta> listaDietas = fachada.listarDietas();

                // FILTRO 1: Apenas dietas do usuário logado
                listaDietas = listaDietas.stream()
                        .filter(dieta -> dieta.getUsuario() != null && dieta.getUsuario().getId().equals(usuarioLogado.getId()))
                        .collect(Collectors.toList());

                // FILTRO 2: Aplicar filtro de tipo de dieta (preferência do usuário)
                if (usuarioLogado.getTipoDieta() != null) {
                    listaDietas = listaDietas.stream()
                            .filter(dieta -> dieta.getTipoDieta() == null || dieta.getTipoDieta() == usuarioLogado.getTipoDieta())
                            .collect(Collectors.toList());
                }

                tabelaDietasUsuario.setItems(FXCollections.observableArrayList(listaDietas));
                tabelaDietasUsuario.refresh();
            } else {
                tabelaDietasUsuario.setItems(FXCollections.observableArrayList());
                System.err.println("DietaController: Nenhum usuário logado ao atualizar a tabela.");
            }
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
            // Obtém a lista de dietas *do usuário*.
            List<Dieta> dietasDoUsuario = mainController.getUsuarioLogado().getDietas();
            String nomeUsuario = mainController.getUsuarioLogado().getNome();

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salvar Relatório de Dietas em PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos PDF", "*.pdf"));
            Stage stage = (Stage) tabelaDietasUsuario.getScene().getWindow();  // Usando a tabela como referência.
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                try {
                    // Chama o método correto do GeradorPDF, passando o nome do usuário.
                    GeradorPDF.gerarRelatorioDietas(dietasDoUsuario, file.getAbsolutePath(), nomeUsuario);
                    showAlert(Alert.AlertType.INFORMATION, "Sucesso!", "Relatório Gerado",
                            "O relatório de dietas foi gerado com sucesso em: " + file.getAbsolutePath());

                } catch (IOException e) { // Captura IOException
                    showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao gerar relatório", "Erro de I/O: " + e.getMessage());
                    e.printStackTrace();
                } catch (Exception e) { // Apenas o catch genérico
                    showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao gerar relatório",
                            "Ocorreu um erro inesperado: " + e.getMessage());
                    e.printStackTrace(); // Stack trace completa no console
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Usuário Não Logado", "É necessário estar logado para gerar o relatório.");
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