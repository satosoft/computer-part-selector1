package com.example.computerPart.dao;

import com.example.computerPart.model.Mainboard;

import java.sql.*;

public class MainboardDAO extends BaseComputerPartDAO<Mainboard> {

    public MainboardDAO() {
        super("Mainboard");
    }

    @Override
    protected Mainboard mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new Mainboard(
            rs.getInt("id"),
                rs.getString("name"),
                rs.getString("manufacturer"),
                rs.getInt("stock"),
                rs.getInt("power"),
                rs.getString("color"),
                rs.getDouble("price"),

                rs.getString("cpuSocket"),
                rs.getInt("ramSlots"),
                rs.getInt("gpuSlots"),
                rs.getInt("cpuSlots"),
                rs.getBoolean("supportNVME"),
                rs.getString("mainSize"),
                rs.getString("ramType")
            );
    }

    @Override
    protected void setParametersForInsert(PreparedStatement pstmt, Mainboard mainboard) throws SQLException {
        pstmt.setString(1, mainboard.getName());
        pstmt.setString(2, mainboard.getManufacturer());
        pstmt.setDouble(3, mainboard.getPrice());
        pstmt.setInt(4, mainboard.getStock());
        pstmt.setInt(5, mainboard.getPower());
        pstmt.setString(6, mainboard.getCpuSocket());
        pstmt.setInt(7, mainboard.getRamSlots());
        pstmt.setInt(8, mainboard.getGpuSlots());
        pstmt.setInt(9, mainboard.getCpuSlots());
        pstmt.setBoolean(10, mainboard.isSupportNVME());
        pstmt.setString(11, mainboard.getMainSize());
        pstmt.setString(12, mainboard.getRamType());
        pstmt.setString(13, mainboard.getColor());
    }

    @Override
    protected void setParametersForUpdate(PreparedStatement pstmt, Mainboard mainboard) throws SQLException {
        pstmt.setString(1, mainboard.getName());
        pstmt.setString(2, mainboard.getManufacturer());
        pstmt.setDouble(3, mainboard.getPrice());
        pstmt.setInt(4, mainboard.getStock());
        pstmt.setInt(5, mainboard.getPower());
        pstmt.setString(6, mainboard.getCpuSocket());
        pstmt.setInt(7, mainboard.getRamSlots());
        pstmt.setInt(8, mainboard.getGpuSlots());
        pstmt.setInt(9, mainboard.getCpuSlots());
        pstmt.setBoolean(10, mainboard.isSupportNVME());
        pstmt.setString(11, mainboard.getMainSize());
        pstmt.setString(12, mainboard.getRamType());
        pstmt.setString(13, mainboard.getColor());
        pstmt.setInt(14, mainboard.getId());
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO Mainboard (name, manufacturer, price, stock, power, cpuSocket, ramSlots, gpuSlots, cpuSlots, supportNVME, mainSize, ramType, color) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE Mainboard SET name = ?, manufacturer = ?, price = ?, stock = ?, power = ?, cpuSocket = ?, ramSlots = ?, gpuSlots = ?, cpuSlots = ?, supportNVME = ?, mainSize = ?, ramType = ?, color = ? WHERE id = ?";
    }
}