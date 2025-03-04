package com.example.queimacaloria.dados;

import com.example.queimacaloria.excecoes.MetaNaoEncontradaException;
import com.example.queimacaloria.interfaces.IRepositorioMetas;
import com.example.queimacaloria.negocio.Meta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RepositorioMetasArray implements IRepositorioMetas {

    private static RepositorioMetasArray instanciaUnica;

    // Construtor privado para o padrão Singleton.
    private RepositorioMetasArray() {
    }

    // Retorna a instância única do repositório (Singleton).
    public static RepositorioMetasArray getInstanciaUnica() {
        if (instanciaUnica == null) {
            instanciaUnica = new RepositorioMetasArray();
        }
        return instanciaUnica;
    }

    @Override
    public void adicionar(Meta meta) throws MetaNaoEncontradaException {
        String sql = "INSERT INTO metas (id, descricao, tipo, valorAlvo, progressoAtual, dataCriacao, dataConclusao) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, meta.getId().toString());
            preparedStatement.setString(2, meta.getDescricao());
            preparedStatement.setString(3, meta.getTipo().name()); // Salva o nome da constante do enum
            preparedStatement.setDouble(4, meta.getValorAlvo());
            preparedStatement.setDouble(5, meta.getProgressoAtual());
            if (meta.getDataCriacao() != null) {
                preparedStatement.setString(6, meta.getDataCriacao().toString());
            } else {
                preparedStatement.setString(6, null);
            }
            if (meta.getDataConclusao() != null) {
                preparedStatement.setString(7, meta.getDataConclusao().toString());
            } else {
                preparedStatement.setString(7, null);
            }

            preparedStatement.executeUpdate();
            System.out.println("Meta adicionada ao banco de dados.");

        } catch (SQLException e) {
            System.err.println("Erro ao adicionar meta!");
            e.printStackTrace();
        }
    }

    @Override
    public void salvar(Meta meta) throws MetaNaoEncontradaException {
        String sql = "UPDATE metas SET descricao = ?, tipo = ?, valorAlvo = ?, progressoAtual = ?, dataCriacao = ?, dataConclusao = ? WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, meta.getDescricao());
            preparedStatement.setString(2, meta.getTipo().name());  // Salva o nome da constante do enum
            preparedStatement.setDouble(3, meta.getValorAlvo());
            preparedStatement.setDouble(4, meta.getProgressoAtual());
            if (meta.getDataCriacao() != null) {
                preparedStatement.setString(5, meta.getDataCriacao().toString());
            } else {
                preparedStatement.setString(5, null);
            }
            if (meta.getDataConclusao() != null) {
                preparedStatement.setString(6, meta.getDataConclusao().toString());
            } else {
                preparedStatement.setString(6, null);
            }
            preparedStatement.setString(7, meta.getId().toString());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new MetaNaoEncontradaException("Meta com ID " + meta.getId() + " não encontrada.");
            }

            System.out.println("Meta atualizada no banco de dados.");

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar meta!");
            e.printStackTrace();
        }
    }

    @Override
    public void remover(UUID id) throws MetaNaoEncontradaException {
        String sql = "DELETE FROM metas WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, id.toString());

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new MetaNaoEncontradaException("Meta com ID " + id + " não encontrada.");
            }

            System.out.println("Meta removida do banco de dados.");

        } catch (SQLException e) {
            System.err.println("Erro ao remover meta!");
            e.printStackTrace();
        }
    }

    @Override
    public Meta buscar(UUID id) throws MetaNaoEncontradaException {
        String sql = "SELECT * FROM metas WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, id.toString());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Meta meta = new Meta();
                    meta.setId(UUID.fromString(resultSet.getString("id")));
                    meta.setDescricao(resultSet.getString("descricao"));
                    String tipoStr = resultSet.getString("tipo");
                    if (tipoStr != null) {
                        try {
                            Meta.Tipo tipo = Meta.Tipo.valueOf(tipoStr);  // Tenta converter usando o nome da constante
                            meta.setTipo(tipo);
                        }
                        catch (IllegalArgumentException e) {
                            System.err.println("Erro ao converter tipo de meta: " + tipoStr);
                            e.printStackTrace();
                        }
                    }
                    meta.setValorAlvo(resultSet.getDouble("valorAlvo"));
                    meta.setProgressoAtual(resultSet.getDouble("progressoAtual"));
                    String dataCriacaoStr = resultSet.getString("dataCriacao");
                    if (dataCriacaoStr != null) {
                        meta.setDataCriacao(LocalDate.parse(dataCriacaoStr));
                    }
                    String dataConclusaoStr = resultSet.getString("dataConclusao");
                    if (dataConclusaoStr != null) {
                        meta.setDataConclusao(LocalDate.parse(dataConclusaoStr));
                    }
                    return meta;
                } else {
                    throw new MetaNaoEncontradaException("Meta com ID " + id + " não encontrada.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar meta!");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Meta> getAll() {
        List<Meta> lista = new ArrayList<>();
        String sql = "SELECT * FROM metas";
        try (Connection connection = DatabaseConnector.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Meta meta = new Meta();
                meta.setId(UUID.fromString(resultSet.getString("id")));
                meta.setDescricao(resultSet.getString("descricao"));
                String tipoStr = resultSet.getString("tipo");
                if (tipoStr != null) {
                    try {
                        Meta.Tipo tipo = Meta.Tipo.valueOf(tipoStr);  // Tenta converter usando o nome da constante
                        meta.setTipo(tipo);
                    }
                    catch (IllegalArgumentException e) {
                        System.err.println("Erro ao converter tipo de meta: " + tipoStr);
                    }
                }
                meta.setValorAlvo(resultSet.getDouble("valorAlvo"));
                meta.setProgressoAtual(resultSet.getDouble("progressoAtual"));
                String dataCriacaoStr = resultSet.getString("dataCriacao");
                if (dataCriacaoStr != null) {
                    meta.setDataCriacao(LocalDate.parse(dataCriacaoStr));
                }
                String dataConclusaoStr = resultSet.getString("dataConclusao");
                if (dataConclusaoStr != null) {
                    meta.setDataConclusao(LocalDate.parse(dataConclusaoStr));
                }

                lista.add(meta);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar metas!");
            e.printStackTrace();
        }
        return lista;
    }
}