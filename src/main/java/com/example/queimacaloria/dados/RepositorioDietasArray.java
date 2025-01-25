package com.example.queimacaloria.dados;

import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;
import com.example.queimacaloria.interfaces.IRepositorioDietas;
import com.example.queimacaloria.negocio.Dieta;

import java.util.UUID; 

public class RepositorioDietasArray implements IRepositorioDietas {
    private Dieta[] dietas;
    private int proximoIndice;

    private static RepositorioDietasArray instanciaUnica;

    private RepositorioDietasArray() {
        dietas = new Dieta[10];
        proximoIndice = 0;
    }

    public static RepositorioDietasArray getInstanciaUnica() {
        if (instanciaUnica == null) {
            instanciaUnica = new RepositorioDietasArray();
        }
        return instanciaUnica;
    }

    private int procurarIndice(UUID id) {
        int i = 0;
        boolean achou = false;
        while ((!achou) && (i < this.proximoIndice)) {
            if (id.equals(this.dietas[i].getId())) {
                achou = true;
            } else {
                i++;
            }
        }
        return achou ? i : proximoIndice;
    }


    @Override
    public void adicionar(Dieta dieta) {
        if (proximoIndice >= dietas.length) {
            int novoTam = dietas.length + 10;
            Dieta[] arrayTemporario = new Dieta[novoTam];
            System.arraycopy(dietas, 0, arrayTemporario, 0, dietas.length);
            dietas = arrayTemporario;
        }
        dietas[proximoIndice] = dieta;
        proximoIndice++;
    }

    @Override
    public void atualizar(Dieta dieta) throws DietaNaoEncontradaException {
        if (dieta != null) {
            int indice = this.procurarIndice(dieta.getId());
            if (indice != proximoIndice) {
                dietas[indice] = dieta;
            } else {
                throw new DietaNaoEncontradaException("Dieta não encontrada para atualizar.");
            }
        } else {
            throw new IllegalArgumentException("Dieta inválida.");
        }
    }

    @Override
    public void remover(UUID id) throws DietaNaoEncontradaException {
        int i = this.procurarIndice(id);
        if (i < proximoIndice) {
            dietas[i] = dietas[proximoIndice - 1];
            dietas[proximoIndice - 1] = null;
            proximoIndice--;
        } else {
            throw new DietaNaoEncontradaException("Dieta não encontrada para remover.");
        }
    }


    @Override
    public Dieta buscar(UUID id) throws DietaNaoEncontradaException {
        int indice = procurarIndice(id);
        if (indice < proximoIndice) {
            return dietas[indice];
        } else {
            throw new DietaNaoEncontradaException("Dieta não encontrada para buscar.");
        }
    }
}