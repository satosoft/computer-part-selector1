package com.example.computerPart.ui.wizard;

import com.example.computerPart.dao.MainboardDAO;
import com.example.computerPart.model.Mainboard;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class WizardStep2Panel extends BaseWizardStepPanel {
    private final MainboardDAO mainboardDAO;
    private final JComboBox<String> cpuSocketBox;
    private final JComboBox<String> cpuSlotsBox;
    private final JComboBox<String> ramTypeBox;
    private final JComboBox<String> ramSlotsBox;
    private final JComboBox<String> gpuSlotsBox;
    private final JCheckBox nvmeCheckBox;
    // private final JComboBox<String> mainSizeBox;
    private List<Mainboard> filteredMainboards;
    private List<Mainboard> suitableMainboards;
    private List<Mainboard> allMainboards;
    // protected Mainboard selectedMainboard;
    private double previousStepsTotalCost;
    private JLabel statusLabel;
    private JPanel statusPanel;
    // private List<Mainboard> filteredMainboardsStep1;

    public WizardStep2Panel() {
        super("Step 2: Mainboard Selection");
        mainboardDAO = new MainboardDAO();
        try {
            allMainboards = mainboardDAO.getAll();
        } catch (SQLException e) {
            showError("Error loading RAM: " + e.getMessage());
        }
        // previousStepsTotalCost = componentManager.calculateTotalCost();

        // Filter panel
        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filters"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // CPU Socket
        gbc.gridx = 0;
        gbc.gridy = 0;
        filterPanel.add(new JLabel("CPU Socket:"), gbc);

        gbc.gridx = 1;
        // Initialize with a default "Any" option, will be populated later
        cpuSocketBox = new JComboBox<>(new String[] { "Any" });
        /*
         * cpuSocketBox.addActionListener(e ->
         * {
         * System.out.println("CPU Socket selection changed to: " +
         * cpuSocketBox.getSelectedItem());
         * Thread.dumpStack();
         * applyFilters();
         * }
         * );
         */
        filterPanel.add(cpuSocketBox, gbc);

        // CPU Slots
        gbc.gridx = 2;
        filterPanel.add(new JLabel("CPU Slots:"), gbc);

        gbc.gridx = 3;
        cpuSlotsBox = new JComboBox<>(new String[] { "Any", "1", "2", "4", "8" });
        cpuSlotsBox.setSelectedItem("Any");
        filterPanel.add(cpuSlotsBox, gbc);

        // RAM Type
        gbc.gridx = 0;
        gbc.gridy = 1;
        filterPanel.add(new JLabel("RAM Type:"), gbc);

        gbc.gridx = 1;
        ramTypeBox = new JComboBox<>(new String[] { "Any", "DDR4", "DDR5" });
        ramTypeBox.setSelectedItem("Any");
        filterPanel.add(ramTypeBox, gbc);

        // RAM Slots
        gbc.gridx = 2;
        filterPanel.add(new JLabel("RAM Slots:"), gbc);

        gbc.gridx = 3;
        ramSlotsBox = new JComboBox<>(new String[] { "Any", "2", "4", "8" });
        ramSlotsBox.setSelectedItem("Any");
        filterPanel.add(ramSlotsBox, gbc);

        // GPU Slots
        gbc.gridx = 0;
        gbc.gridy = 2;
        filterPanel.add(new JLabel("GPU Slots:"), gbc);

        gbc.gridx = 1;
        gpuSlotsBox = new JComboBox<>(new String[] { "Any", "1", "2", "3", "4", "5", "6", "7", "8" });
        gpuSlotsBox.setSelectedItem("Any");
        filterPanel.add(gpuSlotsBox, gbc);

        // NVME Support
        gbc.gridx = 2;
        nvmeCheckBox = new JCheckBox("NVME Support");
        filterPanel.add(nvmeCheckBox, gbc);

        // Main Size
        /*
         * gbc.gridx = 3;
         * filterPanel.add(new JLabel("Size:"), gbc);
         * gbc.gridx = 4;
         * mainSizeBox = new JComboBox<>(new String[] { "Any", "mATX", "ATX" });
         * mainSizeBox.setSelectedItem("Any");
         * filterPanel.add(mainSizeBox, gbc);
         */
        // Apply Filter button
        gbc.gridx = 5;
        JButton applyFilterButton = new JButton("Apply Filters");
        applyFilterButton.addActionListener(e -> applyFilters());
        filterPanel.add(applyFilterButton, gbc);

        // Clear Filter button//
        gbc.gridx = 6;
        JButton clearFilterButton = new JButton("Clear Filters");
        clearFilterButton.addActionListener(e -> clearFilters());
        filterPanel.add(clearFilterButton, gbc);

        add(filterPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {
                "ID", "Name", "Manufacturer", "Stock", "Power(W)", "Color", "Price",
                "CPU Socket", "RAM Type", "RAM Slots", "GPU Slots", "CPU Slots", "NVME Support", "Size"
        };
        setupTable(columnNames);
        // Status panel and cost panel at bottom
        statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel("Required Main Size: N/A");
        statusPanel.add(statusLabel);

        addCostPanel();
        JPanel doublePanelContainer = new JPanel();
        doublePanelContainer.setLayout(new GridLayout(2, 1));
        doublePanelContainer.add(statusPanel);
        doublePanelContainer.add(costPanel);
        add(doublePanelContainer, BorderLayout.SOUTH);

        // Initialize the table and populate CPU socket options
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
        filteredMainboards = suitableMainboards;
        populateCpuSocketOptions();
        updateTable();
    }

    private Mainboard getSelectedMainboard() {
        int modelRow = getSelectedModelRow();
        if (modelRow == -1) {
            return null;
        }

        int id = (int) tableModel.getValueAt(modelRow, 0);
        return filteredMainboards.stream()
                .filter(mb -> mb.getId() == id)
                .findFirst()
                .orElse(null);

    }

    private void populateCpuSocketOptions() {
        Set<String> uniqueSockets = new HashSet<>();
        uniqueSockets.add("Any"); // Always include "Any" option

        for (Mainboard mb : allMainboards) {
            if (mb.getCpuSocket() != null && !mb.getCpuSocket().isEmpty()) {
                uniqueSockets.add(mb.getCpuSocket());
            }
        }

        // Update the combo box
        cpuSocketBox.removeAllItems();
        for (String socket : uniqueSockets) {
            cpuSocketBox.addItem(socket);
        }
        // Always set to "Any" initially
        cpuSocketBox.setSelectedItem("Any");
    }

    @Override
    protected void applyFilters() {
        selectSuitableComponent();
        // Get and validate all filter values upfront
        String selectedSocket = (String) cpuSocketBox.getSelectedItem() != null
                ? (String) cpuSocketBox.getSelectedItem()
                : "Any";
        String selectedRamType = (String) ramTypeBox.getSelectedItem() != null
                ? (String) ramTypeBox.getSelectedItem()
                : "Any";
        String selectedCpuSlots = (String) cpuSlotsBox.getSelectedItem() != null
                ? (String) cpuSlotsBox.getSelectedItem()
                : "Any";

        String selectedRamSlots = (String) ramSlotsBox.getSelectedItem() != null
                ? (String) ramSlotsBox.getSelectedItem()
                : "Any";
        String selectedGpuSlots = (String) gpuSlotsBox.getSelectedItem() != null
                ? (String) gpuSlotsBox.getSelectedItem()
                : "Any";

        filteredMainboards = suitableMainboards.stream()
                .filter(mb -> {
                    boolean socketMatch = "Any".equals(selectedSocket) ||
                            (mb.getCpuSocket().equals(selectedSocket));
                    return socketMatch;
                })
                .filter(mb -> "Any".equals(selectedRamType) ||
                        mb.getRamType().equals(selectedRamType))
                .filter(mb -> "Any".equals(selectedCpuSlots) ||
                        String.valueOf(mb.getCpuSlots()).equals(selectedCpuSlots))
                .filter(mb -> "Any".equals(selectedRamSlots) ||
                        String.valueOf(mb.getRamSlots()).equals(selectedRamSlots))
                .filter(mb -> "Any".equals(selectedGpuSlots) ||
                        String.valueOf(mb.getGpuSlots()).equals(selectedGpuSlots))
                .filter(mb -> !nvmeCheckBox.isSelected() || mb.isSupportNVME())
                .collect(Collectors.toList());
        updateTable();
    }

    @Override
    protected void clearFilters() {
        cpuSocketBox.setSelectedItem("Any");
        cpuSlotsBox.setSelectedItem("Any");
        ramTypeBox.setSelectedItem("Any");
        ramSlotsBox.setSelectedItem("Any");
        gpuSlotsBox.setSelectedItem("Any");
        nvmeCheckBox.setSelected(false);
        applyFilters();
    }

    @Override
    public void updateTable() {
        tableModel.setRowCount(0);
        for (Mainboard mb : filteredMainboards) {
            tableModel.addRow(new Object[] {
                    mb.getId(),
                    mb.getName(),
                    mb.getManufacturer(),
                    mb.getStock(),
                    mb.getPower(),
                    mb.getColor(),
                    priceFormatter.format(mb.getPrice()),

                    mb.getCpuSocket(),
                    mb.getRamType(),
                    mb.getRamSlots(),
                    mb.getGpuSlots(),
                    mb.getCpuSlots(),
                    mb.isSupportNVME() ? "Yes" : "No",
                    mb.getMainSize(),

            });
        }
    }

    @Override
    public void selectSuitableComponent() {
        suitableMainboards = allMainboards.stream()
                .filter(mb -> mb.getMainSize().equals(componentManager.baseMainSize))
                .filter(mb -> "Any".equals(componentManager.basePreferredColor)
                        || mb.getColor().equals(componentManager.basePreferredColor))
                .filter(mb -> mb.getPrice() <= (componentManager.baseBudget - previousStepsTotalCost))
                .filter(mb -> mb.getCpuSlots() >= componentManager.baseCpuNumber)
                .filter(mb -> mb.getGpuSlots() >= componentManager.baseGpuNumber)
                .filter(mb -> mb.getStock() > 0)
                .collect(Collectors.toList());
        if (componentManager.baseHardDiskType.equals("NVME")) {
            suitableMainboards = suitableMainboards.stream()
                    .filter(mb -> mb.isSupportNVME())
                    .collect(Collectors.toList());
        }
    }

    @Override
    public boolean validateSelection() {
        if (getSelectedMainboard() == null) {
            showError("Please select a mainboard");
            return false;
        }
        return true;
    }

    @Override
    public void onSelectionChanged() {
        if (getSelectedMainboard() != null) {
            componentManager.selectedMainboard = getSelectedMainboard();
            updateInformationLabel();
        }
    }

    @Override
    protected void updateInformationLabel() {
        statusLabel.setText("Required Main Size:" + componentManager.baseMainSize);
        this.totalCost = componentManager.calculateTotalCost();
        this.costLabel.setText("Total Cost: " + priceFormatter.format(this.totalCost) + " VND");
    }

    @Override
    public void onPressBackButton() {
        componentManager.selectedMainboard = null;
        this.totalCost = componentManager.calculateTotalCost();
        this.costLabel.setText("Total Cost: " + priceFormatter.format(this.totalCost) + " VND");
    }

}

/*
 * private boolean isNumeric(String str) {
 * try {
 * Integer.parseInt(str); // Use Double.parseDouble(str) for decimal numbers
 * return true;
 * } catch (NumberFormatException e) {
 * return false;
 * }
 * }
 * 
 * @Override
 * protected double calculatePreviousCost() {
 * return 0;
 * }
 * 
 * @Override
 * protected double calculateTotalCost() {
 * return (selectedMainboard != null ? selectedMainboard.getPrice() : 0);
 * // +(selectedCpu != null ? selectedCpu.getPrice() : 0) +
 * // (selectedRam != null ? selectedRam.getPrice() : 0) +
 * // (selectedGpu != null ? selectedGpu.getPrice() : 0) +
 * // (selectedHardDisk != null ? selectedHardDisk.getPrice() : 0) +
 * // (selectedCooler != null ? selectedCooler.getPrice() : 0) +
 * // (selectedComputerCase != null ? selectedComputerCase.getPrice() : 0) +
 * // (selectedPowerSource != null ? selectedPowerSource.getPrice() : 0);
 * }
 * 
 * 
 * Filters the mainboard list by size
 * 
 * @param mainSize The desired mainboard size (ATX, mATX, or Any)
 * 
 * 
 * public void filterByMainSize(String mainSize) {
 * if (mainSize == null || "Any".equals(mainSize)) {
 * return; // No filtering needed
 * }
 * 
 * System.out.println("Filtering by mainboard size: " + mainSize);
 * System.out.println("Mainboards before size filter: " +
 * filteredMainboards.size());
 * 
 * filteredMainboards = filteredMainboards.stream()
 * .filter(mb -> mainSize.equals(mb.getMainSize()))
 * .collect(Collectors.toList());
 * 
 * System.out.println("Mainboards after size filter: " +
 * filteredMainboards.size());
 * updateTable();
 * }
 * 
 * 
 * 
 * @Override
 * public void filterByBudgetAndColor(double budget, String preferredColor) {
 * // super.filterByBudgetAndColor(budget, preferredColor);
 * // System.out.println("Mainboards before budget filter: " +
 * // filteredMainboards.size());
 * 
 * filteredMainboards = filteredMainboards.stream()
 * .filter(mb -> {
 * boolean budgetMatch = mb.getPrice() <= (budget - getTotalCost());
 * if (!budgetMatch) {
 * System.out.println("Filtered out mainboard " + mb.getName() +
 * " due to budget. Required: " + (budget - getTotalCost()) +
 * ", Actual: " + mb.getPrice());
 * }
 * return budgetMatch;
 * })
 * .filter(mb -> "Any".equals(preferredColor) ||
 * preferredColor.equals(mb.getColor()))
 * .collect(Collectors.toList());
 * 
 * System.out.println("Mainboards after budget filter: " +
 * filteredMainboards.size());
 * updateTable();
 * }
 * 
 * 
 * public void filterMainboardFromStep1() {
 * refreshComponents();
 * filteredMainboards = filteredMainboards.stream()
 * .filter(mb->mb.getMainSize().equals(this.getBaseMainSize()))
 * .filter(mb->"Any".equals(this.getBasePreferredColor()) ||
 * mb.getColor().equals(this.getBasePreferredColor()))
 * .filter(mb->mb.getPrice() <= (this.getBaseBudget() - getTotalCost()))
 * .filter(mb->mb.getCpuSlots() >= this.getBaseCpuNumber())
 * .filter(mb->mb.getGpuSlots() >= this.getBaseGpuNumber())
 * .collect(Collectors.toList());
 * 
 * /*
 * //System.out.println("Starting filterMainboardFromStep1 with values:");
 * //System.out.println("BaseMainSize: " + this.getBaseMainSize());
 * //System.out.println("BasePreferredColor: " + this.getBasePreferredColor());
 * //System.out.println("BaseBudget: " + this.getBaseBudget());
 * //System.out.println("BaseGpuNumber: " + this.getBaseGpuNumber());
 * //System.out.println("BaseCpuNumber: " + this.getBaseCpuNumber());
 * 
 * //System.out.println("Mainboards before filtering: " +
 * filteredMainboards.size());
 * filteredMainboards = filteredMainboards.stream()
 * .filter(mb -> {
 * boolean sizeMatch = mb.getMainSize().equals(this.getBaseMainSize());
 * if (!sizeMatch) {
 * System.out
 * .println("Filtered out mainboard " + mb.getName() +
 * " due to size mismatch. Required: "
 * + this.getBaseMainSize() + ", Actual: " + mb.getMainSize());
 * }
 * return sizeMatch;
 * })
 * .filter(mb -> {
 * boolean colorMatch = "Any".equals(this.getBasePreferredColor())
 * || mb.getColor().equals(this.getBasePreferredColor());
 * if (!colorMatch) {
 * System.out
 * .println("Filtered out mainboard " + mb.getName() +
 * " due to color mismatch. Required: "
 * + this.getBasePreferredColor() + ", Actual: " + mb.getColor());
 * }
 * return colorMatch;
 * })
 * .filter(mb -> {
 * boolean budgetMatch = mb.getPrice() <= this.getBaseBudget();
 * if (!budgetMatch) {
 * System.out.println("Filtered out mainboard " + mb.getName() +
 * " due to budget. Required: "
 * + this.getBaseBudget() + ", Actual: " + mb.getPrice());
 * }
 * return budgetMatch;
 * })
 * .filter(mb -> {
 * boolean gpuSlotsMatch = mb.getGpuSlots() >= this.getBaseGpuNumber();
 * if (!gpuSlotsMatch) {
 * System.out.println("Filtered out mainboard " + mb.getName() +
 * " due to GPU slots. Required: "
 * + this.getBaseGpuNumber() + ", Actual: " + mb.getGpuSlots());
 * }
 * return gpuSlotsMatch;
 * })
 * .filter(mb -> {
 * boolean cpuSlotsMatch = mb.getCpuSlots() >= this.getBaseCpuNumber();
 * if (!cpuSlotsMatch) {
 * System.out.println("Filtered out mainboard " + mb.getName() +
 * " due to CPU slots. Required: "
 * + this.getBaseCpuNumber() + ", Actual: " + mb.getCpuSlots());
 * }
 * return cpuSlotsMatch;
 * })
 * .collect(Collectors.toList());
 * 
 * System.out.println("Mainboards after filtering: " +
 * filteredMainboards.size());
 * 
 * updateTable();
 * }
 * 
 * private void applyFilters() {
 * // try {
 * // List<Mainboard> mainboards = mainboardDAO.getAll();
 * // System.out.println("Total mainboards before filtering: " +
 * // mainboards.size());
 * 
 * // Get and validate all filter values upfront
 * final String selectedSocket = (String) cpuSocketBox.getSelectedItem() != null
 * ? (String) cpuSocketBox.getSelectedItem()
 * : "Any";
 * final int selectedCpuSlots = (Integer) cpuSlotsBox.getSelectedItem() != null
 * ? (Integer) cpuSlotsBox.getSelectedItem()
 * : 1;
 * final String selectedRamType = (String) ramTypeBox.getSelectedItem() != null
 * ? (String) ramTypeBox.getSelectedItem()
 * : "Any";
 * final int selectedRamSlots = (Integer) ramSlotsBox.getSelectedItem() != null
 * ? (Integer) ramSlotsBox.getSelectedItem()
 * : 2;
 * final int selectedGpuSlots = (Integer) gpuSlotsBox.getSelectedItem() != null
 * ? (Integer) gpuSlotsBox.getSelectedItem()
 * : 1;
 * 
 * final String selectedMainSize = (String) mainSizeBox.getSelectedItem() !=
 * null
 * ? (String) mainSizeBox.getSelectedItem()
 * : "Any";
 * 
 * 
 * // System.out.println("Selected CPU Socket: " + selectedSocket);
 * 
 * filteredMainboards = suitableMainboards.stream()
 * .filter(mb -> {
 * boolean socketMatch = "Any".equals(selectedSocket) ||
 * (mb.getCpuSocket() != null && mb.getCpuSocket().equals(selectedSocket));
 * if (!socketMatch) {
 * System.out.println("Filtered out mainboard " + mb.getName()
 * + " due to socket mismatch. Board socket: " + mb.getCpuSocket());
 * }
 * return socketMatch;
 * })
 * .filter(mb -> mb.getCpuSlots() >= selectedCpuSlots)
 * .filter(mb -> "Any".equals(selectedRamType) ||
 * mb.getRamType().equals(selectedRamType))
 * .filter(mb -> mb.getRamSlots() >= selectedRamSlots)
 * .filter(mb -> mb.getGpuSlots() >= selectedGpuSlots)
 * .filter(mb -> !nvmeCheckBox.isSelected() || mb.isSupportNVME())
 * .filter(mb -> mb.getMainSize().equals(this.getBaseMainSize()))
 * // .filter(mb -> "Any".equals(selectedMainboard.getColor())
 * // ||mb.getColor().equals(this.getBasePreferredColor()))
 * .collect(Collectors.toList());
 * 
 * System.out.println("Filtered mainboards: " + filteredMainboards.size());
 * updateTable();
 * 
 * // } catch (SQLException e) {
 * // showError("Error applying filters: " + e.getMessage());
 * // }
 * }
 * 
 * //(isNumeric(selectedCpuSlots) && mb.getCpuSlots() ==
 * Integer.parseInt(selectedCpuSlots)))
 */