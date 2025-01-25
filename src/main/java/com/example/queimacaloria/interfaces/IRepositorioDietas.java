package com.example.queimacaloria.interfaces;

import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;
import com.example.queimacaloria.negocio.Dieta;

import java.util.UUID;

public interface IRepositorioDietas {

    void adicionar(Dieta dieta);

    void atualizar(Dieta dieta) throws DietaNaoEncontradaException;

    void remover(UUID id) throws DietaNaoEncontradaException;

    Dieta buscar(UUID id) throws DietaNaoEncontradaException;
}