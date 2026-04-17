package com.Spa.spa.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "packages")
public class Package {
    private String id;
    private String name;
    private String description;
    private double price;
    private double discountPercentage;
    public Package(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public double getDiscountPercentage() {
        return discountPercentage;
    }
    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
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
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    

    
}
