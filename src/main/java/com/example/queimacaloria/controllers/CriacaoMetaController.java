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

public class CriacaoMetaController {

    @FXML private TextField campoDescricao;
    @FXML private ChoiceBox<Meta.Tipo> campoTipo;
    @FXML private TextField campoValorAlvo;
    @FXML private TextField campoProgressoAtual;
    @FXML private DatePicker campoDataConclusao;
    @FXML private Label mensagemErro;
    private Fachada fachada = Fachada.getInstanciaUnica();
    private MetaController metaController;
    private MainController mainController;

    public void setMetaController(MetaController metaController) {
        this.metaController = metaController;
    }
    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        campoTipo.setItems(FXCollections.observableArrayList(Meta.Tipo.values()));
    }

    @FXML
    public void criarMeta() {
        try {
            String descricao = campoDescricao.getText();
            Meta.Tipo tipo = campoTipo.getValue();
            double valorAlvo = Double.parseDouble(campoValorAlvo.getText());
            // O progresso inicial de uma nova meta DEVE ser 0.0. Não use o valor do campo.
            double progressoAtual = 0.0; // Valor inicial correto.
            LocalDate dataConclusao = campoDataConclusao.getValue();

            //Verifica se o usuário está logado
            if(mainController != null && mainController.getUsuarioLogado() != null){
                Meta novaMeta = new Meta();
                fachada.configurarMeta(novaMeta, descricao, tipo, valorAlvo, progressoAtual, dataConclusao);

                mensagemErro.setText("Meta criada com sucesso!");

                // Atualiza o usuário logado *após* a modificação:
                try {
                    Usuario usuarioAtualizado = fachada.buscarUsuarioPorId(mainController.getUsuarioLogado().getId());
                    mainController.setUsuarioLogado(usuarioAtualizado);
                } catch (UsuarioNaoEncontradoException e) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Usuário não encontrado.",
                            "O usuário logado não pôde ser encontrado.");
                }


                // Atualiza a tabela de metas no MetaController
                if (metaController != null) {
                    metaController.initialize();
                }
                if(mainController != null){
                    mainController.atualizarDadosTelaPrincipal();
                }
            } else {
                mensagemErro.setText("Erro, usuário não logado");
            }



            fecharJanela(); // Fecha a janela atual

        } catch (NumberFormatException e) {
            mensagemErro.setText("Erro: Valores numéricos inválidos.");
        } catch (MetaNaoEncontradaException e) {
            mensagemErro.setText("Erro ao criar meta: " + e.getMessage());
        } catch (Exception e) {
            mensagemErro.setText("Erro inesperado: " + e.getMessage());
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