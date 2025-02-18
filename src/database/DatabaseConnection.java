package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/magic_db"; // URL do seu banco
    private static final String USER = "postgres"; // Usuário do PostgreSQL
    private static final String PASSWORD = "0128"; // Senha do PostgreSQL

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao conectar ao banco de dados!");
        }
    }
}
