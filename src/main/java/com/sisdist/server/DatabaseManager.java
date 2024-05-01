package com.sisdist.server;


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

    public DatabaseManager(){}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void createClienteCandidato(String nome, String email, String senha) {
        String sql = "INSERT INTO Cliente_candidato (Nome, Email, Senha) VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nome);
            statement.setString(2, email);
            statement.setString(3, senha);
            statement.executeUpdate();
            System.out.println("Candidato criado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Candidato readClienteCandidato(String email) {
        String sql = "SELECT * FROM Cliente_candidato WHERE Email = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String nome = resultSet.getString("Nome");
                String senha = resultSet.getString("Senha");
                return new Candidato(id, nome, email, senha);
            } else {
                System.out.println("Candidato não encontrado.");
                return null;
            }
        } catch (SQLException e) {
//            e.printStackTrace();
            return null;
        }
    }

    public static void updateClienteCandidato(int id, String novoNome, String novoEmail, String novaSenha) {
        String sql = "UPDATE Cliente_candidato SET Nome = ?, Email = ?, Senha = ? WHERE ID = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, novoNome);
            statement.setString(2, novoEmail);
            statement.setString(3, novaSenha);
            statement.setInt(4, id);
            statement.executeUpdate();
            System.out.println("Candidato atualizado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteClienteCandidato(int id) {
        String sql = "DELETE FROM Cliente_candidato WHERE ID = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("Candidato deletado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
