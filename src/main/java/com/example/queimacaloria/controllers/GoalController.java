package com.example.queimacaloria.controllers;

import com.example.queimacaloria.dados.RepositorioMetasArray;
import com.example.queimacaloria.excecoes.MetaNaoEncontradaException;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Meta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Date;

public class GoalController {

    @FXML private TableView<Meta> goalTable;
    @FXML private TableColumn<Meta, String> descriptionColumn;
    @FXML private TableColumn<Meta, Meta.Tipo> typeColumn;
    @FXML private TableColumn<Meta, Double> progressColumn;

    @FXML private TableColumn<Meta, Date> conclusionDateColumn;

    @FXML
    private Label goalMessage;

    private RepositorioMetasArray repositorioMetas = RepositorioMetasArray.getInstanciaUnica();

    private Fachada fachada = Fachada.getInstanciaUnica();

    @FXML
    public void initialize() {
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descricaoProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().tipoProperty());
        progressColumn.setCellValueFactory(cellData -> cellData.getValue().progressProperty().asObject());
        conclusionDateColumn.setCellValueFactory(cellData -> cellData.getValue().dataConclusaoProperty());
        atualizarTabelaMetas();
    }
    public void openCreateGoalScreen() {
    }


    @FXML
    public void updateGoal() {
        Meta metaSelecionada = goalTable.getSelectionModel().getSelectedItem();
        if(metaSelecionada != null){
            try{
                repositorioMetas.atualizar(metaSelecionada.getId());
                atualizarTabelaMetas();
                goalMessage.setText("Meta atualizada com sucesso!");

            }catch (MetaNaoEncontradaException e) {
                goalMessage.setText("Erro ao atualizar meta");
            }

        }else{
            goalMessage.setText("Selecione uma meta");
        }
    }


    @FXML
    public void removeGoal() {
        Meta metaSelecionada = goalTable.getSelectionModel().getSelectedItem();
        if(metaSelecionada != null){
            try{
                repositorioMetas.remover(metaSelecionada.getId());
                atualizarTabelaMetas();
                goalMessage.setText("Meta removida com sucesso!");

            }catch (MetaNaoEncontradaException e) {
                goalMessage.setText("Erro ao remover meta");
            }

        }else{
            goalMessage.setText("Selecione uma meta");
        }
    }

    private void atualizarTabelaMetas() {
        ObservableList<Meta> listaMetas = FXCollections.observableArrayList();
        try {
            for(int i = 0; i < repositorioMetas.getMetas().length; i++){
                if(repositorioMetas.getMetas()[i] != null){
                    listaMetas.add(repositorioMetas.getMetas()[i]);
                }

            }

            goalTable.setItems(listaMetas);
        } catch (Exception e) {
            goalMessage.setText("Erro ao carregar lista de metas");
        }
    }
}