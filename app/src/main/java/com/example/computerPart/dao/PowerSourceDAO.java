package com.example.computerPart.dao;

import com.example.computerPart.model.PowerSource;

import java.sql.*;

public class PowerSourceDAO extends BaseComputerPartDAO<PowerSource> {

    public PowerSourceDAO() {
        super("PowerSource");
    }

    @Override
    protected PowerSource mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new PowerSource(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("manufacturer"),
            rs.getInt("stock"),
            rs.getInt("power"),
            rs.getString("color"),
            rs.getDouble("price")
            );
    }

    @Override
    protected void setParametersForInsert(PreparedStatement pstmt, PowerSource powerSource) throws SQLException {
        pstmt.setString(1, powerSource.getName());
        pstmt.setString(2, powerSource.getManufacturer());
        pstmt.setDouble(3, powerSource.getPrice());
        pstmt.setInt(4, powerSource.getStock());
        pstmt.setInt(5, powerSource.getPower());
        pstmt.setString(6, powerSource.getColor());
    }

    @Override
    protected void setParametersForUpdate(PreparedStatement pstmt, PowerSource powerSource) throws SQLException {
        pstmt.setString(1, powerSource.getName());
        pstmt.setString(2, powerSource.getManufacturer());
        pstmt.setDouble(3, powerSource.getPrice());
        pstmt.setInt(4, powerSource.getStock());
        pstmt.setInt(5, powerSource.getPower());
        pstmt.setString(6, powerSource.getColor());
        pstmt.setInt(7, powerSource.getId());
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO PowerSource (name, manufacturer, price, stock, power, color) VALUES (?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE PowerSource SET name = ?, manufacturer = ?, price = ?, stock = ?, power = ?, color = ? WHERE id = ?";
    }
}