package com.example.computerPart.ui.dialog;

import com.example.computerPart.model.RAM;
import javax.swing.*;
import java.awt.*;

public class RAMDialog extends BaseComponentDialog<RAM> {
    private JTextField capacityField;
    private JTextField benchmarkField;
    private JTextField ramTypeField;
    private JTextField busField;

    public RAMDialog(RAM ram) {
        super(ram);
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
        ramTypeField = new JTextField(20);
        busField = new JTextField(20);
    }

    @Override
    protected void setupAdditionalFields() {
        addComponent("Capacity (GB):", capacityField);
        addComponent("Benchmark:", benchmarkField);
        addComponent("RAM Type:", ramTypeField);
        addComponent("Bus Speed:", busField);
    }

    @Override
    protected void populateFields() {
        populateBaseFields();
        capacityField.setText(String.valueOf(component.getCapacity()));
        benchmarkField.setText(String.valueOf(component.getBenchmark()));
        ramTypeField.setText(component.getRamType());
        busField.setText(String.valueOf(component.getBus()));
    }

    @Override
    protected boolean validateInput() {
        if (!validateBaseInput()) {
            return false;
        }

        try {
            if (ramTypeField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("All fields must be filled");
            }

            int capacity = Integer.parseInt(capacityField.getText());
            double benchmark = Double.parseDouble(benchmarkField.getText());
            int bus = Integer.parseInt(busField.getText());

            if (capacity < 0 || benchmark < 0 || bus < 0) {
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
    public RAM getComponentPart() {
        int id = component != null ? component.getId() : 0;
        return new RAM(
                id,
                nameField.getText().trim(),
                manufacturerField.getText().trim(),
                Integer.parseInt(stockField.getText()),
                Integer.parseInt(powerField.getText()),
                (String) colorBox.getSelectedItem(),
                Double.parseDouble(priceField.getText()),
                Integer.parseInt(capacityField.getText()),
                Double.parseDouble(benchmarkField.getText()),
                ramTypeField.getText().trim(),
                Integer.parseInt(busField.getText()));
    }
}


// package com.example.computerPart.ui.dialog;

// import com.example.computerPart.model.RAM;
// import javax.swing.*;
// import java.awt.*;

// public class RAMDialog extends BaseComponentDialog<RAM> {
//     private final JTextField capacityField;
//     private final JTextField benchmarkField;
//     private final JComboBox<String> typeField;
//     private final JTextField busField;

//     public RAMDialog(RAM ram) {
//         super(ram, "RAM");

//         // Initialize RAM-specific fields
//         capacityField = new JTextField(20);
//         benchmarkField = new JTextField(20);
//         typeField = new JComboBox<>(new String[] { "DDR3", "DDR4", "DDR5" });
//         busField = new JTextField(20);

//         // Set existing values if editing
//         if (ram != null) {
//             capacityField.setText(String.valueOf(ram.getCapacity()));
//             benchmarkField.setText(String.valueOf(ram.getBenchmark()));
//             typeField.setSelectedItem(ram.getRamType());
//             busField.setText(String.valueOf(ram.getBus()));
//         }
//     }

//     @Override
//     protected void initializePowerField(RAM component) {
//         powerField.setText(String.valueOf(component.getPower()));
//     }

//     @Override
//     protected void createFormPanel(JPanel panel, GridBagConstraints gbc) {
//         addField(panel, "Name:", nameField, gbc);
//         addField(panel, "Manufacturer:", manufacturerField, gbc);
//         addField(panel, "Color:", colorField, gbc);
//         addField(panel, "Type:", typeField, gbc);
//         addField(panel, "Bus Speed:", busField, gbc);
//         addField(panel, "Capacity (GB):", capacityField, gbc);
//         addField(panel, "Benchmark:", benchmarkField, gbc);
//         addField(panel, "Price:", priceField, gbc);
//         addField(panel, "Power (W):", powerField, gbc);
//         addField(panel, "Stock:", stockField, gbc);
//     }

//     @Override
//     protected boolean validateAdditionalFields() {
//         try {
//             Integer.parseInt(capacityField.getText().trim());
//             Double.parseDouble(benchmarkField.getText().trim());
//             Integer.parseInt(busField.getText().trim());
//             return true;
//         } catch (NumberFormatException e) {
//             JOptionPane.showMessageDialog(this, "Please enter valid numbers for capacity, benchmark, and bus speed",
//                     "Validation Error", JOptionPane.ERROR_MESSAGE);
//             return false;
//         }
//     }

//     @Override
//     protected RAM createSpecificComponent(int id, String name, String manufacturer, double price, int stock,
//             int power, String color) {
//         return new RAM(
//                 id,
//                 name,
//                 manufacturer,
//                 price,
//                 stock,
//                 Integer.parseInt(capacityField.getText().trim()),
//                 Double.parseDouble(benchmarkField.getText().trim()),
//                 power,
//                 (String) typeField.getSelectedItem(),
//                 Integer.parseInt(busField.getText().trim()),
//                 color);
//     }
// }