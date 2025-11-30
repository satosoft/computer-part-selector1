package com.example.computerPart.ui.wizard;

import com.example.computerPart.dao.CoolerDAO;
import com.example.computerPart.dao.HardDiskDAO;
import com.example.computerPart.model.Cooler;
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

public class WizardStep7Panel extends BaseWizardStepPanel {
    private final CoolerDAO coolerDAO;
    private List<Cooler> filteredCoolers;
    private List<Cooler> suitableCoolers;
    private List<Cooler> allCoolers;

    // Add manufacturer filter
    private JComboBox<String> manufacturerBox;
    private JComboBox<String> coolerTypeBox;
    private JLabel statusLabel;
    private JPanel statusPanel;
    // private JLabel socketLabel;

    public WizardStep7Panel() {
        super("Step 7: CPU Cooler Selection");

        coolerDAO = new CoolerDAO();
        try {
            allCoolers = coolerDAO.getAll();
        } catch (SQLException e) {
            showError("Error loading hard disks: " + e.getMessage());
        }

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filters"));

        // Socket requirement info
        // socketLabel = new JLabel("Required Socket: None");
        // filterPanel.add(socketLabel);
        // filterPanel.add(Box.createHorizontalStrut(20));

        // Manufacturer filter
        filterPanel.add(new JLabel("Manufacturer:"));
        manufacturerBox = new JComboBox<>(
                new String[] { "Any", "Noctua", "Corsair", "Cooler Master", "be quiet!", "NZXT", "DeepCool" });
        // manufacturerBox.addActionListener(e -> applyFilters());
        filterPanel.add(manufacturerBox);
        filterPanel.add(Box.createHorizontalStrut(10));

        // Cooler Type filter
        filterPanel.add(new JLabel("Cooler Type:"));
        coolerTypeBox = new JComboBox<>(
                new String[] { "Any", "Air", "AIO" });
        // coolerTypeBox.addActionListener(e -> applyFilters());
        filterPanel.add(coolerTypeBox);

        // Apply Filter button
        JButton applyFilterButton = new JButton("Apply Filters");
        applyFilterButton.addActionListener(e -> applyFilters());
        filterPanel.add(applyFilterButton);
        // Clear filter button//
        JButton clearFilterButton = new JButton("Clear Filters");
        clearFilterButton.addActionListener(e -> clearFilters());
        filterPanel.add(clearFilterButton);

        add(filterPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {
                "ID", "Name", "Manufacturer", "Stock", "Power (W)", "Color", "Price",
                "Type", "Socket Support", "LED Color"
        };
        setupTable(columnNames);
        statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel("Required socket: N/A");
        statusPanel.add(statusLabel);
        addCostPanel();
        JPanel doublePanelContainer = new JPanel();
        doublePanelContainer.setLayout(new GridLayout(2, 1));
        doublePanelContainer.add(statusPanel);
        doublePanelContainer.add(costPanel);
        add(doublePanelContainer, BorderLayout.SOUTH);
        // Set up numeric column comparators
        // setNumericComparator(0); // ID
        // setNumericComparator(7); // Power
        // setPriceComparator(8); // Price
        // setNumericComparator(9); // Stock
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
        filteredCoolers = suitableCoolers;
        updateTable();
    }

    private Cooler getSelectedCooler() {
        int modelRow = getSelectedModelRow();
        if (modelRow == -1) {
            return null;
        }

        int id = (int) tableModel.getValueAt(modelRow, 0);
        return filteredCoolers.stream()
                .filter(cooler -> cooler.getId() == id)
                .findFirst()
                .orElse(null);
        // return selectedCooler;
    }

    private void populateManufacturerOptions() {
        Set<String> uniqueItems = new HashSet<>();
        uniqueItems.add("Any"); // Always include "Any" option

        for (Cooler co : allCoolers) {
            if (co.getManufacturer() != null && !co.getManufacturer().isEmpty()) {
                uniqueItems.add(co.getManufacturer());
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

        // List<Cooler> coolers = coolerDAO.getAll();
        selectSuitableComponent();
        String selectedManufacturer = (String) manufacturerBox.getSelectedItem();
        String selectedCoolerType = (String) coolerTypeBox.getSelectedItem();
        filteredCoolers = suitableCoolers.stream()
                .filter(cooler -> {
                    boolean manufacturerMatch = "Any".equals(selectedManufacturer) ||
                            cooler.getManufacturer().equals(selectedManufacturer);
                    return manufacturerMatch;
                })
                .filter(cooler -> {
                    boolean typeMatch = "Any".equals(selectedCoolerType) ||
                            cooler.getCoolerType().equals(selectedCoolerType);
                    return typeMatch;
                })
                .collect(Collectors.toList());

        // System.out.println("Coolers after all filters: " + filteredCoolers.size());
        updateTable();

    }

    @Override
    protected void clearFilters() {
        manufacturerBox.setSelectedItem("Any");
        coolerTypeBox.setSelectedItem("Any");
        applyFilters();
    }

    @Override
    public void updateTable() {
        tableModel.setRowCount(0);

        for (Cooler cooler : filteredCoolers) {
            tableModel.addRow(new Object[] {
                    cooler.getId(),
                    cooler.getName(),
                    cooler.getManufacturer(),
                    cooler.getStock(),
                    cooler.getPower(),
                    cooler.getColor(),
                    priceFormatter.format(cooler.getPrice()),
                    cooler.getCoolerType(),
                    String.join(", ", cooler.getSocketSupport()),
                    cooler.getLedColor() != null ? cooler.getLedColor() : "None"

            });
        }
        // System.out.println("Updated table with " + filteredCoolers.size() + "
        // coolers");
    }

    @Override
    public void selectSuitableComponent() {
        // super.filterByBudgetAndColor(budget, preferredColor);
        // System.out.println("Coolers before budget/color filter: " +
        // filteredCoolers.size());//
        if (componentManager.selectedMainboard != null) {
            suitableCoolers = allCoolers.stream()
                    .filter(cooler -> {
                        double remainingBudget = componentManager.baseBudget - previousStepsTotalCost;
                        double requiredBudget = cooler.getPrice() * componentManager.baseCpuNumber;
                        boolean budgetMatch = (requiredBudget <= remainingBudget);
                        if (!budgetMatch) {
                            System.out.println("Filtered out hard disk " + cooler.getName() +
                                    " due to budget. Required: " + requiredBudget +
                                    ", Remaining: " + remainingBudget);
                        }
                        return budgetMatch;
                    })
                    .filter(cooler -> {
                        String bColor = componentManager.basePreferredColor;
                        boolean colorMatch = "Any".equals(bColor)
                                || bColor.equals(cooler.getColor());
                        return colorMatch;
                    })
                    .filter(cooler -> {
                        String requiredSocket = componentManager.selectedMainboard.getCpuSocket();
                        boolean isCompatible = cooler.getSocketSupport()
                                .stream()
                                .map(String::trim)
                                .anyMatch(sk -> sk.equals(requiredSocket));
                        return isCompatible;
                    })
                    .filter(cooler -> (cooler.getStock() > componentManager.baseCpuNumber))
                    .collect(Collectors.toList());

            // System.out.println("Coolers after budget/color filter: " +
            // filteredCoolers.size());
            // updateTable();
        } else {
            showError("Mainboard not selected");

        }
    }

    @Override
    public boolean validateSelection() {
        if (getSelectedCooler() == null) {
            showError("Please select a CPU cooler");
            return false;
        }
        return true;
    }

    @Override
    public void onSelectionChanged() {
        Cooler sCooler = getSelectedCooler();
        if (sCooler != null) {
            componentManager.selectedCooler = sCooler;
            updateInformationLabel();
        }
    }

    @Override
    public void updateInformationLabel() {
        if (componentManager.selectedMainboard != null) {
            String rSocket = componentManager.selectedMainboard.getCpuSocket();
            statusLabel.setText(" Required socket:" + rSocket);
        } else {
            statusLabel.setText("Required socket: N/A");
        }

        this.totalCost = componentManager.calculateTotalCost();
        this.costLabel.setText("Total Cost: " + priceFormatter.format(this.totalCost) + " VND");
    }

    @Override
    public void onPressBackButton() {
        componentManager.selectedCooler = null;
        this.totalCost = componentManager.calculateTotalCost();
        this.costLabel.setText("Total Cost: " + priceFormatter.format(this.totalCost) + " VND");
    }
}

/*
 * 
 * @Override//
 * public void filterByBudgetAndColor(double budget, String preferredColor) {
 * super.filterByBudgetAndColor(budget, preferredColor);
 * System.out.println("Coolers before budget/color filter: " +
 * filteredCoolers.size());
 * 
 * filteredCoolers = filteredCoolers.stream()
 * .filter(cooler -> {
 * boolean budgetMatch = cooler.getPrice() <= budget;
 * if (!budgetMatch) {
 * System.out.println("Filtered out cooler " + cooler.getName() +
 * " due to budget. Required: " + budget +
 * ", Actual: " + cooler.getPrice());
 * }
 * return budgetMatch;
 * })
 * .filter(cooler -> {
 * boolean colorMatch = "Any".equals(preferredColor) ||
 * preferredColor.equals(cooler.getColor());
 * if (!colorMatch && !"Any".equals(preferredColor)) {
 * System.out.println("Filtered out cooler " + cooler.getName() +
 * " due to color mismatch. Required: " + preferredColor +
 * ", Actual: " + cooler.getColor());
 * }
 * return colorMatch;
 * })
 * .collect(Collectors.toList());
 * 
 * System.out.println("Coolers after budget/color filter: " +
 * filteredCoolers.size());
 * updateTable();
 * }
 * 
 * @Override
 * protected void onMainboardChanged() {
 * if (selectedMainboard != null) {
 * socketLabel.setText("Required Socket: " + selectedMainboard.getCpuSocket());
 * System.out.println("Setting mainboard in Step 7: " +
 * selectedMainboard.getName() +
 * " with socket: " + selectedMainboard.getCpuSocket());
 * } else {
 * socketLabel.setText("Required Socket: None");
 * System.out.println("No mainboard selected in Step 7");
 * }
 * applyFilters();
 * }
 * 
 * 
 * .filter(cooler -> {
 * // Filter by socket compatibility
 * if (selectedMainboard == null) {
 * return true; // No mainboard selected, show all coolers
 * }
 * 
 * String requiredSocket = selectedMainboard.getCpuSocket();
 * boolean socketMatch = cooler.getSocketSupport().contains(requiredSocket);
 * 
 * if (!socketMatch) {
 * System.out.println("Filtered out cooler " + cooler.getName() +
 * " due to socket incompatibility. Required: " + requiredSocket +
 * ", Supported: " + cooler.getSocketSupport());
 * } else {
 * System.out.println("Cooler " + cooler.getName() +
 * " is compatible with socket " + requiredSocket);
 * }
 * return socketMatch;
 * })
 * 
 */

// Check socket compatibility
/*
 * if (selectedMainboard != null) {
 * String requiredSocket = selectedMainboard.getCpuSocket();
 * if (!selectedCooler.getSocketSupport().contains(requiredSocket)) {
 * showError("Selected cooler is not compatible with the mainboard socket: " +
 * requiredSocket);
 * return false;
 * }
 * }
 * 
 * 
 * if (selectedMainboard != null) {
 * String requiredSocket = selectedMainboard.getCpuSocket(); // Trim the
 * required socket
 * 
 * // Check if any trimmed socket in the list matches the required socket
 * boolean isCompatible = selectedCooler.getSocketSupport()
 * .stream()
 * .map(String::trim) // Trim each element in the list
 * .anyMatch(socket -> socket.equals(requiredSocket)); // Compare with trimmed
 * required socket
 * 
 * if (!isCompatible) {
 * showError("Selected cooler is not compatible with the mainboard socket: " +
 * requiredSocket);
 * return false;
 * }
 * }
 */