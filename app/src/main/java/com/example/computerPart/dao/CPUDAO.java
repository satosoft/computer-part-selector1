package com.example.computerPart.dao;

import com.example.computerPart.model.CPU;
import java.sql.*;

public class CPUDAO extends BaseComputerPartDAO<CPU> {

    public CPUDAO() {
        super("CPU");
    }

    @Override
    protected CPU mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new CPU(
            rs.getInt("id"),
                rs.getString("name"),
                rs.getString("manufacturer"),
                rs.getInt("stock"),
                rs.getInt("power"),
                rs.getString("color"),
                rs.getDouble("price"),
 
                rs.getString("generation"),
                rs.getString("socket"),
                rs.getDouble("benchmark"),
                 rs.getInt("coreNum")
            );
    }

    @Override
    protected void setParametersForInsert(PreparedStatement stmt, CPU cpu) throws SQLException {
        stmt.setString(1, cpu.getName());
        stmt.setString(2, cpu.getManufacturer());
        stmt.setDouble(3, cpu.getPrice());
        stmt.setInt(4, cpu.getStock());
        stmt.setString(5, cpu.getGeneration());
        stmt.setString(6, cpu.getSocket());
        stmt.setDouble(7, cpu.getBenchmark());
        stmt.setInt(8, cpu.getPower());
        stmt.setInt(9, cpu.getCoreNum());
        stmt.setString(10, cpu.getColor());
    }

    @Override
    protected void setParametersForUpdate(PreparedStatement stmt, CPU cpu) throws SQLException {
        stmt.setString(1, cpu.getName());
        stmt.setString(2, cpu.getManufacturer());
        stmt.setDouble(3, cpu.getPrice());
        stmt.setInt(4, cpu.getStock());
        stmt.setString(5, cpu.getGeneration());
        stmt.setString(6, cpu.getSocket());
        stmt.setDouble(7, cpu.getBenchmark());
        stmt.setInt(8, cpu.getPower());
        stmt.setInt(9, cpu.getCoreNum());
        stmt.setString(10, cpu.getColor());
        stmt.setInt(11, cpu.getId());
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO CPU (name, manufacturer, price, stock, generation, socket, benchmark, power, coreNum, color) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE CPU SET name = ?, manufacturer = ?, price = ?, stock = ?, generation = ?, socket = ?, benchmark = ?, power = ?, coreNum = ?, color = ? WHERE id = ?";
    }
}

//