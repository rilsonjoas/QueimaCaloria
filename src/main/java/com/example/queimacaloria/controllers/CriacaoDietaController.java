package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;
import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;
import com.example.queimacaloria.negocio.Dieta;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Usuario;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CriacaoDietaController {

    @FXML private TextField campoNome;
    @FXML private ChoiceBox<Dieta.ObjetivoDieta> campoObjetivo;
    @FXML private TextField campoCalorias;
    @FXML private Label mensagemErro;

    private Fachada fachada = Fachada.getInstanciaUnica();
    private DietaController dietaController;  // Referência para o DietaController
    private MainController mainController;

    public void setDietaController(DietaController dietaController) {
        this.dietaController = dietaController; // Injeção de dependência
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        campoObjetivo.setItems(FXCollections.observableArrayList(Dieta.ObjetivoDieta.values()));
    }
    @FXML
    public void criarDieta() {
        System.out.println("CriacaoDietaController.criarDieta: Iniciando...");
        try {
            String nome = campoNome.getText();
            Dieta.ObjetivoDieta objetivo = campoObjetivo.getValue();
            int calorias = Integer.parseInt(campoCalorias.getText());

            if (mainController != null && mainController.getUsuarioLogado() != null) {
                Usuario usuarioLogado = mainController.getUsuarioLogado();
                Dieta novaDieta = new Dieta();
                fachada.configurarDieta(novaDieta, nome, objetivo, calorias, usuarioLogado);

                // *****************************************
                // DEFINE A DIETA COMO ATIVA:
                fachada.setDietaAtiva(usuarioLogado, novaDieta);
                // *****************************************

                // **********  MUDANÇA AQUI  **********
                // Chama um método no *DietaController* para notificar sobre a nova dieta:
                if (dietaController != null) {
                    dietaController.atualizarTabelaDietasUsuario(); // Atualiza a tabela
                    dietaController.showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Dieta Criada", "Dieta criada e definida como ativa com sucesso!"); // Mostra a mensagem de sucesso
                }
                // ************************************

                mensagemErro.setText("Dieta criada com sucesso!"); //Não precisa mais.

                // ATUALIZA O USUÁRIO LOGADO APÓS A MODIFICAÇÃO:  (Isso pode ser feito no DietaController, para manter a consistência)
                Usuario usuarioAtualizado = fachada.buscarUsuarioPorId(usuarioLogado.getId());
                mainController.setUsuarioLogado(usuarioAtualizado);
                mainController.atualizarDadosTelaPrincipal();

                fecharJanela();


            } else {
                System.out.println("CriacaoDietaController.criarDieta: Usuário logado é nulo.");
                mensagemErro.setText("Erro: Usuário não logado."); //Isso não deve mais acontecer.
            }
            //Agora captura *todas* as exceções que podem ser lançadas *neste* bloco:
        } catch (NumberFormatException | DietaNaoEncontradaException | UsuarioNaoEncontradoException e) { //Captura as três possíveis
            mensagemErro.setText("Erro: " + e.getMessage());
        } catch (Exception e) { // Captura genérica para outros erros inesperados
            mensagemErro.setText("Erro inesperado: " + e.getMessage());
        }
    }


    @FXML
    private void fecharJanela() {
        Stage stage = (Stage) campoNome.getScene().getWindow();
        stage.close();
    }
}