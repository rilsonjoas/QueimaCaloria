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
        refeicoes = new Refeicao[100]; // Tamanho inicial maior (conforme seu código original)
        proximoIndice = 0;
    }

    public static RepositorioRefeicoesArray getInstanciaUnica() {
        if (instanciaUnica == null) {
            instanciaUnica = new RepositorioRefeicoesArray();
        }
        return instanciaUnica;
    }

    private int procurarIndice(UUID id) {
        if (id == null) throw new IllegalArgumentException("ID não pode ser nulo.");
        for (int i = 0; i < proximoIndice; i++) {
            if (id.equals(refeicoes[i].getId())) return i;
        }
        return proximoIndice;
    }

    @Override
    public void adicionar(Refeicao refeicao) throws RefeicaoNaoEncontradaException { // Corrigido para throws
        if (refeicao == null) throw new IllegalArgumentException("Refeição não pode ser nula.");
        if (proximoIndice >= refeicoes.length) {
            Refeicao[] temp = new Refeicao[refeicoes.length + 100]; // Aumenta de 100 em 100
            System.arraycopy(refeicoes, 0, temp, 0, refeicoes.length);
            refeicoes = temp;
        }
        refeicoes[proximoIndice++] = refeicao;
    }
    @Override
    public void salvar(Refeicao refeicao) throws RefeicaoNaoEncontradaException {
        if (refeicao == null) throw new IllegalArgumentException("Refeição não pode ser nula.");
        int indice = procurarIndice(refeicao.getId());
        if (indice < proximoIndice) {
            refeicoes[indice] = refeicao;
        } else {
            throw new RefeicaoNaoEncontradaException("Refeição não encontrada.");
        }
    }

    @Override
    public void remover(UUID id) throws RefeicaoNaoEncontradaException {
        if (id == null) throw new IllegalArgumentException("ID não pode ser nulo.");
        int indice = procurarIndice(id);
        if (indice < proximoIndice) {
            refeicoes[indice] = refeicoes[proximoIndice - 1];
            refeicoes[--proximoIndice] = null;  //  Remove a referência
        } else {
            throw new RefeicaoNaoEncontradaException("Refeição não encontrada.");
        }
    }

    @Override
    public Refeicao buscar(UUID id) throws RefeicaoNaoEncontradaException {
        if (id == null) throw new IllegalArgumentException("ID não pode ser nulo.");
        int indice = procurarIndice(id);
        if (indice < proximoIndice) {
            return refeicoes[indice];
        } else {
            throw new RefeicaoNaoEncontradaException("Refeição não encontrada.");
        }
    }

    @Override
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