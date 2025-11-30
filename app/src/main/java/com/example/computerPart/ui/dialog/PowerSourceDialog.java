package com.example.computerPart.ui.dialog;

import com.example.computerPart.model.PowerSource;
import javax.swing.*;
import java.awt.*;

public class PowerSourceDialog extends BaseComponentDialog<PowerSource> {

    public PowerSourceDialog(PowerSource powerSource) {
        super(powerSource);
        if (component != null) {
            populateFields();
        }
    }

    @Override
    protected void initializeAdditionalFields() {
        // PowerSource has no additional fields beyond the base fields
    }

    @Override
    protected void setupAdditionalFields() {
        // PowerSource has no additional fields to setup
    }

    @Override
    protected void populateFields() {
        populateBaseFields();
    }

    @Override
    protected boolean validateInput() {
        return validateBaseInput();
    }

    @Override
    public PowerSource getComponentPart() {
        int id = component != null ? component.getId() : 0;
        return new PowerSource(
                id,
                nameField.getText().trim(),
                manufacturerField.getText().trim(),
                Integer.parseInt(stockField.getText()),
                Integer.parseInt(powerField.getText()),
                (String) colorBox.getSelectedItem(),
                Double.parseDouble(priceField.getText()));
    }
}


// package com.example.computerPart.ui.dialog;

// import com.example.computerPart.model.PowerSource;
// import javax.swing.*;
// import java.awt.*;

// public class PowerSourceDialog extends BaseComponentDialog<PowerSource> {

//     public PowerSourceDialog(PowerSource powerSource) {
//         super(powerSource, "Power Supply");
//     }

//     @Override
//     protected void initializePowerField(PowerSource component) {
//         powerField.setText(String.valueOf(component.getPower()));
//     }

//     @Override
//     protected void createFormPanel(JPanel panel, GridBagConstraints gbc) {
//         addField(panel, "Name:", nameField, gbc);
//         addField(panel, "Manufacturer:", manufacturerField, gbc);
//         addField(panel, "Color:", colorField, gbc);
//         addField(panel, "Supply Power (W):", powerField, gbc);
//         addField(panel, "Price:", priceField, gbc);
//         addField(panel, "Stock:", stockField, gbc);
//     }

//     @Override
//     protected boolean validateAdditionalFields() {
//         return true; // No additional fields to validate
//     }

//     @Override
//     protected PowerSource createSpecificComponent(int id, String name, String manufacturer, double price, int stock,
//             int power, String color) {
//         return new PowerSource(id, name, manufacturer, price, stock, power, color);
//     }
// }