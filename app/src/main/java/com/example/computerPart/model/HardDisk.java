package com.example.computerPart.model;

public class HardDisk extends BaseComputerPart {
    private String type; // HDD, SSD, NVME
    private int speed; // Speed in MB/s
    private int capacity; // Capacity in GB

    // Default constructor
    public HardDisk() {
        super();
    }

    // Constructor
    public HardDisk(int id, String name, String manufacturer, int stock,int power, String color, double price,
            String type, int speed, int capacity) {
        super(id, name, manufacturer, stock, power, color, price);
        this.type = type;
        this.speed = speed;
        this.capacity = capacity;
    }

    // Getters and Setters for HardDisk-specific fields
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}