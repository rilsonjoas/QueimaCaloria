package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;
import com.example.queimacaloria.negocio.Dieta;
import com.example.queimacaloria.negocio.Fachada;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

// Controller para a tela de gerenciamento de dietas.
public class DietaController {

    @FXML private TableView<Dieta> tabelaDietas;
    @FXML private TableColumn<Dieta, String> colunaNome;
    @FXML private TableColumn<Dieta, Dieta.ObjetivoDieta> colunaObjetivo;
    @FXML private TableColumn<Dieta, Integer> colunaCalorias;
    @FXML private TableColumn<Dieta, Double> colunaProgresso;

    @FXML private Label mensagemDieta;
    @FXML private Label labelNomeDieta;
    @FXML private Label labelObjetivoDieta;
    @FXML private Label labelCaloriasDieta;
    @FXML private Label labelProgressoDieta;

    private Fachada fachada = Fachada.getInstanciaUnica();
    private MainController mainController;

    // Injeta o MainController para poder voltar à tela principal.
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    // Inicializa a tabela de dietas e configura os listeners.
    public void initialize() {
        // Define como cada coluna da tabela exibe os dados da Dieta.
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaObjetivo.setCellValueFactory(new PropertyValueFactory<>("objetivo"));
        colunaCalorias.setCellValueFactory(new PropertyValueFactory<>("caloriasDiarias"));
        colunaProgresso.setCellValueFactory(cellData -> cellData.getValue().progressoDaDietaProperty().asObject());

        atualizarTabelaDietas();

        tabelaDietas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Exibe os detalhes da dieta selecionada nos labels.
                labelNomeDieta.setText("Nome: " + newSelection.getNome().get());
                labelObjetivoDieta.setText("Objetivo: " + newSelection.getObjetivo().get());
                labelCaloriasDieta.setText("Calorias: " + newSelection.getCaloriasDiarias().get());
                labelProgressoDieta.setText("Progresso: " + String.format("%.2f%%", newSelection.calcularProgresso()));
            } else {
                // Limpa os labels se nenhuma dieta estiver selecionada.
                labelNomeDieta.setText("Nome: ");
                labelObjetivoDieta.setText("Objetivo: ");
                labelCaloriasDieta.setText("Calorias: ");
                labelProgressoDieta.setText("Progresso: ");

            }
        });
    }

    @FXML
    // Abre a tela para criar uma nova dieta.
    public void abrirTelaCriacaoDieta() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/criacao-dieta-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Criar Nova Dieta");
            stage.setScene(scene);


            CriacaoDietaController controller = loader.getController();
            controller.setDietaController(this);


            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao abrir tela", e.getMessage());
        }
    }

    @FXML
    // Atualiza uma dieta existente.
    public void realizarAtualizacaoDieta() {
        Dieta dietaSelecionada = tabelaDietas.getSelectionModel().getSelectedItem();
        if (dietaSelecionada != null) {
            try {
                fachada.configurarDieta(dietaSelecionada, dietaSelecionada.getNome().get(),
                        dietaSelecionada.getObjetivo().get(), dietaSelecionada.getCaloriasDiarias().get(),
                        dietaSelecionada.getUsuario().get());
                atualizarTabelaDietas();
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
    // Remove uma dieta existente.
    public void realizarRemocaoDieta() {
        Dieta dietaSelecionada = tabelaDietas.getSelectionModel().getSelectedItem();
        if (dietaSelecionada != null) {
            try {
                fachada.configurarDieta(dietaSelecionada, null, null, 0, null);
                atualizarTabelaDietas();
                mensagemDieta.setText("Dieta removida com sucesso!");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao remover dieta", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhuma dieta selecionada",
                    "Por favor, selecione uma dieta para remover.");
        }
    }

    // Atualiza a tabela de dietas com os dados do repositório.
    private void atualizarTabelaDietas() {
        try {
            List<Dieta> listaDietas = fachada.listarDietas();
            tabelaDietas.setItems(FXCollections.observableArrayList(listaDietas));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar dietas", e.getMessage());
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

    @FXML
    // Volta para a tela principal.
    public void voltarParaTelaPrincipal() {
        if (mainController != null) {
            mainController.mostrarTelaPrincipal();
        } else {
            System.err.println("Erro: MainController não foi injetado!");
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro interno", "MainController não foi configurado corretamente.");
        }
    }
}
