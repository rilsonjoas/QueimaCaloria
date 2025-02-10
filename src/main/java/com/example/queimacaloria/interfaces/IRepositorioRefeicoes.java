package com.example.queimacaloria.interfaces;

import com.example.queimacaloria.excecoes.RefeicaoNaoEncontradaException;
import com.example.queimacaloria.negocio.Refeicao;

import java.util.List;
import java.util.UUID;

public interface IRepositorioRefeicoes {

    void adicionar(Refeicao refeicao) throws RefeicaoNaoEncontradaException; // Adicionada a exception

    void salvar(Refeicao refeicao) throws RefeicaoNaoEncontradaException;

    void remover(UUID id) throws RefeicaoNaoEncontradaException;

    Refeicao buscar(UUID id) throws RefeicaoNaoEncontradaException;

    List<Refeicao> getAll();
}