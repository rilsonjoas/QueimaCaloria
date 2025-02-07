package com.example.queimacaloria.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import javafx.scene.control.Label;


public class MainController {

    @FXML private StackPane areaConteudo; // Container para as telas
    @FXML private Label labelNomeUsuario;
    @FXML private Label labelIMC;
    @FXML private Button buttonVerMaisMetas;
    @FXML private Button buttonVerMaisExercicios;
    @FXML private Button buttonVerMaisDietas;

    // Variáveis para armazenar as telas carregadas
    private Parent telaLogin;
    private Parent telaRegistro;
    private Parent telaDieta;
    private Parent telaExercicio;
    private Parent telaMeta;
    private Parent telaRefeicao;
    private Parent telaTreino;


    @FXML
    // Inicializa o controller principal
    public void initialize() {
        try {
            // Carrega todas as telas FXML durante a inicialização
            telaLogin = carregarTela("/com/example/queimacaloria/views/login-view.fxml");
            telaRegistro = carregarTela("/com/example/queimacaloria/views/registro-view.fxml");
            telaDieta = carregarTela("/com/example/queimacaloria/views/dieta-view.fxml");
            telaExercicio = carregarTela("/com/example/queimacaloria/views/exercicio-view.fxml");
            telaMeta = carregarTela("/com/example/queimacaloria/views/meta-view.fxml");
            telaRefeicao = carregarTela("/com/example/queimacaloria/views/refeicao-view.fxml");
            telaTreino = carregarTela("/com/example/queimacaloria/views/treino-view.fxml");

            // Injeta o MainController nos sub-controllers
            ((DietaController) getController(telaDieta)).setMainController(this);
            ((ExercicioController) getController(telaExercicio)).setMainController(this);
            ((MetaController) getController(telaMeta)).setMainController(this);
            ((RefeicaoController) getController(telaRefeicao)).setMainController(this);
            ((TreinoController) getController(telaTreino)).setMainController(this);

        } catch (IOException e) {
            e.printStackTrace(); // Tratar erro de carregamento
        }

        mostrarTelaPrincipal(); // Exibe a tela principal ao iniciar
    }

    // Função auxiliar para obter o controller de uma tela
    private Object getController(Parent parent) {
        return ((FXMLLoader) parent.getProperties().get("fxmlLoader")).getController();
    }

    // Função auxiliar para carregar uma tela FXML
    private Parent carregarTela(String caminho) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
        Parent root = loader.load();
        root.getProperties().put("fxmlLoader", loader); // Armazena o loader para uso posterior
        return root;
    }

    @FXML
    // Exibe a tela de login
    public void mostrarTelaLogin() {
        areaConteudo.getChildren().setAll(telaLogin);
    }

    @FXML
    // Exibe a tela de registro
    public void mostrarTelaRegistro() {
        areaConteudo.getChildren().setAll(telaRegistro);
    }

    @FXML
    // Exibe a tela de dieta
    public void mostrarTelaDieta() {
        areaConteudo.getChildren().setAll(telaDieta);
    }

    @FXML
    // Exibe a tela de exercício
    public void mostrarTelaExercicio() {
        areaConteudo.getChildren().setAll(telaExercicio);
    }

    @FXML
    // Exibe a tela de meta
    public void mostrarTelaMeta() {
        areaConteudo.getChildren().setAll(telaMeta);
    }

    @FXML
    // Exibe a tela de refeição
    public void mostrarTelaRefeicao() {
        areaConteudo.getChildren().setAll(telaRefeicao);
    }

    @FXML
    // Exibe a tela de treino
    public void mostrarTelaTreino() {
        areaConteudo.getChildren().setAll(telaTreino);
    }


    @FXML
    // Exibe a tela principal
    public void mostrarTelaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/main-screen-content.fxml"));
            Parent telaPrincipalContent = loader.load();
            areaConteudo.getChildren().setAll(telaPrincipalContent);


            labelNomeUsuario = (Label) telaPrincipalContent.lookup("#labelNomeUsuario");
            labelIMC = (Label) telaPrincipalContent.lookup("#labelIMC");

            buttonVerMaisMetas = (Button) telaPrincipalContent.lookup("#buttonVerMaisMetas");
            buttonVerMaisExercicios = (Button) telaPrincipalContent.lookup("#buttonVerMaisExercicios");
            buttonVerMaisDietas = (Button) telaPrincipalContent.lookup("#buttonVerMaisDietas");


            // Define os eventos dos botões
            if (buttonVerMaisMetas != null) {
                buttonVerMaisMetas.setOnAction(e -> mostrarTelaMeta());
            }
            if (buttonVerMaisExercicios != null) {
                buttonVerMaisExercicios.setOnAction(e -> mostrarTelaExercicio());
            }
            if (buttonVerMaisDietas != null) {
                buttonVerMaisDietas.setOnAction(e -> mostrarTelaDieta());
            }

            // Define valores para os labels (substitua pelos valores reais)
            if (labelNomeUsuario != null) {
                labelNomeUsuario.setText("Nome do Usuário");
            }
            if (labelIMC != null) {
                labelIMC.setText("IMC: ");
            }

        } catch (IOException e) {
            e.printStackTrace(); // Tratar erro de carregamento
        }
    }

    @FXML
    // Faz logout da aplicação
    public void logout() {
        mostrarTelaLogin();
    }
}

