package com.example.queimacaloria.negocio;

import com.example.queimacaloria.dados.RepositorioExerciciosArray;
import com.example.queimacaloria.excecoes.ExercicioNaoEncontradoException;

import java.util.*;

public class ControladorExercicio {

    RepositorioExerciciosArray repositorio;

    public ControladorExercicio() {
        this.repositorio = RepositorioExerciciosArray.getInstanciaUnica(); // Singleton!
    }

    public void inicializar(Exercicio exercicio, String nome, String descricao, Exercicio.TipoExercicio tipo,
                            int tempo, double caloriasQueimadas) throws ExercicioNaoEncontradoException {
        exercicio.setNome(nome);
        exercicio.setDescricao(descricao);
        exercicio.setTipo(tipo);
        exercicio.setTempo(tempo);
        exercicio.setCaloriasQueimadas(caloriasQueimadas);

        try {
            repositorio.salvar(exercicio);
        } catch (ExercicioNaoEncontradoException e) {
            repositorio.adicionar(exercicio);
        }
    }


    public void adicionarMusculoTrabalhado(Exercicio exercicio, String musculo) throws ExercicioNaoEncontradoException {
        if (musculo != null && !exercicio.getMusculosTrabalhados().contains(musculo)) {
            exercicio.getMusculosTrabalhados().add(musculo);
            repositorio.salvar(exercicio);
        }
    }


    public void removerMusculoTrabalhado(Exercicio exercicio, String musculo) throws ExercicioNaoEncontradoException {
        exercicio.getMusculosTrabalhados().remove(musculo);
        repositorio.salvar(exercicio);
    }

    public void concluir(Exercicio exercicio) throws ExercicioNaoEncontradoException {
        exercicio.setConcluido(true);
        repositorio.salvar(exercicio);
    }

    public List<Exercicio> listarExercicios() {
        if (repositorio == null) {
            System.err.println("ERRO CRÍTICO: Repositório nulo em ControladorExercicio.listarExercicios()!");
        }
        List<Exercicio> exercicios = repositorio.getAll();
        System.out.println("ControladorExercicio.listarExercicios(): Exercicios retornados: " + exercicios); // LOG
        return exercicios;
    }


    public void remover(UUID id) throws ExercicioNaoEncontradoException {
        repositorio.remover(id);
    }
}