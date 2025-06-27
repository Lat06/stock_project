package org.example.shared.model;

import java.io.Serializable;

public class ProductGroupStats implements Serializable {
    private String groupName;
    private int productCount;
    private double totalPrice;
    private double averagePrice;

    public ProductGroupStats() {}

    public ProductGroupStats(String groupName, int productCount, double totalPrice, double averagePrice) {
        this.groupName = groupName;
        this.productCount = productCount;
        this.totalPrice = totalPrice;
        this.averagePrice = averagePrice;
    }

    public String getGroupName() {
        return groupName;
    }

    public int getProductCount() {
        return productCount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getAveragePrice() {
        return averagePrice;
    }
}
