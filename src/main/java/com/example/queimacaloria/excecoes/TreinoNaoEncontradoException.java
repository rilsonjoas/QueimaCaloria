package com.example.queimacaloria.excecoes;

// Exceção lançada quando um treino não é encontrado.
public class TreinoNaoEncontradoException extends Exception {
    public TreinoNaoEncontradoException(String message) {
        super(message);
    }
}