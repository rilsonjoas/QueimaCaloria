package com.example.queimacaloria.negocio;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@ToString
public class Meta {
    private final UUID id;
    private StringProperty descricao = new SimpleStringProperty(); // Usando StringProperty
    private ObjectProperty<Tipo> tipo = new SimpleObjectProperty<>(); // Usando ObjectProperty
    private DoubleProperty valorAlvo = new SimpleDoubleProperty(); // DoubleProperty
    private DoubleProperty progressoAtual = new SimpleDoubleProperty(); // DoubleProperty!
    private ObjectProperty<LocalDate> dataCriacao = new SimpleObjectProperty<>(); // ObjectProperty
    private ObjectProperty<LocalDate> dataConclusao = new SimpleObjectProperty<>();  // ObjectProperty!


    public enum Tipo {
        PERDA_DE_PESO, GANHO_DE_MASSA
    }

    public Meta() {
        this.id = UUID.randomUUID();
        this.dataCriacao.set(LocalDate.now()); // Use o set, não o campo diretamente
    }

    public Meta(String descricao, Tipo tipo, double valorAlvo, double progressoAtual, LocalDate dataCriacao, // Alterado
                LocalDate dataConclusao) {
        this.id = UUID.randomUUID();
        this.descricao.set(descricao);  // Usando os métodos set!
        this.tipo.set(tipo);
        this.valorAlvo.set(valorAlvo);
        this.progressoAtual.set(progressoAtual);
        this.dataCriacao.set(dataCriacao);
        this.dataConclusao.set(dataConclusao);
    }

    // Métodos Property (já corrigidos, mas mostrando novamente para ficar completo):
    public StringProperty descricaoProperty() {
        return this.descricao; // Retorna a *property*, não o valor.
    }

    public ObjectProperty<Tipo> tipoProperty() {
        return this.tipo; // Retorna a *property*, não o valor.
    }

    // Adicione os métodos property que faltavam:

    public DoubleProperty valorAlvoProperty(){
        return this.valorAlvo;
    }

    public DoubleProperty progressoAtualProperty() {
        return this.progressoAtual; // Corrigido!
    }

    public ObjectProperty<LocalDate> dataCriacaoProperty() {
        return this.dataCriacao;
    }

    public ObjectProperty<LocalDate> dataConclusaoProperty() {
        return this.dataConclusao; // Corrigido!
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