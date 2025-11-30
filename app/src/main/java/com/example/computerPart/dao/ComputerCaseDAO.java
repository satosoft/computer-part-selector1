package com.example.computerPart.dao;

import com.example.computerPart.model.ComputerCase;

import java.sql.*;

public class ComputerCaseDAO extends BaseComputerPartDAO<ComputerCase> {

    public ComputerCaseDAO() {
        super("ComputerCase");
    }

    @Override
    protected ComputerCase mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new ComputerCase(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("manufacturer"),
                rs.getInt("stock"),
                rs.getInt("power"),
                rs.getString("color"),
                rs.getDouble("price"),
                
                rs.getString("caseSize"),
                rs.getString("caseMaterial"),
                rs.getString("caseType")
            );
    }

    @Override
    protected void setParametersForInsert(PreparedStatement pstmt, ComputerCase computerCase) throws SQLException {
        pstmt.setString(1, computerCase.getName());
        pstmt.setString(2, computerCase.getManufacturer());
        pstmt.setDouble(3, computerCase.getPrice());
        pstmt.setInt(4, computerCase.getStock());
        pstmt.setString(5, computerCase.getCaseSize());
        pstmt.setString(6, computerCase.getCaseMaterial());
        pstmt.setString(7, computerCase.getCaseType());
        pstmt.setInt(8, computerCase.getPower());
        pstmt.setString(9, computerCase.getColor());
    }

    @Override
    protected void setParametersForUpdate(PreparedStatement pstmt, ComputerCase computerCase) throws SQLException {
        pstmt.setString(1, computerCase.getName());
        pstmt.setString(2, computerCase.getManufacturer());
        pstmt.setDouble(3, computerCase.getPrice());
        pstmt.setInt(4, computerCase.getStock());
        pstmt.setString(5, computerCase.getCaseSize());
        pstmt.setString(6, computerCase.getCaseMaterial());
        pstmt.setString(7, computerCase.getCaseType());
        pstmt.setInt(8, computerCase.getPower());
        pstmt.setString(9, computerCase.getColor());
        pstmt.setInt(10, computerCase.getId());
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO ComputerCase (name, manufacturer, price, stock, caseSize, caseMaterial, caseType, power, color) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE ComputerCase SET name = ?, manufacturer = ?, price = ?, stock = ?, caseSize = ?, caseMaterial = ?, caseType = ?, power = ?, color = ? WHERE id = ?";
    }
}