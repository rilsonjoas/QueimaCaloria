package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;
import com.example.queimacaloria.negocio.Dieta;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Usuario;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

// Controller para a tela de criação de dieta
public class CriacaoDietaController {

    @FXML
    private TextField campoNome;
    @FXML
    private ChoiceBox<Dieta.ObjetivoDieta> campoObjetivo;
    @FXML
    private TextField campoCalorias;
    @FXML
    private Label mensagemErro;

    private Fachada fachada = Fachada.getInstanciaUnica();
    private DietaController dietaController;

    //ADD
    private MainController mainController;


    // Permite que o DietaController seja acessado por esta classe
    public void setDietaController(DietaController dietaController) {
        this.dietaController = dietaController;
    }

    //ADD
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    // Inicializa o ChoiceBox com os objetivos de dieta
    public void initialize() {
        campoObjetivo.setItems(FXCollections.observableArrayList(Dieta.ObjetivoDieta.values()));
    }

    @FXML
    // Método chamado ao clicar no botão "Criar Dieta"
    public void criarDieta() {
        try {
            String nome = campoNome.getText();
            Dieta.ObjetivoDieta objetivo = campoObjetivo.getValue();
            int calorias = Integer.parseInt(campoCalorias.getText());

            // Aqui abaixo estamos testando com um usuário de exemplo
            Usuario usuarioExemplo = new Usuario();
            try {
                fachada.atualizarDadosUsuario(usuarioExemplo, "Exemplo", "exemplo@email.com", "senha",
                        null, Usuario.Sexo.MASCULINO, 70, (float) 1.75);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            Dieta novaDieta = new Dieta();
            fachada.configurarDieta(novaDieta, nome, objetivo, calorias, usuarioExemplo);
            mensagemErro.setText("Dieta criada com sucesso!");


// Atualiza a tabela de dietas no DietaController
            if (dietaController != null) {
                dietaController.initialize();
            }

            //ADD
            if(mainController != null){
                mainController.atualizarDadosTelaPrincipal();
            }

            fecharJanela(); // Fecha a janela atual

        } catch (NumberFormatException e) {
            mensagemErro.setText("Erro: Calorias devem ser um número inteiro.");
        } catch (DietaNaoEncontradaException e) {
            mensagemErro.setText("Erro ao criar dieta: " + e.getMessage());
        } catch (Exception e) {
            mensagemErro.setText("Erro inesperado: " + e.getMessage());
        }
    }

    @FXML
    // Fecha a janela atual
    private void fecharJanela() {
        Stage stage = (Stage) campoNome.getScene().getWindow();
        stage.close();
    }
}

