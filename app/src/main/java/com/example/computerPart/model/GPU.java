package com.example.computerPart.model;

public class GPU extends BaseComputerPart {
    private int capacity; // in GB
    private double benchmark;

    // Default constructor
    public GPU() {
        super();
    }

    // Constructor
    public GPU(int id, String name, String manufacturer, int stock,int power, String color, double price,
            int capacity, double benchmark) {
        super(id, name, manufacturer, stock, power, color, price);
        this.capacity = capacity;
        this.benchmark = benchmark;
    }

    // Getters and Setters for GPU-specific fields
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
}