package com.example.computerPart.ui.wizard;

import com.example.computerPart.dao.GPUDAO;
import com.example.computerPart.model.GPU;
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

public class WizardStep5Panel extends BaseWizardStepPanel {
    private final GPUDAO gpuDAO;
    private List<GPU> filteredGpus;
    private List<GPU> suitableGpus;
    private List<GPU> allGpus;

    // Add manufacturer filter
    private JComboBox<String> manufacturerBox;
    private JComboBox<String> capacityBox;
    // private JLabel gpuSlotsLabel;
    private JLabel statusLabel;
    private JPanel statusPanel;

    public WizardStep5Panel() {
        super("Step 5: GPU Selection");
        // this.requiredGPUs = requiredGPUs;

        gpuDAO = new GPUDAO();
        try {
            allGpus = gpuDAO.getAll();
        } catch (SQLException e) {
            showError("Error loading RAM: " + e.getMessage());
        }

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filters"));

        // GPU Slots info
        // gpuSlotsLabel = new JLabel("GPU Slots: 0");
        // filterPanel.add(gpuSlotsLabel);
        // filterPanel.add(Box.createHorizontalStrut(20));

        // Manufacturer filter
        filterPanel.add(new JLabel("Manufacturer:"));
        manufacturerBox = new JComboBox<>(new String[] { "Any", "NVIDIA", "AMD", "Intel" });
        //manufacturerBox.addActionListener(e -> applyFilters());
        filterPanel.add(manufacturerBox);
        filterPanel.add(Box.createHorizontalStrut(10));

        // Capacity filter
        filterPanel.add(new JLabel("Capacity:"));
        capacityBox = new JComboBox<>(new String[] { "Any", "4", "6", "8", "10", "12", "16", "24", "32" });
        //capacityBox.addActionListener(e -> applyFilters());
        filterPanel.add(capacityBox);

        // Apply Filter button
        JButton applyFilterButton = new JButton("Apply Filters");
        applyFilterButton.addActionListener(e -> applyFilters());
        filterPanel.add(applyFilterButton);
        //Clear Filter button//
        JButton clearFilterButton = new JButton("Clear Filters");
        clearFilterButton.addActionListener(e -> clearFilters());
        filterPanel.add(clearFilterButton);

        add(filterPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {
                "ID", "Name", "Manufacturer", "Stock", "Power(W)", "Color", "Price",
                "Capacity(GB)",
                "Benchmark"
        };
        setupTable(columnNames);

        statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel("Required GPU Number: N/A");
        statusPanel.add(statusLabel);
        addCostPanel();
        JPanel doublePanelContainer = new JPanel();
        doublePanelContainer.setLayout(new GridLayout(2, 1));
        doublePanelContainer.add(statusPanel);
        doublePanelContainer.add(costPanel);
        add(doublePanelContainer, BorderLayout.SOUTH);

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
        filteredGpus = suitableGpus;
        updateTable();
    }

    private GPU getSelectedGpu() {
        int modelRow = getSelectedModelRow();
        if (modelRow == -1) {
            return null;
        }

        int id = (int) tableModel.getValueAt(modelRow, 0);
        return filteredGpus.stream()
                .filter(gpu -> gpu.getId() == id)
                .findFirst()
                .orElse(null);
    }
    private void populateManufacturerOptions() {
        Set<String> uniqueItems = new HashSet<>();
        uniqueItems.add("Any"); // Always include "Any" option

        for (GPU gpu : allGpus) {
            if (gpu.getManufacturer() != null && !gpu.getManufacturer().isEmpty()) {
                uniqueItems.add(gpu.getManufacturer());
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
        // Clear the table model
        selectSuitableComponent();
        String selectedManufacturer = (String) manufacturerBox.getSelectedItem();
        String selectedCapacity = (String) capacityBox.getSelectedItem();
        filteredGpus = suitableGpus.stream()
                .filter(gpu -> {
                    boolean manufacturerMatch = "Any".equals(selectedManufacturer) ||
                            gpu.getManufacturer().equals(selectedManufacturer);
                    if (!manufacturerMatch) {
                        System.out.println("Filtered out GPU " + gpu.getName() +
                                " due to manufacturer mismatch");
                    }
                    return manufacturerMatch;
                })
                .filter(gpu -> {
                    boolean capacityMatch = "Any".equals(selectedCapacity) ||
                            String.valueOf(gpu.getCapacity()).equals(selectedCapacity);
                    if (!capacityMatch) {
                        System.out.println("Filtered out GPU " + gpu.getName() +
                                " due to capacity mismatch");
                    }
                    return capacityMatch;
                })
                .collect(Collectors.toList());
        updateTable();
    }
    @Override
    protected void clearFilters() {
        manufacturerBox.setSelectedItem("Any");
        capacityBox.setSelectedItem("Any");
        applyFilters();
    }

    @Override
    public void updateTable() {
        tableModel.setRowCount(0);

        for (GPU gpu : filteredGpus) {
            tableModel.addRow(new Object[] {
                    gpu.getId(),
                    gpu.getName(),
                    gpu.getManufacturer(),
                    gpu.getStock(),
                    gpu.getPower(),
                    gpu.getColor(),
                    priceFormatter.format(gpu.getPrice()),
                    gpu.getCapacity(),
                    gpu.getBenchmark()

            });
        }
        // System.out.println("Updated table with " + filteredGpus.size() + " GPUs");
    }

    @Override
    public void selectSuitableComponent() {
        if (componentManager.selectedMainboard != null) {
            suitableGpus = allGpus.stream()
                    .filter(gpu -> {
                        int requiredGpuNumber = componentManager.baseGpuNumber;
                        int availableGpuNumber = gpu.getStock();
                        // int availableGpuSlots = componentManager.selectedMainboard.getGpuSlots();
                        double remainingBudget = componentManager.baseBudget - previousStepsTotalCost;
                        double requiredBudget = gpu.getPrice() * requiredGpuNumber;
                        // boolean mainboardMatch = (availableGpuSlots >= requiredGpuNumber);
                        boolean stockMatch = (availableGpuNumber >= requiredGpuNumber);
                        boolean budgetMatch = (requiredBudget <= remainingBudget);
                        if (!budgetMatch) {
                            System.out.println("Filtered out GPU " + gpu.getName() +
                                    " due to budget. Required: " + requiredBudget +
                                    ", Actual: " + remainingBudget);
                        }
                        return budgetMatch && stockMatch;
                    })
                    .filter(gpu -> "Any".equals(componentManager.basePreferredColor)
                            || componentManager.basePreferredColor.equals(gpu.getColor()))
                            .filter(gpu->{
                                int sGpuCapacity = gpu.getCapacity();
                                int rMinGpuCapacity = componentManager.baseGpuMinCapacity;
                                int rMaxGpuCapacity = componentManager.baseGpuMaxCapacity;
                                boolean gpuCapacityMatch = ((rMinGpuCapacity <= sGpuCapacity) && (rMaxGpuCapacity >= sGpuCapacity));
                                return gpuCapacityMatch;
                            })
                    .collect(Collectors.toList());
            // System.out.println("GPUs after budget/color filter: " + filteredGPUs.size());
            //updateTable();
        } else {
            showError("Mainboard not selected");
        }
    }

    @Override
    public boolean validateSelection() {
        if (getSelectedGpu() == null) {
            showError("Please select a GPU");
            return false;
        }
        return true;
    }

    @Override
    public void onSelectionChanged() {
        GPU sGpu = getSelectedGpu();
        if (sGpu != null) {
            componentManager.selectedGpu = sGpu;
            updateInformationLabel();
        }
    }

    @Override
    public void updateInformationLabel() {
        statusLabel.setText(" Required GPU number: " + componentManager.baseGpuNumber);
        this.totalCost = componentManager.calculateTotalCost();
        this.costLabel.setText("Total Cost: " + priceFormatter.format(this.totalCost) + " VND");
    }

    @Override
    public void onPressBackButton() {
        componentManager.selectedGpu = null;
        this.totalCost = componentManager.calculateTotalCost();
        this.costLabel.setText("Total Cost: " + priceFormatter.format(this.totalCost) + " VND");
    }


}

/*
 * lll
 * @Override
 * public void filterByBudgetAndColor(double budget, String preferredColor) {
 * super.filterByBudgetAndColor(budget, preferredColor);
 * System.out.println("GPUs before budget/color filter: " +
 * filteredGPUs.size());
 * 
 * filteredGPUs = filteredGPUs.stream()
 * .filter(gpu -> {
 * boolean budgetMatch = gpu.getPrice() <= budget;
 * if (!budgetMatch) {
 * System.out.println("Filtered out GPU " + gpu.getName() +
 * " due to budget. Required: " + budget +
 * ", Actual: " + gpu.getPrice());
 * }
 * return budgetMatch;
 * })
 * .filter(gpu -> "Any".equals(preferredColor) ||
 * preferredColor.equals(gpu.getColor()))
 * .collect(Collectors.toList());
 * 
 * System.out.println("GPUs after budget/color filter: " + filteredGPUs.size());
 * updateTable();
 * }
 * 
 * protected void updateGpuSlotsLabel() {
 * if (selectedMainboard != null) {
 * System.out.println("Setting mainboard in Step 5: " +
 * selectedMainboard.getName() +
 * " with GPU slots: " + selectedMainboard.getGpuSlots());
 * gpuSlotsLabel.setText("GPU Slots avaiable on mainboard: " +
 * selectedMainboard.getGpuSlots());
 * } else {
 * System.out.println("Warning: Null mainboard set in Step 5");
 * gpuSlotsLabel.setText("GPU Slots: 0");
 * }
 * }
 * 
 * protected void updateStatusLabel() {
 * statusLabel.setText(" Required GPU number: " + this.baseGpuNumber);
 * }
 * 
 * @Override
 * public void filterByBudgetAndColor(double budget, String preferredColor) {
 * super.filterByBudgetAndColor(budget, preferredColor);
 * System.out.println("GPUs before budget/color filter: " +
 * filteredGPUs.size());
 * 
 * filteredGPUs = filteredGPUs.stream()
 * .filter(gpu -> {
 * boolean budgetMatch = gpu.getPrice() <= budget;
 * if (!budgetMatch) {
 * System.out.println("Filtered out GPU " + gpu.getName() +
 * " due to budget. Required: " + budget +
 * ", Actual: " + gpu.getPrice());
 * }
 * return budgetMatch;
 * })
 * .filter(gpu -> "Any".equals(preferredColor) ||
 * preferredColor.equals(gpu.getColor()))
 * .collect(Collectors.toList());
 * 
 * System.out.println("GPUs after budget/color filter: " + filteredGPUs.size());
 * updateTable();
 * }
 */