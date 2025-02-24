package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;
import com.example.queimacaloria.negocio.Dieta;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Meta;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EdicaoDietaController {

    @FXML private TextField campoNome;
    @FXML private ChoiceBox<Meta.Tipo> campoObjetivo;
    @FXML private TextField campoCalorias;
    @FXML private Label mensagemErro;

    private Fachada fachada = Fachada.getInstanciaUnica();
    private Dieta dieta;
    private DietaController dietaController;
    private MainController mainController;

    // Define o controlador da tela de dietas.
    public void setDietaController(DietaController dietaController) {
        this.dietaController = dietaController;
    }

    // Define o controlador principal.
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    // Define a dieta a ser editada.
    public void setDieta(Dieta dieta) {
        this.dieta = dieta;
        preencherCampos();
    }

    // Inicializa o controlador, configurando o ChoiceBox de objetivo.
    @FXML
    public void initialize() {
        campoObjetivo.setItems(FXCollections.observableArrayList(Meta.Tipo.values()));
    }

    // Preenche os campos com os dados da dieta.
    private void preencherCampos() {
        if (dieta != null) {
            campoNome.setText(dieta.getNome());
            campoObjetivo.setValue(dieta.getObjetivo()); // Usar o valor correto (Meta.Tipo)
            campoCalorias.setText(String.valueOf(dieta.getCaloriasDiarias()));
        }
    }

    // Atualiza os dados da dieta.
    @FXML
    public void atualizarDieta() {
        try {
            String nome = campoNome.getText();
            Meta.Tipo objetivo = campoObjetivo.getValue();
            int calorias = Integer.parseInt(campoCalorias.getText());

            fachada.configurarDieta(dieta, nome, objetivo, calorias, dieta.getUsuario());
            mensagemErro.setText("Dieta atualizada com sucesso!");

            // Atualizar a tabela na tela principal *após* a edição
            if(dietaController != null){
                dietaController.atualizarTabelaDietasUsuario();
            }

            fecharJanela();

        } catch (NumberFormatException e) {
            mensagemErro.setText("Erro: Calorias devem ser um número inteiro.");
        } catch (DietaNaoEncontradaException e) {
            mensagemErro.setText("Erro ao atualizar dieta: " + e.getMessage());
        }
    }


    // Fecha a janela atual.
    @FXML
    private void fecharJanela() {
        Stage stage = (Stage) campoNome.getScene().getWindow();
        stage.close();
    }
}