package com.example.queimacaloria.negocio;

import java.time.LocalDate;

public class CoxaRegistro {
    private double coxa;
    private LocalDate data;

    public CoxaRegistro(double coxa, LocalDate data) {
        this.coxa = coxa;
        this.data = data;
    }

    public double getCoxa() {
        return coxa;
    }

    public LocalDate getData() {
        return data;
    }
    @Override
    public String toString() {
        return "CoxaRegistro{" +
                "coxa=" + coxa +
                ", data=" + data +
                '}';
    }
}