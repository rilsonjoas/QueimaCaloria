package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;
import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;
import com.example.queimacaloria.negocio.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CriacaoDietaController {

    @FXML private TextField campoNome;
    @FXML private ChoiceBox<Meta.Tipo> campoObjetivo; // Usar Meta.Tipo
    @FXML private TextField campoCalorias;
    @FXML private Label mensagemErro;

    private Fachada fachada = Fachada.getInstanciaUnica();
    private DietaController dietaController;
    private MainController mainController;

    // Define o controlador da tela de dieta.
    public void setDietaController(DietaController dietaController) {
        this.dietaController = dietaController;
    }

    // Define o controlador principal.
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    // Inicializa o controlador, configurando o ChoiceBox de objetivo.
    @FXML
    public void initialize() {
        campoObjetivo.setItems(FXCollections.observableArrayList(Meta.Tipo.values()));
    }

    // Cria uma nova dieta para o usuário logado.
    @FXML
    public void criarDieta() {
        System.out.println("CriacaoDietaController.criarDieta: Iniciando...");
        try {
            String nome = campoNome.getText();
            Meta.Tipo objetivo = campoObjetivo.getValue(); // Usar Meta.Tipo
            int calorias = Integer.parseInt(campoCalorias.getText());

            if (mainController != null && mainController.getUsuarioLogado() != null) {
                Usuario usuarioLogado = mainController.getUsuarioLogado();
                Dieta novaDieta = new Dieta();
                novaDieta.setUsuario(usuarioLogado);
                fachada.configurarDieta(novaDieta, nome, objetivo, calorias, usuarioLogado);

                fachada.setDietaAtiva(usuarioLogado, novaDieta);


                if (dietaController != null) {
                    dietaController.atualizarTabelaDietasUsuario();
                    dietaController.showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Dieta Criada", "Dieta criada com sucesso!");
                }

                // Recomendação de refeição
                Refeicao refeicaoRecomendada = fachada.getRefeicaoAleatoria();
                if (refeicaoRecomendada != null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Recomendação de Refeição");
                    alert.setHeaderText("Com base na sua nova dieta, recomendamos a seguinte refeição:");
                    alert.setContentText(String.format("Nome: %s\nDescrição: %s\nCalorias: %d",
                            refeicaoRecomendada.getNome(),
                            refeicaoRecomendada.getDescricao(),
                            refeicaoRecomendada.getCalorias()));
                    alert.showAndWait();
                }

                fecharJanela();

            } else {
                System.out.println("CriacaoDietaController.criarDieta: Usuário logado é nulo.");
                mensagemErro.setText("Erro: Usuário não logado.");
            }
        } catch (NumberFormatException | DietaNaoEncontradaException | UsuarioNaoEncontradoException e) {
            mensagemErro.setText("Erro: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            mensagemErro.setText("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // Fecha a janela atual.
    @FXML
    private void fecharJanela() {
        Stage stage = (Stage) campoNome.getScene().getWindow();
        stage.close();
    }
}