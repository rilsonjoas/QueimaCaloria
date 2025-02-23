package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;
import com.example.queimacaloria.negocio.Dieta;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Usuario;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EdicaoDietaController {

    @FXML private TextField campoNome;
    @FXML private ChoiceBox<Dieta.ObjetivoDieta> campoObjetivo;
    @FXML private TextField campoCalorias;
    @FXML private Label mensagemErro;

    private Fachada fachada = Fachada.getInstanciaUnica();
    private Dieta dieta;
    private DietaController dietaController;
    private MainController mainController;

    public void setDietaController(DietaController dietaController) {
        this.dietaController = dietaController;
    }
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setDieta(Dieta dieta) {
        this.dieta = dieta;
        preencherCampos();
    }

    @FXML
    public void initialize() {
        campoObjetivo.setItems(FXCollections.observableArrayList(Dieta.ObjetivoDieta.values()));
    }

    private void preencherCampos() {
        if (dieta != null) {
            campoNome.setText(dieta.getNome());
            campoObjetivo.setValue(dieta.getObjetivo());
            campoCalorias.setText(String.valueOf(dieta.getCaloriasDiarias()));
        }
    }

    @FXML
    public void atualizarDieta() {
        try {
            String nome = campoNome.getText();
            Dieta.ObjetivoDieta objetivo = campoObjetivo.getValue();
            int calorias = Integer.parseInt(campoCalorias.getText());

            fachada.configurarDieta(dieta, nome, objetivo, calorias, dieta.getUsuario()); // Mantém o usuário
            mensagemErro.setText("Dieta atualizada com sucesso!");
            fecharJanela();

        } catch (NumberFormatException e) {
            mensagemErro.setText("Erro: Calorias devem ser um número inteiro.");
        } catch (DietaNaoEncontradaException e) {
            mensagemErro.setText("Erro ao atualizar dieta: " + e.getMessage());
        }
    }

    @FXML
    private void fecharJanela() {
        Stage stage = (Stage) campoNome.getScene().getWindow();
        stage.close();
    }
}