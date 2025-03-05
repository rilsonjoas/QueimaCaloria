package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.ExercicioNaoEncontradoException;
import com.example.queimacaloria.negocio.Exercicio;
import com.example.queimacaloria.negocio.Fachada;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;
import com.example.queimacaloria.interfaces.IBaseAdmin;
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


    @FXML
    public void initialize() {
        System.out.println("AdminExerciciosController.initialize() chamado"); // LOG

        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colunaTempo.setCellValueFactory(new PropertyValueFactory<>("tempo"));
        colunaCalorias.setCellValueFactory(cellData -> cellData.getValue().caloriasQueimadasProperty().asObject());

        campoTipo.setItems(FXCollections.observableArrayList(Exercicio.TipoExercicio.values()));

        carregarExerciciosPreDefinidos();
        atualizarTabelaExercicios();

        // Listener para mudanças na lista
        tabelaExercicios.getItems().addListener((javafx.collections.ListChangeListener.Change<? extends Exercicio> c) -> {
            System.out.println("AdminExerciciosController: Mudança na lista da tabela detectada!");
            while (c.next()) {
                if (c.wasAdded()) {
                    System.out.println("  Itens adicionados: " + c.getAddedSubList());
                }
                if (c.wasRemoved()) {
                    System.out.println("  Itens removidos: " + c.getRemoved());
                }
            }
        });

        // Listener para seleção de itens e preenchimento dos campos
        tabelaExercicios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println("AdminExerciciosController: Item selecionado: " + newSelection);
                preencherCampos(newSelection); // Chama o método para preencher
            }
        });
        //Verifica se tabelaExercicios é nula.
        if (tabelaExercicios == null) {
            System.err.println("Erro: tabelaExercicios é nula em AdminExerciciosController.initialize()");
        }
    }

    private void carregarExerciciosPreDefinidos() {
        System.out.println("AdminExerciciosController.carregarExerciciosPreDefinidos() chamado"); // LOG
        List<Exercicio> exercicios = fachada.getExerciciosPreDefinidos();
        System.out.println("AdminExerciciosController.carregarExerciciosPreDefinidos(): Exercicios pré-definidos carregados: " + exercicios); // LOG
        listaExerciciosPreDefinidos.addAll(exercicios);
        System.out.println("AdminExerciciosController.carregarExerciciosPreDefinidos() finalizado"); // LOG
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
        System.out.println("AdminExerciciosController.atualizarTabelaExercicios() chamado"); // LOG
        List<Exercicio> listaDeExercicios = fachada.listarExercicios();
        System.out.println("AdminExerciciosController.atualizarTabelaExercicios(): Todos os exercícios: " + listaDeExercicios); // LOG
        tabelaExercicios.setItems(FXCollections.observableArrayList(listaDeExercicios));
        //Verifica se a lista está vazia:
        if(listaDeExercicios.isEmpty()){
            System.out.println("AdminExerciciosController: A lista de exercícios está vazia.");
        }
    }

    private void preencherCampos(Exercicio exercicio){
        campoNome.setText(exercicio.getNome());
        campoDescricao.setText(exercicio.getDescricao());
        campoTipo.setValue(exercicio.getTipo());
        campoTempo.setText(String.valueOf(exercicio.getTempo()));
        campoCalorias.setText(String.valueOf(exercicio.getCaloriasQueimadas()));

    }

}