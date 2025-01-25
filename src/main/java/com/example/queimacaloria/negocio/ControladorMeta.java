package com.example.queimacaloria.negocio;

import java.util.*;

public class ControladorMeta {

    // Construtor com parâmetros
    public void inicializar(Meta meta, String descricao, Meta.Tipo tipo, double valorAlvo, double progressoAtual,
            Date dataConclusao) {
        meta.setDescricao(descricao);
        meta.setTipo(tipo);
        meta.setValorAlvo(valorAlvo);
        meta.setProgressoAtual(progressoAtual);
        meta.setDataConclusao(dataConclusao);
    }

    // Verifica se a meta foi concluída
    public boolean isConcluida(Meta meta) {
        return meta.getDataConclusao() != null;
    }

    // Calcula o progresso da meta em porcentagem
    public double getProgresso(Meta meta) {
        if (meta.getValorAlvo() == 0) {
            return 0;
        }
        return (meta.getProgressoAtual() / meta.getValorAlvo()) * 100;
    }

    // Define a data de conclusão para a data atual, marcando a meta como concluída
    public void concluirMeta(Meta meta) {
        meta.setDataConclusao(new Date());
    }
}
