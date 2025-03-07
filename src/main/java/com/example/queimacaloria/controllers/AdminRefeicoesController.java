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
    @Setter private IBaseAdmin mainController;  // Para comunicação.
    private ObservableList<Refeicao> listaRefeicoesPreDefinidas = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
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
        List<Refeicao> refeicoes = fachada.getRefeicoesPreDefinidas();
        listaRefeicoesPreDefinidas.addAll(refeicoes);
        System.out.println("Refeições pré-definidas carregadas.");
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
            // A criação/configuração agora é *toda* feita via Fachada, e o usuário é nulo.
            fachada.configurarRefeicao(novaRefeicao, nome, descricao, macronutrientes, null, null);
            atualizarTabelaRefeicoes(); // Atualiza a tabela *após* a operação na Fachada.
            mensagem.setText("Refeição criada com sucesso.");
        } catch (NumberFormatException e) {
            mensagem.setText("Erro: Os valores de macronutrientes devem ser números.");
        }
        catch (Exception e) { // Captura genérica.
            mensagem.setText("Erro ao criar refeição: " + e.getMessage());
            e.printStackTrace(); // Stack trace completo!
        }
    }

    @FXML
    public void atualizarRefeicao() {
        Refeicao refeicaoSelecionada = tabelaRefeicoes.getSelectionModel().getSelectedItem();
        if (refeicaoSelecionada == null) {
            mensagem.setText("Selecione uma refeição para atualizar.");
            return;
        }

        try {
            String nome = campoNome.getText();
            String descricao = campoDescricao.getText();
            String proteinasStr = campoProteinas.getText();
            String carboidratosStr = campoCarboidratos.getText();
            String gordurasStr = campoGorduras.getText();

            Map<String, Double> macronutrientes = new HashMap<>();
            macronutrientes.put("Proteínas", Double.parseDouble(proteinasStr));  // NumberFormatException
            macronutrientes.put("Carboidratos", Double.parseDouble(carboidratosStr)); // NumberFormatException
            macronutrientes.put("Gorduras", Double.parseDouble(gordurasStr));  // NumberFormatException

            // Toda a lógica de negócio/atualização é feita na Fachada. Usuário = null.
            fachada.configurarRefeicao(refeicaoSelecionada, nome, descricao, macronutrientes, null, null);
            atualizarTabelaRefeicoes(); // Atualiza a tabela *após* a operação na Fachada.
            mensagem.setText("Refeição atualizada com sucesso.");

        } catch (NumberFormatException e) {
            mensagem.setText("Erro: Os valores de macronutrientes devem ser números.");
        }
        catch (Exception e) { // Captura genérica para outros erros.
            mensagem.setText("Erro inesperado: " + e.getMessage());
            e.printStackTrace(); // Imprime o stack trace!
        }
    }

    @FXML
    public void removerRefeicao() {
        Refeicao refeicaoSelecionada = tabelaRefeicoes.getSelectionModel().getSelectedItem();
        if (refeicaoSelecionada == null) {
            mensagem.setText("Selecione uma refeição para remover.");
            return;
        }

        try {
            // A remoção agora é feita *inteiramente* pela Fachada.
            fachada.removerRefeicao(refeicaoSelecionada.getId());
            atualizarTabelaRefeicoes(); // Atualiza *após* a remoção.
            mensagem.setText("Refeição removida com sucesso.");
        } catch (RefeicaoNaoEncontradaException e) {
            mensagem.setText("Erro ao remover refeição: " + e.getMessage());
        } catch (Exception e) { // Captura genérica.
            mensagem.setText("Erro inesperado: " + e.getMessage());
            e.printStackTrace(); // Stack trace completo!
        }
    }

    private void atualizarTabelaRefeicoes() {
        List<Refeicao> listaDeRefeicoes = fachada.listarRefeicoes();
        tabelaRefeicoes.setItems(FXCollections.observableArrayList(listaDeRefeicoes));
        tabelaRefeicoes.refresh(); // Importante!
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