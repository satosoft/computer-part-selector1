package com.example.computerPart.ui.dialog;

import com.example.computerPart.model.Mainboard;
import javax.swing.*;
import java.awt.*;

public class MainboardDialog extends BaseComponentDialog<Mainboard> {
    private JTextField cpuSocketField;
    private JTextField ramSlotsField;
    private JTextField gpuSlotsField;
    private JTextField cpuSlotsField;
    private JCheckBox supportNVMEBox;
    private JTextField mainSizeField;
    private JTextField ramTypeField;

    public MainboardDialog(Mainboard mainboard) {
        super(mainboard);
        initializeAdditionalFields();
        setupAdditionalFields();

        if (component != null) {
            populateFields();
        }
    }

    @Override
    protected void initializeAdditionalFields() {
        cpuSocketField = new JTextField(20);
        ramSlotsField = new JTextField(20);
        gpuSlotsField = new JTextField(20);
        cpuSlotsField = new JTextField(20);
        supportNVMEBox = new JCheckBox();
        mainSizeField = new JTextField(20);
        ramTypeField = new JTextField(20);
    }

    @Override
    protected void setupAdditionalFields() {
        addComponent("CPU Socket:", cpuSocketField);
        addComponent("RAM Slots:", ramSlotsField);
        addComponent("GPU Slots:", gpuSlotsField);
        addComponent("CPU Slots:", cpuSlotsField);
        addComponent("Support NVME:", supportNVMEBox);
        addComponent("Main Size:", mainSizeField);
        addComponent("RAM Type:", ramTypeField);
    }

    @Override
    protected void populateFields() {
        populateBaseFields();
        cpuSocketField.setText(component.getCpuSocket());
        ramSlotsField.setText(String.valueOf(component.getRamSlots()));
        gpuSlotsField.setText(String.valueOf(component.getGpuSlots()));
        cpuSlotsField.setText(String.valueOf(component.getCpuSlots()));
        supportNVMEBox.setSelected(component.isSupportNVME());
        mainSizeField.setText(component.getMainSize());
        ramTypeField.setText(component.getRamType());
    }

    @Override
    protected boolean validateInput() {
        if (!validateBaseInput()) {
            return false;
        }

        try {
            if (cpuSocketField.getText().trim().isEmpty() ||
                mainSizeField.getText().trim().isEmpty() ||
                ramTypeField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("All fields must be filled");
            }

            int ramSlots = Integer.parseInt(ramSlotsField.getText());
            int gpuSlots = Integer.parseInt(gpuSlotsField.getText());
            int cpuSlots = Integer.parseInt(cpuSlotsField.getText());

            if (ramSlots < 0 || gpuSlots < 0 || cpuSlots < 0) {
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
    public Mainboard getComponentPart() {
        int id = component != null ? component.getId() : 0;
        return new Mainboard(
                id,
                nameField.getText().trim(),
                manufacturerField.getText().trim(),
                Integer.parseInt(stockField.getText()),
                Integer.parseInt(powerField.getText()),
                (String) colorBox.getSelectedItem(),
                Double.parseDouble(priceField.getText()),
                cpuSocketField.getText().trim(),
                Integer.parseInt(ramSlotsField.getText()),
                Integer.parseInt(gpuSlotsField.getText()),
                Integer.parseInt(cpuSlotsField.getText()),
                supportNVMEBox.isSelected(),
                mainSizeField.getText().trim(),
                ramTypeField.getText().trim());
    }
}

// package com.example.computerPart.ui.dialog;

// import com.example.computerPart.model.Mainboard;
// import javax.swing.*;
// import java.awt.*;//

// public class MainboardDialog extends BaseComponentDialog<Mainboard> {
//     private final JComboBox<String> socketField;
//     private final JComboBox<String> ramTypeField;
//     private final JCheckBox nvmeCheckBox;
//     private final JComboBox<String> mainSizeField;
//     private final JSpinner ramSlotsField;
//     private final JSpinner gpuSlotsField;
//     private final JSpinner cpuSlotsField;
//     private final JComboBox<String> colorField;

//     public MainboardDialog(Mainboard mainboard) {
//         super(mainboard, "Mainboard");

//         // Initialize fields
//         socketField = new JComboBox<>(new String[] { "LGA 1700", "AM5", "AM4" });
//         ramTypeField = new JComboBox<>(new String[] { "DDR4", "DDR5" });
//         nvmeCheckBox = new JCheckBox("Support NVME");
//         mainSizeField = new JComboBox<>(new String[] { "ATX", "mATX" });
//         colorField = new JComboBox<>(new String[] { "Black", "Silver", "White", "RGB" });

//         // Create spinners for slots with reasonable ranges
//         ramSlotsField = new JSpinner(new SpinnerNumberModel(2, 1, 8, 1));
//         gpuSlotsField = new JSpinner(new SpinnerNumberModel(1, 1, 4, 1));
//         cpuSlotsField = new JSpinner(new SpinnerNumberModel(1, 1, 2, 1));

//         // Set values if editing
//         if (mainboard != null) {
//             socketField.setSelectedItem(mainboard.getCpuSocket());
//             ramTypeField.setSelectedItem(mainboard.getRamType());
//             nvmeCheckBox.setSelected(mainboard.isSupportNVME());
//             mainSizeField.setSelectedItem(mainboard.getMainSize());
//             ramSlotsField.setValue(mainboard.getRamSlots());
//             gpuSlotsField.setValue(mainboard.getGpuSlots());
//             cpuSlotsField.setValue(mainboard.getCpuSlots());
//             colorField.setSelectedItem(mainboard.getColor());
//         }
//     }

//     @Override
//     protected void initializePowerField(Mainboard component) {
//         powerField.setText(String.valueOf(component.getPower()));
//     }

//     @Override
//     protected void createFormPanel(JPanel panel, GridBagConstraints gbc) {
//         addField(panel, "Name:", nameField, gbc);
//         addField(panel, "Manufacturer:", manufacturerField, gbc);
//         addField(panel, "Color:", colorField, gbc);
//         addField(panel, "CPU Socket:", socketField, gbc);
//         addField(panel, "RAM Type:", ramTypeField, gbc);
//         addField(panel, "Price:", priceField, gbc);
//         addField(panel, "Power (W):", powerField, gbc);
//         addField(panel, "Stock:", stockField, gbc);
//         addField(panel, "", nvmeCheckBox, gbc);
//         addField(panel, "Size:", mainSizeField, gbc);
//         addField(panel, "RAM Slots:", ramSlotsField, gbc);
//         addField(panel, "GPU Slots:", gpuSlotsField, gbc);
//         addField(panel, "CPU Slots:", cpuSlotsField, gbc);
//     }

//     @Override
//     protected boolean validateAdditionalFields() {
//         return true; // Basic validation is handled by the parent class
//     }

//     @Override
//     protected Mainboard createSpecificComponent(int id, String name, String manufacturer, double price, int stock,
//             int power, String color) {
//         return new Mainboard(
//                 id,
//                 name,
//                 manufacturer,
//                 price,
//                 stock,
//                 power,
//                 (String) socketField.getSelectedItem(),
//                 (Integer) ramSlotsField.getValue(),
//                 (Integer) gpuSlotsField.getValue(),
//                 (Integer) cpuSlotsField.getValue(),
//                 nvmeCheckBox.isSelected(),
//                 (String) mainSizeField.getSelectedItem(),
//                 (String) ramTypeField.getSelectedItem(),
//                 color);
//     }
// }