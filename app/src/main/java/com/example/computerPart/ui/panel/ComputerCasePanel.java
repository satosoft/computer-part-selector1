package com.example.computerPart.ui.panel;

import com.example.computerPart.dao.ComputerCaseDAO;
import com.example.computerPart.model.ComputerCase;
import com.example.computerPart.ui.dialog.ComputerCaseDialog;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableModel;
import javax.swing.RowFilter;
import java.sql.SQLException;
import java.util.Comparator;

public class ComputerCasePanel extends BaseComponentPanel {
    private final ComputerCaseDAO computerCaseDAO;
    private JComboBox<String> searchColumnBox;

    public ComputerCasePanel() {
        super();
        computerCaseDAO = new ComputerCaseDAO();
        setupSearchColumn();
        refreshTable();
    }

    private void setupSearchColumn() {
        searchColumnBox = new JComboBox<>(new String[] {
                "All Columns", "Name", "Manufacturer", "Color", "Case Size",
                "Case Material", "Case Type", "Price Range"
        });
        searchColumnBox.addActionListener(e -> search());

        // Add to search panel
        JPanel searchPanel = (JPanel) getComponent(0);
        searchPanel.add(new JLabel("in"));
        searchPanel.add(searchColumnBox);
    }

    @Override
    protected String[] getColumnNames() {
        return new String[] {
                "ID", "Name", "Manufacturer", "Color", "Case Size",
                "Case Material", "Case Type", "Price", "Power (W)", "Stock"
        };
    }

    @Override
    protected void refreshTable() {
        try {
            tableModel.setRowCount(0);
            for (ComputerCase computerCase : computerCaseDAO.getAll()) {
                tableModel.addRow(new Object[] {
                        computerCase.getId(),
                        computerCase.getName(),
                        computerCase.getManufacturer(),
                        computerCase.getColor(),
                        computerCase.getCaseSize(),
                        computerCase.getCaseMaterial(),
                        computerCase.getCaseType(),
                        priceFormatter.format(computerCase.getPrice()),
                        computerCase.getPower(),
                        computerCase.getStock()
                });
            }
        } catch (SQLException e) {
            showError("Error loading Computer Case data: " + e.getMessage());
        }
    }

    @Override
    protected void handleAdd() {
        ComputerCaseDialog dialog = new ComputerCaseDialog(null);
        if (dialog.showDialog()) {
            try {
                computerCaseDAO.add(dialog.getComponentPart());
                refreshTable();
            } catch (SQLException e) {
                showError("Error adding computer case: " + e.getMessage());
            }
        }
    }

    @Override
    protected void handleEdit() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a computer case to edit");
            return;
        }

        try {
            int id = (int) table.getValueAt(selectedRow, 0);
            ComputerCase computerCase = computerCaseDAO.getById(id);
            if (computerCase != null) {
                ComputerCaseDialog dialog = new ComputerCaseDialog(computerCase);
                if (dialog.showDialog()) {
                    computerCaseDAO.update(dialog.getComponentPart());
                    refreshTable();
                }
            }
        } catch (SQLException e) {
            showError("Error editing computer case: " + e.getMessage());
        }
    }

    @Override
    protected void handleDelete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a computer case to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this computer case?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = (int) table.getValueAt(selectedRow, 0);
                computerCaseDAO.delete(id);
                refreshTable();
            } catch (SQLException e) {
                showError("Error deleting computer case: " + e.getMessage());
            }
        }
    }

    @Override
    protected void setupSorting() {
        // Set comparators for numeric columns
        sorter.setComparator(0, (Comparator<Integer>) Integer::compare); // ID
        sorter.setComparator(7, (Comparator<Double>) Double::compare); // Price
        sorter.setComparator(8, (Comparator<Integer>) Integer::compare); // Power
        sorter.setComparator(9, (Comparator<Integer>) Integer::compare); // Stock
    }

    @Override
    protected void search() {
        String searchText = searchField.getText().toLowerCase();
        String selectedColumn = (String) searchColumnBox.getSelectedItem();
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        if (searchText.trim().isEmpty()) {
            sorter.setRowFilter(null);
            return;
        }

        RowFilter<Object, Object> filter = null;
        try {
            if ("All Columns".equals(selectedColumn)) {
                filter = RowFilter.regexFilter("(?i)" + searchText);
            } else if ("Price Range".equals(selectedColumn)) {
                filter = createPriceRangeFilter(searchText);
            } else {
                int columnIndex = switch (selectedColumn) {
                    case "Name" -> 1;
                    case "Manufacturer" -> 2;
                    case "Color" -> 3;
                    case "Case Size" -> 4;
                    case "Case Material" -> 5;
                    case "Case Type" -> 6;
                    default -> 1;
                };
                filter = RowFilter.regexFilter("(?i)" + searchText, columnIndex);
            }
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }

        sorter.setRowFilter(filter);
    }

    private RowFilter<Object, Object> createPriceRangeFilter(String text) {
        try {
            if (text.contains("-")) {
                String[] range = text.split("-");
                double min = Double.parseDouble(range[0].trim());
                double max = Double.parseDouble(range[1].trim());

                return new RowFilter<Object, Object>() {
                    @Override
                    public boolean include(Entry<? extends Object, ? extends Object> entry) {
                        // Remove commas before parsing the price
                        double price = Double.parseDouble(entry.getStringValue(7).replace(",", "")); // Column index 7
                                                                                                     // for price
                        return price >= min && price <= max;
                    }
                };
            } else {
                double price = Double.parseDouble(text.trim());
                return RowFilter.numberFilter(RowFilter.ComparisonType.EQUAL, price, 7);
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }
}