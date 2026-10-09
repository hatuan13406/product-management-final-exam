package com.codegym.dao;

import com.codegym.model.Product;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** JDBC CRUD. No SQL query concatenates user-supplied input. */
public class JdbcProductDAO implements ProductDAO {
    private static final String DEFAULT_DB_URL =
            "jdbc:mysql://localhost:3306/product_management_db?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Ho_Chi_Minh";

    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public JdbcProductDAO() {
        this(System.getenv().getOrDefault("PRODUCT_DB_URL", DEFAULT_DB_URL),
             System.getenv().getOrDefault("PRODUCT_DB_USER", "root"),
             System.getenv().getOrDefault("PRODUCT_DB_PASSWORD", ""));
    }

    public JdbcProductDAO(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    private static Product mapRow(ResultSet rs) throws SQLException {
        return new Product(rs.getInt("id"), rs.getString("name"),
                rs.getBigDecimal("price"), rs.getInt("quantity"));
    }

    @Override
    public List<Product> findAll() throws SQLException {
        String sql = "SELECT id, name, price, quantity FROM products ORDER BY id DESC";
        List<Product> products = new ArrayList<>();
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) products.add(mapRow(rs));
        }
        return products;
    }

    @Override
    public Optional<Product> findById(int id) throws SQLException {
        String sql = "SELECT id, name, price, quantity FROM products WHERE id = ?";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        }
    }

    @Override
    public int insert(Product product) throws SQLException {
        String sql = "INSERT INTO products (name, price, quantity) VALUES (?, ?, ?)";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, product.getName());
            statement.setBigDecimal(2, product.getPrice());
            statement.setInt(3, product.getQuantity());
            if (statement.executeUpdate() != 1) {
                throw new SQLException("Could not insert exactly one product");
            }
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (!keys.next()) throw new SQLException("No generated product ID was returned");
                return keys.getInt(1);
            }
        }
    }

    @Override
    public boolean update(Product product) throws SQLException {
        String sql = "UPDATE products SET name = ?, price = ?, quantity = ? WHERE id = ?";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, product.getName());
            statement.setBigDecimal(2, product.getPrice());
            statement.setInt(3, product.getQuantity());
            statement.setInt(4, product.getId());
            return statement.executeUpdate() == 1;
        }
    }

    @Override
    public boolean deleteById(int id) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() == 1;
        }
    }
}
