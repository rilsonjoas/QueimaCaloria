package com.example.queimacaloria.dados;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    private static final String DB_URL = "jdbc:sqlite:youfit.db";  // Nome do seu arquivo de banco de dados
    private static Connection connection;

    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.sqlite.JDBC"); // Carrega o driver JDBC do SQLite
                connection = DriverManager.getConnection(DB_URL);
                System.out.println("Conexão com o banco de dados estabelecida.");
            } catch (ClassNotFoundException e) {
                System.err.println("Driver JDBC do SQLite não encontrado!");
                e.printStackTrace();
                throw new SQLException("Driver JDBC do SQLite não encontrado!", e);
            } catch (SQLException e) {
                System.err.println("Erro ao conectar ao banco de dados!");
                e.printStackTrace();
                throw e;
            }
        }
        return connection;
    }

    public static synchronized void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexão com o banco de dados fechada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar a conexão com o banco de dados!");
            e.printStackTrace();
        } finally {
            connection = null; // Limpa a referência
        }
    }
}