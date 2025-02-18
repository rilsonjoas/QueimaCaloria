
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
import javafx.beans.binding.Bindings;
import com.example.queimacaloria.negocio.Dieta;
import com.example.queimacaloria.negocio.Refeicao;
//Adicione os imports
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import com.example.queimacaloria.negocio.Meta;
import javafx.scene.control.ProgressBar; // Importante!


public class MainController {

    @FXML private StackPane areaConteudo;
    @FXML private Label labelNomeUsuario;
    @FXML private Label labelIMC;
    @FXML private Button buttonVerMaisMetas;
    @FXML private Button buttonVerMaisExercicios;
    @FXML private Button buttonVerMaisDietas;
    @FXML private Button buttonVerMaisPerfil;
    @FXML private Label labelPesoUsuario;
    @FXML private Label labelAlturaUsuario;
    @FXML private Label labelIMCSituacao;
    @FXML private Label labelCaloriasDia;
    //Adicionado
    @FXML private ProgressBar barraProgressoMetas;
    @FXML private Label labelProgressoMetas;


    private Parent telaDieta;
    private Parent telaExercicio;
    private Parent telaMeta;
    private Parent telaRefeicao;
    private Parent telaTreino;
    private Parent telaPerfil;

    private Stage primaryStage;
    private Usuario usuarioLogado;

    // NOVA PROPERTY:
    private DoubleProperty progressoGeral = new SimpleDoubleProperty(0.0);


    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Usuario getUsuarioLogado() {  // Getter para o usuário logado
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuario) {
        //System.out.println("MainController.setUsuarioLogado: Iniciando..."); // PRINT
        this.usuarioLogado = usuario;

        if (usuarioLogado != null) {
            //System.out.println("MainController.setUsuarioLogado: Usuário logado: " + usuarioLogado.getEmail()); // PRINT

            // Bindings (com verificações de nulidade)
            if (labelNomeUsuario != null) labelNomeUsuario.setText(usuarioLogado.getNome());
            if (labelPesoUsuario != null) labelPesoUsuario.textProperty().bind(Bindings.createStringBinding(() -> String.format("Peso: %.2f kg", usuarioLogado.getPeso()), usuarioLogado.pesoProperty()));
            if (labelAlturaUsuario != null) labelAlturaUsuario.textProperty().bind(Bindings.createStringBinding(() -> String.format("Altura: %.2f m", usuarioLogado.getAltura()), usuarioLogado.alturaProperty()));
            if (labelIMC != null) labelIMC.textProperty().bind(Bindings.createStringBinding(() -> String.format("IMC: %.2f", usuarioLogado.getImc()), usuarioLogado.imcProperty()));
            if (labelIMCSituacao != null) labelIMCSituacao.textProperty().bind(Bindings.createStringBinding(() -> "Situação: " + getSituacaoIMC(usuarioLogado.getImc()), usuarioLogado.imcProperty()));

            atualizarCalorias();

            // BINDINGS para a barra de progresso e label:
            if (barraProgressoMetas != null) {
                barraProgressoMetas.progressProperty().bind(progressoGeral); // BINDING!
            }
            if(labelProgressoMetas != null){
                labelProgressoMetas.textProperty().bind(Bindings.createStringBinding(
                        () -> String.format("%.1f%% Completo", progressoGeral.get() * 100), // Formata como porcentagem
                        progressoGeral
                ));
            }

        } else {
            // System.out.println("MainController.setUsuarioLogado: Usuário logado é nulo."); // PRINT
            if (labelNomeUsuario != null) labelNomeUsuario.setText("Nome do Usuário");
            if (labelPesoUsuario != null) labelPesoUsuario.setText("Peso: --");
            if (labelAlturaUsuario != null) labelAlturaUsuario.setText("Altura: --");
            if (labelIMC != null) labelIMC.setText("IMC: --");
            if (labelIMCSituacao != null) labelIMCSituacao.setText("Situação: --");
            if (labelCaloriasDia != null) labelCaloriasDia.setText("Calorias: -- / --");
            // Limpa a barra de progresso se não houver usuário
            if (barraProgressoMetas != null) {
                barraProgressoMetas.progressProperty().unbind(); // Desfaz o binding
                barraProgressoMetas.setProgress(0.0);          // Define como 0
            }
            if(labelProgressoMetas != null){
                labelProgressoMetas.setText("0.0% Completo");
            }
        }

        //System.out.println("MainController.setUsuarioLogado: Chamando atualizarDadosTelaPrincipal");
        atualizarDadosTelaPrincipal(); //Chamada para inicializar a tabela, depois que o usuário está logado.
    }

    private String getSituacaoIMC(float imc) {
        if (imc < 18.5) return "Abaixo do peso";
        else if (imc < 25) return "Peso normal";
        else if (imc < 30) return "Sobrepeso";
        else if (imc < 35) return "Obesidade grau I";
        else if (imc < 40) return "Obesidade grau II";
        else return "Obesidade grau III";
    }

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
            ((PerfilController) getController(telaPerfil)).setMainController(this);

            mostrarTelaPrincipal();

        } catch (IOException e) {
            e.printStackTrace();
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

    @FXML public void mostrarTelaDieta() { areaConteudo.getChildren().setAll(telaDieta); }
    @FXML public void mostrarTelaExercicio() { areaConteudo.getChildren().setAll(telaExercicio); }
    @FXML public void mostrarTelaMeta() { areaConteudo.getChildren().setAll(telaMeta); }
    @FXML public void mostrarTelaRefeicao() { areaConteudo.getChildren().setAll(telaRefeicao); }
    @FXML public void mostrarTelaTreino() { areaConteudo.getChildren().setAll(telaTreino); }

    @FXML
    public void mostrarTelaPerfil() {
        PerfilController perfilController = (PerfilController) getController(telaPerfil);
        perfilController.setUsuarioLogado(usuarioLogado);
        areaConteudo.getChildren().setAll(telaPerfil);
    }


    @FXML
    public void mostrarTelaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/main-screen-content.fxml"));
            Parent telaPrincipalContent = loader.load();
            areaConteudo.getChildren().setAll(telaPrincipalContent);

            labelNomeUsuario = (Label) telaPrincipalContent.lookup("#labelNomeUsuario");
            labelIMC = (Label) telaPrincipalContent.lookup("#labelIMC");
            labelPesoUsuario = (Label) telaPrincipalContent.lookup("#labelPesoUsuario");
            labelAlturaUsuario = (Label) telaPrincipalContent.lookup("#labelAlturaUsuario");
            labelIMCSituacao = (Label) telaPrincipalContent.lookup("#labelIMCSituacao");
            labelCaloriasDia = (Label) telaPrincipalContent.lookup("#labelCaloriasDia");
            buttonVerMaisMetas = (Button) telaPrincipalContent.lookup("#buttonVerMaisMetas");
            buttonVerMaisExercicios = (Button) telaPrincipalContent.lookup("#buttonVerMaisExercicios");
            buttonVerMaisDietas = (Button) telaPrincipalContent.lookup("#buttonVerMaisDietas");
            buttonVerMaisPerfil = (Button) telaPrincipalContent.lookup("#buttonVerMaisPerfil");
            // Obtém a referência à barra de progresso
            barraProgressoMetas = (ProgressBar) telaPrincipalContent.lookup("#barraProgressoMetas");
            labelProgressoMetas = (Label) telaPrincipalContent.lookup("#labelProgressoMetas");


            if (buttonVerMaisMetas != null) buttonVerMaisMetas.setOnAction(e -> mostrarTelaMeta());
            if (buttonVerMaisExercicios != null) buttonVerMaisExercicios.setOnAction(e -> mostrarTelaExercicio());
            if (buttonVerMaisDietas != null) buttonVerMaisDietas.setOnAction(e -> mostrarTelaDieta());
            if (buttonVerMaisPerfil != null) buttonVerMaisPerfil.setOnAction(e -> mostrarTelaPerfil());

            if (usuarioLogado != null) {
                setUsuarioLogado(usuarioLogado);
            }
            else {
                if(labelNomeUsuario != null) labelNomeUsuario.setText("Nome do Usuário");
                if(labelIMC != null)  labelIMC.setText("IMC: --");
                if(labelIMCSituacao != null) labelIMCSituacao.setText("Situação: --");
                if(labelPesoUsuario != null) labelPesoUsuario.setText("Peso: --");
                if(labelAlturaUsuario != null) labelAlturaUsuario.setText("Altura: --");
                if(labelCaloriasDia != null) labelCaloriasDia.setText("Calorias: -- / --");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private int calcularTotalCaloriasConsumidas() {
        //System.out.println("MainController.calcularTotalCaloriasConsumidas: Iniciando..."); // PRINT
        int total = 0;
        if (usuarioLogado != null && usuarioLogado.getDietas() != null) {
            //System.out.println("MainController.calcularTotalCaloriasConsumidas: Dietas do usuário: " + usuarioLogado.getDietas().size()); //PRINT
            for (Dieta dieta : usuarioLogado.getDietas()) {
                total += calcularTotalCaloriasDieta(dieta);
            }
        } else {
            //System.out.println("MainController.calcularTotalCaloriasConsumidas: Usuário ou dietas nulas."); //PRINT
        }
        //System.out.println("MainController.calcularTotalCaloriasConsumidas: Total: " + total); // PRINT
        return total;
    }

    private int calcularTotalCaloriasDieta(Dieta dieta) {
        //System.out.println("MainController.calcularTotalCaloriasDieta: Iniciando para dieta: " + dieta.getNome()); //PRINT
        int total = 0;
        if (dieta.getRefeicoes() != null) {
            //System.out.println("MainController.calcularTotalCaloriasDieta: Refeições na dieta: " + dieta.getRefeicoes().size()); //PRINT
            for (Refeicao refeicao : dieta.getRefeicoes()) {
                total += refeicao.getCalorias();
            }
        }else{
            //System.out.println("MainController.calcularTotalCaloriasDieta: Refeições nulas.");//PRINT
        }
        //System.out.println("MainController.calcularTotalCaloriasDieta: Total da dieta: " + total); // PRINT
        return total;
    }


    private Dieta getDietaAtual() {
        if (usuarioLogado != null && usuarioLogado.getDietas() != null && !usuarioLogado.getDietas().isEmpty()) {
            return usuarioLogado.getDietas().get(0);
        }
        return null;
    }

    private void atualizarCalorias() {
        //System.out.println("MainController.atualizarCalorias: Iniciando...");
        if (usuarioLogado != null) {
            int caloriasConsumidas = calcularTotalCaloriasConsumidas();
            Dieta dietaAtual = getDietaAtual();

            if (dietaAtual != null) {
                int caloriasDiarias = dietaAtual.getCaloriasDiarias();
                if(labelCaloriasDia != null) { //Verificação se o Label existe
                    labelCaloriasDia.setText("Calorias: " + caloriasConsumidas + " / " + caloriasDiarias);
                }
            } else {
                if(labelCaloriasDia != null) { //Verificação se o Label existe
                    labelCaloriasDia.setText("Calorias: " + caloriasConsumidas + " / --"); // Se não houver dieta
                }
            }
        } else{
            if(labelCaloriasDia != null){  //Verificação se o Label existe
                labelCaloriasDia.setText("Calorias: --/--");
            }

        }
    }
    public double calcularProgressoGeralUsuario() {
        double progressoTotal = 0.0;
        int contadorMetas = 0;

        if (usuarioLogado != null && usuarioLogado.getMetas() != null) {
            for (Meta meta : usuarioLogado.getMetas()) {
                if (meta.getValorAlvo() > 0) { // Evita divisão por zero!
                    progressoTotal += (meta.getProgressoAtual() / meta.getValorAlvo()) * 100.0; // Calcula a porcentagem *corretamente*
                    contadorMetas++;
                }
            }
        }

        if (contadorMetas > 0) {
            return progressoTotal / contadorMetas; // Média das *porcentagens*
        } else {
            return 0.0; // Se não houver metas, o progresso é 0.
        }
    }


    public void atualizarDadosTelaPrincipal() {
        //System.out.println("MainController.atualizarDadosTelaPrincipal: Iniciando..."); // PRINT
        if (usuarioLogado != null) {
            atualizarCalorias();
            //Muito Importante:
            progressoGeral.set(calcularProgressoGeralUsuario()/100);

            if (telaDieta != null)
                ((DietaController) getController(telaDieta)).atualizarTabelaDietasUsuario();
            if (telaExercicio != null)
                ((ExercicioController) getController(telaExercicio)).atualizarTabelaExerciciosUsuario();
            if (telaMeta != null)
                ((MetaController) getController(telaMeta)).atualizarTabelaMetasUsuario();
            if (telaRefeicao != null)
                ((RefeicaoController) getController(telaRefeicao)).atualizarTabelaRefeicoesUsuario();
            if (telaTreino != null)
                ((TreinoController) getController(telaTreino)).atualizarTabelaTreinosUsuario();
        } else{
            //System.out.println("MainController.atualizarDadosTelaPrincipal: usuarioLogado NULO."); //PRINT
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

    