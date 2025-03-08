package com.example.queimacaloria.dados;

import com.example.queimacaloria.excecoes.ExercicioNaoEncontradoException;
import com.example.queimacaloria.interfaces.IRepositorioExercicios;
import com.example.queimacaloria.negocio.Exercicio;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RepositorioExerciciosArray implements IRepositorioExercicios {
    private Exercicio[] exercicios;
    private int proximoIndice;
    private static RepositorioExerciciosArray instanciaUnica;

    private RepositorioExerciciosArray() {
        exercicios = new Exercicio[10];
        proximoIndice = 0;
    }

    public static RepositorioExerciciosArray getInstanciaUnica() {
        if (instanciaUnica == null) {
            instanciaUnica = new RepositorioExerciciosArray();
        }
        return instanciaUnica;
    }

    private int procurarIndice(UUID id) {
        if (id == null) throw new IllegalArgumentException("ID não pode ser nulo.");
        for (int i = 0; i < proximoIndice; i++) {
            if (id.equals(exercicios[i].getId())) return i;
        }
        return proximoIndice;
    }

    @Override
    public void adicionar(Exercicio exercicio) throws ExercicioNaoEncontradoException {
        if (exercicio == null) throw new IllegalArgumentException("Exercício não pode ser nulo.");
        if (proximoIndice >= exercicios.length) {
            Exercicio[] temp = new Exercicio[exercicios.length + 10];
            System.arraycopy(exercicios, 0, temp, 0, exercicios.length);
            exercicios = temp;
        }
        exercicios[proximoIndice++] = exercicio;
    }

    @Override
    public void salvar(Exercicio exercicio) throws ExercicioNaoEncontradoException {
        if (exercicio == null) throw new IllegalArgumentException("Exercício não pode ser nulo.");
        int indice = procurarIndice(exercicio.getId());
        if (indice < proximoIndice) {
            exercicios[indice] = exercicio;
        } else {
            throw new ExercicioNaoEncontradoException("Exercício não encontrado.");
        }
    }

    @Override
    public void remover(UUID id) throws ExercicioNaoEncontradoException {
        if (id == null) throw new IllegalArgumentException("ID não pode ser nulo.");
        int indice = procurarIndice(id);
        if (indice < proximoIndice) {
            exercicios[indice] = exercicios[proximoIndice - 1];
            exercicios[--proximoIndice] = null;
        } else {
            throw new ExercicioNaoEncontradoException("Exercício não encontrado.");
        }
    }

    @Override
    public Exercicio buscar(UUID id) throws ExercicioNaoEncontradoException {
        if (id == null) throw new IllegalArgumentException("ID não pode ser nulo.");
        int indice = procurarIndice(id);
        if (indice < proximoIndice) {
            return exercicios[indice];
        } else {
            throw new ExercicioNaoEncontradoException("Exercício não encontrado.");
        }
    }

    @Override
    public List<Exercicio> getAll() {
        System.out.println("RepositorioExerciciosArray.getAll() chamado");
        List<Exercicio> lista = new ArrayList<>();
        for (int i = 0; i < proximoIndice; i++) {
            if (exercicios[i] != null) {
                lista.add(exercicios[i]);
            }
        }
        System.out.println("RepositorioExerciciosArray.getAll(): Retornando lista: " + lista);
        return lista;
    }
}