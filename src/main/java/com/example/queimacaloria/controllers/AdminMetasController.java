package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.MetaNaoEncontradaException;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Meta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import com.example.queimacaloria.interfaces.IBaseAdmin;
import lombok.Setter;

public class AdminMetasController {

    @FXML private TableView<Meta> tabelaMetas;
    @FXML private TableColumn<Meta, String> colunaDescricao;
    @FXML private TableColumn<Meta, Meta.Tipo> colunaTipo;
    @FXML private TableColumn<Meta, Double> colunaValorAlvo;

    @FXML private TextField campoDescricao;
    @FXML private ChoiceBox<Meta.Tipo> campoTipo;
    @FXML private TextField campoValorAlvo;
    @FXML private Label mensagem;

    private Fachada fachada = Fachada.getInstanciaUnica();
    @Setter private IBaseAdmin mainController;
    private ObservableList<Meta> listaMetasPreDefinidas = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        System.out.println("AdminMetasController.initialize() chamado");

        colunaDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colunaTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colunaValorAlvo.setCellValueFactory(new PropertyValueFactory<>("valorAlvo"));

        campoTipo.setItems(FXCollections.observableArrayList(Meta.Tipo.values()));

        carregarMetasPreDefinidas();
        atualizarTabelaMetas();

        // Listener para mudanças na lista
        tabelaMetas.getItems().addListener((javafx.collections.ListChangeListener.Change<? extends Meta> c) -> {
            System.out.println("AdminMetasController: Mudança na lista da tabela detectada!");
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
        tabelaMetas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println("AdminMetasController: Item selecionado: " + newSelection);
                preencherCampos(newSelection); // Chama o método para preencher
            }
        });
        //Verifica se tabelaMetas é nula.
        if (tabelaMetas == null) {
            System.err.println("Erro: tabelaMetas é nula em AdminMetasController.initialize()");
        }
    }

    private void carregarMetasPreDefinidas(){
        System.out.println("AdminMetasController.carregarMetasPreDefinidas() chamado");
        List<Meta> metas = fachada.getMetasPreDefinidas();
        System.out.println("AdminMetasController.carregarMetasPreDefinidas(): Metas pré-definidas carregadas: " + metas);
        listaMetasPreDefinidas.addAll(metas);
        System.out.println("AdminMetasController.carregarMetasPreDefinidas() finalizado");
    }

    @FXML
    public void criarMeta() {
        String descricao = campoDescricao.getText();
        Meta.Tipo tipo = campoTipo.getValue();
        String valorAlvoStr = campoValorAlvo.getText();

        try {
            double valorAlvo = Double.parseDouble(valorAlvoStr);

            Meta novaMeta = new Meta();
            fachada.configurarMeta(novaMeta, descricao, tipo, valorAlvo, 0.0, null, null);

            atualizarTabelaMetas();
            mensagem.setText("Meta criada com sucesso.");
        } catch (Exception e) {
            mensagem.setText("Erro ao criar meta: " + e.getMessage());
        }
    }

    @FXML
    public void atualizarMeta() {
        Meta metaSelecionada = tabelaMetas.getSelectionModel().getSelectedItem();
        if (metaSelecionada != null) {
            try {
                String descricao = campoDescricao.getText();
                Meta.Tipo tipo = campoTipo.getValue();
                String valorAlvoStr = campoValorAlvo.getText();
                double valorAlvo = Double.parseDouble(valorAlvoStr);

                fachada.configurarMeta(metaSelecionada, descricao, tipo, valorAlvo, 0.0, null, null);
                atualizarTabelaMetas();
                mensagem.setText("Meta atualizada com sucesso.");
            } catch (Exception e) {
                mensagem.setText("Erro ao atualizar meta: " + e.getMessage());
            }
        } else {
            mensagem.setText("Selecione uma meta para atualizar.");
        }
    }

    @FXML
    public void removerMeta() {
        Meta metaSelecionada = tabelaMetas.getSelectionModel().getSelectedItem();
        if (metaSelecionada != null) {
            try {
                fachada.removerMeta(metaSelecionada.getId());
                atualizarTabelaMetas();
                mensagem.setText("Meta removida com sucesso.");
            } catch (MetaNaoEncontradaException e) {
                mensagem.setText("Erro ao remover meta: " + e.getMessage());
            }
        } else {
            mensagem.setText("Selecione uma meta para remover.");
        }
    }

    private void atualizarTabelaMetas() {
        System.out.println("AdminMetasController.atualizarTabelaMetas() chamado");
        List<Meta> listaDeMetas = fachada.listarMetas();
        System.out.println("AdminMetasController.atualizarTabelaMetas(): Todas as metas: " + listaDeMetas);
        tabelaMetas.setItems(FXCollections.observableArrayList(listaDeMetas));
        //Verifica se a lista está vazia:
        if(listaDeMetas.isEmpty()){
            System.out.println("AdminMetasController: A lista de metas está vazia.");
        }
    }
    private void preencherCampos(Meta meta){
        campoDescricao.setText(meta.getDescricao());
        campoTipo.setValue(meta.getTipo());
        campoValorAlvo.setText(String.valueOf(meta.getValorAlvo()));

    }
}
