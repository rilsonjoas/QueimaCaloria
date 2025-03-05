package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.TreinoNaoEncontradoException;
import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;
import com.example.queimacaloria.negocio.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors; // Importante para o filtro!

public class TreinoController {

    @FXML private TableView<Treino> tabelaTreinosUsuario;
    @FXML private TableColumn<Treino, String> colunaNomeUsuario;
    @FXML private TableColumn<Treino, Exercicio.TipoExercicio> colunaTipoTreinoUsuario;
    @FXML private TableColumn<Treino, Integer> colunaDuracaoUsuario;
    @FXML private TableColumn<Treino, Integer> colunaNivelDificuldadeUsuario;

    @FXML private TableView<Treino> tabelaTreinosPreDefinidos;
    @FXML private TableColumn<Treino, String> colunaNomePreDefinido;
    @FXML private TableColumn<Treino, Exercicio.TipoExercicio> colunaTipoTreinoPreDefinido;
    @FXML private TableColumn<Treino, Integer> colunaDuracaoPreDefinido;
    @FXML private TableColumn<Treino, Integer> colunaNivelDificuldadePreDefinido;

    @FXML private Label mensagemTreino;
    @FXML private Label labelNomeTreino;
    @FXML private Label labelTipoTreino;
    @FXML private Label labelDuracaoTreino;
    @FXML private Label labelDificuldadeTreino;
    @FXML private Label labelExerciciosTreino;

    private Fachada fachada = Fachada.getInstanciaUnica();
    private MainController mainController;
    private ObservableList<Treino> treinosPreDefinidos = FXCollections.observableArrayList();

    // Botão de compartilhar
    @FXML private Button buttonCompartilhar;


    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        configurarTabelaUsuario();
        atualizarTabelaTreinosUsuario();
        configurarTabelaPreDefinida();
        carregarTreinosPreDefinidos();

        tabelaTreinosUsuario.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    exibirDetalhesTreino(newValue);
                }
        );

        //Verifica se o botão compartilhar está presente antes de configurar o evento.
        if(buttonCompartilhar != null){
            buttonCompartilhar.setOnAction(event -> compartilharLista());
        }
    }

    private void configurarTabelaUsuario() {
        colunaNomeUsuario.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaTipoTreinoUsuario.setCellValueFactory(new PropertyValueFactory<>("tipoDeTreino"));
        colunaDuracaoUsuario.setCellValueFactory(new PropertyValueFactory<>("duracao"));
        colunaNivelDificuldadeUsuario.setCellValueFactory(new PropertyValueFactory<>("nivelDeDificuldade"));
    }

    private void configurarTabelaPreDefinida() {
        colunaNomePreDefinido.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaTipoTreinoPreDefinido.setCellValueFactory(new PropertyValueFactory<>("tipoDeTreino"));
        colunaDuracaoPreDefinido.setCellValueFactory(new PropertyValueFactory<>("duracao"));
        colunaNivelDificuldadePreDefinido.setCellValueFactory(new PropertyValueFactory<>("nivelDeDificuldade"));
    }

    private void carregarTreinosPreDefinidos() {
        // Agora carrega diretamente de InicializadorDados, SEM Fachada.
        treinosPreDefinidos.setAll(InicializadorDados.inicializarTreinos());
        tabelaTreinosPreDefinidos.setItems(treinosPreDefinidos);
    }

    @FXML
    public void abrirTelaCriarTreino() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/criacao-treino-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Criar Novo Treino");
            stage.setScene(scene);

            CriacaoTreinoController controller = loader.getController();
            controller.setTreinoController(this);
            controller.setMainController(mainController);

            stage.showAndWait();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao abrir tela", e.getMessage());
        }
    }

    @FXML
    public void atualizarTreino() {
        Treino treinoSelecionado = tabelaTreinosUsuario.getSelectionModel().getSelectedItem();
        if (treinoSelecionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/edicao-treino-view.fxml"));
                Parent root = loader.load();

                EdicaoTreinoController controller = loader.getController();
                controller.setTreinoController(this);
                controller.setMainController(mainController);
                System.out.println("TreinoController.atualizarTreino(): Treino selecionado ID: " + treinoSelecionado.getId());
                controller.setTreino(treinoSelecionado);

                Stage stage = new Stage();
                stage.setTitle("Editar Treino");
                stage.setScene(new Scene(root));
                stage.showAndWait();

            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao abrir tela de edição", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhum treino selecionado",
                    "Por favor, selecione um treino para atualizar.");
        }
    }
    @FXML
    public void removerTreino() {
        Treino treinoSelecionado = tabelaTreinosUsuario.getSelectionModel().getSelectedItem();
        if (treinoSelecionado != null) {
            try {
                fachada.removerTreino(treinoSelecionado.getId());
                atualizarTabelaTreinosUsuario();
                mensagemTreino.setText("Treino removido com sucesso!");

                if (mainController != null && mainController.getUsuarioLogado() != null) {
                    try {
                        Usuario usuarioAtualizado = fachada.buscarUsuarioPorId(mainController.getUsuarioLogado().getId());
                        mainController.setUsuarioLogado(usuarioAtualizado);
                    } catch (UsuarioNaoEncontradoException e) {
                        showAlert(Alert.AlertType.ERROR, "Erro", "Usuário não encontrado.",
                                "O usuário logado não pôde ser encontrado após a remoção do treino.");
                    }
                }

            } catch (TreinoNaoEncontradoException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao remover treino", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhum treino selecionado",
                    "Por favor, selecione um treino para remover.");
        }
    }
    @FXML
    public void adicionarTreinoPreDefinido() {
        Treino treinoSelecionado = tabelaTreinosPreDefinidos.getSelectionModel().getSelectedItem();
        if (treinoSelecionado != null) {
            try {
                Treino novoTreino = new Treino(
                        treinoSelecionado.getNome(),
                        treinoSelecionado.getTipoDeTreino(),
                        treinoSelecionado.getDuracao(),
                        treinoSelecionado.getNivelDeDificuldade(),
                        new ArrayList<>(treinoSelecionado.getExercicios()),
                        treinoSelecionado.getCaloriasQueimadas(),
                        treinoSelecionado.getProgresso(),
                        treinoSelecionado.isConcluido()
                );

                fachada.configurarTreino(novoTreino, novoTreino.getNome(),
                        novoTreino.getTipoDeTreino(), novoTreino.getDuracao(),
                        novoTreino.getNivelDeDificuldade());
                atualizarTabelaTreinosUsuario();
                mensagemTreino.setText("Treino adicionado com sucesso!");

                if (mainController != null && mainController.getUsuarioLogado() != null) {
                    try {
                        Usuario usuarioAtualizado = fachada.buscarUsuarioPorId(mainController.getUsuarioLogado().getId());
                        fachada.adicionarTreinoAoUsuario(usuarioAtualizado, novoTreino);
                        mainController.setUsuarioLogado(usuarioAtualizado);
                    } catch (UsuarioNaoEncontradoException e) {
                        showAlert(Alert.AlertType.ERROR, "Erro", "Usuário não encontrado.",
                                "O usuário logado não pôde ser encontrado após a adição do treino.");
                    }
                }

                // Adiciona a sugestão de exercícios *após* adicionar o treino
                sugerirExercicios(novoTreino);


            } catch (TreinoNaoEncontradoException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao adicionar treino", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhum treino selecionado",
                    "Por favor, selecione um treino pré-definido para adicionar.");
        }
    }

    public void atualizarTabelaTreinosUsuario() {
        try {

            if(mainController != null && mainController.getUsuarioLogado() != null){
                Usuario usuarioLogado = mainController.getUsuarioLogado();
                List<Treino> listaTreinos = fachada.listarTreinos();

                //FILTRO: Mostrar apenas os treinos do usuário logado
                listaTreinos = listaTreinos.stream().filter(treino -> treino.getUsuario() != null && treino.getUsuario().getId.equals(usuarioLogado.getId()))
                        .collect(Collectors.toList());
                tabelaTreinosUsuario.setItems(FXCollections.observableArrayList(listaTreinos));
                tabelaTreinosUsuario.refresh();

                if (!tabelaTreinosUsuario.getItems().isEmpty()) {
                    tabelaTreinosUsuario.getSelectionModel().select(0);
                    exibirDetalhesTreino(tabelaTreinosUsuario.getItems().get(0));
                } else {
                    exibirDetalhesTreino(null);
                }
            }
            else {
                // Trata caso não haja usuário logado (opcional).
                tabelaTreinosUsuario.setItems(FXCollections.observableArrayList());
                System.err.println("TreinoController: Nenhum usuário logado ao atualizar a tabela.");
            }

        }

        catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar treinos", e.getMessage());
        }
    }

    private void exibirDetalhesTreino(Treino treino) {
        if (treino != null) {
            if(labelNomeTreino != null) labelNomeTreino.setText("Nome: " + treino.getNome());
            if(labelTipoTreino != null) labelTipoTreino.setText("Tipo: " + treino.getTipoDeTreino());
            if(labelDuracaoTreino != null) labelDuracaoTreino.setText("Duração: " + treino.getDuracao() + " min");
            if(labelDificuldadeTreino != null) labelDificuldadeTreino.setText("Dificuldade: " + treino.getNivelDeDificuldade());

            if (treino.getExercicios() != null && !treino.getExercicios().isEmpty()) {
                String exerciciosStr = treino.getExercicios().stream()
                        .map(Exercicio::getNome)
                        .collect(Collectors.joining(", "));
                if(labelExerciciosTreino != null) labelExerciciosTreino.setText("Exercícios: " + exerciciosStr);
            } else {
                if(labelExerciciosTreino != null) labelExerciciosTreino.setText("Exercícios: Nenhum");
            }
        } else {
            if(labelNomeTreino != null) labelNomeTreino.setText("Nome: ");
            if(labelTipoTreino != null) labelTipoTreino.setText("Tipo: ");
            if(labelDuracaoTreino != null) labelDuracaoTreino.setText("Duração: ");
            if(labelDificuldadeTreino != null) labelDificuldadeTreino.setText("Dificuldade: ");
            if(labelExerciciosTreino != null) labelExerciciosTreino.setText("Exercícios: ");
        }
    }
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    public void voltarParaTelaPrincipal() {
        if (mainController != null) {
            mainController.mostrarTelaPrincipal();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro interno", "MainController não foi configurado corretamente.");
        }
    }

    @FXML
    public void compartilharLista() {
        if (mainController != null && mainController.getUsuarioLogado() != null) {
            //Obtém a lista de treinos do usuário.
            List<Treino> treinosDoUsuario = mainController.getUsuarioLogado().getTreinos();
            String nomeUsuario = mainController.getUsuarioLogado().getNome(); // Obtém o nome do usuário

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salvar Relatório de Treinos em PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos PDF", "*.pdf"));
            Stage stage = (Stage) tabelaTreinosUsuario.getScene().getWindow(); // Usando a tabela
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                try {
                    // Chama o método específico para treinos, passando o nome do usuário
                    GeradorPDF.gerarRelatorioTreinos(treinosDoUsuario, file.getAbsolutePath(), nomeUsuario);
                    showAlert(Alert.AlertType.INFORMATION, "Sucesso!", "Relatório Gerado",
                            "O relatório de treinos foi gerado com sucesso em: " + file.getAbsolutePath());

                } catch (IOException e) { // Captura IOException
                    showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao gerar relatório", "Erro de I/O: " + e.getMessage());
                    e.printStackTrace();
                } catch (Exception e) { //Apenas o catch genérico.
                    showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao gerar relatório", "Erro inesperado: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Usuário Não Logado", "É necessário estar logado para gerar o relatório.");
        }
    }

    // Método para sugerir exercícios (NOVO)
    protected void sugerirExercicios(Treino treino) {
        List<Exercicio> todosExercicios = InicializadorDados.inicializarExercicios();
        List<Exercicio> exerciciosSugeridos = new ArrayList<>();
        Random random = new Random();

        if (treino.getTipoDeTreino() == Exercicio.TipoExercicio.FORCA) {
            for (Exercicio ex : todosExercicios) {
                if (ex.getTipo() == Exercicio.TipoExercicio.FORCA) {
                    exerciciosSugeridos.add(ex);
                }
            }
        } else if (treino.getTipoDeTreino() == Exercicio.TipoExercicio.CARDIO) {
            for (Exercicio ex : todosExercicios) {
                if (ex.getTipo() == Exercicio.TipoExercicio.CARDIO) {
                    exerciciosSugeridos.add(ex);
                }
            }
        }
        //Adicione else if para os outros tipos de exercício.
        else if (treino.getTipoDeTreino() == Exercicio.TipoExercicio.FLEXIBILIDADE){
            for(Exercicio ex : todosExercicios){
                if(ex.getTipo() == Exercicio.TipoExercicio.FLEXIBILIDADE){
                    exerciciosSugeridos.add(ex);
                }
            }
        }
        else if (treino.getTipoDeTreino() == Exercicio.TipoExercicio.EQUILIBRIO){
            for(Exercicio ex : todosExercicios){
                if(ex.getTipo() == Exercicio.TipoExercicio.EQUILIBRIO){
                    exerciciosSugeridos.add(ex);
                }
            }
        }

        else if (treino.getTipoDeTreino() == Exercicio.TipoExercicio.AQUATICO){
            for(Exercicio ex : todosExercicios){
                if(ex.getTipo() == Exercicio.TipoExercicio.AQUATICO){
                    exerciciosSugeridos.add(ex);
                }
            }
        }
        else if (treino.getTipoDeTreino() == Exercicio.TipoExercicio.OUTRO){
            for(Exercicio ex : todosExercicios){
                if(ex.getTipo() == Exercicio.TipoExercicio.OUTRO){
                    exerciciosSugeridos.add(ex);
                }
            }
        }
        // Se não for um tipo específico, sugere alguns aleatórios.
        else {
            if (todosExercicios.size() > 3) {
                for (int i = 0; i < 3; i++) { // Sugere 3 exercícios
                    exerciciosSugeridos.add(todosExercicios.get(random.nextInt(todosExercicios.size())));
                }
            } else {
                exerciciosSugeridos.addAll(todosExercicios);
            }
        }


        if (!exerciciosSugeridos.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Com base no seu treino de ").append(treino.getTipoDeTreino()).append(", sugerimos os seguintes exercícios:\n\n");
            for (Exercicio ex : exerciciosSugeridos) {
                sb.append("- ").append(ex.getNome()).append(" (").append(ex.getTipo()).append(")\n");
            }

            //Sugerir uma frequência de treino aleatória
            int frequencia = 2 + random.nextInt(4); //Gera números entre 2 e 5
            sb.append("\nRecomendamos realizar este treino de ").append(frequencia).append(" vezes por semana.");


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sugestão de Exercícios");
            alert.setHeaderText(null);
            alert.setContentText(sb.toString());
            alert.showAndWait();
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Informação", "Não há exercícios sugeridos",
                    "Não encontramos exercícios para sugerir com base no tipo do seu treino.");
        }
    }
}