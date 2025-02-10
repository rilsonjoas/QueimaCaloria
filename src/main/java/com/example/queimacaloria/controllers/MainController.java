package com.example.queimacaloria.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import javafx.scene.control.Label;
import javafx.stage.Stage; // Importante!

public class MainController {

    @FXML private StackPane areaConteudo; // Container para as telas
    @FXML private Label labelNomeUsuario;
    @FXML private Label labelIMC;
    @FXML private Button buttonVerMaisMetas;
    @FXML private Button buttonVerMaisExercicios;
    @FXML private Button buttonVerMaisDietas;

    // Variáveis para armazenar as telas carregadas

    private Parent telaDieta;
    private Parent telaExercicio;
    private Parent telaMeta;
    private Parent telaRefeicao;
    private Parent telaTreino;

    private Stage primaryStage;  // Adicione esta variável

    public void setPrimaryStage(Stage primaryStage) { // Adicione este método
        this.primaryStage = primaryStage;
    }



    @FXML
    // Inicializa o controller principal
    public void initialize() {
        try {
            // Carrega todas as telas FXML durante a inicialização
            telaDieta = carregarTela("/com/example/queimacaloria/views/dieta-view.fxml");
            telaExercicio = carregarTela("/com/example/queimacaloria/views/exercicio-view.fxml");
            telaMeta = carregarTela("/com/example/queimacaloria/views/meta-view.fxml");
            telaRefeicao = carregarTela("/com/example/queimacaloria/views/refeicao-view.fxml");
            telaTreino = carregarTela("/com/example/queimacaloria/views/treino-view.fxml");


            // Injeta o MainController nos sub-controllers *APÓS* o carregamento
            ((DietaController) getController(telaDieta)).setMainController(this);
            ((ExercicioController) getController(telaExercicio)).setMainController(this);
            ((MetaController) getController(telaMeta)).setMainController(this);
            ((RefeicaoController) getController(telaRefeicao)).setMainController(this);
            ((TreinoController) getController(telaTreino)).setMainController(this);


            mostrarTelaPrincipal(); // <---  mostra Tela Principal() inicialmente
        } catch (IOException e) {
            e.printStackTrace(); // Tratar erro de carregamento
        }
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
    public void logout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/auth-view.fxml"));
            Parent authView = loader.load();
            Scene authScene = new Scene(authView);
            primaryStage.setScene(authScene); // Volta para a cena de autenticação
            primaryStage.setTitle("YouFit - Login/Registro"); // Título apropriado
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Trate o erro (por exemplo, mostrar um alerta)
        }
    }
}