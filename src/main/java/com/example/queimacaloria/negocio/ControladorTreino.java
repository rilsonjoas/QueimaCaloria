package com.example.queimacaloria.negocio;

import com.example.queimacaloria.dados.RepositorioTreinosArray;
import com.example.queimacaloria.excecoes.ExercicioNaoEncontradoException;
import com.example.queimacaloria.excecoes.TreinoNaoEncontradoException;
import javafx.beans.property.ObjectProperty;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ControladorTreino {

    private RepositorioTreinosArray repositorio;

    public ControladorTreino() {
        this.repositorio = RepositorioTreinosArray.getInstanciaUnica(); // Singleton!
    }

    public void inicializar(Treino treino, String nome, Exercicio.TipoExercicio tipoDeTreino, int duracao, int nivelDeDificuldade, Usuario usuario, Usuario.NivelExperiencia nivel) throws TreinoNaoEncontradoException {
        treino.setNome(nome);
        treino.setTipoDeTreino(tipoDeTreino);
        treino.setDuracao(duracao);
        treino.setNivelDeDificuldade(nivelDeDificuldade);
        treino.setUsuario(usuario);
        treino.setNivelExperiencia(nivel);

        try {
            repositorio.salvar(treino);
        } catch (TreinoNaoEncontradoException e) {
            repositorio.adicionar(treino);
        }
    }


    public void adicionarExercicio(Treino treino, Exercicio exercicio) throws TreinoNaoEncontradoException, ExercicioNaoEncontradoException {
        if (exercicio != null && !treino.getExercicios().contains(exercicio)) {
            treino.getExercicios().add(exercicio);
            Fachada.getInstanciaUnica().atualizarTreino(treino);
            repositorio.salvar(treino);
        }
    }


    public void removerExercicio(Treino treino, Exercicio exercicio) throws TreinoNaoEncontradoException, ExercicioNaoEncontradoException {
        if (exercicio != null && treino.getExercicios().contains(exercicio)) {
            treino.getExercicios().remove(exercicio);
            Fachada.getInstanciaUnica().atualizarTreino(treino);
            repositorio.salvar(treino);
        }
    }

    public void atualizarProgresso(Treino treino) throws TreinoNaoEncontradoException {
        if (treino.getExercicios().isEmpty()) {
            treino.setProgresso(0.0);
            treino.setConcluido(false);
            repositorio.salvar(treino);
            return;
        }

        long exerciciosConcluidos = treino.getExercicios().stream().filter(Exercicio::isConcluido).count();
        double progresso = (exerciciosConcluidos / (double) treino.getExercicios().size()) * 100.0;
        treino.setProgresso(progresso);
        treino.setConcluido(progresso == 100.0);
        repositorio.salvar(treino);
    }

    public List<Treino> listarTreinos() {
        if (repositorio == null) {
            System.err.println("ERRO CRÍTICO: Repositório nulo em ControladorTreino.listarTreinos()!");
        }
        List<Treino> treinos = repositorio.getAll();
        System.out.println("ControladorTreino.listarTreinos(): Treinos retornados: " + treinos);
        return treinos;
    }

    public List<Treino> listarTreinos(Usuario.NivelExperiencia nivel) {
        return repositorio.getAll().stream()
                .filter(treino -> treino.getNivelExperiencia() == nivel)
                .collect(Collectors.toList());
    }

    public void remover(UUID id) throws TreinoNaoEncontradoException {
        repositorio.remover(id);
    }
}