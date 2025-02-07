package com.example.queimacaloria.negocio;

import com.example.queimacaloria.dados.RepositorioMetasArray;
import com.example.queimacaloria.excecoes.MetaNaoEncontradaException;

import java.time.LocalDate; // Usar LocalDate
import java.util.*;

public class ControladorMeta {

    RepositorioMetasArray repositorio;

    public ControladorMeta() {
        repositorio = RepositorioMetasArray.getInstanciaUnica();
    }

    // Construtor com parâmetros (Corrigido para LocalDate)
    public void inicializar(Meta meta, String descricao, Meta.Tipo tipo, double valorAlvo, double progressoAtual,
            LocalDate dataConclusao) throws MetaNaoEncontradaException { // LocalDate aqui
        meta.setDescricao(descricao);
        meta.setTipo(tipo);
        meta.setValorAlvo(valorAlvo);
        meta.setProgressoAtual(progressoAtual);
        meta.setDataConclusao(dataConclusao); // LocalDate aqui

        // Importante: Se a meta já existe, usa salvar. Se não existe, adiciona.
        try {
            repositorio.salvar(meta);
        } catch (MetaNaoEncontradaException e) {
            repositorio.adicionar(meta);
        }
    }

    // Verifica se a meta foi concluída (Corrigido para LocalDate)
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
    public void concluirMeta(Meta meta) throws MetaNaoEncontradaException {
        meta.setDataConclusao(LocalDate.now()); // Usar LocalDate.now()
        repositorio.salvar(meta);
    }

    // Método para listar todas as metas
    public List<Meta> listarMetas() {
        return repositorio.getAll();
    }
}