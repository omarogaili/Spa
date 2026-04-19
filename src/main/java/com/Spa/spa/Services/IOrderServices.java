package com.Spa.spa.Services;

import com.Spa.spa.models.Order;
import com.Spa.spa.models.PackageSnapShot;

public interface IOrderServices {
    public String addOrder(String packageId, Order order);
    public String updateOrder(String id, Order order);
    public String deleteOrder(String id);
    public Order getOrderById(String id);
    public Iterable<Order> getAllOrders();
    public Iterable<Order> getLastMonthOrders();
    public Iterable<Order> getLastWeekOrders();
    public boolean isfullBooked ();
    public boolean isSpaPackageFullBooked(PackageSnapShot snapShot);
    public boolean isSpaHouseClosed ();
}
