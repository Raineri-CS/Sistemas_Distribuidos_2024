package com.sisdist.server;


import java.sql.*;

public class DatabaseManager {
    // Configurações do banco de dados
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String PASSWORD = "0910";

    public DatabaseManager(){}

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

        // Selecionar o banco de dados desejado
        String selectDB = "USE service";
        try (Statement selectStatement = connection.createStatement()) {
            selectStatement.execute(selectDB);
        }

        return connection;
    }


    public static void createClienteCandidato(String nome, String email, String senha) {
        String sql = "INSERT INTO Cliente_Candidato (Nome, Email, Senha) VALUES (?, ?, ?)";

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

    public static Candidato readClienteCandidato(int id) {
        String sql = "SELECT * FROM Cliente_Candidato WHERE ID = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String nome = resultSet.getString("Nome");
                String senha = resultSet.getString("Senha");
                String email = resultSet.getString("Email");
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

    public static Candidato readClienteCandidatoFromEmail(String email) {
        String sql = "SELECT * FROM Cliente_Candidato WHERE email = ?";

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
        if(id < 1){
            // Sim, vou jogar exception ate aqui
            throw new IllegalArgumentException();
        }
        String sql = "UPDATE Cliente_Candidato SET Nome = ?, Email = ?, Senha = ? WHERE ID = ?";

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
        String sql = "DELETE FROM Cliente_Candidato WHERE ID = ?";

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
