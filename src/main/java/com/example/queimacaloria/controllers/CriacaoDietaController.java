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
        campoObjetivo.setItems(FXCollections.observableArrayList(Dieta.ObjetivoDieta.values()));
    }

    // Cria uma nova dieta para o usuário logado.
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

                fachada.setDietaAtiva(usuarioLogado, novaDieta);

                if (dietaController != null) {
                    dietaController.atualizarTabelaDietasUsuario();
                    dietaController.showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Dieta Criada", "Dieta criada e definida como ativa com sucesso!");
                }

                Usuario usuarioAtualizado = fachada.buscarUsuarioPorId(usuarioLogado.getId());
                mainController.setUsuarioLogado(usuarioAtualizado);
                mainController.atualizarDadosTelaPrincipal();

                fecharJanela();

            } else {
                System.out.println("CriacaoDietaController.criarDieta: Usuário logado é nulo.");
                mensagemErro.setText("Erro: Usuário não logado.");
            }
        } catch (NumberFormatException | DietaNaoEncontradaException | UsuarioNaoEncontradoException e) {
            mensagemErro.setText("Erro: " + e.getMessage());
        } catch (Exception e) {
            mensagemErro.setText("Erro inesperado: " + e.getMessage());
        }
    }

    // Fecha a janela atual.
    @FXML
    private void fecharJanela() {
        Stage stage = (Stage) campoNome.getScene().getWindow();
        stage.close();
    }
}