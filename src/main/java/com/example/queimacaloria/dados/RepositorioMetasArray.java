package com.example.queimacaloria.dados;

import com.example.queimacaloria.excecoes.MetaNaoEncontradaException;
import com.example.queimacaloria.interfaces.IRepositorioMetas;
import com.example.queimacaloria.negocio.Meta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RepositorioMetasArray implements IRepositorioMetas {
    private Meta[] metas;
    private int proximoIndice;
    private static RepositorioMetasArray instanciaUnica;

    private RepositorioMetasArray() {
        metas = new Meta[10];
        proximoIndice = 0;
    }

    public static RepositorioMetasArray getInstanciaUnica() {
        if (instanciaUnica == null) {
            instanciaUnica = new RepositorioMetasArray();
        }
        return instanciaUnica;
    }


    private int procurarIndice(UUID id) {
        if (id == null) throw new IllegalArgumentException("ID não pode ser nulo.");
        for (int i = 0; i < proximoIndice; i++) {
            if (id.equals(metas[i].getId())) return i;
        }
        return proximoIndice;
    }

    @Override
    public void adicionar(Meta meta) throws MetaNaoEncontradaException {
        if (meta == null) throw new IllegalArgumentException("Meta não pode ser nula.");
        if (proximoIndice >= metas.length) {
            Meta[] temp = new Meta[metas.length + 10];
            System.arraycopy(metas, 0, temp, 0, metas.length);
            metas = temp;
        }
        metas[proximoIndice++] = meta;
    }


    @Override
    public void salvar(Meta meta) throws MetaNaoEncontradaException {
        if (meta == null) throw new IllegalArgumentException("Meta não pode ser nula.");
        int indice = procurarIndice(meta.getId());
        if (indice < proximoIndice) {
            System.out.println("RepositorioMetasArray.salvar: Antes da atualização: " + metas[indice]);
            metas[indice] = meta;
            System.out.println("RepositorioMetasArray.salvar: Depois da atualização: " + metas[indice]);
        } else {
            throw new MetaNaoEncontradaException("Meta não encontrada.");
        }
    }

    @Override
    public void remover(UUID id) throws MetaNaoEncontradaException {
        if (id == null) throw new IllegalArgumentException("ID não pode ser nulo.");
        int indice = procurarIndice(id);
        if (indice < proximoIndice) {
            metas[indice] = metas[proximoIndice - 1];
            metas[--proximoIndice] = null;
        } else {
            throw new MetaNaoEncontradaException("Meta não encontrada.");
        }
    }

    @Override
    public Meta buscar(UUID id) throws MetaNaoEncontradaException {
        if (id == null) throw new IllegalArgumentException("ID não pode ser nulo.");
        int indice = procurarIndice(id);
        if (indice < proximoIndice) {
            return metas[indice];
        } else {
            throw new MetaNaoEncontradaException("Meta não encontrada.");
        }
    }

    @Override
    public List<Meta> getAll() {
        System.out.println("RepositorioMetasArray.getAll() chamado");
        List<Meta> lista = new ArrayList<>();
        for (int i = 0; i < proximoIndice; i++) {
            if (metas[i] != null) {
                lista.add(metas[i]);
            }
        }
        System.out.println("RepositorioMetasArray.getAll(): Retornando lista: " + lista);
        return lista;
    }
}