package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.RefeicaoNaoEncontradaException;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Refeicao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.queimacaloria.interfaces.IBaseAdmin;
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


    @FXML
    public void initialize() {
        System.out.println("AdminRefeicoesController.initialize() chamado");

        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colunaCalorias.setCellValueFactory(new PropertyValueFactory<>("calorias"));

        carregarRefeicoesPreDefinidas();
        atualizarTabelaRefeicoes();

        // Listener para a ObservableList da tabela
        tabelaRefeicoes.getItems().addListener((javafx.collections.ListChangeListener.Change<? extends Refeicao> c) -> {
            System.out.println("AdminRefeicoesController: Mudança na lista da tabela detectada!");
            while (c.next()) {
                if (c.wasAdded()) {
                    System.out.println("  Itens adicionados: " + c.getAddedSubList());
                }
                if (c.wasRemoved()) {
                    System.out.println("  Itens removidos: " + c.getRemoved());
                }
            }
        });

        // Listener para seleção e preenchimento
        tabelaRefeicoes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println("AdminRefeicoesController: Item selecionado: " + newSelection);
                preencherCampos(newSelection);
            }
        });
        //Verifica se tabelaRefeicoes é nula.
        if (tabelaRefeicoes == null) {
            System.err.println("Erro: tabelaRefeicoes é nula em AdminRefeicoesController.initialize()");
        }
    }

    private void carregarRefeicoesPreDefinidas(){
        System.out.println("AdminRefeicoesController.carregarRefeicoesPreDefinidas() chamado");
        List<Refeicao> refeicoes = fachada.getRefeicoesPreDefinidas();
        System.out.println("AdminRefeicoesController.carregarRefeicoesPreDefinidas: Refeições pré-definidas: " + refeicoes);
        listaRefeicoesPreDefinidas.addAll(refeicoes);
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
        System.out.println("AdminRefeicoesController.atualizarTabelaRefeicoes() chamado"); // LOG
        List<Refeicao> listaDeRefeicoes = fachada.listarRefeicoes();
        System.out.println("AdminRefeicoesController.atualizarTabelaRefeicoes(): Todas as refeições: " + listaDeRefeicoes); // LOG
        tabelaRefeicoes.setItems(FXCollections.observableArrayList(listaDeRefeicoes));
        //Verifica se a lista está vazia:
        if(listaDeRefeicoes.isEmpty()){
            System.out.println("AdminRefeicoesController: A lista de refeições está vazia.");
        }
    }

    private void preencherCampos(Refeicao refeicao) {
        campoNome.setText(refeicao.getNome());
        campoDescricao.setText(refeicao.getDescricao());

        // Preenche os campos de macronutrientes, lidando com possíveis nulos
        if (refeicao.getMacronutrientes() != null) {
            campoProteinas.setText(String.valueOf(refeicao.getMacronutrientes().getOrDefault("Proteínas", 0.0)));
            campoCarboidratos.setText(String.valueOf(refeicao.getMacronutrientes().getOrDefault("Carboidratos", 0.0)));
            campoGorduras.setText(String.valueOf(refeicao.getMacronutrientes().getOrDefault("Gorduras", 0.0)));
        } else {
            // Se macronutrientes for nulo, limpa os campos
            campoProteinas.clear();
            campoCarboidratos.clear();
            campoGorduras.clear();
        }
    }
}