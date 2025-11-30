package com.example.computerPart.dao;

import com.example.computerPart.model.Cooler;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CoolerDAO extends BaseComputerPartDAO<Cooler> {

    public CoolerDAO() {
        super("Cooler");
    }

    @Override
    protected Cooler mapResultSetToEntity(ResultSet rs) throws SQLException {
        String socketSupportStr = rs.getString("socketSupport");
        List<String> socketSupport = socketSupportStr != null ? Arrays.asList(socketSupportStr.split(",")) : null;

        return new Cooler(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("manufacturer"),
                rs.getInt("stock"),
                rs.getInt("power"),
                rs.getString("color"),
                rs.getDouble("price"),
                rs.getString("coolerType"),
                socketSupport,
                rs.getString("ledColor"));
    }

    @Override
    protected void setParametersForInsert(PreparedStatement pstmt, Cooler cooler) throws SQLException {
        pstmt.setString(1, cooler.getName());
        pstmt.setString(2, cooler.getManufacturer());
        pstmt.setDouble(3, cooler.getPrice());
        pstmt.setInt(4, cooler.getStock());
        pstmt.setInt(5, cooler.getPower());
        pstmt.setString(6, cooler.getColor());
        pstmt.setString(7, cooler.getCoolerType());
        pstmt.setString(8, cooler.getSocketSupport().stream().collect(Collectors.joining(",")));
        pstmt.setString(9, cooler.getLedColor());
    }

    @Override
    protected void setParametersForUpdate(PreparedStatement pstmt, Cooler cooler) throws SQLException {
        pstmt.setString(1, cooler.getName());
        pstmt.setString(2, cooler.getManufacturer());
        pstmt.setDouble(3, cooler.getPrice());
        pstmt.setInt(4, cooler.getStock());
        pstmt.setInt(5, cooler.getPower());
        pstmt.setString(6, cooler.getColor());
        pstmt.setString(7, cooler.getCoolerType());
        pstmt.setString(8, cooler.getSocketSupport().stream().collect(Collectors.joining(",")));
        pstmt.setString(9, cooler.getLedColor());
        pstmt.setInt(10, cooler.getId());
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO Cooler (name, manufacturer, price, stock, power, color, coolerType, socketSupport, ledColor) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE Cooler SET name = ?, manufacturer = ?, price = ?, stock = ?, power = ?, color = ?, coolerType = ?, socketSupport = ?, ledColor = ? WHERE id = ?";
    }
}