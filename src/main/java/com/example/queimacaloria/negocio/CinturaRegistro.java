package com.example.queimacaloria.negocio;

import java.time.LocalDate;

public class CinturaRegistro {
    private double cintura;
    private LocalDate data;

    public CinturaRegistro(double cintura, LocalDate data) {
        this.cintura = cintura;
        this.data = data;
    }

    public double getCintura() {
        return cintura;
    }

    public LocalDate getData() {
        return data;
    }
    @Override
    public String toString() {
        return "CinturaRegistro{" +
                "cintura=" + cintura +
                ", data=" + data +
                '}';
    }
}