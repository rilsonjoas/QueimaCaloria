package com.example.queimacaloria.interfaces;

import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;
import com.example.queimacaloria.negocio.Dieta;

import java.util.List;
import java.util.UUID;

public interface IRepositorioDietas {

    void adicionar(Dieta dieta) throws DietaNaoEncontradaException;

    void salvar(Dieta dieta) throws DietaNaoEncontradaException;

    void remover(UUID id) throws DietaNaoEncontradaException;

    Dieta buscar(UUID id) throws DietaNaoEncontradaException;

    List<Dieta> getAll();
}