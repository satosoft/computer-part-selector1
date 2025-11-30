package com.example.computerPart.ui.wizard;

import com.example.computerPart.dao.RAMDAO;
import com.example.computerPart.model.RAM;
import com.example.computerPart.model.Mainboard;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;//

public class WizardStep4Panel extends BaseWizardStepPanel {
    private final RAMDAO ramDAO;
    private List<RAM> filteredRams;
    private List<RAM> suitableRams;
    private List<RAM> allRams;
    // Add manufacturer filter
    private JComboBox<String> manufacturerBox;
    // private JLabel ramBusLabel;
    // private JPanel ramBusPanel;
    private JLabel statusLabel;
    private JPanel statusPanel;

    public WizardStep4Panel() {
        super("Step 4: RAM Selection");
        // this.ramCombinations = new HashMap<>();
        ramDAO = new RAMDAO();

        try {
            allRams = ramDAO.getAll();
        } catch (SQLException e) {
            showError("Error loading RAM: " + e.getMessage());
        }

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filters"));

        // RAM Bus info
        // ramBusLabel = new JLabel("RAM Type: None");
        // filterPanel.add(ramBusLabel);
        // filterPanel.add(Box.createHorizontalStrut(20));

        // Manufacturer filter
        filterPanel.add(new JLabel("Manufacturer:"));
        manufacturerBox = new JComboBox<>(new String[] { "Any" });
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
                "ID", "Name", "Manufacturer", "Stock", "Power(W)", "Color", "Price",
                "Type", "Capacity(GB)", "Bus(MHz)", "Quantity Needed"
        };
        setupTable(columnNames);

        // Status and cost panel at bottom
        statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel("Required RAM: ");
        statusPanel.add(statusLabel);
        addCostPanel();
        JPanel doublePanelContainer = new JPanel();
        doublePanelContainer.setLayout(new GridLayout(2, 1));
        doublePanelContainer.add(statusPanel);
        doublePanelContainer.add(costPanel);
        add(doublePanelContainer, BorderLayout.SOUTH);

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
        filteredRams = suitableRams;
        updateTable();
    }

    private RAM getSelectedRam() {
        int modelRow = getSelectedModelRow();
        if (modelRow == -1) {
            return null;
        }

        int id = (Integer) tableModel.getValueAt(modelRow, 0);
        return filteredRams.stream()
                .filter(ram -> ram.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private void populateManufacturerOptions() {
        Set<String> uniqueItems = new HashSet<>();
        uniqueItems.add("Any"); // Always include "Any" option

        for (RAM ram : allRams) {
            if (ram.getManufacturer() != null && !ram.getManufacturer().isEmpty()) {
                uniqueItems.add(ram.getManufacturer());
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
        filteredRams = suitableRams.stream()
                .filter(ram -> {
                    boolean manufacturerMatch = "Any".equals(selectedManufacturer) ||
                            ram.getManufacturer().equals(selectedManufacturer);
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
        for (RAM ram : filteredRams) {
            int quantityNeeded = componentManager.baseRamCapacity / ram.getCapacity();
            tableModel.addRow(new Object[] {
                    ram.getId(),
                    ram.getName(),
                    ram.getManufacturer(),
                    ram.getStock(),
                    ram.getPower(),
                    ram.getColor(),
                    priceFormatter.format(ram.getPrice()),

                    ram.getRamType(),
                    ram.getCapacity(),
                    ram.getBus(),

                    quantityNeeded
            });
        }
    }

    @Override
    public void selectSuitableComponent() {
        if (componentManager.selectedMainboard != null) {
            suitableRams = allRams.stream()
                    .filter(ram -> {
                        // Calculate how many modules we need
                        int bRamCapacity = componentManager.baseRamCapacity;
                        int sRamCapacity = ram.getCapacity();
                        int availableSlots = componentManager.selectedMainboard.getRamSlots();
                        int availableStock = ram.getStock();
                        int neededModules = (int) Math.ceil((double) (bRamCapacity) / sRamCapacity);
                        String sRamType = ram.getRamType();
                        String rRamType = componentManager.selectedMainboard.getRamType();
                        double remainingBudget = componentManager.baseBudget - previousStepsTotalCost;
                        double requiredBudget = ram.getPrice() * neededModules;
                        boolean suitableWithMainboard = (availableSlots >= neededModules);
                        boolean suitableRamType = (sRamType.equals(rRamType));
                        boolean suitableCapacity = ((bRamCapacity % sRamCapacity) == 0);
                        boolean suitableStock = (availableStock >= neededModules);
                        boolean budgetMatch = (remainingBudget >= requiredBudget);
                        return budgetMatch && suitableWithMainboard && suitableRamType && suitableCapacity
                                && suitableStock;
                    })
                    .filter(ram -> "Any".equals(componentManager.basePreferredColor)
                            || componentManager.basePreferredColor.equals(ram.getColor()))
                    .collect(Collectors.toList());
            // filteredRams = suitableRams;
            // updateTable();
        } else {
            showError("Mainboard not selected");

        }
    }

    @Override
    public boolean validateSelection() {
        if (getSelectedRam() == null) {
            showError("Please select a RAM module");
            return false;
        }
        return true;
    }

    @Override
    public void onSelectionChanged() {
        RAM sRam = getSelectedRam();
        if (sRam != null) {
            componentManager.selectedRam = sRam;
            componentManager.baseRamNumber = componentManager.baseRamCapacity / sRam.getCapacity();
            updateInformationLabel();
        }
    }

    @Override
    protected void updateInformationLabel() {
        Mainboard sMb = componentManager.selectedMainboard;
        RAM sRam = componentManager.selectedRam;
        int nRam = componentManager.baseRamNumber;
        int tRam = componentManager.baseRamCapacity;

        if (sMb != null && sRam != null) {
            statusLabel.setText(
                    "Required: " + tRam + "GB " + sMb.getRamType() + ". Selected: " + sRam.getName() + " x" + nRam);
        } else if (sRam == null) {
            statusLabel.setText("Required: " + tRam + " " + sMb.getRamType());
        } else if (sMb == null) {
            statusLabel.setText("Required: N/A");
        }

        this.totalCost = componentManager.calculateTotalCost();
        this.costLabel.setText("Total Cost: " + priceFormatter.format(this.totalCost) + " VND");
    }

    @Override
    public void onPressBackButton() {
        componentManager.selectedRam = null;
        componentManager.baseRamNumber = 0;
        this.totalCost = componentManager.calculateTotalCost();
        this.costLabel.setText("Total Cost: " + priceFormatter.format(this.totalCost) + " VND");
    }
}
