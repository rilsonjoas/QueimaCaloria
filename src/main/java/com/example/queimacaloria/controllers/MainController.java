package com.example.queimacaloria.controllers;

import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import com.example.queimacaloria.negocio.Dieta;
import com.example.queimacaloria.negocio.Refeicao;
import javafx.beans.binding.Bindings;
//Adicione os imports
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import com.example.queimacaloria.negocio.Meta;
import javafx.scene.control.ProgressBar;
import javafx.collections.ListChangeListener;
//Imports novos
import com.example.queimacaloria.negocio.Exercicio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;  // Importante!


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
    @FXML private ProgressBar barraProgressoMetas;
    @FXML private Label labelProgressoMetas;
    //Novo atributo
    @FXML
    private ListView<String> listViewAtividadesRecentes;


    private Parent telaDieta;
    private Parent telaExercicio;
    private Parent telaMeta;
    private Parent telaRefeicao;
    private Parent telaTreino;
    private Parent telaPerfil;

    private Stage primaryStage;
    private Usuario usuarioLogado;
    private Dieta dietaSelecionada;
    private DoubleProperty progressoGeral = new SimpleDoubleProperty(0.0);

    //Nova lista
    private ObservableList<String> atividadesRecentes = FXCollections.observableArrayList();



    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuario) {
        System.out.println("MainController.setUsuarioLogado: Iniciando...");
        this.usuarioLogado = usuario;

        if (usuarioLogado != null) {
            System.out.println("MainController.setUsuarioLogado: Usuário logado: " + usuarioLogado.getEmail());
            System.out.println("MainController.setUsuarioLogado: Metas do usuário (antes do listener): " + usuarioLogado.getMetas());

            if (labelNomeUsuario != null) labelNomeUsuario.setText(usuarioLogado.getNome());
            if (labelPesoUsuario != null) labelPesoUsuario.textProperty().bind(Bindings.createStringBinding(() -> String.format("Peso: %.2f kg", usuarioLogado.getPeso()), usuarioLogado.pesoProperty()));
            if (labelAlturaUsuario != null) labelAlturaUsuario.textProperty().bind(Bindings.createStringBinding(() -> String.format("Altura: %.2f m", usuarioLogado.getAltura()), usuarioLogado.alturaProperty()));
            if (labelIMC != null) labelIMC.textProperty().bind(Bindings.createStringBinding(() -> String.format("IMC: %.2f", usuarioLogado.getImc()), usuarioLogado.imcProperty()));
            if (labelIMCSituacao != null) labelIMCSituacao.textProperty().bind(Bindings.createStringBinding(() -> "Situação: " + getSituacaoIMC(usuarioLogado.getImc()), usuarioLogado.imcProperty()));

            atualizarCalorias();

            if (barraProgressoMetas != null) {
                barraProgressoMetas.progressProperty().bind(progressoGeral);
            }
            if(labelProgressoMetas != null){
                labelProgressoMetas.textProperty().bind(Bindings.createStringBinding(
                        () -> String.format("%.1f%% Completo", progressoGeral.get() * 100),
                        progressoGeral
                ));
            }


            if (usuarioLogado != null) {
                usuarioLogado.getMetas().addListener((ListChangeListener<Meta>) change -> {
                    System.out.println("MainController: Listener de metas disparado!");
                    atualizarDadosTelaPrincipal();
                });
                System.out.println("MainController: Listener de metas ADICIONADO.");

            }


        } else {
            if (labelNomeUsuario != null) labelNomeUsuario.setText("Nome do Usuário");
            if (labelPesoUsuario != null) labelPesoUsuario.setText("Peso: --");
            if (labelAlturaUsuario != null) labelAlturaUsuario.setText("Altura: --");
            if (labelIMC != null) labelIMC.setText("IMC: --");
            if (labelIMCSituacao != null) labelIMCSituacao.setText("Situação: --");
            if (labelCaloriasDia != null) labelCaloriasDia.setText("Calorias: -- / --");
            if (barraProgressoMetas != null) {
                barraProgressoMetas.progressProperty().unbind();
                barraProgressoMetas.setProgress(0.0);
            }
            if(labelProgressoMetas != null){
                labelProgressoMetas.setText("0.0% Completo");
            }
        }

        System.out.println("MainController.setUsuarioLogado: Chamando atualizarDadosTelaPrincipal");
        atualizarDadosTelaPrincipal();
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
            Fachada.getInstanciaUnica().setMainController(this);

            mostrarTelaPrincipal();

            // Inicializa a ListView (mesmo que esteja vazia no começo)
            if (listViewAtividadesRecentes != null) {
                listViewAtividadesRecentes.setItems(atividadesRecentes);
            }


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

    public void setDietaSelecionada(Dieta dieta) {
        this.dietaSelecionada = dieta;
        System.out.println("MainController: Dieta selecionada: " + (dieta != null ? dieta.getNome() : "Nenhuma"));
    }

    public Dieta getDietaSelecionada(){
        return this.dietaSelecionada;
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
            barraProgressoMetas = (ProgressBar) telaPrincipalContent.lookup("#barraProgressoMetas");
            labelProgressoMetas = (Label) telaPrincipalContent.lookup("#labelProgressoMetas");
            //  Obtém a referência à ListView
            listViewAtividadesRecentes = (ListView<String>) telaPrincipalContent.lookup("#listViewAtividadesRecentes");


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
                //Adicionei essa verificação, pois estava dando erro:
                if (barraProgressoMetas != null) {
                    barraProgressoMetas.progressProperty().unbind(); // Desfaz o binding
                    barraProgressoMetas.setProgress(0.0);          // Define como 0
                }
            }
            if (listViewAtividadesRecentes != null) {
                listViewAtividadesRecentes.setItems(atividadesRecentes);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int calcularTotalCaloriasConsumidas() {
        System.out.println("MainController.calcularTotalCaloriasConsumidas: Iniciando...");
        int total = 0;
        if (usuarioLogado != null && usuarioLogado.getDietas() != null) {
            System.out.println("MainController.calcularTotalCaloriasConsumidas: Dietas do usuário: " + usuarioLogado.getDietas().size());
            for (Dieta dieta : usuarioLogado.getDietas()) {
                System.out.println("  Dieta: " + dieta.getNome() + ", Refeições: " + dieta.getRefeicoes());
                total += calcularTotalCaloriasDieta(dieta);
            }
        } else {
            System.out.println("MainController.calcularTotalCaloriasConsumidas: Usuário ou dietas nulas.");
        }
        System.out.println("MainController.calcularTotalCaloriasConsumidas: Total: " + total);
        return total;
    }

    private int calcularTotalCaloriasDieta(Dieta dieta) {
        System.out.println("MainController.calcularTotalCaloriasDieta: Iniciando para dieta: " + dieta.getNome());
        int total = 0;
        if (dieta.getRefeicoes() != null) {
            System.out.println("MainController.calcularTotalCaloriasDieta: Refeições na dieta: " + dieta.getRefeicoes().size());
            for (Refeicao refeicao : dieta.getRefeicoes()) {
                System.out.println("    Refeição: " + refeicao.getNome() + ", Calorias: " + refeicao.getCalorias());
                total += refeicao.getCalorias();
            }
        }else{
            System.out.println("MainController.calcularTotalCaloriasDieta: Refeições nulas.");
        }
        System.out.println("MainController.calcularTotalCaloriasDieta: Total da dieta: " + total);
        return total;
    }


    private Dieta getDietaAtual() {
        if (usuarioLogado != null && usuarioLogado.getDietas() != null && !usuarioLogado.getDietas().isEmpty()) {
            return usuarioLogado.getDietas().get(0);
        }
        return null;
    }

    public ObservableList<String> getAtividadesRecentes(){
        return this.atividadesRecentes;
    }

    private void atualizarCalorias() {
        System.out.println("MainController.atualizarCalorias: Iniciando...");
        if (usuarioLogado != null) {
            System.out.println("  Usuário logado: " + usuarioLogado.getEmail());
            int caloriasConsumidas = calcularTotalCaloriasConsumidas();
            Dieta dietaAtual = getDietaAtual();

            System.out.println("  Calorias Consumidas: " + caloriasConsumidas);
            System.out.println("  Dieta Atual: " + (dietaAtual != null ? dietaAtual.getNome() : "Nenhuma"));

            if (dietaAtual != null) {
                int caloriasDiarias = dietaAtual.getCaloriasDiarias();
                System.out.println("  Calorias Diárias da Dieta: " + caloriasDiarias);
                if (labelCaloriasDia != null) {
                    labelCaloriasDia.setText("Calorias: " + caloriasConsumidas + " / " + caloriasDiarias);
                } else {
                    System.out.println("    ERRO: labelCaloriasDia é nulo!");
                }
            } else {
                if (labelCaloriasDia != null) {
                    labelCaloriasDia.setText("Calorias: " + caloriasConsumidas + " / --");
                } else {
                    System.out.println("    ERRO: labelCaloriasDia é nulo!");
                }
            }
        } else {
            System.out.println("  Usuário logado é nulo.");
            if (labelCaloriasDia != null) {
                labelCaloriasDia.setText("Calorias: --/--");
            }else{
                System.out.println("    ERRO: labelCaloriasDia é nulo!");
            }

        }
    }
    public double calcularProgressoGeralUsuario() {
        double progressoTotal = 0.0;
        int contadorMetas = 0;

        if (usuarioLogado != null && usuarioLogado.getMetas() != null) {
            System.out.println("MainController.calcularProgressoGeralUsuario(): Metas do usuário: " + usuarioLogado.getMetas().size());

            for (Meta meta : usuarioLogado.getMetas()) {
                System.out.println("  Meta: " + meta.getDescricao() + ", Valor Alvo: " + meta.getValorAlvo() + ", Progresso Atual: " + meta.getProgressoAtual());

                if (meta.getValorAlvo() > 0) {
                    double progressoMeta = (meta.getProgressoAtual() / meta.getValorAlvo());
                    System.out.println("    Progresso da meta: " + progressoMeta);
                    progressoTotal += progressoMeta;
                    contadorMetas++;
                } else {
                    System.out.println("    Valor alvo da meta é 0. Ignorando no cálculo.");
                }
            }
            System.out.println("    Progresso total (soma): " + progressoTotal);

        } else {
            System.out.println("MainController.calcularProgressoGeralUsuario(): Usuário ou lista de metas nulos.");
        }

        if (contadorMetas > 0) {
            double mediaProgresso = progressoTotal / contadorMetas;
            System.out.println("  Média do progresso: " + mediaProgresso);
            return mediaProgresso;
        } else {
            System.out.println("  Nenhuma meta válida encontrada. Retornando 0.");
            return 0.0;
        }
    }

    //Adiciona um exercício a lista
    public void adicionarExercicioRecente(Exercicio exercicio) {
        String nomeExercicio = exercicio.getNome();

        //Remove o exercício se ele já existir na lista.
        atividadesRecentes.remove(nomeExercicio);
        atividadesRecentes.add(0, nomeExercicio); //Adiciona no começo da lista

        if(atividadesRecentes.size() > 5){
            atividadesRecentes.remove(5, atividadesRecentes.size()); //Mantém no máximo 5 elementos
        }
    }


    public void atualizarDadosTelaPrincipal() {
        System.out.println("MainController.atualizarDadosTelaPrincipal: Iniciando...");
        if (usuarioLogado != null) {
            System.out.println("MainController: Metas do usuário (dentro de atualizarDadosTelaPrincipal): " + usuarioLogado.getMetas());
            atualizarCalorias();
            progressoGeral.set(calcularProgressoGeralUsuario());

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
        } else {
            System.out.println("MainController.atualizarDadosTelaPrincipal: usuarioLogado NULO.");
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