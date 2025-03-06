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
        // Carrega os valores do enum no ChoiceBox
        campoTipoTreino.setItems(FXCollections.observableArrayList(Exercicio.TipoExercicio.values()));
    }

    @FXML
    public void criarTreino() {

        String nome = campoNome.getText();
        Exercicio.TipoExercicio tipoTreino = campoTipoTreino.getValue();
        String duracaoStr = campoDuracao.getText();
        String nivelDificuldadeStr = campoNivelDificuldade.getText();

        if (!validarFormulario(nome, tipoTreino, duracaoStr, nivelDificuldadeStr)) {
            return; // Aborta se a validação falhar
        }

        try {
            int duracao = Integer.parseInt(duracaoStr);
            int nivelDificuldade = Integer.parseInt(nivelDificuldadeStr);
            boolean concluido = checkboxConcluido.isSelected();

            Treino novoTreino = new Treino();
            // Define o usuário no novo treino, se houver um usuário logado.
            if(mainController != null && mainController.getUsuarioLogado() != null){
                novoTreino.setUsuario(mainController.getUsuarioLogado());
            }
            assert mainController != null;
            fachada.configurarTreino(novoTreino, nome, tipoTreino, duracao, novoTreino.getNivelDeDificuldade(), mainController.getUsuarioLogado()); // Passa o usuário
            novoTreino.setTipoDeTreino(tipoTreino);
            novoTreino.setConcluido(concluido);

            if (mainController != null && mainController.getUsuarioLogado() != null) {
                try {
                    Usuario usuarioAtualizado = fachada.buscarUsuarioPorId(mainController.getUsuarioLogado().getId());
                    fachada.adicionarTreinoAoUsuario(usuarioAtualizado, novoTreino);
                    mainController.setUsuarioLogado(usuarioAtualizado);

                    if (concluido) {
                        mainController.getUsuarioLogado().adicionarPontuacao(novoTreino.getNivelDeDificuldade()); // Correto!
                        showAlert(Alert.AlertType.INFORMATION, "Parabéns!", "Treino Concluído",
                                "Você concluiu o treino e ganhou " + novoTreino.getNivelDeDificuldade() + " pontos!"); // Correto!
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


        } catch (NumberFormatException | TreinoNaoEncontradoException e) { // Captura a exceção correta
            mensagemErro.setText("Erro: " + e.getMessage());
        } catch (Exception e) { // Captura genérica para outros erros
            mensagemErro.setText("Erro inesperado: " + e.getMessage());
            e.printStackTrace(); // Sempre importante para debugging!
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