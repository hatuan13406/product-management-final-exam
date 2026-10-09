package com.codegym.dao;

import com.codegym.model.Product;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/** Persistence contract for product CRUD. */
public interface ProductDAO {
    List<Product> findAll() throws SQLException;
    Optional<Product> findById(int id) throws SQLException;
    int insert(Product product) throws SQLException;
    boolean update(Product product) throws SQLException;
    boolean deleteById(int id) throws SQLException;
}
