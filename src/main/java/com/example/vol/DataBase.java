package com.example.vol;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {
    private Connection connection;
    private static DataBase instance;

    private final String URL = "jdbc:mysql://localhost:3306/pidev";
    private final String USER = "root";
    private final String PASSWORD = "191900web";

    private DataBase() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(false);  // Disable auto-commit
            System.out.println("Connected to database");
        } catch (SQLException e) {
            System.out.println("Error while connecting to the database: " + e.getMessage());
        }
    }

    public static DataBase getInstance() {
        if (instance == null)
            instance = new DataBase();  // create an instance if not already created
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void commit() {
        try {
            connection.commit();  // Commit the transaction manually
        } catch (SQLException e) {
            System.out.println("Error during commit: " + e.getMessage());
        }
    }

    public void rollback() {
        try {
            connection.rollback();  // Rollback the transaction
        } catch (SQLException e) {
            System.out.println("Error during rollback: " + e.getMessage());
        }
    }
}
