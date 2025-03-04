package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.RefeicaoNaoEncontradaException;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.InicializadorDados;
import com.example.queimacaloria.negocio.Refeicao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.queimacaloria.interfaces.IBaseAdmin;  // Importe a interface
import lombok.Setter;

public class AdminRefeicoesController {

    @FXML private TableView<Refeicao> tabelaRefeicoes;
    @FXML private TableColumn<Refeicao, String> colunaNome;
    @FXML private TableColumn<Refeicao, String> colunaDescricao;
    @FXML private TableColumn<Refeicao, Integer> colunaCalorias;

    @FXML private TextField campoNome;
    @FXML private TextField campoDescricao;
    @FXML private TextField campoProteinas;
    @FXML private TextField campoCarboidratos;
    @FXML private TextField campoGorduras;
    @FXML private Label mensagem;

    private Fachada fachada = Fachada.getInstanciaUnica();
    @Setter private IBaseAdmin mainController;
    private ObservableList<Refeicao> listaRefeicoesPreDefinidas = FXCollections.observableArrayList();

    public void setMainController(AdminMainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colunaCalorias.setCellValueFactory(new PropertyValueFactory<>("calorias"));

        tabelaRefeicoes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                preencherCamposComRefeicaoSelecionada(newSelection);
            } else {
                limparCampos();
            }
        });

        carregarRefeicoesPreDefinidas();
        atualizarTabelaRefeicoes();
    }

    private void limparCampos() {
        campoNome.setText("");
        campoDescricao.setText("");
        campoProteinas.setText("");
        campoCarboidratos.setText("");
        campoGorduras.setText("");
    }
    private void preencherCamposComRefeicaoSelecionada(Refeicao refeicao) {
        campoNome.setText(refeicao.getNome());
        campoDescricao.setText(refeicao.getDescricao());

        if (refeicao.getMacronutrientes() != null) {
            campoProteinas.setText(String.valueOf(refeicao.getMacronutrientes().getOrDefault("Proteínas", 0.0)));
            campoCarboidratos.setText(String.valueOf(refeicao.getMacronutrientes().getOrDefault("Carboidratos", 0.0)));
            campoGorduras.setText(String.valueOf(refeicao.getMacronutrientes().getOrDefault("Gorduras", 0.0)));
        }
    }

    private void carregarRefeicoesPreDefinidas(){
        List<Refeicao> refeicoes = InicializadorDados.inicializarRefeicoes();
        listaRefeicoesPreDefinidas.addAll(refeicoes);
        tabelaRefeicoes.setItems(listaRefeicoesPreDefinidas);

    }

    @FXML
    public void criarRefeicao() {
        String nome = campoNome.getText();
        String descricao = campoDescricao.getText();
        String proteinasStr = campoProteinas.getText();
        String carboidratosStr = campoCarboidratos.getText();
        String gordurasStr = campoGorduras.getText();

        try {
            Map<String, Double> macronutrientes = new HashMap<>();
            macronutrientes.put("Proteínas", Double.parseDouble(proteinasStr));
            macronutrientes.put("Carboidratos", Double.parseDouble(carboidratosStr));
            macronutrientes.put("Gorduras", Double.parseDouble(gordurasStr));

            Refeicao novaRefeicao = new Refeicao();
            fachada.configurarRefeicao(novaRefeicao, nome, descricao, macronutrientes);
            atualizarTabelaRefeicoes();
            mensagem.setText("Refeição criada com sucesso.");
        } catch (Exception e) {
            mensagem.setText("Erro ao criar refeição: " + e.getMessage());
        }
    }

    @FXML
    public void atualizarRefeicao() {
        Refeicao refeicaoSelecionada = tabelaRefeicoes.getSelectionModel().getSelectedItem();
        if (refeicaoSelecionada != null) {
            try {
                String nome = campoNome.getText();
                String descricao = campoDescricao.getText();
                String proteinasStr = campoProteinas.getText();
                String carboidratosStr = campoCarboidratos.getText();
                String gordurasStr = campoGorduras.getText();

                Map<String, Double> macronutrientes = new HashMap<>();
                macronutrientes.put("Proteínas", Double.parseDouble(proteinasStr));
                macronutrientes.put("Carboidratos", Double.parseDouble(carboidratosStr));
                macronutrientes.put("Gorduras", Double.parseDouble(gordurasStr));

                fachada.configurarRefeicao(refeicaoSelecionada, nome, descricao, macronutrientes);
                atualizarTabelaRefeicoes();
                mensagem.setText("Refeição atualizada com sucesso.");
            } catch (Exception e) {
                mensagem.setText("Erro ao atualizar refeição: " + e.getMessage());
            }
        } else {
            mensagem.setText("Selecione uma refeição para atualizar.");
        }
    }

    @FXML
    public void removerRefeicao() {
        Refeicao refeicaoSelecionada = tabelaRefeicoes.getSelectionModel().getSelectedItem();
        if (refeicaoSelecionada != null) {
            try {
                fachada.removerRefeicao(refeicaoSelecionada.getId());
                atualizarTabelaRefeicoes();
                mensagem.setText("Refeição removida com sucesso.");
            } catch (RefeicaoNaoEncontradaException e) {
                mensagem.setText("Erro ao remover refeição: " + e.getMessage());
            }
        } else {
            mensagem.setText("Selecione uma refeição para remover.");
        }
    }

    private void atualizarTabelaRefeicoes() {
        List<Refeicao> listaDeRefeicoes = fachada.listarRefeicoes();
        tabelaRefeicoes.setItems(FXCollections.observableArrayList(listaDeRefeicoes));
    }
}