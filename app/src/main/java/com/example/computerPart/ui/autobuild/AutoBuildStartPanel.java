package com.example.computerPart.ui.autobuild;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class AutoBuildStartPanel extends JPanel {
    // private final JTextField budgetField;
    private final JFormattedTextField budgetField;
    private final JComboBox<String> colorBox;
    private final JComboBox<String> usageProfileBox;
    private final JComboBox<String> cpuNumberBox;
    private final JComboBox<String> gpuNumberBox;
    private final JComboBox<String> ramCapacityBox;
    private final JComboBox<String> mainSizeBox;
    private final JComboBox<String> maxTimeBox;
    private final JComboBox<String> hdMinCapacityComboBox;
    private final JComboBox<String> hdMaxCapacityComboBox;
    private final JComboBox<String> hdTypeComboBox;
    private final JComboBox<String> gpuMinCapacityComboBox;
    private final JComboBox<String> gpuMaxCapacityComboBox;
    private final NumberFormat priceFormatter;
    // private final JButton autoSelectButton;
    //// private boolean autoSelectRequested = true;
    private int currentRow = 0;

    public AutoBuildStartPanel() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Step 1: Basic Requirements"));

        priceFormatter = NumberFormat.getNumberInstance(Locale.US);
        priceFormatter.setGroupingUsed(true);
        priceFormatter.setMaximumFractionDigits(0);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Budget
        gbc.gridx = 0;
        gbc.gridy = currentRow++;
        add(new JLabel("Budget (VND):"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        // budgetField = new JTextField(10);
        budgetField = new JFormattedTextField(priceFormatter);

        add(budgetField, gbc);

        // Color preference
        gbc.gridx = 0;
        gbc.gridy = currentRow++;
        gbc.weightx = 0;
        add(new JLabel("Preferred Color:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        colorBox = new JComboBox<>(new String[] { "Any", "Black", "White" });
        add(colorBox, gbc);

        // Usage Profile
        gbc.gridx = 0;
        gbc.gridy = currentRow++;
        gbc.weightx = 0;
        add(new JLabel("Usage Profile:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        usageProfileBox = new JComboBox<>(new String[] {
                "Profile 1: Normal (Student, Office)",
                "Profile 2: Developer, IT related",
                "Profile 3: Gaming, Video Editor",
                "Profile 4: Enterprise"
        });
        usageProfileBox.addActionListener(e -> updateDefaultsBasedOnProfile());
        add(usageProfileBox, gbc);

        // CPU Number
        gbc.gridx = 0;
        gbc.gridy = currentRow++;
        gbc.weightx = 0;
        add(new JLabel("Number of CPUs:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cpuNumberBox = new JComboBox<>(new String[] { "1", "2", "Any" });
        add(cpuNumberBox, gbc);

        // GPU Number
        gbc.gridx = 0;
        gbc.gridy = currentRow++;
        gbc.weightx = 0;
        add(new JLabel("Number of GPUs:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gpuNumberBox = new JComboBox<>(new String[] { "1", "2", "3", "4", "Any" });
        add(gpuNumberBox, gbc);

        // GPU Min Capacity
        gbc.gridx = 0;
        gbc.gridy = currentRow++;
        gbc.weightx = 0.0;
        add(new JLabel("GPU Min Capacity:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gpuMinCapacityComboBox = new JComboBox<>(
                new String[] { "Any", "2", "4", "6", "8", "10", "12", "16", "20", "24", "32" });
        add(gpuMinCapacityComboBox, gbc);

        // GPU Max Capacity
        gbc.gridx = 0;
        gbc.gridy = currentRow++;
        gbc.weightx = 0.0;
        add(new JLabel("GPU Max Capacity:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gpuMaxCapacityComboBox = new JComboBox<>(
                new String[] { "Any", "2", "4", "6", "8", "10", "12", "16", "20", "24", "32" });
        add(gpuMaxCapacityComboBox, gbc);

        // RAM Capacity
        gbc.gridx = 0;
        gbc.gridy = currentRow++;
        gbc.weightx = 0;
        add(new JLabel("RAM Capacity (GB):"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        ramCapacityBox = new JComboBox<>(new String[] { "8", "16", "32", "64", "128" });
        add(ramCapacityBox, gbc);

        // Mainboard Size
        gbc.gridx = 0;
        gbc.gridy = currentRow++;
        gbc.weightx = 0;
        add(new JLabel("Mainboard Size:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainSizeBox = new JComboBox<>(new String[] { "ATX", "mATX", "Any" });
        add(mainSizeBox, gbc);

        // Hard disk type
        gbc.gridx = 0;
        gbc.gridy = currentRow++;// 7
        gbc.weightx = 0.0;
        add(new JLabel("Hard Disk Type:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        hdTypeComboBox = new JComboBox<>(new String[] { "HDD", "SSD", "NVME" });
        // hdTypeComboBox.addActionListener(e -> updateHardDiskType());
        add(hdTypeComboBox, gbc);

        // Hard disk capacity
        gbc.gridx = 0;
        gbc.gridy = currentRow++;
        gbc.weightx = 0.0;
        add(new JLabel("Hard Disk Min Capacity:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        hdMinCapacityComboBox = new JComboBox<>(new String[] { "120", "240", "480", "960", "1920" });
        // hdMinCapacityComboBox.addActionListener(e -> updateHardDiskMinCapacity());
        add(hdMinCapacityComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = currentRow++;
        gbc.weightx = 0.0;
        add(new JLabel("Hard Disk Max Capacity:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        hdMaxCapacityComboBox = new JComboBox<>(new String[] { "128", "256", "512", "1024", "2048" });
        // hdMaxCapacityComboBox.addActionListener(e -> updateHardDiskMaxCapacity());
        add(hdMaxCapacityComboBox, gbc);

        // Max Search Time
        gbc.gridx = 0;
        gbc.gridy = currentRow++;
        gbc.weightx = 0;
        add(new JLabel("Max Search Time (seconds):"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        maxTimeBox = new JComboBox<>(new String[] { "30", "60", "120", "300", "600" });
        maxTimeBox.setSelectedItem(60); // Default to 1 minute (60 seconds)
        maxTimeBox.setToolTipText("Maximum time to search for combinations before showing results");
        add(maxTimeBox, gbc);

        // Auto Select Button
        /*
         * gbc.gridx = 0;
         * gbc.gridy = 8;
         * gbc.gridwidth = 2;
         * gbc.anchor = GridBagConstraints.CENTER;
         * autoSelectButton = new JButton("Auto Select");
         * autoSelectButton.
         * setToolTipText("Automatically select compatible components based on your preferences"
         * );
         * autoSelectButton.addActionListener(e -> {
         * if (validateInput()) {
         * autoSelectRequested = true;
         * // The wizard will handle the auto-selection process
         * }
         * });
         * add(autoSelectButton, gbc);
         */

        // Set initial defaults based on Profile 1
        updateDefaultsBasedOnProfile();
    }

    private void updateDefaultsBasedOnProfile() {
        String selectedProfile = (String) usageProfileBox.getSelectedItem();

        if (selectedProfile == null) {
            return;
        }

        // Set defaults based on selected profile
        if (selectedProfile.startsWith("Profile 1")) {
            // Profile 1: Normal (Student, Office)
            cpuNumberBox.setSelectedItem("1");
            gpuNumberBox.setSelectedItem("1");
            gpuMinCapacityComboBox.setSelectedItem("2");
            gpuMaxCapacityComboBox.setSelectedItem("6");
            ramCapacityBox.setSelectedItem("16");
            mainSizeBox.setSelectedItem("mATX");
            hdTypeComboBox.setSelectedItem("SSD");
            hdMinCapacityComboBox.setSelectedItem("240");
            hdMaxCapacityComboBox.setSelectedItem("256");
        } else if (selectedProfile.startsWith("Profile 2")) {
            // Profile 2: Developer, IT related
            cpuNumberBox.setSelectedItem("1");
            gpuNumberBox.setSelectedItem("1");
            gpuMinCapacityComboBox.setSelectedItem("6");
            gpuMaxCapacityComboBox.setSelectedItem("12");
            ramCapacityBox.setSelectedItem("32");
            mainSizeBox.setSelectedItem("mATX");
            hdTypeComboBox.setSelectedItem("SSD");
            hdMinCapacityComboBox.setSelectedItem("480");
            hdMaxCapacityComboBox.setSelectedItem("512");
        } else if (selectedProfile.startsWith("Profile 3")) {
            // Profile 3: Gaming, Video Editor
            cpuNumberBox.setSelectedItem("1");
            gpuNumberBox.setSelectedItem("1");
            gpuMinCapacityComboBox.setSelectedItem("12");
            gpuMaxCapacityComboBox.setSelectedItem("Any");
            ramCapacityBox.setSelectedItem("64");
            mainSizeBox.setSelectedItem("mATX");
            hdTypeComboBox.setSelectedItem("NVME");
            hdMinCapacityComboBox.setSelectedItem("480");
            hdMaxCapacityComboBox.setSelectedItem("512");
        } else if (selectedProfile.startsWith("Profile 4")) {
            // Profile 4: Enterprise
            cpuNumberBox.setSelectedItem("2");
            gpuNumberBox.setSelectedItem("2");
            gpuMinCapacityComboBox.setSelectedItem("16");
            gpuMaxCapacityComboBox.setSelectedItem("Any");
            ramCapacityBox.setSelectedItem("128");
            mainSizeBox.setSelectedItem("ATX");
            hdTypeComboBox.setSelectedItem("NVME");
            hdMinCapacityComboBox.setSelectedItem("960");
            hdMaxCapacityComboBox.setSelectedItem("1024");
        }
    }

    public double getBudget() throws NumberFormatException {
        String budgetText = budgetField.getText().trim().replace(",", "");
        double budget = Double.parseDouble(budgetText);
        if (budget <= 0) {
            throw new NumberFormatException("Budget must be greater than 0");
        }
        return budget;
    }

    public String getPreferredColor() {
        return (String) colorBox.getSelectedItem();
    }

    public int getCpuNumber() {
        String cpuNumber = (String) cpuNumberBox.getSelectedItem();
        return "Any".equals(cpuNumber) ? 0 : Integer.parseInt(cpuNumber);
    }

    public int getGpuNumber() {
        String gpuNumber = (String) gpuNumberBox.getSelectedItem();
        return "Any".equals(gpuNumber) ? 0 : Integer.parseInt(gpuNumber);
    }

    public int getGpuMinCapacity() {
        if ("Any".equals(gpuMinCapacityComboBox.getSelectedItem())) {
            return 0;
        }
        return Integer.parseInt((String) gpuMinCapacityComboBox.getSelectedItem());
    }

    public int getGpuMaxCapacity() {
        if ("Any".equals(gpuMaxCapacityComboBox.getSelectedItem())) {
            return Integer.MAX_VALUE;
        }
        return Integer.parseInt((String) gpuMaxCapacityComboBox.getSelectedItem());
    }

    public int getRamCapacity() {
        return Integer.parseInt((String) ramCapacityBox.getSelectedItem());
    }

    public String getMainSize() {
        return (String) mainSizeBox.getSelectedItem();
    }

    public int getMaxSearchTime() {
        return Integer.parseInt((String) maxTimeBox.getSelectedItem());
    }

    public String getHdType() {
        return (String) hdTypeComboBox.getSelectedItem();
    }

    public int getHdMinCapacity() {
        return Integer.parseInt((String) hdMinCapacityComboBox.getSelectedItem());
    }

    public int getHdMaxCapacity() {
        return Integer.parseInt((String) hdMaxCapacityComboBox.getSelectedItem());
    }

    public boolean validateInput() {
        try {
            getBudget();
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid budget amount",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // public boolean isAutoSelectRequested() {
    // return autoSelectRequested;
    // }

    // public void resetAutoSelectRequest() {
    // autoSelectRequested = false;
    // }
}