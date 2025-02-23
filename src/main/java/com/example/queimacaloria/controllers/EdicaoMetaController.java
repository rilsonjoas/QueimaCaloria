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

    @FXML private TextField campoDescricao;
    @FXML private ChoiceBox<Meta.Tipo> campoTipo;
    @FXML private TextField campoValorAlvo;
    @FXML private TextField campoProgressoAtual;
    @FXML private Label labelDataConclusao; // Mostra a data, mas não é editável.
    @FXML private Button buttonConcluirMeta; // Botão para concluir
    @FXML private Label mensagemErro;

    private Fachada fachada = Fachada.getInstanciaUnica();

    private Meta meta;
    private MetaController metaController;

    //ADD
    private MainController mainController;

    public void setMetaController(MetaController metaController) {
        this.metaController = metaController;
    }

    //ADD
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
                buttonConcluirMeta.setDisable(true); // Já concluída, desabilita o botão
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

            // Atualiza os dados da meta através da fachada.  A data de conclusão NÃO é modificada aqui.
            fachada.configurarMeta(meta, descricao, tipo, valorAlvo, progressoAtual, meta.getDataConclusao());
            mensagemErro.setText("Meta atualizada com sucesso!");

            // Atualiza o usuário logado e a tela principal *após* a modificação:
            if (mainController != null && mainController.getUsuarioLogado() != null) {
                try {
                    Usuario usuarioAtualizado = fachada.buscarUsuarioPorId(mainController.getUsuarioLogado().getId());
                    mainController.setUsuarioLogado(usuarioAtualizado);
                    mainController.atualizarDadosTelaPrincipal(); // <-- Atualiza a tela principal
                } catch (UsuarioNaoEncontradoException e) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Usuário não encontrado.",
                            "O usuário logado não pôde ser encontrado após a atualização da meta.");
                }
            }

            fecharJanela();

        }  catch (NumberFormatException | MetaNaoEncontradaException e) { //Captura apenas o que pode ocorrer.
            mensagemErro.setText("Erro: " + e.getMessage());
            System.out.println("Exceção em atualizarMeta: " + e.getMessage()); // PRINT DA EXCEÇÃO
            e.printStackTrace();

        } catch (Exception e) { //Captura genérica, para outros erros.
            mensagemErro.setText("Erro inesperado: " + e.getMessage());
            System.out.println("Exceção inesperada em atualizarMeta: " + e.getMessage()); // PRINT
            e.printStackTrace();
        }
    }

    @FXML
    public void concluirMeta() {
        try {
            // *ANTES* de chamar a fachada, atualize o progresso:
            meta.setProgressoAtual(meta.getValorAlvo()); //  CORREÇÃO AQUI!
            meta.setDataConclusao(LocalDate.now()); // Define a data de conclusão CORRETO

            fachada.configurarMeta(meta, meta.getDescricao(), meta.getTipo(), meta.getValorAlvo(), meta.getProgressoAtual(), meta.getDataConclusao());// Salva CORRETO
            labelDataConclusao.setText("Concluída em: " + meta.getDataConclusao().toString()); // Atualiza a exibição CORRETO
            buttonConcluirMeta.setDisable(true); // Desabilita o botão CORRETO

            if (metaController != null) {
                metaController.atualizarTabelaMetasUsuario(); // Atualiza a tabela. CORRETO
            }

            //Atualiza o usuário logado *após* a modificação:
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