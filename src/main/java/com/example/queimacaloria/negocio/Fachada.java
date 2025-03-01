package com.example.queimacaloria.negocio;

import com.example.queimacaloria.controllers.MainController;
import com.example.queimacaloria.excecoes.*;

import java.time.LocalDate;
import java.util.*;

public class Fachada {

    private static Fachada instanciaUnica;

    private ControladorUsuario controladorUsuario;
    private ControladorDieta controladorDieta;
    private ControladorExercicio controladorExercicio;
    private ControladorMeta controladorMeta;
    private ControladorRefeicao controladorRefeicao;
    private ControladorTreino controladorTreino;
    private MainController mainController;
    private static final Random random = new Random();

    // Construtor privado (padrão Singleton).
    private Fachada() {
        this.controladorUsuario = new ControladorUsuario();
        this.controladorDieta = new ControladorDieta();
        this.controladorExercicio = new ControladorExercicio();
        this.controladorMeta = new ControladorMeta();
        this.controladorRefeicao = new ControladorRefeicao();
        this.controladorTreino = new ControladorTreino();
    }

    // Retorna a instância única da Fachada (Singleton).
    public static Fachada getInstanciaUnica() {
        if (instanciaUnica == null) {
            instanciaUnica = new Fachada();
        }
        return instanciaUnica;
    }

    // Define o MainController (injeção de dependência).
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    // Métodos de Usuário
    public Usuario cadastrarUsuario(String nome, String email, String senha, LocalDate dataNascimento,
                                    Usuario.Sexo sexo, float peso, float altura, String tipo) {
        //Validações
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("O nome não pode ser nulo ou vazio.");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("O email não pode ser nulo ou vazio.");
        }
        if (senha == null || senha.isEmpty()) {
            throw new IllegalArgumentException("A senha não pode ser nula ou vazia.");
        }
        if (dataNascimento == null) {
            throw new IllegalArgumentException("A data de nascimento não pode ser nula.");
        }
        if (sexo == null) {
            throw new IllegalArgumentException("O sexo não pode ser nulo.");
        }
        if (peso <= 0) {
            throw new IllegalArgumentException("O peso deve ser maior que zero.");
        }
        if (altura <= 0) {
            throw new IllegalArgumentException("A altura deve ser maior que zero.");
        }

        List<Usuario> usuarios = controladorUsuario.listarUsuarios();
        for (Usuario user : usuarios) {
            if (user.getEmail().equals(email)) {
                throw new IllegalArgumentException("Email já cadastrado.");
            }
        }

        Usuario novoUsuario = new Usuario(nome, email, dataNascimento, sexo, peso, altura, Usuario.TipoUsuario.USUARIO_COMUM);
        if(nome.equals("Administrador")){
            novoUsuario.setTipo(Usuario.TipoUsuario.ADMINISTRADOR.getDescricao());
        }
        controladorUsuario.cadastrarUsuario(novoUsuario);
        return novoUsuario;
    }

    public Usuario buscarUsuarioPorId(UUID id) throws UsuarioNaoEncontradoException{
        return controladorUsuario.buscarPorId(id);
    }

    public List<Usuario> listarUsuarios() {
        return controladorUsuario.listarUsuarios();
    }

    public List<Usuario> listarUsuarios(Usuario.TipoUsuario tipo) { //ADICIONADO
        return controladorUsuario.listarUsuarios(tipo);
    }

    public int calcularIdadeUsuario(Usuario usuario) {
        return controladorUsuario.getIdade(usuario);
    }

    public void removerUsuario(UUID id) throws UsuarioNaoEncontradoException {
        controladorUsuario.remover(id);
    }

    // Métodos de Dieta

    public void configurarDieta(Dieta dieta, String nome, Meta.Tipo objetivo, int caloriasDiarias, // Meta.Tipo agora!
                                Usuario usuario) throws DietaNaoEncontradaException {
        controladorDieta.configurarDieta(dieta, nome, objetivo, caloriasDiarias, usuario);
    }

    public void setDietaAtiva(Usuario usuario, Dieta dieta) throws UsuarioNaoEncontradoException{
        controladorUsuario.setDietaAtiva(usuario, dieta);
    }

    public Dieta getDietaAtiva(Usuario usuario) throws  UsuarioNaoEncontradoException{
        return controladorUsuario.getDietaAtiva(usuario);
    }

    public List<Dieta> listarDietas() {
        return controladorDieta.listarDietas();
    }
    public void removerDieta(UUID id) throws DietaNaoEncontradaException{
        controladorDieta.removerDieta(id);
    }
    public void adicionarDietaUsuario(Usuario usuario, Dieta dieta) throws UsuarioNaoEncontradoException {
        controladorUsuario.adicionarDieta(usuario, dieta);
    }

    public Dieta getDietaAleatoria(Meta.Tipo tipo) {
        List<Dieta> dietas = InicializadorDados.inicializarDietas();
        List<Dieta> dietasFiltradas = new ArrayList<>();

        // Filtra as dietas pelo tipo.
        for (Dieta dieta : dietas) {
            if (dieta.getObjetivo() == tipo) {
                dietasFiltradas.add(dieta);
            }
        }

        if (dietasFiltradas.isEmpty()) {
            return null;
        }
        int indiceAleatorio = random.nextInt(dietasFiltradas.size());
        return dietasFiltradas.get(indiceAleatorio);
    }


    // Métodos de Exercício

    public void configurarExercicio(Exercicio exercicio, String nome, String descricao, Exercicio.TipoExercicio tipo,
                                    int tempo, double caloriasQueimadas) throws ExercicioNaoEncontradoException {
        controladorExercicio.inicializar(exercicio, nome, descricao, tipo, tempo, caloriasQueimadas);
    }

    public void adicionarMusculoExercicio(Exercicio exercicio, String musculo) throws ExercicioNaoEncontradoException {
        controladorExercicio.adicionarMusculoTrabalhado(exercicio, musculo);
    }

    public void removerMusculoExercicio(Exercicio exercicio, String musculo) throws ExercicioNaoEncontradoException {
        controladorExercicio.removerMusculoTrabalhado(exercicio, musculo);
    }

    public void concluirExercicio(Exercicio exercicio) throws ExercicioNaoEncontradoException {
        controladorExercicio.concluir(exercicio);
    }

    public List<Exercicio> listarExercicios() {
        return controladorExercicio.listarExercicios();
    }

    public void removerExercicio(UUID id) throws ExercicioNaoEncontradoException {
        controladorExercicio.remover(id);
    }


    // Métodos de Meta

    public void configurarMeta(Meta meta, String descricao, Meta.Tipo tipo, double valorAlvo, double progressoAtual,
                               LocalDate dataConclusao) throws MetaNaoEncontradaException {
        controladorMeta.inicializar(meta, descricao, tipo, valorAlvo, progressoAtual, dataConclusao);

        // Adiciona a meta ao usuário logado (injeção de dependência).
        if (mainController != null && mainController.getUsuarioLogado() != null) {
            try {
                Usuario usuarioAtualizado = controladorUsuario.buscarPorId(mainController.getUsuarioLogado().getId());
                controladorUsuario.cadastrarMeta(usuarioAtualizado, meta);
                mainController.setUsuarioLogado(usuarioAtualizado);
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
    public List<Meta> listarMetas() {
        return controladorMeta.listarMetas();
    }
    public void removerMeta(UUID id) throws MetaNaoEncontradaException {
        controladorMeta.remover(id);
    }
    public void cadastrarMetaUsuario(Usuario usuario, Meta meta) throws UsuarioNaoEncontradoException {
        controladorUsuario.cadastrarMeta(usuario, meta);
    }

    public void atualizarMeta(UUID metaId, String descricao, Meta.Tipo tipo, double valorAlvo, double progressoAtual, LocalDate dataConclusao) throws MetaNaoEncontradaException {
        controladorMeta.atualizarMeta(metaId, descricao, tipo, valorAlvo, progressoAtual, dataConclusao);

        // Atualiza o usuário logado (injeção de dependência - MUITO importante)
        if (mainController != null && mainController.getUsuarioLogado() != null) {
            try {
                Usuario usuarioAtualizado = controladorUsuario.buscarPorId(mainController.getUsuarioLogado().getId());
                mainController.setUsuarioLogado(usuarioAtualizado); // Atualiza o usuário no MainController
            } catch (UsuarioNaoEncontradoException e) {
                System.err.println("Erro ao atualizar usuário na Fachada (após atualizar meta): " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    // Métodos de Refeição

    public void configurarRefeicao(Refeicao refeicao, String nome, String descricao,
                                   Map<String, Double> macronutrientes) {
        controladorRefeicao.inicializar(refeicao, nome, descricao, macronutrientes);
    }

    public int calcularCaloriasRefeicao(Refeicao refeicao) {
        return controladorRefeicao.calcularCalorias(refeicao);
    }

    public Map<String, Double> calcularMacronutrientesRefeicao(Refeicao refeicao) {
        return controladorRefeicao.calcularMacronutrientes(refeicao);
    }
    public List<Refeicao> listarRefeicoes() {
        return controladorRefeicao.listarRefeicoes();
    }
    public void removerRefeicao(UUID id) throws RefeicaoNaoEncontradaException {
        controladorRefeicao.remover(id);
    }

    public Refeicao getRefeicaoAleatoria() {
        List<Refeicao> refeicoes = InicializadorDados.inicializarRefeicoes();
        if (refeicoes.isEmpty()) {
            return null;
        }
        int indiceAleatorio = random.nextInt(refeicoes.size());
        return refeicoes.get(indiceAleatorio);
    }

    // Métodos de Treino
    public void configurarTreino(Treino treino, String nome, Exercicio.TipoExercicio tipoDeTreino, int duracao, int nivelDeDificuldade)
            throws TreinoNaoEncontradoException {
        controladorTreino.inicializar(treino, nome, Exercicio.TipoExercicio.valueOf(String.valueOf(tipoDeTreino)), duracao, nivelDeDificuldade);
    }

    public void adicionarTreinoAoUsuario(Usuario usuario, Treino treino) throws UsuarioNaoEncontradoException {
        controladorUsuario.adicionarTreino(usuario, treino);
    }

    public void atualizarTreino(Treino treino) throws TreinoNaoEncontradoException{
        controladorTreino.atualizarProgresso(treino);
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
        return treino.calcularCaloriasTotais();
    }

    public void atualizarProgressoTreino(Treino treino) throws TreinoNaoEncontradoException {
        controladorTreino.atualizarProgresso(treino);
    }
    public List<Treino> listarTreinos() {
        return controladorTreino.listarTreinos();
    }
    public void removerTreino(UUID id) throws TreinoNaoEncontradoException {
        controladorTreino.remover(id);
    }

    // Métodos para o Administrador

    public List<Usuario> listarAdministradores() {
        return controladorUsuario.listarUsuarios(Usuario.TipoUsuario.ADMINISTRADOR);
    }

    public List<Usuario> listarUsuariosComuns() {
        return controladorUsuario.listarUsuarios(Usuario.TipoUsuario.USUARIO_COMUM);
    }

    //Sobrecarga para incluir o TipoUsuario
    public void atualizarDadosUsuario(Usuario usuario, String nome, String email, String senha,
                                      LocalDate dataNascimento,
                                      Usuario.Sexo sexo, float peso, float altura, String tipo) throws UsuarioNaoEncontradoException
    {
        Usuario.TipoUsuario tipouser;
        if(tipo.equals(Usuario.TipoUsuario.ADMINISTRADOR.getDescricao())){
            tipouser = Usuario.TipoUsuario.ADMINISTRADOR;
        } else {
            tipouser = Usuario.TipoUsuario.USUARIO_COMUM;
        }
        System.out.println("Atualizando usuário: " + usuario.getNome() + ", Tipo: " + usuario.getTipo() + " para tipo " + tipouser.getDescricao());
        controladorUsuario.atualizarDados(usuario.getId(), nome, email, senha, dataNascimento, sexo, peso, altura, tipouser.getDescricao());
    }

    public void beberAgua(Usuario usuario, int ml) throws UsuarioNaoEncontradoException {
        controladorUsuario.beberAgua(usuario, ml);
    }

    public void zerarAgua(Usuario usuario) throws UsuarioNaoEncontradoException {
        controladorUsuario.zerarAgua(usuario);
    }
}