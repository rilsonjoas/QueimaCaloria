package com.example.queimacaloria.controllers;

import com.example.queimacaloria.dados.RepositorioExerciciosArray;
import com.example.queimacaloria.excecoes.ExercicioNaoEncontradoException;
import com.example.queimacaloria.negocio.Exercicio;
import com.example.queimacaloria.negocio.Fachada;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ExerciseController {

    @FXML private TableView<Exercicio> exerciseTable;

    @FXML private TableColumn<Exercicio, String> nameColumn;
    @FXML private TableColumn<Exercicio, Exercicio.TipoExercicio> typeColumn;
    @FXML private TableColumn<Exercicio, Integer> timeColumn;
    @FXML private TableColumn<Exercicio, Double> caloriesBurnedColumn;

    @FXML
    private Label exerciseMessage;

    private RepositorioExerciciosArray repositorioExercicios = RepositorioExerciciosArray.getInstanciaUnica();

    private Fachada fachada = Fachada.getInstanciaUnica();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().tipoProperty());
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().tempoProperty().asObject());
        caloriesBurnedColumn.setCellValueFactory(cellData -> cellData.getValue().caloriasQueimadasProperty().asObject());
        atualizarTabelaExercicios();
    }
    public void openCreateExerciseScreen() {

    }

    @FXML
    public void updateExercise() {
        Exercicio exercicioSelecionado = exerciseTable.getSelectionModel().getSelectedItem();
        if(exercicioSelecionado != null){
            try{
                repositorioExercicios.atualizar(exercicioSelecionado.getId());
                atualizarTabelaExercicios();
                exerciseMessage.setText("Exercício atualizado com sucesso!");

            }catch (ExercicioNaoEncontradoException e) {
                exerciseMessage.setText("Erro ao atualizar exercício");
            }

        }else{
            exerciseMessage.setText("Selecione um exercício");
        }
    }


    @FXML
    public void removeExercise() {
        Exercicio exercicioSelecionado = exerciseTable.getSelectionModel().getSelectedItem();
        if(exercicioSelecionado != null){
            try{
                repositorioExercicios.remover(exercicioSelecionado.getId());
                atualizarTabelaExercicios();
                exerciseMessage.setText("Exercício removido com sucesso!");

            }catch (ExercicioNaoEncontradoException e) {
                exerciseMessage.setText("Erro ao remover exercício");
            }

        }else{
            exerciseMessage.setText("Selecione um exercício");
        }
    }

    private void atualizarTabelaExercicios() {
        ObservableList<Exercicio> listaExercicios = FXCollections.observableArrayList();
        try {
            for (int i = 0; i < repositorioExercicios.getExercicios().length; i++) {
                if (repositorioExercicios.getExercicios()[i] != null) {
                    listaExercicios.add(repositorioExercicios.getExercicios()[i]);
                }
            }

            exerciseTable.setItems(listaExercicios);
        } catch (Exception e) {
            exerciseMessage.setText("Erro ao carregar lista de exercícios");
        }
    }
}