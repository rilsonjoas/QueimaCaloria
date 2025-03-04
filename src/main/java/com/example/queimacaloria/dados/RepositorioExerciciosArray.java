package com.example.queimacaloria.dados;

import com.example.queimacaloria.excecoes.ExercicioNaoEncontradoException;
import com.example.queimacaloria.interfaces.IRepositorioExercicios;
import com.example.queimacaloria.negocio.Exercicio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RepositorioExerciciosArray implements IRepositorioExercicios {

    private static RepositorioExerciciosArray instanciaUnica;

    // Construtor privado para o padrão Singleton.
    private RepositorioExerciciosArray() {
    }

    // Retorna a instância única do repositório (Singleton).
    public static RepositorioExerciciosArray getInstanciaUnica() {
        if (instanciaUnica == null) {
            instanciaUnica = new RepositorioExerciciosArray();
        }
        return instanciaUnica;
    }

    @Override
    public void adicionar(Exercicio exercicio) throws ExercicioNaoEncontradoException {
        String sql = "INSERT INTO exercicios (id, nome, descricao, tipo, tempo, caloriasQueimadas) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, exercicio.getId().toString());
            preparedStatement.setString(2, exercicio.getNome());
            preparedStatement.setString(3, exercicio.getDescricao());
            preparedStatement.setString(4, exercicio.getTipo().toString());
            preparedStatement.setInt(5, exercicio.getTempo());
            preparedStatement.setDouble(6, exercicio.getCaloriasQueimadas());

            preparedStatement.executeUpdate();
            System.out.println("Exercício adicionado ao banco de dados.");

        } catch (SQLException e) {
            System.err.println("Erro ao adicionar exercício!");
            e.printStackTrace();
        }
    }

    @Override
    public void salvar(Exercicio exercicio) throws ExercicioNaoEncontradoException {
        String sql = "UPDATE exercicios SET nome = ?, descricao = ?, tipo = ?, tempo = ?, caloriasQueimadas = ? WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, exercicio.getNome());
            preparedStatement.setString(2, exercicio.getDescricao());
            preparedStatement.setString(3, exercicio.getTipo().toString());
            preparedStatement.setInt(4, exercicio.getTempo());
            preparedStatement.setDouble(5, exercicio.getCaloriasQueimadas());
            preparedStatement.setString(6, exercicio.getId().toString());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new ExercicioNaoEncontradoException("Exercício com ID " + exercicio.getId() + " não encontrado.");
            }

            System.out.println("Exercício atualizado no banco de dados.");

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar exercício!");
            e.printStackTrace();
        }
    }

    @Override
    public void remover(UUID id) throws ExercicioNaoEncontradoException {
        String sql = "DELETE FROM exercicios WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, id.toString());

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new ExercicioNaoEncontradoException("Exercício com ID " + id + " não encontrado.");
            }

            System.out.println("Exercício removido do banco de dados.");

        } catch (SQLException e) {
            System.err.println("Erro ao remover exercício!");
            e.printStackTrace();
        }
    }

    @Override
    public Exercicio buscar(UUID id) throws ExercicioNaoEncontradoException {
        String sql = "SELECT * FROM exercicios WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, id.toString());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Exercicio exercicio = new Exercicio();
                    exercicio.setId(UUID.fromString(resultSet.getString("id")));
                    exercicio.setNome(resultSet.getString("nome"));
                    exercicio.setDescricao(resultSet.getString("descricao"));
                    String tipoStr = resultSet.getString("tipo");
                    Exercicio.TipoExercicio tipo = null;
                    if(tipoStr != null){
                        tipo = Exercicio.TipoExercicio.valueOf(tipoStr);
                        exercicio.setTipo(tipo);
                    }
                    exercicio.setTempo(resultSet.getInt("tempo"));
                    exercicio.setCaloriasQueimadas(resultSet.getDouble("caloriasQueimadas"));
                    return exercicio;
                } else {
                    throw new ExercicioNaoEncontradoException("Exercício com ID " + id + " não encontrado.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar exercício!");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Exercicio> getAll() {
        List<Exercicio> lista = new ArrayList<>();
        String sql = "SELECT * FROM exercicios";
        try (Connection connection = DatabaseConnector.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Exercicio exercicio = new Exercicio();
                exercicio.setId(UUID.fromString(resultSet.getString("id")));
                exercicio.setNome(resultSet.getString("nome"));
                exercicio.setDescricao(resultSet.getString("descricao"));
                String tipoStr = resultSet.getString("tipo");
                Exercicio.TipoExercicio tipo = null;
                if(tipoStr != null){
                    tipo = Exercicio.TipoExercicio.valueOf(tipoStr);
                    exercicio.setTipo(tipo);
                }
                exercicio.setTempo(resultSet.getInt("tempo"));
                exercicio.setCaloriasQueimadas(resultSet.getDouble("caloriasQueimadas"));
                lista.add(exercicio);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar exercícios!");
            e.printStackTrace();
        }
        return lista;
    }
}