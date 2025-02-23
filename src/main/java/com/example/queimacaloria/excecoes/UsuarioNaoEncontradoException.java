package com.example.queimacaloria.excecoes;

// Exceção lançada quando um usuário não é encontrado.
public class UsuarioNaoEncontradoException extends Exception {
    public UsuarioNaoEncontradoException(String message) {
        super(message);
    }
}