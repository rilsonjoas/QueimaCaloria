package com.example.queimacaloria.controllers;

import com.example.queimacaloria.interfaces.IBaseAdmin;
import com.example.queimacaloria.negocio.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javafx.scene.control.ButtonType;


import java.io.IOException;
import java.util.Optional;

public class AdminMainController implements IBaseAdmin {

    @FXML private StackPane adminContentArea;
    private Stage primaryStage;
    private Usuario usuarioLogado;

    //Views dos botões ADM
    private Parent telaAdminUsuarios;
    private Parent telaAdminDietas;
    private Parent telaAdminRefeicoes;
    private Parent telaAdminExercicios;
    private Parent telaAdminMetas;
    private Parent telaAdminTreinos; //ADD

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    @FXML
    public void initialize() {
        // Carrega as telas em um método separado
        carregarTelas();
    }

    private void carregarTelas() {
        try {
            telaAdminUsuarios = carregarTela("/com/example/queimacaloria/views/admin-usuarios-view.fxml");
            telaAdminDietas = carregarTela("/com/example/queimacaloria/views/admin-dietas-view.fxml");
            telaAdminRefeicoes = carregarTela("/com/example/queimacaloria/views/admin-refeicoes-view.fxml");
            telaAdminExercicios = carregarTela("/com/example/queimacaloria/views/admin-exercicios-view.fxml");
            telaAdminMetas = carregarTela("/com/example/queimacaloria/views/admin-metas-view.fxml");
            telaAdminTreinos = carregarTela("/com/example/queimacaloria/views/admin-treinos-view.fxml");

            //Injetando o Main Controller
            ((AdminUsuariosController) getController(telaAdminUsuarios)).setMainController(this);
            ((AdminDietasController) getController(telaAdminDietas)).setMainController(this);
            ((AdminRefeicoesController) getController(telaAdminRefeicoes)).setMainController(this);
            ((AdminExerciciosController) getController(telaAdminExercicios)).setMainController(this);
            ((AdminMetasController) getController(telaAdminMetas)).setMainController(this);
            ((AdminTreinosController) getController(telaAdminTreinos)).setMainController(this);

            mostrarTelaAdminUsuarios();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar telas do Admin", e.getMessage());
        }
    }

    private Object getController(Parent parent){
        return ((FXMLLoader) parent.getProperties().get("fxmlLoader")).getController();
    }

    private Parent carregarTela(String caminho) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
        Parent root = loader.load();
        root.getProperties().put("fxmlLoader", loader);
        return root;
    }

    @FXML
    public void mostrarTelaAdminUsuarios() {
        adminContentArea.getChildren().setAll(telaAdminUsuarios);
    }

    @FXML
    public void mostrarTelaAdminDietas() {
        adminContentArea.getChildren().setAll(telaAdminDietas);
    }

    @FXML
    public void mostrarTelaAdminRefeicoes() {
        adminContentArea.getChildren().setAll(telaAdminRefeicoes);
    }

    @FXML
    public void mostrarTelaAdminExercicios() {
        adminContentArea.getChildren().setAll(telaAdminExercicios);
    }

    @FXML
    public void mostrarTelaAdminMetas() {
        adminContentArea.getChildren().setAll(telaAdminMetas);
    }

    @FXML
    public void mostrarTelaAdminTreinos() {
        adminContentArea.getChildren().setAll(telaAdminTreinos);
    }

    // Método para fazer o logout e voltar para a tela de autenticação
    @FXML
    public void logout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Logout");
        alert.setHeaderText("Sair do YouFit");
        alert.setContentText("Tem certeza que deseja sair?");

        ButtonType buttonTypeSim = new ButtonType("Sim");
        ButtonType buttonTypeNao = new ButtonType("Não");

        alert.getButtonTypes().setAll(buttonTypeSim, buttonTypeNao);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == buttonTypeSim) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/auth-view.fxml"));
                Parent authView = loader.load();
                Scene authScene = new Scene(authView);
                primaryStage.setScene(authScene);
                primaryStage.setTitle("YouFit - Login/Registro");
                primaryStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void atualizarDadosTelaPrincipal() {
        // Implemente a lógica de atualização da tela principal do admin aqui, se necessário.
        System.out.println("Atualizando dados da tela principal do Admin...");
    }
}