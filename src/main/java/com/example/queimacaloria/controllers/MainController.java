package com.example.queimacaloria.controllers;

import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Refeicao;
import com.example.queimacaloria.negocio.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.List;

import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import com.example.queimacaloria.negocio.Meta;
import javafx.scene.control.ProgressBar;
import javafx.collections.ListChangeListener;
//Imports novos
import com.example.queimacaloria.negocio.Exercicio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
//import javafx.scene.control.ListView; Removido
import javafx.beans.binding.StringBinding; // Importante para o binding da pontuação

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
    //@FXML private ListView<String> listViewAtividadesRecentes; Removido
    //Novo label
    @FXML
    private Label labelPontuacao;
    //Label para as atividades recentes
    @FXML
    private Label labelAtividadesRecentes;

    private Parent telaDieta;
    private Parent telaExercicio;
    private Parent telaMeta;
    private Parent telaRefeicao;
    private Parent telaTreino;
    private Parent telaPerfil;

    private Stage primaryStage;
    private Usuario usuarioLogado;
    private DoubleProperty progressoGeral = new SimpleDoubleProperty(0.0);
    private ObservableList<String> atividadesRecentes = FXCollections.observableArrayList();

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    //Getter modificado
    public ObservableList<String> getAtividadesRecentes() {
        return this.atividadesRecentes;
    }

    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;

        if (usuarioLogado != null) {
            if (labelNomeUsuario != null) labelNomeUsuario.setText(usuarioLogado.getNome());
            if (labelPesoUsuario != null)
                labelPesoUsuario.textProperty().bind(Bindings.createStringBinding(() -> String.format("Peso: %.2f kg", usuarioLogado.getPeso()), usuarioLogado.pesoProperty()));
            if (labelAlturaUsuario != null)
                labelAlturaUsuario.textProperty().bind(Bindings.createStringBinding(() -> String.format("Altura: %.2f m", usuarioLogado.getAltura()), usuarioLogado.alturaProperty()));
            if (labelIMC != null)
                labelIMC.textProperty().bind(Bindings.createStringBinding(() -> String.format("IMC: %.2f", usuarioLogado.getImc()), usuarioLogado.imcProperty()));
            if (labelIMCSituacao != null)
                labelIMCSituacao.textProperty().bind(Bindings.createStringBinding(() -> "Situação: " + getSituacaoIMC(usuarioLogado.getImc()), usuarioLogado.imcProperty()));

            atualizarCalorias();

            if (barraProgressoMetas != null) {
                barraProgressoMetas.progressProperty().bind(progressoGeral);
            }
            if (labelProgressoMetas != null) {
                labelProgressoMetas.textProperty().bind(Bindings.createStringBinding(
                        () -> String.format("%.1f%% Completo", progressoGeral.get() * 100),
                        progressoGeral
                ));
            }
            //Novo binding
            if (labelPontuacao != null) {
                labelPontuacao.textProperty().bind(Bindings.createStringBinding(
                        () -> "Pontuação: " + usuarioLogado.getPontuacao(), // Formatação
                        usuarioLogado.pontuacaoProperty() // A propriedade!
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
            if (labelProgressoMetas != null) {
                labelProgressoMetas.setText("0.0% Completo");
            }
            //Se o usuário não estiver logado, a pontuação some.
            if (labelPontuacao != null) {
                labelPontuacao.setText("Pontuação: --");
            }
        }

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
            barraProgressoMetas = (ProgressBar) telaPrincipalContent.lookup("#barraProgressoMetas");
            labelProgressoMetas = (Label) telaPrincipalContent.lookup("#labelProgressoMetas");
            //listViewAtividadesRecentes = (ListView<String>) telaPrincipalContent.lookup("#listViewAtividadesRecentes"); Removido
            //Nova label
            labelPontuacao = (Label) telaPrincipalContent.lookup("#labelPontuacao");
            labelAtividadesRecentes = (Label) telaPrincipalContent.lookup("#labelAtividadesRecentes"); //Obtém a nova label.

            if (buttonVerMaisMetas != null) buttonVerMaisMetas.setOnAction(e -> mostrarTelaMeta());
            if (buttonVerMaisExercicios != null)
                buttonVerMaisExercicios.setOnAction(e -> mostrarTelaExercicio());
            if (buttonVerMaisDietas != null) buttonVerMaisDietas.setOnAction(e -> mostrarTelaDieta());
            if (buttonVerMaisPerfil != null) buttonVerMaisPerfil.setOnAction(e -> mostrarTelaPerfil());

            if (usuarioLogado != null) {
                setUsuarioLogado(usuarioLogado);
            } else {
                if (labelNomeUsuario != null) labelNomeUsuario.setText("Nome do Usuário");
                if (labelIMC != null) labelIMC.setText("IMC: --");
                if (labelIMCSituacao != null) labelIMCSituacao.setText("Situação: --");
                if (labelPesoUsuario != null) labelPesoUsuario.setText("Peso: --");
                if (labelAlturaUsuario != null) labelAlturaUsuario.setText("Altura: --");
                if (labelCaloriasDia != null) labelCaloriasDia.setText("Calorias: -- / --");
                if (barraProgressoMetas != null) {
                    barraProgressoMetas.progressProperty().unbind();
                    barraProgressoMetas.setProgress(0.0);
                }
                //Se o usuário não estiver logado, a pontuação some.
                if (labelPontuacao != null) {
                    labelPontuacao.setText("Pontuação: --");
                }
            }
            //REMOVIDO if (listViewAtividadesRecentes != null) {listViewAtividadesRecentes.setItems(atividadesRecentes);}
            atualizarAtividadesRecentes(); //Inicializa a lista na tela.

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int calcularTotalCaloriasConsumidas() {
        if (usuarioLogado == null) {
            return 0;
        }
        // Soma as calorias de *TODAS* as refeições do usuário, independentemente de dietas.
        int total = 0;
        List<Refeicao> todasRefeicoes = Fachada.getInstanciaUnica().listarRefeicoes();
        for(Refeicao r : todasRefeicoes) {
            total += r.getCalorias();
        }
        return total;
    }

    private void atualizarCalorias() {
        if (usuarioLogado != null) {
            int caloriasConsumidas = calcularTotalCaloriasConsumidas();
            if (labelCaloriasDia != null) {
                labelCaloriasDia.setText("Calorias: " + caloriasConsumidas + " / --"); //  Não tem mais dieta associada
            }
        } else {
            if (labelCaloriasDia != null) {
                labelCaloriasDia.setText("Calorias: --/--");
            }
        }
    }

    public double calcularProgressoGeralUsuario() {
        double progressoTotal = 0.0;
        int contadorMetas = 0;
        double totalCaloriasConsumidas = 0;

        if (usuarioLogado != null) {
            // Calcula progresso das metas
            if (usuarioLogado.getMetas() != null) {
                for (Meta meta : usuarioLogado.getMetas()) {
                    if (meta.getValorAlvo() > 0) {
                        double progressoMeta = (meta.getProgressoAtual() / meta.getValorAlvo());
                        progressoTotal += progressoMeta;
                        contadorMetas++;
                    }
                }
            }
        }

        if (contadorMetas > 0) {
            return progressoTotal / contadorMetas;
        } else {
            return 0.0;
        }
    }


    //Adiciona um exercício a lista
    public void adicionarExercicioRecente(Exercicio exercicio) {

        String nomeExercicio = exercicio.getNome();

        //Remove o exercício se ele já existir na lista.
        atividadesRecentes.remove(nomeExercicio);
        atividadesRecentes.add(0, nomeExercicio);

        if(atividadesRecentes.size() > 3){ // Agora, no máximo 3
            atividadesRecentes.remove(3); // Remove a partir do índice 3
        }
        atualizarAtividadesRecentes(); //Chama o metodo de atualizar a label

    }

    //Novo método para atualizar o texto na label:
    private void atualizarAtividadesRecentes() {
        if (labelAtividadesRecentes != null) {
            if (atividadesRecentes.isEmpty()) {
                labelAtividadesRecentes.setText("Nenhuma atividade recente.");
            } else {
                // Junta os nomes dos exercícios com vírgulas
                String texto = String.join(", ", atividadesRecentes); // <-- Mudança aqui
                labelAtividadesRecentes.setText(texto);
            }
        }
    }

    public void atualizarDadosTelaPrincipal() {
        if (usuarioLogado != null) {
            atualizarCalorias();
            progressoGeral.set(calcularProgressoGeralUsuario());
            if (telaDieta != null) ((DietaController) getController(telaDieta)).atualizarTabelaDietasUsuario();
            if (telaExercicio != null)
                ((ExercicioController) getController(telaExercicio)).atualizarTabelaExerciciosUsuario();
            if (telaMeta != null) ((MetaController) getController(telaMeta)).atualizarTabelaMetasUsuario();
            if (telaRefeicao != null)
                ((RefeicaoController) getController(telaRefeicao)).atualizarTabelaRefeicoesUsuario();
            if (telaTreino != null)
                ((TreinoController) getController(telaTreino)).atualizarTabelaTreinosUsuario();
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