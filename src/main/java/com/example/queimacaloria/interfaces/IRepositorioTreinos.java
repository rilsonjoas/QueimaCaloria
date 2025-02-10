package com.example.queimacaloria.interfaces;

import com.example.queimacaloria.excecoes.TreinoNaoEncontradoException;
import com.example.queimacaloria.negocio.Treino;

import java.util.List;
import java.util.UUID;

public interface IRepositorioTreinos {

    void adicionar(Treino treino) throws TreinoNaoEncontradoException;

    void salvar(Treino treino) throws TreinoNaoEncontradoException;

    void remover(UUID id) throws TreinoNaoEncontradoException;

    Treino buscar(UUID id) throws TreinoNaoEncontradoException;

    List<Treino> getAll();
}