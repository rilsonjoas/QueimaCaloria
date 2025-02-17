package com.example.queimacaloria.negocio;

import com.example.queimacaloria.dados.RepositorioMetasArray;
import com.example.queimacaloria.excecoes.MetaNaoEncontradaException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class ControladorMeta {

    private RepositorioMetasArray repositorio;

    public ControladorMeta() {
        this.repositorio = RepositorioMetasArray.getInstanciaUnica();
    }

    // Configura uma meta.
    public void inicializar(Meta meta, String descricao, Meta.Tipo tipo, double valorAlvo, double progressoAtual,
                            LocalDate dataConclusao) throws MetaNaoEncontradaException {

        meta.setDescricao(descricao);
        meta.setTipo(tipo);
        meta.setValorAlvo(valorAlvo);
        meta.setProgressoAtual(progressoAtual);
        meta.setDataConclusao(dataConclusao);

        try {
            repositorio.salvar(meta); // Tenta atualizar.
        } catch (MetaNaoEncontradaException e) {
            repositorio.adicionar(meta); // Se não existir, adiciona.
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

    // Conclui a meta (define a data de conclusão como a data atual).
    public void concluirMeta(Meta meta) throws MetaNaoEncontradaException {
        meta.setDataConclusao(LocalDate.now());
        repositorio.salvar(meta); // Salva a meta como concluída.
    }

    // Lista todas as metas do repositório.
    public List<Meta> listarMetas() {
        return repositorio.getAll();
    }

    // Adiciona o método de remoção
    public void remover(UUID id) throws MetaNaoEncontradaException {
        repositorio.remover(id);
    }
}