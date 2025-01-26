package com.example.queimacaloria.negocio;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.*;

@Getter
@ToString
public class Meta {
    private final UUID id;
    @Setter String descricao;
    @Setter private Tipo tipo;
    @Setter private double valorAlvo;
    @Setter private double progressoAtual;
    @Setter private Date dataCriacao;
    @Setter private Date dataConclusao;

    // Enum para representar os tipos de meta
    public enum Tipo {
        PESO, MEDIDAS, OUTROS;
    }

    // Construtor
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
}