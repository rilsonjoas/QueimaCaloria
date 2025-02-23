package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.MetaNaoEncontradaException;
import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Meta;
import com.example.queimacaloria.negocio.Usuario;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class EdicaoMetaController {

    @FXML
    private TextField campoDescricao;
    @FXML
    private ChoiceBox<Meta.Tipo> campoTipo;
    @FXML
    private TextField campoValorAlvo;
    @FXML
    private TextField campoProgressoAtual;
    @FXML
    private Label labelDataConclusao;
    @FXML
    private Button buttonConcluirMeta;
    @FXML
    private Label mensagemErro;

    private Fachada fachada = Fachada.getInstanciaUnica();

    private Meta meta;
    private MetaController metaController;
    private MainController mainController;

    public void setMetaController(MetaController metaController) {
        this.metaController = metaController;
    }

    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
        preencherCampos();
    }

    @FXML
    public void initialize() {
        campoTipo.setItems(FXCollections.observableArrayList(Meta.Tipo.values()));
    }

    private void preencherCampos() {
        if (meta != null) {
            campoDescricao.setText(meta.getDescricao());
            campoTipo.setValue(meta.getTipo());
            campoValorAlvo.setText(String.valueOf(meta.getValorAlvo()));
            campoProgressoAtual.setText(String.valueOf(meta.getProgressoAtual()));

            if (meta.getDataConclusao() != null) {
                labelDataConclusao.setText("Concluída em: " + meta.getDataConclusao().toString());
            } else {
                labelDataConclusao.setText("Meta não concluída");
            }

            if (meta.getDataConclusao() != null) {
                buttonConcluirMeta.setDisable(true);
            }
        }
    }

    @FXML
    public void atualizarMeta() {
        try {
            String descricao = campoDescricao.getText();
            Meta.Tipo tipo = campoTipo.getValue();
            double valorAlvo = Double.parseDouble(campoValorAlvo.getText());
            double progressoAtual = Double.parseDouble(campoProgressoAtual.getText());

            // Usa o método *atualizarMeta* da Fachada, passando o ID da meta
            fachada.atualizarMeta(meta.getId(), descricao, tipo, valorAlvo, progressoAtual, meta.getDataConclusao());
            mensagemErro.setText("Meta atualizada com sucesso!");

            // Atualiza o usuário no MainController *após* a atualização
            if (mainController != null) {
                mainController.atualizarDadosTelaPrincipal(); // Atualiza a tela *principal*
            }


            fecharJanela();

        }  catch (NumberFormatException | MetaNaoEncontradaException e) {
            mensagemErro.setText("Erro: " + e.getMessage());
            System.out.println("Exceção em atualizarMeta: " + e.getMessage());
            e.printStackTrace();

        } catch (Exception e) {
            mensagemErro.setText("Erro inesperado: " + e.getMessage());
            System.out.println("Exceção inesperada em atualizarMeta: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    public void concluirMeta() {
        try {
            // Atualiza o progresso e a data *na instância local* da meta
            meta.setProgressoAtual(meta.getValorAlvo());
            meta.setDataConclusao(LocalDate.now());

            // Usa o método *atualizarMeta* da Fachada, passando o ID
            fachada.atualizarMeta(meta.getId(), meta.getDescricao(), meta.getTipo(), meta.getValorAlvo(), meta.getProgressoAtual(), meta.getDataConclusao());

            labelDataConclusao.setText("Concluída em: " + meta.getDataConclusao().toString());
            buttonConcluirMeta.setDisable(true);
            campoProgressoAtual.setText(String.valueOf(meta.getProgressoAtual()));

            if (metaController != null) {
                metaController.atualizarTabelaMetasUsuario(); // Atualiza a tabela *na tela de metas*
            }

            // Atualiza o usuário no MainController *após* a atualização
            if (mainController != null) {
                mainController.atualizarDadosTelaPrincipal(); // Atualiza a tela *principal*
            }

            mensagemErro.setText("Meta concluída com sucesso!");

        } catch (MetaNaoEncontradaException e) {
            mensagemErro.setText("Erro ao concluir meta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void fecharJanela() {
        Stage stage = (Stage) campoDescricao.getScene().getWindow();
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