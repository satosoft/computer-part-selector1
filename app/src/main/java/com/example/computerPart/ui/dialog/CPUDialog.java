package com.example.computerPart.ui.dialog;

import com.example.computerPart.model.CPU;
import javax.swing.*;
import java.awt.*;

public class CPUDialog extends BaseComponentDialog<CPU> {
    private JTextField generationField;
    private JTextField socketField;
    private JTextField benchmarkField;
    private JTextField coreNumField;

    public CPUDialog(CPU cpu) {
        super(cpu);
        initializeAdditionalFields();
        setupAdditionalFields();
        if (component != null) {
            populateFields();
        }
    }
    @Override
    protected void initializeAdditionalFields() {
        generationField = new JTextField(20);
        socketField = new JTextField(20);
        benchmarkField = new JTextField(20);
        coreNumField = new JTextField(20);
    }
    @Override
    protected void setupAdditionalFields() {
        addComponent("Generation:", generationField);
        addComponent("Socket:", socketField);
        addComponent("Benchmark:", benchmarkField);
        addComponent("Core Number:", coreNumField);
    }
    @Override
    protected void populateFields() {
        populateBaseFields();
        generationField.setText(component.getGeneration());
        socketField.setText(component.getSocket());
        benchmarkField.setText(String.valueOf(component.getBenchmark()));
        coreNumField.setText(String.valueOf(component.getCoreNum()));
    }

    @Override
    protected boolean validateInput() {
        if (!validateBaseInput()) {
            return false;
        }

        try {
            if (generationField.getText().trim().isEmpty() ||
                    socketField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("All fields must be filled");
            }

            double benchmark = Double.parseDouble(benchmarkField.getText());
            int coreNum = Integer.parseInt(coreNumField.getText());

            if (benchmark < 0 || coreNum < 0) {
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

    public CPU getComponentPart() {
        int id = component != null ? component.getId() : 0;
        return new CPU(
                id,
                nameField.getText().trim(),
                manufacturerField.getText().trim(),
                Integer.parseInt(stockField.getText()),
                Integer.parseInt(powerField.getText()), 
                (String) colorBox.getSelectedItem(),
                Double.parseDouble(priceField.getText()),
                
                generationField.getText().trim(),
                socketField.getText().trim(),
                Double.parseDouble(benchmarkField.getText()),
                Integer.parseInt(coreNumField.getText())
                
        );
    }
}

// public class CPUDialog extends BaseComponentDialog {
// private boolean confirmed = false;
// private CPU cpu;
// private JTextField generationField;
// private JTextField socketField;
// private JTextField benchmarkField;
// private JTextField coreNumField;

// public CPUDialog(CPU cpu) {
// super(cpu);
// setTitle(cpu == null ? "Add New CPU" : "Edit CPU");
// setModal(true);
// setLayout(new BorderLayout());

// Create main panel with GridBagLayout
// JPanel mainPanel = new JPanel(new GridBagLayout());
// GridBagConstraints gbc = new GridBagConstraints();
// gbc.insets = new Insets(5, 5, 5, 5);
// gbc.fill = GridBagConstraints.HORIZONTAL;

// Initialize components
// nameField = new JTextField(20);
// manufacturerField = new JTextField(20);
// generationField = new JTextField(20);
// socketField = new JTextField(20);
// benchmarkField = new JTextField(20);
// priceField = new JTextField(20);
// powerField = new JTextField(20);
// stockField = new JTextField(20);
// coreNumField = new JTextField(20);
// colorBox = new JComboBox<>(new String[]{"Black", "White", "Silver"});
// initializeAdditionalFields();

// // Add components to panel
// int row = 0;
// addComponent(formPanel, gbc, "Name:", nameField, row++);
// addComponent(formPanel, gbc, "Manufacturer:", manufacturerField, row++);
// addComponent(formPanel, gbc, "Generation:", generationField, row++);
// addComponent(formPanel, gbc, "Socket:", socketField, row++);
// addComponent(formPanel, gbc, "Benchmark:", benchmarkField, row++);
// addComponent(formPanel, gbc, "Price:", priceField, row++);
// addComponent(formPanel, gbc, "Power (W):", powerField, row++);
// addComponent(formPanel, gbc, "Stock:", stockField, row++);
// addComponent(formPanel, gbc, "Core Number:", coreNumField, row++);
// addComponent(formPanel, gbc, "Color:", colorBox, row);

// // If editing, populate fields with CPU data
// if (cpu != null) {
// populateFields();
// }

// Button panel
// JPanel buttonPanel = new JPanel();
// JButton okButton = new JButton("OK");
// JButton cancelButton = new JButton("Cancel");

// okButton.addActionListener(e -> {
// if (validateInput()) {
// confirmed = true;
// dispose();
// }
// });

// cancelButton.addActionListener(e -> dispose());

// buttonPanel.add(okButton);
// buttonPanel.add(cancelButton);

// Add panels to dialog
// add(formPanel, BorderLayout.CENTER);
// add(buttonPanel, BorderLayout.SOUTH);

// pack();
// setLocationRelativeTo(null);
// }
// protected void initializeAdditionalFields() {
// generationField = new JTextField(20);
// socketField = new JTextField(20);
// benchmarkField = new JTextField(20)
// coreNumField = new JTextField(20);
// }

// private void addComponent(JPanel panel, GridBagConstraints gbc, String label,
// JComponent component, int row) {
// gbc.gridx = 0;
// gbc.gridy = row;
// panel.add(new JLabel(label), gbc);

// gbc.gridx = 1;
// panel.add(component, gbc);
// }

// private void populateFields() {
// nameField.setText(cpu.getName());
// manufacturerField.setText(cpu.getManufacturer());
// generationField.setText(cpu.getGeneration());
// socketField.setText(cpu.getSocket());
// benchmarkField.setText(String.valueOf(cpu.getBenchmark()));
// priceField.setText(String.valueOf(cpu.getPrice()));
// powerField.setText(String.valueOf(cpu.getPower()));
// stockField.setText(String.valueOf(cpu.getStock()));
// coreNumField.setText(String.valueOf(cpu.getCoreNum()));
// colorBox.setSelectedItem(cpu.getColor());
// }

// private boolean validateInput() {
// try {
// if (nameField.getText().trim().isEmpty() ||
// manufacturerField.getText().trim().isEmpty() ||
// generationField.getText().trim().isEmpty() ||
// socketField.getText().trim().isEmpty()) {
// throw new IllegalArgumentException("All fields must be filled");
// }

// double benchmark = Double.parseDouble(benchmarkField.getText());
// double price = Double.parseDouble(priceField.getText());
// int power = Integer.parseInt(powerField.getText());
// int stock = Integer.parseInt(stockField.getText());
// int coreNum = Integer.parseInt(coreNumField.getText());

// if (benchmark < 0 || price < 0 || power < 0 || stock < 0 || coreNum < 0) {
// throw new IllegalArgumentException("Numeric values cannot be negative");
// }

// return true;
// } catch (NumberFormatException e) {
// JOptionPane.showMessageDialog(this, "Please enter valid numbers", "Input
// Error", JOptionPane.ERROR_MESSAGE);
// return false;
// } catch (IllegalArgumentException e) {
// JOptionPane.showMessageDialog(this, e.getMessage(), "Input Error",
// JOptionPane.ERROR_MESSAGE);
// return false;
// }
// }

// public boolean showDialog() {
// setVisible(true);
// return confirmed;
// }

// public CPU getBaseComponent() {
// int id = cpu != null ? cpu.getId() : 0;
// return new CPU(
// id,
// nameField.getText().trim(),
// manufacturerField.getText().trim(),
// Double.parseDouble(priceField.getText()),
// Integer.parseInt(stockField.getText()),
// generationField.getText().trim(),
// socketField.getText().trim(),
// Double.parseDouble(benchmarkField.getText()),
// Integer.parseInt(powerField.getText()),
// Integer.parseInt(coreNumField.getText()),
// (String) colorBox.getSelectedItem()
// );
// }
// }

/*
 * public class CPUDialog extends BaseComponentDialog<CPU> {
 * private final JTextField generationField;
 * private final JComboBox<String> socketField;
 * private final JTextField benchmarkField;
 * private final JTextField coreNumField;
 * private final JComboBox<String> colorField;
 * 
 * public CPUDialog(CPU cpu) {
 * super(cpu, "CPU");
 * 
 * // Initialize CPU-specific fields
 * generationField = new JTextField(20);
 * socketField = new JComboBox<>(new String[] { "AM4", "AM5", "LGA 1700" });
 * benchmarkField = new JTextField(20);
 * coreNumField = new JTextField(20);
 * colorField = new JComboBox<>(new String[] { "Black", "Silver", "White", "RGB"
 * });
 * 
 * // Set existing values if editing
 * if (cpu != null) {
 * generationField.setText(cpu.getGeneration());
 * socketField.setSelectedItem(cpu.getSocket());
 * benchmarkField.setText(String.valueOf(cpu.getBenchmark()));
 * coreNumField.setText(String.valueOf(cpu.getCoreNum()));
 * colorField.setSelectedItem(cpu.getColor());
 * }
 * }
 * 
 * @Override
 * protected void createFormPanel(JPanel panel, GridBagConstraints gbc) {
 * addField(panel, "Name:", nameField, gbc);
 * addField(panel, "Manufacturer:", manufacturerField, gbc);
 * addField(panel, "Color:", colorField, gbc);
 * addField(panel, "Generation:", generationField, gbc);
 * addField(panel, "Socket:", socketField, gbc);
 * addField(panel, "Benchmark:", benchmarkField, gbc);
 * addField(panel, "Core Number:", coreNumField, gbc);
 * addField(panel, "Price:", priceField, gbc);
 * addField(panel, "Power (W):", powerField, gbc);
 * addField(panel, "Stock:", stockField, gbc);
 * }
 * 
 * @Override
 * protected boolean validateAdditionalFields() {
 * try {
 * if (generationField.getText().trim().isEmpty()) {
 * throw new Exception("Generation is required");
 * }
 * Double.parseDouble(benchmarkField.getText());
 * Integer.parseInt(coreNumField.getText());
 * return true;
 * } catch (NumberFormatException e) {
 * JOptionPane.showMessageDialog(this,
 * "Please enter valid numbers for benchmark and core number",
 * "Validation Error",
 * JOptionPane.ERROR_MESSAGE);
 * return false;
 * } catch (Exception e) {
 * JOptionPane.showMessageDialog(this, e.getMessage(), "Validation Error",
 * JOptionPane.ERROR_MESSAGE);
 * return false;
 * }
 * }
 * 
 * @Override
 * protected CPU createSpecificComponent(int id, String name, String
 * manufacturer, double price, int stock,
 * int power, String color) {
 * return new CPU(
 * id,
 * name,
 * manufacturer,
 * price,
 * stock,
 * generationField.getText().trim(),
 * (String) socketField.getSelectedItem(),
 * Double.parseDouble(benchmarkField.getText()),
 * power,
 * Integer.parseInt(coreNumField.getText()),
 * color);
 * }
 * }
 */
