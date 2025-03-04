package com.example.queimacaloria.dados;

import com.example.queimacaloria.excecoes.RefeicaoNaoEncontradaException;
import com.example.queimacaloria.interfaces.IRepositorioRefeicoes;
import com.example.queimacaloria.negocio.Refeicao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RepositorioRefeicoesArray implements IRepositorioRefeicoes {

    private static RepositorioRefeicoesArray instanciaUnica;

    // Construtor privado para o padrão Singleton.
    private RepositorioRefeicoesArray() {
    }

    // Retorna a instância única do repositório (Singleton).
    public static RepositorioRefeicoesArray getInstanciaUnica() {
        if (instanciaUnica == null) {
            instanciaUnica = new RepositorioRefeicoesArray();
        }
        return instanciaUnica;
    }

    @Override
    public void adicionar(Refeicao refeicao) throws RefeicaoNaoEncontradaException {
        String sql = "INSERT INTO refeicoes (id, nome, descricao, calorias) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, refeicao.getId().toString());
            preparedStatement.setString(2, refeicao.getNome());
            preparedStatement.setString(3, refeicao.getDescricao());
            preparedStatement.setInt(4, refeicao.getCalorias());

            preparedStatement.executeUpdate();
            System.out.println("Refeição adicionada ao banco de dados.");

        } catch (SQLException e) {
            System.err.println("Erro ao adicionar refeição!");
            e.printStackTrace();
        }
    }

    @Override
    public void salvar(Refeicao refeicao) throws RefeicaoNaoEncontradaException {
        String sql = "UPDATE refeicoes SET nome = ?, descricao = ?, calorias = ? WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, refeicao.getNome());
            preparedStatement.setString(2, refeicao.getDescricao());
            preparedStatement.setInt(3, refeicao.getCalorias());
            preparedStatement.setString(4, refeicao.getId().toString());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new RefeicaoNaoEncontradaException("Refeição com ID " + refeicao.getId() + " não encontrada.");
            }

            System.out.println("Refeição atualizada no banco de dados.");

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar refeição!");
            e.printStackTrace();
        }
    }

    @Override
    public void remover(UUID id) throws RefeicaoNaoEncontradaException {
        String sql = "DELETE FROM refeicoes WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, id.toString());

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new RefeicaoNaoEncontradaException("Refeição com ID " + id + " não encontrada.");
            }

            System.out.println("Refeição removida do banco de dados.");

        } catch (SQLException e) {
            System.err.println("Erro ao remover refeição!");
            e.printStackTrace();
        }
    }

    @Override
    public Refeicao buscar(UUID id) throws RefeicaoNaoEncontradaException {
        String sql = "SELECT * FROM refeicoes WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, id.toString());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Refeicao refeicao = new Refeicao();
                    refeicao.setId(UUID.fromString(resultSet.getString("id")));
                    refeicao.setNome(resultSet.getString("nome"));
                    refeicao.setDescricao(resultSet.getString("descricao"));
                    refeicao.setCalorias(resultSet.getInt("calorias"));
                    return refeicao;
                } else {
                    throw new RefeicaoNaoEncontradaException("Refeição com ID " + id + " não encontrada.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar refeição!");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Refeicao> getAll() {
        List<Refeicao> lista = new ArrayList<>();
        String sql = "SELECT * FROM refeicoes";
        try (Connection connection = DatabaseConnector.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Refeicao refeicao = new Refeicao();
                refeicao.setId(UUID.fromString(resultSet.getString("id")));
                refeicao.setNome(resultSet.getString("nome"));
                refeicao.setDescricao(resultSet.getString("descricao"));
                refeicao.setCalorias(resultSet.getInt("calorias"));
                lista.add(refeicao);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar refeições!");
            e.printStackTrace();
        }
        return lista;
    }
}