package com.example.queimacaloria.interfaces;

import com.example.queimacaloria.excecoes.RefeicaoNaoEncontradaException;
import com.example.queimacaloria.negocio.Refeicao;

import java.util.UUID;

public interface IRepositorioRefeicoes {

    public void adicionar(Refeicao refeicao) throws RefeicaoNaoEncontradaException;

    public void salvar(Refeicao refeicao) throws RefeicaoNaoEncontradaException;

    public void remover(UUID id) throws RefeicaoNaoEncontradaException;

    public Refeicao buscar(UUID id) throws RefeicaoNaoEncontradaException;
}