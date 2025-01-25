package com.example.queimacaloria.negocio;

import java.util.*;

public class Meta {
    private UUID id;
    private String descricao;
    private Tipo tipo;
    private double valorAlvo;
    private double progressoAtual;
    private Date dataCriacao;
    private Date dataConclusao;

    // Enum para representar os tipos de meta
    public enum Tipo {
        PESO, MEDIDAS, OUTROS;
    }

    // Construtor
    public Meta() {
        this.id = UUID.randomUUID();
        this.dataCriacao = new Date();
    }

    public Meta(UUID id, String descricao, Tipo tipo, double valorAlvo, double progressoAtual, Date dataCriacao,
            Date dataConclusao) {
        this.id = id;
        this.descricao = descricao;
        this.tipo = tipo;
        this.valorAlvo = valorAlvo;
        this.progressoAtual = progressoAtual;
        this.dataCriacao = dataCriacao;
        this.dataConclusao = dataConclusao;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public Date getDataConclusao() {
        return dataConclusao;
    }

    public double getValorAlvo() {
        return valorAlvo;
    }

    public double getProgressoAtual() {
        return progressoAtual;
    }

    // Setters

    public void setValorAlvo(double valorAlvo) {
        this.valorAlvo = valorAlvo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setProgressoAtual(double progressoAtual) {
        this.progressoAtual = progressoAtual;
    }

    public void setDataConclusao(Date dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

}