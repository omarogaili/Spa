package com.Spa.spa.Services;

import java.util.List;

import com.Spa.spa.models.Order;
import com.Spa.spa.models.PackageSnapShot;

public interface IOrderServices {
    public Order addOrder(String packageId, Order order);
    public Order updateOrder(String id, Order order, String packageName);
    public String deleteOrder(String id);
    public Order getOrderById(String id);
    public List<Order> getAllOrders();
    public List<Order> getLastMonthOrders();
    public List<Order> getLastWeekOrders();
    public long lastMonthOverView();
    public long lastWeekOverView();
    public long isfullBooked ();
    public boolean isSpaPackageFullBooked(PackageSnapShot snapShot);
    public boolean isSpaHouseClosed ();
}
