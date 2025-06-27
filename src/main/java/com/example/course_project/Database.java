package com.example.course_project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Database instance;
    private Connection conn;
    private final String URL = "jdbc:mysql://localhost:3306/qualification_courses";
    private final String USER = "admin";
    private final String PASS = "qwerty";
    private Database() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch(ClassNotFoundException e) {
            throw new SQLException(e);
        }
        conn = DriverManager.getConnection(URL, USER, PASS);
    }
    public static Connection getConnection() throws SQLException {
        if (instance==null || instance.conn.isClosed())
            instance = new Database();
        return instance.conn;
    }
}