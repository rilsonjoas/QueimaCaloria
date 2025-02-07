package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.ExercicioNaoEncontradoException;
import com.example.queimacaloria.negocio.Exercicio;
import com.example.queimacaloria.negocio.Fachada;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Setter;

// Controller para a tela de criação de exercício
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
    private TextField campoCaloriasPorMinuto;
    @FXML
    private Label mensagemErro;

    private Fachada fachada = Fachada.getInstanciaUnica();

    @Setter
    private ExercicioController exercicioController;

    @FXML
    // Inicializa o ChoiceBox com os tipos de exercício
    public void initialize() {
        campoTipo.setItems(FXCollections.observableArrayList(Exercicio.TipoExercicio.values()));
    }

    @FXML
    // Método chamado ao clicar no botão "Criar Exercício"
    public void criarExercicio() {
        try {
            String nome = campoNome.getText();
            String descricao = campoDescricao.getText();
            Exercicio.TipoExercicio tipo = campoTipo.getValue();
            int tempo = Integer.parseInt(campoTempo.getText());
            double caloriasPorMinuto = Double.parseDouble(campoCaloriasPorMinuto.getText());

            Exercicio novoExercicio = new Exercicio();
            fachada.configurarExercicio(novoExercicio, nome, descricao, tipo, tempo, caloriasPorMinuto);

            mensagemErro.setText("Exercício criado com sucesso!");

            // Atualiza a tabela de exercícios no ExercicioController
            if (exercicioController != null) {
                exercicioController.initialize();
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

    @FXML
    // Fecha a janela atual
    private void fecharJanela() {
        Stage stage = (Stage) campoNome.getScene().getWindow();
        stage.close();
    }
}