package org.example.server.network.dao;

import org.example.shared.model.Product;
import org.example.ProductGroup;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupDao {

    public List<ProductGroup> getAllGroups() {
        List<ProductGroup> result = new ArrayList<>();
        String sql = "SELECT name, description FROM product_groups";

        try (Connection conn = DbUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String name = rs.getString("name");
                String description = rs.getString("description");
                result.add(new ProductGroup(name, description));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean addGroup(String name, String description) {
        String sql = "INSERT INTO product_groups (name, description) VALUES (?, ?)";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, description);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(" SQL помилка при додаванні групи: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateGroup(String oldName, String newName, String newDescription) {
        String sql = "UPDATE product_groups SET name = ?, description = ? WHERE name = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newName);
            stmt.setString(2, newDescription);
            stmt.setString(3, oldName);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteGroupByName(String name) {
        String sql = "DELETE FROM product_groups WHERE name = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getAllGroupNames() {
        List<String> result = new ArrayList<>();
        String sql = "SELECT name FROM product_groups";

        try (Connection conn = DbUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                result.add(rs.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean addGroup(String name) {
        return addGroup(name, "");
    }

    public boolean updateGroupName(String oldName, String newName) {
        return updateGroup(oldName, newName, "");
    }
}
