package com.example.queimacaloria.dados;

import com.example.queimacaloria.excecoes.TreinoNaoEncontradoException;
import com.example.queimacaloria.interfaces.IRepositorioTreinos;
import com.example.queimacaloria.negocio.Exercicio;
import com.example.queimacaloria.negocio.Treino;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RepositorioTreinosArray implements IRepositorioTreinos {

    private static RepositorioTreinosArray instanciaUnica;

    // Construtor privado para o padrão Singleton.
    public RepositorioTreinosArray() {
    }

    // Retorna a instância única do repositório (Singleton).
    public static RepositorioTreinosArray getInstanciaUnica() {
        if (instanciaUnica == null) {
            instanciaUnica = new RepositorioTreinosArray();
        }
        return instanciaUnica;
    }

    @Override
    public void adicionar(Treino treino) throws TreinoNaoEncontradoException {
        String sql = "INSERT INTO treinos (id, nome, tipo, duracao, nivelDeDificuldade) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, treino.getId().toString());
            preparedStatement.setString(2, treino.getNome());
            preparedStatement.setString(3, treino.getTipoDeTreino().toString());
            preparedStatement.setInt(4, treino.getDuracao());
            preparedStatement.setInt(5, treino.getNivelDeDificuldade());

            preparedStatement.executeUpdate();
            System.out.println("Treino adicionado ao banco de dados.");

        } catch (SQLException e) {
            System.err.println("Erro ao adicionar treino!");
            e.printStackTrace();
        }
    }

    @Override
    public void salvar(Treino treino) throws TreinoNaoEncontradoException {
        String sql = "UPDATE treinos SET nome = ?, tipo = ?, duracao = ?, nivelDeDificuldade = ? WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, treino.getNome());
            preparedStatement.setString(2, treino.getTipoDeTreino().toString());
            preparedStatement.setInt(3, treino.getDuracao());
            preparedStatement.setInt(4, treino.getNivelDeDificuldade());
            preparedStatement.setString(5, treino.getId().toString());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new TreinoNaoEncontradoException("Treino com ID " + treino.getId() + " não encontrado.");
            }

            System.out.println("Treino atualizado no banco de dados.");

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar treino!");
            e.printStackTrace();
        }
    }

    @Override
    public void remover(UUID id) throws TreinoNaoEncontradoException {
        String sql = "DELETE FROM treinos WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, id.toString());

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new TreinoNaoEncontradoException("Treino com ID " + id + " não encontrado.");
            }

            System.out.println("Treino removido do banco de dados.");

        } catch (SQLException e) {
            System.err.println("Erro ao remover treino!");
            e.printStackTrace();
        }
    }

    @Override
    public Treino buscar(UUID id) throws TreinoNaoEncontradoException {
        String sql = "SELECT * FROM treinos WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, id.toString());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Treino treino = new Treino();
                    treino.setId(UUID.fromString(resultSet.getString("id")));
                    treino.setNome(resultSet.getString("nome"));
                    String tipoStr = resultSet.getString("tipo");
                    Exercicio.TipoExercicio tipo = null;
                    if(tipoStr != null){
                        tipo = Exercicio.TipoExercicio.valueOf(tipoStr);
                        treino.setTipoDeTreino(tipo);
                    }
                    treino.setDuracao(resultSet.getInt("duracao"));
                    treino.setNivelDeDificuldade(resultSet.getInt("nivelDeDificuldade"));
                    return treino;
                } else {
                    throw new TreinoNaoEncontradoException("Treino com ID " + id + " não encontrado.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar treino!");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Treino> getAll() {
        List<Treino> lista = new ArrayList<>();
        String sql = "SELECT * FROM treinos";
        try (Connection connection = DatabaseConnector.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Treino treino = new Treino();
                treino.setId(UUID.fromString(resultSet.getString("id")));
                treino.setNome(resultSet.getString("nome"));
                String tipoStr = resultSet.getString("tipo");
                Exercicio.TipoExercicio tipo = null;
                if(tipoStr != null){
                    tipo = Exercicio.TipoExercicio.valueOf(tipoStr);
                    treino.setTipoDeTreino(tipo);
                }
                treino.setDuracao(resultSet.getInt("duracao"));
                treino.setNivelDeDificuldade(resultSet.getInt("nivelDeDificuldade"));
                lista.add(treino);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar treinos!");
            e.printStackTrace();
        }
        return lista;
    }
}