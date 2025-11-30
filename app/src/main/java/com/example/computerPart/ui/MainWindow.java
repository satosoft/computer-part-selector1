package com.example.computerPart.ui;

import com.example.computerPart.ui.wizard.SystemBuildingWizard;
import com.example.computerPart.ui.autobuild.AutoSystemBuilder;
import com.example.computerPart.ui.panel.*;
//import com.example.computerPart.ui.wizard.*;
//import com.example.computerPart.ui.dialog.*;
import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private JTabbedPane tabbedPane;

    public MainWindow() {
        setTitle("Computer Parts Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        // Build System menu
        JMenu buildMenu = new JMenu("WizardBuild");
        JMenuItem startWizardItem = new JMenuItem("Start Wizard");
        startWizardItem.addActionListener(e -> SystemBuildingWizard.showDialog(this));
        buildMenu.add(startWizardItem);
        menuBar.add(buildMenu);

        // Build AutoBuild menu
        JMenu autoBuildMenu = new JMenu("AutoBuild");
        JMenuItem startAutoBuildItem = new JMenuItem("Start AutoBuild");
        startAutoBuildItem.addActionListener(e -> AutoSystemBuilder.showDialog(this));
        autoBuildMenu.add(startAutoBuildItem);
        menuBar.add(autoBuildMenu);

        setJMenuBar(menuBar);

        // Create toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        // Add Build System button
        // JButton buildSystemButton = new JButton("Build System");
        // buildSystemButton.addActionListener(e ->
        // SystemBuildingWizard.showDialog(this));
        // toolBar.add(buildSystemButton);

        add(toolBar, BorderLayout.NORTH);

        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("CPU", new CPUPanel());
        tabbedPane.addTab("Mainboard", new MainboardPanel());
        tabbedPane.addTab("RAM", new RAMPanel());
        tabbedPane.addTab("GPU", new GPUPanel());
        tabbedPane.addTab("Hard Disk", new HardDiskPanel());
        tabbedPane.addTab("Power Source", new PowerSourcePanel());
        tabbedPane.addTab("Cooler", new CoolerPanel());
        tabbedPane.addTab("Computer Case", new ComputerCasePanel());

        add(tabbedPane, BorderLayout.CENTER);
    }
}