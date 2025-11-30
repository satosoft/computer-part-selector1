package com.example.computerPart.ui.wizard;

import com.example.computerPart.dao.HardDiskDAO;
import com.example.computerPart.model.HardDisk;
import com.example.computerPart.model.Mainboard;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class WizardStep6Panel extends BaseWizardStepPanel {
    private final HardDiskDAO hardDiskDAO;
    private List<HardDisk> filteredHardDisks;
    private List<HardDisk> suitableHardDisks;
    private List<HardDisk> allHardDisks;

    // Add manufacturer filter
    private JComboBox<String> manufacturerBox;
    // private JComboBox<String> typeBox;
    private JLabel statusLabel;
    private JPanel statusPanel;

    public WizardStep6Panel() {
        super("Step 6: Storage Selection");

        hardDiskDAO = new HardDiskDAO();
        try {
            allHardDisks = hardDiskDAO.getAll();
        } catch (SQLException e) {
            showError("Error loading hard disks: " + e.getMessage());
        }

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filters"));

        // Manufacturer filter
        filterPanel.add(new JLabel("Manufacturer:"));
        manufacturerBox = new JComboBox<>(
                new String[] { "Any", "Samsung", "Western Digital", "Seagate", "Crucial", "Kingston" });
        //manufacturerBox.addActionListener(e -> applyFilters());
        filterPanel.add(manufacturerBox);
        filterPanel.add(Box.createHorizontalStrut(10));

        // Type filter
        // filterPanel.add(new JLabel("Type:"));
        // typeBox = new JComboBox<>(new String[] { "Any", "SSD", "HDD", "NVME" });
        // typeBox.addActionListener(e -> applyFilters());
        // filterPanel.add(typeBox);

        // Apply Filter button
        JButton applyFilterButton = new JButton("Apply Filters");
        applyFilterButton.addActionListener(e -> applyFilters());
        filterPanel.add(applyFilterButton);

        // Clear Filter button//
        JButton clearFilterButton = new JButton("Clear Filters");
        clearFilterButton.addActionListener(e -> clearFilters());
        filterPanel.add(clearFilterButton);

        add(filterPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {
                "ID", "Name", "Manufacturer", "Stock", "Power(W)", "Color", "Price",
                "Type", "Speed(MB/s)", "Capacity(GB)"
        };
        setupTable(columnNames);
        statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // statusLabel = new JLabel("Required HardDiskCapacity: " +
        // this.baseHardDiskCapacity);
        statusLabel = new JLabel("Required HardDisk: N/A");
        statusPanel.add(statusLabel);
        addCostPanel();
        JPanel doublePanelContainer = new JPanel();
        doublePanelContainer.setLayout(new GridLayout(2, 1));
        doublePanelContainer.add(statusPanel);
        doublePanelContainer.add(costPanel);
        add(doublePanelContainer, BorderLayout.SOUTH);

        // Set up numeric column comparators
        // setNumericComparator(0); // ID
        // setNumericComparator(5); // Speed
        // setPriceComparator(6); // Price
        // setNumericComparator(7); // Power
        // setNumericComparator(8); // Stock

        // Initialize the table
        // refreshComponents();
    }

    @Override
    protected void updateWizardStepDataTable() {
        previousStepsTotalCost = componentManager.calculateTotalCost();
        updateInformationLabel();
        refreshComponents();
    }

    @Override
    protected void refreshComponents() {
        selectSuitableComponent();
        populateManufacturerOptions();
        filteredHardDisks = suitableHardDisks;
        updateTable();
    }

    private HardDisk getSelectedHardDisk() {
        int modelRow = getSelectedModelRow();
        if (modelRow == -1) {
            return null;
        }

        int id = (int) tableModel.getValueAt(modelRow, 0);
        return filteredHardDisks.stream()
                .filter(hd -> hd.getId() == id)
                .findFirst()
                .orElse(null);
    }
    private void populateManufacturerOptions() {
        Set<String> uniqueItems = new HashSet<>();
        uniqueItems.add("Any"); // Always include "Any" option

        for (HardDisk hd : allHardDisks) {
            if (hd.getManufacturer() != null && !hd.getManufacturer().isEmpty()) {
                uniqueItems.add(hd.getManufacturer());
            }
        }
        // Update the combo box
        manufacturerBox.removeAllItems();
        for (String ite : uniqueItems) {
            manufacturerBox.addItem(ite);
        }
        // Always set to "Any" initially
        manufacturerBox.setSelectedItem("Any");
    }

    @Override
    protected void applyFilters() {
        selectSuitableComponent();
        String selectedManufacturer = (String) manufacturerBox.getSelectedItem();
        // String selectedType = (String) typeBox.getSelectedItem();
        filteredHardDisks = suitableHardDisks.stream()
                .filter(hd -> {
                    boolean manufacturerMatch = "Any".equals(selectedManufacturer) ||
                            hd.getManufacturer().equals(selectedManufacturer);
                    return manufacturerMatch;
                })
                .collect(Collectors.toList());
        updateTable();
    }

    @Override
    protected void clearFilters() {
        manufacturerBox.setSelectedItem("Any");
        applyFilters();
    }

    @Override
    public void updateTable() {
        tableModel.setRowCount(0);
        for (HardDisk hd : filteredHardDisks) {
            tableModel.addRow(new Object[] {
                    hd.getId(),
                    hd.getName(),
                    hd.getManufacturer(),
                    hd.getStock(),
                    hd.getPower(),
                    hd.getColor(),
                    priceFormatter.format(hd.getPrice()),
                    hd.getType(),
                    hd.getSpeed(),
                    hd.getCapacity()
            });
        }
        // System.out.println("Updated table with " + filteredHardDisks.size() + " hard
        // disks");
    }

    @Override
    public void selectSuitableComponent() {
        suitableHardDisks = allHardDisks.stream()
                .filter(hd -> {
                    double remainingBudget = componentManager.baseBudget - previousStepsTotalCost;
                    double requiredBudget = hd.getPrice();
                    boolean budgetMatch = (requiredBudget <= remainingBudget);
                    if (!budgetMatch) {
                        System.out.println("Filtered out hard disk " + hd.getName() +
                                " due to budget. Required: " + requiredBudget +
                                ", Remaining: " + (remainingBudget));
                    }
                    return budgetMatch;
                })
                .filter(hd -> hd.getStock() > 0)
                .filter(hd -> {
                    int minCapacity = componentManager.baseHardDiskMinCapacity;
                    int maxCapacity = componentManager.baseHardDiskMaxCapacity;
                    int sCapacity = hd.getCapacity();
                    boolean capacityMatch = (sCapacity >= minCapacity && sCapacity <= maxCapacity);
                    return capacityMatch;
                })
                .filter(hd -> {
                    String sType = hd.getType();
                    String bType = componentManager.baseHardDiskType;
                    boolean typeMatch = (bType.equals("Any") || bType.equals(sType));
                    return typeMatch;
                })
                // .filter(hd -> "Any".equals(preferredColor) ||
                // preferredColor.equals(hd.getColor()))
                .collect(Collectors.toList());

        // System.out.println("Hard disks after budget/color filter: " +
        // filteredHardDisks.size());
        //updateTable();
    }

    @Override
    public boolean validateSelection() {
        // selectedHardDisk = getSelectedHardDisk();
        if (getSelectedHardDisk() == null) {
            showError("Please select a storage device");
            return false;
        }

        return true;
    }

    @Override
    public void onSelectionChanged() {
        HardDisk sHardDisk = getSelectedHardDisk();
        if (sHardDisk != null) {
            componentManager.selectedHardDisk = getSelectedHardDisk();
            updateInformationLabel();
        }
    }

    @Override
    public void updateInformationLabel() {
        int minCapacity = componentManager.baseHardDiskMinCapacity;
        int maxCapacity = componentManager.baseHardDiskMaxCapacity;
        String bType = componentManager.baseHardDiskType;
        statusLabel.setText(" Required " + bType + " HardDisk. Capacity: " + minCapacity + "-" + maxCapacity + " GB");
        this.totalCost = componentManager.calculateTotalCost();
        this.costLabel.setText("Total Cost: " + priceFormatter.format(this.totalCost) + " VND");
    }

    @Override
    public void onPressBackButton() {
        componentManager.selectedHardDisk = null;
        this.totalCost = componentManager.calculateTotalCost();
        this.costLabel.setText("Total Cost: " + priceFormatter.format(this.totalCost) + " VND");
    }

}

/*
 * @Override
 * protected void onMainboardChanged() {
 * // No specific mainboard requirements for hard disks
 * System.out.println("Setting mainboard in Step 6: " +
 * (selectedMainboard != null ? selectedMainboard.getName() : "None"));
 * }
 * 
 * 
 * @Override
 * public void filterByBudgetAndColor(double budget, String preferredColor) {
 * super.filterByBudgetAndColor(budget, preferredColor);
 * System.out.println("Hard disks before budget/color filter: " +
 * filteredHardDisks.size());
 * 
 * filteredHardDisks = filteredHardDisks.stream()
 * .filter(hd -> {
 * boolean budgetMatch = hd.getPrice() <= budget;
 * if (!budgetMatch) {
 * System.out.println("Filtered out hard disk " + hd.getName() +
 * " due to budget. Required: " + budget +
 * ", Actual: " + hd.getPrice());
 * }
 * return budgetMatch;
 * })
 * .filter(hd -> "Any".equals(preferredColor) ||
 * preferredColor.equals(hd.getColor()))
 * .collect(Collectors.toList());
 * 
 * System.out.println("Hard disks after budget/color filter: " +
 * filteredHardDisks.size());
 * updateTable();
 * }
 * 
 * try {
 * List<HardDisk> hardDisks = hardDiskDAO.getAll();
 * String selectedManufacturer = (String) manufacturerBox.getSelectedItem();
 * String selectedType = (String) typeBox.getSelectedItem();
 * 
 * System.out.println("Applying filters - Manufacturer: " + selectedManufacturer
 * +
 * ", Type: " + selectedType);
 * 
 * filteredHardDisks = hardDisks.stream()
 * .filter(hd -> {
 * boolean manufacturerMatch = "Any".equals(selectedManufacturer) ||
 * hd.getManufacturer().equals(selectedManufacturer);
 * if (!manufacturerMatch) {
 * System.out.println("Filtered out hard disk " + hd.getName() +
 * " due to manufacturer mismatch");
 * }
 * return manufacturerMatch;
 * })
 * .filter(hd -> {
 * boolean typeMatch = "Any".equals(selectedType) ||
 * hd.getType().equals(selectedType);
 * if (!typeMatch) {
 * System.out.println("Filtered out hard disk " + hd.getName() +
 * " due to type mismatch");
 * }
 * return typeMatch;
 * })
 * .collect(Collectors.toList());
 * 
 * System.out.println("Hard disks after all filters: " +
 * filteredHardDisks.size());
 * updateTable();
 * } catch (SQLException e) {
 * showError("Error applying filters: " + e.getMessage());
 * }
 * }
 */