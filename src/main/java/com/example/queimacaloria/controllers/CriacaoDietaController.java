// CriacaoDietaController.java
package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;
import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;
import com.example.queimacaloria.negocio.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CriacaoDietaController {

    @FXML private TextField campoNome;
    @FXML private ChoiceBox<Meta.Tipo> campoObjetivo;
    @FXML private TextField campoCalorias;
    @FXML private Label mensagemErro;

    private Fachada fachada = Fachada.getInstanciaUnica();
    private DietaController dietaController;
    private MainController mainController;

    // Define o controlador da tela de dieta.
    public void setDietaController(DietaController dietaController) {
        this.dietaController = dietaController;
    }

    // Define o controlador principal.
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    // Inicializa o controlador, configurando o ChoiceBox de objetivo.
    @FXML
    public void initialize() {
        campoObjetivo.setItems(FXCollections.observableArrayList(Meta.Tipo.values()));
    }
    @FXML
    public void salvarDieta() {
        //Validações
        if (campoNome.getText() == null || campoNome.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Nome inválido", "O nome da dieta não pode estar vazio.");
            return;
        }
        if (campoObjetivo.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Objetivo inválido", "Selecione um objetivo.");
            return;
        }
        if (campoCalorias.getText() == null || campoCalorias.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Calorias inválidas", "O número de calorias não pode estar vazio.");
            return;
        }
        try {
            Integer.parseInt(campoCalorias.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Calorias inválidas", "Insira um número válido para as calorias.");
            return;
        }

        try {
            Dieta novaDieta = new Dieta(
                    campoNome.getText(),
                    campoObjetivo.getValue(),
                    Integer.parseInt(campoCalorias.getText()),
                    mainController.getUsuarioLogado()

            );

            fachada.configurarDieta(novaDieta, novaDieta.getNome(), novaDieta.getObjetivo(),
                    novaDieta.getCaloriasDiarias(), novaDieta.getUsuario(), novaDieta.getTipoDieta());
            mainController.getUsuarioLogado().getDietas().add(novaDieta);
            Fachada.getInstanciaUnica().setDietaAtiva(mainController.getUsuarioLogado(), novaDieta); // Define como ativa

            if (dietaController != null) {
                dietaController.atualizarTabelaDietasUsuario(); //Já atualiza.
            }
            if(mainController != null){
                mainController.atualizarDadosTelaPrincipal(); //Já atualiza
            }

            Stage stage = (Stage) campoNome.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao criar dieta", e.getMessage());
            e.printStackTrace();
        }
    }


    //Função auxiliar para verificar se é um número.
    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Fecha a janela atual.
    @FXML
    private void fecharJanela() {
        Stage stage = (Stage) campoNome.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}