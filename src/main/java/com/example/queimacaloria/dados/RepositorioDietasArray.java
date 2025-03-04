package com.example.queimacaloria.dados;

import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;
import com.example.queimacaloria.interfaces.IRepositorioDietas;
import com.example.queimacaloria.negocio.Dieta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RepositorioDietasArray implements IRepositorioDietas {
    private Dieta[] dietas;
    private int proximoIndice;
    private static RepositorioDietasArray instanciaUnica;

    // Construtor privado para o padrão Singleton.
    private RepositorioDietasArray() {
        dietas = new Dieta[10];
        proximoIndice = 0;
    }

    // Retorna a instância única do repositório (Singleton).
    public static RepositorioDietasArray getInstanciaUnica() {
        if (instanciaUnica == null) {
            instanciaUnica = new RepositorioDietasArray();
        }
        return instanciaUnica;
    }

    // Procura o índice de uma dieta pelo ID.
    private int procurarIndice(UUID id) {
        if (id == null) throw new IllegalArgumentException("ID não pode ser nulo.");
        for (int i = 0; i < proximoIndice; i++) {
            if (id.equals(dietas[i].getId())) {
                return i;
            }
        }
        return proximoIndice;
    }

    // Adiciona uma dieta ao repositório.
    @Override
    public void adicionar(Dieta dieta) throws DietaNaoEncontradaException {
        if (dieta == null) throw new IllegalArgumentException("Dieta não pode ser nula.");
        if (proximoIndice >= dietas.length) {
            Dieta[] temp = new Dieta[dietas.length + 10];
            System.arraycopy(dietas, 0, temp, 0, dietas.length);
            dietas = temp;
        }
        dietas[proximoIndice++] = dieta;
    }

    // Salva (atualiza) uma dieta no repositório.
    @Override
    public void salvar(Dieta dieta) throws DietaNaoEncontradaException {
        if (dieta == null) throw new IllegalArgumentException("Dieta não pode ser nula.");
        int indice = procurarIndice(dieta.getId());
        if (indice < proximoIndice) {
            dietas[indice] = dieta;

        } else {
            throw new DietaNaoEncontradaException("Dieta não encontrada.");
        }
    }

    // Remove uma dieta do repositório pelo ID.
    @Override
    public void remover(UUID id) throws DietaNaoEncontradaException {
        if (id == null) throw new IllegalArgumentException("ID não pode ser nulo.");
        int indice = procurarIndice(id);
        if (indice < proximoIndice) {
            dietas[indice] = dietas[proximoIndice - 1];
            dietas[--proximoIndice] = null;
        } else {
            throw new DietaNaoEncontradaException("Dieta não encontrada.");
        }
    }

    // Busca uma dieta pelo ID.
    @Override
    public Dieta buscar(UUID id) throws DietaNaoEncontradaException {
        if (id == null) throw new IllegalArgumentException("ID não pode ser nulo.");
        int indice = procurarIndice(id);
        if (indice < proximoIndice) {
            return dietas[indice];
        } else {
            throw new DietaNaoEncontradaException("Dieta não encontrada.");
        }
    }

    // Retorna todas as dietas do repositório.
    @Override
    public List<Dieta> getAll() {
        List<Dieta> lista = new ArrayList<>();
        for (int i = 0; i < proximoIndice; i++) {
            if (dietas[i] != null) {
                lista.add(dietas[i]);
            }
        }
        return lista;
    }
}