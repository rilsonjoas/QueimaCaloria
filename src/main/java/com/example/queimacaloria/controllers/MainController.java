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
import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException; // Importante
import com.example.queimacaloria.negocio.Dieta; // Importante

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
    @FXML private Label labelPontuacao;
    @FXML private Label labelAtividadesRecentes;

    //Novos elementos para o card de água
    @FXML private Label labelAguaConsumida;
    @FXML private Button buttonBeberAgua;
    @FXML private Button buttonZerarAgua;
    private static final int INCREMENTO_AGUA_ML = 200;

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

    public ObservableList<String> getAtividadesRecentes() {
        return this.atividadesRecentes;
    }

    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;

        if (usuarioLogado != null) {
            // Tenta obter a dieta ativa.  Se não existir, não faz nada.
            try {
                Dieta dietaAtiva = Fachada.getInstanciaUnica().getDietaAtiva(usuarioLogado);
                if (dietaAtiva != null) {
                    usuarioLogado.setDietaAtiva(dietaAtiva); // Define a dieta ativa no *próprio* usuário.
                }
            } catch (UsuarioNaoEncontradoException e) {
                // Tratar exceção, se necessário (logar, talvez).
                System.err.println("Usuário não encontrado ao buscar dieta ativa: " + e.getMessage());
            }


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

            if (labelPontuacao != null) {
                labelPontuacao.textProperty().bind(Bindings.createStringBinding(
                        () -> "Pontuação: " + usuarioLogado.getPontuacao(),
                        usuarioLogado.pontuacaoProperty()
                ));
            }

            // Binding para o label de água consumida.
            if (labelAguaConsumida != null) {
                labelAguaConsumida.textProperty().bind(Bindings.createStringBinding(
                        () -> "Água: " + usuarioLogado.getAguaConsumida() + " ml",
                        usuarioLogado.aguaConsumidaProperty() // Importante
                ));
            }

            usuarioLogado.getMetas().addListener((ListChangeListener<Meta>) change -> {
                System.out.println("MainController: Listener de metas disparado!");
                atualizarDadosTelaPrincipal();
            });
        }
        else { // Usuário deslogado -  Código *já existente*
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
            if (labelPontuacao != null) {
                labelPontuacao.setText("Pontuação: --");
            }
            if (labelAguaConsumida != null) {
                labelAguaConsumida.setText("Água: -- ml");
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
    // ... restante do código do MainController ...

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

    @FXML public void mostrarTelaDieta() { areaConteudo.getChildren().setAll(telaDieta); }
    @FXML public void mostrarTelaExercicio() { areaConteudo.getChildren().setAll(telaExercicio); }
    @FXML public void mostrarTelaMeta() { areaConteudo.getChildren().setAll(telaMeta); }
    @FXML public void mostrarTelaRefeicao() { areaConteudo.getChildren().setAll(telaRefeicao); }
    @FXML public void mostrarTelaTreino() { areaConteudo.getChildren().setAll(telaTreino); }
    @FXML public void mostrarTelaPerfil() {
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
            labelPontuacao = (Label) telaPrincipalContent.lookup("#labelPontuacao");
            labelAtividadesRecentes = (Label) telaPrincipalContent.lookup("#labelAtividadesRecentes");
            labelAguaConsumida = (Label) telaPrincipalContent.lookup("#labelAguaConsumida");
            buttonBeberAgua = (Button) telaPrincipalContent.lookup("#buttonBeberAgua");
            buttonZerarAgua = (Button) telaPrincipalContent.lookup("#buttonZerarAgua"); // Get reference


            if (buttonVerMaisMetas != null) buttonVerMaisMetas.setOnAction(e -> mostrarTelaMeta());
            if (buttonVerMaisExercicios != null)
                buttonVerMaisExercicios.setOnAction(e -> mostrarTelaExercicio());
            if (buttonVerMaisDietas != null) buttonVerMaisDietas.setOnAction(e -> mostrarTelaDieta());
            if (buttonVerMaisPerfil != null) buttonVerMaisPerfil.setOnAction(e -> mostrarTelaPerfil());

            if (buttonBeberAgua != null) {
                buttonBeberAgua.setOnAction(e -> {
                    if (usuarioLogado != null) {
                        try {
                            Fachada.getInstanciaUnica().beberAgua(usuarioLogado, INCREMENTO_AGUA_ML);
                            // O binding já cuida de atualizar a label.
                            //A fachada já está atualizando o usuário.
                        } catch (UsuarioNaoEncontradoException ex) {
                            //Tratar
                            System.err.println("Erro ao beber agua: " + ex.getMessage());
                        }
                    }
                });
            }

            //Adiciona a ação do novo botão
            if (buttonZerarAgua != null) {
                buttonZerarAgua.setOnAction(e -> {
                    if (usuarioLogado != null) {
                        try {
                            Fachada.getInstanciaUnica().zerarAgua(usuarioLogado);
                        } catch (UsuarioNaoEncontradoException ex) {
                            System.err.println("Erro ao zerar agua: " + ex.getMessage());
                        }
                    }
                });
            }

            if (usuarioLogado != null) {
                setUsuarioLogado(usuarioLogado);  // Corrigido!
            } else {
                if(labelNomeUsuario != null) labelNomeUsuario.setText("Nome do Usuário");
                if(labelIMC != null)  labelIMC.setText("IMC: --");
                if(labelIMCSituacao != null) labelIMCSituacao.setText("Situação: --");
                if(labelPesoUsuario != null) labelPesoUsuario.setText("Peso: --");
                if(labelAlturaUsuario != null) labelAlturaUsuario.setText("Altura: --");
                if(labelCaloriasDia != null) labelCaloriasDia.setText("Calorias: -- / --");
                if (barraProgressoMetas != null) {
                    barraProgressoMetas.progressProperty().unbind();
                    barraProgressoMetas.setProgress(0.0);
                }
                //Se o usuário não estiver logado, a pontuação some.
                if(labelPontuacao != null){
                    labelPontuacao.setText("Pontuação: --");
                }
            }
            atualizarAtividadesRecentes();

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
            int caloriasDiarias = 0; // Valor padrão

            try {
                Dieta dietaAtiva = Fachada.getInstanciaUnica().getDietaAtiva(usuarioLogado); //Obtém a dieta ativa
                if(dietaAtiva != null){
                    caloriasDiarias = dietaAtiva.getCaloriasDiarias();
                }
            } catch (UsuarioNaoEncontradoException e){
                //Lida com a exceção. Pode logar, mostrar uma mensagem, etc
                System.err.println("Usuário não encontrado ao buscar dieta ativa: " + e.getMessage());
            }


            if (labelCaloriasDia != null) {
                labelCaloriasDia.setText("Calorias: " + caloriasConsumidas + " / " + caloriasDiarias); //  Mostra as calorias
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
            atualizarAgua();
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
    private void atualizarAgua(){  //Não precisa mais.
        /*if (usuarioLogado != null) {
            if (labelAguaConsumida != null) { //Verificação extra
                labelAguaConsumida.setText("Água: " + usuarioLogado.getAguaConsumida() + " ml");
            }

        } else {
            if (labelAguaConsumida != null) {
                labelAguaConsumida.setText("Água: -- ml");
            }
        }
        */

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