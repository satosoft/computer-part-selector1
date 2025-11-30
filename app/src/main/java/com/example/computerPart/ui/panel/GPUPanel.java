package com.example.computerPart.ui.panel;

import com.example.computerPart.dao.GPUDAO;
import com.example.computerPart.model.GPU;
import com.example.computerPart.ui.dialog.GPUDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.RowFilter;
import java.sql.SQLException;
import java.util.Comparator;

public class GPUPanel extends BaseComponentPanel {
    private final GPUDAO gpuDAO;
    private JComboBox<String> searchColumnBox;

    public GPUPanel() {
        super();
        setupSearchColumn();
        gpuDAO = new GPUDAO();
        refreshTable();
    }

    @Override
    protected String[] getColumnNames() {
        return new String[] {
                "ID", "Name", "Manufacturer", "Color", "Capacity (GB)", "Benchmark",
                "Price", "Power (W)", "Stock"
        };
    }

    @Override
    protected void refreshTable() {
        try {
            tableModel.setRowCount(0);
            for (GPU gpu : gpuDAO.getAll()) {
                tableModel.addRow(new Object[] {
                        gpu.getId(),
                        gpu.getName(),
                        gpu.getManufacturer(),
                        gpu.getColor(),
                        gpu.getCapacity(),
                        gpu.getBenchmark(),
                        priceFormatter.format(gpu.getPrice()),
                        gpu.getPower(),
                        gpu.getStock()
                });
            }
        } catch (SQLException e) {
            showError("Error loading GPU data: " + e.getMessage());
        }
    }

    @Override
    protected void handleAdd() {
        GPUDialog dialog = new GPUDialog(null);
        if (dialog.showDialog()) {
            try {
                gpuDAO.add(dialog.getComponentPart());
                refreshTable();
            } catch (SQLException e) {
                showError("Error adding GPU: " + e.getMessage());
            }
        }
    }

    @Override
    protected void handleEdit() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a GPU to edit");
            return;
        }

        try {
            int id = (int) table.getValueAt(selectedRow, 0);
            GPU gpu = gpuDAO.getById(id);
            if (gpu != null) {
                GPUDialog dialog = new GPUDialog(gpu);
                if (dialog.showDialog()) {
                    gpuDAO.update(dialog.getComponentPart());
                    refreshTable();
                }
            }
        } catch (SQLException e) {
            showError("Error editing GPU: " + e.getMessage());
        }
    }

    @Override
    protected void handleDelete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a GPU to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this GPU?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = (int) table.getValueAt(selectedRow, 0);
                gpuDAO.delete(id);
                refreshTable();
            } catch (SQLException e) {
                showError("Error deleting GPU: " + e.getMessage());
            }
        }
    }

    @Override
    protected void setupSorting() {
        // Set comparators for numeric columns
        sorter.setComparator(0, (Comparator<Integer>) Integer::compare); // ID
        sorter.setComparator(4, (Comparator<Integer>) Integer::compare); // Capacity
        sorter.setComparator(5, (Comparator<Double>) Double::compare); // Benchmark
        sorter.setComparator(6, (Comparator<Double>) Double::compare); // Price
        sorter.setComparator(7, (Comparator<Integer>) Integer::compare); // Power
        sorter.setComparator(8, (Comparator<Integer>) Integer::compare); // Stock
    }

    private void setupSearchColumn() {
        // Add column selector for search
        searchColumnBox = new JComboBox<>(new String[] {
                "All Columns", "Name", "Manufacturer", "Color", "Capacity",
                "Benchmark", "Price Range"
        });
        searchColumnBox.addActionListener(e -> search());

        // Add to search panel
        JPanel searchPanel = (JPanel) getComponent(0);
        searchPanel.add(new JLabel("in"));
        searchPanel.add(searchColumnBox);
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
                case "Capacity" -> 4;
                case "Benchmark" -> 5;
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
                    double price = Double.parseDouble(entry.getValue(6).toString().replace(",", ""));

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