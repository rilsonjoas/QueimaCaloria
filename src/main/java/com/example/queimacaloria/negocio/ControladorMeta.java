package com.example.queimacaloria.negocio;

import com.example.queimacaloria.dados.RepositorioMetasArray;
import com.example.queimacaloria.excecoes.MetaNaoEncontradaException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class ControladorMeta {

    private RepositorioMetasArray repositorio;

    // Construtor, inicializa o repositório.
    public ControladorMeta() {
        this.repositorio = RepositorioMetasArray.getInstanciaUnica();
    }

    // Inicializa uma meta, atualizando ou adicionando ao repositório.
    public void inicializar(Meta meta, String descricao, Meta.Tipo tipo, double valorAlvo, double progressoAtual,
                            LocalDate dataConclusao) throws MetaNaoEncontradaException {

        System.out.println("ControladorMeta.inicializar: ");
        System.out.println("Meta ID " + meta.getId());
        System.out.println("Descrição: " + descricao);

        meta.setDescricao(descricao);
        meta.setTipo(tipo);
        meta.setValorAlvo(valorAlvo);
        meta.setProgressoAtual(progressoAtual);
        meta.setDataConclusao(dataConclusao);

        System.out.println("Meta após setar valores no Controlador: " + meta);

        try {
            repositorio.salvar(meta);
        } catch (MetaNaoEncontradaException e) {
            repositorio.adicionar(meta);
        }
    }

    // Verifica se a meta foi concluída.
    public boolean isConcluida(Meta meta) {
        return meta.getDataConclusao() != null;
    }

    // Calcula o progresso da meta (em porcentagem).
    public double getProgresso(Meta meta) {
        if (meta.getValorAlvo() == 0) {
            return 0;
        }
        return (meta.getProgressoAtual() / meta.getValorAlvo()) * 100;
    }

    // Conclui a meta (define a data de conclusão).
    public void concluirMeta(Meta meta) throws MetaNaoEncontradaException {
        meta.setDataConclusao(LocalDate.now());
        repositorio.salvar(meta);
    }

    // Lista todas as metas do repositório.
    public List<Meta> listarMetas() {
        return repositorio.getAll();
    }

    // Remove uma meta pelo ID.
    public void remover(UUID id) throws MetaNaoEncontradaException {
        repositorio.remover(id);
    }
}