package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.ExercicioNaoEncontradoException;
import com.example.queimacaloria.negocio.Exercicio;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.InicializadorDados;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;
import com.example.queimacaloria.interfaces.IBaseAdmin;  // Importe a interface
import lombok.Setter;

public class AdminExerciciosController {

    @FXML private TableView<Exercicio> tabelaExercicios;
    @FXML private TableColumn<Exercicio, String> colunaNome;
    @FXML private TableColumn<Exercicio, Exercicio.TipoExercicio> colunaTipo;
    @FXML private TableColumn<Exercicio, Integer> colunaTempo;
    @FXML private TableColumn<Exercicio, Double> colunaCalorias;

    @FXML private TextField campoNome;
    @FXML private TextField campoDescricao;
    @FXML private ChoiceBox<Exercicio.TipoExercicio> campoTipo;
    @FXML private TextField campoTempo;
    @FXML private TextField campoCalorias;
    @FXML private Label mensagem;

    private Fachada fachada = Fachada.getInstanciaUnica();
    @Setter private IBaseAdmin mainController;
    private ObservableList<Exercicio> listaExerciciosPreDefinidos = FXCollections.observableArrayList();


    public void setMainController(AdminMainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colunaTempo.setCellValueFactory(new PropertyValueFactory<>("tempo"));
        colunaCalorias.setCellValueFactory(new PropertyValueFactory<>("caloriasQueimadas"));

        campoTipo.setItems(FXCollections.observableArrayList(Exercicio.TipoExercicio.values()));

        tabelaExercicios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                preencherCamposComExercicioSelecionado(newSelection);
            } else {
                limparCampos();
            }
        });

        carregarExerciciosPreDefinidos();
        atualizarTabelaExercicios();
    }

    private void limparCampos() {
        campoNome.setText("");
        campoDescricao.setText("");
        campoTipo.setValue(null);
        campoTempo.setText("");
        campoCalorias.setText("");
    }

    private void preencherCamposComExercicioSelecionado(Exercicio exercicio) {
        campoNome.setText(exercicio.getNome());
        campoDescricao.setText(exercicio.getDescricao());
        campoTipo.setValue(exercicio.getTipo());
        campoTempo.setText(String.valueOf(exercicio.getTempo()));
        campoCalorias.setText(String.valueOf(exercicio.getCaloriasQueimadas()));
    }

    private void carregarExerciciosPreDefinidos() {
        List<Exercicio> exercicios = fachada.getExerciciosPreDefinidos();
        listaExerciciosPreDefinidos.addAll(exercicios);
        tabelaExercicios.setItems(FXCollections.observableArrayList(listaExerciciosPreDefinidos));
    }

    @FXML
    public void criarExercicio() {
        String nome = campoNome.getText();
        String descricao = campoDescricao.getText();
        Exercicio.TipoExercicio tipo = campoTipo.getValue();
        String tempoStr = campoTempo.getText();
        String caloriasStr = campoCalorias.getText();

        try {
            int tempo = Integer.parseInt(tempoStr);
            double calorias = Double.parseDouble(caloriasStr);

            Exercicio novoExercicio = new Exercicio();
            novoExercicio.setMusculosTrabalhados(new ArrayList<>());
            fachada.configurarExercicio(novoExercicio, nome, descricao, tipo, tempo, calorias);
            atualizarTabelaExercicios();
            mensagem.setText("Exercício criado com sucesso.");
        } catch (Exception e) {
            mensagem.setText("Erro ao criar exercício: " + e.getMessage());
        }
    }

    @FXML
    public void atualizarExercicio() {
        Exercicio exercicioSelecionado = tabelaExercicios.getSelectionModel().getSelectedItem();
        if (exercicioSelecionado != null) {
            try {
                String nome = campoNome.getText();
                String descricao = campoDescricao.getText();
                Exercicio.TipoExercicio tipo = campoTipo.getValue();
                String tempoStr = campoTempo.getText();
                String caloriasStr = campoCalorias.getText();
                int tempo = Integer.parseInt(tempoStr);
                double calorias = Double.parseDouble(caloriasStr);

                fachada.configurarExercicio(exercicioSelecionado, nome, descricao, tipo, tempo, calorias);
                atualizarTabelaExercicios();
                mensagem.setText("Exercício atualizado com sucesso.");
            } catch (Exception e) {
                mensagem.setText("Erro ao atualizar exercício: " + e.getMessage());
            }
        } else {
            mensagem.setText("Selecione um exercício para atualizar.");
        }
    }

    @FXML
    public void removerExercicio() {
        Exercicio exercicioSelecionado = tabelaExercicios.getSelectionModel().getSelectedItem();
        if (exercicioSelecionado != null) {
            try {
                fachada.removerExercicio(exercicioSelecionado.getId());
                atualizarTabelaExercicios();
                mensagem.setText("Exercício removido com sucesso.");
            } catch (ExercicioNaoEncontradoException e) {
                mensagem.setText("Erro ao remover exercício: " + e.getMessage());
            }
        } else {
            mensagem.setText("Selecione um exercício para remover.");
        }
    }

    private void atualizarTabelaExercicios() {
        List<Exercicio> listaDeExercicios = fachada.listarExercicios();
        tabelaExercicios.setItems(FXCollections.observableArrayList(listaDeExercicios));
    }
}