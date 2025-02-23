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

    // Enum com strings associadas
    public enum ObjetivoDieta {
        PERDA_DE_PESO("Perda de peso"),
        GANHO_DE_MASSA("Ganho de massa"),
        MANUTENCAO("Manutenção"); // Ponto e vírgula no último!

        private final String descricao; // Campo para a string

        ObjetivoDieta(String descricao) { // Construtor
            this.descricao = descricao;
        }

        public String getDescricao() { // Getter para a string
            return descricao;
        }

        // Método para converter de String para enum (opcional, mas útil)
        public static ObjetivoDieta fromString(String text) {
            for (ObjetivoDieta objetivo : ObjetivoDieta.values()) {
                if (objetivo.descricao.equalsIgnoreCase(text)) {
                    return objetivo;
                }
            }
            return null; // Ou lançar uma exceção IllegalArgumentException, se preferir
        }

        @Override
        public String toString() { //Para o choice box exibir a descrição
            return descricao;
        }
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dieta dieta = (Dieta) o;
        return id.equals(dieta.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

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