package com.example.computerPart.ui.panel;

import com.example.computerPart.dao.RAMDAO;
import com.example.computerPart.model.RAM;
import com.example.computerPart.ui.dialog.RAMDialog;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableModel;
import javax.swing.RowFilter;
import java.sql.SQLException;

public class RAMPanel extends BaseComponentPanel {
    private final RAMDAO ramDAO;
    private JComboBox<String> searchColumnBox;

    public RAMPanel() {
        super();
        setupSearchColumn();
        ramDAO = new RAMDAO();
        refreshTable();
    }

    private void setupSearchColumn() {
        searchColumnBox = new JComboBox<>(new String[] {
                "All Columns", "Name", "Manufacturer", "Color", "Type", "Bus", "Capacity",
                "Benchmark", "Price Range"
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
                "ID", "Name", "Manufacturer", "Color", "Type", "Bus", "Capacity (GB)",
                "Benchmark", "Price", "Power (W)", "Stock"
        };
    }

    @Override
    protected void refreshTable() {
        try {
            tableModel.setRowCount(0);
            for (RAM ram : ramDAO.getAll()) {
                tableModel.addRow(new Object[] {
                        ram.getId(),
                        ram.getName(),
                        ram.getManufacturer(),
                        ram.getColor(),
                        ram.getRamType(),
                        ram.getBus(),
                        ram.getCapacity(),
                        ram.getBenchmark(),
                        priceFormatter.format(ram.getPrice()),
                        ram.getPower(),
                        ram.getStock()
                });
            }
        } catch (SQLException e) {
            showError("Error loading RAM data: " + e.getMessage());
        }
    }

    @Override
    protected void handleAdd() {
        RAMDialog dialog = new RAMDialog(null);
        if (dialog.showDialog()) {
            try {
                ramDAO.add(dialog.getComponentPart());
                refreshTable();
            } catch (SQLException e) {
                showError("Error adding RAM: " + e.getMessage());
            }
        }
    }

    @Override
    protected void handleEdit() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a RAM to edit");
            return;
        }

        try {
            int id = (int) table.getValueAt(selectedRow, 0);
            RAM ram = ramDAO.getById(id);
            if (ram != null) {
                RAMDialog dialog = new RAMDialog(ram);
                if (dialog.showDialog()) {
                    ramDAO.update(dialog.getComponentPart());
                    refreshTable();
                }
            }
        } catch (SQLException e) {
            showError("Error editing RAM: " + e.getMessage());
        }
    }

    @Override
    protected void handleDelete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a RAM to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this RAM?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = (int) table.getValueAt(selectedRow, 0);
                ramDAO.delete(id);
                refreshTable();
            } catch (SQLException e) {
                showError("Error deleting RAM: " + e.getMessage());
            }
        }
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
                    case "Type" -> 4;
                    case "Bus" -> 5;
                    case "Capacity" -> 6;
                    case "Benchmark" -> 7;
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
                        // Remove commas before parsing
                        String priceStr = entry.getStringValue(8).replace(",", "");
                        double price = Double.parseDouble(priceStr);
                        return price >= min && price <= max;
                    }
                };
            } else {
                double price = Double.parseDouble(text.trim());
                return RowFilter.numberFilter(RowFilter.ComparisonType.EQUAL, price, 8);
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

}

/*
 * private RowFilter<Object, Object> createPriceRangeFilter000(String text) {
 * try {
 * if (text.contains("-")) {
 * String[] range = text.split("-");
 * double min = Double.parseDouble(range[0].trim());
 * double max = Double.parseDouble(range[1].trim());
 * return RowFilter.numberFilter(RowFilter.ComparisonType.BETWEEN, min,
 * max,8);
 * } else {
 * double price = Double.parseDouble(text.trim());
 * return RowFilter.numberFilter(RowFilter.ComparisonType.EQUAL, price, 8);
 * }
 * } catch (NumberFormatException e) {
 * return null;
 * }
 * }
 */