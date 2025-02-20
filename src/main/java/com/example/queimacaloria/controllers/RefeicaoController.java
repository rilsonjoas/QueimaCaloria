package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;
import com.example.queimacaloria.excecoes.RefeicaoNaoEncontradaException;
import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;
import com.example.queimacaloria.negocio.Dieta;
import com.example.queimacaloria.negocio.Fachada;
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
import javafx.stage.Stage;
import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class RefeicaoController {

    @FXML private TableView<Refeicao> tabelaRefeicoesUsuario;
    @FXML private TableColumn<Refeicao, String> colunaNomeUsuario;
    @FXML private TableColumn<Refeicao, Integer> colunaCaloriasUsuario;
    @FXML private TableColumn<Refeicao, String> colunaMacronutrientesUsuario; // TIPO ALTERADO

    @FXML private TableView<Refeicao> tabelaRefeicoesPreDefinidas;
    @FXML private TableColumn<Refeicao, String> colunaNomePreDefinida;
    @FXML private TableColumn<Refeicao, Integer> colunaCaloriasPreDefinida;
    @FXML private TableColumn<Refeicao, String> colunaMacronutrientesPreDefinida; // TIPO ALTERADO

    @FXML private Label mensagemRefeicao;

    private Fachada fachada = Fachada.getInstanciaUnica();
    private MainController mainController;
    private ObservableList<Refeicao> refeicoesPreDefinidas = FXCollections.observableArrayList();

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        configurarTabelaUsuario();
        atualizarTabelaRefeicoesUsuario(); // Adicionado para carregar as refeições do usuário ao iniciar
        configurarTabelaPreDefinida();
        carregarRefeicoesPreDefinidas();
    }

    private void configurarTabelaUsuario() {
        colunaNomeUsuario.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaCaloriasUsuario.setCellValueFactory(new PropertyValueFactory<>("calorias"));
        colunaMacronutrientesUsuario.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMacronutrientesFormatados())); // Usando o método
    }

    private void configurarTabelaPreDefinida() {
        colunaNomePreDefinida.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaCaloriasPreDefinida.setCellValueFactory(new PropertyValueFactory<>("calorias"));
        colunaMacronutrientesPreDefinida.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMacronutrientesFormatados())); // Usando o método
    }

    private void carregarRefeicoesPreDefinidas() {
        try {
            List<Refeicao> refeicoes = InicializadorDados.inicializarRefeicoes();
            refeicoesPreDefinidas.setAll(refeicoes);
            tabelaRefeicoesPreDefinidas.setItems(refeicoesPreDefinidas);
        } catch (Exception e){
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
            atualizarTabelaRefeicoesUsuario(); // Adicionado para atualizar após criar refeição
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

                // Atualizar usuário logado após a edição
                if (mainController != null && mainController.getUsuarioLogado() != null) {
                    try {
                        Usuario usuarioAtualizado = fachada.buscarUsuarioPorId(mainController.getUsuarioLogado().getId());
                        mainController.setUsuarioLogado(usuarioAtualizado);
                        //Notifica:
                        mainController.atualizarDadosTelaPrincipal();
                    } catch (UsuarioNaoEncontradoException e) {
                        showAlert(Alert.AlertType.ERROR, "Erro", "Usuário não encontrado.", "O usuário logado não pôde ser encontrado.");
                    }
                }


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

                // Atualizar usuário logado após a remoção
                if (mainController != null && mainController.getUsuarioLogado() != null) {
                    try {
                        Usuario usuarioAtualizado = fachada.buscarUsuarioPorId(mainController.getUsuarioLogado().getId());
                        mainController.setUsuarioLogado(usuarioAtualizado);
                        //Notifica
                        mainController.atualizarDadosTelaPrincipal();
                    } catch (UsuarioNaoEncontradoException e) {
                        showAlert(Alert.AlertType.ERROR, "Erro", "Usuário não encontrado.", "O usuário logado não pôde ser encontrado.");
                    }
                }

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

        // Verifica se a dieta e a refeição foram selecionadas
        if (mainController != null && mainController.getDietaSelecionada() != null && refeicaoSelecionada != null) {
            try {
                // Obtém a dieta selecionada do MainController
                Dieta dietaSelecionada = mainController.getDietaSelecionada();

                //Antes de adicionar a refeição à dieta, precisamos adicioná-la à tabela de usuario
                // Cria uma *NOVA* instância, copiando os valores.
                Refeicao novaRefeicao = new Refeicao(
                        refeicaoSelecionada.getNome(),
                        refeicaoSelecionada.getDescricao(),
                        new HashMap<>(refeicaoSelecionada.getMacronutrientes()) // Copia o Map
                );
                fachada.configurarRefeicao(novaRefeicao, novaRefeicao.getNome(),
                        novaRefeicao.getDescricao(), novaRefeicao.getMacronutrientes());
                atualizarTabelaRefeicoesUsuario(); //precisa atualizar a tabela aqui para ter a nova refeicao.
                //fim

                System.out.println("RefeicaoController.adicionarRefeicao(): Dieta selecionada: " + dietaSelecionada.getNome());
                System.out.println("RefeicaoController.adicionarRefeicao(): Refeicao selecionada: " + refeicaoSelecionada.getNome());

                //Adiciona essa nova refeição para a dieta.
                fachada.inserirRefeicaoDieta(dietaSelecionada, novaRefeicao);

                System.out.println("RefeicaoController.adicionarRefeicao(): Refeicao adicionada com sucesso.");
                mensagemRefeicao.setText("Refeição adicionada com sucesso!");

                // Atualiza a tela principal (o progresso agora deve mudar)
                mainController.atualizarDadosTelaPrincipal();


            } catch (DietaNaoEncontradaException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Dieta não encontrada", e.getMessage());
            }

        } else {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Seleção Necessária",
                    "Por favor, selecione uma dieta e uma refeição.");
        }
    }

    public void atualizarTabelaRefeicoesUsuario() {
        System.out.println("RefeicaoController.atualizarTabelaRefeicoesUsuario() chamado"); // PRINT
        try {
            List<Refeicao> listaRefeicoes = fachada.listarRefeicoes();
            System.out.println("Refeicoes (todas): " + listaRefeicoes); // PRINT: Todas as refeições

            // Adicione o filtro *correto* pelo usuário logado:
            if (mainController != null && mainController.getUsuarioLogado() != null) {
                // Obtém o usuário logado:
                Usuario usuarioLogado = mainController.getUsuarioLogado();
                System.out.println("Usuário Logado: " + usuarioLogado.getEmail()); // PRINT

                // Obtenha as dietas do usuário *da fachada*:
                List<Dieta> dietasDoUsuario = fachada.listarDietas().stream()
                        .filter(dieta -> dieta.getUsuario() != null && dieta.getUsuario().getId().equals(usuarioLogado.getId()))
                        .collect(Collectors.toList());


                List<Refeicao> refeicoesDoUsuario = listaRefeicoes.stream()
                        .filter(refeicao -> {
                            System.out.println("  Verificando refeição: " + refeicao.getNome()); // PRINT
                            //Verifica se alguma dieta do usuário possui a refeição
                            //AGORA, USANDO A LISTA OBTIDA DA FACHADA:
                            for(Dieta dieta : dietasDoUsuario){
                                // Verifica se a dieta contém a refeição (usando ID):
                                boolean encontrada = dieta.getRefeicoes().stream()
                                        .anyMatch(r -> r.getId().equals(refeicao.getId()));
                                if(encontrada){
                                    return true;
                                }
                            }
                            return false; //Se não, retorna false.
                        })
                        .collect(Collectors.toList());

                System.out.println("Refeicoes do usuário (filtradas): " + refeicoesDoUsuario); // PRINT: Refeições *filtradas*

                // Usa uma ObservableList:
                ObservableList<Refeicao> observableRefeicoes = FXCollections.observableArrayList(refeicoesDoUsuario);
                tabelaRefeicoesUsuario.setItems(observableRefeicoes);

            } else {
                System.out.println("RefeicaoController.atualizarTabelaRefeicoesUsuario(): Usuário logado é nulo."); // PRINT
                tabelaRefeicoesUsuario.setItems(FXCollections.emptyObservableList()); // Lista vazia
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar refeições", e.getMessage());
            e.printStackTrace(); //  Imprime o stack trace!  Muito importante.
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
}