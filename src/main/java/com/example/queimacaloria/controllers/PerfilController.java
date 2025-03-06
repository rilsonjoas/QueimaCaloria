package com.example.queimacaloria.controllers;

import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Usuario;
import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.beans.binding.Bindings;
import javafx.util.converter.NumberStringConverter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PerfilController {

    @FXML private Label labelNomeAtual;
    @FXML private Label labelEmailAtual;
    @FXML private Label labelPesoAtual;
    @FXML private Label labelAlturaAtual;
    @FXML private Label labelIMCatual;
    @FXML private Label labelCinturaAtual;
    @FXML private Label labelBicepsAtual;
    @FXML private Label labelCoxaAtual;
    @FXML private Label labelQuadrilAtual;
    @FXML private Label mensagemPerfil;

    @FXML private TextField campoNome;
    @FXML private TextField campoEmail;
    @FXML private TextField campoPeso;
    @FXML private TextField campoAltura;
    @FXML private TextField campoCintura;
    @FXML private TextField campoBiceps;
    @FXML private TextField campoCoxa;
    @FXML private TextField campoQuadril;


    private Fachada fachada = Fachada.getInstanciaUnica();
    private MainController mainController;
    private Usuario usuarioLogado;

    // Define o controlador principal.
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    // Define o usuário logado e configura os bindings.
    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
        bindLabels();
    }

    // Configura os bindings dos labels e campos de texto.
    private void bindLabels() {
        if (usuarioLogado != null) {
            labelNomeAtual.textProperty().bind(usuarioLogado.nomeProperty());
            labelEmailAtual.textProperty().bind(usuarioLogado.emailProperty());

            labelPesoAtual.textProperty().bind(Bindings.createStringBinding(
                    () -> String.format("%.2f", usuarioLogado.getPeso()),
                    usuarioLogado.pesoProperty()
            ));
            labelAlturaAtual.textProperty().bind(Bindings.createStringBinding(
                    () -> String.format("%.0f", usuarioLogado.getAltura()),
                    usuarioLogado.alturaProperty()
            ));
            labelIMCatual.textProperty().bind(Bindings.createStringBinding(
                    () -> String.format("%.2f", usuarioLogado.getImc()),
                    usuarioLogado.imcProperty()
            ));

            campoNome.textProperty().bindBidirectional(usuarioLogado.nomeProperty());
            campoEmail.textProperty().bindBidirectional(usuarioLogado.emailProperty());
            campoPeso.textProperty().bindBidirectional(usuarioLogado.pesoProperty(), new NumberStringConverter());
            campoAltura.textProperty().bindBidirectional(usuarioLogado.alturaProperty(), new NumberStringConverter());
            campoCintura.textProperty().bindBidirectional(usuarioLogado.cinturaProperty(), new NumberStringConverter());
            campoBiceps.textProperty().bindBidirectional(usuarioLogado.bicepsProperty(), new NumberStringConverter());
            campoCoxa.textProperty().bindBidirectional(usuarioLogado.coxaProperty(), new NumberStringConverter());
            campoQuadril.textProperty().bindBidirectional(usuarioLogado.quadrilProperty(), new NumberStringConverter());

            labelCinturaAtual.textProperty().bind(Bindings.createStringBinding(() -> String.format("%.1f cm", usuarioLogado.getCintura()), usuarioLogado.cinturaProperty()));
            labelBicepsAtual.textProperty().bind(Bindings.createStringBinding(() -> String.format("%.1f cm", usuarioLogado.getBiceps()), usuarioLogado.bicepsProperty()));
            labelCoxaAtual.textProperty().bind(Bindings.createStringBinding(() -> String.format("%.1f cm", usuarioLogado.getCoxa()), usuarioLogado.coxaProperty()));
            labelQuadrilAtual.textProperty().bind(Bindings.createStringBinding(() -> String.format("%.1f cm", usuarioLogado.getQuadril()), usuarioLogado.quadrilProperty()));
        }
    }

    // Atualiza os dados do perfil do usuário.
    @FXML
    public void atualizarPerfil() {
        if (usuarioLogado == null) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Usuário não logado", "Não há um usuário logado para atualizar.");
            return;
        }

        String nome = campoNome.getText();
        String email = campoEmail.getText();
        String pesoStr = campoPeso.getText();
        String alturaStr = campoAltura.getText();

        //Valida os campos.
        if (!validarFormulario(nome, email, pesoStr, alturaStr)) {
            return;
        }

        try {
            float peso = Float.parseFloat(pesoStr);
            float altura = Float.parseFloat(alturaStr);
            //agora chamamos o metodo certo passando os 8 argumentos esperados.
            fachada.atualizarDadosUsuario(usuarioLogado, nome, email, null, null, null, peso, altura, usuarioLogado.getTipo(),
                    Double.parseDouble(campoCintura.getText()),
                    Double.parseDouble(campoBiceps.getText()),
                    Double.parseDouble(campoCoxa.getText()),
                    Double.parseDouble(campoQuadril.getText()));
            mensagemPerfil.setText("Perfil atualizado com sucesso!");
            if(mainController != null){
                mainController.atualizarDadosTelaPrincipal(); //Atualiza os dados.
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Dados inválidos", "Peso, altura, e medidas devem ser números válidos.");
        }  catch (UsuarioNaoEncontradoException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Usuário não encontrado", e.getMessage());
        } catch (Exception e) { //  Captura genérica para outros erros.
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar perfil", e.getMessage());
            e.printStackTrace();
        }
    }

    //Função auxiliar para validar o formulário.
    private boolean validarFormulario(String nome, String email, String pesoStr, String alturaStr) {
        if (nome == null || nome.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O nome não pode estar vazio.");
            return false;
        }

        if (email == null || email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O email não pode estar vazio.");
            return false;
        }

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O email não é válido.");
            return false;
        }

        if (pesoStr == null || pesoStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O peso não pode estar vazio.");
            return false;
        }

        if (!isNumeric(pesoStr)) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O peso deve ser um número.");
            return false;
        }

        if (alturaStr == null || alturaStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "A altura não pode estar vazia.");
            return false;
        }

        if (!isNumeric(alturaStr)) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "A altura deve ser um número.");
            return false;
        }

        //Validando os campos de medidas.
        if (!isNumeric(campoCintura.getText()) || campoCintura.getText().isEmpty()){
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo Inválido", "A medida da cintura deve ser um número.");
            return false;
        }

        if (!isNumeric(campoBiceps.getText()) || campoBiceps.getText().isEmpty()){
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo Inválido", "A medida do bíceps deve ser um número.");
            return false;
        }

        if (!isNumeric(campoCoxa.getText()) || campoCoxa.getText().isEmpty()){
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo Inválido", "A medida da coxa deve ser um número.");
            return false;
        }

        if (!isNumeric(campoQuadril.getText()) || campoQuadril.getText().isEmpty()){
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo Inválido", "A medida do quadril deve ser um número.");
            return false;
        }

        return true;
    }

    //Função auxiliar para validar se o email é válido.
    private boolean isValidEmail(String email) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //Função auxiliar para validar se é um valor numérico
    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Volta para a tela principal.
    @FXML
    public void voltarParaTelaPrincipal() {
        if (mainController != null) {
            mainController.mostrarTelaPrincipal();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro interno", "MainController não foi configurado corretamente.");
        }
    }

    // Exibe um alerta na tela.
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}