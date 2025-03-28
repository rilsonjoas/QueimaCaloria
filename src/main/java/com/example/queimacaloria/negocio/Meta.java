package com.example.queimacaloria.negocio;

import javafx.beans.property.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class Meta {

    private final UUID id;
    private StringProperty descricao = new SimpleStringProperty();
    private ObjectProperty<Tipo> tipo = new SimpleObjectProperty<>();
    private DoubleProperty valorAlvo = new SimpleDoubleProperty();
    private DoubleProperty progressoAtual = new SimpleDoubleProperty();
    private ObjectProperty<LocalDate> dataCriacao = new SimpleObjectProperty<>();
    private ObjectProperty<LocalDate> dataConclusao = new SimpleObjectProperty<>();

    //Usuário
    private ObjectProperty<Usuario> usuario = new SimpleObjectProperty<>();

    public enum Tipo {
        PERDA_DE_PESO("Perda de Peso"),
        GANHO_DE_MASSA("Ganho de Massa"),
        MANUTENCAO("Manutenção");

        private final String descricao;

        Tipo(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }

        public static Tipo fromString(String text) {
            for (Tipo tipo : Tipo.values()) {
                if (tipo.descricao.equalsIgnoreCase(text)) {
                    return tipo;
                }
            }
            return null;
        }
    }

    public Meta() {
        this.id = UUID.randomUUID();
        this.dataCriacao.set(LocalDate.now());
    }

    public Meta(String descricao, Tipo tipo, double valorAlvo, double progressoAtual, LocalDate dataCriacao,
                LocalDate dataConclusao) {
        this.id = UUID.randomUUID();
        this.descricao.set(descricao);
        this.tipo.set(tipo);
        this.valorAlvo.set(valorAlvo);
        this.progressoAtual.set(progressoAtual);
        this.dataCriacao.set(dataCriacao);
        this.dataConclusao.set(dataConclusao);
    }

    // Métodos Property
    public StringProperty descricaoProperty() { return this.descricao; }
    public ObjectProperty<Tipo> tipoProperty() { return this.tipo; }
    public DoubleProperty valorAlvoProperty() { return this.valorAlvo; }
    public DoubleProperty progressoAtualProperty() { return this.progressoAtual; }
    public ObjectProperty<LocalDate> dataCriacaoProperty() { return this.dataCriacao; }
    public ObjectProperty<LocalDate> dataConclusaoProperty() { return this.dataConclusao; }

    //Setters
    public void setDescricao(String descricao) { this.descricao.set(descricao); }
    public void setTipo(Tipo tipo) { this.tipo.set(tipo); }
    public void setValorAlvo(double valorAlvo) { this.valorAlvo.set(valorAlvo); }
    public void setProgressoAtual(double progressoAtual) { this.progressoAtual.set(progressoAtual); }
    public void setDataCriacao(LocalDate dataCriacao) { this.dataCriacao.set(dataCriacao); }
    public void setDataConclusao(LocalDate dataConclusao) { this.dataConclusao.set(dataConclusao); }

    //Getters:
    public String getDescricao() { return descricao.get(); }
    public Tipo getTipo() { return tipo.get(); }
    public double getValorAlvo() { return valorAlvo.get(); }
    public double getProgressoAtual() { return progressoAtual.get(); }
    public LocalDate getDataCriacao() { return dataCriacao.get(); }
    public LocalDate getDataConclusao() { return dataConclusao.get(); }
    public UUID getId() { return id; } // Adicionei o getter para o ID.

    // Getter e Property para Usuario
    public Usuario getUsuario() { return usuario.get(); }
    public ObjectProperty<Usuario> usuarioProperty() { return usuario; }
    public void setUsuario(Usuario usuario) {
        this.usuario.set(usuario);
    }

    @Override
    public String toString() {
        return "Meta{" +
                "id=" + id +
                ", descricao=" + descricao.get() +
                ", tipo=" + (tipo.get() != null ? tipo.get().getDescricao() : "null") + // Use getDescricao()
                ", valorAlvo=" + valorAlvo.get() +
                ", progressoAtual=" + progressoAtual.get() +
                ", dataCriacao=" + dataCriacao.get() +
                ", dataConclusao=" + dataConclusao.get() +
                ", usuarioId=" + (usuario.get() != null ? usuario.get().getId() : "null") + // Apenas ID, evita recursão.
                '}';
    }
}