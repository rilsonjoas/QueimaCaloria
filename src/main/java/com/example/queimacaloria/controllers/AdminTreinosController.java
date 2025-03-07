package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.TreinoNaoEncontradoException;
import com.example.queimacaloria.negocio.Exercicio;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Treino;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;
import com.example.queimacaloria.interfaces.IBaseAdmin;
import lombok.Setter;

public class AdminTreinosController {

    @FXML private TableView<Treino> tabelaTreinos;
    @FXML private TableColumn<Treino, String> colunaNome;
    @FXML private TableColumn<Treino, Exercicio.TipoExercicio> colunaTipo;
    @FXML private TableColumn<Treino, Integer> colunaDuracao;
    @FXML private TableColumn<Treino, Integer> colunaNivelDificuldade;

    @FXML private TextField campoNome;
    @FXML private ChoiceBox<Exercicio.TipoExercicio> campoTipo;
    @FXML private TextField campoDuracao;
    @FXML private TextField campoNivelDificuldade;
    @FXML private Label mensagem;

    private Fachada fachada = Fachada.getInstanciaUnica();
    @Setter private IBaseAdmin mainController; // Usado para comunicação.
    private ObservableList<Treino> listaTreinosPreDefinidos = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        // Configuração da tabela
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaTipo.setCellValueFactory(new PropertyValueFactory<>("tipoDeTreino"));
        colunaDuracao.setCellValueFactory(new PropertyValueFactory<>("duracao"));
        colunaNivelDificuldade.setCellValueFactory(new PropertyValueFactory<>("nivelDeDificuldade"));

        campoTipo.setItems(FXCollections.observableArrayList(Exercicio.TipoExercicio.values()));

        carregarTreinosPreDefinidos();
        atualizarTabelaTreinos();

        // Listener para a ObservableList (mudanças na lista da tabela)
        tabelaTreinos.getItems().addListener((javafx.collections.ListChangeListener.Change<? extends Treino> c) -> {
            System.out.println("AdminTreinosController: Mudança na lista da tabela detectada!");
            while (c.next()) {
                if (c.wasAdded()) {
                    System.out.println("  Itens adicionados: " + c.getAddedSubList());
                }
                if (c.wasRemoved()) {
                    System.out.println("  Itens removidos: " + c.getRemoved());
                }
            }
        });

        // Listener de seleção (item selecionado na tabela)
        tabelaTreinos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println("AdminTreinosController: Item selecionado: " + newSelection);
                preencherCampos(newSelection);
            }
        });
        //Verifica se tabelaTreinos é nula.
        if (tabelaTreinos == null) {
            System.err.println("Erro: tabelaTreinos é nula em AdminTreinosController.initialize()");
        }
    }

    private void carregarTreinosPreDefinidos() {
        List<Treino> treinos = fachada.getTreinosPreDefinidos();
        listaTreinosPreDefinidos.addAll(treinos);
        System.out.println("Treinos pré-definidos carregados.");
    }


    @FXML
    public void criarTreino() {
        String nome = campoNome.getText();
        Exercicio.TipoExercicio tipo = campoTipo.getValue();
        String duracaoStr = campoDuracao.getText();
        String nivelDificuldadeStr = campoNivelDificuldade.getText();

        try {
            int duracao = Integer.parseInt(duracaoStr);
            int nivelDificuldade = Integer.parseInt(nivelDificuldadeStr);

            Treino novoTreino = new Treino();
            novoTreino.setExercicios(new ArrayList<>());
            // A criação/configuração agora é *toda* feita via Fachada. Usuário e nível são nulos (tela de admin).
            fachada.configurarTreino(novoTreino, nome, tipo, duracao, nivelDificuldade, null, null);
            atualizarTabelaTreinos(); // A atualização é feita *após* a operação na Fachada.
            mensagem.setText("Treino criado com sucesso.");
        } catch (NumberFormatException e) {
            mensagem.setText("Erro: Duração e nível de dificuldade devem ser números inteiros.");
        } catch (TreinoNaoEncontradoException e) { // Captura a exceção específica
            mensagem.setText("Erro ao criar treino: " + e.getMessage());
        } catch (Exception e) { // Captura genérica para qualquer outra exceção.
            mensagem.setText("Erro ao criar treino: " + e.getMessage());
            e.printStackTrace(); //  Imprime o stack trace completo!  Muito importante.
        }
    }


    @FXML
    public void atualizarTreino() {
        Treino treinoSelecionado = tabelaTreinos.getSelectionModel().getSelectedItem();
        if (treinoSelecionado == null) {
            mensagem.setText("Selecione um treino para atualizar.");
            return;
        }

        try {
            String nome = campoNome.getText();
            Exercicio.TipoExercicio tipo = campoTipo.getValue();
            String duracaoStr = campoDuracao.getText();
            String nivelDificuldadeStr = campoNivelDificuldade.getText();
            int duracao = Integer.parseInt(duracaoStr);  // NumberFormatException
            int nivelDificuldade = Integer.parseInt(nivelDificuldadeStr); // NumberFormatException

            // Toda a lógica de negócio/atualização é feita na Fachada. Usuário e nível nulos (tela admin).
            fachada.configurarTreino(treinoSelecionado, nome, tipo, duracao, nivelDificuldade, null, null);
            atualizarTabelaTreinos(); // Atualiza a tabela *após* a operação na Fachada.
            mensagem.setText("Treino atualizado com sucesso.");
        } catch (NumberFormatException e) {
            mensagem.setText("Erro: Duração e nível de dificuldade devem ser números inteiros.");
        } catch (TreinoNaoEncontradoException e) {
            mensagem.setText("Erro ao atualizar treino: " + e.getMessage());
        }
        catch (Exception e) { // Captura genérica, para qualquer outro erro.
            mensagem.setText("Erro inesperado: " + e.getMessage());
            e.printStackTrace(); // Imprime o Stack Trace
        }
    }


    @FXML
    public void removerTreino() {
        Treino treinoSelecionado = tabelaTreinos.getSelectionModel().getSelectedItem();
        if (treinoSelecionado == null) {
            mensagem.setText("Selecione um treino para remover.");
            return;
        }

        try {
            // A remoção agora é feita *inteiramente* pela Fachada.
            fachada.removerTreino(treinoSelecionado.getId()); // Remove.
            atualizarTabelaTreinos(); // Atualiza *após* a remoção.
            mensagem.setText("Treino removido com sucesso.");
        } catch (TreinoNaoEncontradoException e) {
            mensagem.setText("Erro ao remover treino: " + e.getMessage());
        } catch (Exception e) { //  Captura genérica, para qualquer outro erro.
            mensagem.setText("Erro inesperado: " + e.getMessage());
            e.printStackTrace(); //  Imprime o stack trace completo.
        }
    }


    private void atualizarTabelaTreinos() {
        List<Treino> listaDeTreinos = fachada.listarTreinos();
        tabelaTreinos.setItems(FXCollections.observableArrayList(listaDeTreinos));
        tabelaTreinos.refresh();
    }

    private void preencherCampos(Treino treino){
        campoNome.setText(treino.getNome());
        campoTipo.setValue(treino.getTipoDeTreino());
        campoDuracao.setText(String.valueOf(treino.getDuracao()));
        campoNivelDificuldade.setText(String.valueOf(treino.getNivelDeDificuldade()));
    }
}