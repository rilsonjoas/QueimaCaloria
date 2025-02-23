package com.example.queimacaloria.negocio;

import com.example.queimacaloria.dados.RepositorioTreinosArray;
import com.example.queimacaloria.excecoes.ExercicioNaoEncontradoException;
import com.example.queimacaloria.excecoes.TreinoNaoEncontradoException;

import java.util.List;
import java.util.UUID;

public class ControladorTreino {

    private RepositorioTreinosArray repositorio;

    // Construtor, inicializa o repositório.
    public ControladorTreino() {
        this.repositorio = RepositorioTreinosArray.getInstanciaUnica();
    }

    // Inicializa um treino, atualizando ou adicionando ao repositório.
    public void inicializar(Treino treino, String nome, String tipoDeTreino, int duracao, int nivelDeDificuldade) throws TreinoNaoEncontradoException {
        treino.setNome(nome);
        treino.setTipoDeTreino(tipoDeTreino);
        treino.setDuracao(duracao);
        treino.setNivelDeDificuldade(nivelDeDificuldade);

        try {
            repositorio.salvar(treino);
        } catch (TreinoNaoEncontradoException e) {
            repositorio.adicionar(treino);
        }
    }

    // Adiciona um exercício ao treino.
    public void adicionarExercicio(Treino treino, Exercicio exercicio) throws TreinoNaoEncontradoException, ExercicioNaoEncontradoException {
        if (exercicio != null && !treino.getExercicios().contains(exercicio)) {
            treino.getExercicios().add(exercicio);
            Fachada.getInstanciaUnica().atualizarTreino(treino);
            repositorio.salvar(treino);
        }
    }

    // Remove um exercício do treino.
    public void removerExercicio(Treino treino, Exercicio exercicio) throws TreinoNaoEncontradoException, ExercicioNaoEncontradoException {
        if (exercicio != null && treino.getExercicios().contains(exercicio)) {
            treino.getExercicios().remove(exercicio);
            Fachada.getInstanciaUnica().atualizarTreino(treino);
            repositorio.salvar(treino);
        }
    }

    // Atualiza o progresso do treino.
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

    // Lista todos os treinos do repositório.
    public List<Treino> listarTreinos() {
        return repositorio.getAll();
    }

    // Remove um treino pelo ID.
    public void remover(UUID id) throws TreinoNaoEncontradoException {
        repositorio.remover(id);
    }
}