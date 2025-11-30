package com.example.computerPart.model;

public class ComputerCase extends BaseComputerPart {
    private String caseSize; // mATX, ATX//
    private String caseMaterial;
    private String caseType;

    // Default constructor
    public ComputerCase() {
        super();
    }

    // Constructor
    public ComputerCase(int id, String name, String manufacturer, int stock, int power, String color, double price,
    String caseSize, String caseMaterial, String caseType) {
        super(id, name, manufacturer, stock, power, color, price);
        this.caseSize = caseSize;
        this.caseMaterial = caseMaterial;
        this.caseType = caseType;
    }

    // Getters and Setters for Case-specific fields
    public String getCaseSize() {
        return caseSize;
    }

    public void setCaseSize(String caseSize) {
        this.caseSize = caseSize;
    }

    public String getCaseMaterial() {
        return caseMaterial;
    }

    public void setCaseMaterial(String caseMaterial) {
        this.caseMaterial = caseMaterial;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }
}