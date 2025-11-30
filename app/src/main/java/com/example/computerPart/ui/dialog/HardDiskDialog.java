package com.example.computerPart.ui.dialog;

import com.example.computerPart.model.HardDisk;
import javax.swing.*;
import java.awt.*;

public class HardDiskDialog extends BaseComponentDialog<HardDisk> {
    private JTextField capacityField;
    private JTextField speedField;
    private JTextField diskTypeField;

    public HardDiskDialog(HardDisk hardDisk) {
        super(hardDisk);
        initializeAdditionalFields();
        setupAdditionalFields();

        if (component != null) {
            populateFields();
        }
    }

    @Override
    protected void initializeAdditionalFields() {
        diskTypeField = new JTextField(20);
        speedField = new JTextField(20);
        capacityField = new JTextField(20);
    }

    @Override
    protected void setupAdditionalFields() {
        addComponent("Disk Type:", diskTypeField);
        addComponent("Speed (MB/s):", speedField);
        addComponent("Capacity (GB):", capacityField);
    }

    @Override
    protected void populateFields() {
        populateBaseFields();
        diskTypeField.setText(component.getType());
        speedField.setText(String.valueOf(component.getSpeed()));
        capacityField.setText(String.valueOf(component.getCapacity()));
    }

    @Override
    protected boolean validateInput() {
        if (!validateBaseInput()) {
            return false;
        }

        try {
            if (diskTypeField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("All fields must be filled");
            }

            int capacity = Integer.parseInt(capacityField.getText());
            double speed = Double.parseDouble(speedField.getText());

            if (capacity < 0 || speed < 0) {
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
    public HardDisk getComponentPart() {
        int id = component != null ? component.getId() : 0;
        return new HardDisk(
                id,
                nameField.getText().trim(),
                manufacturerField.getText().trim(),
                Integer.parseInt(stockField.getText()),
                Integer.parseInt(powerField.getText()),
                (String) colorBox.getSelectedItem(),
                Double.parseDouble(priceField.getText()),
                
                diskTypeField.getText().trim(),
                Integer.parseInt(speedField.getText()),
                Integer.parseInt(capacityField.getText())
            );
    }
}

// package com.example.computerPart.ui.dialog;

// import com.example.computerPart.model.HardDisk;
// import javax.swing.*;
// import java.awt.*;

// public class HardDiskDialog extends BaseComponentDialog<HardDisk> {
// private final JComboBox<String> typeComboBox;
// private final JTextField speedField;
// private final JComboBox<String> colorField;
// private final JTextField capacityField;

// public HardDiskDialog(HardDisk hardDisk) {
// super(hardDisk, "Hard Disk");
// typeComboBox = new JComboBox<>(new String[] { "HDD", "SSD", "NVME" });
// speedField = new JTextField(20);
// colorField = new JComboBox<>(new String[] { "Black", "Silver", "White", "RGB"
// });
// capacityField = new JTextField(20);

// if (hardDisk != null) {
// typeComboBox.setSelectedItem(hardDisk.getType());
// speedField.setText(String.valueOf(hardDisk.getSpeed()));
// colorField.setSelectedItem(hardDisk.getColor());
// capacityField.setText(String.valueOf(hardDisk.getCapacity()));
// }
// }

// @Override
// protected void initializePowerField(HardDisk component) {
// powerField.setText(String.valueOf(component.getPower()));
// }

// @Override
// protected void createFormPanel(JPanel panel, GridBagConstraints gbc) {
// addField(panel, "Name:", nameField, gbc);
// addField(panel, "Manufacturer:", manufacturerField, gbc);
// addField(panel, "Color:", colorField, gbc);
// addField(panel, "Type:", typeComboBox, gbc);
// addField(panel, "Speed (MB/s):", speedField, gbc);
// addField(panel, "Capacity (GB):", capacityField, gbc);
// addField(panel, "Price:", priceField, gbc);
// addField(panel, "Power (W):", powerField, gbc);
// addField(panel, "Stock:", stockField, gbc);
// }

// @Override
// protected boolean validateAdditionalFields() {
// try {
// Integer.parseInt(speedField.getText().trim());
// Integer.parseInt(capacityField.getText().trim());
// return true;
// } catch (NumberFormatException e) {
// JOptionPane.showMessageDialog(this, "Please enter valid speed and capacity
// values",
// "Validation Error", JOptionPane.ERROR_MESSAGE);
// return false;
// }
// }

// @Override
// protected HardDisk createSpecificComponent(int id, String name, String
// manufacturer, double price, int stock,
// int power, String color) {
// return new HardDisk(
// id,
// name,
// manufacturer,
// price,
// stock,
// (String) typeComboBox.getSelectedItem(),
// power,
// Integer.parseInt(speedField.getText().trim()),
// color,
// Integer.parseInt(capacityField.getText().trim()));
// }
// }