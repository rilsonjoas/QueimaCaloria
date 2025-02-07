package com.example.queimacaloria.dados;

import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;
import com.example.queimacaloria.interfaces.IRepositorioDietas;
import com.example.queimacaloria.negocio.Dieta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Repositório de dietas implementado com um array.
public class RepositorioDietasArray implements IRepositorioDietas {
    private Dieta[] dietas;
    private int proximoIndice;

    private static RepositorioDietasArray instanciaUnica;

    // Construtor privado para garantir que só haja uma instância (Singleton).
    private RepositorioDietasArray() {
        dietas = new Dieta[10]; // Inicializa o array com tamanho 10.
        proximoIndice = 0; // Inicializa o índice do próximo elemento.
    }

    // Método para obter a instância única do repositório.
    public static RepositorioDietasArray getInstanciaUnica() {
        if (instanciaUnica == null) {
            instanciaUnica = new RepositorioDietasArray();
        }
        return instanciaUnica;
    }

    // Procura o índice de uma dieta no array pelo seu ID.
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
        return achou ? i : proximoIndice; // Retorna o índice se encontrar, senão retorna proximoIndice.
    }

    @Override
    // Adiciona uma nova dieta ao repositório.
    public void adicionar(Dieta dieta) throws DietaNaoEncontradaException {
        if (proximoIndice >= dietas.length) { // Verifica se o array está cheio.
            // Se estiver cheio, aumenta o tamanho do array.
            int novoTam = dietas.length + 10;
            Dieta[] arrayTemporario = new Dieta[novoTam];
            System.arraycopy(dietas, 0, arrayTemporario, 0, dietas.length); // Copia os elementos para o novo array.
            dietas = arrayTemporario;
        }
        dietas[proximoIndice] = dieta;
        proximoIndice++;
    }

    @Override
    // Salva (atualiza) uma dieta existente no repositório.
    public void salvar(Dieta dieta) throws DietaNaoEncontradaException {
        if (dieta != null) {
            int indice = this.procurarIndice(dieta.getId());
            if (indice != proximoIndice) { // Verifica se a dieta foi encontrada.
                dietas[indice] = dieta;
            } else {
                throw new DietaNaoEncontradaException("Dieta não encontrada para atualizar.");
            }
        } else {
            throw new IllegalArgumentException("Dieta inválida.");
        }
    }

    @Override
    // Remove uma dieta do repositório pelo seu ID.
    public void remover(UUID id) throws DietaNaoEncontradaException {
        int i = this.procurarIndice(id);
        if (i < proximoIndice) { // Verifica se a dieta foi encontrada.
            dietas[i] = dietas[proximoIndice - 1]; // Move o último elemento para a posição da dieta removida.
            dietas[proximoIndice - 1] = null; // Define o último elemento como null.
            proximoIndice--;
        } else {
            throw new DietaNaoEncontradaException("Dieta não encontrada para remover.");
        }
    }

    @Override
    // Busca uma dieta no repositório pelo seu ID.
    public Dieta buscar(UUID id) throws DietaNaoEncontradaException {
        int indice = procurarIndice(id);
        if (indice < proximoIndice) { // Verifica se a dieta foi encontrada.
            return dietas[indice];
        } else {
            throw new DietaNaoEncontradaException("Dieta não encontrada para buscar.");
        }
    }

    // Retorna todas as dietas do repositório.
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
