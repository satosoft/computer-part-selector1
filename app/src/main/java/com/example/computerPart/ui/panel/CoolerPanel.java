package com.example.computerPart.ui.panel;

import com.example.computerPart.dao.CoolerDAO;
import com.example.computerPart.model.Cooler;
import com.example.computerPart.ui.dialog.CoolerDialog;

import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableModel;
import javax.swing.RowFilter;
import java.sql.SQLException;
import java.util.Comparator;

public class CoolerPanel extends BaseComponentPanel {
    private final CoolerDAO coolerDAO;
    private JComboBox<String> searchColumnBox;

    public CoolerPanel() {
        super();
        coolerDAO = new CoolerDAO();
        setupSearchColumn();
        refreshTable();
    }

    private void setupSearchColumn() {
        searchColumnBox = new JComboBox<>(new String[] {
        "All Columns", "Name", "Manufacturer", "Color", "Cooler Type", "Socket Support",
        "LED Color", "Price Range"
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
                "ID", "Name", "Manufacturer", "Color", "Cooler Type", "Socket Support",
                "LED Color", "Price", "Power (W)", "Stock"
        };
    }

    @Override
    protected void refreshTable() {
        try {
            tableModel.setRowCount(0);
            for (Cooler cooler : coolerDAO.getAll()) {
                tableModel.addRow(new Object[] {
                        cooler.getId(),
                        cooler.getName(),
                        cooler.getManufacturer(),
                        cooler.getColor(),
                        cooler.getCoolerType(),
                        String.join(", ", cooler.getSocketSupport()),
                        cooler.getLedColor(),
                        priceFormatter.format(cooler.getPrice()),
                        cooler.getPower(),
                        cooler.getStock()
                });
            }
        } catch (SQLException e) {
            showError("Error loading Cooler data: " + e.getMessage());
        }
    }

    @Override
    protected void handleAdd() {
        CoolerDialog dialog = new CoolerDialog(null);
        if (dialog.showDialog()) {
            try {
                coolerDAO.add(dialog.getComponentPart());
                refreshTable();
            } catch (SQLException e) {
                showError("Error adding cooler: " + e.getMessage());
            }
        }
    }

    @Override
    protected void handleEdit() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a cooler to edit");
            return;
        }

        try {
            int id = (int) table.getValueAt(selectedRow, 0);
            Cooler cooler = coolerDAO.getById(id);
            if (cooler != null) {
                CoolerDialog dialog = new CoolerDialog(cooler);
                if (dialog.showDialog()) {
                    coolerDAO.update(dialog.getComponentPart());
                    refreshTable();
                }
            }
        } catch (SQLException e) {
            showError("Error editing cooler: " + e.getMessage());
        }
    }

    @Override
    protected void handleDelete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a cooler to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this cooler?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = (int) table.getValueAt(selectedRow, 0);
                coolerDAO.delete(id);
                refreshTable();
            } catch (SQLException e) {
                showError("Error deleting cooler: " + e.getMessage());
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
                    case "Cooler Type" -> 4;
                    case "Socket Support" -> 5;
                    case "LED Color" -> 6;
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
                        double price = Double.parseDouble(entry.getStringValue(7).replace(",", ""));
                        // Column index 7
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