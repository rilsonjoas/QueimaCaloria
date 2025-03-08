package com.example.queimacaloria.negocio;

import com.example.queimacaloria.dados.RepositorioMetasArray;
import com.example.queimacaloria.excecoes.MetaNaoEncontradaException;
import javafx.beans.property.ObjectProperty;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class ControladorMeta {

    private RepositorioMetasArray repositorio;

    public ControladorMeta() {
        this.repositorio = RepositorioMetasArray.getInstanciaUnica();
    }

    // Modificado: Recebe o Usuario
    public void inicializar(Meta meta, String descricao, Meta.Tipo tipo, double valorAlvo, double progressoAtual,
                            LocalDate dataConclusao, Usuario usuario) throws MetaNaoEncontradaException {

        System.out.println("ControladorMeta.inicializar: ");
        System.out.println("Meta ID " + meta.getId());
        System.out.println("Descrição: " + descricao);

        meta.setDescricao(descricao);
        meta.setTipo(tipo);
        meta.setValorAlvo(valorAlvo);
        meta.setProgressoAtual(progressoAtual);
        meta.setDataConclusao(dataConclusao);
        meta.setUsuario(usuario);

        System.out.println("Meta após setar valores no Controlador: " + meta);

        try {
            repositorio.salvar(meta);
        } catch (MetaNaoEncontradaException e) {
            repositorio.adicionar(meta);
        }
    }

    public boolean isConcluida(Meta meta) {
        return meta.getDataConclusao() != null;
    }

    public double getProgresso(Meta meta) {
        if (meta.getValorAlvo() == 0) {
            return 0;
        }
        return (meta.getProgressoAtual() / meta.getValorAlvo()) * 100;
    }

    public void atualizarMeta(UUID metaId, String descricao, Meta.Tipo tipo, double valorAlvo, double progressoAtual, LocalDate dataConclusao)
            throws MetaNaoEncontradaException {

        Meta meta = repositorio.buscar(metaId);
        if (meta == null) {
            throw new MetaNaoEncontradaException("Meta com ID " + metaId + " não encontrada.");
        }

        meta.setDescricao(descricao);
        meta.setTipo(tipo);
        meta.setValorAlvo(valorAlvo);
        meta.setProgressoAtual(progressoAtual);
        meta.setDataConclusao(dataConclusao);

        repositorio.salvar(meta);
    }


    public void concluirMeta(Meta meta) throws MetaNaoEncontradaException {
        meta.setDataConclusao(LocalDate.now());
        repositorio.salvar(meta);
    }

    public List<Meta> listarMetas() {
        if (repositorio == null) {
            System.err.println("ERRO CRÍTICO: Repositório nulo em ControladorMeta.listarMetas()!");
        }
        List<Meta> metas = repositorio.getAll();
        System.out.println("ControladorMeta.listarMetas(): Metas retornadas: " + metas);
        return metas;
    }

    public void remover(UUID id) throws MetaNaoEncontradaException {
        repositorio.remover(id);
    }
}