package org.example.util;

import org.example.server.network.dao.DbUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseViewer {
    public static void main(String[] args) {
        try (Connection conn = DbUtil.getConnection();
             Statement stmt = conn.createStatement()) {

            System.out.println("=== ГРУПИ ===");
            ResultSet rs1 = stmt.executeQuery("SELECT * FROM product_group");
            while (rs1.next()) {
                System.out.printf("ID: %d | Name: %s | Desc: %s%n",
                        rs1.getInt("id"),
                        rs1.getString("name"),
                        rs1.getString("description"));
            }

            System.out.println("\n=== ТОВАРИ ===");
            ResultSet rs2 = stmt.executeQuery("SELECT * FROM product");
            while (rs2.next()) {
                System.out.printf("ID: %d | Name: %s | Price: %.2f | GroupID: %d%n",
                        rs2.getInt("id"),
                        rs2.getString("name"),
                        rs2.getDouble("price"),
                        rs2.getInt("group_id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
