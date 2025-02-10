package com.example.queimacaloria;

import com.example.queimacaloria.controllers.MainController;
import com.example.queimacaloria.negocio.InicializadorDados;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import javafx.scene.control.Alert;

// Classe principal da aplicação JavaFX
public class MainApplication extends Application {

    private Stage primaryStage;

    @Override
    // Método chamado quando a aplicação inicia
    public void start(Stage palco) throws IOException { // 'palco' = 'stage'
        this.primaryStage = palco; // Guarde a referência ao palco principal
        try {
            // 1. Inicializa os dados
            InicializadorDados.inicializarDados();


            // 2. Carrega o FXML principal e configura a cena
            FXMLLoader carregadorFXML = new FXMLLoader(MainApplication.class.getResource("/com/example/queimacaloria/views/auth-view.fxml"));
            Parent raiz = carregadorFXML.load();
            Scene cena = new Scene(raiz, 800, 600);
            palco.setTitle("YouFit");
            palco.setScene(cena);
            palco.show();



        } catch (Exception e) { // Captura qualquer exceção durante a inicialização
            e.printStackTrace(); // Para ajudar na depuração se há algum erro.

            // Mostra um alerta para o usuário
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Erro ao iniciar a aplicação");
            alerta.setHeaderText("Ocorreu um erro durante a inicialização dos dados.");
            alerta.setContentText("Verifique o console para mais detalhes.  " + e.getMessage());
            alerta.showAndWait();


        }
    }

    public void mostrarTelaPrincipal() {  // Novo método para exibir a tela principal
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/main-view.fxml"));
            Parent telaPrincipal = loader.load();

            MainController mainController = loader.getController();
            mainController.setPrimaryStage(primaryStage); // Injete o stage principal no MainController

            Scene scene = new Scene(telaPrincipal);
            primaryStage.setScene(scene);
            primaryStage.setTitle("YouFit - Tela Principal");
            primaryStage.show();


        } catch (IOException e) {
            e.printStackTrace();
            // Trate o erro, por exemplo, mostrando um alerta.
        }
    }

    public static void main(String[] args) {
        System.out.println("Início do Teste");
        launch(); // Inicia a aplicação JavaFX
    }
}