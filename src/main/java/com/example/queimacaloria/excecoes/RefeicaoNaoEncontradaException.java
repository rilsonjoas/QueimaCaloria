package com.example.queimacaloria.excecoes;

// Exceção lançada quando uma refeição não é encontrada.
public class RefeicaoNaoEncontradaException extends RuntimeException {
    public RefeicaoNaoEncontradaException(String message) {
        super(message);
    }
}