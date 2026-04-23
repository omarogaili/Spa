package com.Spa.spa.models;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private String customerName;
    private int numberOfPeople;
    private String telephoneNumber;
    private String email;
    private double totalPrice;
    private double standardPrice;
    private LocalDate orderDate;
    private PackageSnapShot packageSnapShot;
    private String packageId;
    private String packageName;


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Order(String id, String customerName, int numberOfPeople, double standardPrice,
            LocalDate orderDate, String telephoneNumber, String email, PackageSnapShot packageSnapShot, String packageId, String packageName) {
        this.id = id;
        this.customerName = customerName;
        this.numberOfPeople = numberOfPeople;
        this.standardPrice = standardPrice;
        this.orderDate = orderDate;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
        this.packageSnapShot = packageSnapShot;
        this.packageId = packageId;
        this.packageName = packageName;
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

    public int getNumberOfPeople() {
        return numberOfPeople;
    }
    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }
    public double getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public double getStandardPrice() {
        return standardPrice;
    }
    public void setStandardPrice(double standardPrice) {
        this.standardPrice = standardPrice;
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

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public PackageSnapShot getPackageSnapShot() {
        return packageSnapShot;
    }
    public void setPackageSnapShot(PackageSnapShot packageSnapShot) {
        this.packageSnapShot = packageSnapShot;
    }

    public String getPackageId() {
        return packageId;
    }
    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }
    
}
