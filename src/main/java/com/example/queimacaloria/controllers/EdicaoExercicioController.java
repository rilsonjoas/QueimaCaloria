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
    @FXML private TextField campoCaloriasQueimadas;
    @FXML private Label mensagemErro;

    private Fachada fachada = Fachada.getInstanciaUnica();
    private Exercicio exercicio;
    private ExercicioController exercicioController;
    private MainController mainController;

    // Define o controlador da tela de exercícios.
    public void setExercicioController(ExercicioController exercicioController) {
        this.exercicioController = exercicioController;
    }

    // Define o controlador principal.
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    // Define o exercício a ser editado.
    public void setExercicio(Exercicio exercicio) {
        this.exercicio = exercicio;
        preencherCampos();
    }

    // Inicializa o controlador, configurando o ChoiceBox de tipo.
    @FXML
    public void initialize() {
        campoTipo.setItems(FXCollections.observableArrayList(Exercicio.TipoExercicio.values()));
    }

    // Preenche os campos com os dados do exercício.
    private void preencherCampos() {
        if (exercicio != null) {
            campoNome.setText(exercicio.getNome());
            campoDescricao.setText(exercicio.getDescricao());
            campoTipo.setValue(exercicio.getTipo());
            campoTempo.setText(String.valueOf(exercicio.getTempo()));
            campoCaloriasQueimadas.setText(String.valueOf(exercicio.getCaloriasQueimadas()));
        }
    }

    // Atualiza os dados do exercício.
    @FXML
    public void atualizarExercicio() {
        try {
            String nome = campoNome.getText();
            String descricao = campoDescricao.getText();
            Exercicio.TipoExercicio tipo = campoTipo.getValue();
            int tempo = Integer.parseInt(campoTempo.getText());
            double caloriasQueimadas = Double.parseDouble(campoCaloriasQueimadas.getText());

            fachada.configurarExercicio(exercicio, nome, descricao, tipo, tempo, caloriasQueimadas);

            mensagemErro.setText("Exercício atualizado com sucesso!");

            if (exercicioController != null) {
                exercicioController.initialize();
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

    // Fecha a janela atual.
    @FXML
    private void fecharJanela() {
        Stage stage = (Stage) campoNome.getScene().getWindow();
        stage.close();
    }
}