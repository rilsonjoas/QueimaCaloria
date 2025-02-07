package com.example.queimacaloria.interfaces;

import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;
import com.example.queimacaloria.negocio.Dieta;

import java.util.UUID;

// Interface que define os métodos para um repositório de dietas.
public interface IRepositorioDietas {

    // Adiciona uma nova dieta ao repositório.
    void adicionar(Dieta dieta) throws DietaNaoEncontradaException;

    // Salva (atualiza) uma dieta existente no repositório.
    void salvar(Dieta dieta) throws DietaNaoEncontradaException;

    // Remove uma dieta do repositório pelo seu ID.
    void remover(UUID id) throws DietaNaoEncontradaException;

    // Busca uma dieta no repositório pelo seu ID.
    Dieta buscar(UUID id) throws DietaNaoEncontradaException;
}
