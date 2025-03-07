package com.example.queimacaloria.negocio;

import java.time.LocalDate;

public class QuadrilRegistro {
    private double quadril;
    private LocalDate data;

    public QuadrilRegistro(double quadril, LocalDate data) {
        this.quadril = quadril;
        this.data = data;
    }

    public double getQuadril() {
        return quadril;
    }

    public LocalDate getData() {
        return data;
    }

    @Override
    public String toString() {
        return "QuadrilRegistro{" +
                "quadril=" + quadril +
                ", data=" + data +
                '}';
    }
}