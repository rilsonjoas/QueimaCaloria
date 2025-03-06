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
    @FXML private Label labelDataConclusao;
    @FXML private Button buttonConcluirMeta;
    @FXML private Label mensagemErro;

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
        String descricao = campoDescricao.getText();
        Meta.Tipo tipo = campoTipo.getValue();
        String valorAlvoStr = campoValorAlvo.getText();
        String progressoAtualStr = campoProgressoAtual.getText();

        if (!validarFormulario(descricao, tipo, valorAlvoStr, progressoAtualStr)) {
            return;
        }

        try {
            double valorAlvo = Double.parseDouble(valorAlvoStr);
            double progressoAtual = Double.parseDouble(progressoAtualStr);

            System.out.println("EdicaoMetaController.atualizarMeta() - Antes da fachada: Meta ID: " + meta.getId() + ", Progresso: " + meta.getProgressoAtual() + ", Usuario: " + (meta.getUsuario() != null ? meta.getUsuario().getEmail() : "null"));
            fachada.atualizarMeta(meta.getId(), descricao, tipo, valorAlvo, progressoAtual, meta.getDataConclusao());

            if (mainController != null && mainController.getUsuarioLogado() != null) {
                try {
                    Usuario usuarioAtualizado = fachada.buscarUsuarioPorId(mainController.getUsuarioLogado().getId());
                    mainController.setUsuarioLogado(usuarioAtualizado);
                }catch (UsuarioNaoEncontradoException e){
                    showAlert(Alert.AlertType.ERROR, "Erro", "Usuário não encontrado.",
                            "O usuário logado não pôde ser encontrado.");
                }
            }

            System.out.println("EdicaoMetaController.atualizarMeta() - Após a fachada: Meta ID: " + meta.getId()  + ", Progresso: " + meta.getProgressoAtual());  //DEBUG

            mensagemErro.setText("Meta atualizada com sucesso!");

            if (mainController != null) {
                mainController.atualizarDadosTelaPrincipal(); // Mantém apenas UMA chamada.
                System.out.println("MainController.atualizarDadosTelaPrincipal chamado de EdicaoMetaController.atualizarMeta()");
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


    //Verifica se o formulário foi validado
    private boolean validarFormulario(String descricao, Meta.Tipo tipo, String valorAlvoStr, String progressoAtualStr) {
        if (descricao == null || descricao.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "A descrição não pode estar vazia.");
            return false;
        }

        if (tipo == null) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O tipo não pode ser nulo.");
            return false;
        }

        if (valorAlvoStr == null || valorAlvoStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O valor alvo não pode estar vazio.");
            return false;
        }

        if (!isNumeric(valorAlvoStr)) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O valor alvo deve ser um número.");
            return false;
        }

        if (progressoAtualStr == null || progressoAtualStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O progresso atual não pode estar vazio.");
            return false;
        }

        if (!isNumeric(progressoAtualStr)) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O progresso atual deve ser um número.");
            return false;
        }

        return true;
    }

    //Função auxiliar para ver se é um número
    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
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
            System.out.println("Meta CONCLUÍDA: Progresso Atual = " + meta.getProgressoAtual()); // Debug

            labelDataConclusao.setText("Concluída em: " + meta.getDataConclusao().toString());
            buttonConcluirMeta.setDisable(true);
            campoProgressoAtual.setText(String.valueOf(meta.getProgressoAtual()));

            // Atualiza o usuário no MainController *após* a atualização
            if (mainController != null) {
                mainController.atualizarDadosTelaPrincipal(); // Atualiza a tela *principal*
                System.out.println("MainController.atualizarDadosTelaPrincipal chamado de EdicaoMetaController.concluirMeta()");
            }

            if (metaController != null) {
                metaController.atualizarTabelaMetasUsuario(); // Atualiza a tabela *na tela de metas*
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