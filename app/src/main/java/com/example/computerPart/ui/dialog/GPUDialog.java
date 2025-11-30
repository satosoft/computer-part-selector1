package com.example.computerPart.ui.dialog;

import com.example.computerPart.model.GPU;
import javax.swing.*;
import java.awt.*;

public class GPUDialog extends BaseComponentDialog<GPU> {
    private JTextField capacityField;
    private JTextField benchmarkField;

    public GPUDialog(GPU gpu) {
        super(gpu);
        initializeAdditionalFields();
        setupAdditionalFields();

        if (component != null) {
            populateFields();
        }
    }

    @Override
    protected void initializeAdditionalFields() {
        capacityField = new JTextField(20);
        benchmarkField = new JTextField(20);
    }

    @Override
    protected void setupAdditionalFields() {
        addComponent("Capacity (GB):", capacityField);
        addComponent("Benchmark:", benchmarkField);
    }

    @Override
    protected void populateFields() {
        populateBaseFields();
        capacityField.setText(String.valueOf(component.getCapacity()));
        benchmarkField.setText(String.valueOf(component.getBenchmark()));
    }

    @Override
    protected boolean validateInput() {
        if (!validateBaseInput()) {
            return false;
        }

        try {
            if (capacityField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("All fields must be filled");
            }

            int capacity = Integer.parseInt(capacityField.getText());
            double benchmark = Double.parseDouble(benchmarkField.getText());

            if (capacity < 0 || benchmark < 0) {
                throw new IllegalArgumentException("Numeric values cannot be negative");
            }

            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    @Override
    public GPU getComponentPart() {
        int id = component != null ? component.getId() : 0;
        return new GPU(
                id,
                nameField.getText().trim(),
                manufacturerField.getText().trim(),                
                Integer.parseInt(stockField.getText()),
                Integer.parseInt(powerField.getText()),
                (String) colorBox.getSelectedItem(),
                Double.parseDouble(priceField.getText()),
                
                Integer.parseInt(capacityField.getText()),
                Double.parseDouble(benchmarkField.getText())
                
            );
    }
}

// package com.example.computerPart.ui.dialog;

// import com.example.computerPart.model.GPU;
// import javax.swing.*;
// import java.awt.*;
// import com.example.computerPart.model.CPU;
// import javax.swing.*;
// import java.awt.*;

/*
 * public class GPUDialog extends BaseComponentDialog<GPU> {
 * private final JTextField capacityField;
 * private final JTextField benchmarkField;
 * private final JComboBox<String> colorField;
 * 
 * public GPUDialog(GPU gpu) {
 * super(gpu, "GPU");
 * 
 * // Initialize GPU-specific fields
 * capacityField = new JTextField(20);
 * benchmarkField = new JTextField(20);
 * colorField = new JComboBox<>(new String[] { "Black", "Silver", "White", "RGB"
 * });
 * 
 * // Set existing values if editing
 * if (gpu != null) {
 * capacityField.setText(String.valueOf(gpu.getCapacity()));
 * benchmarkField.setText(String.valueOf(gpu.getBenchmark()));
 * colorField.setSelectedItem(gpu.getColor());
 * }
 * }
 * 
 * @Override
 * protected void initializePowerField(GPU component) {
 * powerField.setText(String.valueOf(component.getPower()));
 * }
 * 
 * @Override
 * protected void createFormPanel(JPanel panel, GridBagConstraints gbc) {
 * addField(panel, "Name:", nameField, gbc);
 * addField(panel, "Manufacturer:", manufacturerField, gbc);
 * addField(panel, "Color:", colorField, gbc);
 * addField(panel, "Capacity (GB):", capacityField, gbc);
 * addField(panel, "Benchmark:", benchmarkField, gbc);
 * addField(panel, "Price:", priceField, gbc);
 * addField(panel, "Power (W):", powerField, gbc);
 * addField(panel, "Stock:", stockField, gbc);
 * }
 * 
 * @Override
 * protected boolean validateAdditionalFields() {
 * try {
 * Integer.parseInt(capacityField.getText().trim());
 * Double.parseDouble(benchmarkField.getText().trim());
 * return true;
 * } catch (NumberFormatException e) {
 * JOptionPane.showMessageDialog(this,
 * "Please enter valid numbers for capacity and benchmark",
 * "Validation Error", JOptionPane.ERROR_MESSAGE);
 * return false;
 * }
 * }
 * 
 * @Override
 * protected GPU createSpecificComponent(int id, String name, String
 * manufacturer, double price, int stock, int power,
 * String color) {
 * return new GPU(
 * id,
 * name,
 * manufacturer,
 * price,
 * stock,
 * Integer.parseInt(capacityField.getText().trim()),
 * Double.parseDouble(benchmarkField.getText().trim()),
 * power,
 * color);
 * }
 * }
 */

/*
 * package com.example.computerPart.ui.dialog;
 * 
 * import com.example.computerPart.model.CPU;
 * import javax.swing.*;
 * import java.awt.*;
 * 
 * public class CPUDialog extends JDialog {
 * private boolean confirmed = false;
 * private CPU cpu;
 * 
 * private JTextField nameField;
 * private JTextField manufacturerField;
 * private JTextField generationField;
 * private JTextField socketField;
 * private JTextField benchmarkField;
 * private JTextField priceField;
 * private JTextField powerField;
 * private JTextField stockField;
 * private JTextField coreNumField;
 * private JComboBox<String> colorBox;
 * 
 * public CPUDialog(CPU cpu) {
 * this.cpu = cpu;
 * setTitle(cpu == null ? "Add New CPU" : "Edit CPU");
 * setModal(true);
 * setLayout(new BorderLayout());
 * 
 * // Create main panel with GridBagLayout
 * JPanel mainPanel = new JPanel(new GridBagLayout());
 * GridBagConstraints gbc = new GridBagConstraints();
 * gbc.insets = new Insets(5, 5, 5, 5);
 * gbc.fill = GridBagConstraints.HORIZONTAL;
 * 
 * // Initialize components
 * nameField = new JTextField(20);
 * manufacturerField = new JTextField(20);
 * generationField = new JTextField(20);
 * socketField = new JTextField(20);
 * benchmarkField = new JTextField(20);
 * priceField = new JTextField(20);
 * powerField = new JTextField(20);
 * stockField = new JTextField(20);
 * coreNumField = new JTextField(20);
 * colorBox = new JComboBox<>(new String[]{"Black", "White", "Silver"});
 * 
 * // Add components to panel
 * int row = 0;
 * addComponent(mainPanel, gbc, "Name:", nameField, row++);
 * addComponent(mainPanel, gbc, "Manufacturer:", manufacturerField, row++);
 * addComponent(mainPanel, gbc, "Generation:", generationField, row++);
 * addComponent(mainPanel, gbc, "Socket:", socketField, row++);
 * addComponent(mainPanel, gbc, "Benchmark:", benchmarkField, row++);
 * addComponent(mainPanel, gbc, "Price:", priceField, row++);
 * addComponent(mainPanel, gbc, "Power (W):", powerField, row++);
 * addComponent(mainPanel, gbc, "Stock:", stockField, row++);
 * addComponent(mainPanel, gbc, "Core Number:", coreNumField, row++);
 * addComponent(mainPanel, gbc, "Color:", colorBox, row);
 * 
 * // If editing, populate fields with CPU data
 * if (cpu != null) {
 * populateFields();
 * }
 * 
 * // Button panel
 * JPanel buttonPanel = new JPanel();
 * JButton okButton = new JButton("OK");
 * JButton cancelButton = new JButton("Cancel");
 * 
 * okButton.addActionListener(e -> {
 * if (validateInput()) {
 * confirmed = true;
 * dispose();
 * }
 * });
 * 
 * cancelButton.addActionListener(e -> dispose());
 * 
 * buttonPanel.add(okButton);
 * buttonPanel.add(cancelButton);
 * 
 * // Add panels to dialog
 * add(mainPanel, BorderLayout.CENTER);
 * add(buttonPanel, BorderLayout.SOUTH);
 * 
 * pack();
 * setLocationRelativeTo(null);
 * }
 * 
 * private void addComponent(JPanel panel, GridBagConstraints gbc, String label,
 * JComponent component, int row) {
 * gbc.gridx = 0;
 * gbc.gridy = row;
 * panel.add(new JLabel(label), gbc);
 * 
 * gbc.gridx = 1;
 * panel.add(component, gbc);
 * }
 * 
 * private void populateFields() {
 * nameField.setText(cpu.getName());
 * manufacturerField.setText(cpu.getManufacturer());
 * generationField.setText(cpu.getGeneration());
 * socketField.setText(cpu.getSocket());
 * benchmarkField.setText(String.valueOf(cpu.getBenchmark()));
 * priceField.setText(String.valueOf(cpu.getPrice()));
 * powerField.setText(String.valueOf(cpu.getPower()));
 * stockField.setText(String.valueOf(cpu.getStock()));
 * coreNumField.setText(String.valueOf(cpu.getCoreNum()));
 * colorBox.setSelectedItem(cpu.getColor());
 * }
 * 
 * private boolean validateInput() {
 * try {
 * if (nameField.getText().trim().isEmpty() ||
 * manufacturerField.getText().trim().isEmpty() ||
 * generationField.getText().trim().isEmpty() ||
 * socketField.getText().trim().isEmpty()) {
 * throw new IllegalArgumentException("All fields must be filled");
 * }
 * 
 * double benchmark = Double.parseDouble(benchmarkField.getText());
 * double price = Double.parseDouble(priceField.getText());
 * int power = Integer.parseInt(powerField.getText());
 * int stock = Integer.parseInt(stockField.getText());
 * int coreNum = Integer.parseInt(coreNumField.getText());
 * 
 * if (benchmark < 0 || price < 0 || power < 0 || stock < 0 || coreNum < 0) {
 * throw new IllegalArgumentException("Numeric values cannot be negative");
 * }
 * 
 * return true;
 * } catch (NumberFormatException e) {
 * JOptionPane.showMessageDialog(this, "Please enter valid numbers",
 * "Input Error", JOptionPane.ERROR_MESSAGE);
 * return false;
 * } catch (IllegalArgumentException e) {
 * JOptionPane.showMessageDialog(this, e.getMessage(), "Input Error",
 * JOptionPane.ERROR_MESSAGE);
 * return false;
 * }
 * }
 * 
 * public boolean showDialog() {
 * setVisible(true);
 * return confirmed;
 * }
 * 
 * public CPU getBaseComponent() {
 * int id = cpu != null ? cpu.getId() : 0;
 * return new CPU(
 * id,
 * nameField.getText().trim(),
 * manufacturerField.getText().trim(),
 * Double.parseDouble(priceField.getText()),
 * Integer.parseInt(stockField.getText()),
 * generationField.getText().trim(),
 * socketField.getText().trim(),
 * Double.parseDouble(benchmarkField.getText()),
 * Integer.parseInt(powerField.getText()),
 * Integer.parseInt(coreNumField.getText()),
 * (String) colorBox.getSelectedItem()
 * );
 * }
 * }
 */