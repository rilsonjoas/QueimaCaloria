package com.example.queimacaloria.excecoes;

public class RefeicaoNaoEncontradaException extends RuntimeException {
    public RefeicaoNaoEncontradaException(String message) {
        super(message);
    }
}