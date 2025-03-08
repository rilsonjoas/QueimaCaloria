package com.example.queimacaloria.controllers;

import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Usuario;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroController {

    @FXML private TextField campoNome;
    @FXML private TextField campoEmail;
    @FXML private PasswordField campoSenha;
    @FXML private DatePicker campoDataNascimento;
    @FXML private ComboBox<Usuario.Sexo> campoSexo;
    @FXML private TextField campoPeso;
    @FXML private TextField campoAltura;
    @FXML private Label mensagemRegistro;

    //Novos campos
    @FXML private ChoiceBox<Usuario.TipoDieta> campoTipoDieta;
    @FXML private ListView<Usuario.RestricaoAlimentar> listaRestricoes; // ListView, não mais TextField
    @FXML private ChoiceBox<Usuario.NivelExperiencia> campoNivelExperiencia;
    @FXML private TextField campoCintura;
    @FXML private TextField campoBiceps;
    @FXML private TextField campoCoxa;
    @FXML private TextField campoQuadril;



    private final Fachada fachada = Fachada.getInstanciaUnica();
    @FXML
    @Setter
    private AuthController authController;


    @FXML
    public void initialize() {
        campoSexo.setItems(FXCollections.observableArrayList(Usuario.Sexo.values()));

        // Inicializa os ChoiceBox
        campoTipoDieta.setItems(FXCollections.observableArrayList(Usuario.TipoDieta.values()));
        campoNivelExperiencia.setItems(FXCollections.observableArrayList(Usuario.NivelExperiencia.values()));

        // Configura a ListView para seleção múltipla
        listaRestricoes.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listaRestricoes.setItems(FXCollections.observableArrayList(Usuario.RestricaoAlimentar.values()));

        // ADICIONE ESTE BLOCO DE CÓDIGO: Configura o cellFactory para usar CheckBoxListCell
        listaRestricoes.setCellFactory(CheckBoxListCell.forListView(new Callback<Usuario.RestricaoAlimentar, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(Usuario.RestricaoAlimentar item) {
                BooleanProperty observable = new SimpleBooleanProperty();
                // Listener para ATUALIZAR a seleção quando o checkbox mudar.
                observable.addListener((obs, wasSelected, isNowSelected) -> {
                    if (isNowSelected) {
                        listaRestricoes.getSelectionModel().select(item);
                    } else {
                        listaRestricoes.getSelectionModel().clearSelection(listaRestricoes.getItems().indexOf(item));
                    }
                });

                //  É preciso setar o valor inicial do checkbox, baseado no que veio da seleção
                observable.set(listaRestricoes.getSelectionModel().getSelectedItems().contains(item));
                return observable;
            }
        }, new StringConverter<>() {
            @Override
            public String toString(Usuario.RestricaoAlimentar object) {
                return object == null ? "" : object.toString();
            }

            @Override
            public Usuario.RestricaoAlimentar fromString(String string) {
                return null;  // Não precisamos converter de volta.
            }
        }));
    }

    @FXML
    public void registrar() {
        String nome = campoNome.getText();
        String email = campoEmail.getText();
        String password = campoSenha.getText();
        LocalDate dataNascimento = campoDataNascimento.getValue();
        Usuario.Sexo sexo = campoSexo.getValue();
        String pesoStr = campoPeso.getText();
        String alturaStr = campoAltura.getText();


        //Valida os campos.
        if (!validarFormulario(nome, email, password, dataNascimento, sexo, pesoStr, alturaStr)) {
            return; // Aborta se a validação falhar
        }

        try {
            float peso = Float.parseFloat(pesoStr);
            float altura = Float.parseFloat(alturaStr);

            // Obtém os valores dos novos campos
            Usuario.TipoDieta tipoDieta = campoTipoDieta.getValue();
            Usuario.NivelExperiencia nivelExperiencia = campoNivelExperiencia.getValue();

            // Obtém as restrições selecionadas *ANTES* de criar o usuário
            Set<Usuario.RestricaoAlimentar> restricoes = new HashSet<>(listaRestricoes.getSelectionModel().getSelectedItems());


            // Correção: Passando todos os 8 argumentos esperados, incluindo tipoDieta, nivel, e restricoes
            Usuario novoUsuario = fachada.cadastrarUsuario(
                    nome,
                    email,
                    password,
                    dataNascimento,
                    sexo,
                    peso,
                    altura,
                    Usuario.TipoUsuario.USUARIO_COMUM.getDescricao() // Tipo padrão
            );

            //AGORA, COM O USUARIO CRIADO, ADICIONA AS NOVAS INFORMAÇÕES.
            if (tipoDieta != null) {
                novoUsuario.tipoDietaProperty().set(tipoDieta); //Usa o setter do Property
            }
            if (nivelExperiencia != null) {
                novoUsuario.nivelExperienciaProperty().set(nivelExperiencia);
            }

            // Definir as restrições, mas verifique se restricoes não é nulo.
            if (restricoes != null && !restricoes.isEmpty()) {
                novoUsuario.setRestricoes(FXCollections.observableSet(restricoes));  // Define o ObservableSet diretamente!
            }

            //Novas métricas:
            if (campoCintura.getText() != null && !campoCintura.getText().isEmpty()) {
                novoUsuario.setCintura(Double.parseDouble(campoCintura.getText()));
            }
            if (campoBiceps.getText() != null && !campoBiceps.getText().isEmpty()) {
                novoUsuario.setBiceps(Double.parseDouble(campoBiceps.getText()));
            }
            if (campoCoxa.getText() != null && !campoCoxa.getText().isEmpty()) {
                novoUsuario.setCoxa(Double.parseDouble(campoCoxa.getText()));
            }
            if (campoQuadril.getText() != null && !campoQuadril.getText().isEmpty()) {
                novoUsuario.setQuadril(Double.parseDouble(campoQuadril.getText()));
            }

            mensagemRegistro.setText("Usuário cadastrado com sucesso!");

            // ----  ALERTA AQUI -----
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Bem-vindo!");
            alert.setHeaderText("Cadastro realizado com sucesso!");
            alert.setContentText("Olá, " + novoUsuario.getNome() + "! Seja bem-vindo ao YouFit. Lembre-se de beber água para se manter hidratado! Você pode registrar seu consumo na tela principal.");
            alert.showAndWait(); // Mostra e espera
            // ------------------------

            if (authController != null) {
                authController.mostrarTelaPrincipal(getPrimaryStage(), novoUsuario);
            } else {
                // Tratar erro
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao mostrar a tela principal", "AuthController não está definido.");
            }
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao cadastrar usuário", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro inesperado", e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para validar o formulário
    private boolean validarFormulario(String nome, String email, String password, LocalDate dataNascimento,
                                      Usuario.Sexo sexo, String pesoStr, String alturaStr) {
        if (nome == null || nome.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O nome não pode estar vazio.");
            return false;
        }

        if (email == null || email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O e-mail não pode estar vazio.");
            return false;
        }

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O e-mail não é válido.");
            return false;
        }

        if (password == null || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "A senha não pode estar vazia.");
            return false;
        }

        //Data de nascimento não é mais obrigatória
        /*if (dataNascimento == null) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "A data de nascimento não pode ser nula.");
            return false;
        }*/

        //Sexo não é mais obrigatório
        /*if (sexo == null) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campo inválido", "O sexo não pode ser nulo.");
            return false;
        }*/

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

        return true;
    }

    //Função auxiliar para validar se o email é válido
    private boolean isValidEmail(String email) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(email);
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

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void irParaLogin() {
        if (authController != null) {
            authController.mostrarTelaLogin();
        } else {
            System.err.println("Erro: AuthController não foi injetado!");
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro interno", "AuthController não foi configurado corretamente.");
        }
    }

    private Stage getPrimaryStage() {
        if (authController != null) {
            return (Stage) campoEmail.getScene().getWindow();
        }
        return null;
    }
}