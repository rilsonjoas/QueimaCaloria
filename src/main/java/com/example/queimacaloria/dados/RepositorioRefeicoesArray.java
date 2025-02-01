package com.example.queimacaloria.dados;

import com.example.queimacaloria.excecoes.RefeicaoNaoEncontradaException;
import com.example.queimacaloria.interfaces.IRepositorioRefeicoes;
import com.example.queimacaloria.negocio.Refeicao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RepositorioRefeicoesArray implements IRepositorioRefeicoes {

    private Refeicao[] refeicoes;
    private int proximoIndice;

    private static RepositorioRefeicoesArray instanciaUnica;

    private RepositorioRefeicoesArray() {
        refeicoes = new Refeicao[100];
        proximoIndice = 0;
    }

    public static RepositorioRefeicoesArray getInstanciaUnica() {
        if (instanciaUnica == null) {
            instanciaUnica = new RepositorioRefeicoesArray();
        }
        return instanciaUnica;
    }

    private int procurarIndice(UUID id) {
        int i = 0;
        boolean achou = false;
        while ((!achou) && (i < this.proximoIndice)) {
            if (id.equals(this.refeicoes[i].getId())) {
                achou = true;
            } else {
                i++;
            }
        }
        return achou ? i : proximoIndice; //retorna proximoIndice se nao achar
    }

    @Override
    public void adicionar(Refeicao refeicao) {
        if (proximoIndice >= refeicoes.length) {
            int novoTam = refeicoes.length + 100;
            Refeicao[] arrayTemporario = new Refeicao[novoTam];
            System.arraycopy(refeicoes, 0, arrayTemporario, 0, refeicoes.length);
            refeicoes = arrayTemporario;
        }
        refeicoes[proximoIndice] = refeicao;
        proximoIndice++;

    }


    @Override
    public void salvar(Refeicao refeicao) throws RefeicaoNaoEncontradaException {
        if(refeicao != null) {
            int indice = this.procurarIndice(refeicao.getId());
            if (indice != proximoIndice) {
                refeicoes[indice] = refeicao;
            }else {
                throw new RefeicaoNaoEncontradaException("Refeição não encontrada para atualizar.");
            }
        }else{
            throw new IllegalArgumentException("Refeição inválida.");
        }
    }


    @Override
    public void remover(UUID id) throws RefeicaoNaoEncontradaException {
        int i = this.procurarIndice(id);
        if (i < proximoIndice) {
            refeicoes[i] = refeicoes[proximoIndice - 1];
            refeicoes[proximoIndice - 1] = null;
            proximoIndice--;
        } else {
            throw new RefeicaoNaoEncontradaException("Refeição não encontrada para remover.");
        }
    }

    @Override
    public Refeicao buscar(UUID id) throws RefeicaoNaoEncontradaException {
        int indice = procurarIndice(id);
        if (indice < proximoIndice) {
            return refeicoes[indice];
        } else {
            throw new RefeicaoNaoEncontradaException("Refeição não encontrada.");
        }
    }
    public List<Refeicao> getAll() {
        List<Refeicao> lista = new ArrayList<>();
        for (int i = 0; i < proximoIndice; i++) {
            if (refeicoes[i] != null) {
                lista.add(refeicoes[i]);
            }
        }
        return lista;
    }
}