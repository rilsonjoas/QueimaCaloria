package com.example.queimacaloria.dados;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void createTables() {
        try (Connection connection = DatabaseConnector.getConnection();
             Statement statement = connection.createStatement()) {

            // Cria a tabela de Usuários
            String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios (" +
                    "id VARCHAR(36) PRIMARY KEY," +
                    "nome TEXT NOT NULL," +
                    "email TEXT NOT NULL UNIQUE," +
                    "senha TEXT NOT NULL," +
                    "dataNascimento TEXT," +
                    "sexo TEXT," +
                    "peso REAL," +
                    "altura REAL," +
                    "tipo TEXT," +
                    "aguaConsumida INTEGER," +
                    "pontuacao INTEGER" +
                    ")";
            statement.execute(sqlUsuarios);

            // Cria a tabela de Dietas
            String sqlDietas = "CREATE TABLE IF NOT EXISTS dietas (" +
                    "id VARCHAR(36) PRIMARY KEY," +
                    "nome TEXT NOT NULL," +
                    "objetivo TEXT NOT NULL," +
                    "caloriasDiarias INTEGER," +
                    "usuarioId VARCHAR(36)," +
                    "FOREIGN KEY (usuarioId) REFERENCES usuarios(id)" +
                    ")";
            statement.execute(sqlDietas);

            // Cria a tabela de Exercícios
            String sqlExercicios = "CREATE TABLE IF NOT EXISTS exercicios (" +
                    "id VARCHAR(36) PRIMARY KEY," +
                    "nome TEXT NOT NULL," +
                    "descricao TEXT," +
                    "tipo TEXT," +
                    "tempo INTEGER," +
                    "caloriasQueimadas REAL" +
                    ")";
            statement.execute(sqlExercicios);

            // Cria a tabela de Metas
            String sqlMetas = "CREATE TABLE IF NOT EXISTS metas (" +
                    "id VARCHAR(36) PRIMARY KEY," +
                    "descricao TEXT NOT NULL," +
                    "tipo TEXT," +
                    "valorAlvo REAL," +
                    "progressoAtual REAL," +
                    "dataCriacao TEXT," +
                    "dataConclusao TEXT" +
                    ")";
            statement.execute(sqlMetas);

            // Cria a tabela de Refeições
            String sqlRefeicoes = "CREATE TABLE IF NOT EXISTS refeicoes (" +
                    "id VARCHAR(36) PRIMARY KEY," +
                    "nome TEXT NOT NULL," +
                    "descricao TEXT," +
                    "calorias INTEGER" +
                    ")";
            statement.execute(sqlRefeicoes);

            // Cria a tabela de Treinos
            String sqlTreinos = "CREATE TABLE IF NOT EXISTS treinos (" +
                    "id VARCHAR(36) PRIMARY KEY," +
                    "nome TEXT NOT NULL," +
                    "tipo TEXT," +
                    "duracao INTEGER," +
                    "nivelDeDificuldade INTEGER" +
                    ")";
            statement.execute(sqlTreinos);

            System.out.println("Tabelas criadas com sucesso.");

        } catch (SQLException e) {
            System.err.println("Erro ao criar tabelas!");
            e.printStackTrace();
        }
    }
}