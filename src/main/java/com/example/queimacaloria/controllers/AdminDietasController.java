package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;
import com.example.queimacaloria.negocio.Dieta;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Meta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.example.queimacaloria.interfaces.IBaseAdmin;  // Importe a interface
import lombok.Setter;

import java.util.List;

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
    @Setter private IBaseAdmin mainController; //Para comunicação.
    private ObservableList<Dieta> listaDietasPreDefinidas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        System.out.println("AdminDietasController.initialize() chamado"); // LOG

        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaObjetivo.setCellValueFactory(new PropertyValueFactory<>("objetivo"));
        colunaCalorias.setCellValueFactory(new PropertyValueFactory<>("caloriasDiarias"));

        campoObjetivo.setItems(FXCollections.observableArrayList(Meta.Tipo.values()));


        carregarDietasPreDefinidas();
        atualizarTabelaDietas();

        // Listener para a ObservableList
        tabelaDietas.getItems().addListener((javafx.collections.ListChangeListener.Change<? extends Dieta> c) -> {
            System.out.println("AdminDietasController: Mudança na lista da tabela detectada!");
            while (c.next()) {
                if (c.wasAdded()) {
                    System.out.println("  Itens adicionados: " + c.getAddedSubList());
                }
                if (c.wasRemoved()) {
                    System.out.println("  Itens removidos: " + c.getRemoved());
                }
                // Outras verificações: wasReplaced(), wasUpdated(), etc.
            }
        });

        // Listener de seleção (aqui é onde a mágica acontece!)
        tabelaDietas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println("AdminDietasController: Item selecionado: " + newSelection);
                preencherCampos(newSelection); // Chama o método para preencher
            }
        });

        //Verifica se tabelaDietas é nula.
        if (tabelaDietas == null) {
            System.err.println("Erro: tabelaDietas é nula em AdminDietasController.initialize()");
        }
    }

    //  Método para preencher os campos com os dados da dieta selecionada
    private void preencherCampos(Dieta dieta) {
        campoNome.setText(dieta.getNome());
        campoObjetivo.setValue(dieta.getObjetivo());
        campoCalorias.setText(String.valueOf(dieta.getCaloriasDiarias()));
    }


    // Carregar os exercícios do InicializadorDados
    private void carregarDietasPreDefinidas() {
        System.out.println("AdminDietasController.carregarDietasPreDefinidas() chamado"); // LOG
        List<Dieta> dietas = fachada.getDietasPreDefinidas();
        System.out.println("AdminDietasController.carregarDietasPreDefinidas(): Dietas pré-definidas carregadas: " + dietas); // LOG
        listaDietasPreDefinidas.addAll(dietas);
        // tabelaDietas.setItems(FXCollections.observableArrayList(dietas)); // REMOVA ESTA LINHA.  Você já faz isso em atualizarTabelaDietas()
        System.out.println("AdminDietasPreDefinidas() finalizado"); // LOG
    }

    @FXML
    public void criarDieta() {
        String nome = campoNome.getText();
        Meta.Tipo objetivo = campoObjetivo.getValue();
        String caloriasStr = campoCalorias.getText();

        try {
            int calorias = Integer.parseInt(caloriasStr);
            Dieta novaDieta = new Dieta();
            fachada.configurarDieta(novaDieta, nome, objetivo, calorias, null, null); //Usuario e tipo de dieta nulo
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

                fachada.configurarDieta(dietaSelecionada, nome, objetivo, calorias, null, null);
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
        System.out.println("AdminDietasController.atualizarTabelaDietas() chamado"); // LOG
        List<Dieta> listaDeDietas = fachada.listarDietas();
        System.out.println("AdminDietasController.atualizarTabelaDietas(): Todas as dietas: " + listaDeDietas);  // LOG
        tabelaDietas.setItems(FXCollections.observableArrayList(listaDeDietas));

        //Verifica se a lista está vazia:
        if(listaDeDietas.isEmpty()){
            System.out.println("AdminDietasController: A lista de dietas está vazia.");
        }
    }
}