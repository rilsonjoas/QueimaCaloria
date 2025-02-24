package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.TreinoNaoEncontradoException;
import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;
import com.example.queimacaloria.negocio.Exercicio;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Treino;
import com.example.queimacaloria.negocio.Usuario;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.CheckBox;

public class EdicaoTreinoController {

    @FXML private TextField campoNome;
    @FXML private ChoiceBox<Exercicio.TipoExercicio> campoTipoTreino;
    @FXML private TextField campoDuracao;
    @FXML private TextField campoNivelDificuldade;
    @FXML private Label mensagemErro;

    @FXML
    private CheckBox checkboxConcluido;

    private Fachada fachada = Fachada.getInstanciaUnica();
    private Treino treino;
    private TreinoController treinoController;
    private MainController mainController;

    public void setTreinoController(TreinoController treinoController) {
        this.treinoController = treinoController;
    }

    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }

    public void setTreino(Treino treino) {
        System.out.println("EdicaoTreinoController.setTreino() chamado. ID do treino: " + treino.getId());
        this.treino = treino;
        preencherCampos();
    }

    @FXML
    public void initialize() {
        // Carrega os valores do enum no ChoiceBox.
        campoTipoTreino.setItems(FXCollections.observableArrayList(Exercicio.TipoExercicio.values()));
    }

    private void preencherCampos() {
        if (treino != null) {
            campoNome.setText(treino.getNome());
            campoTipoTreino.setValue(treino.getTipoDeTreino()); // Define o valor do ChoiceBox
            campoDuracao.setText(String.valueOf(treino.getDuracao()));
            campoNivelDificuldade.setText(String.valueOf(treino.getNivelDeDificuldade()));

            if (checkboxConcluido != null) {
                checkboxConcluido.setSelected(treino.isConcluido());
            }
        }
    }

    @FXML
    public void atualizarTreino() {
        System.out.println("EdicaoTreinoController.atualizarTreino() chamado");

        try {
            String nome = campoNome.getText();
            Exercicio.TipoExercicio tipoTreino = campoTipoTreino.getValue();
            int duracao = Integer.parseInt(campoDuracao.getText());
            int nivelDificuldade = Integer.parseInt(campoNivelDificuldade.getText());

            if (checkboxConcluido != null) {
                System.out.println("  CheckBox existe.  Estado atual: " + treino.isConcluido());
                System.out.println("  Checkbox selecionado? " + checkboxConcluido.isSelected());

                boolean estadoAnterior = treino.isConcluido();
                treino.setConcluido(checkboxConcluido.isSelected());

                System.out.println("  Novo estado do treino: " + treino.isConcluido());

                if (treino.isConcluido() && estadoAnterior != treino.isConcluido() && mainController != null && mainController.getUsuarioLogado() != null) {
                    mainController.getUsuarioLogado().adicionarPontuacao(treino.getNivelDeDificuldade());
                    System.out.println("  Pontuação adicionada!");

                    showAlert(Alert.AlertType.INFORMATION, "Parabéns!", "Treino Concluído",
                            "Você concluiu o treino \"" + treino.getNome() + "\" e ganhou " +
                                    treino.getNivelDeDificuldade() + " pontos!");
                } else {
                    System.out.println("  Pontuação NÃO adicionada. Condições não satisfeitas.");
                }
            }

            fachada.configurarTreino(treino, nome, tipoTreino, duracao, nivelDificuldade);
            treino.setTipoDeTreino(tipoTreino);


            mensagemErro.setText("Treino atualizado com sucesso!");

            if (treinoController != null) {
                treinoController.atualizarTabelaTreinosUsuario();
            }
            if(mainController != null){
                mainController.atualizarDadosTelaPrincipal();
            }
            fecharJanela();

        } catch (NumberFormatException e) {
            mensagemErro.setText("Erro: Duração e dificuldade devem ser números inteiros.");
        }
        catch (TreinoNaoEncontradoException e) {
            mensagemErro.setText("Erro: " + e.getMessage());
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
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}