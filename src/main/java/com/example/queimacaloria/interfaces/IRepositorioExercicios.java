package com.example.queimacaloria.interfaces;

import com.example.queimacaloria.excecoes.ExercicioNaoEncontradoException;
import com.example.queimacaloria.negocio.Exercicio;

import java.util.List;
import java.util.UUID;

public interface IRepositorioExercicios {

    void adicionar(Exercicio exercicio) throws ExercicioNaoEncontradoException;

    void salvar(Exercicio exercicio) throws ExercicioNaoEncontradoException;

    void remover(UUID id) throws ExercicioNaoEncontradoException;

    Exercicio buscar(UUID id) throws ExercicioNaoEncontradoException;

    List<Exercicio> getAll();
}