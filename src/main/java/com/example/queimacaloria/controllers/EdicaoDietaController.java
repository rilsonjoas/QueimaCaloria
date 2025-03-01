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
            campoObjetivo.setValue(dieta.getObjetivo());
            campoCalorias.setText(String.valueOf(dieta.getCaloriasDiarias()));
        }
    }

    // Atualiza os dados da dieta.
    @FXML
    public void atualizarDieta() {
        String nome = campoNome.getText();
        Meta.Tipo objetivo = campoObjetivo.getValue();
        String caloriasStr = campoCalorias.getText();

        if (!validarFormulario(nome, objetivo, caloriasStr)) {
            return; // Aborta se a validação falhar
        }

        try {
            int calorias = Integer.parseInt(caloriasStr);
            fachada.configurarDieta(dieta, nome, objetivo, calorias, dieta.getUsuario());
            mensagemErro.setText("Dieta atualizada com sucesso!");

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

    //Função auxiliar para validar o formulário.
    private boolean validarFormulario(String nome, Meta.Tipo objetivo, String caloriasStr) {
        if (nome == null || nome.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O nome não pode estar vazio.");
            return false;
        }

        if (objetivo == null) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O objetivo não pode ser nulo.");
            return false;
        }

        if (caloriasStr == null || caloriasStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "As calorias não podem estar vazias.");
            return false;
        }

        if (!isNumeric(caloriasStr)) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "As calorias devem ser um número.");
            return false;
        }

        return true;
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