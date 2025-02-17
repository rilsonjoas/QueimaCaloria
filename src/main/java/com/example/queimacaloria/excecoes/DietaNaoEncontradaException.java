package com.example.queimacaloria.excecoes;

// Exceção lançada quando uma dieta não é encontrada.
public class DietaNaoEncontradaException extends Exception {
  public DietaNaoEncontradaException(String message) {
    super(message);
  }
}