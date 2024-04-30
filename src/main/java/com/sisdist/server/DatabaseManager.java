package com.sisdist.server;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {
    // Configurações do banco de dados
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String PASSWORD = "0910";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void create(String tabela, String @NotNull ... dados) {
        String sql = "INSERT INTO " + tabela + " VALUES (" + repeat("?,", dados.length - 1) + "?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < dados.length; i++) {
                statement.setString(i + 1, dados[i]);
            }
            statement.executeUpdate();
            System.out.println("Registro criado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void read(String tabela, int id) {
        String sql = "SELECT * FROM " + tabela + " WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            // Processar o resultado da consulta
            if (resultSet.next()) {
                // Exemplo: leitura de dados
                String nome = resultSet.getString("nome");
                String ramo = resultSet.getString("ramo");
                // E assim por diante...
                System.out.println("Nome: " + nome + ", Ramo: " + ramo);
            } else {
                System.out.println("Registro não encontrado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void update(String tabela, int id, String... novosDados) {
        String sql = "UPDATE " + tabela + " SET ";
        for (int i = 0; i < novosDados.length; i++) {
            sql += (i == 0 ? "" : ", ") + "coluna" + (i + 1) + " = ?";
        }
        sql += " WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < novosDados.length; i++) {
                statement.setString(i + 1, novosDados[i]);
            }
            statement.setInt(novosDados.length + 1, id);
            statement.executeUpdate();
            System.out.println("Registro atualizado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(String tabela, int id) {
        String sql = "DELETE FROM " + tabela + " WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("Registro deletado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String repeat(String str, int times) {
        return new String(new char[times]).replace("\0", str);
    }
}
