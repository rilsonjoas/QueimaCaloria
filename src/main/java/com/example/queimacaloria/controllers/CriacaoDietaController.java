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
    @FXML private ChoiceBox<Meta.Tipo> campoObjetivo;
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
        String nome = campoNome.getText();
        Meta.Tipo objetivo = campoObjetivo.getValue();
        String caloriasStr = campoCalorias.getText();

        if (!validarFormulario(nome, objetivo, caloriasStr)) {
            return; // Aborta se a validação falhar
        }

        try {
            int calorias = Integer.parseInt(caloriasStr);

            if (mainController != null && mainController.getUsuarioLogado() != null) {
                Usuario usuarioLogado = mainController.getUsuarioLogado();
                Dieta novaDieta = new Dieta();
                novaDieta.setUsuario(usuarioLogado);

                fachada.configurarDieta(novaDieta, nome, objetivo, calorias, usuarioLogado);
                fachada.setDietaAtiva(usuarioLogado, novaDieta);

                usuarioLogado.getDietas().add(novaDieta);

                if (dietaController != null) {
                    dietaController.atualizarTabelaDietasUsuario();
                    dietaController.showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Dieta Criada", "Dieta criada com sucesso!");
                }

                // Recomendação de refeição (mantém como estava)
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
                mensagemErro.setText("Erro: Usuário não logado.");
            }
        } catch (NumberFormatException e) {
            mensagemErro.setText("Erro: Calorias devem ser um número inteiro válido.");
        } catch (DietaNaoEncontradaException | UsuarioNaoEncontradoException e) { // Captura ambas as exceções
            mensagemErro.setText("Erro: " + e.getMessage());
        } catch (Exception e) { // Captura genérica para outros erros
            mensagemErro.setText("Erro inesperado: " + e.getMessage());
            e.printStackTrace(); // Sempre importante para debugging
        }
    }

    //Função auxiliar para validar o formulário.
    private boolean validarFormulario(String nome, Meta.Tipo objetivo, String caloriasStr) {
        if (nome == null || nome.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O nome não pode estar vazio.");
            return false;
        }

        if (objetivo == null) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O objetivo não pode ser nulo.");
            return false;
        }

        if (caloriasStr == null || caloriasStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "As calorias não podem estar vazias.");
            return false;
        }

        if (!isNumeric(caloriasStr)) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "As calorias devem ser um número.");
            return false;
        }

        return true;
    }

    //Função auxiliar para verificar se é um número.
    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Fecha a janela atual.
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