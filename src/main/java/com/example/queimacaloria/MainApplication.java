package com.example.queimacaloria;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/com/example/queimacaloria/views/register-view.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 800, 600); // Define um tamanho inicial para a janela
        stage.setTitle("Queima Caloria");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        System.out.println("Início do Teste");
        launch();
    }
}