package com.example.queimacaloria.negocio;

import com.example.queimacaloria.dados.RepositorioExerciciosArray;
import com.example.queimacaloria.excecoes.ExercicioNaoEncontradoException;

import java.util.*;

public class ControladorExercicio {

    RepositorioExerciciosArray repositorio;

    // Construtor, inicializa o repositório.
    public ControladorExercicio() {
        repositorio = RepositorioExerciciosArray.getInstanciaUnica();
    }

    // Inicializa um exercício, atualizando ou adicionando ao repositório.
    // SIMPLIFICADO: Agora caloriasQueimadas já é a taxa por minuto.
    public void inicializar(Exercicio exercicio, String nome, String descricao, Exercicio.TipoExercicio tipo,
                            int tempo, double caloriasQueimadas) throws ExercicioNaoEncontradoException {
        exercicio.setNome(nome);
        exercicio.setDescricao(descricao);
        exercicio.setTipo(tipo);
        exercicio.setTempo(tempo);
        exercicio.setCaloriasQueimadas(caloriasQueimadas); // Direto, sem cálculo


        try {
            repositorio.salvar(exercicio);
        } catch (ExercicioNaoEncontradoException e) {
            repositorio.adicionar(exercicio);
        }

    }

    // Adiciona um músculo à lista de músculos trabalhados do exercício.
    public void adicionarMusculoTrabalhado(Exercicio exercicio, String musculo) throws ExercicioNaoEncontradoException {
        if (musculo != null && !exercicio.getMusculosTrabalhados().contains(musculo)) {
            exercicio.getMusculosTrabalhados().add(musculo);
            repositorio.salvar(exercicio);
        }
    }

    // Remove um músculo da lista de músculos trabalhados do exercício.
    public void removerMusculoTrabalhado(Exercicio exercicio, String musculo) throws ExercicioNaoEncontradoException {
        exercicio.getMusculosTrabalhados().remove(musculo);
        repositorio.salvar(exercicio);
    }


    // Marca o exercício como concluído.
    public void concluir(Exercicio exercicio) throws ExercicioNaoEncontradoException {
        exercicio.setConcluido(true);
        repositorio.salvar(exercicio);
    }

    // Lista todos os exercícios do repositório.
    public List<Exercicio> listarExercicios() {
        return repositorio.getAll();
    }

    // Remove um exercício pelo ID.
    public void remover(UUID id) throws ExercicioNaoEncontradoException {
        repositorio.remover(id);
    }
}