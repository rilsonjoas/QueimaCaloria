package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.TreinoNaoEncontradoException;
import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;
import com.example.queimacaloria.negocio.Exercicio;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Treino;
import com.example.queimacaloria.negocio.Usuario;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Alert;

public class CriacaoTreinoController {

    @FXML
    private TextField campoNome;
    @FXML
    private ChoiceBox<Exercicio.TipoExercicio> campoTipoTreino;
    @FXML
    private TextField campoDuracao;
    @FXML
    private TextField campoNivelDificuldade;
    @FXML
    private Label mensagemErro;

    @FXML
    private CheckBox checkboxConcluido;

    private Fachada fachada = Fachada.getInstanciaUnica();

    private TreinoController treinoController;
    private MainController mainController;

    public void setTreinoController(TreinoController treinoController) {
        this.treinoController = treinoController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        campoTipoTreino.setItems(FXCollections.observableArrayList(Exercicio.TipoExercicio.values()));
    }

    @FXML
    public void criarTreino() {

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
            boolean concluido = checkboxConcluido.isSelected();

            Treino novoTreino = new Treino();
            if (mainController != null && mainController.getUsuarioLogado() != null) {
                novoTreino.setUsuario(mainController.getUsuarioLogado());
            }

            novoTreino.setTipoDeTreino(tipoTreino);
            novoTreino.setConcluido(concluido);
            fachada.configurarTreino(novoTreino, nome, tipoTreino, duracao, nivelDificuldade, mainController.getUsuarioLogado());



            if (mainController != null && mainController.getUsuarioLogado() != null) {
                try {
                    fachada.adicionarTreinoAoUsuario(mainController.getUsuarioLogado(), novoTreino);

                    Usuario usuarioAtualizado = fachada.buscarUsuarioPorId(mainController.getUsuarioLogado().getId());
                    mainController.setUsuarioLogado(usuarioAtualizado);

                    if (concluido) {
                        mainController.getUsuarioLogado().adicionarPontuacao(nivelDificuldade);
                        showAlert(Alert.AlertType.INFORMATION, "Parabéns!", "Treino Concluído",
                                "Você concluiu o treino e ganhou " + nivelDificuldade + " pontos!");
                    }
                } catch (UsuarioNaoEncontradoException e) {
                    System.err.println("Erro ao buscar usuário: " + e.getMessage());
                }
            }

            if (treinoController != null) {
                treinoController.atualizarTabelaTreinosUsuario();
            }

            if (mainController != null) {
                mainController.atualizarDadosTelaPrincipal();
            }
            mensagemErro.setText("Treino criado com sucesso!");

            // Sugestão de exercícios:
            if (treinoController != null) {
                treinoController.sugerirExercicios(novoTreino);
            }

            fecharJanela();

        } catch (NumberFormatException | TreinoNaoEncontradoException e) {
            mensagemErro.setText("Erro: " + e.getMessage());
            e.printStackTrace();

        }  catch (Exception e) {
            mensagemErro.setText("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Validar se o formulário foi bem preenchido.
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