package com.example.queimacaloria.negocio;

import com.example.queimacaloria.controllers.MainController;
import com.example.queimacaloria.dados.RepositorioUsuariosArray;
import com.example.queimacaloria.excecoes.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Fachada {

    private static Fachada instanciaUnica;

    private ControladorUsuario controladorUsuario;
    private ControladorDieta controladorDieta;
    private ControladorExercicio controladorExercicio;
    private ControladorMeta controladorMeta;
    private ControladorRefeicao controladorRefeicao;
    private ControladorTreino controladorTreino;

    // Adicionado: Referência ao MainController
    private MainController mainController;

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

    // Adicionado: Método para definir o MainController
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }


    // MÉTODO para cadastro
    public Usuario cadastrarUsuario(String nome, String email, String senha, LocalDate dataNascimento,
                                    Usuario.Sexo sexo, float peso, float altura)  { // Removida a exception
        // Verifica se o e-mail já existe (importante!)
        List<Usuario> usuarios = controladorUsuario.listarUsuarios();
        for (Usuario user : usuarios) {
            if (user.getEmail().equals(email)) {
                throw new IllegalArgumentException("Email já cadastrado."); // Use IllegalArgumentException, mais apropriado
            }
        }

        // Cria e adiciona o novo usuário diretamente
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(nome);
        novoUsuario.setEmail(email);
        novoUsuario.setSenha(senha);
        novoUsuario.setDataNascimento(dataNascimento);
        novoUsuario.setSexo(sexo);
        novoUsuario.setPeso(peso);
        novoUsuario.setAltura(altura);
        // Adiciona o usuário ao repositório e já retorna o usuário.
        controladorUsuario.cadastrarUsuario(novoUsuario);
        return novoUsuario;

    }

    // Métodos de Usuário
    public void atualizarDadosUsuario(Usuario usuario, String nome, String email, String senha,
                                      LocalDate dataNascimento,
                                      Usuario.Sexo sexo, float peso, float altura) throws UsuarioNaoEncontradoException {
        controladorUsuario.atualizarDados(usuario.getId(), nome, email, senha, dataNascimento, sexo, peso, altura);
    }

    public void cadastrarMetaUsuario(Usuario usuario, Meta meta) throws UsuarioNaoEncontradoException {
        controladorUsuario.cadastrarMeta(usuario, meta);
    }

    public Usuario buscarUsuarioPorId(UUID id) throws UsuarioNaoEncontradoException{
        return controladorUsuario.buscarPorId(id);
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

    //Adicionado o método listarUsuários
    public List<Usuario> listarUsuarios() {
        return controladorUsuario.listarUsuarios();
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

    // Adicionado o método de remoção de exercício
    public void removerExercicio(UUID id) throws ExercicioNaoEncontradoException {
        controladorExercicio.remover(id);
    }

    // Métodos de Meta
    public void configurarMeta(Meta meta, String descricao, Meta.Tipo tipo, double valorAlvo, double progressoAtual,
                               LocalDate dataConclusao) throws MetaNaoEncontradaException {
        System.out.println("Fachada.configurarMeta:");
        System.out.println("  Meta ID: " + meta.getId());
        System.out.println("  Descrição: " + descricao);
        System.out.println("  Tipo: " + tipo);
        System.out.println("  Valor Alvo: " + valorAlvo);
        System.out.println("  Progresso Atual: " + progressoAtual);
        System.out.println("  Data Conclusão: " + dataConclusao);

        controladorMeta.inicializar(meta, descricao, tipo, valorAlvo, progressoAtual, dataConclusao);

        // MUITO IMPORTANTE: Depois de inicializar (ou seja, criar ou atualizar)
        // a meta, adicione-a ao usuário logado, se houver um.
        if (mainController != null && mainController.getUsuarioLogado() != null) {
            try {
                Usuario usuarioAtualizado = controladorUsuario.buscarPorId(mainController.getUsuarioLogado().getId());
                controladorUsuario.cadastrarMeta(usuarioAtualizado, meta);  // ADICIONA A META AO USUÁRIO!
                mainController.setUsuarioLogado(usuarioAtualizado);     // ATUALIZA O USUÁRIO LOGADO!
            } catch (UsuarioNaoEncontradoException e) {
                System.out.println("Erro ao atualizar usuário na Fachada: " + e.getMessage());
            }

        } else {
            System.out.println("Não foi possível adicionar a meta ao usuário, usuário não logado ou MainController nulo");

        }
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

    public void removerTreino(UUID id) throws TreinoNaoEncontradoException {
        controladorTreino.remover(id);
    }

    public void removerRefeicao(UUID id) throws RefeicaoNaoEncontradaException {
        controladorRefeicao.remover(id);
    }

    public void removerMeta(UUID id) throws MetaNaoEncontradaException {
        controladorMeta.remover(id);
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