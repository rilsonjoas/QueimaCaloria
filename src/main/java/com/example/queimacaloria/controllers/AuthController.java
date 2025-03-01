package com.example.queimacaloria.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import com.example.queimacaloria.negocio.Usuario;

//Este controller administra as telas de login e cadastro. Ele faz com que elas operem separados da tela principal.

public class AuthController {

    @FXML
    private StackPane authContainer;

    private Parent telaLogin;
    private Parent telaRegistro;

    private Stage primaryStage; // ADICIONADO

    // Inicializa o controlador, carregando as telas de login e registro.
    @FXML
    public void initialize(){
        try{
            telaLogin = carregarTela("/com/example/queimacaloria/views/login-view.fxml");
            telaRegistro = carregarTela("/com/example/queimacaloria/views/registro-view.fxml");

            ((LoginController) getController(telaLogin)).setAuthController(this);
            ((RegistroController) getController(telaRegistro)).setAuthController(this);

            mostrarTelaLogin();

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    // Obtém o controlador de uma tela a partir do seu Parent.
    private Object getController(Parent parent){
        return ((FXMLLoader) parent.getProperties().get("fxmlLoader")).getController();
    }

    // Carrega uma tela FXML a partir do caminho especificado.
    private Parent carregarTela(String caminho) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
        Parent root = loader.load();
        root.getProperties().put("fxmlLoader", loader); // Armazena o loader.
        return root;
    }

    // Exibe a tela de login no container principal.
    public void mostrarTelaLogin() {
        authContainer.getChildren().setAll(telaLogin);
    }

    // Exibe a tela de registro no container principal.
    public void mostrarTelaRegistro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/registro-view.fxml"));
            Parent telaRegistro = loader.load();

            RegistroController registroController = loader.getController();
            registroController.setAuthController(this);

            authContainer.getChildren().setAll(telaRegistro);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Exibe a tela principal, definindo o usuário logado.
    public void mostrarTelaPrincipal(Stage primaryStage, Usuario usuario) {
        this.primaryStage = primaryStage;  //ADICIONADO
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/main-view.fxml"));
            Parent telaPrincipal = loader.load();

            MainController mainController = loader.getController();
            mainController.setPrimaryStage(primaryStage);
            mainController.setUsuarioLogado(usuario);

            Scene scene = new Scene(telaPrincipal);
            primaryStage.setScene(scene);
            primaryStage.setTitle("YouFit - Tela Principal");
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar a tela principal", e.getMessage());
        }
    }

    // Exibe a tela ADM diretamente
    public void mostrarTelaAdminUsuarios(Stage primaryStage, Usuario usuario) {
        this.primaryStage = primaryStage;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/admin-usuarios-view.fxml"));
            Parent telaAdmin = loader.load();

            AdminUsuariosController adminUsuariosController = loader.getController();
            adminUsuariosController.setUsuarioLogado(usuario);
            MainController mainController = null; //Declarar maincontroller


            Scene scene = primaryStage.getScene();

            if(scene != null) {
                Parent root = scene.getRoot();
                FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/main-view.fxml"));
                Parent telaPrincipal = mainLoader.load();
                mainController = mainLoader.getController();
                System.out.println("MainController recuperado com sucesso");

            }

            if(mainController != null){
                adminUsuariosController.setMainController(mainController);
            } else {
                System.err.println("MainController is null");
            }

            adminUsuariosController.setPrimaryStage(primaryStage); //Passando o stage para o controller
            atualizarDadosTelaAdmin(adminUsuariosController); //Passando os dados da tela principal para a tela adm
            scene = new Scene(telaAdmin);
            primaryStage.setScene(scene);
            primaryStage.setTitle("YouFit - Administração de Usuários");
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar a tela de administração", e.getMessage());
        }
    }

    public void atualizarDadosTelaAdmin(AdminUsuariosController adminUsuariosController) {
        if(adminUsuariosController == null) {
            System.err.println("AdminUsuariosController is null");
            return;
        }
        //Pegando o controlador da tela principal
        if (primaryStage != null && primaryStage.getScene() != null && primaryStage.getScene().getRoot() != null) {
            try {
                FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/main-view.fxml"));
                //Parent telaPrincipal = mainLoader.load();
                MainController mainController = mainLoader.getController();
                //Se o usuário a ser atualizado for o administrador, então atualize
                //Setando como usuário logado o usuário da tela inicial
                FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/main-view.fxml"));
                Parent tela = rootLoader.load();
                MainController controllerTelaPrincipal =  rootLoader.getController();

                if (controllerTelaPrincipal != null) {
                    Usuario adminLogado = controllerTelaPrincipal.getUsuarioLogado();

                    if (adminLogado != null) {
                        System.out.println("Dados da tela principal copiados para a tela ADM.");
                        adminUsuariosController.setUsuarioLogado(adminLogado);
                        adminUsuariosController.setMainController(controllerTelaPrincipal);

                    }
                }

                else
                    System.err.println("MainController is null");
            }
            catch (IOException e) {
                System.err.println("Erro ao carregar a tela de principal");
                e.printStackTrace();
            }
        }
    }

    // Exibe um alerta na tela.
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}