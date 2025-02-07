package com.example.queimacaloria;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// Classe principal da aplicação JavaFX
public class MainApplication extends Application {

    @Override
    // Método chamado quando a aplicação inicia
    public void start(Stage stage) throws IOException {
        // Carrega o arquivo FXML da interface principal
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/com/example/queimacaloria/views/main-view.fxml"));
        Parent root = fxmlLoader.load();

        // Cria a cena principal com o conteúdo carregado do FXML
        Scene scene = new Scene(root, 800, 600); // Define tamanho inicial da janela
        stage.setTitle("YouFit"); // Define o título da janela
        stage.setScene(scene); // Define a cena na janela
        stage.show(); // Exibe a janela
    }


    public static void main(String[] args) {
        System.out.println("Início do Teste");
        launch(); // Inicia a aplicação JavaFX
    }
}
