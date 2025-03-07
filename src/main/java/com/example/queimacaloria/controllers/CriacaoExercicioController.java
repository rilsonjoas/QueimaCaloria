package com.example.queimacaloria.controllers;

import com.example.queimacaloria.excecoes.ExercicioNaoEncontradoException;
import com.example.queimacaloria.negocio.Exercicio;
import com.example.queimacaloria.negocio.Fachada;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;

public class CriacaoExercicioController {

    @FXML
    private TextField campoNome;
    @FXML
    private TextField campoDescricao;
    @FXML
    private ChoiceBox<Exercicio.TipoExercicio> campoTipo;
    @FXML
    private TextField campoTempo;
    @FXML
    private TextField campoCaloriasQueimadas;

    @FXML
    private Label mensagemErro;

    private Fachada fachada = Fachada.getInstanciaUnica();

    @Setter
    private ExercicioController exercicioController;

    private MainController mainController;

    // Define o controlador principal.
    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }
    // Inicializa o ChoiceBox com os tipos de exercício.
    @FXML
    public void initialize() {
        campoTipo.setItems(FXCollections.observableArrayList(Exercicio.TipoExercicio.values()));
    }

    // Cria um novo exercício.
    @FXML
    public void criarExercicio() {
        String nome = campoNome.getText();
        String descricao = campoDescricao.getText();
        Exercicio.TipoExercicio tipo = campoTipo.getValue();
        String tempoStr = campoTempo.getText();
        String caloriasQueimadasStr = campoCaloriasQueimadas.getText();

        if (!validarFormulario(nome, descricao, tipo, tempoStr, caloriasQueimadasStr)) {
            return;
        }

        try {
            int tempo = Integer.parseInt(tempoStr);
            double caloriasQueimadas = Double.parseDouble(caloriasQueimadasStr);

            Exercicio novoExercicio = new Exercicio();

            // Verifica se há um usuário logado e o associa ao exercício.  Fundamental!
            if (mainController != null && mainController.getUsuarioLogado() != null) {
                novoExercicio.setUsuario(mainController.getUsuarioLogado()); // Define o usuário
            } else {
                mensagemErro.setText("Erro: Usuário não logado."); // Adicionado tratamento de erro.
                return;
            }

            // Agora, a configuração é feita *toda* pela Fachada, e passamos o nivel do usuario.
            fachada.configurarExercicio(novoExercicio, nome, descricao, tipo, tempo, caloriasQueimadas, mainController.getUsuarioLogado(), mainController.getUsuarioLogado().getNivelExperiencia());

            mensagemErro.setText("Exercício criado com sucesso!");

            if (exercicioController != null) {
                //exercicioController.atualizarTabelaExerciciosUsuario(); Removido.  O listener no MainController cuida disso.
            }
            if(mainController != null){
                mainController.getUsuarioLogado().getExercicios().add(novoExercicio); //Adiciona aos exercícios do usuario logado
                mainController.atualizarDadosTelaPrincipal();  // O listener no MainController já cuida da atualização.
            }

            fecharJanela();

        } catch (NumberFormatException e) {
            mensagemErro.setText("Erro: Tempo e calorias devem ser números válidos.");
        } catch (ExercicioNaoEncontradoException e) {
            mensagemErro.setText("Erro ao criar exercicio: " + e.getMessage());
        } catch (Exception e) { // Captura genérica
            mensagemErro.setText("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Função auxiliar para validar o formulário.
    private boolean validarFormulario(String nome, String descricao, Exercicio.TipoExercicio tipo, String tempoStr, String caloriasQueimadasStr) {
        if (nome == null || nome.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O nome não pode estar vazio.");
            return false;
        }

        if (descricao == null || descricao.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "A descrição não pode estar vazia.");
            return false;
        }

        if (tipo == null) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O tipo não pode ser nulo.");
            return false;
        }

        if (tempoStr == null || tempoStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O tempo não pode estar vazio.");
            return false;
        }

        if (!isNumeric(tempoStr)) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O tempo deve ser um número.");
            return false;
        }

        if (caloriasQueimadasStr == null || caloriasQueimadasStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "As calorias queimadas não podem estar vazias.");
            return false;
        }

        if (!isNumeric(caloriasQueimadasStr)) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "As calorias queimadas devem ser um número.");
            return false;
        }

        return true;
    }

    //Função auxiliar para verificar se é um número.
    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Fecha a janela atual.
    @FXML
    private void fecharJanela() {
        Stage stage = (Stage) campoNome.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}