package com.example.computerPart.dao;

import com.example.computerPart.model.BaseComputerPart;
import com.example.computerPart.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseComputerPartDAO<T extends BaseComputerPart> {
    protected Connection connection;
    protected String tableName;

    public BaseComputerPartDAO(String tableName) {
        this.tableName = tableName;
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Abstract methods to be implemented by subclasses
    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;

    protected abstract void setParametersForInsert(PreparedStatement pstmt, T entity) throws SQLException;

    protected abstract void setParametersForUpdate(PreparedStatement pstmt, T entity) throws SQLException;

    // Common CRUD operations
    public List<T> getAll() throws SQLException {
        List<T> entities = new ArrayList<>();
        String query = "SELECT * FROM " + tableName;

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                entities.add(mapResultSetToEntity(rs));
            }
        }
        return entities;
    }

    public T getById(int id) throws SQLException {
        String query = "SELECT * FROM " + tableName + " WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        }
        return null;
    }

    public void add(T entity) throws SQLException {
        // This is a generic implementation that will be customized by subclasses
        String query = getInsertQuery();

        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setParametersForInsert(pstmt, entity);
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void update(T entity) throws SQLException {
        String query = getUpdateQuery();

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            setParametersForUpdate(pstmt, entity);
            pstmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM " + tableName + " WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    // Helper methods for building queries
    protected abstract String getInsertQuery();

    protected abstract String getUpdateQuery();
}