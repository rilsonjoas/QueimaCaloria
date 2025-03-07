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
    @Setter private IBaseAdmin mainController;
    private ObservableList<Treino> listaTreinosPreDefinidos = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        System.out.println("AdminTreinosController.initialize() chamado");

        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaTipo.setCellValueFactory(new PropertyValueFactory<>("tipoDeTreino"));
        colunaDuracao.setCellValueFactory(new PropertyValueFactory<>("duracao"));
        colunaNivelDificuldade.setCellValueFactory(new PropertyValueFactory<>("nivelDeDificuldade"));

        campoTipo.setItems(FXCollections.observableArrayList(Exercicio.TipoExercicio.values()));

        carregarTreinosPreDefinidos();
        atualizarTabelaTreinos();

        // Listener para a ObservableList
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

        // Listener de seleção e preenchimento
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
        System.out.println("AdminTreinosController.carregarTreinosPreDefinidos() chamado"); // LOG
        List<Treino> treinos = fachada.getTreinosPreDefinidos();
        System.out.println("AdminTreinosController.carregarTreinosPreDefinidos(): Treinos pré-definidos carregados: " + treinos); // LOG
        listaTreinosPreDefinidos.addAll(treinos);
        System.out.println("AdminTreinosController.carregarTreinosPreDefinidos() finalizado");
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
            fachada.configurarTreino(novoTreino, nome, tipo, duracao, nivelDificuldade, null);
            atualizarTabelaTreinos();
            mensagem.setText("Treino criado com sucesso.");
        } catch (Exception e) {
            mensagem.setText("Erro ao criar treino: " + e.getMessage());
        }
    }

    @FXML
    public void atualizarTreino() {
        Treino treinoSelecionado = tabelaTreinos.getSelectionModel().getSelectedItem();
        if (treinoSelecionado != null) {
            try {
                String nome = campoNome.getText();
                Exercicio.TipoExercicio tipo = campoTipo.getValue();
                String duracaoStr = campoDuracao.getText();
                String nivelDificuldadeStr = campoNivelDificuldade.getText();
                int duracao = Integer.parseInt(duracaoStr);
                int nivelDificuldade = Integer.parseInt(nivelDificuldadeStr);

                fachada.configurarTreino(treinoSelecionado, nome, tipo, duracao, nivelDificuldade, null);
                atualizarTabelaTreinos();
                mensagem.setText("Treino atualizado com sucesso.");
            } catch (Exception e) {
                mensagem.setText("Erro ao atualizar treino: " + e.getMessage());
            }
        } else {
            mensagem.setText("Selecione um treino para atualizar.");
        }
    }

    @FXML
    public void removerTreino() {
        Treino treinoSelecionado = tabelaTreinos.getSelectionModel().getSelectedItem();
        if (treinoSelecionado != null) {
            try {
                fachada.removerTreino(treinoSelecionado.getId());
                atualizarTabelaTreinos();
                mensagem.setText("Treino removido com sucesso.");
            } catch (TreinoNaoEncontradoException e) {
                mensagem.setText("Erro ao remover treino: " + e.getMessage());
            }
        } else {
            mensagem.setText("Selecione um treino para remover.");
        }
    }

    private void atualizarTabelaTreinos() {
        System.out.println("AdminTreinosController.atualizarTabelaTreinos() chamado");  // LOG
        List<Treino> listaDeTreinos = fachada.listarTreinos();
        System.out.println("AdminTreinosController.atualizarTabelaTreinos(): Todos os treinos: " + listaDeTreinos); // LOG
        tabelaTreinos.setItems(FXCollections.observableArrayList(listaDeTreinos));
        //Verifica se a lista está vazia:
        if(listaDeTreinos.isEmpty()){
            System.out.println("AdminTreinosController: A lista de treinos está vazia.");
        }
    }

    private void preencherCampos(Treino treino){
        campoNome.setText(treino.getNome());
        campoTipo.setValue(treino.getTipoDeTreino());
        campoDuracao.setText(String.valueOf(treino.getDuracao()));
        campoNivelDificuldade.setText(String.valueOf(treino.getNivelDeDificuldade()));
    }
}