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
    private Exercicio exercicio;
    private ExercicioController exercicioController;
    //ADD
    private MainController mainController;

    public void setExercicioController(ExercicioController exercicioController) {
        this.exercicioController = exercicioController;
    }
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
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
            campoNome.setText(exercicio.getNome());
            campoDescricao.setText(exercicio.getDescricao());
            campoTipo.setValue(exercicio.getTipo());
            campoTempo.setText(String.valueOf(exercicio.getTempo()));
            campoCaloriasPorMinuto.setText(String.valueOf(exercicio.getCaloriasQueimadasPorMinuto()));
        }
    }


    @FXML
    public void atualizarExercicio() {
        try {
            // Obtém os valores dos campos
            String nome = campoNome.getText();
            String descricao = campoDescricao.getText();
            Exercicio.TipoExercicio tipo = campoTipo.getValue();
            int tempo = Integer.parseInt(campoTempo.getText());
            double caloriasPorMinuto = Double.parseDouble(campoCaloriasPorMinuto.getText());

            fachada.configurarExercicio(exercicio, nome, descricao, tipo, tempo, caloriasPorMinuto);

            mensagemErro.setText("Exercício atualizado com sucesso!");

            if (exercicioController != null) {
                exercicioController.initialize(); //  Chama initialize
            }
            if(mainController != null){
                mainController.atualizarDadosTelaPrincipal();
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