package com.example.queimacaloria;

import com.example.queimacaloria.controllers.AuthController;
import com.example.queimacaloria.dados.DatabaseConnector;
import com.example.queimacaloria.dados.DatabaseInitializer;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Usuario;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import javafx.scene.control.Alert;

public class MainApplication extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage palco) throws IOException {
        this.primaryStage = palco;

        try {
            // Inicializar a conexão com o banco de dados
            DatabaseConnector.getConnection();

            // Garantir que a conta ADMIN exista
            garantirAdminExistente();

            // Carregue os dados predefinidos
            Fachada fachada = Fachada.getInstanciaUnica();

            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/com/example/queimacaloria/views/auth-view.fxml"));
            Parent raiz = loader.load();

            AuthController authController = loader.getController();

            Scene cena = new Scene(raiz, 800, 600);
            palco.setTitle("YouFit");
            palco.setScene(cena);
            palco.show();

        } catch (SQLException e) {
            e.printStackTrace();

            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Erro ao iniciar a aplicação");
            alerta.setHeaderText("Ocorreu um erro durante a inicialização.");
            alerta.setContentText("Verifique o console para mais detalhes.  " + e.getMessage());
            alerta.showAndWait();
        }
    }

    @Override
    public void stop() {
        // Fechar a conexão com o banco de dados ao encerrar a aplicação
        DatabaseConnector.closeConnection();
        System.out.println("Conexão fechada antes de encerrar");
    }

    private void garantirAdminExistente() {
        Fachada fachada = Fachada.getInstanciaUnica();
        List<Usuario> administradores = fachada.listarUsuarios(Usuario.TipoUsuario.ADMINISTRADOR);

        if (administradores.isEmpty()) {
            // Crie o administrador padrão se não existir
            fachada.cadastrarUsuario(
                    "Administrador",
                    "admin",
                    "admin",
                    LocalDate.now().minusYears(30),
                    Usuario.Sexo.Masculino,
                    70f,
                    170f,
                    Usuario.TipoUsuario.ADMINISTRADOR.getDescricao()
            );

            System.out.println("Conta de administrador padrão criada.");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}