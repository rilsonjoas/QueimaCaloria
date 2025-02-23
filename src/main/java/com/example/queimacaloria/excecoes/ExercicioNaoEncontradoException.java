package com.example.queimacaloria.excecoes;

// Exceção lançada quando um exercício não é encontrado.
public class ExercicioNaoEncontradoException extends Exception {
    public ExercicioNaoEncontradoException(String message) {
        super(message);
    }
}