package com.example.computerPart.model;

public class CPU extends BaseComputerPart {
    private String generation;
    private String socket;
    private double benchmark;
    private int coreNum;

    // Default constructor
    public CPU() {
        super();
    }

    // Constructor
    public CPU(int id, String name, String manufacturer, int stock,int power, String color, double price,
            String generation, String socket, double benchmark,  int coreNum) {
        super(id, name, manufacturer, stock, power, color, price);
        this.generation = generation;
        this.socket = socket;
        this.benchmark = benchmark;
        this.coreNum = coreNum;
    }

    // Getters and Setters for CPU-specific fields
    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }

    public String getSocket() {
        return socket;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    public double getBenchmark() {
        return benchmark;
    }

    public void setBenchmark(double benchmark) {
        this.benchmark = benchmark;
    }

    public int getCoreNum() {
        return coreNum;
    }

    public void setCoreNum(int coreNum) {
        this.coreNum = coreNum;
    }
}