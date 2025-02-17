package com.example.queimacaloria.controllers;

import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Refeicao;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

// Controller para a tela de criação de refeição
public class CriacaoRefeicaoController {

    @FXML
    private TextField campoNome;
    @FXML
    private TextField campoDescricao;
    @FXML
    private TextField campoProteinas;
    @FXML
    private TextField campoCarboidratos;
    @FXML
    private TextField campoGorduras;
    @FXML
    private Label mensagemErro;

    private Fachada fachada = Fachada.getInstanciaUnica();

    private RefeicaoController refeicaoController;

    private MainController mainController; //ADD

    // Permite que o RefeicaoController seja acessado por esta classe
    public void setRefeicaoController(RefeicaoController refeicaoController) {
        this.refeicaoController = refeicaoController;
    }

    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }

    @FXML
    // Método chamado ao clicar no botão "Criar Refeição"
    public void criarRefeicao() {
        try {
            String nome = campoNome.getText();
            String descricao = campoDescricao.getText();

            // Coleta os macronutrientes
            Map<String, Double> macronutrientes = new HashMap<>();
            macronutrientes.put("Proteínas", Double.parseDouble(campoProteinas.getText()));
            macronutrientes.put("Carboidratos", Double.parseDouble(campoCarboidratos.getText()));
            macronutrientes.put("Gorduras", Double.parseDouble(campoGorduras.getText()));

            Refeicao novaRefeicao = new Refeicao();
            fachada.configurarRefeicao(novaRefeicao, nome, descricao, macronutrientes);

            mensagemErro.setText("Refeição criada com sucesso!");

            // Atualiza a tabela de refeições no RefeicaoController
            if (refeicaoController != null) {
                refeicaoController.initialize();
            }

            //Adicionado
            if(mainController != null){
                mainController.atualizarDadosTelaPrincipal();
            }

            fecharJanela();

        } catch (NumberFormatException e) {
            mensagemErro.setText("Erro: Valores de macronutrientes inválidos.");
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