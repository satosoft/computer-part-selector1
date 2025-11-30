package com.example.computerPart.ui.panel;

import com.example.computerPart.dao.CPUDAO;
import com.example.computerPart.model.CPU;
import com.example.computerPart.ui.dialog.CPUDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.RowFilter;
import java.sql.SQLException;
import java.util.Comparator;

//import java.util.ArrayList;
//import java.util.List;

//import javax.swing.table.TableRowSorter;
//import javax.swing.RowFilter.Entry;
public class CPUPanel extends BaseComponentPanel {
    private final CPUDAO cpuDAO;
    private JComboBox<String> searchColumnBox;

    public CPUPanel() {
        super();
        setupSearchColumn();
        cpuDAO = new CPUDAO();
        refreshTable();
    }

    private void setupSearchColumn() {
        // Add column selector for search
        searchColumnBox = new JComboBox<>(new String[] {
                "All Columns", "Name", "Manufacturer", "Color", "Generation",
                "Socket", "Benchmark", "Core Number", "Price Range"
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
                "ID", "Name", "Manufacturer", "Color", "Generation", "Socket", "Benchmark",
                "Core Number", "Price", "Power (W)", "Stock"
        };
    }

    @Override
    protected void refreshTable() {
        try {
            tableModel.setRowCount(0);
            for (CPU cpu : cpuDAO.getAll()) {
                tableModel.addRow(new Object[] {
                        cpu.getId(),
                        cpu.getName(),
                        cpu.getManufacturer(),
                        cpu.getColor(),
                        cpu.getGeneration(),
                        cpu.getSocket(),
                        cpu.getBenchmark(),
                        cpu.getCoreNum(),
                        priceFormatter.format(cpu.getPrice()),
                        cpu.getPower(),
                        cpu.getStock()
                });
            }
        } catch (SQLException e) {
            showError("Error loading CPU data: " + e.getMessage());
        }
    }

    @Override
    protected void handleAdd() {
        CPUDialog dialog = new CPUDialog(null);
        if (dialog.showDialog()) {
            try {
                cpuDAO.add(dialog.getComponentPart());
                refreshTable();
            } catch (SQLException e) {
                showError("Error adding CPU: " + e.getMessage());
            }
        }
    }

    @Override
    protected void handleEdit() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a CPU to edit");
            return;
        }

        try {
            int id = (int) table.getValueAt(selectedRow, 0);
            CPU cpu = cpuDAO.getById(id);
            if (cpu != null) {
                CPUDialog dialog = new CPUDialog(cpu);
                if (dialog.showDialog()) {
                    cpuDAO.update(dialog.getComponentPart());
                    refreshTable();
                }
            }
        } catch (SQLException e) {
            showError("Error editing CPU: " + e.getMessage());
        }
    }

    @Override
    protected void handleDelete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a CPU to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this CPU?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = (int) table.getValueAt(selectedRow, 0);
                cpuDAO.delete(id);
                refreshTable();
            } catch (SQLException e) {
                showError("Error deleting CPU: " + e.getMessage());
            }
        }
    }

    @Override
    protected void setupSorting() {
        // Set comparators for numeric columns
        sorter.setComparator(0, (Comparator<Integer>) Integer::compare); // ID//
        sorter.setComparator(6, (Comparator<Double>) Double::compare); // Benchmark
        sorter.setComparator(7, (Comparator<Integer>) Integer::compare); // Core Number
        sorter.setComparator(8, (Comparator<Double>) Double::compare); // Price
        sorter.setComparator(9, (Comparator<Integer>) Integer::compare); // Power
        sorter.setComparator(10, (Comparator<Integer>) Integer::compare); // Stock
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
            filter = createPriceRangeFilter(text, 8);
        } else {
            int columnIndex = switch (selectedColumn) {
                case "Name" -> 1;
                case "Manufacturer" -> 2;
                case "Color" -> 3;
                case "Generation" -> 4;
                case "Socket" -> 5;
                case "Benchmark" -> 6;
                case "Core Number" -> 7;
                default -> 0;
            };
            filter = RowFilter.regexFilter("(?i)" + text, columnIndex);
        }
        sorter.setRowFilter(filter);
    }

    private RowFilter<DefaultTableModel, Object> createPriceRangeFilter(String text, int columnIndex) {
        return new RowFilter<DefaultTableModel, Object>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                try {
                    // Remove commas before parsing
                    String priceStr = entry.getValue(columnIndex).toString().replace(",", "");
                    double price = Double.parseDouble(priceStr);

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