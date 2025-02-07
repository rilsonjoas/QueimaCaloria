package com.example.queimacaloria.negocio;

import com.example.queimacaloria.dados.*;
import com.example.queimacaloria.excecoes.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;  // Importe java.util.Map

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

    public static Fachada getInstanciaUnica() {
        if (instanciaUnica == null) {
            instanciaUnica = new Fachada();
        }
        return instanciaUnica;
    }

    // Métodos de Usuário
    public void atualizarDadosUsuario(Usuario usuario, String nome, String email, String senha,
            LocalDate dataNascimento,
            Usuario.Sexo sexo, float peso, float altura) throws UsuarioNaoEncontradoException {
        controladorUsuario.atualizarDados(usuario, nome, email, senha, dataNascimento, sexo, peso, altura);
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

    public double calcularProgressoGeralUsuario(Usuario usuario) {
        return controladorUsuario.getProgresso(usuario);
    }

    public int calcularIdadeUsuario(Usuario usuario) {
        return controladorUsuario.getIdade(usuario);
    }

    // Métodos de Dieta
    public void configurarDieta(Dieta dieta, String nome, Dieta.ObjetivoDieta objetivo, int caloriasDiarias,
            Usuario usuario) throws DietaNaoEncontradaException {
        controladorDieta.configurarDieta(dieta, nome, objetivo, caloriasDiarias, usuario);
    }

    public void inserirRefeicaoDieta(Dieta dieta, Refeicao refeicao) throws DietaNaoEncontradaException {
        controladorDieta.inserirRefeicao(dieta, refeicao);
    }

    public void excluirRefeicaoDieta(Dieta dieta, Refeicao refeicao) throws DietaNaoEncontradaException {
        controladorDieta.excluirRefeicao(dieta, refeicao);
    }

    public Map<String, Double> calcularTotalMacronutrientesDieta(Dieta dieta) {
        return controladorDieta.calcularTotalMacronutrientes(dieta);
    }

    public int calcularTotalCaloriasDieta(Dieta dieta) {
        return controladorDieta.calcularTotalCalorias(dieta);
    }

    public double calcularProgressoDieta(Dieta dieta) {
        return controladorDieta.calcularProgresso(dieta);
    }

    public boolean verificarDietaConcluida(Dieta dieta) {
        return controladorDieta.verificarDietaConcluida(dieta);
    }

    // Métodos de Exercício
    public void configurarExercicio(Exercicio exercicio, String nome, String descricao, Exercicio.TipoExercicio tipo,
            int tempo, double caloriasQueimadasPorMinuto) throws ExercicioNaoEncontradoException {
        controladorExercicio.inicializar(exercicio, nome, descricao, tipo, tempo, caloriasQueimadasPorMinuto);
    }

    public void adicionarMusculoExercicio(Exercicio exercicio, String musculo) throws ExercicioNaoEncontradoException {
        controladorExercicio.adicionarMusculoTrabalhado(exercicio, musculo);
    }

    public void removerMusculoExercicio(Exercicio exercicio, String musculo) throws ExercicioNaoEncontradoException {
        controladorExercicio.removerMusculoTrabalhado(exercicio, musculo);
    }

    public double calcularCaloriasQueimadasExercicio(Exercicio exercicio) throws ExercicioNaoEncontradoException {
        return controladorExercicio.calcularCaloriasQueimadas(exercicio);
    }

    public void concluirExercicio(Exercicio exercicio) throws ExercicioNaoEncontradoException {
        controladorExercicio.concluir(exercicio);
    }

    // Métodos de Meta
    public void configurarMeta(Meta meta, String descricao, Meta.Tipo tipo, double valorAlvo, double progressoAtual,
            LocalDate dataConclusao) throws MetaNaoEncontradaException {
        controladorMeta.inicializar(meta, descricao, tipo, valorAlvo, progressoAtual, dataConclusao);
    }

    public boolean verificarMetaConcluida(Meta meta) {
        return controladorMeta.isConcluida(meta);
    }

    public double calcularProgressoMeta(Meta meta) {
        return controladorMeta.getProgresso(meta);
    }

    public void concluirMeta(Meta meta) throws MetaNaoEncontradaException {
        controladorMeta.concluirMeta(meta);
    }

    // Métodos de Refeição
    public void configurarRefeicao(Refeicao refeicao, String nome, String descricao,
            Map<String, Double> macronutrientes) {  // Já está correto
        controladorRefeicao.inicializar(refeicao, nome, descricao, macronutrientes);
    }

    public int calcularCaloriasRefeicao(Refeicao refeicao) {
        return controladorRefeicao.calcularCalorias(refeicao);
    }

    public Map<String, Double> calcularMacronutrientesRefeicao(Refeicao refeicao) {
        return controladorRefeicao.calcularMacronutrientes(refeicao);
    }

    // Métodos de Treino
    public void configurarTreino(Treino treino, String nome, String tipoDeTreino, int duracao, int nivelDeDificuldade)
            throws TreinoNaoEncontradoException {
        controladorTreino.inicializar(treino, nome, tipoDeTreino, duracao, nivelDeDificuldade);
    }

    public void inserirExercicioTreino(Treino treino, Exercicio exercicio)
            throws TreinoNaoEncontradoException, ExercicioNaoEncontradoException {
        controladorTreino.adicionarExercicio(treino, exercicio);
    }

    public void excluirExercicioTreino(Treino treino, Exercicio exercicio)
            throws TreinoNaoEncontradoException, ExercicioNaoEncontradoException {
        controladorTreino.removerExercicio(treino, exercicio);
    }

    public double calcularCaloriasQueimadasTreino(Treino treino)
            throws TreinoNaoEncontradoException, ExercicioNaoEncontradoException {
        return controladorTreino.calcularCaloriasQueimadas(treino);
    }

    public void atualizarProgressoTreino(Treino treino) throws TreinoNaoEncontradoException {
        controladorTreino.atualizarProgresso(treino);
    }

    public List<Dieta> listarDietas() {
        return controladorDieta.listarDietas();
    }

    public List<Exercicio> listarExercicios() {
        return controladorExercicio.listarExercicios();
    }

    public List<Meta> listarMetas() {
        return controladorMeta.listarMetas();
    }

    public List<Refeicao> listarRefeicoes() {
        return controladorRefeicao.listarRefeicoes();
    }

    public List<Treino> listarTreinos() {
        return controladorTreino.listarTreinos();
    }
}