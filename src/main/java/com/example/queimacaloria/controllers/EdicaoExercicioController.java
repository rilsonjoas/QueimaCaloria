package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.ExercicioNaoEncontradoException;
import com.example.queimacaloria.negocio.Exercicio;
import com.example.queimacaloria.negocio.Fachada;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EdicaoExercicioController {

    @FXML private TextField campoNome;
    @FXML private TextField campoDescricao;
    @FXML private ChoiceBox<Exercicio.TipoExercicio> campoTipo;
    @FXML private TextField campoTempo;
    @FXML private TextField campoCaloriasPorMinuto;
    @FXML private Label mensagemErro;

    private Fachada fachada = Fachada.getInstanciaUnica();
    private Exercicio exercicio; // O exercício a ser editado
    private ExercicioController exercicioController;

    public void setExercicioController(ExercicioController exercicioController) {
        this.exercicioController = exercicioController;
    }

    public void setExercicio(Exercicio exercicio) {
        this.exercicio = exercicio;
        preencherCampos();
    }

    @FXML
    public void initialize() {
        campoTipo.setItems(FXCollections.observableArrayList(Exercicio.TipoExercicio.values()));
    }

    private void preencherCampos() {
        if (exercicio != null) {
            campoNome.textProperty().bindBidirectional(exercicio.nomeProperty());
            campoDescricao.setText(exercicio.getDescricao()); //Descricao nao tem binding
            campoTipo.valueProperty().bindBidirectional(exercicio.tipoProperty());
            campoTempo.textProperty().bindBidirectional(exercicio.tempoProperty(), new javafx.util.converter.NumberStringConverter());
            campoCaloriasPorMinuto.textProperty().bindBidirectional(exercicio.caloriasQueimadasProperty(), new javafx.util.converter.NumberStringConverter());
        }
    }


    @FXML
    public void atualizarExercicio() {
        try {

            exercicio.setDescricao(campoDescricao.getText()); //Atualiza a descrição que não tem binding.

            fachada.configurarExercicio(exercicio, exercicio.getNome(), exercicio.getDescricao(),
                    exercicio.getTipo(), exercicio.getTempo(),
                    exercicio.getCaloriasQueimadasPorMinuto());

            mensagemErro.setText("Exercício atualizado com sucesso!");

            if (exercicioController != null) {
                exercicioController.initialize();
            }
            fecharJanela();

        } catch (NumberFormatException e) {
            mensagemErro.setText("Erro: Tempo e calorias devem ser números válidos.");
        } catch (ExercicioNaoEncontradoException e) {
            mensagemErro.setText("Erro ao atualizar exercicio: " + e.getMessage());
        }
        catch (Exception e) {
            mensagemErro.setText("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void fecharJanela() {
        Stage stage = (Stage) campoNome.getScene().getWindow();
        stage.close();
    }
}