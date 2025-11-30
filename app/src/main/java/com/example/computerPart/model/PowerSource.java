package com.example.computerPart.model;

public class PowerSource extends BaseComputerPart {
    // Default constructor
    public PowerSource() {
        super();
    }

    // Constructor
    public PowerSource(int id, String name, String manufacturer, int stock,int power, String color, double price) {
        super(id, name, manufacturer, stock, power, color, price);
    }
}