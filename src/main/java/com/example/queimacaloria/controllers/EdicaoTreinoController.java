//Mesmas correções do AdminTreinosController, mas agora, sempre passamos o usuário logado.
package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.TreinoNaoEncontradoException;
import com.example.queimacaloria.negocio.Exercicio;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Treino;
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
    private TreinoController treinoController; //Para comunicação
    private MainController mainController;  //Para comunicação e acesso ao usuário.

    public void setTreinoController(TreinoController treinoController) {
        this.treinoController = treinoController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }


    public void setTreino(Treino treino) {
        this.treino = treino;
        preencherCampos();
    }

    @FXML
    public void initialize() {
        // Carrega os valores do enum no ChoiceBox
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
                boolean estadoAnterior = treino.isConcluido(); // Guarda o estado anterior
                treino.setConcluido(checkboxConcluido.isSelected());  // Atualiza o estado do treino

                // Verifica se o treino foi marcado como concluído *e* se não estava concluído antes
                if (treino.isConcluido() && estadoAnterior != treino.isConcluido() && mainController != null && mainController.getUsuarioLogado() != null) {
                    mainController.getUsuarioLogado().adicionarPontuacao(treino.getNivelDeDificuldade());
                    showAlert(Alert.AlertType.INFORMATION, "Parabéns!", "Treino Concluído",
                            "Você concluiu o treino \"" + treino.getNome() + "\" e ganhou " +
                                    treino.getNivelDeDificuldade() + " pontos!");
                }
            }

            // Agora sempre passa o usuário logado e o nível de experiência
            fachada.configurarTreino(treino, nome, tipoTreino, duracao, nivelDificuldade, mainController.getUsuarioLogado(), mainController.getUsuarioLogado().getNivelExperiencia());
            mensagemErro.setText("Treino atualizado com sucesso!");

            //Removido
            /*if (treinoController != null) {
                treinoController.atualizarTabelaTreinosUsuario();
            }
            if(mainController != null) {
                mainController.atualizarDadosTelaPrincipal(); //Isso não é mais necessário, o listener no main controller já faz isso.
            }*/


            fecharJanela();

        } catch (NumberFormatException e) {
            mensagemErro.setText("Erro: Duração e dificuldade devem ser números inteiros.");
        } catch (TreinoNaoEncontradoException e) {
            mensagemErro.setText("Erro ao atualizar treino: " + e.getMessage());
        } catch (Exception e) { // Captura genérica
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

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}