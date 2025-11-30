package com.example.computerPart.ui.panel;

import com.example.computerPart.dao.PowerSourceDAO;
import com.example.computerPart.model.PowerSource;
import com.example.computerPart.ui.dialog.PowerSourceDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.RowFilter;
import java.sql.SQLException;
import java.util.Comparator;
import java.text.DecimalFormat;

public class PowerSourcePanel extends BaseComponentPanel {
    private final PowerSourceDAO powerSourceDAO;
    private JComboBox<String> searchColumnBox;
    private DecimalFormat priceFormatter = new DecimalFormat("#,##0.00");

    public PowerSourcePanel() {
        super();
        setupSearchColumn();
        powerSourceDAO = new PowerSourceDAO();
        refreshTable();
    }

    private void setupSearchColumn() {
        // Add column selector for search
        searchColumnBox = new JComboBox<>(new String[] {
                "All Columns", "Name", "Manufacturer", "Color", "Power Range", "Price Range"
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
                "ID", "Name", "Manufacturer", "Color", "Power (W)", "Price", "Stock"
        };
    }

    @Override
    protected void refreshTable() {
        try {
            tableModel.setRowCount(0);
            for (PowerSource powerSource : powerSourceDAO.getAll()) {
                tableModel.addRow(new Object[] {
                        powerSource.getId(),
                        powerSource.getName(),
                        powerSource.getManufacturer(),
                        powerSource.getColor(),
                        powerSource.getPower(),
                        priceFormatter.format(powerSource.getPrice()),
                        powerSource.getStock()
                });
            }
        } catch (SQLException e) {
            showError("Error loading Power Source data: " + e.getMessage());
        }
    }

    @Override
    protected void handleAdd() {
        PowerSourceDialog dialog = new PowerSourceDialog(null);
        if (dialog.showDialog()) {
            try {
                powerSourceDAO.add(dialog.getComponentPart());
                refreshTable();
            } catch (SQLException e) {
                showError("Error adding Power Source: " + e.getMessage());
            }
        }
    }

    @Override
    protected void handleEdit() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a Power Source to edit");
            return;
        }

        try {
            PowerSource powerSource = powerSourceDAO.getById((int) table.getValueAt(selectedRow, 0));
            PowerSourceDialog dialog = new PowerSourceDialog(powerSource);
            if (dialog.showDialog()) {
                powerSourceDAO.update(dialog.getComponentPart());
                refreshTable();
            }
        } catch (SQLException e) {
            showError("Error editing Power Source: " + e.getMessage());
        }
    }

    @Override
    protected void handleDelete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a Power Source to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this Power Source?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = (int) table.getValueAt(selectedRow, 0);
                powerSourceDAO.delete(id);
                refreshTable();
            } catch (SQLException e) {
                showError("Error deleting Power Source: " + e.getMessage());
            }
        }
    }

    @Override
    protected void setupSorting() {
        // Set comparators for numeric columns
        sorter.setComparator(0, (Comparator<Integer>) Integer::compare); // ID
        sorter.setComparator(4, (Comparator<Integer>) Integer::compare); // Power
        sorter.setComparator(5, (Comparator<Double>) Double::compare); // Price
        sorter.setComparator(6, (Comparator<Integer>) Integer::compare); // Stock
    }

    @Override
    protected void search() {
        String text = searchField.getText().trim();
        String selectedColumn = (String) searchColumnBox.getSelectedItem();

        if (text.isEmpty()) {
            sorter.setRowFilter(null);
            return;
        }

        RowFilter<DefaultTableModel, Object> filter;
        if ("All Columns".equals(selectedColumn)) {
            filter = RowFilter.regexFilter("(?i)" + text);
        } else if ("Power Range".equals(selectedColumn)) {
            filter = createRangeFilter(text, 4);
        } else if ("Price Range".equals(selectedColumn)) {
            filter = createRangeFilter(text, 5);
        } else {
            // Search in specific column
            int columnIndex = switch (selectedColumn) {
                case "Name" -> 1;
                case "Manufacturer" -> 2;
                case "Color" -> 3;
                default -> 0;
            };
            filter = RowFilter.regexFilter("(?i)" + text, columnIndex);
        }
        sorter.setRowFilter(filter);
    }

    private RowFilter<DefaultTableModel, Object> createRangeFilter(String text,
            int columnIndex) {
        return new RowFilter<DefaultTableModel, Object>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                try {
                    double value = Double.parseDouble(entry.getValue(columnIndex).toString());

                    if (text.contains("-")) {
                        String[] range = text.split("-");
                        if (range.length != 2 || range[0].isEmpty() || range[1].isEmpty()) {
                            return true;
                        }
                        try {
                            double min = Double.parseDouble(range[0].trim());
                            double max = Double.parseDouble(range[1].trim());
                            return value >= min && value <= max;
                        } catch (NumberFormatException e) {
                            return true;
                        }
                    } else if (text.startsWith(">")) {
                        if (text.length() == 1)
                            return true;
                        try {
                            double min = Double.parseDouble(text.substring(1).trim());
                            return value > min;
                        } catch (NumberFormatException e) {
                            return true;
                        }
                    } else if (text.startsWith("<")) {
                        if (text.length() == 1)
                            return true;
                        try {
                            double max = Double.parseDouble(text.substring(1).trim());
                            return value < max;
                        } catch (NumberFormatException e) {
                            return true;
                        }
                    } else {
                        try {
                            double searchValue = Double.parseDouble(text);
                            return value == searchValue;
                        } catch (NumberFormatException e) {
                            return true;
                        }
                    }
                } catch (Exception e) {
                    return true;
                }
            }
        };
    }
}