package com.example.queimacaloria.negocio;

import java.util.*;

public class ControladorTreino {

    // Construtor com parâmetros
    public void inicializar(Treino treino, String nome, String tipoDeTreino, int duracao,
            int nivelDeDificuldade, Usuario usuario) {

        treino.setNome(nome);
        treino.setTipoDeTreino(tipoDeTreino);
        treino.setDuracao(duracao);
        treino.setNivelDeDificuldade(nivelDeDificuldade);
        treino.setUsuario(usuario);
    }

    // Método para adicionar um exercício ao treino
    public void adicionarExercicio(Treino treino, Exercicio exercicio) {
        if (exercicio != null && !treino.getExercicios().contains(exercicio)) {
            treino.getExercicios().add(exercicio);
            calcularCaloriasQueimadas(treino); // Recalcula as calorias queimadas
            atualizarProgresso(treino); // Atualiza o progresso do treino
        }
    }

    // Método para remover um exercício do treino
    public void removerExercicio(Treino treino, Exercicio exercicio) {
        if (treino.getExercicios().remove(exercicio)) {
            calcularCaloriasQueimadas(treino); // Recalcula as calorias queimadas
            atualizarProgresso(treino); // Atualiza o progresso do treino
        }
    }

    // Método para calcular as calorias queimadas no treino
    public double calcularCaloriasQueimadas(Treino treino) {
        double caloriasQueimadas = 0;
        for (Exercicio exercicio : treino.getExercicios()) {
            ControladorExercicio ce = new ControladorExercicio();
            caloriasQueimadas += ce.calcularCaloriasQueimadas(exercicio);
        }
        treino.setCaloriasQueimadas(caloriasQueimadas);
        return caloriasQueimadas;
    }

    // Método para atualizar o progresso do treino
    public void atualizarProgresso(Treino treino) {
        if (treino.getExercicios().isEmpty()) {
            treino.setProgresso(0.0);
            treino.setConcluido(false);
            return;
        }

        long exerciciosConcluidos = treino.getExercicios().stream().filter(Exercicio::isConcluido).count();
        double progresso = (exerciciosConcluidos / (double) treino.getExercicios().size()) * 100.0;
        treino.setProgresso(progresso);
        treino.setConcluido(progresso == 100.0);
    }

}