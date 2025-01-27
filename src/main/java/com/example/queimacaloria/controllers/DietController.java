package com.example.queimacaloria.controllers;

import com.example.queimacaloria.dados.RepositorioDietasArray;
import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;
import com.example.queimacaloria.negocio.Dieta;
import com.example.queimacaloria.negocio.Fachada;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.UUID;

public class DietController {

    @FXML
    private TableView<Dieta> dietTable;
    @FXML
    private TableColumn<Dieta, String> nameColumn;
    @FXML
    private TableColumn<Dieta, Dieta.Objetivo> goalColumn;
    @FXML
    private TableColumn<Dieta, Integer> caloriesColumn;
    @FXML
    private TableColumn<Dieta, Double> progressColumn;

    @FXML
    private Label dietMessage;


    private RepositorioDietasArray repositorioDietas = RepositorioDietasArray.getInstanciaUnica();

    private Fachada fachada = Fachada.getInstanciaUnica();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        goalColumn.setCellValueFactory(cellData -> cellData.getValue().objetivoProperty());
        caloriesColumn.setCellValueFactory(cellData -> cellData.getValue().caloriasDiariasProperty().asObject());
        progressColumn.setCellValueFactory(cellData -> cellData.getValue().progressProperty().asObject());
        atualizarTabelaDietas();
    }

    public void openCreateDietScreen() {

    }

    @FXML
    public void updateDiet() {
        Dieta dietaSelecionada = dietTable.getSelectionModel().getSelectedItem();
        if (dietaSelecionada != null) {
            try {
                repositorioDietas.atualizar(dietaSelecionada.getId());
                atualizarTabelaDietas();
                dietMessage.setText("Dieta atualizada com sucesso!");

            } catch (DietaNaoEncontradaException e) {
                dietMessage.setText("Erro ao atualizar dieta");
            }

        } else {
            dietMessage.setText("Selecione uma dieta");
        }
    }

    @FXML
    public void removeDiet() {
        Dieta dietaSelecionada = dietTable.getSelectionModel().getSelectedItem();
        if (dietaSelecionada != null) {
            try {
                repositorioDietas.remover(dietaSelecionada.getId());
                atualizarTabelaDietas();
                dietMessage.setText("Dieta removida com sucesso!");

            } catch (DietaNaoEncontradaException e) {
                dietMessage.setText("Erro ao remover dieta");
            }

        } else {
            dietMessage.setText("Selecione uma dieta");
        }
    }

    private void atualizarTabelaDietas() {
        ObservableList<Dieta> listaDietas = FXCollections.observableArrayList();
        try {
            for (int i = 0; i < repositorioDietas.getDietas().length; i++) {
                if (repositorioDietas.getDietas()[i] != null) {
                    listaDietas.add(repositorioDietas.getDietas()[i]);
                }
            }

            dietTable.setItems(listaDietas);
        } catch (Exception e) {
            dietMessage.setText("Erro ao carregar lista de dietas");
        }
    }
}