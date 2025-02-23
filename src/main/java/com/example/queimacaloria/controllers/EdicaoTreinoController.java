package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.TreinoNaoEncontradoException;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Treino;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
//Adicione esse import
import javafx.scene.control.CheckBox;

public class EdicaoTreinoController {

    @FXML private TextField campoNome;
    @FXML private TextField campoTipoTreino;
    @FXML private TextField campoDuracao;
    @FXML private TextField campoNivelDificuldade;
    @FXML private Label mensagemErro;

    //Adicione este campo:
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
        System.out.println("EdicaoTreinoController.setTreino() chamado. ID do treino: " + treino.getId()); // LOG
        this.treino = treino;
        preencherCampos();
    }




    private void preencherCampos() {
        if (treino != null) {
            campoNome.setText(treino.getNome());
            campoTipoTreino.setText(treino.getTipoDeTreino());
            campoDuracao.setText(String.valueOf(treino.getDuracao()));
            campoNivelDificuldade.setText(String.valueOf(treino.getNivelDeDificuldade()));

            //  Preenche o CheckBox:
            if (checkboxConcluido != null) {
                checkboxConcluido.setSelected(treino.isConcluido());
            }
        }
    }

    @FXML
    public void atualizarTreino() {
        System.out.println("EdicaoTreinoController.atualizarTreino() chamado"); // LOG

        try {
            // Obtém os valores dos campos.
            String nome = campoNome.getText();
            String tipoTreino = campoTipoTreino.getText();
            int duracao = Integer.parseInt(campoDuracao.getText());
            int nivelDificuldade = Integer.parseInt(campoNivelDificuldade.getText());

            //  1.  Lógica de Conclusão *ANTES* de chamar a fachada:
            if (checkboxConcluido != null) {
                System.out.println("  CheckBox existe.  Estado atual: " + treino.isConcluido());
                System.out.println("  Checkbox selecionado? " + checkboxConcluido.isSelected());

                boolean estadoAnterior = treino.isConcluido();
                treino.setConcluido(checkboxConcluido.isSelected()); //  AGORA SIM!

                System.out.println("  Novo estado do treino: " + treino.isConcluido());


                //   Adiciona a pontuação *SE* o estado mudou *E* agora está concluído
                if (treino.isConcluido() && estadoAnterior != treino.isConcluido() && mainController != null && mainController.getUsuarioLogado() != null) {
                    mainController.getUsuarioLogado().adicionarPontuacao(treino.getNivelDeDificuldade());
                    System.out.println("  Pontuação adicionada!");
                } else {
                    System.out.println("  Pontuação NÃO adicionada. Condições não satisfeitas.");
                }

            }

            // 2. *AGORA* chama a fachada, DEPOIS de modificar o treino
            fachada.configurarTreino(treino, nome, tipoTreino, duracao, nivelDificuldade);
            //Como a linha acima, atualiza o treino, então a linha abaixo se torna redundante
            //fachada.atualizarProgressoTreino(treino);


            mensagemErro.setText("Treino atualizado com sucesso!");

            //Notifica o main controller para atualizar
            if (treinoController != null) {
                treinoController.atualizarTabelaTreinosUsuario(); //  Chama initialize
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
        } catch (Exception e) { // Captura genérica, importante para outros erros
            mensagemErro.setText("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void fecharJanela() {
        Stage stage = (Stage) campoNome.getScene().getWindow();
        stage.close();
    }
}