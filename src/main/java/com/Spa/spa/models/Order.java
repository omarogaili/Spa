package com.Spa.spa.models;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private String customerName;
    private String spaPackage;
    private int numberOfPeople;
    private double price;
    private LocalDate orderDate;
    private String telephoneNumber;
    public Order(String id, String customerName, String spaPackage, int numberOfPeople, double price,
            LocalDate orderDate, String telephoneNumber) {
        this.id = id;
        this.customerName = customerName;
        this.spaPackage = spaPackage;
        this.numberOfPeople = numberOfPeople;
        this.price = price;
        this.orderDate = orderDate;
        this.telephoneNumber = telephoneNumber;
    }

    public String getId() {
        return id;
    }
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public String getSpaPackage() {
        return spaPackage;
    }
    public void setSpaPackage(String spaPackage) {
        this.spaPackage = spaPackage;
    }
    public int getNumberOfPeople() {
        return numberOfPeople;
    }
    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public LocalDate getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }
    public String getTelephoneNumber() {
        return telephoneNumber;
    }
    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }
    


}
