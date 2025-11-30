package com.example.computerPart.dao;

import com.example.computerPart.model.HardDisk;

import java.sql.*;

public class HardDiskDAO extends BaseComputerPartDAO<HardDisk> {

    public HardDiskDAO() {
        super("HardDisk");
    }

    @Override
    protected HardDisk mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new HardDisk(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("manufacturer"),
                rs.getInt("stock"),
                rs.getInt("power"),
                rs.getString("color"),
                rs.getDouble("price"),

                rs.getString("type"),
                rs.getInt("speed"),
                rs.getInt("capacity"));
    }

    @Override
    protected void setParametersForInsert(PreparedStatement pstmt, HardDisk hardDisk) throws SQLException {
        pstmt.setString(1, hardDisk.getName());
        pstmt.setString(2, hardDisk.getManufacturer());
        pstmt.setDouble(3, hardDisk.getPrice());
        pstmt.setInt(4, hardDisk.getStock());
        pstmt.setString(5, hardDisk.getType());
        pstmt.setInt(6, hardDisk.getPower());
        pstmt.setInt(7, hardDisk.getSpeed());
        pstmt.setString(8, hardDisk.getColor());
        pstmt.setInt(9, hardDisk.getCapacity());
    }

    @Override
    protected void setParametersForUpdate(PreparedStatement pstmt, HardDisk hardDisk) throws SQLException {
        pstmt.setString(1, hardDisk.getName());
        pstmt.setString(2, hardDisk.getManufacturer());
        pstmt.setDouble(3, hardDisk.getPrice());
        pstmt.setInt(4, hardDisk.getStock());
        pstmt.setString(5, hardDisk.getType());
        pstmt.setInt(6, hardDisk.getPower());
        pstmt.setInt(7, hardDisk.getSpeed());
        pstmt.setString(8, hardDisk.getColor());
        pstmt.setInt(9, hardDisk.getId());
        pstmt.setInt(10, hardDisk.getCapacity());
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO HardDisk (name, manufacturer, price, stock, type, power, speed, color, capacity) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE HardDisk SET name = ?, manufacturer = ?, price = ?, stock = ?, type = ?, power = ?, speed = ?, color = ?, capacity=? WHERE id = ?";
    }
}