package com.example.queimacaloria.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import com.example.queimacaloria.negocio.Usuario;
import javafx.beans.binding.Bindings; // Importante para o binding

public class MainController {

    @FXML private StackPane areaConteudo;
    @FXML private Label labelNomeUsuario;
    @FXML private Label labelIMC;
    @FXML private Button buttonVerMaisMetas;
    @FXML private Button buttonVerMaisExercicios;
    @FXML private Button buttonVerMaisDietas;
    @FXML private Button buttonVerMaisPerfil;

    // NOVOS LABELS
    @FXML private Label labelPesoUsuario;
    @FXML private Label labelAlturaUsuario;
    @FXML private Label labelIMCSituacao; // Label para a situação do IMC


    private Parent telaDieta;
    private Parent telaExercicio;
    private Parent telaMeta;
    private Parent telaRefeicao;
    private Parent telaTreino;
    private Parent telaPerfil;

    private Stage primaryStage;
    private Usuario usuarioLogado;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;

        if (usuarioLogado != null) {
            labelNomeUsuario.setText(usuarioLogado.getNome());

            // Binding para Peso
            labelPesoUsuario.textProperty().bind(Bindings.createStringBinding(
                    () -> String.format("Peso: %.2f kg", usuarioLogado.getPeso()),
                    usuarioLogado.pesoProperty()
            ));

            // Binding para Altura
            labelAlturaUsuario.textProperty().bind(Bindings.createStringBinding(
                    () -> String.format("Altura: %.2f m", usuarioLogado.getAltura()),
                    usuarioLogado.alturaProperty()
            ));

            // Binding para IMC (agora com a situação)
            labelIMC.textProperty().bind(Bindings.createStringBinding(
                    () -> String.format("IMC: %.2f", usuarioLogado.getImc()),
                    usuarioLogado.imcProperty()
            ));

            // Binding para a Situação do IMC *DENTRO* do binding do IMC
            labelIMCSituacao.textProperty().bind(Bindings.createStringBinding(
                    () -> "Situação: " + getSituacaoIMC(usuarioLogado.getImc()), // Chama o método para obter a situação
                    usuarioLogado.imcProperty() // Atualiza quando o IMC mudar
            ));


        } else {
            labelNomeUsuario.setText("Nome do Usuário");
            labelPesoUsuario.setText("Peso: --"); // Valor padrão
            labelAlturaUsuario.setText("Altura: --"); // Valor padrão
            labelIMC.setText("IMC: --");
            labelIMCSituacao.setText("Situação: --"); // Valor padrão
        }
    }

    // Método para determinar a situação do IMC (NOVO)
    private String getSituacaoIMC(float imc) {
        if (imc < 18.5) {
            return "Abaixo do peso";
        } else if (imc < 25) {
            return "Peso normal";
        } else if (imc < 30) {
            return "Sobrepeso";
        } else if (imc < 35) {
            return "Obesidade grau I";
        } else if (imc < 40) {
            return "Obesidade grau II";
        } else {
            return "Obesidade grau III";
        }
    }

    // ... restante do MainController (sem alterações) ...
    @FXML
    public void initialize() {
        try {
            telaDieta = carregarTela("/com/example/queimacaloria/views/dieta-view.fxml");
            telaExercicio = carregarTela("/com/example/queimacaloria/views/exercicio-view.fxml");
            telaMeta = carregarTela("/com/example/queimacaloria/views/meta-view.fxml");
            telaRefeicao = carregarTela("/com/example/queimacaloria/views/refeicao-view.fxml");
            telaTreino = carregarTela("/com/example/queimacaloria/views/treino-view.fxml");
            telaPerfil = carregarTela("/com/example/queimacaloria/views/perfil-view.fxml");

            ((DietaController) getController(telaDieta)).setMainController(this);
            ((ExercicioController) getController(telaExercicio)).setMainController(this);
            ((MetaController) getController(telaMeta)).setMainController(this);
            ((RefeicaoController) getController(telaRefeicao)).setMainController(this);
            ((TreinoController) getController(telaTreino)).setMainController(this);
            ((PerfilController) getController(telaPerfil)).setMainController(this); //isso garante que o main controller seja setado antes.

            // mostrarTelaPrincipal() *ANTES* de tentar modificar os labels!
            mostrarTelaPrincipal();


        } catch (IOException e) {
            e.printStackTrace();
            // Trate a exceção apropriadamente (ex: mostrar um alerta)
        }
    }

    private Object getController(Parent parent) {
        return ((FXMLLoader) parent.getProperties().get("fxmlLoader")).getController();
    }

    private Parent carregarTela(String caminho) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
        Parent root = loader.load();
        root.getProperties().put("fxmlLoader", loader);
        return root;
    }

    @FXML
    public void mostrarTelaDieta() {
        areaConteudo.getChildren().setAll(telaDieta);
    }

    @FXML
    public void mostrarTelaExercicio() {
        areaConteudo.getChildren().setAll(telaExercicio);
    }

    @FXML
    public void mostrarTelaMeta() {
        areaConteudo.getChildren().setAll(telaMeta);
    }

    @FXML
    public void mostrarTelaRefeicao() {
        areaConteudo.getChildren().setAll(telaRefeicao);
    }

    @FXML
    public void mostrarTelaTreino() {
        areaConteudo.getChildren().setAll(telaTreino);
    }

    @FXML
    public void mostrarTelaPerfil() {
        // *PRIMEIRO* obtém a instância do controlador.
        PerfilController perfilController = (PerfilController) getController(telaPerfil);

        // *DEPOIS* define o usuário logado.  Isso vai chamar atualizarLabels()
        // e preencher os labels *ANTES* de a tela ser exibida.
        perfilController.setUsuarioLogado(usuarioLogado);

        // *AGORA* você pode adicionar a tela ao StackPane.
        areaConteudo.getChildren().setAll(telaPerfil);
    }

    @FXML
    public void mostrarTelaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/main-screen-content.fxml"));
            Parent telaPrincipalContent = loader.load();
            areaConteudo.getChildren().setAll(telaPrincipalContent);

            // Obtenção dos elementos gráficos *DEPOIS* de carregar o FXML
            labelNomeUsuario = (Label) telaPrincipalContent.lookup("#labelNomeUsuario");
            labelIMC = (Label) telaPrincipalContent.lookup("#labelIMC");  // Certifique-se de que este ID está correto no FXML
            labelPesoUsuario = (Label) telaPrincipalContent.lookup("#labelPesoUsuario"); // Adicionado
            labelAlturaUsuario = (Label) telaPrincipalContent.lookup("#labelAlturaUsuario"); //Adicionado
            labelIMCSituacao = (Label) telaPrincipalContent.lookup("#labelIMCSituacao"); // Adicionado
            buttonVerMaisMetas = (Button) telaPrincipalContent.lookup("#buttonVerMaisMetas");
            buttonVerMaisExercicios = (Button) telaPrincipalContent.lookup("#buttonVerMaisExercicios");
            buttonVerMaisDietas = (Button) telaPrincipalContent.lookup("#buttonVerMaisDietas");
            buttonVerMaisPerfil = (Button) telaPrincipalContent.lookup("#buttonVerMaisPerfil");

            // Configuração dos eventos dos botões
            if (buttonVerMaisMetas != null) {
                buttonVerMaisMetas.setOnAction(e -> mostrarTelaMeta());
            }
            if (buttonVerMaisExercicios != null) {
                buttonVerMaisExercicios.setOnAction(e -> mostrarTelaExercicio());
            }
            if (buttonVerMaisDietas != null) {
                buttonVerMaisDietas.setOnAction(e -> mostrarTelaDieta());
            }

            if (buttonVerMaisPerfil != null) {
                buttonVerMaisPerfil.setOnAction(e -> mostrarTelaPerfil());
            }

            // Preenchimento dos labels, *SE* o usuário estiver logado
            if (usuarioLogado != null) {
                setUsuarioLogado(usuarioLogado); //Chama o setUsuarioLogado para atualizar a interface.  DEPOIS de obter os labels!
            }
            else { // Caso não haja usuário logado.
                labelNomeUsuario.setText("Nome do Usuário");
                labelIMC.setText("IMC: --");
                labelIMCSituacao.setText("Situação: --");
                labelPesoUsuario.setText("Peso: --");
                labelAlturaUsuario.setText("Altura: --");
            }


        } catch (IOException e) {
            e.printStackTrace();
            // Trate a exceção apropriadamente (ex: mostrar um alerta)
        }
    }



    @FXML
    public void logout() {
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