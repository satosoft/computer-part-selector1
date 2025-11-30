package com.example.computerPart.ui.dialog;

import com.example.computerPart.model.ComputerCase;
import javax.swing.*;
import java.awt.*;

public class ComputerCaseDialog extends BaseComponentDialog<ComputerCase> {
    private JTextField caseSizeField;
    private JTextField caseMaterialField;
    private JTextField caseTypeField;

    public ComputerCaseDialog(ComputerCase computerCase) {
        super(computerCase);
        initializeAdditionalFields();
        setupAdditionalFields();

        if (component != null) {
            populateFields();
        }
    }

    @Override
    protected void initializeAdditionalFields() {
        caseSizeField = new JTextField(20);
        caseMaterialField = new JTextField(20);
        caseTypeField = new JTextField(20);
    }

    @Override
    protected void setupAdditionalFields() {
        addComponent("Case Size:", caseSizeField);
        addComponent("Case Material:", caseMaterialField);
        addComponent("Case Type:", caseTypeField);
    }

    @Override
    protected void populateFields() {
        populateBaseFields();
        caseSizeField.setText(component.getCaseSize());
        caseMaterialField.setText(component.getCaseMaterial());
        caseTypeField.setText(component.getCaseType());
    }

    @Override
    protected boolean validateInput() {
        if (!validateBaseInput()) {
            return false;
        }

        try {
            if (caseSizeField.getText().trim().isEmpty() ||
                    caseMaterialField.getText().trim().isEmpty() ||
                    caseTypeField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("All fields must be filled");
            }
            return true;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    @Override
    public ComputerCase getComponentPart() {
        int id = component != null ? component.getId() : 0;
        return new ComputerCase(
                id,
                nameField.getText().trim(),
                manufacturerField.getText().trim(),
                Integer.parseInt(stockField.getText()),
                Integer.parseInt(powerField.getText()),
                (String) colorBox.getSelectedItem(),
                Double.parseDouble(priceField.getText()),

                caseSizeField.getText().trim(),
                caseMaterialField.getText().trim(),
                caseTypeField.getText().trim()

        );
    }
}

// package com.example.computerPart.ui.dialog;

// import com.example.computerPart.model.ComputerCase;
// import javax.swing.*;
// import java.awt.*;

// public class ComputerCaseDialog extends BaseComponentDialog<ComputerCase> {
// private final JComboBox<String> caseSizeField;
// private final JComboBox<String> caseMaterialField;
// private final JComboBox<String> caseTypeField;

// public ComputerCaseDialog(ComputerCase computerCase) {
// super(computerCase, "Computer Case");

// // Initialize fields
// caseSizeField = new JComboBox<>(new String[] { "ATX", "mATX", "ITX" });
// caseMaterialField = new JComboBox<>(new String[] { "Steel", "Aluminum",
// "Plastic", "Glass", "Mixed" });
// caseTypeField = new JComboBox<>(new String[] { "Tower", "Desktop", "Mini",
// "Slim" });

// // Set existing values if editing
// if (computerCase != null) {
// caseSizeField.setSelectedItem(computerCase.getCaseSize());
// caseMaterialField.setSelectedItem(computerCase.getCaseMaterial());
// caseTypeField.setSelectedItem(computerCase.getCaseType());
// }
// }

// @Override
// protected void createFormPanel(JPanel panel, GridBagConstraints gbc) {
// addField(panel, "Name:", nameField, gbc);
// addField(panel, "Manufacturer:", manufacturerField, gbc);
// addField(panel, "Color:", colorField, gbc);
// addField(panel, "Case Size:", caseSizeField, gbc);
// addField(panel, "Case Material:", caseMaterialField, gbc);
// addField(panel, "Case Type:", caseTypeField, gbc);
// addField(panel, "Price:", priceField, gbc);
// addField(panel, "Power (W):", powerField, gbc);
// addField(panel, "Stock:", stockField, gbc);
// }

// @Override
// protected boolean validateAdditionalFields() {
// return true;
// }

// @Override
// protected ComputerCase createSpecificComponent(int id, String name, String
// manufacturer, double price, int stock,
// int power, String color) {
// return new ComputerCase(
// id,
// name,
// manufacturer,
// price,
// stock,
// (String) caseSizeField.getSelectedItem(),
// (String) caseMaterialField.getSelectedItem(),
// (String) caseTypeField.getSelectedItem(),
// power,
// color);
// }
// }