package com.example.queimacaloria.negocio;

import com.example.queimacaloria.dados.RepositorioTreinosArray;
import com.example.queimacaloria.excecoes.TreinoNaoEncontradoException;

import java.util.*;

public class ControladorTreino {

    RepositorioTreinosArray repositorio;

    public ControladorTreino() {
        repositorio = RepositorioTreinosArray.getInstanciaUnica();
    }

    // Construtor com parâmetros
    public void inicializar(Treino treino, String nome, String tipoDeTreino, int duracao,
            int nivelDeDificuldade) {

        treino.setNome(nome);
        treino.setTipoDeTreino(tipoDeTreino);
        treino.setDuracao(duracao);
        treino.setNivelDeDificuldade(nivelDeDificuldade);

    }

    // Método para adicionar um exercício ao treino
    public void adicionarExercicio(Treino treino, Exercicio exercicio) throws TreinoNaoEncontradoException {
        if (exercicio != null && !treino.getExercicios().contains(exercicio)) {
            treino.getExercicios().add(exercicio);
            calcularCaloriasQueimadas(treino); // Recalcula as calorias queimadas
            atualizarProgresso(treino); // Atualiza o progresso do treino e salva no repositorio
        }
    }

    // Método para remover um exercício do treino
    public void removerExercicio(Treino treino, Exercicio exercicio) throws TreinoNaoEncontradoException {
        if (exercicio != null && treino.getExercicios().contains(exercicio)) {
            treino.getExercicios().remove(exercicio);
            calcularCaloriasQueimadas(treino); // Recalcula as calorias queimadas
            atualizarProgresso(treino); // Atualiza o progresso do treino e salva no repositorio
        }
    }

    // Método para calcular as calorias queimadas no treino
    public double calcularCaloriasQueimadas(Treino treino) throws TreinoNaoEncontradoException {
        double caloriasQueimadas = 0;
        for (Exercicio exercicio : treino.getExercicios()) {
            ControladorExercicio ce = new ControladorExercicio();
            caloriasQueimadas += ce.calcularCaloriasQueimadas(exercicio);
        }
        treino.setCaloriasQueimadas(caloriasQueimadas);
        repositorio.salvar(treino);
        return caloriasQueimadas;
    }

    // Método para atualizar o progresso do treino
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

}