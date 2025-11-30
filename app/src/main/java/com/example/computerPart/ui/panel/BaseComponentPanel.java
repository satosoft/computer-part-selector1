package com.example.computerPart.ui.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
//import java.sql.SQLException;//
import java.util.Comparator;
import java.text.NumberFormat;
import java.util.Locale;

public abstract class BaseComponentPanel extends JPanel {
    protected JTable table;
    protected DefaultTableModel tableModel;
    protected JButton addButton;
    protected JButton editButton;
    protected JButton deleteButton;
    protected JButton refreshButton;
    protected JTextField searchField;
    protected TableRowSorter<DefaultTableModel> sorter;
    protected final NumberFormat priceFormatter;

    public BaseComponentPanel() {
        setLayout(new BorderLayout());
        priceFormatter = NumberFormat.getNumberInstance(Locale.US);
        priceFormatter.setGroupingUsed(true);
        priceFormatter.setMaximumFractionDigits(0);
        initializeComponents();
        setupSorting();
        setupLayout();
        setupListeners();
    }

    private void initializeComponents() {
        // Create table with custom model that handles price formatting
        tableModel = new DefaultTableModel(getColumnNames(), 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public void setValueAt(Object value, int row, int column) {
                // Format price values before displaying
                if (value instanceof Number && getColumnName(column).equals("Price")) {
                    super.setValueAt(priceFormatter.format(value), row, column);
                } else {
                    super.setValueAt(value, row, column);
                }
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add sorter
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        // Create search field
        searchField = new JTextField(20);
        searchField.setToolTipText("Search in all columns");

        // Create buttons
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        refreshButton = new JButton("Refresh");
    }

    private void setupLayout() {
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        add(searchPanel, BorderLayout.NORTH);

        // Table panel
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupListeners() {
        addButton.addActionListener(e -> handleAdd());
        editButton.addActionListener(e -> handleEdit());
        deleteButton.addActionListener(e -> handleDelete());
        refreshButton.addActionListener(e -> refreshTable());

        // Add search functionality
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                search();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                search();
            }

            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                search();
            }
        });
    }

    protected void search() {
        String text = searchField.getText().trim().toLowerCase();
        if (text.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            // Search in all columns
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    protected abstract String[] getColumnNames();

    protected abstract void refreshTable();

    protected abstract void handleAdd();

    protected abstract void handleEdit();

    protected abstract void handleDelete();

    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    protected void setupSorting() {
        // Default implementation - override in subclasses if needed
        sorter.setComparator(0, (Comparator<Integer>) Integer::compare); // ID column
    }

}