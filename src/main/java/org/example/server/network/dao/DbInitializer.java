package org.example.server.network.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DbInitializer {
    public static void init() {
        try (Connection conn = DbUtil.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS product_group (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    description TEXT
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS product (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    description TEXT,
                    price REAL,
                    group_id INTEGER,
                    FOREIGN KEY(group_id) REFERENCES product_group(id)
                );
            """);

            System.out.println("Таблиці створено або вже існують.");

        } catch (SQLException e) {
            System.err.println("Помилка ініціалізації бази:");
            e.printStackTrace();
        }
    }
}
