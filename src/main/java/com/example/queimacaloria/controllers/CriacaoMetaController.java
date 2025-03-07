// CriacaoMetaController.java

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
import com.example.queimacaloria.negocio.Dieta;

import java.time.LocalDate;

public class CriacaoMetaController {

    @FXML private TextField campoDescricao;
    @FXML private ChoiceBox<Meta.Tipo> campoTipo;
    @FXML private TextField campoValorAlvo;
    @FXML private TextField campoProgressoAtual;
    @FXML private Label mensagemErro;
    @FXML private Button buttonConcluirMeta;
    @FXML private Label labelDataConclusao;


    private Fachada fachada = Fachada.getInstanciaUnica();
    private MetaController metaController;
    private MainController mainController;

    @FXML
    public void initialize() {
        campoTipo.setItems(FXCollections.observableArrayList(Meta.Tipo.values()));
        campoProgressoAtual.setEditable(true);
        campoProgressoAtual.setText("0.0");  // Valor inicial
        labelDataConclusao.setText(""); //Começa vazio.

    }
    public void setMetaController(MetaController metaController) {
        this.metaController = metaController;
    }

    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }

    @FXML
    public void criarMeta() {
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
            if (progressoAtual < 0) {
                showAlert(Alert.AlertType.WARNING, "Aviso", "Progresso Inválido!", "O progresso não pode ser negativo.");
                return;
            }

            LocalDate dataConclusao = null;
            if (buttonConcluirMeta.isDisabled()) {
                dataConclusao = LocalDate.now();
            }

            if (mainController != null && mainController.getUsuarioLogado() != null) {
                Meta novaMeta = new Meta();
                novaMeta.setUsuario(mainController.getUsuarioLogado()); // Define o usuário
                fachada.configurarMeta(novaMeta, descricao, tipo, valorAlvo, progressoAtual, dataConclusao, mainController.getUsuarioLogado()); // Passa o usuário

                // Adiciona a meta à lista de metas do usuário
                mainController.getUsuarioLogado().getMetas().add(novaMeta);

                mensagemErro.setText("Meta criada com sucesso!");

                // Sugestão de dieta (mantém como estava)
                Dieta dietaRecomendada = fachada.getDietaAleatoria(tipo);
                if (dietaRecomendada != null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Recomendação de Dieta");
                    alert.setHeaderText("Com base na sua nova meta, recomendamos a seguinte dieta:");
                    alert.setContentText(String.format("Nome: %s\nObjetivo: %s\nCalorias: %d",
                            dietaRecomendada.getNome(),
                            dietaRecomendada.getObjetivo().getDescricao(),
                            dietaRecomendada.getCaloriasDiarias()));
                    alert.showAndWait();
                }


            } else {
                mensagemErro.setText("Erro, usuário não logado");
            }

            fecharJanela();

        } catch (NumberFormatException e) {
            mensagemErro.setText("Erro: Valores numéricos inválidos.");
        } catch (MetaNaoEncontradaException e) { // Captura a exceção correta
            mensagemErro.setText("Erro ao criar meta: " + e.getMessage());
        }
        catch (Exception e) {
            mensagemErro.setText("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validarFormulario(String descricao, Meta.Tipo tipo, String valorAlvoStr, String progressoAtualStr) {
        // validação (manter como está)
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
        //Essa função não faz mais sentido, uma vez que a logica foi movida para o atualizarMeta
        //Para evitar problemas, ela foi mantida, mas agora apenas exibe um aviso.
        showAlert(Alert.AlertType.WARNING, "Aviso", "Concluir Meta", "Para marcar a meta como concluída, por favor, edite a meta e coloque o valor do progresso atual como o valor alvo.");
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