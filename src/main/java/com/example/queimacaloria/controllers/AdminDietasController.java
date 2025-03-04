package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;
import com.example.queimacaloria.negocio.Dieta;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.InicializadorDados;
import com.example.queimacaloria.negocio.Meta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import com.example.queimacaloria.interfaces.IBaseAdmin;  // Importe a interface
import lombok.Setter;

public class AdminDietasController {

    @FXML private TableView<Dieta> tabelaDietas;
    @FXML private TableColumn<Dieta, String> colunaNome;
    @FXML private TableColumn<Dieta, Meta.Tipo> colunaObjetivo;
    @FXML private TableColumn<Dieta, Integer> colunaCalorias;

    @FXML private TextField campoNome;
    @FXML private ChoiceBox<Meta.Tipo> campoObjetivo;
    @FXML private TextField campoCalorias;
    @FXML private Label mensagem;

    private Fachada fachada = Fachada.getInstanciaUnica();
    @Setter private IBaseAdmin mainController; //Utiliza Interface
    private ObservableList<Dieta> listaDietasPreDefinidas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaObjetivo.setCellValueFactory(new PropertyValueFactory<>("objetivo"));
        colunaCalorias.setCellValueFactory(new PropertyValueFactory<>("caloriasDiarias"));

        campoObjetivo.setItems(FXCollections.observableArrayList(Meta.Tipo.values()));

        // Adicionar listener para preencher campos ao selecionar uma dieta
        tabelaDietas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                preencherCamposComDietaSelecionada(newSelection);
            } else {
                limparCampos();
            }
        });

        carregarDietasPreDefinidas();
        atualizarTabelaDietas();
    }
    private void limparCampos() {
        campoNome.setText("");
        campoCalorias.setText("");
        campoObjetivo.setValue(null);
    }

    private void preencherCamposComDietaSelecionada(Dieta dieta) {
        campoNome.setText(dieta.getNome());
        campoObjetivo.setValue(dieta.getObjetivo());
        campoCalorias.setText(String.valueOf(dieta.getCaloriasDiarias()));
    }

    // Carregar os exercícios do InicializadorDados
    private void carregarDietasPreDefinidas() {
        List<Dieta> dietas = fachada.getDietasPreDefinidas();  //Utilize a fachada para pegar os dados
        listaDietasPreDefinidas.addAll(dietas);
        tabelaDietas.setItems(FXCollections.observableArrayList(dietas));
    }

    @FXML
    public void criarDieta() {
        String nome = campoNome.getText();
        Meta.Tipo objetivo = campoObjetivo.getValue();
        String caloriasStr = campoCalorias.getText();

        try {
            int calorias = Integer.parseInt(caloriasStr);
            Dieta novaDieta = new Dieta();
            fachada.configurarDieta(novaDieta, nome, objetivo, calorias, null);
            atualizarTabelaDietas();
            mensagem.setText("Dieta criada com sucesso.");
        } catch (Exception e) {
            mensagem.setText("Erro ao criar dieta: " + e.getMessage());
        }
    }

    @FXML
    public void atualizarDieta() {
        Dieta dietaSelecionada = tabelaDietas.getSelectionModel().getSelectedItem();
        if (dietaSelecionada != null) {
            try {
                String nome = campoNome.getText();
                Meta.Tipo objetivo = campoObjetivo.getValue();
                String caloriasStr = campoCalorias.getText();
                int calorias = Integer.parseInt(caloriasStr);

                fachada.configurarDieta(dietaSelecionada, nome, objetivo, calorias, null);
                atualizarTabelaDietas();
                mensagem.setText("Dieta atualizada com sucesso.");
            } catch (Exception e) {
                mensagem.setText("Erro ao atualizar dieta: " + e.getMessage());
            }
        } else {
            mensagem.setText("Selecione uma dieta para atualizar.");
        }
    }

    @FXML
    public void removerDieta() {
        Dieta dietaSelecionada = tabelaDietas.getSelectionModel().getSelectedItem();
        if (dietaSelecionada != null) {
            try {
                fachada.removerDieta(dietaSelecionada.getId());
                atualizarTabelaDietas();
                mensagem.setText("Dieta removida com sucesso.");
            } catch (DietaNaoEncontradaException e) {
                mensagem.setText("Erro ao remover dieta: " + e.getMessage());
            }
        } else {
            mensagem.setText("Selecione uma dieta para remover.");
        }
    }

    private void atualizarTabelaDietas() {
        List<Dieta> listaDeDietas = fachada.listarDietas();
        tabelaDietas.setItems(FXCollections.observableArrayList(listaDeDietas));
    }
}