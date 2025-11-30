package com.example.computerPart.model;

public class Mainboard extends BaseComputerPart {
    private String cpuSocket;
    private int ramSlots;
    private int gpuSlots;
    private int cpuSlots;
    private boolean supportNVME;
    private String mainSize; // mATX, ATX
    private String ramType; // DDR4, DDR5

    // Default constructor
    public Mainboard() {
        super();
    }

    // Constructor
    public Mainboard(int id, String name, String manufacturer, int stock,int power, String color, double price,
            String cpuSocket, int ramSlots, int gpuSlots, int cpuSlots,
            boolean supportNVME, String mainSize, String ramType) {
        super(id, name, manufacturer, stock, power, color, price);
        this.cpuSocket = cpuSocket;
        this.ramSlots = ramSlots;
        this.gpuSlots = gpuSlots;
        this.cpuSlots = cpuSlots;
        this.supportNVME = supportNVME;
        this.mainSize = mainSize;
        this.ramType = ramType;
    }

    // Getters and Setters for Mainboard-specific fields
    public String getCpuSocket() {
        return cpuSocket;
    }

    public void setCpuSocket(String cpuSocket) {
        this.cpuSocket = cpuSocket;
    }

    public int getRamSlots() {
        return ramSlots;
    }

    public void setRamSlots(int ramSlots) {
        this.ramSlots = ramSlots;
    }

    public int getGpuSlots() {
        return gpuSlots;
    }

    public void setGpuSlots(int gpuSlots) {
        this.gpuSlots = gpuSlots;
    }

    public int getCpuSlots() {
        return cpuSlots;
    }

    public void setCpuSlots(int cpuSlots) {
        this.cpuSlots = cpuSlots;
    }

    public boolean isSupportNVME() {
        return supportNVME;
    }

    public void setSupportNVME(boolean supportNVME) {
        this.supportNVME = supportNVME;
    }

    public String getMainSize() {
        return mainSize;
    }

    public void setMainSize(String mainSize) {
        this.mainSize = mainSize;
    }

    public String getRamType() {
        return ramType;
    }

    public void setRamType(String ramType) {
        this.ramType = ramType;
    }
}