package org.example.server.network.dao;

import org.example.ProductGroup;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupDao {

    public boolean groupExists(String name) {
        String sql = "SELECT COUNT(*) FROM product_groups WHERE name = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addGroup(String name, String description) {
        if (groupExists(name)) {
            return false;
        }

        String sql = "INSERT INTO product_groups (name, description) VALUES (?, ?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
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

    public boolean deleteGroup(String name) {
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

    public List<ProductGroup> getAllGroups() {
        List<ProductGroup> groups = new ArrayList<>();
        String sql = "SELECT id, name, description FROM product_groups";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                groups.add(new ProductGroup(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return groups;
    }
}
