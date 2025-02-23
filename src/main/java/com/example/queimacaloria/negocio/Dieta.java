package com.example.queimacaloria.negocio;

import javafx.beans.property.*;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
public class Dieta {

    private final UUID id = UUID.randomUUID();
    private StringProperty nome = new SimpleStringProperty("");
    private ObjectProperty<ObjetivoDieta> objetivo = new SimpleObjectProperty<>();
    private IntegerProperty caloriasDiarias = new SimpleIntegerProperty(0);
    private ObjectProperty<Usuario> usuario = new SimpleObjectProperty<>();

    // Enum com strings associadas para os objetivos da dieta.
    public enum ObjetivoDieta {
        PERDA_DE_PESO("Perda de peso"),
        GANHO_DE_MASSA("Ganho de massa"),
        MANUTENCAO("Manutenção");

        private final String descricao;

        ObjetivoDieta(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }

        public static ObjetivoDieta fromString(String text) {
            for (ObjetivoDieta objetivo : ObjetivoDieta.values()) {
                if (objetivo.descricao.equalsIgnoreCase(text)) {
                    return objetivo;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return descricao;
        }
    }

    // Construtor da classe Dieta.
    public Dieta(String nome, ObjetivoDieta objetivo, int caloriasDiarias, Usuario usuario) {
        this.nome.set(nome);
        this.objetivo.set(objetivo);
        this.caloriasDiarias.set(caloriasDiarias);
        this.usuario.set(usuario);
    }

    // Getters:
    public UUID getId() { return id; }
    public String getNome() { return nome.get(); }
    public ObjetivoDieta getObjetivo() { return objetivo.get(); }
    public int getCaloriasDiarias() { return caloriasDiarias.get(); }
    public Usuario getUsuario() { return usuario.get(); }

    // Setters
    public void setNome(String nome) { this.nome.set(nome); }
    public void setObjetivo(ObjetivoDieta objetivo) { this.objetivo.set(objetivo); }
    public void setCaloriasDiarias(int caloriasDiarias) { this.caloriasDiarias.set(caloriasDiarias); }
    public void setUsuario(Usuario usuario) { this.usuario.set(usuario); }

    // Métodos Property
    public StringProperty nomeProperty() { return nome; }
    public ObjectProperty<ObjetivoDieta> objetivoProperty() { return objetivo; }
    public IntegerProperty caloriasDiariasProperty() { return caloriasDiarias; }
    public ObjectProperty<Usuario> usuarioProperty() { return usuario; }

    // Método equals, para comparar objetos Dieta.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dieta dieta = (Dieta) o;
        return id.equals(dieta.id);
    }

    // Método hashCode, para gerar um código hash do objeto Dieta.
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    // Método toString, para representação textual do objeto Dieta.
    @Override
    public String toString() {
        return "Dieta{" +
                "id=" + id +
                ", nome=" + nome.get() +
                ", objetivo=" + objetivo.get() +
                ", caloriasDiarias=" + caloriasDiarias.get() +
                ", usuario=" + (usuario.get() != null ? usuario.get().getEmail() : "null") +
                '}';
    }
}