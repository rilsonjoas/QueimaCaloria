package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;
import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;
import com.example.queimacaloria.negocio.Dieta;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.InicializadorDados;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
    //@FXML private TableColumn<Dieta, Double> colunaProgressoPreDefinida; //REMOVER

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
        configurarTabelaUsuario();
        configurarTabelaPreDefinida(); //Agora sem a coluna progresso
        carregarDietasPreDefinidas();
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
        //Removido
        //colunaProgressoPreDefinida.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().calcularProgresso()).asObject()); //Mesmo método de progresso

    }

    private void carregarDietasPreDefinidas() {
        try {
            // Usa o InicializadorDados para OBTER uma cópia. Não modifica o original.
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
            controller.setDietaController(this); // Injeta o DietaController
            controller.setMainController(mainController);  // Injeta o MainController
            stage.showAndWait();

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
                controller.setMainController(mainController); //Muito importante
                controller.setDieta(dietaSelecionada);
                Stage stage = new Stage();
                stage.setTitle("Editar Dieta");
                stage.setScene(new Scene(root));
                stage.showAndWait();

                //ATUALIZA USUÁRIO LOGADO
                if(mainController != null){
                    try{
                        Usuario usuarioAtualizado = fachada.buscarUsuarioPorId(mainController.getUsuarioLogado().getId());
                        mainController.setUsuarioLogado(usuarioAtualizado);
                    } catch(UsuarioNaoEncontradoException e){
                        showAlert(Alert.AlertType.ERROR, "Erro", "Usuário não encontrado.", "O usuário logado não pôde ser encontrado.");
                    }
                }


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
                fachada.configurarDieta(dietaSelecionada, null, null, 0, null); //Removendo, precisa do objeto dieta para remover.
                atualizarTabelaDietasUsuario();
                mensagemDieta.setText("Dieta removida.");

                //ATUALIZA USUÁRIO LOGADO
                if(mainController != null){
                    try{
                        Usuario usuarioAtualizado = fachada.buscarUsuarioPorId(mainController.getUsuarioLogado().getId());
                        mainController.setUsuarioLogado(usuarioAtualizado);
                    } catch(UsuarioNaoEncontradoException e){
                        showAlert(Alert.AlertType.ERROR, "Erro", "Usuário não encontrado.", "O usuário logado não pôde ser encontrado.");
                    }
                }

            } catch (Exception e) {
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

        System.out.println("DietaController.adicionarDietaPreDefinida: Iniciando..."); // PRINT
        try {
            Dieta novaDieta = new Dieta(
                    dietaSelecionada.getNome(),
                    dietaSelecionada.getObjetivo(),
                    dietaSelecionada.getCaloriasDiarias(),
                    new HashMap<>(dietaSelecionada.getMacronutrientes()),
                    new ArrayList<>(dietaSelecionada.getRefeicoes()),
                    null
            );

            if (mainController != null && mainController.getUsuarioLogado() != null) {
                novaDieta.setUsuario(mainController.getUsuarioLogado()); //  <-  USA O USUÁRIO LOGADO
                System.out.println("DietaController.adicionarDietaPreDefinida: Usuário logado: " + mainController.getUsuarioLogado().getEmail()); //PRINT
                fachada.configurarDieta(novaDieta, novaDieta.getNome(), novaDieta.getObjetivo(),
                        novaDieta.getCaloriasDiarias(), novaDieta.getUsuario());
                atualizarTabelaDietasUsuario();
                mensagemDieta.setText("Dieta adicionada.");

                //ATUALIZA O USUÁRIO LOGADO -  CORRETO E COMPLETO
                if (mainController != null) {
                    try {
                        Usuario usuarioAtualizado = fachada.buscarUsuarioPorId(mainController.getUsuarioLogado().getId());
                        mainController.setUsuarioLogado(usuarioAtualizado);
                    } catch (UsuarioNaoEncontradoException e) {
                        showAlert(Alert.AlertType.ERROR, "Erro", "Usuário não encontrado.", "O usuário logado não foi encontrado.");
                    }
                }

            } else {
                System.out.println("DietaController.adicionarDietaPreDefinida: Usuário logado é nulo.");// PRINT
                showAlert(Alert.AlertType.ERROR, "Erro", "Nenhum usuário logado", "Não foi possível adicionar a dieta.");
            }

        } catch (DietaNaoEncontradaException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao adicionar dieta", e.getMessage());
        }
    }
    public void atualizarTabelaDietasUsuario() {
        System.out.println("DietaController.atualizarTabelaDietasUsuario: Iniciando...");
        try {
            List<Dieta> listaDietas = fachada.listarDietas();

            if(mainController != null && mainController.getUsuarioLogado() != null){
                System.out.println("DietaController.atualizarTabelaDietasUsuario: Filtrando dietas para o usuário: " + mainController.getUsuarioLogado().getEmail()); //PRINT
                List<Dieta> dietasDoUsuario = listaDietas.stream()
                        .filter(dieta -> dieta.getUsuario() != null && dieta.getUsuario().getId().equals(mainController.getUsuarioLogado().getId()))
                        .collect(Collectors.toList());

                tabelaDietasUsuario.setItems(FXCollections.observableArrayList(dietasDoUsuario));
            } else{
                System.out.println("DietaController.atualizarTabelaDietasUsuario: Usuário logado é nulo. Exibindo lista vazia."); //PRINT
                tabelaDietasUsuario.setItems(FXCollections.observableArrayList()); //Define uma lista vazia.
            }

            if (!tabelaDietasUsuario.getItems().isEmpty()) {
                tabelaDietasUsuario.getSelectionModel().select(0);
                exibirDetalhesDieta(tabelaDietasUsuario.getItems().get(0)); //Mostra detalhes
            } else {
                exibirDetalhesDieta(null); // Limpa os detalhes.
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar dietas", e.getMessage());
            e.printStackTrace(); //  Imprime o stack trace
        }
    }



    private void exibirDetalhesDieta(Dieta dieta) {
        if (dieta != null) {
            if (labelNomeDieta != null) labelNomeDieta.setText("Nome: " + dieta.getNome());
            if (labelObjetivoDieta != null) labelObjetivoDieta.setText("Objetivo: " + dieta.getObjetivo());
            if (labelCaloriasDieta != null) labelCaloriasDieta.setText("Calorias: " + dieta.getCaloriasDiarias());
            if (labelProgressoDieta != null) labelProgressoDieta.setText("Progresso: " + String.format("%.2f%%", dieta.calcularProgresso()));
        } else {
            if (labelNomeDieta != null) labelNomeDieta.setText("Nome: ");
            if (labelObjetivoDieta != null) labelObjetivoDieta.setText("Objetivo: ");
            if (labelCaloriasDieta != null) labelCaloriasDieta.setText("Calorias: ");
            if (labelProgressoDieta != null) labelProgressoDieta.setText("Progresso: ");
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