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

import java.util.Date;
import java.util.UUID;

@Getter
@ToString
public class Meta {
    private final UUID id;
    @Setter
    private String descricao;
    @Setter
    private Tipo tipo;
    @Setter
    private double valorAlvo;
    @Setter
    private double progressoAtual;
    @Setter
    private Date dataCriacao;
    @Setter
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

    public StringProperty descricaoProperty() {
        return new SimpleStringProperty(descricao);
    }

    public ObjectProperty<Tipo> tipoProperty() {
        return new SimpleObjectProperty<>(tipo);
    }


    public DoubleProperty progressoProperty() {
        return new SimpleDoubleProperty(progressoAtual);
    }

    public ObjectProperty<Date> dataConclusaoProperty() {
        return new SimpleObjectProperty<>(dataConclusao);
    }


}