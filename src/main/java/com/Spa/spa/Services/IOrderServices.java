package com.Spa.spa.Services;

import com.Spa.spa.models.Order;

public interface IOrderServices {
    public String addOrder(String packageId, Order order);
    public String updateOrder(String id, Order order);
    public String deleteOrder(String id);
    public Order getOrderById(String id);
    public Iterable<Order> getAllOrders();
}
