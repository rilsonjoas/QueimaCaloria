package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;
import com.example.queimacaloria.negocio.Dieta;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.InicializadorDados;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import com.example.queimacaloria.negocio.Usuario;



import java.util.HashMap;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DietaController {

    @FXML private TableView<Dieta> tabelaDietasUsuario;
    @FXML private TableColumn<Dieta, String> colunaNomeUsuario;
    @FXML private TableColumn<Dieta, Dieta.ObjetivoDieta> colunaObjetivoUsuario;
    @FXML private TableColumn<Dieta, Integer> colunaCaloriasUsuario;
    @FXML private TableColumn<Dieta, Double> colunaProgressoUsuario;

    @FXML private TableView<Dieta> tabelaDietasPreDefinidas;
    @FXML private TableColumn<Dieta, String> colunaNomePreDefinida;
    @FXML private TableColumn<Dieta, Dieta.ObjetivoDieta> colunaObjetivoPreDefinida;
    @FXML private TableColumn<Dieta, Integer> colunaCaloriasPreDefinida;
    @FXML private TableColumn<Dieta, Double> colunaProgressoPreDefinida;


    @FXML private Label mensagemDieta;
    @FXML private Label labelNomeDieta;
    @FXML private Label labelObjetivoDieta;
    @FXML private Label labelCaloriasDieta;
    @FXML private Label labelProgressoDieta;

    private Fachada fachada = Fachada.getInstanciaUnica();
    private MainController mainController;
    private ObservableList<Dieta> dietasPreDefinidas = FXCollections.observableArrayList();


    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        // Configuração da tabela do usuário
        configurarTabelaUsuario();
        atualizarTabelaDietasUsuario();

        // Configuração da tabela pré-definida
        configurarTabelaPreDefinida();
        carregarDietasPreDefinidas();

        // Listener para seleção de item (em ambas as tabelas)
        tabelaDietasUsuario.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                tabelaDietasPreDefinidas.getSelectionModel().clearSelection(); // Desmarca a outra tabela
                exibirDetalhesDieta(newSelection);
            }
        });

        tabelaDietasPreDefinidas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                tabelaDietasUsuario.getSelectionModel().clearSelection(); // Desmarca a outra tabela
                exibirDetalhesDieta(newSelection);
            }
        });
    }

    private void configurarTabelaUsuario() {
        colunaNomeUsuario.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaObjetivoUsuario.setCellValueFactory(new PropertyValueFactory<>("objetivo"));
        colunaCaloriasUsuario.setCellValueFactory(new PropertyValueFactory<>("caloriasDiarias"));
        colunaProgressoUsuario.setCellValueFactory(cellData -> {
            Dieta dieta = cellData.getValue();
            return new SimpleDoubleProperty(dieta.calcularProgresso()).asObject();
        });
    }

    private void configurarTabelaPreDefinida() {
        colunaNomePreDefinida.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaObjetivoPreDefinida.setCellValueFactory(new PropertyValueFactory<>("objetivo"));
        colunaCaloriasPreDefinida.setCellValueFactory(new PropertyValueFactory<>("caloriasDiarias"));
        colunaProgressoPreDefinida.setCellValueFactory(cellData -> {
            Dieta dieta = cellData.getValue();
            return new SimpleDoubleProperty(dieta.calcularProgresso()).asObject(); //Mesmo método de progresso
        });
    }


    private void carregarDietasPreDefinidas() {
        try {
            // Usa o InicializadorDados para OBTER uma cópia.  NÃO modifica o original.
            List<Dieta> dietas = InicializadorDados.inicializarDietas(); // Método estático
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
            stage.showAndWait(); // Use showAndWait para bloquear a tela principal
            atualizarTabelaDietasUsuario(); //Atualiza após o fechamento da tela de criação

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao abrir tela", e.getMessage());
        }
    }

    @FXML
    public void realizarAtualizacaoDieta() {
        Dieta dietaSelecionada = tabelaDietasUsuario.getSelectionModel().getSelectedItem();
        if (dietaSelecionada != null) {
            try {
                fachada.configurarDieta(dietaSelecionada, dietaSelecionada.getNome(),
                        dietaSelecionada.getObjetivo(), dietaSelecionada.getCaloriasDiarias(),
                        dietaSelecionada.getUsuario());
                atualizarTabelaDietasUsuario();
                mensagemDieta.setText("Dieta atualizada com sucesso!");
            } catch (DietaNaoEncontradaException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar dieta", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhuma dieta selecionada",
                    "Por favor, selecione uma dieta para atualizar.");
        }
    }

    @FXML
    public void realizarRemocaoDieta() {
        Dieta dietaSelecionada = tabelaDietasUsuario.getSelectionModel().getSelectedItem();
        if (dietaSelecionada != null) {
            try {
                fachada.configurarDieta(dietaSelecionada, null, null, 0, null);
                atualizarTabelaDietasUsuario();
                mensagemDieta.setText("Dieta removida com sucesso!");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao remover dieta", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhuma dieta selecionada",
                    "Por favor, selecione uma dieta para remover.");
        }
    }

    @FXML
    public void adicionarDietaPreDefinida() {
        Dieta dietaSelecionada = tabelaDietasPreDefinidas.getSelectionModel().getSelectedItem();
        if (dietaSelecionada != null) {
            try {
                // Cria uma *NOVA* instância de Dieta, copiando os valores
                Dieta novaDieta = new Dieta(
                        dietaSelecionada.getNome(),
                        dietaSelecionada.getObjetivo(),
                        dietaSelecionada.getCaloriasDiarias(),
                        new HashMap<>(dietaSelecionada.getMacronutrientes()), // Copia o Map
                        new ArrayList<>(dietaSelecionada.getRefeicoes()),    // Copia a lista
                        null // Usuário será definido depois.  NÃO use o usuário da dieta pré-definida.
                );

                // Obtém o usuário logado (simulação - substitua pela lógica correta)
                Usuario usuarioExemplo = new Usuario();
                try {
                    fachada.atualizarDadosUsuario(usuarioExemplo, "Exemplo", "exemplo@email.com", "senha",
                            null, Usuario.Sexo.MASCULINO, 70, (float) 1.75);

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                novaDieta.setUsuario(usuarioExemplo); // Define o usuário correto
                fachada.configurarDieta(novaDieta, novaDieta.getNome(), novaDieta.getObjetivo(),
                        novaDieta.getCaloriasDiarias(), novaDieta.getUsuario()); // Adiciona através da fachada
                atualizarTabelaDietasUsuario(); // Atualiza a tabela
                mensagemDieta.setText("Dieta adicionada com sucesso!");

            } catch (DietaNaoEncontradaException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao adicionar dieta", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhuma dieta selecionada",
                    "Por favor, selecione uma dieta pré-definida para adicionar.");
        }
    }

    private void atualizarTabelaDietasUsuario() {
        try {
            List<Dieta> listaDietas = fachada.listarDietas();

            // Filtrar para mostrar apenas as dietas do usuário logado (simulação)
            List<Dieta> dietasDoUsuario = listaDietas.stream()
                    .filter(dieta -> dieta.getUsuario() != null && dieta.getUsuario().getEmail().equals("exemplo@email.com")) //Filtra
                    .collect(Collectors.toList());

            tabelaDietasUsuario.setItems(FXCollections.observableArrayList(dietasDoUsuario));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar dietas", e.getMessage());
        }
    }


    private void exibirDetalhesDieta(Dieta dieta) {
        if (dieta != null) {
            labelNomeDieta.setText("Nome: " + dieta.getNome());
            labelObjetivoDieta.setText("Objetivo: " + dieta.getObjetivo());
            labelCaloriasDieta.setText("Calorias: " + dieta.getCaloriasDiarias());
            labelProgressoDieta.setText("Progresso: " + String.format("%.2f%%", dieta.calcularProgresso()));
        } else {
            labelNomeDieta.setText("Nome: ");
            labelObjetivoDieta.setText("Objetivo: ");
            labelCaloriasDieta.setText("Calorias: ");
            labelProgressoDieta.setText("Progresso: ");
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