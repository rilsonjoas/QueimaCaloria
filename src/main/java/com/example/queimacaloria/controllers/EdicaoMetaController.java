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

    // Define o controlador da tela de metas.
    public void setMetaController(MetaController metaController) {
        this.metaController = metaController;
    }

    // Define o controlador principal.
    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }

    // Define a meta a ser editada.
    public void setMeta(Meta meta) {
        this.meta = meta;
        preencherCampos();
    }

    // Inicializa o controlador, configurando o ChoiceBox.
    @FXML
    public void initialize() {
        campoTipo.setItems(FXCollections.observableArrayList(Meta.Tipo.values()));
    }

    // Preenche os campos com os dados da meta.
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

    // Atualiza os dados da meta.
    @FXML
    public void atualizarMeta() {
        try {
            String descricao = campoDescricao.getText();
            Meta.Tipo tipo = campoTipo.getValue();
            double valorAlvo = Double.parseDouble(campoValorAlvo.getText());
            double progressoAtual = Double.parseDouble(campoProgressoAtual.getText());

            System.out.println("EdicaoMetaController.atualizarMeta:");
            System.out.println("  Descrição: " + descricao);
            System.out.println("  Tipo: " + tipo);
            System.out.println("  Valor Alvo: " + valorAlvo);
            System.out.println("  Progresso Atual: " + progressoAtual);
            System.out.println("  Data Conclusão (antes): " + meta.getDataConclusao());

            fachada.configurarMeta(meta, descricao, tipo, valorAlvo, progressoAtual, meta.getDataConclusao());
            mensagemErro.setText("Meta atualizada com sucesso!");

            System.out.println("  Data Conclusão (depois): " + meta.getDataConclusao());

            if (mainController != null && mainController.getUsuarioLogado() != null) {
                try {
                    Usuario usuarioAtualizado = fachada.buscarUsuarioPorId(mainController.getUsuarioLogado().getId());
                    mainController.setUsuarioLogado(usuarioAtualizado);
                    mainController.atualizarDadosTelaPrincipal();
                } catch (UsuarioNaoEncontradoException e) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Usuário não encontrado.",
                            "O usuário logado não pôde ser encontrado após a atualização da meta.");
                }
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

    // Conclui a meta.
    @FXML
    public void concluirMeta() {
        try {
            meta.setProgressoAtual(meta.getValorAlvo());
            meta.setDataConclusao(LocalDate.now());

            fachada.configurarMeta(meta, meta.getDescricao(), meta.getTipo(), meta.getValorAlvo(), meta.getProgressoAtual(), meta.getDataConclusao());
            labelDataConclusao.setText("Concluída em: " + meta.getDataConclusao().toString());
            buttonConcluirMeta.setDisable(true);
            campoProgressoAtual.setText(String.valueOf(meta.getProgressoAtual()));

            if (metaController != null) {
                metaController.atualizarTabelaMetasUsuario();
            }

            if (mainController != null && mainController.getUsuarioLogado() != null) {
                try {
                    Usuario usuarioAtualizado = fachada.buscarUsuarioPorId(mainController.getUsuarioLogado().getId());
                    mainController.setUsuarioLogado(usuarioAtualizado);
                    mainController.atualizarDadosTelaPrincipal();
                } catch (UsuarioNaoEncontradoException e) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Usuário não encontrado.",
                            "O usuário logado não pôde ser encontrado.");
                }
            }
            mensagemErro.setText("Meta concluída com sucesso!");
        } catch (MetaNaoEncontradaException e) {
            mensagemErro.setText("Erro ao concluir meta: " + e.getMessage());
        }
    }

    // Fecha a janela atual.
    @FXML
    private void fecharJanela() {
        Stage stage = (Stage) campoDescricao.getScene().getWindow();
        stage.close();
    }

    // Exibe um alerta na tela.
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}