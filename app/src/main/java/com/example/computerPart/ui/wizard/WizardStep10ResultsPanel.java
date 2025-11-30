package com.example.computerPart.ui.wizard;

import com.example.computerPart.model.CPU;
import com.example.computerPart.model.GPU;
import com.example.computerPart.model.HardDisk;
import com.example.computerPart.model.Mainboard;
import com.example.computerPart.model.RAM;
import com.example.computerPart.ui.autobuild.AutoBuildResultsPanel.SystemCombination;
import com.example.computerPart.model.Cooler;
import com.example.computerPart.model.ComputerCase;
import com.example.computerPart.model.PowerSource;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.checkerframework.checker.units.qual.s;

public class WizardStep10ResultsPanel extends BaseWizardStepPanel {
    private final JTable resultTable;
    private final DefaultTableModel tableModel;
    // private final NumberFormat priceFormatter;
    // private final List<SystemCombination> combinations;
    private Mainboard sMainboard;
    private CPU sCpu;
    private RAM sRam;
    private GPU sGpu;
    private HardDisk sHardDisk;
    private Cooler sCooler;
    private ComputerCase sComputerCase;
    private PowerSource sPowerSource;
    private double totalPower;
    private double totalBenchmark;

    // private double baseBudget;
    private int bCpuNumber;
    private int bRamNumber;
    private int bGpuNumber;

    public WizardStep10ResultsPanel() {
        super("Result full set of Components");
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Results"));

        // priceFormatter = NumberFormat.getNumberInstance(Locale.US);
        // priceFormatter.setGroupingUsed(true);
        // priceFormatter.setMaximumFractionDigits(0);

        // combinations = new ArrayList<>();

        // Table
        String[] columnNames = {
                "Mainboard", "CPU", "RAM", "GPU", "Hard Disk", "Cooler", "Case", "Power Supply", "Price",
                "Benchmark",
                "Power(W)"
        };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    protected void updateWizardStepDataTable() {
        System.out.println("Updating WizardStep10ResultsPanel data table");
        if (componentManager.checkCompleteSelection()) {
            System.out.println("All components are selected, retrieving data...");
            sMainboard = componentManager.selectedMainboard;
            sCpu = componentManager.selectedCpu;
            sRam = componentManager.selectedRam;
            sGpu = componentManager.selectedGpu;
            sHardDisk = componentManager.selectedHardDisk;
            sCooler = componentManager.selectedCooler;
            sComputerCase = componentManager.selectedComputerCase;
            sPowerSource = componentManager.selectedPowerSource;
            bCpuNumber = componentManager.baseCpuNumber;
            bRamNumber = componentManager.baseRamNumber;
            bGpuNumber = componentManager.baseGpuNumber;
            totalCost = componentManager.calculateTotalCost();
            totalBenchmark = componentManager.calculateTotalBenchmark();
            totalPower = componentManager.calculateTotalPower();
            System.out.println("Data retrieved successfully. Total cost: " + totalCost);
        } else {
            System.out.println("Error: Not all components are selected");
            showError("Please select all components before proceeding to the next step.");
            return;
        }
        updateInformationLabel();
        refreshComponents();
    };

    @Override
    protected void refreshComponents() {
        // selectSuitableComponent();
        // filteredComponent = suitableComponet;
        updateTable();
    };

    @Override
    protected void updateTable() {
        tableModel.setRowCount(0);
        tableModel.addRow(new Object[] {
                formatMainboard(sMainboard),
                formatCPU(sCpu, bCpuNumber),
                formatRAM(sRam, bRamNumber),
                formatGPU(sGpu, bGpuNumber),
                formatHardDisk(sHardDisk),
                formatCooler(sCooler),
                formatComputerCase(sComputerCase),
                formatPowerSource(sPowerSource),
                priceFormatter.format(totalCost),
                priceFormatter.format(totalBenchmark),
                totalPower
        });
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
        return String.format("%s x%d (%s, %s)",
                ram.getName(),
                quantity,
                ram.getManufacturer(),
                ram.getRamType());
    }

    private String formatGPU(GPU gpu, int quantity) {
        return String.format("%s x%d (%s, %dGB)",
                gpu.getName(),
                quantity,
                gpu.getManufacturer(),
                gpu.getCapacity());
    }

    private String formatHardDisk(HardDisk hd) {
        return String.format("%s (%s, %s, %d MB/s)",
                hd.getName(),
                hd.getManufacturer(),
                hd.getType(),
                hd.getSpeed());
    }

    private String formatCooler(Cooler cooler) {
        return String.format("%s (%s, %s)",
                cooler.getName(),
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

    @Override
    protected void applyFilters() {
    };

    @Override
    protected void clearFilters() {
    };

    @Override
    public void selectSuitableComponent() {
    };

    @Override
    protected boolean validateSelection() {
        return true;
    };

    @Override
    protected void onSelectionChanged() {
    };

    @Override
    protected void updateInformationLabel() {
    };

    @Override
    protected void onPressBackButton() {
        // componentManager.selectedPowerSource = null;
    };

    @Override
    protected void exportToExcel(String filePath) throws IOException {
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

            // Get the first (and only) combination
            if (!componentManager.checkCompleteSelection()) {
                throw new IOException("No system configuration to export, please select all components.");
            }

            // SystemCombination combo = combinations.get(0);

            // Add data rows
            int rowNum = 1;

            // Mainboard
            addComponentRow(sheet, rowNum++, "Mainboard",
                    sMainboard.getName() +
                            " (id=" + sMainboard.getId() +
                            "/Socket=" + sMainboard.getCpuSocket() +
                            "/Size=" + sMainboard.getMainSize() + ")",
                    sMainboard.getColor(),
                    sMainboard.getPower(),
                    1, sMainboard.getPrice());

            // CPU
            addComponentRow(sheet, rowNum++, "CPU",
                    sCpu.getName() +
                            " (id=" + sCpu.getId() +
                            "/Socket=" + sCpu.getSocket() + ")",
                    "", sCpu.getPower(),
                    bCpuNumber, sCpu.getPrice());

            // RAM
            addComponentRow(sheet, rowNum++, "RAM",
                    sRam.getName() + " (id=" + sRam.getId() +
                            "/Size=" + sRam.getCapacity() +
                            "/Bus=" + sRam.getBus() + ")",
                    sRam.getColor(), sRam.getPower(),
                    bRamNumber, sRam.getPrice());

            // GPU
            addComponentRow(sheet, rowNum++, "GPU",
                    sGpu.getName() + " (id=" + sGpu.getId() +
                            "Capacity=" + sGpu.getCapacity() + ")",
                    sGpu.getColor(), sGpu.getPower(),
                    bGpuNumber, sGpu.getPrice());

            // Hard Disk
            addComponentRow(sheet, rowNum++, "Hard Disk",
                    sHardDisk.getName() +
                            " (id=" + sHardDisk.getId() +
                            "/Type" + sHardDisk.getType() +
                            "/Capacity=" + sHardDisk.getCapacity() + ")",
                    "", sHardDisk.getPower(),
                    1, sHardDisk.getPrice());

            // Cooler
            addComponentRow(sheet, rowNum++, "Cooler",
                    sCooler.getName() +
                            " (id=" + sCooler.getId() +
                            "/Socket Support=" + sCooler.getSocketSupport() + ")",
                    sCooler.getColor(), sCooler.getPower(),
                    bCpuNumber, sCooler.getPrice());

            // Computer Case
            addComponentRow(sheet, rowNum++, "Case",
                    sComputerCase.getName() +
                            " (id=" + sComputerCase.getId() +
                            "/Size=" + sComputerCase.getCaseSize() + ")",
                    sComputerCase.getColor(), sComputerCase.getPower(),
                    1, sComputerCase.getPrice());

            // Power Source
            addComponentRow(sheet, rowNum++, "Power Supply",
                    sPowerSource.getName() +
                            " (id=" + sPowerSource.getId() + ")",
                    sPowerSource.getColor(), sPowerSource.getPower(),
                    1, sPowerSource.getPrice());

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
            totalPowerValueCell.setCellValue(componentManager.calculateTotalPower());
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
            totalValueCell.setCellValue(totalCost);
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
            int power, int quantity, double price) {
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
}
