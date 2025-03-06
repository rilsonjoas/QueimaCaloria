package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.RefeicaoNaoEncontradaException;
import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Refeicao;
import com.example.queimacaloria.negocio.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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

        String nome = campoNome.getText();
        String descricao = campoDescricao.getText();
        String proteinasStr = campoProteinas.getText();
        String carboidratosStr = campoCarboidratos.getText();
        String gordurasStr = campoGorduras.getText();

        if (!validarFormulario(nome, descricao, proteinasStr, carboidratosStr, gordurasStr)) {
            return; // Aborta se a validação falhar
        }

        try {
            Map<String, Double> novosMacronutrientes = new HashMap<>();
            try {
                novosMacronutrientes.put("Proteínas", Double.parseDouble(campoProteinas.getText()));
                novosMacronutrientes.put("Carboidratos", Double.parseDouble(campoCarboidratos.getText()));
                novosMacronutrientes.put("Gorduras", Double.parseDouble(campoGorduras.getText()));

            }catch (NumberFormatException e){
                mensagemErro.setText("Erro: Os macronutrientes devem ser números");
                return;
            }
            // Passamos o usuário logado.
            fachada.configurarRefeicao(refeicao,nome, descricao, novosMacronutrientes, mainController.getUsuarioLogado());
            mensagemErro.setText("Refeição atualizada com sucesso!");

            // **********  MUDANÇA AQUI ***********
            // Atualiza o usuário logado:  Isso é *crítico*.
            if (mainController != null && mainController.getUsuarioLogado() != null) {
                try {
                    Usuario usuarioAtualizado = fachada.buscarUsuarioPorId(mainController.getUsuarioLogado().getId());
                    mainController.setUsuarioLogado(usuarioAtualizado); // Atualiza o usuário no MainController
                }
                catch (UsuarioNaoEncontradoException e){
                    showAlert(Alert.AlertType.ERROR, "Erro", "Usuário não encontrado.",
                            "O usuário logado não pôde ser encontrado.");
                }
            }

            if (refeicaoController != null) {
                refeicaoController.initialize(); //Remover, pois o listener ja atualiza.
            }

            if(mainController != null){
                mainController.atualizarDadosTelaPrincipal();// Removido
                mainController.atualizarCalorias();

            }

            fecharJanela();

        } catch (Exception e) {
            mensagemErro.setText("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Função auxiliar para validar o formulário.
    private boolean validarFormulario(String nome, String descricao, String proteinasStr, String carboidratosStr, String gordurasStr) {
        if (nome == null || nome.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O nome não pode estar vazio.");
            return false;
        }

        if (descricao == null || descricao.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "A descrição não pode estar vazia.");
            return false;
        }

        if (proteinasStr == null || proteinasStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "A proteína não pode estar vazia.");
            return false;
        }

        if (!isNumeric(proteinasStr)) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "A proteína deve ser um número.");
            return false;
        }

        if (carboidratosStr == null || carboidratosStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "Os carboidratos não podem estar vazios.");
            return false;
        }

        if (!isNumeric(carboidratosStr)) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "Os carboidratos devem ser um número.");
            return false;
        }

        if (gordurasStr == null || gordurasStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "A gordura não pode estar vazia.");
            return false;
        }

        if (!isNumeric(gordurasStr)) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "A gordura deve ser um número.");
            return false;
        }

        return true;
    }

    //Função auxiliar para verificar se é um número.
    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
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