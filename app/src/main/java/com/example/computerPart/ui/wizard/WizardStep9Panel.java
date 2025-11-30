package com.example.computerPart.ui.wizard;

import com.example.computerPart.dao.ComputerCaseDAO;
import com.example.computerPart.dao.PowerSourceDAO;
import com.example.computerPart.model.ComputerCase;
import com.example.computerPart.model.PowerSource;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class WizardStep9Panel extends BaseWizardStepPanel {
    private final PowerSourceDAO powerSourceDAO;
    private List<PowerSource> filteredPowerSources;
    private List<PowerSource> suitablePowerSources;
    private List<PowerSource> allPowerSources;

    // private int requiredPower = 0;

    // Add filters
    private JComboBox<String> manufacturerBox;
    // private JLabel requiredPowerLabel;
    private JLabel statusLabel;
    private JPanel statusPanel;

    public WizardStep9Panel() {
        super("Step 9: Power Source Selection");
        powerSourceDAO = new PowerSourceDAO();
        try {
            allPowerSources = powerSourceDAO.getAll();
        } catch (SQLException e) {
            showError("Error loading hard disks: " + e.getMessage());
        }

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filters"));

        // Required power info
        // requiredPowerLabel = new JLabel("Required Power: 0W");
        // filterPanel.add(requiredPowerLabel);
        // filterPanel.add(Box.createHorizontalStrut(20));

        // Manufacturer filter
        filterPanel.add(new JLabel("Manufacturer:"));
        manufacturerBox = new JComboBox<>(
                new String[] { "Any", "Corsair", "EVGA", "Seasonic", "be quiet!", "Thermaltake", "Cooler Master" });
        // manufacturerBox.addActionListener(e -> applyFilters());
        filterPanel.add(manufacturerBox);

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
                "ID", "Name", "Manufacturer", "Stock", "Power(W)", "Color", "Price"
        };
        setupTable(columnNames);
        statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel("Required min power : ");
        statusPanel.add(statusLabel);
        addCostPanel();
        JPanel doublePanelContainer = new JPanel();
        doublePanelContainer.setLayout(new GridLayout(2, 1));
        doublePanelContainer.add(statusPanel);
        doublePanelContainer.add(costPanel);
        add(doublePanelContainer, BorderLayout.SOUTH);

        // Set up numeric column comparators
        // setNumericComparator(0); // ID
        // setNumericComparator(4); // Power
        // setPriceComparator(5); // Price
        // setNumericComparator(6); // Stock

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
        filteredPowerSources = suitablePowerSources;
        updateTable();
    }

    private PowerSource getSelectedPowerSource() {
        int modelRow = getSelectedModelRow();
        if (modelRow == -1) {
            return null;
        }

        int id = (int) tableModel.getValueAt(modelRow, 0);
        PowerSource sPowerSource = filteredPowerSources.stream()
                .filter(powerSource -> powerSource.getId() == id)
                .findFirst()
                .orElse(null);
        return sPowerSource;
    }
    private void populateManufacturerOptions() {
        Set<String> uniqueItems = new HashSet<>();
        uniqueItems.add("Any"); // Always include "Any" option
        for (PowerSource ps : allPowerSources) {
            if (ps.getManufacturer() != null && !ps.getManufacturer().isEmpty()) {
                uniqueItems.add(ps.getManufacturer());
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
        filteredPowerSources = suitablePowerSources.stream()
                .filter(powerSource -> {
                    boolean manufacturerMatch = "Any".equals(selectedManufacturer) ||
                            powerSource.getManufacturer().equals(selectedManufacturer);   
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
        for (PowerSource powerSource : filteredPowerSources) {
            tableModel.addRow(new Object[] {
                    powerSource.getId(),
                    powerSource.getName(),
                    powerSource.getManufacturer(),
                    powerSource.getStock(),
                    powerSource.getPower(),
                    powerSource.getColor(),
                    priceFormatter.format(powerSource.getPrice())
            });
        }
        // System.out.println("Updated table with " + filteredPowerSources.size() + "
        // power sources");
    }

    @Override
    public void selectSuitableComponent() {
        suitablePowerSources = allPowerSources.stream()
                .filter(pSource -> {
                    double remainingBudget = componentManager.baseBudget - previousStepsTotalCost;
                    double requiredBudget = pSource.getPrice();
                    boolean budgetMatch = (requiredBudget <= remainingBudget);
                    return budgetMatch;
                })
                .filter(pSource -> {
                    String bColor = componentManager.basePreferredColor;
                    boolean colorMatch = "Any".equals(bColor)
                            || bColor.equals(pSource.getColor());
                    return colorMatch;
                })
                .filter(pSource -> pSource.getStock() > 0)
                .filter(pSource -> {
                    int rPower = componentManager.calculateTotalPower();
                    int sPower = pSource.getPower();
                    boolean powerMatch = (sPower >= rPower);
                    return powerMatch;
                })
                .collect(Collectors.toList());
        //updateTable();
    }

    @Override
    public boolean validateSelection() {
        if (getSelectedPowerSource() == null) {
            showError("Please select a power source");
            return false;
        }
        return true;
    }

    @Override
    public void onSelectionChanged() {
        PowerSource sPowerSource = getSelectedPowerSource();
        if (sPowerSource != null) {
            componentManager.selectedPowerSource = sPowerSource;
            updateInformationLabel();
            // double psCost = selectedPowerSource.getPrice();
            // setTotalCost(psCost);
        }
    }

    @Override
    public void updateInformationLabel() {
        statusLabel.setText(" Required power: " + componentManager.calculateTotalPower());
        this.totalCost = componentManager.calculateTotalCost();
        this.costLabel.setText("Total Cost: " + priceFormatter.format(this.totalCost) + " VND");
    }

    @Override
    public void onPressBackButton() {
        componentManager.selectedPowerSource = null;
        this.totalCost = componentManager.calculateTotalCost();
        this.costLabel.setText("Total Cost: " + priceFormatter.format(this.totalCost) + " VND");
    }

}

