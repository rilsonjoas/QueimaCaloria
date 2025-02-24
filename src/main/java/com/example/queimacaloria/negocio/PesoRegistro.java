package com.example.queimacaloria.negocio;

import java.time.LocalDate;

public class PesoRegistro {
    private float peso;
    private LocalDate data;

    public PesoRegistro(float peso, LocalDate data) {
        this.peso = peso;
        this.data = data;
    }

    public float getPeso() {
        return peso;
    }

    public LocalDate getData() {
        return data;
    }

    @Override
    public String toString() {
        return "PesoRegistro{" +
                "peso=" + peso +
                ", data=" + data +
                '}';
    }
}