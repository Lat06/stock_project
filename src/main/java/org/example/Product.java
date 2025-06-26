package org.example;

public class Product {
    private int id;
    private String name;
    private String description;
    private String manufacturer;
    private int quantity;
    private double pricePerUnit;
    private ProductGroup group;

    public Product(int id, String name, String description, String manufacturer,
                   int quantity, double pricePerUnit, ProductGroup group) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.manufacturer = manufacturer;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.group = group;
    }

    // Для створення без ID (нові об'єкти)
    public Product(String name, String description, String manufacturer,
                   int quantity, double pricePerUnit, ProductGroup group) {
        this(-1, name, description, manufacturer, quantity, pricePerUnit, group);
    }

    // Getters і Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public ProductGroup getGroup() {
        return group;
    }

    public void setGroup(ProductGroup group) {
        this.group = group;
    }
}
