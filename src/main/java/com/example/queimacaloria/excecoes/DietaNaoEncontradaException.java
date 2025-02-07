package com.example.queimacaloria.excecoes;

// Exceção lançada quando uma dieta não é encontrada.
public class DietaNaoEncontradaException extends Exception {
  public DietaNaoEncontradaException(String message) {
    super(message); // Chama o construtor da classe Exception com a mensagem de erro.
  }
}