package com.example.computerPart.ui.dialog;

import com.example.computerPart.model.BaseComputerPart;
import javax.swing.*;
import java.awt.*;

public abstract class BaseComponentDialog<T extends BaseComputerPart> extends JDialog {
    protected boolean confirmed = false;
    // protected JTextField idField;
    protected JTextField nameField;
    protected JTextField manufacturerField;
    protected JTextField priceField;
    protected JTextField powerField;
    protected JTextField stockField;
    protected JComboBox<String> colorBox;
    protected JPanel mainPanel;
    protected GridBagConstraints gbc;
    protected int currentRow = 0;
    protected T component;

    public BaseComponentDialog(T com) {
        // setTitle(title);
        this.component = com;
        setTitle(component == null ? "Add New component" : "Edit component");
        setModal(true);
        setLayout(new BorderLayout());

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setPreferredSize(new Dimension(400, 500));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        initializeBaseComponents();
        setupBaseComponents();
        setupButtons();
        pack();
        setLocationRelativeTo(null);
    }

    private void initializeBaseComponents() {
        // idField = new JTextField(20);
        nameField = new JTextField(20);
        manufacturerField = new JTextField(20);
        priceField = new JTextField(20);
        powerField = new JTextField(20);
        stockField = new JTextField(20);
        colorBox = new JComboBox<>(new String[] { "Black", "White", "Silver" });
    }

    private void setupBaseComponents() {
        // addComponent("Id:", idField);
        addComponent("Name:", nameField);
        addComponent("Manufacturer:", manufacturerField);
        addComponent("Price:", priceField);
        addComponent("Power (W):", powerField);
        addComponent("Stock:", stockField);
        addComponent("Color:", colorBox);
    }

    protected void addComponent(String label, JComponent component) {
        gbc.gridx = 0;
        gbc.gridy = currentRow++;
        mainPanel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        mainPanel.add(component, gbc);
        currentRow++;
    }

    private void setupButtons() {
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        okButton.addActionListener(e -> {
            if (validateInput()) {
                confirmed = true;
                dispose();
            }
        });

        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    protected void populateBaseFields() {
        // idField.setText(String.valueOf(component.getId()));
        nameField.setText(component.getName());
        manufacturerField.setText(component.getManufacturer());
        priceField.setText(String.valueOf(component.getPrice()));
        powerField.setText(String.valueOf(component.getPower()));
        stockField.setText(String.valueOf(component.getStock()));
        colorBox.setSelectedItem(component.getColor());
    }
    

    protected boolean validateBaseInput() {
        try {
            if (nameField.getText().trim().isEmpty() ||
                    manufacturerField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Name and manufacturer are required");
            }

            double price = Double.parseDouble(priceField.getText());
            int power = Integer.parseInt(powerField.getText());
            int stock = Integer.parseInt(stockField.getText());

            if (price < 0 || power < 0 || stock < 0) {
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
    public boolean showDialog() {
        setVisible(true);
        return confirmed;
    }

    protected abstract boolean validateInput();
    protected abstract void initializeAdditionalFields();
    protected abstract void setupAdditionalFields();
    protected abstract void populateFields();
    public abstract T getComponentPart();
   
}

// public abstract class BaseComponentDialog<T extends BaseComputerPart> extends
// JDialog {
// protected boolean approved = false;

// protected JTextField idField;
// protected JTextField nameField;
// protected JTextField manufacturerField;
// protected JTextField priceField;
// protected JTextField stockField;
// protected JTextField powerField;
// protected JComboBox<String> colorField;

// protected int id;
// protected String name;
// protected String manufacturer;
// protected double price;
// protected int stock;
// protected double power;
// protected String color;
// protected T component;
// protected JPanel formPanel;
// protected GridBagConstraints gbc;

// public BaseComponentDialog(T com) {
// this.component = com;
// setTitle(component == null ? "Add New" : "Edit "); // componentType : "Edit "
// + componentType);
// setModal(true);
// setLayout(new BorderLayout());

// // Initialize fields
// // idField=new JTextField(20);
// // nameField = new JTextField(20);
// // manufacturerField = new JTextField(20);
// // priceField = new JTextField(20);
// // stockField = new JTextField(20);
// // powerField = new JTextField(20);
// // colorField = new JComboBox<>(new String[] { "Black", "Silver", "White",
// "RGB"
// // });
// initializeBaseFields();
// //initializeAdditionalFields();

// showBaseParameters();
// // Create form panel
// formPanel = new JPanel(new GridBagLayout());
// gbc = new GridBagConstraints();
// gbc.gridx = 0;
// gbc.gridy = 0;
// gbc.anchor = GridBagConstraints.WEST;
// gbc.insets = new Insets(5, 5, 5, 5);

// createFormPanel(formPanel, gbc);
// add(formPanel, BorderLayout.CENTER);

// // Buttons
// JPanel buttonPanel = new JPanel();
// JButton okButton = new JButton("OK");
// JButton cancelButton = new JButton("Cancel");

// okButton.addActionListener(e -> {
// if (validateInput()) {
// approved = true;
// dispose();
// }
// });

// cancelButton.addActionListener(e -> dispose());
// buttonPanel.add(okButton);
// buttonPanel.add(cancelButton);
// add(buttonPanel, BorderLayout.SOUTH);

// pack();
// setLocationRelativeTo(null);
// }

// protected void initializeBaseFields() {
// idField = new JTextField(20);
// nameField = new JTextField(20);
// manufacturerField = new JTextField(20);
// priceField = new JTextField(20);
// stockField = new JTextField(20);
// powerField = new JTextField(20);
// colorField = new JComboBox<>(new String[] { "Black", "Silver", "White", "RGB"
// });
// }
// protected abstract void createAdditionalFields() ;

// protected void showBaseParameters() {
// if (component != null) {
// idField.setText(String.valueOf(component.getId()));
// nameField.setText(component.getName());
// manufacturerField.setText(component.getManufacturer());
// priceField.setText(String.valueOf(component.getPrice()));
// stockField.setText(String.valueOf(component.getStock()));
// colorField.setSelectedItem(component.getColor());
// powerField.setText(String.valueOf(component.getPower()));
// }
// }

// // protected abstract void initializePowerField(T component);

// protected void addField(JPanel panel, String label, JComponent field,
// GridBagConstraints gbc) {
// gbc.gridx = 0;
// JLabel labelComponent = new JLabel(label);
// panel.add(labelComponent, gbc);
// gbc.gridx = 1;
// panel.add(field, gbc);
// gbc.gridy++;
// }

// protected abstract void createFormPanel(JPanel panel, GridBagConstraints
// gbc);

// protected boolean validateInput() {
// if (nameField.getText().trim().isEmpty()) {
// JOptionPane.showMessageDialog(this, "Name cannot be empty", "Validation
// Error", JOptionPane.ERROR_MESSAGE);
// return false;
// }
// if (manufacturerField.getText().trim().isEmpty()) {
// JOptionPane.showMessageDialog(this, "Manufacturer cannot be empty",
// "Validation Error",
// JOptionPane.ERROR_MESSAGE);
// return false;
// }
// try {
// Double.parseDouble(priceField.getText().trim());
// } catch (NumberFormatException e) {
// JOptionPane.showMessageDialog(this, "Please enter a valid price", "Validation
// Error",
// JOptionPane.ERROR_MESSAGE);
// return false;
// }
// try {
// int stock = Integer.parseInt(stockField.getText().trim());
// if (stock < 0) {
// throw new NumberFormatException();
// }
// } catch (NumberFormatException e) {
// JOptionPane.showMessageDialog(this, "Please enter a valid stock number
// (non-negative integer)",
// "Validation Error", JOptionPane.ERROR_MESSAGE);
// return false;
// }
// try {
// Integer.parseInt(powerField.getText().trim());
// } catch (NumberFormatException e) {
// JOptionPane.showMessageDialog(this, "Please enter a valid power usage value",
// "Validation Error",
// JOptionPane.ERROR_MESSAGE);
// return false;
// }

// return validateAdditionalFields();
// }

// protected abstract boolean validateAdditionalFields();

// public boolean showDialog() {
// setVisible(true);
// return approved;
// }

// protected void setBaseparameters() {
// id = existingComponent != null ? existingComponent.getId() : 0;
// name = nameField.getText().trim();
// manufacturer = manufacturerField.getText().trim();
// price = Double.parseDouble(priceField.getText().trim());
// stock = Integer.parseInt(stockField.getText().trim());
// power = Integer.parseInt(powerField.getText().trim());
// color = (String) colorField.getSelectedItem();
// // return createSpecificComponent(id, name, manufacturer, price, stock,
// power,
// // color);
// }

// protected abstract void setSpecificParameters();

// protected abstract T createSpecificComponent();

// }

/*
 * protected abstract T createSpecificComponent(int id, String name, String
 * manufacturer, double price, int stock,
 * int power, String color);
 * 
 * public T getBaseComponent() {
 * return createBaseComponent(existingComponent);
 * }
 * 
 */