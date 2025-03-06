package com.example.queimacaloria.negocio;

import com.example.queimacaloria.dados.RepositorioUsuariosArray;
import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;

public class ControladorUsuario {

    private RepositorioUsuariosArray repositorio;

    // Construtor, inicializa o repositório.
    public ControladorUsuario() {
        this.repositorio = RepositorioUsuariosArray.getInstanciaUnica();
    }

    // Cadastra um novo usuário no repositório.
    public void cadastrarUsuario(Usuario novoUsuario) {
        List<Usuario> usuarios = repositorio.getAll();
        for (Usuario user : usuarios) {
            if (user.getEmail().equals(novoUsuario.getEmail())) {
                throw new IllegalArgumentException("Email já cadastrado.");
            }
        }

        repositorio.adicionar(novoUsuario);
    }

    // Atualiza os dados de um usuário existente.
    public void atualizarDados(UUID usuarioId, String nome, String email, String senha, LocalDate dataNascimento,
                               Usuario.Sexo sexo, float peso, float altura, String tipo,
                               double cintura, double biceps, double coxa, double quadril) // Novos parâmetros
            throws UsuarioNaoEncontradoException
    {
        Usuario usuario = repositorio.buscar(usuarioId);
        if (usuario == null) {
            throw new UsuarioNaoEncontradoException("Usuário com ID " + usuarioId + " não encontrado.");
        }

        // Atualiza os campos existentes, verificando nulos e vazios
        if (nome != null && !nome.isEmpty()) {
            usuario.setNome(nome);
        }
        if (email != null && !email.isEmpty()) {
            usuario.setEmail(email);
        }
        if (senha != null && !senha.isEmpty()) { //  Permite atualizar senha (opcional)
            usuario.setSenha(senha);
        }
        if (dataNascimento != null) {
            usuario.setDataNascimento(dataNascimento);
        }
        if (sexo != null) {
            usuario.setSexo(sexo);
        }
        if (peso > 0) {
            usuario.setPeso(peso);
        }
        if (altura > 0) {
            usuario.setAltura(altura);
        }
        if (tipo != null && !tipo.isEmpty()) {
            usuario.setTipo(tipo);
        }

        //Novos atributos:
        if(cintura > 0) {
            usuario.setCintura(cintura);
        }
        if(biceps > 0) {
            usuario.setBiceps(biceps);
        }
        if(coxa > 0) {
            usuario.setCoxa(coxa);
        }
        if(quadril > 0) {
            usuario.setQuadril(quadril);
        }

        System.out.println("Antes de salvar - Atualizando usuario: " + usuario.getNome() + " para tipo " + usuario.getTipo());
        repositorio.salvar(usuario);
        System.out.println("Depois de salvar - Atualizando usuario: " + usuario.getNome() + " para tipo " + usuario.getTipo());
    }

    // Busca um usuário pelo ID.
    public Usuario buscarPorId(UUID id) throws UsuarioNaoEncontradoException{
        return repositorio.buscar(id);
    }

    // Lista todos os usuários do repositório.
    public List<Usuario> listarUsuarios() {
        return repositorio.getAll();
    }

    //Lista todos os usuários por tipo
    public List<Usuario> listarUsuarios(Usuario.TipoUsuario tipo) { //ADICIONADO
        return repositorio.getAll(tipo);
    }

    // Calcula a idade do usuário.
    public int getIdade(Usuario usuario) {
        if (usuario.getDataNascimento() == null) return 0;
        return Period.between(usuario.getDataNascimento(), LocalDate.now()).getYears();
    }

    // Métodos relacionados a Metas
    public void cadastrarMeta(Usuario usuario, Meta meta) throws UsuarioNaoEncontradoException {
        if (meta != null) {
            usuario.getMetas().add(meta);
            repositorio.salvar(usuario);
        }
    }

    // Métodos relacionados a Treinos
    public void adicionarTreino(Usuario usuario, Treino treino) throws UsuarioNaoEncontradoException {
        if (treino != null && !usuario.getTreinos().contains(treino)) {
            usuario.getTreinos().add(treino);
            repositorio.salvar(usuario);
        }
    }

    // Métodos relacionados a Dietas
    public void adicionarDieta(Usuario usuario, Dieta dieta) throws UsuarioNaoEncontradoException {
        if (dieta != null && !usuario.getDietas().contains(dieta)) {
            usuario.getDietas().add(dieta);
            repositorio.salvar(usuario);
        }
    }

    public void setDietaAtiva(Usuario usuario, Dieta dieta) throws UsuarioNaoEncontradoException{
        if(usuario != null && dieta != null){
            usuario.setDietaAtiva(dieta);
            repositorio.salvar(usuario);
        }
    }

    public Dieta getDietaAtiva(Usuario usuario) throws UsuarioNaoEncontradoException {
        if(usuario != null){
            return usuario.getDietaAtiva();
        }
        return null;
    }

    // Métodos relacionados à água
    public void beberAgua(Usuario usuario, int ml) throws UsuarioNaoEncontradoException{
        if(usuario != null){
            usuario.beberAgua(ml);
            repositorio.salvar(usuario);
        }
    }

    public void zerarAgua(Usuario usuario) throws UsuarioNaoEncontradoException {
        if(usuario != null){
            usuario.setAguaConsumida(0);
            repositorio.salvar(usuario);
        }
    }

    // Remover o usuário
    public void remover(UUID id) throws UsuarioNaoEncontradoException {
        repositorio.remover(id);
    }

    public void atualizarTipoDieta(Usuario usuario, Usuario.TipoDieta tipoDieta) throws UsuarioNaoEncontradoException {
        if (usuario != null && tipoDieta != null) {
            usuario.tipoDietaProperty().set(tipoDieta);
            repositorio.salvar(usuario);
        }
    }

    public void atualizarNivelExperiencia(Usuario usuario, Usuario.NivelExperiencia nivel) throws UsuarioNaoEncontradoException {
        if(usuario != null && nivel != null){
            usuario.nivelExperienciaProperty().set(nivel);
            repositorio.salvar(usuario);
        }
    }
}