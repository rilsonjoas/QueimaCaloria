package com.example.queimacaloria.controllers;

import com.example.queimacaloria.dados.RepositorioTreinosArray;
import com.example.queimacaloria.excecoes.TreinoNaoEncontradoException;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Treino;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.util.List;

public class WorkoutController {

    @FXML private TableView<Treino> workoutTable;
    @FXML private TableColumn<Treino, String> nameColumn;
    @FXML private TableColumn<Treino, String> typeColumn;
    @FXML private TableColumn<Treino, Integer> durationColumn;
    @FXML private TableColumn<Treino, Integer> difficultyColumn;
    @FXML private TableColumn<Treino, Double> progressColumn;

    @FXML
    private Label workoutMessage;

    private RepositorioTreinosArray repositorioTreinos = RepositorioTreinosArray.getInstanciaUnica();

    private Fachada fachada = Fachada.getInstanciaUnica();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().tipoDeTreinoProperty());
        durationColumn.setCellValueFactory(cellData -> cellData.getValue().duracaoProperty().asObject());
        difficultyColumn.setCellValueFactory(cellData -> cellData.getValue().nivelDeDificuldadeProperty().asObject());
        progressColumn.setCellValueFactory(cellData -> cellData.getValue().progressProperty().asObject());
        atualizarTabelaTreinos();
    }
    public void openCreateWorkoutScreen() {
    }


    @FXML
    public void updateWorkout() {
        Treino treinoSelecionado = workoutTable.getSelectionModel().getSelectedItem();
        if(treinoSelecionado != null){
            try{
                repositorioTreinos.salvar(treinoSelecionado);
                atualizarTabelaTreinos();
                workoutMessage.setText("Treino atualizado com sucesso!");

            }catch (TreinoNaoEncontradoException e) {
                workoutMessage.setText("Erro ao atualizar treino");
            }

        }else{
            workoutMessage.setText("Selecione um treino");
        }
    }
    @FXML
    public void removeWorkout() {
        Treino treinoSelecionado = workoutTable.getSelectionModel().getSelectedItem();
        if(treinoSelecionado != null){
            try{
                repositorioTreinos.remover(treinoSelecionado.getId());
                atualizarTabelaTreinos();
                workoutMessage.setText("Treino removido com sucesso!");

            }catch (TreinoNaoEncontradoException e) {
                workoutMessage.setText("Erro ao remover treino");
            }

        }else{
            workoutMessage.setText("Selecione um treino");
        }
    }

    private void atualizarTabelaTreinos() {
        List<Treino> listaTreinos = repositorioTreinos.getAll();
        workoutTable.setItems(FXCollections.observableArrayList(listaTreinos));
    }
}