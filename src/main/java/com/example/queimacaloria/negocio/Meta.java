package com.example.queimacaloria.negocio;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.Date;
import java.util.UUID;

public class Meta {
    private final UUID id;
    private String descricao;
    private Tipo tipo;
    private double valorAlvo;
    private double progressoAtual;
    private Date dataCriacao;
    private Date dataConclusao;

    public enum Tipo {
        PESO, MEDIDAS, OUTROS;
    }

    public Meta() {
        this.id = UUID.randomUUID();
        this.dataCriacao = new Date();
    }

    public Meta(String descricao, Tipo tipo, double valorAlvo, double progressoAtual, Date dataCriacao,
                Date dataConclusao) {
        this.id = UUID.randomUUID();
        this.descricao = descricao;
        this.tipo = tipo;
        this.valorAlvo = valorAlvo;
        this.progressoAtual = progressoAtual;
        this.dataCriacao = dataCriacao;
        this.dataConclusao = dataConclusao;
    }

    public UUID getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public double getValorAlvo() {
        return valorAlvo;
    }

    public void setValorAlvo(double valorAlvo) {
        this.valorAlvo = valorAlvo;
    }

    public double getProgressoAtual() {
        return progressoAtual;
    }

    public void setProgressoAtual(double progressoAtual) {
        this.progressoAtual = progressoAtual;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(Date dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public StringProperty descricaoProperty() {
        return new SimpleStringProperty(descricao);
    }

    public ObjectProperty<Tipo> tipoProperty() {
        return new SimpleObjectProperty<>(tipo);
    }


    public DoubleProperty progressProperty() {
        return new SimpleDoubleProperty(progressoAtual);
    }

    public ObjectProperty<Date> dataConclusaoProperty() {
        return new SimpleObjectProperty<>(dataConclusao);
    }


    @Override
    public String toString() {
        return "Meta{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", tipo=" + tipo +
                ", valorAlvo=" + valorAlvo +
                ", progressoAtual=" + progressoAtual +
                ", dataCriacao=" + dataCriacao +
                ", dataConclusao=" + dataConclusao +
                '}';
    }
}