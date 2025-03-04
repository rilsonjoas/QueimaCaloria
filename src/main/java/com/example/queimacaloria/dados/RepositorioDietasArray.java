package com.example.queimacaloria.dados;

import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;
import com.example.queimacaloria.interfaces.IRepositorioDietas;
import com.example.queimacaloria.negocio.Dieta;
import com.example.queimacaloria.negocio.Meta;
import com.example.queimacaloria.negocio.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RepositorioDietasArray implements IRepositorioDietas {

    private static RepositorioDietasArray instanciaUnica;

    // Construtor privado para o padrão Singleton.
    private RepositorioDietasArray() {
    }

    // Retorna a instância única do repositório (Singleton).
    public static RepositorioDietasArray getInstanciaUnica() {
        if (instanciaUnica == null) {
            instanciaUnica = new RepositorioDietasArray();
        }
        return instanciaUnica;
    }

    @Override
    public void adicionar(Dieta dieta) throws DietaNaoEncontradaException {
        String sql = "INSERT INTO dietas (id, nome, objetivo, caloriasDiarias, usuarioId) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, dieta.getId().toString());
            preparedStatement.setString(2, dieta.getNome());
            preparedStatement.setString(3, dieta.getObjetivo().name()); // Salva o nome da constante do enum
            preparedStatement.setInt(4, dieta.getCaloriasDiarias());
            if (dieta.getUsuario() != null) {
                preparedStatement.setString(5, dieta.getUsuario().getId().toString());
            } else {
                preparedStatement.setString(5, null);
            }

            preparedStatement.executeUpdate();
            System.out.println("Dieta adicionada ao banco de dados.");

        } catch (SQLException e) {
            System.err.println("Erro ao adicionar dieta!");
            e.printStackTrace();
        }
    }

    @Override
    public void salvar(Dieta dieta) throws DietaNaoEncontradaException {
        String sql = "UPDATE dietas SET nome = ?, objetivo = ?, caloriasDiarias = ?, usuarioId = ? WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, dieta.getNome());
            preparedStatement.setString(2, dieta.getObjetivo().name()); // Salva o nome da constante do enum
            preparedStatement.setInt(3, dieta.getCaloriasDiarias());
            if (dieta.getUsuario() != null) {
                preparedStatement.setString(4, dieta.getUsuario().getId().toString());
            } else {
                preparedStatement.setString(4, null);
            }
            preparedStatement.setString(5, dieta.getId().toString());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DietaNaoEncontradaException("Dieta com ID " + dieta.getId() + " não encontrada.");
            }

            System.out.println("Dieta atualizada no banco de dados.");

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar dieta!");
            e.printStackTrace();
        }
    }

    @Override
    public void remover(UUID id) throws DietaNaoEncontradaException {
        String sql = "DELETE FROM dietas WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, id.toString());

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new DietaNaoEncontradaException("Dieta com ID " + id + " não encontrada.");
            }

            System.out.println("Dieta removida do banco de dados.");

        } catch (SQLException e) {
            System.err.println("Erro ao remover dieta!");
            e.printStackTrace();
        }
    }

    @Override
    public Dieta buscar(UUID id) throws DietaNaoEncontradaException {
        String sql = "SELECT * FROM dietas WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, id.toString());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Dieta dieta = new Dieta();
                    dieta.setId(UUID.fromString(resultSet.getString("id")));
                    dieta.setNome(resultSet.getString("nome"));
                    String objetivoStr = resultSet.getString("objetivo");
                    Meta.Tipo objetivo = null;
                    if(objetivoStr != null){
                        try {
                            objetivo = Meta.Tipo.valueOf(objetivoStr); // Tenta converter usando o nome da constante
                        } catch (IllegalArgumentException e) {
                            System.err.println("Valor inválido para objetivo no banco de dados: " + objetivoStr);
                            // Trate o erro adequadamente (por exemplo, defina um valor padrão ou lance uma exceção)
                        }
                        dieta.setObjetivo(objetivo);
                    }
                    dieta.setCaloriasDiarias(resultSet.getInt("caloriasDiarias"));
                    String usuarioIdStr = resultSet.getString("usuarioId");
                    Usuario usuario = null;
                    if (usuarioIdStr != null) {
                        usuario = new Usuario();
                        usuario.setId(UUID.fromString(usuarioIdStr));
                        dieta.setUsuario(usuario);
                    }
                    return dieta;
                } else {
                    throw new DietaNaoEncontradaException("Dieta com ID " + id + " não encontrada.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar dieta!");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Dieta> getAll() {
        List<Dieta> lista = new ArrayList<>();
        String sql = "SELECT * FROM dietas";
        try (Connection connection = DatabaseConnector.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Dieta dieta = new Dieta();
                dieta.setId(UUID.fromString(resultSet.getString("id")));
                dieta.setNome(resultSet.getString("nome"));
                String objetivoStr = resultSet.getString("objetivo");
                Meta.Tipo objetivo = null;
                if(objetivoStr != null){
                    try {
                        objetivo = Meta.Tipo.valueOf(objetivoStr); // Tenta converter usando o nome da constante
                    } catch (IllegalArgumentException e) {
                        System.err.println("Valor inválido para objetivo no banco de dados: " + objetivoStr);
                        // Trate o erro adequadamente (por exemplo, defina um valor padrão ou lance uma exceção)
                        continue; // Pula esta iteração se o valor for inválido
                    }
                    dieta.setObjetivo(objetivo);
                }
                dieta.setCaloriasDiarias(resultSet.getInt("caloriasDiarias"));
                String usuarioIdStr = resultSet.getString("usuarioId");
                Usuario usuario = null;
                if (usuarioIdStr != null) {
                    usuario = new Usuario();
                    usuario.setId(UUID.fromString(usuarioIdStr));
                    dieta.setUsuario(usuario);
                }

                lista.add(dieta);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar dietas!");
            e.printStackTrace();
        }
        return lista;
    }
}