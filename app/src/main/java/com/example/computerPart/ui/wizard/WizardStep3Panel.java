package com.example.computerPart.ui.wizard;

import com.example.computerPart.dao.CPUDAO;
import com.example.computerPart.model.BaseComputerPart;
import com.example.computerPart.model.CPU;
import com.example.computerPart.model.Mainboard;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class WizardStep3Panel extends BaseWizardStepPanel {
    private final CPUDAO cpuDAO;
    private final JComboBox<String> manufacturerBox;
    private List<CPU> filteredCpus;
    private List<CPU> suitableCpus;
    private List<CPU> allCpus;
    // private List<BaseComputerPart> allComponents;
    // private CPU selectedCpu;
    // private JLabel socketLabel;
    // private JPanel socketPanel;
    private double previousStepsTotalCost;
    private JLabel statusLabel;
    private JPanel statusPanel;

    public WizardStep3Panel() {
        super("Step 3: CPU Selection");
        cpuDAO = new CPUDAO();
        try {
            allCpus = cpuDAO.getAll();
        } catch (SQLException e) {
            showError("Error loading CPU: " + e.getMessage());
        }

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filters"));

        // Manufacturer filter
        filterPanel.add(new JLabel("Manufacturer:"));
        manufacturerBox = new JComboBox<>(new String[] { "Any", "Intel", "AMD" });
        //manufacturerBox.addActionListener(e -> applyFilters());
        filterPanel.add(manufacturerBox);

        // Apply Filter button, filter panel at top
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
                "Generation", "Socket","Benchmark", "Core Number"
        };
        setupTable(columnNames);

        // Status panel and cost panel at bottom
        statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel("Required CPUs: N/A" );
        statusPanel.add(statusLabel);

        addCostPanel();
        JPanel doublePanelContainer = new JPanel();
        doublePanelContainer.setLayout(new GridLayout(2, 1));
        doublePanelContainer.add(statusPanel);
        doublePanelContainer.add(costPanel);
        add(doublePanelContainer, BorderLayout.SOUTH);
        //refreshComponents();
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
            filteredCpus = suitableCpus;
            updateTable();
    }
    private CPU getselectedCpu() {
        int modelRow = getSelectedModelRow();
        if (modelRow == -1) {
            return null;
        }

        int id = (int) tableModel.getValueAt(modelRow, 0);
        return filteredCpus.stream()
                .filter(cpu -> cpu.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    protected void applyFilters() {
            selectSuitableComponent();
            String selectedManufacturer = (String) manufacturerBox.getSelectedItem();
            filteredCpus = suitableCpus.stream()
                    .filter(cpu -> {
                        boolean manufacturerMatch = "Any".equals(selectedManufacturer) ||
                                cpu.getManufacturer().equals(selectedManufacturer);
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
        for (CPU cpu : filteredCpus) {
            tableModel.addRow(new Object[] {
                    cpu.getId(),
                    cpu.getName(),
                    cpu.getManufacturer(),
                    cpu.getStock(),
                    cpu.getPower(),
                    cpu.getColor(),
                    priceFormatter.format(cpu.getPrice()),
                    cpu.getGeneration(),
                    cpu.getSocket(),
                    cpu.getBenchmark(),
                    cpu.getCoreNum(),

            });
        }
    }

    @Override
    public void selectSuitableComponent() {
        if (componentManager.selectedMainboard != null) {
            suitableCpus = allCpus.stream()
                    .filter(cpu -> {
                        double remainingBudget = componentManager.baseBudget - previousStepsTotalCost;
                        double requiredBudget = cpu.getPrice() * componentManager.baseCpuNumber;
                        boolean budgetMatch = (requiredBudget <= remainingBudget);
                        /* 
                        if (!budgetMatch) {
                            System.out.println("Filtered out CPU " + cpu.getName() +
                                    " due to budget. Remain: " + remainingBudget +
                                    ", Required: " + requiredBudget);
                        }
                        */
                        return budgetMatch;
                    })
                    .filter(cpu -> cpu.getSocket().equals(componentManager.selectedMainboard.getCpuSocket()))
                    .filter(cpu -> cpu.getStock() >= componentManager.baseCpuNumber)
                    .collect(Collectors.toList());
            //updateTable();
        }else {
            showError("Mainboard not selected");
            
        }
    }

    @Override
    protected boolean validateSelection() {
        if (getselectedCpu() == null) {
            showError("Please select a CPU");
            return false;
        }
        /* 
        CPU sCpu = getselectedCpu();
        if (sCpu.getStock() < componentManager.baseCpuNumber) {
            showError(String.format("Not enough CPUs in stock. Need %d, but only %d available.",
                    componentManager.baseCpuNumber, sCpu.getStock()));
            return false;
        }
        */

        return true;
    }

    @Override
    public void onSelectionChanged() {
        if (getselectedCpu() != null) {
            componentManager.selectedCpu = getselectedCpu();
            updateInformationLabel();
        }
    }

    @Override
    protected void updateInformationLabel() {
        if (componentManager.selectedMainboard != null) {
            statusLabel.setText("Socket: " + componentManager.selectedMainboard.getCpuSocket()
                    + ". Required CPU number: " + componentManager.baseCpuNumber);
        } else {
            statusLabel.setText("Socket: N/A");
        }
        // previousStepsTotalCost = componentManager.calculateTotalCost();
        this.totalCost = componentManager.calculateTotalCost();
        this.costLabel.setText("Total Cost: " + priceFormatter.format(this.totalCost) + " VND");
    }

    @Override
    public void onPressBackButton() {
        componentManager.selectedCpu = null;
        this.totalCost = componentManager.calculateTotalCost();
        this.costLabel.setText("Total Cost: " + priceFormatter.format(this.totalCost) + " VND");
    }


    

}

/*
 * // Socket info
 * //JLabel socketLabel = new JLabel(
 * // "Selected Socket: " + (componentManager.selectedMainboard != null
 * // ? componentManager.selectedMainboard.getCpuSocket()
 * // : "Null"));
 * //filterPanel.add(socketLabel);
 * //filterPanel.add(Box.createHorizontalStrut(20));
 * 
 * @Override
 * protected void setupSorting() {
 * sorter = new TableRowSorter<>(tableModel);
 * componentTable.setRowSorter(sorter);
 * componentTable.getSelectionModel().addListSelectionListener(e -> {
 * if (!e.getValueIsAdjusting()) {
 * onSelectionChanged();
 * }
 * });
 * 
 * // Price column needs special handling because it's formatted with commas
 * sorter.setComparator(6, new Comparator<String>() {
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