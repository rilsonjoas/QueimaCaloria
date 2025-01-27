package com.example.queimacaloria.controllers;

import com.example.queimacaloria.dados.RepositorioRefeicoesArray;
import com.example.queimacaloria.excecoes.RefeicaoNaoEncontradaException;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Refeicao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Map;

public class MealController {

    @FXML private TableView<Refeicao> mealTable;
    @FXML private TableColumn<Refeicao, String> nameColumn;
    @FXML private TableColumn<Refeicao, Integer> caloriesColumn;
    @FXML private TableColumn<Refeicao, Map<String, Double>> macronutrientsColumn;

    @FXML
    private Label mealMessage;

    private RepositorioRefeicoesArray repositorioRefeicoes = RepositorioRefeicoesArray.getInstanciaUnica();

    private Fachada fachada = Fachada.getInstanciaUnica();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        caloriesColumn.setCellValueFactory(cellData -> cellData.getValue().caloriasProperty().asObject());
        macronutrientsColumn.setCellValueFactory(cellData -> cellData.getValue().macronutrientesProperty());
        atualizarTabelaRefeicoes();
    }

    public void openCreateMealScreen() {

    }

    @FXML
    public void updateMeal() {
        Refeicao refeicaoSelecionada = mealTable.getSelectionModel().getSelectedItem();
        if(refeicaoSelecionada != null){
            try{
                repositorioRefeicoes.atualizar(refeicaoSelecionada.getId());
                atualizarTabelaRefeicoes();
                mealMessage.setText("Refeição atualizada com sucesso!");

            }catch (RefeicaoNaoEncontradaException e) {
                mealMessage.setText("Erro ao atualizar refeição");
            }

        }else{
            mealMessage.setText("Selecione uma refeição");
        }
    }

    @FXML
    public void removeMeal() {
        Refeicao refeicaoSelecionada = mealTable.getSelectionModel().getSelectedItem();
        if(refeicaoSelecionada != null){
            try{
                repositorioRefeicoes.remover(refeicaoSelecionada.getId());
                atualizarTabelaRefeicoes();
                mealMessage.setText("Refeição removida com sucesso!");

            }catch (RefeicaoNaoEncontradaException e) {
                mealMessage.setText("Erro ao remover refeição");
            }

        }else{
            mealMessage.setText("Selecione uma refeição");
        }
    }

    private void atualizarTabelaRefeicoes() {
        ObservableList<Refeicao> listaRefeicoes = FXCollections.observableArrayList();
        try {
            for (int i = 0; i < repositorioRefeicoes.getRefeicoes().length; i++){
                if(repositorioRefeicoes.getRefeicoes()[i] != null){
                    listaRefeicoes.add(repositorioRefeicoes.getRefeicoes()[i]);
                }
            }
            mealTable.setItems(listaRefeicoes);
        } catch (Exception e) {
            mealMessage.setText("Erro ao carregar lista de refeições");
        }
    }
}