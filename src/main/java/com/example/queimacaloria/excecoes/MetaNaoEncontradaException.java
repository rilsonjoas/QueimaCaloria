package com.example.queimacaloria.excecoes;

// Exceção lançada quando uma meta não é encontrada.
public class MetaNaoEncontradaException extends Exception {
    public MetaNaoEncontradaException(String message) {
        super(message);
    }
}