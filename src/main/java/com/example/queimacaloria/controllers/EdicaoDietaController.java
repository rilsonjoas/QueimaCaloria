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
    private Dieta dieta;  // A dieta a ser editada
    private DietaController dietaController;

    public void setDietaController(DietaController dietaController) {
        this.dietaController = dietaController;
    }


    // Método para receber a dieta a ser editada
    public void setDieta(Dieta dieta) {
        this.dieta = dieta;
        preencherCampos(); // Preenche os campos com os dados da dieta
    }

    @FXML
    public void initialize() {
        campoObjetivo.setItems(FXCollections.observableArrayList(Dieta.ObjetivoDieta.values()));
        // Os bindings serão feitos no preencherCampos()
    }

    // Preenche os campos com os dados da dieta
    private void preencherCampos() {
        if (dieta != null) {
            campoNome.textProperty().bindBidirectional(dieta.nomeProperty());
            campoObjetivo.valueProperty().bindBidirectional(dieta.objetivoProperty());
            campoCalorias.textProperty().bindBidirectional(dieta.caloriasDiariasProperty(), new javafx.util.converter.NumberStringConverter());
        }
    }

    @FXML
    public void atualizarDieta() {
        try {
            // O nome, objetivo e calorias *já foram atualizados* na instância 'dieta'
            // devido aos bindings bidirecionais.
            fachada.configurarDieta(dieta, dieta.getNome(), dieta.getObjetivo(), dieta.getCaloriasDiarias(), dieta.getUsuario());
            mensagemErro.setText("Dieta atualizada com sucesso!");

            if (dietaController != null) {
                dietaController.initialize(); //Atualiza a tabela na tela principal.
            }


            fecharJanela(); // Fecha a janela de edição

        } catch (NumberFormatException e) {
            mensagemErro.setText("Erro: Calorias devem ser um número inteiro.");
        } catch (DietaNaoEncontradaException e) {
            mensagemErro.setText("Erro ao atualizar dieta: " + e.getMessage());
        } catch (Exception e) {
            mensagemErro.setText("Erro inesperado: " + e.getMessage());
            e.printStackTrace(); // Boa prática: imprimir o stack trace
        }
    }

    @FXML
    private void fecharJanela() {
        Stage stage = (Stage) campoNome.getScene().getWindow();
        stage.close();
    }

    //Método showAlert (para não repetir código)
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}