package com.example.queimacaloria.negocio;

import com.example.queimacaloria.dados.RepositorioExerciciosArray;
import com.example.queimacaloria.excecoes.ExercicioNaoEncontradoException;

import java.util.*;

public class ControladorExercicio {

    RepositorioExerciciosArray repositorio;

    public ControladorExercicio() {
        repositorio = RepositorioExerciciosArray.getInstanciaUnica();
    }

    // Construtor com parâmetros
    public void inicializar(Exercicio exercicio, String nome, String descricao, Exercicio.TipoExercicio tipo,
            int tempo, double caloriasQueimadasPorMinuto) throws ExercicioNaoEncontradoException {
        exercicio.setNome(nome);
        exercicio.setDescricao(descricao);
        exercicio.setTipo(tipo);
        exercicio.setTempo(tempo);
        exercicio.setCaloriasQueimadasPorMinuto(caloriasQueimadasPorMinuto);

        // Importante: Se o exercício já existe, usa salvar. Se não existe, adiciona.
        try {
            repositorio.salvar(exercicio);
        } catch (ExercicioNaoEncontradoException e) {
            repositorio.adicionar(exercicio);
        }

    }

    // Adiciona um músculo à lista de músculos trabalhados, verificando se já existe
    public void adicionarMusculoTrabalhado(Exercicio exercicio, String musculo) throws ExercicioNaoEncontradoException {
        if (musculo != null && !exercicio.getMusculosTrabalhados().contains(musculo)) {
            exercicio.getMusculosTrabalhados().add(musculo);
            repositorio.salvar(exercicio);
        }
    }

    // Remove um músculo da lista de músculos trabalhados
    public void removerMusculoTrabalhado(Exercicio exercicio, String musculo) throws ExercicioNaoEncontradoException {
        exercicio.getMusculosTrabalhados().remove(musculo);
        repositorio.salvar(exercicio);
    }

    // Calcula o total de calorias queimadas durante o exercício
    public double calcularCaloriasQueimadas(Exercicio exercicio) throws ExercicioNaoEncontradoException {
        double caloriasQueimadas = (exercicio.getTempo() / 60.0) * exercicio.getCaloriasQueimadasPorMinuto();
        exercicio.setCaloriasQueimadas(caloriasQueimadas);
        repositorio.salvar(exercicio);
        return caloriasQueimadas;
    }

    // Marca o exercício como concluído
    public void concluir(Exercicio exercicio) throws ExercicioNaoEncontradoException {
        exercicio.setConcluido(true);
        repositorio.salvar(exercicio);
    }

    // Método para listar todos os exercícios
    public List<Exercicio> listarExercicios() {
        return repositorio.getAll();
    }
}