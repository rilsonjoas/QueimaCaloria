package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.ExercicioNaoEncontradoException;
import com.example.queimacaloria.negocio.Exercicio;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.GeradorPDF;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; // Importante para o filtro!
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

    // Botão de compartilhar
    @FXML
    private Button buttonCompartilhar;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        configurarTabelaUsuario();
        configurarTabelaPreDefinida();
        carregarExerciciosPreDefinidos();
        atualizarTabelaExerciciosUsuario();

        //Verifica se o botão compartilhar está presente antes de configurar o evento.
        if(buttonCompartilhar != null){
            buttonCompartilhar.setOnAction(event -> compartilharLista());
        }

    }

    private void configurarTabelaUsuario() {
        colunaNomeUsuario.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaTipoUsuario.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colunaTempoUsuario.setCellValueFactory(new PropertyValueFactory<>("tempo"));
        colunaCaloriasQueimadasUsuario.setCellValueFactory(cellData -> cellData.getValue().caloriasQueimadasProperty().asObject());
    }

    private void configurarTabelaPreDefinida() {
        colunaNomePreDefinido.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaTipoPreDefinido.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colunaTempoPreDefinido.setCellValueFactory(new PropertyValueFactory<>("tempo"));
        colunaCaloriasQueimadasPreDefinido.setCellValueFactory(cellData -> cellData.getValue().caloriasQueimadasProperty().asObject());
    }

    private void carregarExerciciosPreDefinidos() {
        try {
            List<Exercicio> exercicios = InicializadorDados.inicializarExercicios();
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
            controller.setMainController(mainController);

            stage.showAndWait();
            atualizarTabelaExerciciosUsuario();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao abrir tela", e.getMessage());
        }
    }

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
    @FXML
    public void adicionarExercicioPreDefinido() {
        Exercicio exercicioSelecionado = tabelaExerciciosPreDefinidos.getSelectionModel().getSelectedItem();
        if (exercicioSelecionado == null) { //Verifica antes
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhum exercício selecionado",
                    "Por favor, selecione um exercício pré-definido para adicionar.");
            return;
        }


        if (mainController == null || mainController.getUsuarioLogado() == null) { //Verifica Usuário
            showAlert(Alert.AlertType.ERROR, "Erro", "Nenhum usuário logado",
                    "Não foi possível adicionar o exercício.");
            return;
        }


        try {
            Exercicio novoExercicio = new Exercicio(
                    exercicioSelecionado.getNome(),
                    exercicioSelecionado.getDescricao(),
                    // Copia a lista de músculos ou cria uma nova se for nula
                    exercicioSelecionado.getMusculosTrabalhados() != null ? new ArrayList<>(exercicioSelecionado.getMusculosTrabalhados()) : new ArrayList<>(),
                    exercicioSelecionado.getTipo(),
                    exercicioSelecionado.getTempo(),
                    exercicioSelecionado.getCaloriasQueimadas(),
                    false
            );

            //REVISÃO:  Definindo o usuário LOGADO.
            novoExercicio.setUsuario(mainController.getUsuarioLogado());

            // Copia o nível de experiência, se disponível.  Isso é importante!
            novoExercicio.setNivelExperiencia(exercicioSelecionado.getNivelExperiencia());

            fachada.configurarExercicio(novoExercicio, novoExercicio.getNome(),
                    novoExercicio.getDescricao(), novoExercicio.getTipo(),
                    novoExercicio.getTempo(), novoExercicio.getCaloriasQueimadas(), novoExercicio.getUsuario()); //Passa o Usuario


            atualizarTabelaExerciciosUsuario();
            mensagemExercicio.setText("Exercício adicionado com sucesso!");

            if (mainController != null) {
                mainController.getUsuarioLogado().getExercicios().add(novoExercicio); //Adicionado
                mainController.atualizarDadosTelaPrincipal();
            }
        } catch (ExercicioNaoEncontradoException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao adicionar exercício", e.getMessage());
        }
    }

    public void atualizarTabelaExerciciosUsuario() {
        try {
            if (mainController != null && mainController.getUsuarioLogado() != null) {
                Usuario usuarioLogado = mainController.getUsuarioLogado();
                List<Exercicio> listaExercicios = fachada.listarExercicios();

                // FILTRO 1: Apenas exercícios do usuário logado
                listaExercicios = listaExercicios.stream()
                        .filter(exercicio -> exercicio.getUsuario() != null && exercicio.getUsuario().getId().equals(usuarioLogado.getId()))
                        .collect(Collectors.toList());

                // FILTRO 2: Aplicar filtro de nível de experiência
                if (usuarioLogado.getNivelExperiencia() != null) {
                    listaExercicios = listaExercicios.stream()
                            .filter(exercicio -> exercicio.getNivelExperiencia() == null || exercicio.getNivelExperiencia() == usuarioLogado.getNivelExperiencia())
                            .collect(Collectors.toList());
                }

                tabelaExerciciosUsuario.setItems(FXCollections.observableArrayList(listaExercicios));
                tabelaExerciciosUsuario.refresh();
                if(mainController != null){
                    mainController.getAtividadesRecentes().clear();
                }

                for(Exercicio exercicio : listaExercicios){
                    if(mainController != null){
                        mainController.adicionarExercicioRecente(exercicio);
                    }
                }

            } else {
                // Trata caso não haja usuário logado (opcional)
                tabelaExerciciosUsuario.setItems(FXCollections.observableArrayList());
                System.err.println("ExercicioController: Nenhum usuário logado ao atualizar a tabela.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar exercicios", e.getMessage());
            e.printStackTrace(); //  Importante para debug!
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
            List<Exercicio> exerciciosDoUsuario = mainController.getUsuarioLogado().getExercicios();
            String nomeUsuario = mainController.getUsuarioLogado().getNome(); // Obtém o nome do usuário

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salvar Relatório de Exercícios em PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos PDF", "*.pdf"));
            Stage stage = (Stage) tabelaExerciciosUsuario.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                try {
                    // Passa o nome do usuário para o método gerarRelatorioExercicios
                    GeradorPDF.gerarRelatorioExercicios(exerciciosDoUsuario, file.getAbsolutePath(), nomeUsuario);
                    showAlert(Alert.AlertType.INFORMATION, "Sucesso!", "Relatório Gerado",
                            "O relatório de exercícios foi gerado com sucesso em: " + file.getAbsolutePath());

                } catch (IOException e) { // Captura IOException especificamente
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