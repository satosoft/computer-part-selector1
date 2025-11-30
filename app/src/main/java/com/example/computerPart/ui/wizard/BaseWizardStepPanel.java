package com.example.computerPart.ui.wizard;

import com.example.computerPart.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Base class for all wizard step panels that provides common functionality
 * such as table setup, sorting, filtering, and validation.////
 */
public abstract class BaseWizardStepPanel extends JPanel {
    protected WizardComponentManager componentManager;
    protected NumberFormat priceFormatter;
    protected JTable componentTable;
    protected DefaultTableModel tableModel;
    protected TableRowSorter<DefaultTableModel> sorter;
    protected double previousStepsTotalCost = 0;
    protected double totalCost = 0;
    protected JPanel costPanel;
    protected JLabel costLabel;

    public BaseWizardStepPanel(String title) {
        componentManager = WizardComponentManager.getInstance();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder(title));
        // Initialize common formatters
        priceFormatter = NumberFormat.getNumberInstance(Locale.US);
        priceFormatter.setGroupingUsed(true);
        priceFormatter.setMaximumFractionDigits(0);
    }

    public void addCostPanel() {
        costPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        costPanel.setBorder(BorderFactory.createTitledBorder("Total Cost"));
        costLabel = new JLabel("Total Cost: " + priceFormatter.format(totalCost));
        costPanel.add(costLabel);
        add(costPanel, BorderLayout.SOUTH);
    }

    protected void setupTable(String[] columnNames) {
        tableModel = new DefaultTableModel(columnNames, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        componentTable = new JTable(tableModel);
        componentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Setup sorting
        setupSorting();

        // Add table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(componentTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    protected void setupSorting() {
        sorter = new TableRowSorter<>(tableModel);
        componentTable.setRowSorter(sorter);
        componentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                onSelectionChanged();
            }
        });

        // List of column names that should be sorted as integers
        String[] columnInteger = { "ID", "Stock", "Power(W)", "Capacity(GB)",
                "Bus(MHz)", "Quantity Needed", "Speed(MB/s)", "RAM Slots",
                "GPU Slots", "CPU Slots", "Core Number" };

        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            String columnName = tableModel.getColumnName(i);

            // If the column name matches one of the specified integer columns, set
            // comparator
            if (Arrays.asList(columnInteger).contains(columnName)) {
                sorter.setComparator(i, (Comparator<Integer>) Integer::compare);
            } else if ("Benchmark".equals(columnName)) {
                sorter.setComparator(i, (Comparator<Double>) Double::compare);
            } else if ("Price".equals(columnName)) {
                sorter.setComparator(i, (Comparator<String>) (s1, s2) -> {
                    try {
                        // Remove commas and parse as double
                        double price1 = Double.parseDouble(s1.replace(",", ""));
                        double price2 = Double.parseDouble(s2.replace(",", ""));
                        return Double.compare(price1, price2);
                    } catch (NumberFormatException e) {
                        return s1.compareTo(s2); // Fallback to string comparison
                    }
                });
            }
        }

    }

    protected int getSelectedModelRow() {
        int selectedRow = componentTable.getSelectedRow();
        if (selectedRow == -1) {
            return -1;
        }

        // Convert view index to model index since the table might be sorted
        return componentTable.convertRowIndexToModel(selectedRow);
    }

    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    protected abstract void refreshComponents();

    protected abstract void applyFilters();

    protected abstract void clearFilters();

    protected abstract void updateTable();

    protected abstract void selectSuitableComponent();

    protected abstract boolean validateSelection();

    protected abstract void onSelectionChanged();

    protected abstract void updateInformationLabel();

    protected abstract void onPressBackButton();

    protected abstract void updateWizardStepDataTable();

    protected void exportToExcel(String filePath) throws IOException {
    };
}

/*
 * protected Mainboard sMainboard;
 * protected CPU sCpu;
 * protected RAM sRam;
 * protected GPU sGpu;
 * protected HardDisk sHardDisk;
 * protected Cooler sCooler;
 * protected ComputerCase sComputerCase;
 * protected PowerSource sPowerSource;
 * // protected String sPreferredColor;
 * // protected String baseMainSize;
 * // protected double sBudget;
 * // protected double remainingBudget;
 * //
 * // protected int baseCpuNumber = 0;
 * // protected int baseGpuNumber = 0;
 * // protected int selectedRamNumber;
 * // protected int baseRamCapacity = 0;
 * // protected int baseHardDiskCapacity = 0;
 * // protected int BaseMaxSearchTime = 0;
 * // protected double selectedMainboardPrice = 0;
 * // protected double selectedCpuPrice = 0;
 * // protected double selectedRamPrice = 0;
 * // protected double selectedGpuPrice = 0;
 * // protected double selectedHardDiskPrice = 0;
 * // protected double selectedCoolerPrice = 0;
 * // protected double selectedComputerCasePrice = 0;
 * // protected double selectedPowerSourcePrice = 0;
 * 
 * 
 * Constructor for the base wizard step panel
 * 
 * @param title The title to display in the panel border
 *
 * 
 * 
 * public void setTotalCost(double cost) {
 * this.totalCost += cost;
 * updateCostDisplay();
 * }**
 * 
 * public double getTotalCost() {
 * return totalCost;
 * }**
 * 
 * Sets the mainboard for this step**
 * 
 * @param
 * mainboard The
 * selected mainboard***
 * 
 * public void setMainboard(Mainboard mainboard) {
 * this.selectedMainboard = mainboard;
 * onMainboardChanged();
 * refreshComponents();
 * }****
 * 
 * Called when
 * the mainboard
 * is changed*Override this
 * method to
 * update UI
 * elements based
 * on the mainboard***
 * 
 * protected void onMainboardChanged() {
 * // Default implementation does nothing
 * }****
 * 
 * Filters components
 * by budget
 * and color***
 * 
 * public void filterByBudgetAndColor(double budget, String preferredColor) {
 * // this.BaseBudgetbudget = budget;
 * // this.BasePreferredColor = preferredColor;
 * double remainingBudget = budget - totalCost;
 * if (remainingBudget <= 0) {
 * tableModel.setRowCount(0);
 * return;
 * }
 * System.out.println("Filtering by budget: " + budget + " and color: " +
 * preferredColor);
 * refreshComponents();
 * }***
 * 
 * public void filterByBudget(double budget) {
 * 
 * }**
 * 
 * public void filterByColor(String preferredColor) {
 * 
 * }*
 * 
 * Refreshes the
 * component list
 * and updates
 * the table*
 * This method
 * should be
 * implemented by subclasses**
 * 
 * protected void setNumericComparator(int columnIndex) {
 * sorter.setComparator(columnIndex, (Comparator<Integer>) Integer::compare);
 * }
 * *
 * 
 * int priceColumnIndex = -1;
 * for (int i = 0; i < tableModel.getColumnCount(); i++) {
 * if ("Price".equals(tableModel.getColumnName(i))) {
 * priceColumnIndex = i;
 * break;
 * }
 * }
 * 
 * // Price column needs special handling because it's formatted with commas
 * if (priceColumnIndex != -1) {
 * sorter.setComparator(priceColumnIndex, new Comparator<String>() {
 * 
 * @Override
 * public int compare(String s1, String s2) {
 * try {
 * // Remove commas and parse as double
 * double price1 = Double.parseDouble(s1.replace(",", ""));
 * double price2 = Double.parseDouble(s2.replace(",", ""));
 * return Double.compare(price1, price2);
 * } catch (NumberFormatException e) {
 * return s1.compareTo(s2); // Fallback to string comparison
 * }
 * }
 * });
 * }
 * // Set comparators based on column class types
 * 
 * for (int i = 0; i < tableModel.getColumnCount(); i++) {
 * Class<?> columnClass = tableModel.getColumnClass(i);
 * if (columnClass == Integer.class) {
 * sorter.setComparator(i, (Comparator<Integer>) Integer::compare);
 * } else if (columnClass == Double.class) {
 * sorter.setComparator(i, (Comparator<Double>) Double::compare);
 * } else if ("Price".equals(tableModel.getColumnName(i))) {
 * sorter.setComparator(i, new Comparator<String>() {
 * 
 * @Override
 * public int compare(String s1, String s2) {
 * try {
 * double price1 = Double.parseDouble(s1.replace(",", ""));
 * double price2 = Double.parseDouble(s2.replace(",", ""));
 * return Double.compare(price1, price2);
 * } catch (NumberFormatException e) {
 * return s1.compareTo(s2);
 * }
 * }
 * });
 * }
 * }
 * 
 * protected void setupTable(String[] columnNames) {
 * tableModel = new DefaultTableModel(columnNames, 0) {
 * 
 * @Override
 * public Class<?> getColumnClass(int column) {
 * if (getRowCount() > 0) {
 * Object value = getValueAt(0, column);
 * if (value != null) {
 * return value.getClass();
 * }
 * }
 * return Object.class;
 * }
 * 
 * @Override
 * public boolean isCellEditable(int row, int column) {
 * return false;
 * }
 * };
 * 
 * componentTable = new JTable(tableModel);
 * componentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
 * 
 * setupSorting();
 * 
 * JScrollPane scrollPane = new JScrollPane(componentTable);
 * add(scrollPane, BorderLayout.CENTER);
 * }
 * protected void setPriceComparator(int columnIndex) {
 * sorter.setComparator(columnIndex, new Comparator<String>() {
 * 
 * @Override
 * public int compare(String s1, String s2) {
 * try {
 * // Remove commas and parse as double
 * double price1 = Double.parseDouble(s1.replace(",", ""));
 * double price2 = Double.parseDouble(s2.replace(",", ""));
 * return Double.compare(price1, price2);
 * } catch (NumberFormatException e) {
 * return s1.compareTo(s2); // Fallback to string comparison
 * }
 * }
 * });
 * }
 */