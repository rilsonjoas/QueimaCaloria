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
            campoTipoTreino.setValue(treino.getTipoDeTreino());
            campoDuracao.setText(String.valueOf(treino.getDuracao()));
            campoNivelDificuldade.setText(String.valueOf(treino.getNivelDeDificuldade()));

            if (checkboxConcluido != null) {
                checkboxConcluido.setSelected(treino.isConcluido());
            }
        }
    }

    @FXML
    public void atualizarTreino() {
        String nome = campoNome.getText();
        Exercicio.TipoExercicio tipoTreino = campoTipoTreino.getValue();
        String duracaoStr = campoDuracao.getText();
        String nivelDificuldadeStr = campoNivelDificuldade.getText();

        if (!validarFormulario(nome, tipoTreino, duracaoStr, nivelDificuldadeStr)) {
            return;
        }
        try {
            int duracao = Integer.parseInt(duracaoStr);
            int nivelDificuldade = Integer.parseInt(nivelDificuldadeStr);

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

            fachada.configurarTreino(treino, nome, tipoTreino, duracao, nivelDificuldade, mainController.getUsuarioLogado());
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


    //Função auxiliar para validar o formulário.
    private boolean validarFormulario(String nome, Exercicio.TipoExercicio tipoTreino, String duracaoStr, String nivelDificuldadeStr) {
        if (nome == null || nome.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O nome não pode estar vazio.");
            return false;
        }

        if (tipoTreino == null) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O tipo de treino não pode ser nulo.");
            return false;
        }

        if (duracaoStr == null || duracaoStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "A duração não pode estar vazia.");
            return false;
        }

        if (!isNumeric(duracaoStr)) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "A duração deve ser um número.");
            return false;
        }

        if (nivelDificuldadeStr == null || nivelDificuldadeStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O nível de dificuldade não pode estar vazio.");
            return false;
        }

        if (!isNumeric(nivelDificuldadeStr)) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O nível de dificuldade deve ser um número.");
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