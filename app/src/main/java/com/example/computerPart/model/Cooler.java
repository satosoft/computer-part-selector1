package com.example.computerPart.model;

import java.util.List;
import java.util.ArrayList;

public class Cooler extends BaseComputerPart {
    private String coolerType;
    private List<String> socketSupport;
    private String ledColor;

    // Default constructor
    public Cooler() {
        super();
        this.socketSupport = new ArrayList<>();
    }

    // Constructor
    public Cooler(int id, String name, String manufacturer, int stock, int power, String color,double price,
             String coolerType, List<String> socketSupport, String ledColor) {
        super(id, name, manufacturer, stock, power, color, price);
        this.coolerType = coolerType;
        this.socketSupport = socketSupport != null ? new ArrayList<>(socketSupport) : new ArrayList<>();
        this.ledColor = ledColor;
    }

    // Getters and Setters for Cooler-specific fields
    public String getCoolerType() {
        return coolerType;
    }

    public void setCoolerType(String coolerType) {
        this.coolerType = coolerType;
    }

    public List<String> getSocketSupport() {
        return new ArrayList<>(socketSupport);
    }

    public void setSocketSupport(List<String> socketSupport) {
        this.socketSupport = new ArrayList<>(socketSupport);
    }

    public String getLedColor() {
        return ledColor;
    }

    public void setLedColor(String ledColor) {
        this.ledColor = ledColor;
    }
}