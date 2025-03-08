package com.example.queimacaloria.dados;

import com.example.queimacaloria.excecoes.TreinoNaoEncontradoException;
import com.example.queimacaloria.interfaces.IRepositorioTreinos;
import com.example.queimacaloria.negocio.Treino;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RepositorioTreinosArray implements IRepositorioTreinos {
    private Treino[] treinos;
    private int proximoIndice;
    private static RepositorioTreinosArray instanciaUnica;

    public RepositorioTreinosArray() {
        treinos = new Treino[10];
        proximoIndice = 0;
    }

    public static RepositorioTreinosArray getInstanciaUnica() {
        if (instanciaUnica == null) {
            instanciaUnica = new RepositorioTreinosArray();
        }
        return instanciaUnica;
    }

    private int procurarIndice(UUID id) {
        if (id == null) throw new IllegalArgumentException("ID não pode ser nulo.");
        for (int i = 0; i < proximoIndice; i++) {
            if (id.equals(treinos[i].getId())) return i;
        }
        return proximoIndice;
    }

    @Override
    public void adicionar(Treino treino) throws TreinoNaoEncontradoException{
        if (treino == null) throw new IllegalArgumentException("Treino não pode ser nulo.");
        if (proximoIndice >= treinos.length) {
            Treino[] temp = new Treino[treinos.length + 10];
            System.arraycopy(treinos, 0, temp, 0, treinos.length);
            treinos = temp;
        }
        treinos[proximoIndice++] = treino;
    }

    @Override
    public void salvar(Treino treino) throws TreinoNaoEncontradoException {
        if (treino == null) throw new IllegalArgumentException("Treino não pode ser nulo.");
        int indice = procurarIndice(treino.getId());
        if (indice < proximoIndice) {
            treinos[indice] = treino;
        } else {
            throw new TreinoNaoEncontradoException("Treino não encontrado.");
        }
    }

    @Override
    public void remover(UUID id) throws TreinoNaoEncontradoException {
        if (id == null) throw new IllegalArgumentException("ID não pode ser nulo.");
        int indice = procurarIndice(id);
        if (indice < proximoIndice) {
            treinos[indice] = treinos[proximoIndice - 1];
            treinos[--proximoIndice] = null;
        } else {
            throw new TreinoNaoEncontradoException("Treino não encontrado.");
        }
    }

    @Override
    public Treino buscar(UUID id) throws TreinoNaoEncontradoException {
        if (id == null) throw new IllegalArgumentException("ID não pode ser nulo.");
        int indice = procurarIndice(id);
        if (indice < proximoIndice) {
            return treinos[indice];
        } else {
            throw new TreinoNaoEncontradoException("Treino não encontrado.");
        }
    }

    @Override
    public List<Treino> getAll() {
        System.out.println("RepositorioTreinosArray.getAll() chamado");
        List<Treino> lista = new ArrayList<>();
        for (int i = 0; i < proximoIndice; i++) {
            if (treinos[i] != null) {
                lista.add(treinos[i]);
            }
        }
        System.out.println("RepositorioTreinosArray.getAll(): Retornando lista: " + lista);
        return lista;
    }
}