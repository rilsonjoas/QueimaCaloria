package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.ExercicioNaoEncontradoException;
import com.example.queimacaloria.negocio.Exercicio;
import com.example.queimacaloria.negocio.Fachada;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Setter;

public class CriacaoExercicioController {

    @FXML
    private TextField campoNome;
    @FXML
    private TextField campoDescricao;
    @FXML
    private ChoiceBox<Exercicio.TipoExercicio> campoTipo;
    @FXML
    private TextField campoTempo;
    @FXML
    private TextField campoCaloriasQueimadas;

    @FXML
    private Label mensagemErro;

    private Fachada fachada = Fachada.getInstanciaUnica();

    @Setter
    private ExercicioController exercicioController;

    private MainController mainController;

    // Define o controlador principal.
    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }
    // Inicializa o ChoiceBox com os tipos de exercício.
    @FXML
    public void initialize() {
        campoTipo.setItems(FXCollections.observableArrayList(Exercicio.TipoExercicio.values()));
    }

    // Cria um novo exercício.
    @FXML
    public void criarExercicio() {
        try {
            String nome = campoNome.getText();
            String descricao = campoDescricao.getText();
            Exercicio.TipoExercicio tipo = campoTipo.getValue();
            int tempo = Integer.parseInt(campoTempo.getText());
            double caloriasQueimadas = Double.parseDouble(campoCaloriasQueimadas.getText());

            Exercicio novoExercicio = new Exercicio();
            fachada.configurarExercicio(novoExercicio, nome, descricao, tipo, tempo, caloriasQueimadas);

            mensagemErro.setText("Exercício criado com sucesso!");

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
            mensagemErro.setText("Erro ao criar exercicio: " + e.getMessage());
        } catch (Exception e) {
            mensagemErro.setText("Erro inesperado: " + e.getMessage());
        }

    }

    // Fecha a janela atual.
    @FXML
    private void fecharJanela() {
        Stage stage = (Stage) campoNome.getScene().getWindow();
        stage.close();
    }
}