package com.example.computerPart.ui.autobuild;

import com.example.computerPart.model.CPU;
import com.example.computerPart.model.GPU;
import com.example.computerPart.model.HardDisk;
import com.example.computerPart.model.Mainboard;
import com.example.computerPart.model.RAM;
import com.example.computerPart.model.Cooler;
import com.example.computerPart.model.ComputerCase;
import com.example.computerPart.model.PowerSource;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class AutoBuildResultsPanel extends JPanel {
    // protected JTable resultTable;
    protected DefaultTableModel tableModel;
    protected TableRowSorter<DefaultTableModel> sorter;
    protected JTable componentTable;
    protected NumberFormat priceFormatter;
    private final List<SystemCombination> combinations;
    protected JLabel statusLabel;

    public AutoBuildResultsPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Results"));

        priceFormatter = NumberFormat.getNumberInstance(Locale.US);
        priceFormatter.setGroupingUsed(true);
        priceFormatter.setMaximumFractionDigits(0);

        combinations = new ArrayList<>();

        // Table
        String[] columnNames = {
                "Mainboard", "CPU", "RAM", "GPU", "Hard Disk", "Cooler", "Case", "Power Supply", "Total Price",
                "Total Benchmark", "Total Power(W)"
        };
        // tableModel = new DefaultTableModel(columnNames, 0) {
        // @Override
        // public boolean isCellEditable(int row, int column) {
        // return false;//
        // }
        // };
        setupTable(columnNames);
        // componentTable = new JTable(tableModel);
        // componentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // componentTable.getSelectionModel().addListSelectionListener(e -> {
        // if (!e.getValueIsAdjusting()) {
        // updateStatusLabel();
        // }
        // });

        // JScrollPane scrollPane = new JScrollPane(componentTable);
        // add(scrollPane, BorderLayout.CENTER);

        // Status panel
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusLabel = new JLabel("Select a combination to export");
        statusPanel.add(statusLabel, BorderLayout.WEST);

        // Instructions
        JLabel instructionsLabel = new JLabel("Click on a combination to select it for export");
        statusPanel.add(instructionsLabel, BorderLayout.EAST);

        add(statusPanel, BorderLayout.SOUTH);
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
        // componentTable.getSelectionModel().addListSelectionListener(e -> {
        // if (!e.getValueIsAdjusting()) {
        // onSelectionChanged();
        // }
        // });

        // List of column names that should be sorted as integers
        String[] columnInteger = { "ID", "Stock", "Power(W)", "Capacity(GB)",
                "Bus(MHz)", "Quantity Needed", "Speed(MB/s)", "RAM Slots",
                "GPU Slots", "CPU Slots", "Core Number", "Total Power(W)" };

        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            String columnName = tableModel.getColumnName(i);

            // If the column name matches one of the specified integer columns, set
            // comparator
            if (Arrays.asList(columnInteger).contains(columnName)) {
                sorter.setComparator(i, (Comparator<Integer>) Integer::compare);
            } else if ("Total Benchmark".equals(columnName)) {
                sorter.setComparator(i, (Comparator<Double>) Double::compare);
            } else if ("Total Price".equals(columnName)) {
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

    private void updateStatusLabel() {
        int selectedRow = componentTable.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < combinations.size()) {
            SystemCombination combo = combinations.get(selectedRow);
            statusLabel.setText(String.format("Selected: Combination #%d - Price: %s",
                    selectedRow + 1,
                    priceFormatter.format(combo.getTotalPrice())));
        } else {
            statusLabel.setText("Select a combination to export");
        }
    }

    public void addCombination(Mainboard mainboard, CPU cpu, RAM ram, GPU gpu, HardDisk hardDisk, Cooler cooler,
            ComputerCase computerCase, PowerSource powerSource, int cpuQuantity, int ramQuantity, int gpuQuantity) {
        SystemCombination combination = new SystemCombination(
                mainboard, cpu, ram, gpu, hardDisk, cooler, computerCase, powerSource, cpuQuantity, ramQuantity,
                gpuQuantity);
        combinations.add(combination);
        updateTable();
    }

    public void clearCombinations() {
        combinations.clear();
        updateTable();
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        int index = 1;
        for (SystemCombination combo : combinations) {
            tableModel.addRow(new Object[] {
                    formatMainboard(combo.mainboard),
                    formatCPU(combo.cpu, combo.cpuQuantity),
                    formatRAM(combo.ram, combo.ramQuantity),
                    formatGPU(combo.gpu, combo.gpuQuantity),
                    formatHardDisk(combo.hardDisk),
                    formatCooler(combo.cooler, combo.cpuQuantity),
                    formatComputerCase(combo.computerCase),
                    formatPowerSource(combo.powerSource),
                    priceFormatter.format(combo.getTotalPrice()),
                    combo.getTotalBenchmark(),
                    combo.getTotalPower()
            });
            index++;
        }

        // Select the first row if available
        /*
         * if (tableModel.getRowCount() > 0) {
         * componentTable.setRowSelectionInterval(0, 0);
         * }
         */

        updateStatusLabel();
    }

    private String formatMainboard(Mainboard mb) {
        return String.format("%s (%s, %s)",
                mb.getName(),
                mb.getManufacturer(),
                mb.getMainSize());
    }

    private String formatCPU(CPU cpu, int quantity) {
        return String.format("%s x%d (%s, %s)",
                cpu.getName(),
                quantity,
                cpu.getManufacturer(),
                cpu.getSocket());
    }

    private String formatRAM(RAM ram, int quantity) {
        return String.format("%s x%d (%s, %s,%d GB)",
                ram.getName(),
                quantity,
                ram.getManufacturer(),
                ram.getRamType(),
                ram.getCapacity());
    }

    private String formatGPU(GPU gpu, int quantity) {
        return String.format("%s x%d (%s, %dGB)",
                gpu.getName(),
                quantity,
                gpu.getManufacturer(),
                gpu.getCapacity());
    }

    private String formatHardDisk(HardDisk hd) {
        return String.format("%s (%s, %s, %d, %d MB/s)",
                hd.getName(),
                hd.getManufacturer(),
                hd.getType(),
                hd.getCapacity(),
                hd.getSpeed());
    }

    private String formatCooler(Cooler cooler, int quantity) {
        return String.format("%s x%d (%s, %s)",
                cooler.getName(),
                quantity,
                cooler.getManufacturer(),
                String.join(", ", cooler.getSocketSupport()));
    }

    private String formatComputerCase(ComputerCase computerCase) {
        return String.format("%s (%s, %s)",
                computerCase.getName(),
                computerCase.getManufacturer(),
                computerCase.getCaseSize());
    }

    private String formatPowerSource(PowerSource powerSource) {
        return String.format("%s (%s, %dW)",
                powerSource.getName(),
                powerSource.getManufacturer(),
                powerSource.getPower());
    }

    public void exportToExcel(String filePath) throws IOException {
        int selectedRow = componentTable.getSelectedRow();
        if (selectedRow == -1 || selectedRow >= combinations.size()) {
            throw new IOException("Please select a combination to export");
        }

        SystemCombination combo = combinations.get(selectedRow);

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("System Configuration");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("STT");
            headerRow.createCell(1).setCellValue("ComponentType");
            headerRow.createCell(2).setCellValue("Description");
            headerRow.createCell(3).setCellValue("Color");
            headerRow.createCell(4).setCellValue("Power");
            headerRow.createCell(5).setCellValue("Qty");
            headerRow.createCell(6).setCellValue("Price");
            headerRow.createCell(7).setCellValue("Total");

            // Create cell styles
            CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            for (int i = 0; i < 8; i++) {
                headerRow.getCell(i).setCellStyle(headerStyle);
            }

            // Add data rows//
            int rowNum = 1;

            // Mainboard
            addComponentRow(sheet, rowNum++, "Mainboard",
                    combo.mainboard.getName() +
                            " (id=" + combo.mainboard.getId() +
                            "/Socket=" + combo.mainboard.getCpuSocket() +
                            "/Size=" + combo.mainboard.getMainSize() + ")",
                    combo.mainboard.getColor(),
                    combo.mainboard.getPower(),
                    1, combo.mainboard.getPrice());

            // CPU
            addComponentRow(sheet, rowNum++, "CPU", combo.cpu.getName() +
                    " (id=" + combo.cpu.getId() +
                    "/Socket=" + combo.cpu.getSocket() + ")",
                    "", // combo.cpu.getColor(),
                    combo.cpu.getPower(),
                    combo.cpuQuantity, combo.cpu.getPrice());

            // RAM
            addComponentRow(sheet, rowNum++, "RAM", combo.ram.getName() +
                    " (id=" + combo.ram.getId() + "/Size=" + combo.ram.getCapacity() + "/Bus=" + combo.ram.getBus()
                    + ")",
                    combo.ram.getColor(),
                    combo.ram.getPower(),
                    combo.ramQuantity, combo.ram.getPrice());

            // GPU
            addComponentRow(sheet, rowNum++, "GPU", 
                    combo.gpu.getName() + 
                            " (id=" + combo.gpu.getId() +
                            "Capacity=" + combo.gpu.getCapacity() + ")",
                    combo.gpu.getColor(),
                    combo.gpu.getPower(),
                    combo.gpuQuantity, combo.gpu.getPrice());

            // Hard Disk
            addComponentRow(sheet, rowNum++, "Hard Disk",
                    combo.hardDisk.getName() +
                            " (id=" + combo.hardDisk.getId() + 
                            "/Type" + combo.hardDisk.getType() + 
                            "/Capacity=" + combo.hardDisk.getCapacity() + ")",
                    "", // combo.hardDisk.getColor(),
                    combo.hardDisk.getPower(),
                    1, combo.hardDisk.getPrice());

            // Cooler
            addComponentRow(sheet, rowNum++, "Cooler", combo.cooler.getName() +
                    " (id=" + combo.cooler.getId() +
                    "/Socket Support=" + combo.cooler.getSocketSupport() + ")",
                    combo.cooler.getColor(), combo.cooler.getPower(),
                    combo.cpuQuantity, combo.cooler.getPrice());

            // Computer Case
            addComponentRow(sheet, rowNum++, "Case",
                    combo.computerCase.getName() +
                            " (id=" + combo.computerCase.getId() + "/Size=" + combo.computerCase.getCaseSize() + ")",
                    combo.computerCase.getColor(),
                    combo.computerCase.getPower(),
                    1, combo.computerCase.getPrice());

            // Power Source
            addComponentRow(sheet, rowNum++, "Power Supply",
                    combo.powerSource.getName() + " (id=" + combo.powerSource.getId() + ")",
                    combo.powerSource.getColor(), combo.powerSource.getPower(),
                    1, combo.powerSource.getPrice());

            // Total Power row
            Row totalPowerRow = sheet.createRow(rowNum++);
            Cell totalPowerLabelCell = totalPowerRow.createCell(2);
            totalPowerLabelCell.setCellValue("Total Power Usage");

            CellStyle totalStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font totalFont = workbook.createFont();
            totalFont.setBold(true);
            totalStyle.setFont(totalFont);
            totalPowerLabelCell.setCellStyle(totalStyle);

            Cell totalPowerValueCell = totalPowerRow.createCell(4);
            totalPowerValueCell.setCellValue(combo.getTotalPower());
            totalPowerValueCell.setCellStyle(totalStyle);

            // Grand Total row
            Row totalRow = sheet.createRow(rowNum++);
            Cell totalLabelCell = totalRow.createCell(2);
            totalLabelCell.setCellValue("Grand Total");

            // CellStyle totalStyle = workbook.createCellStyle();
            // org.apache.poi.ss.usermodel.Font totalFont = workbook.createFont();
            // totalFont.setBold(true);
            // totalStyle.setFont(totalFont);
            totalLabelCell.setCellStyle(totalStyle);

            Cell totalValueCell = totalRow.createCell(7);
            totalValueCell.setCellValue(combo.getTotalPrice());
            totalValueCell.setCellStyle(totalStyle);

            // Auto-size columns
            for (int i = 0; i < 8; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write to file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        }
    }

    private void addComponentRow(Sheet sheet, int rowNum, String componentType, String description, String color,
            int power,
            int quantity,
            double price) {
        Row row = sheet.createRow(rowNum);

        // STT (auto-incremented number)
        row.createCell(0).setCellValue(rowNum);

        // ComponentType
        row.createCell(1).setCellValue(componentType);

        // Description
        row.createCell(2).setCellValue(description);

        // Color
        row.createCell(3).setCellValue(color);
        // power
        row.createCell(4).setCellValue(power);

        // Quantity
        row.createCell(5).setCellValue(quantity);

        // Price
        row.createCell(6).setCellValue(price);

        // Total (Price * Quantity)
        row.createCell(7).setCellValue(price * quantity);
    }

    public int getSelectedCombinationIndex() {
        return componentTable.getSelectedRow();
    }

    public SystemCombination getSelectedCombination() {
        int selectedRow = componentTable.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < combinations.size()) {
            return combinations.get(selectedRow);
        }
        return null;
    }

    public static class SystemCombination {
        public final Mainboard mainboard;
        public final CPU cpu;
        public final RAM ram;
        public final GPU gpu;
        public final HardDisk hardDisk;
        public final Cooler cooler;
        public final ComputerCase computerCase;
        public final PowerSource powerSource;
        public final int cpuQuantity;
        public final int ramQuantity;
        public final int gpuQuantity;

        public SystemCombination(Mainboard mainboard, CPU cpu, RAM ram, GPU gpu, HardDisk hardDisk, Cooler cooler,
                ComputerCase computerCase, PowerSource powerSource, int cpuQuantity, int ramQuantity, int gpuQuantity) {
            this.mainboard = mainboard;
            this.cpu = cpu;
            this.ram = ram;
            this.gpu = gpu;
            this.hardDisk = hardDisk;
            this.cooler = cooler;
            this.computerCase = computerCase;
            this.powerSource = powerSource;
            this.cpuQuantity = cpuQuantity;
            this.ramQuantity = ramQuantity;
            this.gpuQuantity = gpuQuantity;
        }

        public double getTotalPrice() {
            return mainboard.getPrice() +
                    (cpu.getPrice() * cpuQuantity) +
                    (ram.getPrice() * ramQuantity) +
                    (gpu.getPrice() * gpuQuantity) +
                    hardDisk.getPrice() +
                    cooler.getPrice() * cpuQuantity +
                    computerCase.getPrice() +
                    powerSource.getPrice();
        }

        public double getTotalBenchmark() {
            return (cpu.getBenchmark() * cpuQuantity) +
                    (ram.getBus() * ram.getCapacity() / 10) +
                    (gpu.getBenchmark() * gpuQuantity) +
                    (hardDisk.getSpeed() * hardDisk.getCapacity() / 1000);
        }

        public int getTotalPower() {
            return mainboard.getPower() +
                    (cpu.getPower() * cpuQuantity) +
                    (ram.getPower() * ramQuantity) +
                    (gpu.getPower() * gpuQuantity) +
                    hardDisk.getPower() +
                    cooler.getPower() * cpuQuantity +
                    computerCase.getPower();
        }
    }
}