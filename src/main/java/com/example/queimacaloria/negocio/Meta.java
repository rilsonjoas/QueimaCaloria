package com.example.queimacaloria.negocio;

import javafx.beans.property.*;
import lombok.Getter;
import lombok.Setter; // Este import não é estritamente necessário se você não usar @Setter nos campos
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@ToString
public class Meta {
    private final UUID id;
    private StringProperty descricao = new SimpleStringProperty();
    private ObjectProperty<Tipo> tipo = new SimpleObjectProperty<>();
    private DoubleProperty valorAlvo = new SimpleDoubleProperty();
    private DoubleProperty progressoAtual = new SimpleDoubleProperty();
    private ObjectProperty<LocalDate> dataCriacao = new SimpleObjectProperty<>();
    private ObjectProperty<LocalDate> dataConclusao = new SimpleObjectProperty<>();

    public enum Tipo {
        PERDA_DE_PESO("Perda de Peso"),
        GANHO_DE_MASSA("Ganho de Massa");

        private final String descricao;

        Tipo(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }

        // Opcional: Método para converter de String para enum (útil para o ChoiceBox)
        public static Tipo fromString(String text) {
            for (Tipo tipo : Tipo.values()) {
                if (tipo.descricao.equalsIgnoreCase(text)) {
                    return tipo;
                }
            }
            return null; // Ou lançar IllegalArgumentException
        }

        @Override
        public String toString() {
            return descricao; // Para o ChoiceBox exibir a descrição
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
    public StringProperty descricaoProperty() {
        return this.descricao;
    }

    public ObjectProperty<Tipo> tipoProperty() {
        return this.tipo;
    }

    public DoubleProperty valorAlvoProperty() {
        return this.valorAlvo;
    }

    public DoubleProperty progressoAtualProperty() {
        return this.progressoAtual;
    }

    public ObjectProperty<LocalDate> dataCriacaoProperty() {
        return this.dataCriacao;
    }

    public ObjectProperty<LocalDate> dataConclusaoProperty() {
        return this.dataConclusao;
    }


    public void setDescricao(String descricao) {
        this.descricao.set(descricao);
    }

    public void setTipo(Tipo tipo) {
        this.tipo.set(tipo);
    }

    public void setValorAlvo(double valorAlvo) {
        this.valorAlvo.set(valorAlvo);
    }

    public void setProgressoAtual(double progressoAtual) {
        this.progressoAtual.set(progressoAtual);
    }
    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao.set(dataCriacao);
    }

    public void setDataConclusao(LocalDate dataConclusao) {
        this.dataConclusao.set(dataConclusao);
    }

    public String getDescricao() {
        return descricao.get();
    }

    public Tipo getTipo() {
        return tipo.get();
    }

    public double getValorAlvo() {
        return valorAlvo.get();
    }

    public double getProgressoAtual() {
        return progressoAtual.get();
    }
    public LocalDate getDataCriacao() {
        return dataCriacao.get();
    }

    public LocalDate getDataConclusao() {
        return dataConclusao.get();
    }
}