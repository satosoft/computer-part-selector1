package com.example.computerPart.ui.dialog;

import com.example.computerPart.model.Cooler;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class CoolerDialog extends BaseComponentDialog<Cooler> {
    private JTextField coolerTypeField;
    private JTextField socketSupportField;
    private JTextField ledColorField;

    public CoolerDialog(Cooler cooler) {
        super(cooler);
        initializeAdditionalFields();
        setupAdditionalFields();

        if (component != null) {
            populateFields();
        }
    }

    @Override
    protected void initializeAdditionalFields() {
        coolerTypeField = new JTextField(20);
        // coolerTypeField = new JComboBox<>(new String[] { "Air", "AIO" });
        socketSupportField = new JTextField(20);
        ledColorField = new JTextField(20);
    }

    @Override
    protected void setupAdditionalFields() {
        addComponent("Cooler Type:", coolerTypeField);
        addComponent("Socket Support:", socketSupportField);
        addComponent("LED Color:", ledColorField);
    }

    @Override
    protected void populateFields() {
        populateBaseFields();
        coolerTypeField.setText(component.getCoolerType());
        socketSupportField.setText(String.join(", ", component.getSocketSupport()));
        ledColorField.setText(component.getLedColor());
    }

    @Override
    protected boolean validateInput() {
        if (!validateBaseInput()) {
            return false;
        }

        try {
            if (coolerTypeField.getText().trim().isEmpty() ||
                    socketSupportField.getText().trim().isEmpty() ||
                    ledColorField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("All fields must be filled");
            }
            return true;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    @Override
    public Cooler getComponentPart() {
        int id = component != null ? component.getId() : 0;
        return new Cooler(
                id,
                nameField.getText().trim(),
                manufacturerField.getText().trim(),
                Integer.parseInt(stockField.getText()),
                Integer.parseInt(powerField.getText()),
                (String) colorBox.getSelectedItem(),
                Double.parseDouble(priceField.getText()),

                coolerTypeField.getText().trim(),
                Arrays.asList(socketSupportField.getText().trim().split("\\s*,\\s*")),
                ledColorField.getText().trim());
    }
}

// package com.example.computerPart.ui.dialog;

// import com.example.computerPart.model.Cooler;
// import javax.swing.*;
// import java.awt.*;
// import java.util.Arrays;
// import java.util.List;
// //import java.util.stream.Collectors;

// public class CoolerDialog extends BaseComponentDialog<Cooler> {
// private final JComboBox<String> coolerTypeField;
// private final JList<String> socketSupportList;
// private final JComboBox<String> ledColorField;

// public CoolerDialog(Cooler cooler) {
// super(cooler, "Cooler");

// // Initialize fields
// coolerTypeField = new JComboBox<>(new String[] { "Air Cooling", "Water
// Cooling", "Passive" });

// // Socket support list with common CPU sockets
// String[] sockets = { "AM4", "AM5", "LGA 1700", "LGA 1200", "TR4" };
// socketSupportList = new JList<>(sockets);
// socketSupportList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

// ledColorField = new JComboBox<>(new String[] { "None", "RGB", "Red", "Blue",
// "Green", "White" });

// // Set existing values if editing
// if (cooler != null) {
// coolerTypeField.setSelectedItem(cooler.getCoolerType());
// List<String> selectedSockets = cooler.getSocketSupport();
// int[] selectedIndices = new int[selectedSockets.size()];
// for (int i = 0; i < selectedSockets.size(); i++) {
// for (int j = 0; j < sockets.length; j++) {
// if (sockets[j].equals(selectedSockets.get(i))) {
// selectedIndices[i] = j;
// break;
// }
// }
// }
// socketSupportList.setSelectedIndices(selectedIndices);
// ledColorField.setSelectedItem(cooler.getLedColor());
// }
// }

// @Override
// protected void createFormPanel(JPanel panel, GridBagConstraints gbc) {
// addField(panel, "Name:", nameField, gbc);
// addField(panel, "Manufacturer:", manufacturerField, gbc);
// addField(panel, "Color:", colorField, gbc);
// addField(panel, "Cooler Type:", coolerTypeField, gbc);

// // Add socket support list with scroll pane
// gbc.gridx = 0;
// gbc.gridy++;
// panel.add(new JLabel("Socket Support:"), gbc);
// gbc.gridx = 1;
// JScrollPane scrollPane = new JScrollPane(socketSupportList);
// scrollPane.setPreferredSize(new Dimension(200, 80));
// panel.add(scrollPane, gbc);

// gbc.gridx = 0;
// gbc.gridy++;
// addField(panel, "LED Color:", ledColorField, gbc);
// addField(panel, "Price:", priceField, gbc);
// addField(panel, "Power (W):", powerField, gbc);
// addField(panel, "Stock:", stockField, gbc);
// }

// @Override
// protected boolean validateAdditionalFields() {
// if (socketSupportList.getSelectedIndices().length == 0) {
// JOptionPane.showMessageDialog(this, "Please select at least one socket type",
// "Validation Error",
// JOptionPane.ERROR_MESSAGE);
// return false;
// }
// return true;
// }

// @Override
// protected Cooler createSpecificComponent(int id, String name, String
// manufacturer, double price, int stock,
// int power, String color) {
// List<String> selectedSockets = Arrays.asList(
// socketSupportList.getSelectedValuesList().toArray(new String[0]));

// return new Cooler(
// id,
// name,
// manufacturer,
// price,
// stock,
// power,
// color,
// (String) coolerTypeField.getSelectedItem(),
// selectedSockets,
// (String) ledColorField.getSelectedItem());
// }
// }