package com.example.queimacaloria.interfaces;

import com.example.queimacaloria.excecoes.MetaNaoEncontradaException;
import com.example.queimacaloria.negocio.Meta;

import java.util.UUID;

public interface IRepositorioMetas {

    void adicionar(Meta meta) throws MetaNaoEncontradaException;

    void atualizar(UUID id) throws MetaNaoEncontradaException;

    void remover(UUID id) throws MetaNaoEncontradaException;

    Meta buscar(UUID id) throws MetaNaoEncontradaException;
}