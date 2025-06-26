package org.example.server.network.dao;

import org.example.shared.model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {

    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        Connection connection = DbUtil.getConnection();
        String query = "SELECT * FROM product";
        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Product product = new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getInt("group_id")
            );
            products.add(product);
        }

        rs.close();
        ps.close();
        connection.close();
        return products;
    }

    public boolean addProduct(Product product) throws SQLException {
        Connection connection = DbUtil.getConnection();
        String query = "INSERT INTO product (name, description, price, group_id) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, product.getName());
        ps.setString(2, product.getDescription());
        ps.setDouble(3, product.getPrice());
        ps.setInt(4, product.getGroupId());

        boolean result = ps.executeUpdate() > 0;
        ps.close();
        connection.close();
        return result;
    }

    public boolean editProduct(Product product) throws SQLException {
        Connection connection = DbUtil.getConnection();
        String query = "UPDATE product SET name = ?, description = ?, price = ?, group_id = ? WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, product.getName());
        ps.setString(2, product.getDescription());
        ps.setDouble(3, product.getPrice());
        ps.setInt(4, product.getGroupId());
        ps.setInt(5, product.getId());

        boolean result = ps.executeUpdate() > 0;
        ps.close();
        connection.close();
        return result;
    }

    public boolean deleteProduct(int id) throws SQLException {
        Connection connection = DbUtil.getConnection();
        String query = "DELETE FROM product WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, id);

        boolean result = ps.executeUpdate() > 0;
        ps.close();
        connection.close();
        return result;
    }
}
