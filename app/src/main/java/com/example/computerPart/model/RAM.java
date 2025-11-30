package com.example.computerPart.model;

public class RAM extends BaseComputerPart {
    private int capacity; // in GB
    private double benchmark;
    private String ramType; // DDR3/4/5
    private int bus;

    // Default constructor
    public RAM() {
        super();
    }

    // Constructor
    public RAM(int id, String name, String manufacturer, int stock,int power, String color, double price,
            int capacity, double benchmark, String ramType, int bus) {
        super(id, name, manufacturer, stock, power, color, price);
        this.capacity = capacity;
        this.benchmark = benchmark;
        this.ramType = ramType;
        this.bus = bus;
    }

    // Getters and Setters for RAM-specific fields
    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getBenchmark() {
        return benchmark;
    }

    public void setBenchmark(double benchmark) {
        this.benchmark = benchmark;
    }

    public String getRamType() {
        return ramType;
    }

    public void setRamType(String ramType) {
        this.ramType = ramType;
    }

    public int getBus() {
        return bus;
    }

    public void setBus(int bus) {
        this.bus = bus;
    }
}