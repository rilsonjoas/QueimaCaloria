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
    private MainController mainController; // Para injeção de dependência
    private static final Random random = new Random();

    // Construtor privado (padrão Singleton).
    private Fachada() {
        this.controladorUsuario = new ControladorUsuario();
        this.controladorDieta = new ControladorDieta();
        this.controladorExercicio = new ControladorExercicio();
        this.controladorMeta = new ControladorMeta();
        this.controladorRefeicao = new ControladorRefeicao();
        this.controladorTreino = new ControladorTreino();
        inicializarDadosPredefinidos(); //Inicializa os dados.
    }


    public static Fachada getInstanciaUnica() {
        if (instanciaUnica == null) {
            instanciaUnica = new Fachada();
        }
        return instanciaUnica;
    }

    // Injeção de dependência do MainController
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }


    // Métodos de Usuário (sem mudanças)
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
    public void configurarDieta(Dieta dieta, String nome, Meta.Tipo objetivo, int caloriasDiarias, Usuario usuario)
            throws DietaNaoEncontradaException {
        dieta.setNome(nome);
        dieta.setObjetivo(objetivo);
        dieta.setCaloriasDiarias(caloriasDiarias);
        dieta.setUsuario(usuario); // Associa a dieta ao usuário.

        try {
            controladorDieta.configurarDieta(dieta, nome, objetivo, caloriasDiarias, usuario);
        } catch (DietaNaoEncontradaException e) {
            // Se a dieta não existe, ela é criada, então em tese nunca deve cair aqui, mas mantemos por segurança
            controladorDieta.configurarDieta(dieta, nome, objetivo, caloriasDiarias, usuario);
        }
    }
    public List<Dieta> listarDietas() {
        return controladorDieta.listarDietas();
    }
    public void removerDieta(UUID id) throws DietaNaoEncontradaException{
        controladorDieta.removerDieta(id);
    }

    public void setDietaAtiva(Usuario usuario, Dieta dieta) throws UsuarioNaoEncontradoException{
        controladorUsuario.setDietaAtiva(usuario, dieta);
    }

    public Dieta getDietaAtiva(Usuario usuario) throws  UsuarioNaoEncontradoException{
        return controladorUsuario.getDietaAtiva(usuario);
    }


    // Métodos de Exercício
    public void configurarExercicio(Exercicio exercicio, String nome, String descricao, Exercicio.TipoExercicio tipo,
                                    int tempo, double caloriasQueimadas, Usuario usuario) throws ExercicioNaoEncontradoException {
        controladorExercicio.inicializar(exercicio, nome, descricao, tipo, tempo, caloriasQueimadas, usuario);
    }

    public List<Exercicio> listarExercicios() {
        return controladorExercicio.listarExercicios();
    }

    public void adicionarMusculoExercicio(Exercicio exercicio, String musculo) throws ExercicioNaoEncontradoException {
        controladorExercicio.adicionarMusculoTrabalhado(exercicio, musculo);
    }

    public void removerMusculoExercicio(Exercicio exercicio, String musculo) throws ExercicioNaoEncontradoException {
        controladorExercicio.removerMusculoTrabalhado(exercicio, musculo);
    }

    public void concluirExercicio(Exercicio exercicio) throws ExercicioNaoEncontradoException {
        exercicio.setConcluido(true);
        controladorExercicio.concluir(exercicio);
    }

    public void removerExercicio(UUID id) throws ExercicioNaoEncontradoException {
        controladorExercicio.remover(id);
    }

    // Métodos de Meta
    public void configurarMeta(Meta meta, String descricao, Meta.Tipo tipo,
                               double valorAlvo, double progressoAtual,
                               LocalDate dataConclusao, Usuario usuario) throws MetaNaoEncontradaException {

        if (meta.getUsuario() == null) {
            throw new IllegalArgumentException("A meta deve estar associada a um usuário.");
        }

        controladorMeta.inicializar(meta, descricao, tipo, valorAlvo,
                progressoAtual, dataConclusao, meta.getUsuario()); // Usar o usuário da meta
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


    public void atualizarMeta(UUID metaId, String descricao, Meta.Tipo tipo, double valorAlvo, double progressoAtual, LocalDate dataConclusao) throws MetaNaoEncontradaException {

        Meta meta = controladorMeta.listarMetas().stream().filter(m -> m.getId().equals(metaId)).findFirst().orElseThrow(() -> new MetaNaoEncontradaException("Meta não encontrada com o ID fornecido."));

        controladorMeta.atualizarMeta(metaId, descricao, tipo, valorAlvo, progressoAtual, dataConclusao);
    }



    // Métodos de Refeição - O usuário agora é definido dentro do controlador
    public void configurarRefeicao(Refeicao refeicao, String nome, String descricao,
                                   Map<String, Double> macronutrientes, Usuario usuario) {
        controladorRefeicao.inicializar(refeicao, nome, descricao, macronutrientes,  usuario);
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

    // Métodos de Treino -  O usuário agora é definido dentro do controlador.
    public void configurarTreino(Treino treino, String nome, Exercicio.TipoExercicio tipoDeTreino, int duracao, int nivelDeDificuldade, Usuario usuario)
            throws TreinoNaoEncontradoException {
        controladorTreino.inicializar(treino, nome, tipoDeTreino, duracao, nivelDeDificuldade, usuario);
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

    // Método para obter uma dieta aleatória (usado para sugestões)
    public Dieta getDietaAleatoria(Meta.Tipo tipo) {
        List<Dieta> dietas = InicializadorDados.inicializarDietas();
        List<Dieta> dietasFiltradas = new ArrayList<>();

        // Filtra as dietas pelo tipo (se o tipo for fornecido).
        for (Dieta dieta : dietas) {
            if (dieta.getObjetivo() == tipo) {
                dietasFiltradas.add(dieta);
            }
        }

        //Se não houver dietas, ou dietas com o tipo, retorna uma dieta aleatória
        if (dietasFiltradas.isEmpty()) {
            if (dietas.isEmpty()) {
                return null; //Se não houver nenhuma dieta, retorna null
            } else {
                //Se não houver dietas filtradas, usar a lista toda.
                dietasFiltradas = dietas;
            }
        }

        int indiceAleatorio = random.nextInt(dietasFiltradas.size());
        return dietasFiltradas.get(indiceAleatorio);
    }

    public Refeicao getRefeicaoAleatoria() {
        List<Refeicao> refeicoes = InicializadorDados.inicializarRefeicoes();
        if (refeicoes.isEmpty()) {
            return null;
        }
        int indiceAleatorio = random.nextInt(refeicoes.size());
        return refeicoes.get(indiceAleatorio);
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
                                      LocalDate dataNascimento, Usuario.Sexo sexo, float peso, float altura, String tipo,
                                      double cintura, double biceps, double coxa, double quadril)
            throws UsuarioNaoEncontradoException
    {
        Usuario.TipoUsuario tipouser;
        if(tipo.equals(Usuario.TipoUsuario.ADMINISTRADOR.getDescricao())){
            tipouser = Usuario.TipoUsuario.ADMINISTRADOR;
        } else {
            tipouser = Usuario.TipoUsuario.USUARIO_COMUM;
        }
        System.out.println("Atualizando usuário: " + usuario.getNome() + ", Tipo: " + usuario.getTipo() + " para tipo " + tipouser.getDescricao());
        controladorUsuario.atualizarDados(usuario.getId(), nome, email, senha, dataNascimento, sexo, peso, altura, tipo,
                cintura, biceps, coxa, quadril);
    }

    public void beberAgua(Usuario usuario, int ml) throws UsuarioNaoEncontradoException {
        controladorUsuario.beberAgua(usuario, ml);
    }

    public void zerarAgua(Usuario usuario) throws UsuarioNaoEncontradoException {
        controladorUsuario.zerarAgua(usuario);
    }


    // Métodos para retornar as listas predefinidas para o administrador
    public List<Dieta> getDietasPreDefinidas() {
        return InicializadorDados.inicializarDietas();
    }

    public List<Exercicio> getExerciciosPreDefinidos() {
        return InicializadorDados.inicializarExercicios();
    }

    public List<Meta> getMetasPreDefinidas() {
        return InicializadorDados.inicializarMetas();
    }

    public List<Refeicao> getRefeicoesPreDefinidas() {
        return InicializadorDados.inicializarRefeicoes();
    }

    public List<Treino> getTreinosPreDefinidos() {
        return InicializadorDados.inicializarTreinos();
    }


    private void inicializarDadosPredefinidos() {
        try {
            // Adiciona as dietas diretamente, sem usuário.
            for (Dieta dieta : InicializadorDados.inicializarDietas()) {
                controladorDieta.configurarDieta(dieta, dieta.getNome(), dieta.getObjetivo(), dieta.getCaloriasDiarias(), null); // Usuário nulo!
            }

            // Adiciona os exercícios diretamente, sem usuário.
            for (Exercicio exercicio : InicializadorDados.inicializarExercicios()) {
                controladorExercicio.inicializar(exercicio, exercicio.getNome(), exercicio.getDescricao(), exercicio.getTipo(), exercicio.getTempo(), exercicio.getCaloriasQueimadas(),null); // Sem usuário
            }

            // Adiciona as metas diretamente, sem usuário.
            for (Meta meta : InicializadorDados.inicializarMetas()) {
                controladorMeta.inicializar(meta, meta.getDescricao(), meta.getTipo(), meta.getValorAlvo(), meta.getProgressoAtual(), meta.getDataCriacao(), null); // Sem usuário
            }

            // Adiciona as refeições diretamente, sem usuário.
            for (Refeicao refeicao : InicializadorDados.inicializarRefeicoes()) {
                controladorRefeicao.inicializar(refeicao, refeicao.getNome(), refeicao.getDescricao(), refeicao.getMacronutrientes(), null); // Sem usuário
            }

            // Adiciona os treinos diretamente, sem usuário.
            for (Treino treino : InicializadorDados.inicializarTreinos()) {
                controladorTreino.inicializar(treino, treino.getNome(), treino.getTipoDeTreino(), treino.getDuracao(), treino.getNivelDeDificuldade(), null); // Sem usuário
            }

        } catch (Exception e) {
            System.err.println("Erro ao inicializar os dados na Fachada: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
