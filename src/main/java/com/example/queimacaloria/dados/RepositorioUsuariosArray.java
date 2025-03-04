package com.example.queimacaloria.dados;

import com.example.queimacaloria.interfaces.IRepositorioUsuarios;
import com.example.queimacaloria.negocio.Usuario;
import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RepositorioUsuariosArray implements IRepositorioUsuarios {

    private static RepositorioUsuariosArray instanciaUnica;

    private RepositorioUsuariosArray() {
    }

    public static RepositorioUsuariosArray getInstanciaUnica() {
        if (instanciaUnica == null) {
            instanciaUnica = new RepositorioUsuariosArray();
        }
        return instanciaUnica;
    }

    @Override
    public void adicionar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (id, nome, email, senha, dataNascimento, sexo, peso, altura, tipo, aguaConsumida, pontuacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, usuario.getId().toString());
            preparedStatement.setString(2, usuario.getNome());
            preparedStatement.setString(3, usuario.getEmail());
            preparedStatement.setString(4, usuario.getSenha());
            if (usuario.getDataNascimento() != null) {
                preparedStatement.setString(5, usuario.getDataNascimento().toString());
            } else {
                preparedStatement.setString(5, null);
            }
            if (usuario.getSexo() != null) {
                preparedStatement.setString(6, usuario.getSexo().toString());
            } else {
                preparedStatement.setString(6, null);
            }
            preparedStatement.setFloat(7, usuario.getPeso());
            preparedStatement.setFloat(8, usuario.getAltura());
            preparedStatement.setString(9, usuario.getTipo());
            preparedStatement.setInt(10, usuario.getAguaConsumida());
            preparedStatement.setInt(11, usuario.getPontuacao());

            preparedStatement.executeUpdate();
            System.out.println("Usuário adicionado ao banco de dados.");

        } catch (SQLException e) {
            System.err.println("Erro ao adicionar usuário!");
            e.printStackTrace();
        }
    }

    @Override
    public void salvar(Usuario usuario) throws UsuarioNaoEncontradoException {
        String sql = "UPDATE usuarios SET nome = ?, email = ?, senha = ?, dataNascimento = ?, sexo = ?, peso = ?, altura = ?, tipo = ?, aguaConsumida = ?, pontuacao = ? WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, usuario.getNome());
            preparedStatement.setString(2, usuario.getEmail());
            preparedStatement.setString(3, usuario.getSenha());
            if (usuario.getDataNascimento() != null) {
                preparedStatement.setString(4, usuario.getDataNascimento().toString());
            } else {
                preparedStatement.setString(4, null);
            }
            if (usuario.getSexo() != null) {
                preparedStatement.setString(5, usuario.getSexo().toString());
            } else {
                preparedStatement.setString(5, null);
            }
            preparedStatement.setFloat(6, usuario.getPeso());
            preparedStatement.setFloat(7, usuario.getAltura());
            preparedStatement.setString(8, usuario.getTipo());
            preparedStatement.setInt(9, usuario.getAguaConsumida());
            preparedStatement.setInt(10, usuario.getPontuacao());
            preparedStatement.setString(11, usuario.getId().toString());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new UsuarioNaoEncontradoException("Usuário com ID " + usuario.getId() + " não encontrado.");
            }

            System.out.println("Usuário atualizado no banco de dados.");

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar usuário!");
            e.printStackTrace();
        }
    }

    @Override
    public void remover(UUID id) throws UsuarioNaoEncontradoException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, id.toString());

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new UsuarioNaoEncontradoException("Usuário com ID " + id + " não encontrado.");
            }

            System.out.println("Usuário removido do banco de dados.");

        } catch (SQLException e) {
            System.err.println("Erro ao remover usuário!");
            e.printStackTrace();
        }
    }

    @Override
    public Usuario buscar(UUID id) throws UsuarioNaoEncontradoException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, id.toString());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(UUID.fromString(resultSet.getString("id")));
                    usuario.setNome(resultSet.getString("nome"));
                    usuario.setEmail(resultSet.getString("email"));
                    usuario.setSenha(resultSet.getString("senha"));
                    String dataNascimentoStr = resultSet.getString("dataNascimento");
                    if (dataNascimentoStr != null) {
                        usuario.setDataNascimento(LocalDate.parse(dataNascimentoStr));
                    }
                    String sexoStr = resultSet.getString("sexo");
                    if (sexoStr != null) {
                        usuario.setSexo(Usuario.Sexo.valueOf(sexoStr));
                    }
                    usuario.setPeso(resultSet.getFloat("peso"));
                    usuario.setAltura(resultSet.getFloat("altura"));
                    usuario.setTipo(resultSet.getString("tipo"));
                    usuario.setAguaConsumida(resultSet.getInt("aguaConsumida"));
                    usuario.setPontuacao(resultSet.getInt("pontuacao"));

                    return usuario;
                } else {
                    throw new UsuarioNaoEncontradoException("Usuário com ID " + id + " não encontrado.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário!");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Usuario> getAll() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (Connection connection = DatabaseConnector.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(UUID.fromString(resultSet.getString("id")));
                usuario.setNome(resultSet.getString("nome"));
                usuario.setEmail(resultSet.getString("email"));
                usuario.setSenha(resultSet.getString("senha"));
                String dataNascimentoStr = resultSet.getString("dataNascimento");
                if (dataNascimentoStr != null) {
                    usuario.setDataNascimento(LocalDate.parse(dataNascimentoStr));
                }
                String sexoStr = resultSet.getString("sexo");
                if (sexoStr != null) {
                    usuario.setSexo(Usuario.Sexo.valueOf(sexoStr));
                }
                usuario.setPeso(resultSet.getFloat("peso"));
                usuario.setAltura(resultSet.getFloat("altura"));
                usuario.setTipo(resultSet.getString("tipo"));
                usuario.setAguaConsumida(resultSet.getInt("aguaConsumida"));
                usuario.setPontuacao(resultSet.getInt("pontuacao"));

                lista.add(usuario);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar usuários!");
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<Usuario> getAll(Usuario.TipoUsuario tipo) {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE tipo = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, tipo.getDescricao());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(UUID.fromString(resultSet.getString("id")));
                    usuario.setNome(resultSet.getString("nome"));
                    usuario.setEmail(resultSet.getString("email"));
                    usuario.setSenha(resultSet.getString("senha"));
                    String dataNascimentoStr = resultSet.getString("dataNascimento");
                    if (dataNascimentoStr != null) {
                        usuario.setDataNascimento(LocalDate.parse(dataNascimentoStr));
                    }
                    String sexoStr = resultSet.getString("sexo");
                    if (sexoStr != null) {
                        usuario.setSexo(Usuario.Sexo.valueOf(sexoStr));
                    }
                    usuario.setPeso(resultSet.getFloat("peso"));
                    usuario.setAltura(resultSet.getFloat("altura"));
                    usuario.setTipo(resultSet.getString("tipo"));
                    usuario.setAguaConsumida(resultSet.getInt("aguaConsumida"));
                    usuario.setPontuacao(resultSet.getInt("pontuacao"));

                    lista.add(usuario);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar usuários por tipo!");
            e.printStackTrace();
        }
        return lista;
    }
}