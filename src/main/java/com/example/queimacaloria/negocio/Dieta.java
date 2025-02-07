package com.example.queimacaloria.negocio;

import javafx.beans.property.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Dieta {
    private final UUID id = UUID.randomUUID();
    private StringProperty nome = new SimpleStringProperty("");
    private ObjectProperty<ObjetivoDieta> objetivo = new SimpleObjectProperty<>();
    private IntegerProperty caloriasDiarias = new SimpleIntegerProperty(0);
    private ObjectProperty<Map<String, Double>> macronutrientes = new SimpleObjectProperty<>(new HashMap<>());
    private ObjectProperty<ArrayList<Refeicao>> refeicoes = new SimpleObjectProperty<>(new ArrayList<>());
    private ObjectProperty<Usuario> usuario = new SimpleObjectProperty<>();

    public enum ObjetivoDieta {
        PERDA_DE_PESO, GANHO_DE_MASSA, MANUTENCAO
    }

    public Dieta(String nome, ObjetivoDieta objetivo, int caloriasDiarias,
            Map<String, Double> macronutrientes, ArrayList<Refeicao> refeicoes, Usuario usuario) {
        this.nome = new SimpleStringProperty(nome);
        this.objetivo = new SimpleObjectProperty<>(objetivo);
        this.caloriasDiarias = new SimpleIntegerProperty(caloriasDiarias);
        this.macronutrientes = new SimpleObjectProperty<>(macronutrientes);
        this.refeicoes = new SimpleObjectProperty<>(refeicoes);
        this.usuario = new SimpleObjectProperty<>(usuario);
    }

    public double calcularProgresso() {
        ControladorDieta controladorDieta = new ControladorDieta();
        return controladorDieta.calcularProgresso(this);
    }

    public DoubleProperty progressoDaDietaProperty() {
        return new SimpleDoubleProperty(calcularProgresso());
    }

    // Métodos Property *DENTRO* da classe Dieta (essenciais!)
    public StringProperty nomeProperty() {
        return nome;
    }

    public ObjectProperty<ObjetivoDieta> objetivoProperty() {
        return objetivo;
    }

    public IntegerProperty caloriasDiariasProperty() {
        return caloriasDiarias;
    }

    public ObjectProperty<Map<String, Double>> macronutrientesProperty() {
        return macronutrientes;
    }

    public ObjectProperty<ArrayList<Refeicao>> refeicoesProperty() {
        return refeicoes;
    }

    public ObjectProperty<Usuario> usuarioProperty() {
        return usuario;
    }
}