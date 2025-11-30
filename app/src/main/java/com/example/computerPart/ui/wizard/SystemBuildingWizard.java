package com.example.computerPart.ui.wizard;

import javax.swing.*;

import org.checkerframework.checker.units.qual.s;

import java.awt.*;
import java.awt.event.*;
import com.example.computerPart.model.Mainboard;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap; // HashMap implements Map

public class SystemBuildingWizard extends JDialog {
    protected WizardComponentManager componentManager;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final JButton backButton;
    private final JButton nextButton;
    private int currentStep = 1;
    java.util.List<BaseWizardStepPanel> stepPanels;

    public SystemBuildingWizard(JFrame parent) {
        super(parent, "System Building Wizard", true);
        componentManager = WizardComponentManager.getInstance();
        setResizable(true);

        // Get the screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // Set the dialog size to 90% of screen size
        setSize((int) (screenSize.width * 0.9), (int) (screenSize.height * 0.9));
        setLocationRelativeTo(parent);

        // Create panels
        stepPanels = new ArrayList<>();
        stepPanels.add(null);
        stepPanels.add(new WizardStep1Panel());
        stepPanels.add(new WizardStep2Panel());
        stepPanels.add(new WizardStep3Panel());
        stepPanels.add(new WizardStep4Panel());
        stepPanels.add(new WizardStep5Panel());
        stepPanels.add(new WizardStep6Panel());
        stepPanels.add(new WizardStep7Panel());
        stepPanels.add(new WizardStep8Panel());
        stepPanels.add(new WizardStep9Panel());
        stepPanels.add(new WizardStep10ResultsPanel());

        // Setup card layout
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        for (int i = 1; i < stepPanels.size() - 1; i++) {
            cardPanel.add(stepPanels.get(i), "step" + i);
        }
        cardPanel.add(stepPanels.get(stepPanels.size() - 1), "results");
        /*
         * cardPanel.add(step1Panel, "step1");
         * cardPanel.add(step2Panel, "step2");
         * cardPanel.add(step3Panel, "step3");
         * cardPanel.add(step4Panel, "step4");
         * cardPanel.add(step5Panel, "step5");
         * cardPanel.add(step6Panel, "step6");
         * cardPanel.add(step7Panel, "step7");
         * cardPanel.add(step8Panel, "step8");
         * cardPanel.add(step9Panel, "step9");
         * cardPanel.add(resultsPanel, "results");
         */
        // Navigation buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backButton = new JButton("Back");
        nextButton = new JButton("Next");
        JButton cancelButton = new JButton("Cancel");

        backButton.addActionListener(e -> handleBack());
        nextButton.addActionListener(e -> handleNext());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(backButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(cancelButton);

        // Layout
        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Initial button state
        backButton.setEnabled(false);
        updateButtonStates();
    }

    private void handleBack() {
        if (currentStep > 1) {
            // String component = getComponentByStep(currentStep);
            // selectedComponentPrices.put(component, 0.0);
            resetComponentByStep(currentStep);
            currentStep--;
            cardLayout.show(cardPanel, "step" + currentStep);
            updateButtonStates();
        }
    }

    protected void resetComponentByStep(int step) {
        if (step > 0 && step < stepPanels.size()) {
            stepPanels.get(step).onPressBackButton();
        }
    }

    private void handleNext() {
        if (currentStep < 10) {
            if (validateCurrentStep()) {
                if (currentStep >= 1) {
                    stepPanels.get(currentStep + 1).updateWizardStepDataTable();
                }
                /*
                 * if (validateCurrentStep()) {
                 * if (currentStep == 1) {
                 * step2Panel.updateWizardStepDataTable();
                 * } else if (currentStep == 2) {
                 * step3Panel.updateWizardStepDataTable();
                 * } else if (currentStep == 3) {
                 * step4Panel.updateWizardStepDataTable();
                 * } else if (currentStep == 4) {
                 * step5Panel.updateWizardStepDataTable();
                 * } else if (currentStep == 5) {
                 * step6Panel.updateWizardStepDataTable();
                 * } else if (currentStep == 6) {
                 * step7Panel.updateWizardStepDataTable();
                 * } else if (currentStep == 7) {
                 * step8Panel.updateWizardStepDataTable();
                 * } else if (currentStep == 8) {
                 * step9Panel.updateWizardStepDataTable();
                 * } else if (currentStep == 9) {
                 * generateCombinations();
                 * }
                 */

                currentStep++;
                cardLayout.show(cardPanel, currentStep == 10 ? "results" : "step" + currentStep);
                updateButtonStates();
            }
        } else if (currentStep == stepPanels.size() - 1) {
            // Handle export functionality
            exportToExcel();
        }
    }

    private boolean validateCurrentStep() {
        if (currentStep > 0 && currentStep < stepPanels.size()) {
            return stepPanels.get(currentStep).validateSelection();
        }
        return true;
    }

    private void updateButtonStates() {
        backButton.setEnabled(currentStep > 1);
        if (currentStep == (stepPanels.size() - 2)) {
            nextButton.setText("Finish");
        } else if (currentStep == 10) {
            nextButton.setText("Export");
        } else {
            nextButton.setText("Next");
        }
    }

    private void exportToExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Excel File");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.endsWith(".xlsx")) {
                filePath += ".xlsx";
            }

            try {
                stepPanels.get(stepPanels.size() - 1).exportToExcel(filePath);
                JOptionPane.showMessageDialog(this,
                        "Export successful!\nFile saved to: " + filePath,
                        "Export Complete",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error exporting to Excel: " + e.getMessage(),
                        "Export Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    public static void showDialog(JFrame parent) {
        SystemBuildingWizard wizard = new SystemBuildingWizard(parent);
        wizard.setVisible(true);
    }
}

/*
 * private String getComponentByStep(int step) {
 * switch (step) {
 * case 2:
 * return "Mainboard";
 * case 3:
 * return "CPU";
 * case 4:
 * return "RAM";
 * case 5:
 * return "GPU";
 * case 6:
 * return "HardDisk";
 * case 7:
 * return "Cooler";
 * case 8:
 * return "ComputerCase";
 * case 9:
 * return "PowerSource";
 * default:
 * return "";
 * }
 * }
 * private int calculateTotalPower() {
 * int totalPower = 0;
 * 
 * // Add mainboard power
 * if (step2Panel.getSelectedMainboard() != null) {
 * totalPower += step2Panel.getSelectedMainboard().getPower();
 * }
 * 
 * // Add CPU power (multiplied by quantity)
 * if (step3Panel.selectedCpu != null) {
 * totalPower += step3Panel.selectedCpu.getPower() * step1Panel.getCpuNumber();
 * }
 * 
 * // Add RAM power (multiplied by quantity)
 * if (step4Panel.selectedRam != null) {
 * totalPower += step4Panel.selectedRam.getPower() *
 * step4Panel.selectedRamNumber;
 * }
 * 
 * // Add GPU power (multiplied by quantity)
 * if (step5Panel.getSelectedGPU() != null) {
 * totalPower += step5Panel.getSelectedGPU().getPower() *
 * step1Panel.getGpuNumber();
 * }
 * 
 * // Add Hard Disk power
 * if (step6Panel.getSelectedHardDisk() != null) {
 * totalPower += step6Panel.getSelectedHardDisk().getPower();
 * }
 * 
 * // Add Cooler power
 * if (step7Panel.getSelectedCooler() != null) {
 * totalPower += step7Panel.getSelectedCooler().getPower();
 * }
 * 
 * // Add Computer Case power
 * if (step8Panel.getSelectedComputerCase() != null) {
 * totalPower += step8Panel.getSelectedComputerCase().getPower();
 * }
 * 
 * // Add a 20% safety margin
 * totalPower = (int) (totalPower * 1.2);
 * 
 * System.out.println("Calculated total power requirement: " + totalPower +
 * "W (including 20% safety margin)");
 * return totalPower;
 * }
 * 
 * private boolean validateCurrentStep00() {
 * switch (currentStep) {
 * case 1:
 * return step1Panel.validateSelection();
 * case 2:
 * return step2Panel.validateSelection();
 * case 3:
 * return step3Panel.validateSelection();
 * case 4:
 * return step4Panel.validateSelection();
 * case 5:
 * return step5Panel.validateSelection();
 * case 6:
 * return step6Panel.validateSelection();
 * case 7:
 * return step7Panel.validateSelection();
 * case 8:
 * return step8Panel.validateSelection();
 * case 9:
 * return step9Panel.validateSelection();
 * default:
 * return true;
 * }
 * }
 * 
 * 
 * 
 * private void generateCombinations() {
 * // Add the current combination
 * resultsPanel.addCombination(
 * componentManager.selectedMainboard,
 * componentManager.selectedCpu,
 * componentManager.selectedRam,
 * componentManager.selectedGpu,
 * componentManager.selectedHardDisk,
 * componentManager.selectedCooler,
 * componentManager.selectedComputerCase,
 * componentManager.selectedPowerSource,
 * componentManager.baseCpuNumber,
 * componentManager.baseRamNumber,
 * componentManager.baseGpuNumber);
 * 
 * }
 *
 * // step1Panel = new WizardStep1Panel();
 * // step2Panel = new WizardStep2Panel();
 * // step3Panel = new WizardStep3Panel();
 * // step4Panel = new WizardStep4Panel();
 * // step5Panel = new WizardStep5Panel();
 * // step6Panel = new WizardStep6Panel(); // Hard Disk selection panel
 * // step7Panel = new WizardStep7Panel(); // Cooler selection panel
 * // step8Panel = new WizardStep8Panel(); // Computer Case selection panel
 * // step9Panel = new WizardStep9Panel(); // Power Source selection panel
 * // resultsPanel = new WizardResultsPanel();
 * // Object[] stepPanels = new Object[11];
 * private WizardStep1Panel step1Panel;
 * private WizardStep2Panel step2Panel;
 * private WizardStep3Panel step3Panel;
 * private WizardStep4Panel step4Panel;
 * private WizardStep5Panel step5Panel;
 * private WizardStep6Panel step6Panel;
 * private WizardStep7Panel step7Panel;
 * private WizardStep8Panel step8Panel;
 * private WizardStep9Panel step9Panel;
 * private WizardStep10ResultsPanel resultsPanel;
 */
