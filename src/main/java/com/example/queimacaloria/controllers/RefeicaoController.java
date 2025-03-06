package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.RefeicaoNaoEncontradaException;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.GeradorPDF;
import com.example.queimacaloria.negocio.InicializadorDados;
import com.example.queimacaloria.negocio.Refeicao;
import com.example.queimacaloria.negocio.Usuario;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors; // Importante para o filtro!

public class RefeicaoController {

    @FXML private TableView<Refeicao> tabelaRefeicoesUsuario;
    @FXML private TableColumn<Refeicao, String> colunaNomeUsuario;
    @FXML private TableColumn<Refeicao, Integer> colunaCaloriasUsuario;
    @FXML private TableColumn<Refeicao, String> colunaMacronutrientesUsuario;

    @FXML private TableView<Refeicao> tabelaRefeicoesPreDefinidas;
    @FXML private TableColumn<Refeicao, String> colunaNomePreDefinida;
    @FXML private TableColumn<Refeicao, Integer> colunaCaloriasPreDefinida;
    @FXML private TableColumn<Refeicao, String> colunaMacronutrientesPreDefinida;

    @FXML private Label mensagemRefeicao;

    private Fachada fachada = Fachada.getInstanciaUnica();
    private MainController mainController;
    private ObservableList<Refeicao> refeicoesPreDefinidas = FXCollections.observableArrayList();

    // Botão de compartilhar
    @FXML
    private Button buttonCompartilhar;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
    @FXML
    public void initialize() {
        configurarTabelaUsuario();
        atualizarTabelaRefeicoesUsuario();
        configurarTabelaPreDefinida();
        carregarRefeicoesPreDefinidas();
        //Verifica se o botão compartilhar está presente antes de configurar o evento.
        if(buttonCompartilhar != null){
            buttonCompartilhar.setOnAction(event -> compartilharLista());
        }
    }

    private void configurarTabelaUsuario() {
        colunaNomeUsuario.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaCaloriasUsuario.setCellValueFactory(new PropertyValueFactory<>("calorias"));
        colunaMacronutrientesUsuario.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMacronutrientesFormatados()));
    }

    private void configurarTabelaPreDefinida() {
        colunaNomePreDefinida.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaCaloriasPreDefinida.setCellValueFactory(new PropertyValueFactory<>("calorias"));
        colunaMacronutrientesPreDefinida.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMacronutrientesFormatados()));
    }

    private void carregarRefeicoesPreDefinidas() {
        try {
            List<Refeicao> refeicoes = InicializadorDados.inicializarRefeicoes();
            refeicoesPreDefinidas.setAll(refeicoes);
            tabelaRefeicoesPreDefinidas.setItems(refeicoesPreDefinidas);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar refeições pré-definidas", e.getMessage());
        }
    }

    @FXML
    public void abrirTelaCriarRefeicao() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/criacao-refeicao-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Criar Nova Refeição");
            stage.setScene(scene);

            CriacaoRefeicaoController controller = loader.getController();
            controller.setRefeicaoController(this);
            controller.setMainController(mainController);

            stage.showAndWait();
            atualizarTabelaRefeicoesUsuario();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao abrir tela", e.getMessage());
        }
    }

    @FXML
    public void atualizarRefeicao() {
        Refeicao refeicaoSelecionada = tabelaRefeicoesUsuario.getSelectionModel().getSelectedItem();
        if (refeicaoSelecionada != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/queimacaloria/views/edicao-refeicao-view.fxml"));
                Parent root = loader.load();

                EdicaoRefeicaoController controller = loader.getController();
                controller.setRefeicaoController(this);
                controller.setMainController(mainController);
                controller.setRefeicao(refeicaoSelecionada);

                Stage stage = new Stage();
                stage.setTitle("Editar Refeição");
                stage.setScene(new Scene(root));
                stage.showAndWait();
                atualizarTabelaRefeicoesUsuario();

            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao abrir tela de edição", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhuma refeição selecionada", "Selecione uma refeição para atualizar.");
        }
    }

    @FXML
    public void removerRefeicao() {
        Refeicao refeicaoSelecionada = tabelaRefeicoesUsuario.getSelectionModel().getSelectedItem();
        if (refeicaoSelecionada != null) {
            try {
                fachada.removerRefeicao(refeicaoSelecionada.getId());
                atualizarTabelaRefeicoesUsuario();
                mensagemRefeicao.setText("Refeição removida.");
            } catch (RefeicaoNaoEncontradaException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao remover refeição", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhuma refeição selecionada", "Selecione uma refeição para remover.");
        }
    }
    @FXML
    public void adicionarRefeicaoPreDefinida() {
        Refeicao refeicaoSelecionada = tabelaRefeicoesPreDefinidas.getSelectionModel().getSelectedItem();
        if(refeicaoSelecionada == null){  //Verifica
            showAlert(Alert.AlertType.WARNING, "Aviso", "Nenhuma refeição selecionada", "Selecione uma refeição para adicionar.");
            return;
        }


        if (mainController == null || mainController.getUsuarioLogado() == null) { //Verifica
            showAlert(Alert.AlertType.ERROR, "Erro", "Nenhum usuário logado", "Não foi possível adicionar a refeição.");
            return;
        }

        try {
            Refeicao novaRefeicao = new Refeicao(
                    refeicaoSelecionada.getNome(),
                    refeicaoSelecionada.getDescricao(),
                    refeicaoSelecionada.getCalorias(),
                    refeicaoSelecionada.getMacronutrientes()
            );

            //REVISÃO: Definindo o usuário LOGADO.
            novaRefeicao.setUsuario(mainController.getUsuarioLogado());

            fachada.configurarRefeicao(novaRefeicao, novaRefeicao.getNome(),
                    novaRefeicao.getDescricao(), novaRefeicao.getMacronutrientes(), novaRefeicao.getUsuario());//Passa o Usuario
            atualizarTabelaRefeicoesUsuario(); //Atualiza antes.
            mensagemRefeicao.setText("Refeição adicionada com sucesso!");


        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao adicionar refeição", e.getMessage());
            e.printStackTrace();
        }
    }

    public void atualizarTabelaRefeicoesUsuario() {
        try {
            if (mainController != null && mainController.getUsuarioLogado() != null) {
                Usuario usuarioLogado = mainController.getUsuarioLogado();
                List<Refeicao> listaRefeicoes = fachada.listarRefeicoes();

                // FILTRO: Mostra apenas as refeições do usuário logado
                listaRefeicoes = listaRefeicoes.stream()
                        .filter(refeicao -> refeicao.getUsuario() != null && refeicao.getUsuario().getId().equals(usuarioLogado.getId()))
                        .collect(Collectors.toList());

                tabelaRefeicoesUsuario.setItems(FXCollections.observableArrayList(listaRefeicoes));
                tabelaRefeicoesUsuario.refresh();
            } else {
                // Trata caso não haja usuário logado (opcional)
                tabelaRefeicoesUsuario.setItems(FXCollections.observableArrayList());
                System.err.println("RefeicaoController: Nenhum usuário logado ao atualizar a tabela.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar refeições", e.getMessage());
            e.printStackTrace(); // Sempre útil para debugging
        }
    }


    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    public void voltarParaTelaPrincipal() {
        if (mainController != null) {
            mainController.mostrarTelaPrincipal();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro interno", "MainController não foi configurado.");
        }
    }

    @FXML
    public void compartilharLista() {
        if (mainController != null && mainController.getUsuarioLogado() != null) {
            Usuario usuarioLogado = mainController.getUsuarioLogado();
            String nomeUsuario = usuarioLogado.getNome();  // Obtém o nome do usuário

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salvar Relatório de Refeições em PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos PDF", "*.pdf"));
            Stage stage = (Stage) tabelaRefeicoesUsuario.getScene().getWindow(); // Janela correta
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                try {
                    // Obtém a lista de *todas* as refeições.
                    List<Refeicao> todasRefeicoes = fachada.listarRefeicoes();
                    // Passa o nome do usuário
                    GeradorPDF.gerarRelatorioRefeicoes(todasRefeicoes, file.getAbsolutePath(), nomeUsuario);
                    showAlert(Alert.AlertType.INFORMATION, "Sucesso!", "Relatório Gerado",
                            "O relatório de refeições foi gerado com sucesso em: " + file.getAbsolutePath());

                } catch (IOException e) { // Captura IOException
                    showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao gerar relatório", "Erro de I/O: " + e.getMessage());
                    e.printStackTrace();
                }catch (Exception e) {  //Apenas o catch genérico.
                    showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao gerar relatório", "Erro inesperado: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Usuário Não Logado", "É necessário estar logado para gerar o relatório.");
        }
    }
}