package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;
import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException; // Import CORRETO!
import com.example.queimacaloria.negocio.Dieta;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Usuario;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CriacaoDietaController {

    @FXML private TextField campoNome;
    @FXML private ChoiceBox<Dieta.ObjetivoDieta> campoObjetivo;
    @FXML private TextField campoCalorias;
    @FXML private Label mensagemErro;

    private Fachada fachada = Fachada.getInstanciaUnica();
    private DietaController dietaController;
    private MainController mainController;

    public void setDietaController(DietaController dietaController) {
        this.dietaController = dietaController;
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
                System.out.println("CriacaoDietaController.criarDieta: Usuário logado: " + usuarioLogado.getEmail());

                Dieta novaDieta = new Dieta();
                fachada.configurarDieta(novaDieta, nome, objetivo, calorias, usuarioLogado);
                mensagemErro.setText("Dieta criada com sucesso!");

                // ATUALIZA O USUÁRIO LOGADO APÓS A MODIFICAÇÃO:
                // (Sem try-catch interno)
                Usuario usuarioAtualizado = fachada.buscarUsuarioPorId(usuarioLogado.getId());
                mainController.setUsuarioLogado(usuarioAtualizado);


                if (dietaController != null) {
                    dietaController.atualizarTabelaDietasUsuario();
                }
                if (mainController != null) {
                    mainController.atualizarDadosTelaPrincipal();
                }
                fecharJanela();

            } else {
                System.out.println("CriacaoDietaController.criarDieta: Usuário logado é nulo.");
                mensagemErro.setText("Erro: Usuário não logado.");
            }
            // Agora captura *todas* as exceções que podem ser lançadas *neste* bloco:
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

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}