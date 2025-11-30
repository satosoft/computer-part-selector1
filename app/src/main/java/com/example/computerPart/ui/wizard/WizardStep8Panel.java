package com.example.computerPart.ui.wizard;

import com.example.computerPart.dao.ComputerCaseDAO;
import com.example.computerPart.model.ComputerCase;
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

public class WizardStep8Panel extends BaseWizardStepPanel {
    private final ComputerCaseDAO computerCaseDAO;
    private List<ComputerCase> filteredCases;
    private List<ComputerCase> suitableCases;
    private List<ComputerCase> allCases;

    // Add filters
    private JComboBox<String> manufacturerBox;
    private JComboBox<String> materialBox;
    private JComboBox<String> typeBox;
    // private JLabel caseSizeLabel;
    private JLabel statusLabel;
    private JPanel statusPanel;

    public WizardStep8Panel() {
        super("Step 8: Computer Case Selection");
        computerCaseDAO = new ComputerCaseDAO();
        try {
            allCases = computerCaseDAO.getAll();
        } catch (SQLException e) {
            showError("Error loading hard disks: " + e.getMessage());
        }

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filters"));

        // Case size info
        // caseSizeLabel = new JLabel("Required Size: None");
        // filterPanel.add(caseSizeLabel);
        // filterPanel.add(Box.createHorizontalStrut(20));

        // Manufacturer filter
        filterPanel.add(new JLabel("Manufacturer:"));
        manufacturerBox = new JComboBox<>(
                new String[] { "Any", "Corsair", "NZXT", "Fractal Design", "Cooler Master", "Lian Li", "Thermaltake",
                        "be quiet!" });
        // manufacturerBox.addActionListener(e -> applyFilters());
        filterPanel.add(manufacturerBox);
        filterPanel.add(Box.createHorizontalStrut(10));

        // Material filter
        filterPanel.add(new JLabel("Material:"));
        materialBox = new JComboBox<>(
                new String[] { "Any", "Steel", "Aluminum", "Plastic", "Glass","Acrylic" });
        // materialBox.addActionListener(e -> applyFilters());
        filterPanel.add(materialBox);
        filterPanel.add(Box.createHorizontalStrut(10));

        // Type filter
        filterPanel.add(new JLabel("Type:"));
        typeBox = new JComboBox<>(
                new String[] { "Any", "Mid Tower", "Full Tower", "Mini Tower" });
        // typeBox.addActionListener(e -> applyFilters());
        filterPanel.add(typeBox);

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
                "Size", "Material", "Type"
        };
        setupTable(columnNames);
        statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel("Required case size: ");
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
        filteredCases = suitableCases;
        updateTable();

    }

    private ComputerCase getSelectedComputerCase() {
        int modelRow = getSelectedModelRow();
        if (modelRow == -1) {
            return null;
        }

        int id = (int) tableModel.getValueAt(modelRow, 0);
        ComputerCase sComputerCase = filteredCases.stream()
                .filter(computerCase -> computerCase.getId() == id)
                .findFirst()
                .orElse(null);
        return sComputerCase;
    }
    private void populateManufacturerOptions() {
        Set<String> uniqueItems = new HashSet<>();
        uniqueItems.add("Any"); // Always include "Any" option

        for (ComputerCase com : allCases) {
            if (com.getManufacturer() != null && !com.getManufacturer().isEmpty()) {
                uniqueItems.add(com.getManufacturer());
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
        String selectedMaterial = (String) materialBox.getSelectedItem();
        String selectedType = (String) typeBox.getSelectedItem();
        filteredCases = suitableCases.stream()
                .filter(computerCase -> {
                    boolean manufacturerMatch = "Any".equals(selectedManufacturer) ||
                            computerCase.getManufacturer().equals(selectedManufacturer);
                    if (!manufacturerMatch) {
                        System.out.println("Filtered out case " + computerCase.getName() +
                                " due to manufacturer mismatch");
                    }
                    return manufacturerMatch;
                })
                .filter(computerCase -> {
                    boolean materialMatch = "Any".equals(selectedMaterial) ||
                            computerCase.getCaseMaterial().contains(selectedMaterial);
                    if (!materialMatch) {
                        System.out.println("Filtered out case " + computerCase.getName() +
                                " due to material mismatch");
                    }
                    return materialMatch;
                })
                .filter(computerCase -> {
                    boolean typeMatch = "Any".equals(selectedType) ||
                            computerCase.getCaseType().equals(selectedType);
                    if (!typeMatch) {
                        System.out.println("Filtered out case " + computerCase.getName() +
                                " due to type mismatch");
                    }
                    return typeMatch;
                })
                .collect(Collectors.toList());

        // System.out.println("Cases after all filters: " + filteredCases.size());
        updateTable();

    }
    @Override
    protected void clearFilters() {
        manufacturerBox.setSelectedItem("Any");
        materialBox.setSelectedItem("Any");
        typeBox.setSelectedItem("Any");
        applyFilters();
    }

    @Override
    public void updateTable() {
        tableModel.setRowCount(0);

        for (ComputerCase computerCase : filteredCases) {
            tableModel.addRow(new Object[] {
                    computerCase.getId(),
                    computerCase.getName(),
                    computerCase.getManufacturer(),
                    computerCase.getStock(),
                    computerCase.getPower(),
                    computerCase.getColor(),
                    priceFormatter.format(computerCase.getPrice()),
                    computerCase.getCaseSize(),
                    computerCase.getCaseMaterial(),
                    computerCase.getCaseType()
            });
        }
        // System.out.println("Updated table with " + filteredCases.size() + " computer
        // cases");
    }

    @Override
    public void selectSuitableComponent() {
        if (componentManager.selectedMainboard != null) {
            suitableCases = allCases.stream()
                    .filter(comCase -> {
                        double remainingBudget = componentManager.baseBudget - previousStepsTotalCost;
                        double requiredBudget = comCase.getPrice();
                        boolean budgetMatch = (requiredBudget <= remainingBudget);
                        return budgetMatch;
                    })
                    .filter(comCase -> {
                        String bColor = componentManager.basePreferredColor;
                        boolean colorMatch = "Any".equals(bColor)
                                || bColor.equals(comCase.getColor());
                        return colorMatch;
                    })
                    .filter(comCase -> {
                        boolean sizeMatch = (componentManager.selectedMainboard.getMainSize())
                                .equals(comCase.getCaseSize());
                        return sizeMatch;
                    })
                    .filter(comCase -> comCase.getStock() > 0)
                    .collect(Collectors.toList());
            //updateTable();
        } else {
            showError("Mainboard not selected");
        }
    }

    @Override
    public boolean validateSelection() {
        ComputerCase sComputerCase = getSelectedComputerCase();
        if (sComputerCase == null) {
            showError("Please select a computer case");
            return false;
        }
        return true;
    }

    @Override
    public void onSelectionChanged() {
        ComputerCase sComputerCase = getSelectedComputerCase();
        if (sComputerCase != null) {
            componentManager.selectedComputerCase = sComputerCase;
            updateInformationLabel();
        }
    }

    @Override
    public void updateInformationLabel() {
        if (componentManager.selectedMainboard != null) {
            String rSize = componentManager.selectedMainboard.getMainSize();
            statusLabel.setText(" Required case size: " + rSize);
        } else {
            statusLabel.setText(" Required case size: ");
        }
        this.totalCost = componentManager.calculateTotalCost();
        this.costLabel.setText("Total Cost: " + priceFormatter.format(this.totalCost) + " VND");
    }

    @Override
    public void onPressBackButton() {
        componentManager.selectedComputerCase = null;
        this.totalCost = componentManager.calculateTotalCost();
        this.costLabel.setText("Total Cost: " + priceFormatter.format(this.totalCost) + " VND");
    }

}

/*
 * @Override
 * protected double calculatePreviousCost() {
 * return (selectedMainboard != null ? selectedMainboard.getPrice() : 0)
 * + (selectedCpu != null ? selectedCpu.getPrice() * baseCpuNumber : 0)
 * + (selectedRam != null ? (selectedRam.getPrice() * selectedRamNumber) : 0)
 * + (selectedGpu != null ? selectedGpu.getPrice() : 0)
 * + (selectedHardDisk != null ? selectedHardDisk.getPrice() : 0)
 * + (selectedCooler != null ? selectedCooler.getPrice() : 0);
 * // (selectedComputerCase != null ? selectedComputerCase.getPrice() : 0) +
 * // (selectedPowerSource != null ? selectedPowerSource.getPrice() : 0);
 * }
 * 
 * @Override
 * protected double calculateTotalCost() {
 * return (selectedMainboard != null ? selectedMainboard.getPrice() : 0)
 * + (selectedCpu != null ? (selectedCpu.getPrice() * baseCpuNumber) : 0)
 * + (selectedRam != null ? (selectedRam.getPrice() * selectedRamNumber) : 0)
 * + (selectedGpu != null ? (selectedGpu.getPrice() * baseGpuNumber) : 0)
 * + (selectedHardDisk != null ? selectedHardDisk.getPrice() : 0)
 * + (selectedCooler != null ? selectedCooler.getPrice() : 0)
 * + (selectedComputerCase != null ? selectedComputerCase.getPrice() : 0);
 * // +(selectedPowerSource != null ? selectedPowerSource.getPrice() : 0);
 * }
 * 
 * @Override
 * protected void onMainboardChanged() {
 * if (selectedMainboard != null) {
 * caseSizeLabel.setText("Required Size: " + selectedMainboard.getMainSize());
 * System.out.println("Setting mainboard in Step 8: " +
 * selectedMainboard.getName() +
 * " with size: " + selectedMainboard.getMainSize());
 * 
 * // Refresh components to apply size filtering immediately
 * refreshComponents();
 * } else {
 * caseSizeLabel.setText("Required Size: None");
 * System.out.println("No mainboard selected in Step 8");
 * refreshComponents();
 * }
 * }
 * 
 * 
 * 
 * @Override
 * protected void refreshComponents() {
 * try {
 * System.out.println("Loading all computer cases");
 * filteredCases = computerCaseDAO.getAll();
 * System.out.println("Total computer cases loaded: " + filteredCases.size());
 * 
 * // Apply size filtering if a mainboard is selected
 * if (selectedMainboard != null) {
 * String requiredSize = selectedMainboard.getMainSize();
 * System.out.println("Filtering cases by required size: " + requiredSize);
 * 
 * filteredCases = filteredCases.stream()
 * .filter(computerCase -> {
 * boolean sizeMatch = computerCase.getCaseSize().equals(requiredSize);
 * if (!sizeMatch) {
 * System.out.println("Filtered out case " + computerCase.getName() +
 * " due to size incompatibility. Required: " + requiredSize +
 * ", Actual: " + computerCase.getCaseSize());
 * }
 * return sizeMatch;
 * })
 * .collect(Collectors.toList());
 * 
 * System.out.println("Cases after size filtering: " + filteredCases.size());
 * }
 * 
 * updateTable();
 * } catch (SQLException e) {
 * showError("Error loading computer cases: " + e.getMessage());
 * }
 * }
 * 
 * private void applyFilters() {
 * selectSuitableComponent();
 * String selectedManufacturer = (String) manufacturerBox.getSelectedItem();
 * String selectedMaterial = (String) materialBox.getSelectedItem();
 * String selectedType = (String) typeBox.getSelectedItem();
 * filteredCases = suitableCases.stream()
 * .filter(computerCase -> {
 * boolean manufacturerMatch = "Any".equals(selectedManufacturer) ||
 * computerCase.getManufacturer().equals(selectedManufacturer);
 * if (!manufacturerMatch) {
 * System.out.println("Filtered out case " + computerCase.getName() +
 * " due to manufacturer mismatch");
 * }
 * return manufacturerMatch;
 * })
 * .filter(computerCase -> {
 * boolean materialMatch = "Any".equals(selectedMaterial) ||
 * computerCase.getCaseMaterial().equals(selectedMaterial);
 * if (!materialMatch) {
 * System.out.println("Filtered out case " + computerCase.getName() +
 * " due to material mismatch");
 * }
 * return materialMatch;
 * })
 * .filter(computerCase -> {
 * boolean typeMatch = "Any".equals(selectedType) ||
 * computerCase.getCaseType().equals(selectedType);
 * if (!typeMatch) {
 * System.out.println("Filtered out case " + computerCase.getName() +
 * " due to type mismatch");
 * }
 * return typeMatch;
 * })
 * .filter(computerCase -> {
 * // Filter by size compatibility with mainboard
 * if (selectedMainboard == null) {
 * return true; // No mainboard selected, show all cases
 * }
 * 
 * String requiredSize = selectedMainboard.getMainSize();
 * boolean sizeMatch = computerCase.getCaseSize().equals(requiredSize);
 * 
 * if (!sizeMatch) {
 * System.out.println("Filtered out case " + computerCase.getName() +
 * " due to size incompatibility. Required: " + requiredSize +
 * ", Actual: " + computerCase.getCaseSize());
 * } else {
 * System.out.println("Case " + computerCase.getName() +
 * " is compatible with mainboard size " + requiredSize);
 * }
 * return sizeMatch;
 * })
 * .collect(Collectors.toList());
 * 
 * System.out.println("Cases after all filters: " + filteredCases.size());
 * updateTable();
 * 
 * }
 * 
 * 
 * @Override
 * public void filterByBudgetAndColor(double budget, String preferredColor) {
 * super.filterByBudgetAndColor(budget, preferredColor);
 * System.out.println("Cases before budget/color filter: " +
 * filteredCases.size());
 * 
 * filteredCases = filteredCases.stream()
 * .filter(computerCase -> {
 * boolean budgetMatch = computerCase.getPrice() <= budget;
 * if (!budgetMatch) {
 * System.out.println("Filtered out case " + computerCase.getName() +
 * " due to budget. Required: " + budget +
 * ", Actual: " + computerCase.getPrice());
 * }
 * return budgetMatch;
 * })
 * .filter(computerCase -> {
 * boolean colorMatch = "Any".equals(preferredColor) ||
 * preferredColor.equals(computerCase.getColor());
 * if (!colorMatch && !"Any".equals(preferredColor)) {
 * System.out.println("Filtered out case " + computerCase.getName() +
 * " due to color mismatch. Required: " + preferredColor +
 * ", Actual: " + computerCase.getColor());
 * }
 * return colorMatch;
 * })
 * .collect(Collectors.toList());
 * 
 * System.out.println("Cases after budget/color filter: " +
 * filteredCases.size());
 * updateTable();
 * }
 */