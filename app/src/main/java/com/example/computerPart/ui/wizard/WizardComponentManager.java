package com.example.computerPart.ui.wizard;

import com.example.computerPart.model.*;

//Bill Pugh singleton implementation
public final class WizardComponentManager {

    private WizardComponentManager() {
        // Private constructor
    }

    private static class SingletonHelper {
        private static final WizardComponentManager INSTANCE = new WizardComponentManager();
    }

    public static WizardComponentManager getInstance() {
        return SingletonHelper.INSTANCE;
    }

    protected Mainboard selectedMainboard;
    protected CPU selectedCpu;
    protected RAM selectedRam;
    protected GPU selectedGpu;
    protected HardDisk selectedHardDisk;
    protected Cooler selectedCooler;
    protected ComputerCase selectedComputerCase;
    protected PowerSource selectedPowerSource;

    protected double baseBudget;
    protected int baseCpuNumber;
    protected int baseRamNumber;
    protected int baseRamCapacity;
    protected int baseGpuNumber;
    protected int baseGpuMinCapacity;
    protected int baseGpuMaxCapacity;
    protected int baseHardDiskMinCapacity;
    protected int baseHardDiskMaxCapacity;
    protected String baseHardDiskType;
    protected int baseMaxSearchTime;
    protected String basePreferredColor;
    protected String baseMainSize;

    /*
     * private static WizardComponentManager instance;
     * private WizardComponentManager() {
     * // Private constructor
     * }
     * 
     * public static synchronized WizardComponentManager getInstance() {
     * if (instance == null) {
     * instance = new WizardComponentManager();
     * }
     * return instance;
     * }
     */

    public void clearSelections() {
        selectedMainboard = null;
        selectedCpu = null;
        selectedRam = null;
        selectedGpu = null;
        selectedHardDisk = null;
        selectedCooler = null;
        selectedComputerCase = null;
        selectedPowerSource = null;
    }

    protected boolean checkCompleteSelection() {
        return selectedMainboard != null &&
                selectedCpu != null &&
                selectedRam != null &&
                selectedGpu != null &&
                selectedHardDisk != null &&
                selectedCooler != null &&
                selectedComputerCase != null &&
                selectedPowerSource != null;
    }

    protected double calculateTotalCost() {
        return (selectedMainboard != null ? selectedMainboard.getPrice() : 0)
                + (selectedCpu != null ? (selectedCpu.getPrice() * baseCpuNumber) : 0)
                + (selectedRam != null ? (selectedRam.getPrice() * baseRamNumber) : 0)
                + (selectedGpu != null ? (selectedGpu.getPrice() * baseGpuNumber) : 0)
                + (selectedHardDisk != null ? selectedHardDisk.getPrice() : 0)
                + (selectedCooler != null ? (selectedCooler.getPrice() * baseCpuNumber) : 0)
                + (selectedComputerCase != null ? selectedComputerCase.getPrice() : 0)
                + (selectedPowerSource != null ? selectedPowerSource.getPrice() : 0);
    }

    protected int calculateTotalPower() {
        return (selectedMainboard != null ? selectedMainboard.getPower() : 0)
                + (selectedCpu != null ? (selectedCpu.getPower() * baseCpuNumber) : 0)
                + (selectedRam != null ? (selectedRam.getPower() * baseRamNumber) : 0)
                + (selectedGpu != null ? (selectedGpu.getPower() * baseGpuNumber) : 0)
                + (selectedHardDisk != null ? selectedHardDisk.getPower() : 0)
                + (selectedCooler != null ? (selectedCooler.getPower() * baseCpuNumber) : 0)
                + (selectedComputerCase != null ? selectedComputerCase.getPower() : 0);
        // + (selectedPowerSource != null ? selectedPowerSource.getPower() : 0);
    }

    protected double calculateTotalBenchmark() {

        double cpuBenchmark = (selectedCpu != null) ? (selectedCpu.getBenchmark() * baseCpuNumber) : 0;
        double gpuBenchmark = (selectedGpu != null) ? (selectedGpu.getBenchmark() * baseGpuNumber) : 0;
        double ramBenchmark = (selectedRam != null) ? (selectedRam.getBus() * selectedRam.getCapacity() / 10) : 0;
        double hardDiskBenchmark = (selectedHardDisk != null)
                ? (selectedHardDisk.getSpeed() * selectedHardDisk.getCapacity() / 1000)
                : 0;
        return cpuBenchmark + gpuBenchmark + ramBenchmark + hardDiskBenchmark;
    }
}
/*
 * // Getters and setters for each component
 * public Mainboard getSelectedMainboard() {
 * return selectedMainboard;
 * }
 * 
 * public void setSelectedMainboard(Mainboard mainboard) {
 * this.selectedMainboard = mainboard;
 * }
 * 
 * public void resetSelectedMainboard() {
 * this.selectedMainboard = null;
 * }
 * 
 * public CPU getSelectedCpu() {
 * return selectedCpu;
 * }
 * 
 * public void setSelectedCpu(CPU cpu) {
 * this.selectedCpu = cpu;
 * }
 * 
 * public void resetSelectedCpu() {
 * this.selectedCpu = null;
 * }
 * 
 * public RAM getSelectedRam() {
 * return selectedRam;
 * }
 * 
 * public void setSelectedRam(RAM ram) {
 * this.selectedRam = ram;
 * }
 * 
 * public void resetSelectedRam() {
 * this.selectedRam = null;
 * }
 * 
 * public GPU getSelectedGpu() {
 * return selectedGpu;
 * }
 * 
 * public void setSelectedGpu(GPU gpu) {
 * this.selectedGpu = gpu;
 * }
 * 
 * public void resetSelectedGpu() {
 * this.selectedGpu = null;
 * }
 * 
 * public HardDisk getSelectedHardDisk() {
 * return selectedHardDisk;
 * }
 * 
 * public void setSelectedHardDisk(HardDisk hardDisk) {
 * this.selectedHardDisk = hardDisk;
 * }
 * 
 * public void resetSelectedHardDisk() {
 * this.selectedHardDisk = null;
 * }
 * 
 * public Cooler getSelectedCooler() {
 * return selectedCooler;
 * }
 * 
 * public void setSelectedCooler(Cooler cooler) {
 * this.selectedCooler = cooler;
 * }
 * 
 * public void resetSelectedCooler() {
 * this.selectedCooler = null;
 * }
 * 
 * public ComputerCase getSelectedComputerCase() {
 * return selectedComputerCase;
 * }
 * 
 * public void setSelectedComputerCase(ComputerCase computerCase) {
 * this.selectedComputerCase = computerCase;
 * }
 * 
 * public void resetSelectedComputerCase() {
 * this.selectedComputerCase = null;
 * }
 * 
 * public PowerSource getSelectedPowerSource() {
 * return selectedPowerSource;
 * }
 * 
 * public void setSelectedPowerSource(PowerSource powerSource) {
 * this.selectedPowerSource = powerSource;
 * }
 * 
 * public void resetSelectedPowerSource() {
 * this.selectedPowerSource = null;
 * }
 * protected double calculatePreviousCost() {
 * return (selectedMainboard != null ? selectedMainboard.getPrice() : 0)
 * + (selectedCpu != null ? (selectedCpu.getPrice() * cpuNumber) : 0)
 * + (selectedRam != null ? (selectedRam.getPrice() * ramNumber) : 0)
 * + (selectedGpu != null ? (selectedGpu.getPrice()*gpuNumber) : 0)
 * + (selectedHardDisk != null ? selectedHardDisk.getPrice() : 0)
 * + (selectedCooler != null ? selectedCooler.getPrice() : 0)
 * + (selectedComputerCase != null ? selectedComputerCase.getPrice() : 0);
 * // (selectedPowerSource != null ? selectedPowerSource.getPrice() : 0);
 * }
 */