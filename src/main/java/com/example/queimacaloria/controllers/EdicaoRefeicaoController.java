package com.example.queimacaloria.controllers;

import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Refeicao;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;


public class EdicaoRefeicaoController {

    @FXML private TextField campoNome;
    @FXML private TextField campoDescricao;
    @FXML private TextField campoProteinas;
    @FXML private TextField campoCarboidratos;
    @FXML private TextField campoGorduras;
    @FXML private Label mensagemErro;

    private Fachada fachada = Fachada.getInstanciaUnica();
    private Refeicao refeicao;
    private RefeicaoController refeicaoController;
    private MainController mainController;

    // Define o controlador da tela de refeições.
    public void setRefeicaoController(RefeicaoController refeicaoController) {
        this.refeicaoController = refeicaoController;
    }

    // Define o controlador principal.
    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }

    // Define a refeição a ser editada.
    public void setRefeicao(Refeicao refeicao) {
        this.refeicao = refeicao;
        preencherCampos();
    }

    // Preenche os campos com os dados da refeição.
    private void preencherCampos() {
        if (refeicao != null) {
            campoNome.setText(refeicao.getNome());
            campoDescricao.setText(refeicao.getDescricao());


            if (refeicao.getMacronutrientes() != null) {
                campoProteinas.setText(String.valueOf(refeicao.getMacronutrientes().getOrDefault("Proteínas", 0.0)));
                campoCarboidratos.setText(String.valueOf(refeicao.getMacronutrientes().getOrDefault("Carboidratos", 0.0)));
                campoGorduras.setText(String.valueOf(refeicao.getMacronutrientes().getOrDefault("Gorduras", 0.0)));
            }
        }
    }

    // Atualiza os dados da refeição.
    @FXML
    public void atualizarRefeicao() {
        try {
            String nome = campoNome.getText();
            String descricao = campoDescricao.getText();

            Map<String, Double> novosMacronutrientes = new HashMap<>();
            try {
                novosMacronutrientes.put("Proteínas", Double.parseDouble(campoProteinas.getText()));
                novosMacronutrientes.put("Carboidratos", Double.parseDouble(campoCarboidratos.getText()));
                novosMacronutrientes.put("Gorduras", Double.parseDouble(campoGorduras.getText()));

            }catch (NumberFormatException e){
                mensagemErro.setText("Erro: Os macronutrientes devem ser números");
                return;
            }

            fachada.configurarRefeicao(refeicao,nome, descricao, novosMacronutrientes);
            mensagemErro.setText("Refeição atualizada com sucesso!");

            if (refeicaoController != null) {
                refeicaoController.initialize();
            }

            if(mainController != null){
                mainController.atualizarDadosTelaPrincipal();
            }

            fecharJanela();

        } catch (Exception e) {
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