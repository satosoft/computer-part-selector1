package com.example.computerPart.ui.wizard;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

public class WizardStep1Panel extends BaseWizardStepPanel {
    //private final JTextField budgetField;
    private final JFormattedTextField budgetField;
    private final JComboBox<String> colorComboBox;
    private final JComboBox<String> mainSizeComboBox;
    private final JComboBox<String> cpuNumberComboBox;
    private final JComboBox<String> gpuNumberComboBox;
    private final JComboBox<String> gpuMinCapacityComboBox;
    private final JComboBox<String> gpuMaxCapacityComboBox;
    private final JComboBox<String> ramCapacityComboBox;
    private final JComboBox<String> hdMinCapacityComboBox;
    private final JComboBox<String> hdMaxCapacityComboBox;
    private final JComboBox<String> hdTypeComboBox;
    private final JComboBox<String> maxSearchTimeComboBox;
    private final JComboBox<String> usageProfileBox;
    // private final NumberFormat priceFormatter;
    private boolean isUpdating = false;
    private int currentRow = 0;

    public WizardStep1Panel() {
        super("Step 1: System Requirements");
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Step 1: Basic Requirements"));

        // priceFormatter = NumberFormat.getCurrencyInstance(Locale.US);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Budget
        gbc.gridx = 0;
        gbc.gridy = currentRow++;
        gbc.weightx = 0.0;
        add(new JLabel("Budget:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        //budgetField = new JTextField(10);
        budgetField = new JFormattedTextField(priceFormatter);
        add(budgetField, gbc);

        // Color
        gbc.gridx = 0;
        gbc.gridy = currentRow++;
        gbc.weightx = 0.0;
        add(new JLabel("Preferred Color:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        colorComboBox = new JComboBox<>(new String[] { "Any", "Black", "White", "Red", "Blue", "RGB" });
        // this.sPreferredColor = "Any"; // Initialize with default value
        colorComboBox.addActionListener(e -> updateColorSettings());
        add(colorComboBox, gbc);

        // Usage Profile
        gbc.gridx = 0;
        gbc.gridy = currentRow++;
        gbc.weightx = 0.0;
        add(new JLabel("Usage Profile:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        usageProfileBox = new JComboBox<>(new String[] { "Profile 1: Normal (Student, Office)",
                "Profile 2: Developer, IT related", "Profile 3: Gaming, Video Editor",
                "Profile 4: Enterprise" });
        usageProfileBox.addActionListener(e -> updateProfileSettings());
        add(usageProfileBox, gbc);

        // CPU Number
        gbc.gridx = 0;
        gbc.gridy = currentRow++;
        gbc.weightx = 0.0;
        add(new JLabel("CPU Number:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cpuNumberComboBox = new JComboBox<>(new String[] { "1", "2" });
        cpuNumberComboBox.addActionListener(e -> updateCpuNumber());
        add(cpuNumberComboBox, gbc);

        // GPU Number
        gbc.gridx = 0;
        gbc.gridy = currentRow++;
        gbc.weightx = 0.0;
        add(new JLabel("GPU Number:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gpuNumberComboBox = new JComboBox<>(new String[] { "1", "2", "3", "4" });
        gpuNumberComboBox.addActionListener(e -> updateGpuNumber());
        add(gpuNumberComboBox, gbc);
        // GPU Min Capacity
        gbc.gridx = 0;
        gbc.gridy = currentRow++;
        gbc.weightx = 0.0;
        add(new JLabel("GPU Min Capacity (GB):"), gbc);
        gbc.gridx = 1;
        gbc.weightx =1.0;
        gpuMinCapacityComboBox = new JComboBox<>(new String[] { "Any","2", "4", "6", "8", "10", "12", "16", "20", "24", "28", "32"});
        gpuMinCapacityComboBox.addActionListener(e -> updateGpuMinCapacity());
        add(gpuMinCapacityComboBox, gbc);

        // GPU Max Capacity
        gbc.gridx = 0;
        gbc.gridy = currentRow++;
        gbc.weightx = 0.0;
        add(new JLabel("GPU Max Capacity (GB):"), gbc);
        gbc.gridx = 1;
        gbc.weightx =1.0;
        gpuMaxCapacityComboBox = new JComboBox<>(new String[] { "Any","2", "4", "6", "8", "10", "12", "16", "20", "24", "28", "32"});
        gpuMaxCapacityComboBox.addActionListener(e -> updateGpuMaxCapacity());
        add(gpuMaxCapacityComboBox, gbc);

        // RAM Capacity
        gbc.gridx = 0;
        gbc.gridy = currentRow++;
        gbc.weightx = 0.0;
        add(new JLabel("RAM Capacity (GB):"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        ramCapacityComboBox = new JComboBox<>(new String[] { "8", "16", "32", "64", "128" });
        ramCapacityComboBox.addActionListener(e -> updateRamCapacity());
        add(ramCapacityComboBox, gbc);

        // Mainboard Size
        gbc.gridx = 0;
        gbc.gridy = currentRow++;
        gbc.weightx = 0.0;
        add(new JLabel("Mainboard Size:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainSizeComboBox = new JComboBox<>(new String[] { "ATX", "mATX" });
        mainSizeComboBox.addActionListener(e -> updateMainboardSize());
        add(mainSizeComboBox, gbc);

        // Hard disk type
        gbc.gridx = 0;
        gbc.gridy = currentRow++;
        gbc.weightx = 0.0;
        add(new JLabel("Hard Disk Type:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        hdTypeComboBox = new JComboBox<>(new String[] { "HDD", "SSD", "NVME" });
        hdTypeComboBox.addActionListener(e -> updateHardDiskType());
        add(hdTypeComboBox, gbc);

        // Hard disk capacity
        gbc.gridx = 0;
        gbc.gridy = currentRow++;
        gbc.weightx = 0.0;
        add(new JLabel("Hard Disk Min Capacity:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        hdMinCapacityComboBox = new JComboBox<>(new String[] { "120", "240", "480", "960", "1920" });
        hdMinCapacityComboBox.addActionListener(e -> updateHardDiskMinCapacity());
        add(hdMinCapacityComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = currentRow++;
        gbc.weightx = 0.0;
        add(new JLabel("Hard Disk Max Capacity:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        hdMaxCapacityComboBox = new JComboBox<>(new String[] { "128", "256", "512", "1024", "2048" });
        hdMaxCapacityComboBox.addActionListener(e -> updateHardDiskMaxCapacity());
        add(hdMaxCapacityComboBox, gbc);

        // Max Search Time
        gbc.gridx = 0;
        gbc.gridy = currentRow++;
        gbc.weightx = 0.0;
        add(new JLabel("Max Search Time (seconds):"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        maxSearchTimeComboBox = new JComboBox<>(new String[] { "30", "60", "120", "300", "600" });
        maxSearchTimeComboBox.addActionListener(e -> updateMaxSearchTime());
        add(maxSearchTimeComboBox, gbc);

        // Set initial defaults based on Profile 1
        updateProfileSettings();
    }

    private void updateProfileSettings() {
        String selectedProfile = (String) usageProfileBox.getSelectedItem();

        if (selectedProfile == null) {
            return;
        }

        // Set defaults based on selected profile
        if (selectedProfile.startsWith("Profile 1")) {
            // Profile 1: Normal (Student, Office)
            cpuNumberComboBox.setSelectedItem("1");
            gpuNumberComboBox.setSelectedItem("1");
            gpuMinCapacityComboBox.setSelectedItem("2");
            gpuMaxCapacityComboBox.setSelectedItem("6");
            ramCapacityComboBox.setSelectedItem("16");
            mainSizeComboBox.setSelectedItem("mATX");
            hdTypeComboBox.setSelectedItem("SSD");
            hdMinCapacityComboBox.setSelectedItem("240");
            hdMaxCapacityComboBox.setSelectedItem("256");
        } else if (selectedProfile.startsWith("Profile 2")) {
            // Profile 2: Developer, IT related
            cpuNumberComboBox.setSelectedItem("1");
            gpuNumberComboBox.setSelectedItem("1");
            gpuMinCapacityComboBox.setSelectedItem("6");
            gpuMaxCapacityComboBox.setSelectedItem("12");
            ramCapacityComboBox.setSelectedItem("32");
            mainSizeComboBox.setSelectedItem("mATX");
            hdTypeComboBox.setSelectedItem("SSD");
            hdMinCapacityComboBox.setSelectedItem("480");
            hdMaxCapacityComboBox.setSelectedItem("512");
        } else if (selectedProfile.startsWith("Profile 3")) {
            // Profile 3: Gaming, Video Editor
            cpuNumberComboBox.setSelectedItem("1");
            gpuNumberComboBox.setSelectedItem("1");
            gpuMinCapacityComboBox.setSelectedItem("12");
            gpuMaxCapacityComboBox.setSelectedItem("Any");
            ramCapacityComboBox.setSelectedItem("64");
            mainSizeComboBox.setSelectedItem("mATX");
            hdTypeComboBox.setSelectedItem("NVME");
            hdMinCapacityComboBox.setSelectedItem("480");
            hdMaxCapacityComboBox.setSelectedItem("512");
        } else if (selectedProfile.startsWith("Profile 4")) {
            // Profile 4: Enterprise
            cpuNumberComboBox.setSelectedItem("2");
            gpuNumberComboBox.setSelectedItem("2");
            gpuMinCapacityComboBox.setSelectedItem("16");
            gpuMaxCapacityComboBox.setSelectedItem("Any");
            ramCapacityComboBox.setSelectedItem("128");
            mainSizeComboBox.setSelectedItem("ATX");
            hdTypeComboBox.setSelectedItem("NVME");
            hdMinCapacityComboBox.setSelectedItem("960");
            hdMaxCapacityComboBox.setSelectedItem("1024");
        }
    }

    // budget
    private void updateBudget() {
        try {
            String budgetText = budgetField.getText().trim().replace(",", "");
            if (!budgetText.isEmpty()) {
                double bBudget = Double.parseDouble(budgetText);
                if (bBudget <= 0) {
                    showError("Budget must be greater than 0");
                    return;
                }
                componentManager.baseBudget = bBudget;
            }
        } catch (NumberFormatException e) {
            // Don't show error while typing
        }
    }
    // color
    private void updateColorSettings() {
        String sColor = (String) colorComboBox.getSelectedItem();
        if (sColor != null) {
            componentManager.basePreferredColor = sColor;
            System.out.println("sColor: " + sColor);
        }
    }

    // cpu number
    private void updateCpuNumber() {
        int cpuNumber = Integer.parseInt((String) cpuNumberComboBox.getSelectedItem());
        componentManager.baseCpuNumber = cpuNumber;
        System.out.println("cpuNumber: " + cpuNumber);
    }

    // gpu number
    private void updateGpuNumber() {
        int gpuNumber = Integer.parseInt((String) gpuNumberComboBox.getSelectedItem());
        componentManager.baseGpuNumber = gpuNumber;
    }
    // gpu min capacity
    private void updateGpuMinCapacity() {
        if("Any".equals(gpuMaxCapacityComboBox.getSelectedItem())){
            componentManager.baseGpuMinCapacity = 0;
            return;
        }
        int gpuMinCapacity = Integer.parseInt((String) gpuMinCapacityComboBox.getSelectedItem());
        componentManager.baseGpuMinCapacity = gpuMinCapacity;
    }
    // gpu max capacity
    private void updateGpuMaxCapacity() {
        if("Any".equals(gpuMaxCapacityComboBox.getSelectedItem())){
            componentManager.baseGpuMaxCapacity = Integer.MAX_VALUE;
            return;
        }
        int gpuMaxCapacity = Integer.parseInt((String) gpuMaxCapacityComboBox.getSelectedItem());
        componentManager.baseGpuMaxCapacity = gpuMaxCapacity;
    }

    // ram capacity
    private void updateRamCapacity() {
        int ramCapacity = Integer.parseInt((String) ramCapacityComboBox.getSelectedItem());
        componentManager.baseRamCapacity = ramCapacity;
    }

    // main
    private void updateMainboardSize() {
        String sMainSize = (String) mainSizeComboBox.getSelectedItem();
        componentManager.baseMainSize = sMainSize;
    }

    // hd type
    private void updateHardDiskType() {
        String sHdType = (String) hdTypeComboBox.getSelectedItem();
        componentManager.baseHardDiskType = sHdType;
    }

    // hd min capacity
    private void updateHardDiskMinCapacity() {
        int hdMinCapacity = Integer.parseInt((String) hdMinCapacityComboBox.getSelectedItem());
        componentManager.baseHardDiskMinCapacity = hdMinCapacity;
    }

    private void updateHardDiskMaxCapacity() {
        int hdMaxCapacity = Integer.parseInt((String) hdMaxCapacityComboBox.getSelectedItem());
        componentManager.baseHardDiskMaxCapacity = hdMaxCapacity;
    }

    // max search time
    private void updateMaxSearchTime() {
        int maxSearchTime = Integer.parseInt((String) maxSearchTimeComboBox.getSelectedItem());
        componentManager.baseMaxSearchTime = maxSearchTime;
    }

    @Override
    public void refreshComponents() {
    };

    @Override
    protected void applyFilters() {
    }

    @Override
    protected void clearFilters() {
    }

    @Override
    public void updateTable() {
    };

    @Override
    protected void selectSuitableComponent() {
    };

    @Override
    public boolean validateSelection() {
        try {
            double sBudget = Double.parseDouble(budgetField.getText().trim().replace(",", ""));
            System.out.println("sBudget: " + sBudget);
            if (sBudget < 0) {
                showError("Budget must be greater than 0");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Please enter a valid budget amount");
            return false;
        }
        int hdMinCapacity = Integer.parseInt(hdMinCapacityComboBox.getSelectedItem().toString());
        int hdMaxCapacity = Integer.parseInt(hdMaxCapacityComboBox.getSelectedItem().toString());
        if (hdMinCapacity > hdMaxCapacity) {
            showError("Min capacity must be less than max capacity");
            return false;
        }
        
        updateBudget();
        updateColorSettings();
        updateCpuNumber();
        updateGpuNumber();
        updateGpuMinCapacity();
        updateGpuMaxCapacity();
        if(componentManager.baseGpuMinCapacity > componentManager.baseGpuMaxCapacity){
            showError("Min capacity must be less than max capacity");
            return false;
        }
        updateRamCapacity();
        updateMainboardSize();
        updateHardDiskType();
        updateHardDiskMinCapacity();
        updateHardDiskMaxCapacity();
        updateMaxSearchTime();
        System.out.println("Step1 budget: " + componentManager.baseBudget);
        System.out.println("Step1 color: " + componentManager.basePreferredColor);
        System.out.println("Step1 cpuNumber: " + componentManager.baseCpuNumber);
        System.out.println("Step1 gpuNumber: " + componentManager.baseGpuNumber);
        System.out.println("Step1 gpuMinCapacity: " + componentManager.baseGpuMinCapacity);
        System.out.println("Step1 gpuMaxCapacity: " + componentManager.baseGpuMaxCapacity);
        System.out.println("Step1 ramCapacity: " + componentManager.baseRamCapacity);
        System.out.println("Step1 mainSize: " + componentManager.baseMainSize);
        System.out.println("Step1 hdType: " + componentManager.baseHardDiskType);
        System.out.println("Step1 hdMinCapacity: " + componentManager.baseHardDiskMinCapacity);
        System.out.println("Step1 hdMaxCapacity: " + componentManager.baseHardDiskMaxCapacity);
        return true;
    }

    @Override
    public void onSelectionChanged() {
    }

    @Override
    protected void updateInformationLabel() {
    }

    @Override
    protected void onPressBackButton() {
    }

    @Override
    protected void updateWizardStepDataTable() {
    }

}
/*
 * budgetField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                formatBudgetField();
                updateBudget();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                formatBudgetField();
                updateBudget();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                formatBudgetField();
                updateBudget();
            }
        });

     private void formatBudgetField() {
        if (isUpdating)
            return;
        isUpdating = true;

        SwingUtilities.invokeLater(() -> {
            try {
                String text = budgetField.getText().replaceAll("[^\\d]", "");
                if (!text.isEmpty()) {
                    double value = Double.parseDouble(text);
                    String formatted = priceFormatter.format(value);
                    budgetField.setText(formatted);
                }
            } catch (NumberFormatException e) {
                // Ignore formatting errors while typing
            } finally {
                isUpdating = false;
            }
        });
    }
 */