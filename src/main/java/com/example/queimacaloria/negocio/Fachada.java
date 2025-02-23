package com.example.queimacaloria.negocio;

import com.example.queimacaloria.controllers.MainController;
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
    private MainController mainController; // Adicionado

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

    // Método para definir o MainController
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }


    public Usuario cadastrarUsuario(String nome, String email, String senha, LocalDate dataNascimento,
                                    Usuario.Sexo sexo, float peso, float altura) {
        //Valida os dados do novo usuário antes de criar
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
        // Verifica se o e-mail já existe
        List<Usuario> usuarios = controladorUsuario.listarUsuarios();
        for (Usuario user : usuarios) {
            if (user.getEmail().equals(email)) {
                throw new IllegalArgumentException("Email já cadastrado.");
            }
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(nome);
        novoUsuario.setEmail(email);
        novoUsuario.setSenha(senha);
        novoUsuario.setDataNascimento(dataNascimento);
        novoUsuario.setSexo(sexo);
        novoUsuario.setPeso(peso);
        novoUsuario.setAltura(altura);

        // Agora sim, chama o método do CONTROLADOR.
        controladorUsuario.cadastrarUsuario(novoUsuario);  // CORRIGIDO!
        return novoUsuario; // Retorna o usuário criado
    }

    // Métodos de Usuário
    public void atualizarDadosUsuario(Usuario usuario, String nome, String email, String senha,
                                      LocalDate dataNascimento,
                                      Usuario.Sexo sexo, float peso, float altura) throws UsuarioNaoEncontradoException
    {
        // Agora sim, chama o método do CONTROLADOR, passando o ID.
        controladorUsuario.atualizarDados(usuario.getId(), nome, email, senha, dataNascimento, sexo, peso, altura); // CORRIGIDO!
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


    public int calcularIdadeUsuario(Usuario usuario) {
        return controladorUsuario.getIdade(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return controladorUsuario.listarUsuarios();
    }

    // Métodos de Dieta (Adaptados para a nova lógica)
    public void configurarDieta(Dieta dieta, String nome, Dieta.ObjetivoDieta objetivo, int caloriasDiarias,
                                Usuario usuario) throws DietaNaoEncontradaException {
        controladorDieta.configurarDieta(dieta, nome, objetivo, caloriasDiarias, usuario);
    }

    // Métodos de Exercício (Mantidos)
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

    public void removerExercicio(UUID id) throws ExercicioNaoEncontradoException {
        controladorExercicio.remover(id);
    }

    // Métodos de Meta (Mantidos)
    public void configurarMeta(Meta meta, String descricao, Meta.Tipo tipo, double valorAlvo, double progressoAtual,
                               LocalDate dataConclusao) throws MetaNaoEncontradaException {
        controladorMeta.inicializar(meta, descricao, tipo, valorAlvo, progressoAtual, dataConclusao);

        // MUITO IMPORTANTE: Adiciona a meta ao usuário logado, se houver
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

    // Métodos de Refeição (Mantidos)
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

    public void removerTreino(UUID id) throws TreinoNaoEncontradoException {
        controladorTreino.remover(id);
    }

    public void removerRefeicao(UUID id) throws RefeicaoNaoEncontradaException {
        controladorRefeicao.remover(id);
    }
    public void removerDieta(UUID id) throws DietaNaoEncontradaException{
        controladorDieta.removerDieta(id);
    }
    public void removerMeta(UUID id) throws MetaNaoEncontradaException {
        controladorMeta.remover(id);
    }

    // Métodos de Treino (Mantidos)
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

    // Métodos de Listagem (Mantidos)
    public List<Dieta> listarDietas() {
        return controladorDieta.listarDietas();
    }

    public List<Exercicio> listarExercicios() {
        return controladorExercicio.listarExercicios();
    }

    public List<Meta> listarMetas() {
        return controladorMeta.listarMetas();
    }

    public void setDietaAtiva(Usuario usuario, Dieta dieta) throws UsuarioNaoEncontradoException {
        controladorUsuario.setDietaAtiva(usuario, dieta);
    }

    public Dieta getDietaAtiva(Usuario usuario) throws  UsuarioNaoEncontradoException{
        return controladorUsuario.getDietaAtiva(usuario);
    }

    public List<Refeicao> listarRefeicoes() {
        return controladorRefeicao.listarRefeicoes();
    }

    public List<Treino> listarTreinos() {
        return controladorTreino.listarTreinos();
    }
}