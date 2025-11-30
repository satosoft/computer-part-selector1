package com.example.computerPart.dao;

import com.example.computerPart.model.GPU;

import java.sql.*;

public class GPUDAO extends BaseComputerPartDAO<GPU> {

    public GPUDAO() {
        super("GPU");
    }

    @Override
    protected GPU mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new GPU(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("manufacturer"),
                rs.getInt("stock"),
                rs.getInt("power"),
                rs.getString("color"),
                rs.getDouble("price"),

                rs.getInt("capacity"),
                rs.getDouble("benchmark"));
    }

    @Override
    protected void setParametersForInsert(PreparedStatement pstmt, GPU gpu) throws SQLException {
        pstmt.setString(1, gpu.getName());
        pstmt.setString(2, gpu.getManufacturer());
        pstmt.setDouble(3, gpu.getPrice());
        pstmt.setInt(4, gpu.getStock());
        pstmt.setInt(5, gpu.getCapacity());
        pstmt.setDouble(6, gpu.getBenchmark());
        pstmt.setInt(7, gpu.getPower());
        pstmt.setString(8, gpu.getColor());
    }

    @Override
    protected void setParametersForUpdate(PreparedStatement pstmt, GPU gpu) throws SQLException {
        pstmt.setString(1, gpu.getName());
        pstmt.setString(2, gpu.getManufacturer());
        pstmt.setDouble(3, gpu.getPrice());
        pstmt.setInt(4, gpu.getStock());
        pstmt.setInt(5, gpu.getCapacity());
        pstmt.setDouble(6, gpu.getBenchmark());
        pstmt.setInt(7, gpu.getPower());
        pstmt.setString(8, gpu.getColor());
        pstmt.setInt(9, gpu.getId());
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO GPU (name, manufacturer, price, stock, capacity, benchmark, power, color) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE GPU SET name = ?, manufacturer = ?, price = ?, stock = ?, capacity = ?, benchmark = ?, power = ?, color = ? WHERE id = ?";
    }
}