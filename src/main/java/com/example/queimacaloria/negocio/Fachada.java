package com.example.queimacaloria.negocio;

import com.example.queimacaloria.excecoes.TreinoNaoEncontradoException;
import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

public class Fachada {

    private static Fachada instanciaUnica;

    private ControladorUsuario controladorUsuario;
    private ControladorDieta controladorDieta;
    private ControladorExercicio controladorExercicio;
    private ControladorMeta controladorMeta;
    private ControladorRefeicao controladorRefeicao;
    private ControladorTreino controladorTreino;

    private Fachada() {
        this.controladorUsuario = new ControladorUsuario();
        this.controladorDieta = new ControladorDieta();
        this.controladorExercicio = new ControladorExercicio();
        this.controladorMeta = new ControladorMeta();
        this.controladorRefeicao = new ControladorRefeicao();
        this.controladorTreino = new ControladorTreino();


    }

    public static Fachada getInstanciaUnica() { if (instanciaUnica == null) {
        instanciaUnica = new Fachada();
    }
        return instanciaUnica;

    }

    // Métodos de Usuário
    public void atualizarDadosUsuario(Usuario usuario, String nome, String email,String senha, LocalDate dataNascimento,
            Usuario.Sexo sexo, float peso, float altura) throws UsuarioNaoEncontradoException {
        controladorUsuario.atualizarDados(usuario, nome, email,senha, dataNascimento, sexo, peso, altura);
    }

    public float calcularIMCUsuario(Usuario usuario) throws UsuarioNaoEncontradoException {
        return controladorUsuario.calcularIMC(usuario);
    }

    public void cadastrarMetaUsuario(Usuario usuario, Meta meta) throws UsuarioNaoEncontradoException {
        controladorUsuario.cadastrarMeta(usuario, meta);
    }

    public void adicionarTreinoUsuario(Usuario usuario, Treino treino) throws UsuarioNaoEncontradoException {
        controladorUsuario.adicionarTreino(usuario, treino);
    }

    public void adicionarDietaUsuario(Usuario usuario, Dieta dieta) throws UsuarioNaoEncontradoException {
        controladorUsuario.adicionarDieta(usuario, dieta);
    }

    public double getProgressoUsuario(Usuario usuario) {
        return controladorUsuario.getProgresso(usuario);
    }

    public int getIdadeUsuario(Usuario usuario) {
        return controladorUsuario.getIdade(usuario);
    }

    // Métodos de Dieta
    public void inicializarDieta(Dieta dieta, String nome, Dieta.Objetivo objetivo, int caloriasDiarias,
            Usuario usuario) {
        controladorDieta.inicializar(dieta, nome, objetivo, caloriasDiarias, usuario);
    }

    public void adicionarRefeicaoDieta(Dieta dieta, Refeicao refeicao) {
        controladorDieta.adicionarRefeicao(dieta, refeicao);
    }

    public void removerRefeicaoDieta(Dieta dieta, Refeicao refeicao) {
        controladorDieta.removerRefeicao(dieta, refeicao);
    }

    public Map<String, Double> calcularMacronutrientesDieta(Dieta dieta) {
        return controladorDieta.calcularMacronutrientes(dieta);
    }

    public int calcularCaloriasTotaisDieta(Dieta dieta) {
        return controladorDieta.calcularCaloriasTotais(dieta);
    }

    public double getProgressoDieta(Dieta dieta) {
        return controladorDieta.getProgresso(dieta);

    }

    public boolean isConcluidoDieta(Dieta dieta) {
        return controladorDieta.isConcluido(dieta);
    }

    // Métodos de Exercício
    public void inicializarExercicio(Exercicio exercicio, String nome, String descricao, Exercicio.TipoExercicio tipo,
            int tempo, double caloriasQueimadasPorMinuto) {
        controladorExercicio.inicializar(exercicio, nome, descricao, tipo, tempo, caloriasQueimadasPorMinuto);
    }

    public void adicionarMusculoTrabalhadoExercicio(Exercicio exercicio, String musculo) {
        controladorExercicio.adicionarMusculoTrabalhado(exercicio, musculo);
    }

    public void removerMusculoTrabalhadoExercicio(Exercicio exercicio, String musculo) {
        controladorExercicio.removerMusculoTrabalhado(exercicio, musculo);

    }

    public double calcularCaloriasQueimadasExercicio(Exercicio exercicio) {
        return controladorExercicio.calcularCaloriasQueimadas(exercicio);
    }

    public void concluirExercicio(Exercicio exercicio) {
        controladorExercicio.concluir(exercicio);
    }

    // Métodos de Meta
    public void inicializarMeta(Meta meta, String descricao, Meta.Tipo tipo, double valorAlvo, double progressoAtual,
            Date dataConclusao) {
        controladorMeta.inicializar(meta, descricao, tipo, valorAlvo, progressoAtual, dataConclusao);
    }

    public boolean isConcluidaMeta(Meta meta) {
        return controladorMeta.isConcluida(meta);
    }

    public double getProgressoMeta(Meta meta) {
        return controladorMeta.getProgresso(meta);

    }

    public void concluirMeta(Meta meta) {
        controladorMeta.concluirMeta(meta);
    }

    // Métodos de Refeição
    public void inicializarRefeicao(Refeicao refeicao, String nome, String descricao,
            Map<String, Double> macronutrientes) {
        controladorRefeicao.inicializar(refeicao, nome, descricao, macronutrientes);
    }

    public int calcularCaloriasRefeicao(Refeicao refeicao) {
        return controladorRefeicao.calcularCalorias(refeicao);

    }

    public Map<String, Double> calcularMacronutrientesRefeicao(Refeicao refeicao) {
        return controladorRefeicao.calcularMacronutrientes(refeicao);
    }

    // Métodos de Treino
    public void inicializarTreino(Treino treino, String nome, String tipoDeTreino, int duracao, int nivelDeDificuldade,
            Usuario usuario) {
        controladorTreino.inicializar(treino, nome, tipoDeTreino, duracao, nivelDeDificuldade);
    }

    public void adicionarExercicioTreino(Treino treino, Exercicio exercicio) throws TreinoNaoEncontradoException {
        controladorTreino.adicionarExercicio(treino, exercicio);
    }

    public void removerExercicioTreino(Treino treino, Exercicio exercicio) throws TreinoNaoEncontradoException {
        controladorTreino.removerExercicio(treino, exercicio);
    }

    public double calcularCaloriasQueimadasTreino(Treino treino) throws TreinoNaoEncontradoException {
        return controladorTreino.calcularCaloriasQueimadas(treino);
    }

    public void atualizarProgressoTreino(Treino treino) throws TreinoNaoEncontradoException {
        controladorTreino.atualizarProgresso(treino);
    }

}