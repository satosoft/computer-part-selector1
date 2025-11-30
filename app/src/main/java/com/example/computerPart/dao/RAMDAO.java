package com.example.computerPart.dao;

import com.example.computerPart.model.RAM;

import java.sql.*;

public class RAMDAO extends BaseComputerPartDAO<RAM> {

    public RAMDAO() {
        super("RAM");
    }

    @Override
    protected RAM mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new RAM(
            rs.getInt("id"),
                rs.getString("name"),
                rs.getString("manufacturer"),
                rs.getInt("stock"),
                rs.getInt("power"),
                rs.getString("color"),
                rs.getDouble("price"),

                rs.getInt("capacity"),
                rs.getDouble("benchmark"),
                rs.getString("ramType"),
                rs.getInt("bus")
                );
    }

    @Override
    protected void setParametersForInsert(PreparedStatement pstmt, RAM ram) throws SQLException {
        pstmt.setString(1, ram.getName());
        pstmt.setString(2, ram.getManufacturer());
        pstmt.setDouble(3, ram.getPrice());
        pstmt.setInt(4, ram.getStock());
        pstmt.setInt(5, ram.getCapacity());
        pstmt.setDouble(6, ram.getBenchmark());
        pstmt.setInt(7, ram.getPower());
        pstmt.setString(8, ram.getRamType());
        pstmt.setInt(9, ram.getBus());
        pstmt.setString(10, ram.getColor());
    }

    @Override
    protected void setParametersForUpdate(PreparedStatement pstmt, RAM ram) throws SQLException {
        pstmt.setString(1, ram.getName());
        pstmt.setString(2, ram.getManufacturer());
        pstmt.setDouble(3, ram.getPrice());
        pstmt.setInt(4, ram.getStock());
        pstmt.setInt(5, ram.getCapacity());
        pstmt.setDouble(6, ram.getBenchmark());
        pstmt.setInt(7, ram.getPower());
        pstmt.setString(8, ram.getRamType());
        pstmt.setInt(9, ram.getBus());
        pstmt.setString(10, ram.getColor());
        pstmt.setInt(11, ram.getId());
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO RAM (name, manufacturer, price, stock, capacity, benchmark, power, ramType, bus, color) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE RAM SET name = ?, manufacturer = ?, price = ?, stock = ?, capacity = ?, benchmark = ?, power = ?, ramType = ?, bus = ?, color = ? WHERE id = ?";
    }
}