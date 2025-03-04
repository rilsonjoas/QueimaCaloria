package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.TreinoNaoEncontradoException;
import com.example.queimacaloria.negocio.Exercicio;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.InicializadorDados;
import com.example.queimacaloria.negocio.Treino;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;
import com.example.queimacaloria.interfaces.IBaseAdmin;  // Importe a interface
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

    public void setMainController(AdminMainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaTipo.setCellValueFactory(new PropertyValueFactory<>("tipoDeTreino"));
        colunaDuracao.setCellValueFactory(new PropertyValueFactory<>("duracao"));
        colunaNivelDificuldade.setCellValueFactory(new PropertyValueFactory<>("nivelDeDificuldade"));

        campoTipo.setItems(FXCollections.observableArrayList(Exercicio.TipoExercicio.values()));

        tabelaTreinos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                preencherCamposComTreinoSelecionado(newSelection);
            } else {
                limparCampos();
            }
        });

        carregarTreinosPreDefinidos();
        atualizarTabelaTreinos();
    }

    private void limparCampos() {
        campoNome.setText("");
        campoTipo.setValue(null);
        campoDuracao.setText("");
        campoNivelDificuldade.setText("");
    }

    private void preencherCamposComTreinoSelecionado(Treino treino) {
        campoNome.setText(treino.getNome());
        campoTipo.setValue(treino.getTipoDeTreino());
        campoDuracao.setText(String.valueOf(treino.getDuracao()));
        campoNivelDificuldade.setText(String.valueOf(treino.getNivelDeDificuldade()));
    }

    private void carregarTreinosPreDefinidos() {
        List<Treino> treinos = fachada.getTreinosPreDefinidos();
        listaTreinosPreDefinidos.addAll(treinos);
        tabelaTreinos.setItems(FXCollections.observableArrayList(listaTreinosPreDefinidos));
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
            fachada.configurarTreino(novoTreino, nome, tipo, duracao, nivelDificuldade);
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

                fachada.configurarTreino(treinoSelecionado, nome, tipo, duracao, nivelDificuldade);
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
        List<Treino> listaDeTreinos = fachada.listarTreinos();
        tabelaTreinos.setItems(FXCollections.observableArrayList(listaDeTreinos));
    }
}