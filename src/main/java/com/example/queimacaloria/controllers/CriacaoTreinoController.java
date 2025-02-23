package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.TreinoNaoEncontradoException;
import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Treino;
import com.example.queimacaloria.negocio.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
//Adicione esse import
import javafx.scene.control.CheckBox;
//Novo import
import javafx.scene.control.Alert;

public class CriacaoTreinoController {

    @FXML
    private TextField campoNome;
    @FXML
    private TextField campoTipoTreino;
    @FXML
    private TextField campoDuracao;
    @FXML
    private TextField campoNivelDificuldade;
    @FXML
    private Label mensagemErro;

    //Adicione este campo:
    @FXML
    private CheckBox checkboxConcluido;


    private Fachada fachada = Fachada.getInstanciaUnica();

    private TreinoController treinoController;

    private MainController mainController; //ADD

    // Injeta o TreinoController para que a tabela seja atualizada após criar um novo treino.
    public void setTreinoController(TreinoController treinoController) {
        this.treinoController = treinoController;
    }

    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }


    @FXML
    public void criarTreino() {
        try {
            String nome = campoNome.getText();
            String tipoTreino = campoTipoTreino.getText();
            int duracao = Integer.parseInt(campoDuracao.getText());
            int nivelDificuldade = Integer.parseInt(campoNivelDificuldade.getText());
            boolean concluido = checkboxConcluido.isSelected();

            Treino novoTreino = new Treino();
            fachada.configurarTreino(novoTreino, nome, tipoTreino, duracao, nivelDificuldade);
            novoTreino.setConcluido(concluido);


            // ATUALIZA O USUÁRIO LOGADO *APÓS* TODAS AS MODIFICAÇÕES:
            if (mainController != null && mainController.getUsuarioLogado() != null) {
                try {
                    Usuario usuarioAtualizado = fachada.buscarUsuarioPorId(mainController.getUsuarioLogado().getId());
                    mainController.setUsuarioLogado(usuarioAtualizado); // Atualiza o usuário *no MainController*.

                    // Adiciona a pontuação *se* o treino for criado como concluído.
                    if (concluido) {
                        mainController.getUsuarioLogado().adicionarPontuacao(nivelDificuldade);
                        //Mostra mensagem de sucesso
                        showAlert(Alert.AlertType.INFORMATION,"Parabéns!", "Treino Concluído", "Você concluiu o treino e ganhou " + nivelDificuldade + " pontos!");
                    }

                } catch (UsuarioNaoEncontradoException e) {
                    System.err.println("Erro ao buscar usuário: " + e.getMessage()); //melhor tratamento
                }
            }
            // *******************************************************


            if (treinoController != null) {
                treinoController.atualizarTabelaTreinosUsuario(); // Já estava correto.
            }

            if(mainController != null){
                mainController.atualizarDadosTelaPrincipal(); // Já estava correto.
            }
            mensagemErro.setText("Treino criado com sucesso!"); //Mensagem de sucesso
            fecharJanela();

        } catch (NumberFormatException | TreinoNaoEncontradoException  e) {
            mensagemErro.setText("Erro: " + e.getMessage());
        } catch (Exception e) { // Captura genérica *depois* das específicas.
            mensagemErro.setText("Erro inesperado: " + e.getMessage());
            e.printStackTrace();  // Muito importante para debugging!
        }
    }
    @FXML
    private void fecharJanela() {
        Stage stage = (Stage) campoNome.getScene().getWindow();
        stage.close();
    }

    //Método showAlert para usar o Alert
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}