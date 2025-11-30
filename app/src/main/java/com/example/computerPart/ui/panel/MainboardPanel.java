package com.example.computerPart.ui.panel;

import com.example.computerPart.dao.MainboardDAO;
import com.example.computerPart.model.Mainboard;
import com.example.computerPart.ui.dialog.MainboardDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.RowFilter;
import java.sql.SQLException;
import java.util.Comparator;

public class MainboardPanel extends BaseComponentPanel {
    private final MainboardDAO mainboardDAO;
    private JComboBox<String> searchColumnBox;

    public MainboardPanel() {
        super();
        setupSearchColumn();
        mainboardDAO = new MainboardDAO();
        refreshTable();
    }

    private void setupSearchColumn() {
        // Add column selector for search
        searchColumnBox = new JComboBox<>(new String[] {
                "All Columns", "Name", "Manufacturer", "Color", "CPU Socket",
                "RAM Type", "Size", "Price Range"
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
                "ID", "Name", "Manufacturer", "Color", "CPU Socket", "RAM Type",
                "RAM Slots", "GPU Slots", "CPU Slots", "NVME Support", "Size",
                "Price", "Power (W)", "Stock"
        };
    }

    @Override
    protected void refreshTable() {
        try {
            tableModel.setRowCount(0);
            for (Mainboard mainboard : mainboardDAO.getAll()) {
                tableModel.addRow(new Object[] {
                        mainboard.getId(),
                        mainboard.getName(),
                        mainboard.getManufacturer(),
                        mainboard.getColor(),
                        mainboard.getCpuSocket(),
                        mainboard.getRamType(),
                        mainboard.getRamSlots(),
                        mainboard.getGpuSlots(),
                        mainboard.getCpuSlots(),
                        mainboard.isSupportNVME(),
                        mainboard.getMainSize(),
                        priceFormatter.format(mainboard.getPrice()),
                        mainboard.getPower(),
                        mainboard.getStock()
                });
            }
        } catch (SQLException e) {
            showError("Error loading Mainboard data: " + e.getMessage());
        }
    }

    @Override
    protected void handleAdd() {
        MainboardDialog dialog = new MainboardDialog(null);
        if (dialog.showDialog()) {
            try {
                mainboardDAO.add(dialog.getComponentPart());
                refreshTable();
            } catch (SQLException e) {
                showError("Error adding Mainboard: " + e.getMessage());
            }
        }
    }

    @Override
    protected void handleEdit() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a Mainboard to edit");
            return;
        }

        try {
            int id = (int) table.getValueAt(selectedRow, 0);
            Mainboard mainboard = mainboardDAO.getById(id);
            if (mainboard != null) {
                MainboardDialog dialog = new MainboardDialog(mainboard);
                if (dialog.showDialog()) {
                    mainboardDAO.update(dialog.getComponentPart());
                    refreshTable();
                }
            }
        } catch (SQLException e) {
            showError("Error editing Mainboard: " + e.getMessage());
        }
    }

    @Override
    protected void handleDelete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a Mainboard to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this Mainboard?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = (int) table.getValueAt(selectedRow, 0);
                mainboardDAO.delete(id);
                refreshTable();
            } catch (SQLException e) {
                showError("Error deleting Mainboard: " + e.getMessage());
            }
        }
    }

    @Override
    protected void setupSorting() {
        // Set comparators for numeric columns
        sorter.setComparator(0, (Comparator<Integer>) Integer::compare); // ID
        sorter.setComparator(6, (Comparator<Integer>) Integer::compare); // RAM Slots
        sorter.setComparator(7, (Comparator<Integer>) Integer::compare); // GPU Slots
        sorter.setComparator(8, (Comparator<Integer>) Integer::compare); // CPU Slots
        sorter.setComparator(11, (Comparator<Double>) Double::compare); // Price
        sorter.setComparator(12, (Comparator<Integer>) Integer::compare); // Power
        sorter.setComparator(13, (Comparator<Integer>) Integer::compare); // Stock
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
        } else if ("Price Range".equals(selectedColumn)) {
            filter = createPriceRangeFilter(text);
        } else {
            // Search in specific column
            int columnIndex = switch (selectedColumn) {
                case "Name" -> 1;
                case "Manufacturer" -> 2;
                case "Color" -> 3;
                case "CPU Socket" -> 4;
                case "RAM Type" -> 5;
                case "Size" -> 10;
                default -> 0;
            };
            filter = RowFilter.regexFilter("(?i)" + text, columnIndex);
        }
        sorter.setRowFilter(filter);
    }

    private RowFilter<DefaultTableModel, Object> createPriceRangeFilter(String text) {
        return new RowFilter<DefaultTableModel, Object>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                try {
                    // Remove commas before parsing the price
                    double price = Double.parseDouble(entry.getValue(11).toString().replace(",",
                            ""));

                    if (text.contains("-")) {
                        String[] range = text.split("-");
                        if (range.length != 2 || range[0].isEmpty() || range[1].isEmpty()) {
                            return true;
                        }
                        try {
                            double min = Double.parseDouble(range[0].trim());
                            double max = Double.parseDouble(range[1].trim());
                            return price >= min && price <= max;
                        } catch (NumberFormatException e) {
                            return true;
                        }
                    } else if (text.startsWith(">")) {
                        if (text.length() == 1)
                            return true;
                        try {
                            double min = Double.parseDouble(text.substring(1).trim());
                            return price > min;
                        } catch (NumberFormatException e) {
                            return true;
                        }
                    } else if (text.startsWith("<")) {
                        if (text.length() == 1)
                            return true;
                        try {
                            double max = Double.parseDouble(text.substring(1).trim());
                            return price < max;
                        } catch (NumberFormatException e) {
                            return true;
                        }
                    } else {
                        try {
                            double value = Double.parseDouble(text);
                            return price == value;
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