package com.example.queimacaloria.controllers;

import com.example.queimacaloria.interfaces.IBaseAdmin;
import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Usuario;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.Optional;

public class AdminUsuariosController {

    @FXML private TableView<Usuario> tabelaUsuarios;
    @FXML private TableColumn<Usuario, String> colunaNome;
    @FXML private TableColumn<Usuario, String> colunaEmail;
    @FXML private TableColumn<Usuario, String> colunaTipo;

    @FXML private TextField campoNome;
    @FXML private TextField campoEmail;
    @FXML private ComboBox<Usuario.TipoUsuario> campoTipo;
    @FXML private Label mensagem;

    private Fachada fachada = Fachada.getInstanciaUnica();
    @Setter private IBaseAdmin mainController; //Modifiquei Aqui.
    @Setter private Usuario usuarioLogado;
    @Setter private Stage primaryStage;

    @FXML
    public void initialize() {
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colunaTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        campoTipo.setItems(FXCollections.observableArrayList(Usuario.TipoUsuario.values()));

        atualizarTabelaUsuarios();
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    @FXML
    public void criarUsuario() {
        String nome = campoNome.getText();
        String email = campoEmail.getText();
        Usuario.TipoUsuario tipo = campoTipo.getValue();

        try {
            Usuario novoUsuario = fachada.cadastrarUsuario(
                    nome,
                    email,
                    "senhaPadrao",
                    LocalDate.now().minusYears(18),
                    Usuario.Sexo.Masculino,
                    70.0f,
                    170.0f, tipo.getDescricao()
            );
            novoUsuario.setTipo(tipo.getDescricao());
            atualizarTabelaUsuarios();
            mensagem.setText("Usuário criado com sucesso.");
        } catch (Exception e) {
            mensagem.setText("Erro ao criar usuário: " + e.getMessage());
        }
    }

    @FXML
    public void atualizarUsuario() {
        Usuario usuarioSelecionado = tabelaUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioSelecionado != null) {
            try {
                String nome = campoNome.getText();
                String email = campoEmail.getText();
                Usuario.TipoUsuario tipo = campoTipo.getValue();

                /*fachada.atualizarDadosUsuario(usuarioSelecionado, nome, email, null, null, null, 0, 0, tipo);*/
                usuarioSelecionado.setTipo(tipo.getDescricao());
                atualizarTabelaUsuarios();
                mensagem.setText("Usuário atualizado com sucesso.");
            } catch (Exception e) {
                mensagem.setText("Erro ao atualizar usuário: " + e.getMessage());
            }
        } else {
            mensagem.setText("Selecione um usuário para atualizar.");
        }
    }

    @FXML
    public void removerUsuario() {
        Usuario usuarioSelecionado = tabelaUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioSelecionado != null) {
            try {
                fachada.removerUsuario(usuarioSelecionado.getId());
                atualizarTabelaUsuarios();
                mensagem.setText("Usuário removido com sucesso.");
            } catch (UsuarioNaoEncontradoException e) {
                mensagem.setText("Erro ao remover usuário: " + e.getMessage());
            }
        } else {
            mensagem.setText("Selecione um usuário para remover.");
        }
    }

    private void atualizarTabelaUsuarios() {
        List<Usuario> listaDeUsuarios = fachada.listarUsuarios();

        // Use a listaUsuariosComuns para inicializar o tabelaUsuarios
        tabelaUsuarios.setItems(FXCollections.observableArrayList(listaDeUsuarios));
    }

}