package com.example.queimacaloria.negocio;

import java.time.LocalDate;

public class BicepsRegistro {
    private double biceps;
    private LocalDate data;

    public BicepsRegistro(double biceps, LocalDate data) {
        this.biceps = biceps;
        this.data = data;
    }

    public double getBiceps() {
        return biceps;
    }

    public LocalDate getData() {
        return data;
    }
    @Override
    public String toString() {
        return "BicepsRegistro{" +
                "biceps=" + biceps +
                ", data=" + data +
                '}';
    }
}