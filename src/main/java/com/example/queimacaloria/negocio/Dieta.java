package com.example.queimacaloria.negocio;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Dieta {
    private final UUID id;
    private String nome;
    private Objetivo objetivo;
    private int caloriasDiarias;
    private Map<String, Double> macronutrientes;
    private ArrayList<Refeicao> refeicoes;
    private Usuario usuario;

    public enum Objetivo {
        PERDA_DE_PESO, GANHO_DE_MASSA, MANUTENCAO;
    }

    public Dieta() {
        this.id = UUID.randomUUID();
        this.macronutrientes = new HashMap<>();
        this.refeicoes = new ArrayList<>();
    }

    public Dieta(String nome, Objetivo objetivo, int caloriasDiarias, Map<String, Double> macronutrientes,
                 ArrayList<Refeicao> refeicoes, Usuario usuario) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.objetivo = objetivo;
        this.caloriasDiarias = caloriasDiarias;
        this.macronutrientes = macronutrientes;
        this.refeicoes = refeicoes;
        this.usuario = usuario;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Objetivo getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(Objetivo objetivo) {
        this.objetivo = objetivo;
    }

    public int getCaloriasDiarias() {
        return caloriasDiarias;
    }

    public void setCaloriasDiarias(int caloriasDiarias) {
        this.caloriasDiarias = caloriasDiarias;
    }

    public Map<String, Double> getMacronutrientes() {
        return macronutrientes;
    }

    public void setMacronutrientes(Map<String, Double> macronutrientes) {
        this.macronutrientes = macronutrientes;
    }

    public ArrayList<Refeicao> getRefeicoes() {
        return refeicoes;
    }

    public void setRefeicoes(ArrayList<Refeicao> refeicoes) {
        this.refeicoes = refeicoes;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public StringProperty nomeProperty() {
        return new SimpleStringProperty(nome);
    }

    public ObjectProperty<Objetivo> objetivoProperty() {
        return new SimpleObjectProperty<>(objetivo);
    }

    public IntegerProperty caloriasDiariasProperty() {
        return new SimpleIntegerProperty(caloriasDiarias);
    }

    public DoubleProperty progressProperty() {
        ControladorDieta controladorDieta = new ControladorDieta();
        return new SimpleDoubleProperty(controladorDieta.getProgresso(this));
    }
    @Override
    public String toString() {
        return "Dieta{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", objetivo=" + objetivo +
                ", caloriasDiarias=" + caloriasDiarias +
                ", macronutrientes=" + macronutrientes +
                ", refeicoes=" + refeicoes +
                ", usuario=" + usuario +
                '}';
    }
}