package com.example.queimacaloria.interfaces;

import com.example.queimacaloria.excecoes.ExercicioNaoEncontradoException;
import com.example.queimacaloria.negocio.Exercicio;

import java.util.UUID;

public interface IRepositorioExercicios {

    void adicionar(Exercicio exercicio);

    void atualizar(Exercicio exercicio) throws ExercicioNaoEncontradoException;

    void remover(UUID id) throws ExercicioNaoEncontradoException;

    Exercicio buscar(UUID id) throws ExercicioNaoEncontradoException;
}