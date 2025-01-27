package com.example.queimacaloria.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import com.example.queimacaloria.negocio.ControladorUsuario;
import com.example.queimacaloria.negocio.Fachada;
import com.example.queimacaloria.negocio.Usuario;
import com.example.queimacaloria.dados.RepositorioUsuariosArray;
import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;

import java.util.UUID;


public class UserDashboardController {

    @FXML private Label userNameLabel;

    @FXML private ProgressBar overallProgress;
    @FXML private Label imcLabel;

    @FXML
    private Label ageLabel;


    private Fachada fachada = Fachada.getInstanciaUnica();

    private  Usuario usuarioLogado = null;


    @FXML
    public void initialize() {
        try {
            usuarioLogado = RepositorioUsuariosArray.getInstanciaUnica().buscar(UUID.fromString("d40e143e-7035-4b3f-b38e-70e40c440d12"));

            if (usuarioLogado != null) {
                userNameLabel.setText("Bem-vindo(a), " + usuarioLogado.getNome());
                double progresso = fachada.getProgressoUsuario(usuarioLogado);
                overallProgress.setProgress(progresso/ 100);
                imcLabel.setText("Seu IMC: " + String.format("%.2f", fachada.calcularIMCUsuario(usuarioLogado)));
                ageLabel.setText("Sua idade: " + String.valueOf(fachada.getIdadeUsuario(usuarioLogado)));
            } else {
                userNameLabel.setText("Usuário não logado");
            }
        } catch (UsuarioNaoEncontradoException e) {
            userNameLabel.setText("Usuário não encontrado");
        }


    }
}