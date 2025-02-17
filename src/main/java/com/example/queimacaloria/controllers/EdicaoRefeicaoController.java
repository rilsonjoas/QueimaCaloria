package com.example.queimacaloria.controllers;

import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Refeicao;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.HashMap; // Importante
import java.util.Map;


public class EdicaoRefeicaoController {

    @FXML private TextField campoNome;
    @FXML private TextField campoDescricao;
    @FXML private TextField campoProteinas;
    @FXML private TextField campoCarboidratos;
    @FXML private TextField campoGorduras;
    @FXML private Label mensagemErro;

    private Fachada fachada = Fachada.getInstanciaUnica();
    private Refeicao refeicao; // A refeição a ser editada
    private RefeicaoController refeicaoController;

    public void setRefeicaoController(RefeicaoController refeicaoController) {
        this.refeicaoController = refeicaoController;
    }


    public void setRefeicao(Refeicao refeicao) {
        this.refeicao = refeicao;
        preencherCampos();
    }

    private void preencherCampos() {
        if (refeicao != null) {
            campoNome.textProperty().bindBidirectional(refeicao.nomeProperty());
            campoDescricao.setText(refeicao.getDescricao()); // Descrição não tem binding.

            // Para os macronutrientes, não usamos binding (é mais complexo com Map).
            // Em vez disso, preenchemos os campos diretamente *e* atualizamos
            // manualmente no método atualizarRefeicao().
            if (refeicao.getMacronutrientes() != null) {
                campoProteinas.setText(String.valueOf(refeicao.getMacronutrientes().getOrDefault("Proteínas", 0.0)));
                campoCarboidratos.setText(String.valueOf(refeicao.getMacronutrientes().getOrDefault("Carboidratos", 0.0)));
                campoGorduras.setText(String.valueOf(refeicao.getMacronutrientes().getOrDefault("Gorduras", 0.0)));
            }
        }
    }

    @FXML
    public void atualizarRefeicao() {
        try {
            // Atualiza a descrição
            refeicao.setDescricao(campoDescricao.getText());

            // Coleta e atualiza os macronutrientes
            Map<String, Double> novosMacronutrientes = new HashMap<>();
            try {
                novosMacronutrientes.put("Proteínas", Double.parseDouble(campoProteinas.getText()));
                novosMacronutrientes.put("Carboidratos", Double.parseDouble(campoCarboidratos.getText()));
                novosMacronutrientes.put("Gorduras", Double.parseDouble(campoGorduras.getText()));

            }catch (NumberFormatException e){
                mensagemErro.setText("Erro: Os macronutrientes devem ser números");
                return;
            }
            refeicao.setMacronutrientes(novosMacronutrientes);
            // Recalcula as calorias (importante!)
            refeicao.setCalorias(fachada.calcularCaloriasRefeicao(refeicao));


            fachada.configurarRefeicao(refeicao, refeicao.getNome(), refeicao.getDescricao(), refeicao.getMacronutrientes());
            mensagemErro.setText("Refeição atualizada com sucesso!");
            if (refeicaoController != null) {
                refeicaoController.initialize();
            }

            fecharJanela();

        } catch (Exception e) {
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