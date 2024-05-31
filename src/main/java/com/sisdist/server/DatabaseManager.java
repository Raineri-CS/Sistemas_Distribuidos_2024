package com.sisdist.server;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createClienteEmpresa(String nome, String email, String senha, String ramo, String descricao) {
        String sql = "INSERT INTO Cliente_Empresa (Nome, Email, Senha, Ramo, Descricao) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nome);
            statement.setString(2, email);
            statement.setString(3, senha);
            statement.setString(4, ramo);
            statement.setString(5, descricao);
            statement.executeUpdate();
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
                return null;
            }
        } catch (SQLException e) {
//            e.printStackTrace();
            return null;
        }
    }

    public static Empresa readClienteEmpresa(int id) {
        String sql = "SELECT * FROM Cliente_Empresa WHERE ID = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String nome = resultSet.getString("Nome");
                String senha = resultSet.getString("Senha");
                String email = resultSet.getString("Email");
                String ramo = resultSet.getString("Ramo");
                String descricao = resultSet.getString("Descricao");
                return new Empresa(id, nome, email, senha, ramo, descricao);
            } else {
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
                return null;
            }
        } catch (SQLException e) {
//            e.printStackTrace();
            return null;
        }
    }

    public static Empresa readClienteEmpresaFromEmail(String email) {
        String sql = "SELECT * FROM Cliente_Empresa WHERE email = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String nome = resultSet.getString("Nome");
                String senha = resultSet.getString("Senha");
                String ramo = resultSet.getString("Ramo");
                String descricao = resultSet.getString("Descricao");
                return new Empresa(id, nome, email, senha, ramo, descricao);
            } else {
                return null;
            }
        } catch (SQLException e) {
//            e.printStackTrace();
            return null;
        }
    }

    public static int updateClienteCandidato(int id, Map<String, String> camposAtualizados) {
        if (id < 1) {
            throw new IllegalArgumentException("ID must be greater than 0");
        }

        StringBuilder sqlBuilder = new StringBuilder("UPDATE Cliente_Candidato SET ");
        List<String> campos = new ArrayList<>(camposAtualizados.keySet());
        int totalCampos = campos.size();

        for (int i = 0; i < totalCampos; i++) {
            sqlBuilder.append(campos.get(i)).append(" = ?");
            if (i < totalCampos - 1) {
                sqlBuilder.append(", ");
            }
        }
        sqlBuilder.append(" WHERE ID = ?");
        String sql = sqlBuilder.toString();
        int rowsAffected = 0;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            int paramIndex = 1;
            for (String campo : campos) {
                statement.setString(paramIndex++, camposAtualizados.get(campo));
            }
            statement.setInt(paramIndex, id);
            rowsAffected = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowsAffected;
    }

    public static int updateClienteEmpresa(int id, Map<String, String> camposAtualizados) {
        if (id < 1) {
            throw new IllegalArgumentException("ID must be greater than 0");
        }

        StringBuilder sqlBuilder = new StringBuilder("UPDATE Cliente_Empresa SET ");
        List<String> campos = new ArrayList<>(camposAtualizados.keySet());
        int totalCampos = campos.size();

        for (int i = 0; i < totalCampos; i++) {
            sqlBuilder.append(campos.get(i)).append(" = ?");
            if (i < totalCampos - 1) {
                sqlBuilder.append(", ");
            }
        }
        sqlBuilder.append(" WHERE ID = ?");
        String sql = sqlBuilder.toString();
        int rowsAffected = 0;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            int paramIndex = 1;
            for (String campo : campos) {
                statement.setString(paramIndex++, camposAtualizados.get(campo));
            }
            statement.setInt(paramIndex, id);
            rowsAffected = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowsAffected;
    }


    public static void deleteClienteCandidato(int id) {
        String sql = "DELETE FROM Cliente_Candidato WHERE ID = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteClienteEmpresa(int id) {
        String sql = "DELETE FROM Cliente_Empresa WHERE ID = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
