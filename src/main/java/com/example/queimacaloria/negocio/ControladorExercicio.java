package com.example.queimacaloria.negocio;

import java.util.*;

public class ControladorExercicio {

    // Construtor com parâmetros
    public void inicializar(Exercicio exercicio, String nome, String descricao, Exercicio.TipoExercicio tipo,
            int tempo, double caloriasQueimadasPorMinuto) {
        exercicio.setNome(nome);
        exercicio.setDescricao(descricao);
        exercicio.setTipo(tipo);
        exercicio.setTempo(tempo);
        exercicio.setCaloriasQueimadasPorMinuto(caloriasQueimadasPorMinuto);
    }

    // Adiciona um músculo à lista de músculos trabalhados, verificando se já existe
    public void adicionarMusculoTrabalhado(Exercicio exercicio, String musculo) {
        if (musculo != null && !exercicio.getMusculosTrabalhados().contains(musculo)) {
            exercicio.getMusculosTrabalhados().add(musculo);
        }
    }

    // Remove um músculo da lista de músculos trabalhados
    public void removerMusculoTrabalhado(Exercicio exercicio, String musculo) {
        exercicio.getMusculosTrabalhados().remove(musculo);
    }

    // Calcula o total de calorias queimadas durante o exercício
    public double calcularCaloriasQueimadas(Exercicio exercicio) {
        double caloriasQueimadas = (exercicio.getTempo() / 60.0) * exercicio.getCaloriasQueimadasPorMinuto();
        exercicio.setCaloriasQueimadas(caloriasQueimadas);
        return caloriasQueimadas;
    }

    // Marca o exercício como concluído
    public void concluir(Exercicio exercicio) {
        exercicio.setConcluido(true);
    }

}
