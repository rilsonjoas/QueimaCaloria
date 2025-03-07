package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;
import com.example.queimacaloria.interfaces.IBaseAdmin;
import com.example.queimacaloria.negocio.*;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javafx.stage.Stage;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainController implements IBaseAdmin {

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
    @FXML private Label labelAguaConsumida;
    @FXML private Button buttonBeberAgua;
    @FXML private Button buttonZerarAgua;

    @FXML private Label labelAdmin;
    @FXML private Button buttonGerenciarUsuarios;
    @FXML private Label labelNumeroUsuarios;

    @FXML private LineChart<String, Number> graficoHistoricoPeso;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;

    private static final int INCREMENTO_AGUA_ML = 200;
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Parent telaDieta;
    private Parent telaExercicio;
    private Parent telaMeta;
    private Parent telaRefeicao;
    private Parent telaTreino;
    private Parent telaPerfil;
    private Parent telaAdminUsuarios;

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

    private void atualizarGraficoPeso() {
        if (usuarioLogado != null && graficoHistoricoPeso != null) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Peso");

            List<PesoRegistro> historicoOrdenado = new ArrayList<>(usuarioLogado.getHistoricoPeso());
            historicoOrdenado.sort((r1, r2) -> r1.getData().compareTo(r2.getData()));

            for (PesoRegistro registro : historicoOrdenado) {
                String dataFormatada = registro.getData().format(dateFormatter);
                series.getData().add(new XYChart.Data<>(dataFormatada, registro.getPeso()));
            }

            graficoHistoricoPeso.getData().clear();
            graficoHistoricoPeso.getData().add(series);
            graficoHistoricoPeso.setLegendVisible(false);
        }
    }

    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;

        if (usuarioLogado != null) {

            if (labelNomeUsuario != null) labelNomeUsuario.setText(usuarioLogado.getNome());
            if (labelPesoUsuario != null)
                labelPesoUsuario.textProperty().bind(Bindings.createStringBinding(() -> String.format("Peso: %.2f kg", usuarioLogado.getPeso()), usuarioLogado.pesoProperty()));
            if (labelAlturaUsuario != null)
                labelAlturaUsuario.textProperty().bind(Bindings.createStringBinding(
                        () -> String.format("Altura: %.0f cm", usuarioLogado.getAltura()),
                        usuarioLogado.alturaProperty()));
            if (labelIMC != null)
                labelIMC.textProperty().bind(Bindings.createStringBinding(() -> String.format("IMC: %.2f", usuarioLogado.getImc()), usuarioLogado.imcProperty()));
            if (labelIMCSituacao != null)
                labelIMCSituacao.textProperty().bind(Bindings.createStringBinding(() -> "Situação: " + getSituacaoIMC(usuarioLogado.getImc()), usuarioLogado.imcProperty()));

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

            if (labelAguaConsumida != null) {
                labelAguaConsumida.textProperty().bind(Bindings.createStringBinding(
                        () -> "Água: " + usuarioLogado.getAguaConsumida() + " ml",
                        usuarioLogado.aguaConsumidaProperty()
                ));
            }

            boolean isAdmin = usuarioLogado.getTipo().equals(Usuario.TipoUsuario.ADMINISTRADOR.getDescricao());
            if(labelAdmin != null) labelAdmin.setVisible(isAdmin);
            if(buttonGerenciarUsuarios != null) buttonGerenciarUsuarios.setVisible(isAdmin);
            if(labelNumeroUsuarios != null) {
                labelNumeroUsuarios.setVisible(isAdmin);
                atualizarNumeroUsuarios();
            }

            // Listeners para atualização automática
            usuarioLogado.getMetas().addListener((ListChangeListener<Meta>) change -> {
                atualizarDadosTelaPrincipal();
                while (change.next()) {
                    if (change.wasAdded()) {
                        for (Meta meta : change.getAddedSubList()) {
                            meta.progressoAtualProperty().addListener((obs, oldVal, newVal) -> atualizarDadosTelaPrincipal());
                            meta.dataConclusaoProperty().addListener((obs, oldVal, newVal) -> atualizarDadosTelaPrincipal());
                        }
                    }
                }
            });

            usuarioLogado.pesoProperty().addListener((obs, oldVal, newVal) -> {
                atualizarGraficoPeso();
                atualizarDadosTelaPrincipal();
            });

            usuarioLogado.alturaProperty().addListener((obs, oldVal, newVal) -> atualizarDadosTelaPrincipal());
            usuarioLogado.aguaConsumidaProperty().addListener((obs, oldVal, newVal) -> atualizarDadosTelaPrincipal());
            usuarioLogado.pontuacaoProperty().addListener((obs, oldVal, newVal) -> atualizarDadosTelaPrincipal());
            usuarioLogado.getHistoricoPeso().addListener((ListChangeListener<PesoRegistro>) change -> atualizarGraficoPeso());

            usuarioLogado.getDietas().addListener((ListChangeListener<Dieta>) change -> atualizarDadosTelaPrincipal());
            usuarioLogado.getExercicios().addListener((ListChangeListener<Exercicio>) change -> atualizarDadosTelaPrincipal());
            usuarioLogado.getTreinos().addListener((ListChangeListener<Treino>) change -> atualizarDadosTelaPrincipal());

            atualizarDadosTelaPrincipal();

        } else {
            // Limpar os campos caso não haja usuário logado
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
            if (labelProgressoMetas != null) labelProgressoMetas.setText("0.0% Completo");
            if (labelPontuacao != null) labelPontuacao.setText("Pontuação: --");
            if (labelAguaConsumida != null) labelAguaConsumida.setText("Água: -- ml");
            if (graficoHistoricoPeso != null) graficoHistoricoPeso.getData().clear();
            if (labelAdmin != null) labelAdmin.setVisible(false);
            if (buttonGerenciarUsuarios != null) buttonGerenciarUsuarios.setVisible(false);
            if (labelNumeroUsuarios != null) labelNumeroUsuarios.setVisible(false);
        }
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
            telaAdminUsuarios = carregarTela("/com/example/queimacaloria/views/admin-usuarios-view.fxml");

            ((DietaController) getController(telaDieta)).setMainController(this);
            ((ExercicioController) getController(telaExercicio)).setMainController(this);
            ((MetaController) getController(telaMeta)).setMainController(this);
            ((RefeicaoController) getController(telaRefeicao)).setMainController(this);
            ((TreinoController) getController(telaTreino)).setMainController(this);
            ((PerfilController) getController(telaPerfil)).setMainController(this);
            ((AdminUsuariosController) getController(telaAdminUsuarios)).setMainController(this);
            Fachada.getInstanciaUnica().setMainController(this);
            Fachada.getInstanciaUnica().registrarObservadorRefeicoes(this::atualizarDadosTelaPrincipal);

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
    @FXML public void mostrarTelaAdminUsuarios() {
        AdminUsuariosController adminUsuariosController = (AdminUsuariosController) getController(telaAdminUsuarios);
        adminUsuariosController.setUsuarioLogado(usuarioLogado);
        areaConteudo.getChildren().setAll(telaAdminUsuarios);
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
            buttonZerarAgua = (Button) telaPrincipalContent.lookup("#buttonZerarAgua");
            labelAdmin = (Label) telaPrincipalContent.lookup("#labelAdmin");
            buttonGerenciarUsuarios = (Button) telaPrincipalContent.lookup("#buttonGerenciarUsuarios");
            labelNumeroUsuarios = (Label) telaPrincipalContent.lookup("#labelNumeroUsuarios");
            graficoHistoricoPeso = (LineChart<String, Number>) telaPrincipalContent.lookup("#graficoHistoricoPeso");
            xAxis = (CategoryAxis) telaPrincipalContent.lookup("#xAxis");
            yAxis = (NumberAxis) telaPrincipalContent.lookup("#yAxis");


            if (buttonVerMaisMetas != null) buttonVerMaisMetas.setOnAction(e -> mostrarTelaMeta());
            if (buttonVerMaisExercicios != null) buttonVerMaisExercicios.setOnAction(e -> mostrarTelaExercicio());
            if (buttonVerMaisDietas != null) buttonVerMaisDietas.setOnAction(e -> mostrarTelaDieta());
            if (buttonVerMaisPerfil != null) buttonVerMaisPerfil.setOnAction(e -> mostrarTelaPerfil());
            if (buttonGerenciarUsuarios != null) buttonGerenciarUsuarios.setOnAction(e -> mostrarTelaAdminUsuarios());

            if (buttonBeberAgua != null) {
                buttonBeberAgua.setOnAction(e -> {
                    if (usuarioLogado != null) {
                        try {
                            Fachada.getInstanciaUnica().beberAgua(usuarioLogado, INCREMENTO_AGUA_ML);
                        } catch (UsuarioNaoEncontradoException ex) {
                            System.err.println("Erro ao beber agua: " + ex.getMessage());
                        }
                    }
                });
            }

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
                setUsuarioLogado(usuarioLogado);
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
                if(labelAdmin != null) labelAdmin.setVisible(false);
                if(buttonGerenciarUsuarios != null) buttonGerenciarUsuarios.setVisible(false);
                if(labelNumeroUsuarios != null) labelNumeroUsuarios.setVisible(false);

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

        try {
            Dieta dietaAtiva = Fachada.getInstanciaUnica().getDietaAtiva(usuarioLogado);
            if (dietaAtiva == null) {
                return 0;
            }

            int total = 0;
            List<Refeicao> todasRefeicoes = Fachada.getInstanciaUnica().listarRefeicoes();

            for (Refeicao r : todasRefeicoes) {
                if (r.getUsuario() != null && r.getUsuario().getId().equals(usuarioLogado.getId())) {
                    total += r.getCalorias();
                }
            }
            return total;


        } catch (UsuarioNaoEncontradoException e) {
            System.err.println("Usuário não encontrado ao calcular calorias consumidas: " + e.getMessage());
            return 0;
        }
    }

    void atualizarCalorias() {
        if (usuarioLogado == null || labelCaloriasDia == null) {
            if (labelCaloriasDia != null) {
                labelCaloriasDia.setText("Calorias: --/--");
            }
            return;
        }

        int caloriasConsumidas = calcularTotalCaloriasConsumidas();
        int caloriasDiarias = 0;

        try {
            Dieta dietaAtiva = Fachada.getInstanciaUnica().getDietaAtiva(usuarioLogado);
            if (dietaAtiva != null) {
                caloriasDiarias = dietaAtiva.getCaloriasDiarias();
            }
        } catch (UsuarioNaoEncontradoException e) {
            System.err.println("Usuário não encontrado ao buscar dieta ativa: " + e.getMessage());
        }

        labelCaloriasDia.setText("Calorias: " + caloriasConsumidas + " / " + caloriasDiarias);
    }


    public double calcularProgressoGeralUsuario() {
        double progressoTotal = 0.0;
        int contadorMetas = 0;

        if (usuarioLogado != null && usuarioLogado.getMetas() != null) {
            for (Meta meta : usuarioLogado.getMetas()) {
                if (meta.getUsuario() != null && meta.getUsuario().getId().equals(usuarioLogado.getId())) {
                    if (meta.getValorAlvo() > 0) {
                        progressoTotal += (meta.getProgressoAtual() / meta.getValorAlvo());
                        contadorMetas++;
                    }
                }
            }
        }
        return (contadorMetas > 0) ? progressoTotal / contadorMetas : 0.0;
    }

    public void adicionarExercicioRecente(Exercicio exercicio) {

        String nomeExercicio = exercicio.getNome();

        atividadesRecentes.remove(nomeExercicio);
        atividadesRecentes.add(0, nomeExercicio);

        if(atividadesRecentes.size() > 3){
            atividadesRecentes.remove(3);
        }
        atualizarAtividadesRecentes();

    }

    private void atualizarAtividadesRecentes() {
        if (labelAtividadesRecentes != null) {
            if (atividadesRecentes.isEmpty()) {
                labelAtividadesRecentes.setText("Nenhuma atividade recente.");
            } else {
                String texto = String.join(", ", atividadesRecentes);
                labelAtividadesRecentes.setText(texto);
            }
        }
    }

    public void atualizarDadosTelaPrincipal() {
        System.out.println("MainController.atualizarDadosTelaPrincipal() chamado");
        if (usuarioLogado != null) {
            try {
                Dieta dietaAtiva = Fachada.getInstanciaUnica().getDietaAtiva(usuarioLogado);
                if (dietaAtiva != null) {
                    usuarioLogado.setDietaAtiva(dietaAtiva);
                }
            }
            catch (UsuarioNaoEncontradoException e){
                System.err.println("Usuário não encontrado ao buscar dieta ativa: " + e.getMessage());
            }

            atualizarCalorias();
            atualizarGraficoPeso();
            progressoGeral.set(calcularProgressoGeralUsuario());

            if (telaDieta != null) {
                ((DietaController) getController(telaDieta)).atualizarTabelaDietasUsuario();
            }
            if (telaExercicio != null) {
                ((ExercicioController) getController(telaExercicio)).atualizarTabelaExerciciosUsuario();
            }
            if (telaMeta != null) {
                ((MetaController) getController(telaMeta)).atualizarTabelaMetasUsuario();
            }
            if (telaRefeicao != null) {
                ((RefeicaoController) getController(telaRefeicao)).atualizarTabelaRefeicoesUsuario();
            }
            if (telaTreino != null) {
                ((TreinoController) getController(telaTreino)).atualizarTabelaTreinosUsuario();
            }

            boolean isAdmin = usuarioLogado.getTipo().equals(Usuario.TipoUsuario.ADMINISTRADOR.getDescricao());
            if(labelAdmin != null) labelAdmin.setVisible(isAdmin);
            if(buttonGerenciarUsuarios != null) buttonGerenciarUsuarios.setVisible(isAdmin);
            if(labelNumeroUsuarios != null) {
                labelNumeroUsuarios.setVisible(isAdmin);
                atualizarNumeroUsuarios();
            }
        }
    }

    private void atualizarNumeroUsuarios() {
        if (labelNumeroUsuarios != null) {
            int totalUsuarios = Fachada.getInstanciaUnica().listarUsuarios().size();
            labelNumeroUsuarios.setText("Total de Usuários: " + totalUsuarios);
        }
    }

    private void atualizarAgua(){

    }

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
}