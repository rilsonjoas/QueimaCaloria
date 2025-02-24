package com.example.queimacaloria;

import com.example.queimacaloria.controllers.AuthController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.Alert;

public class MainApplication extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage palco) throws IOException {
        this.primaryStage = palco;

        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/com/example/queimacaloria/views/auth-view.fxml"));
            Parent raiz = loader.load();

            AuthController authController = loader.getController();

            Scene cena = new Scene(raiz, 800, 600);
            palco.setTitle("YouFit");
            palco.setScene(cena);
            palco.show();

        } catch (Exception e) {
            e.printStackTrace();

            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Erro ao iniciar a aplicação");
            alerta.setHeaderText("Ocorreu um erro durante a inicialização.");
            alerta.setContentText("Verifique o console para mais detalhes.  " + e.getMessage());
            alerta.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}