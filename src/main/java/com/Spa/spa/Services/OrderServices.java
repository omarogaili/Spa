package com.Spa.spa.Services;

import java.time.LocalDate;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import com.Spa.spa.models.Order;
import com.Spa.spa.models.Package;
import com.Spa.spa.models.PackageSnapShot;


@Service
public class OrderServices implements IOrderServices {
    MongoOperations mongoOperations;

    public OrderServices(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public Double calculatePrice(PackageSnapShot spaPackage, int numberOfPeople) {
        double unitPrice = spaPackage.getPrice();
        if (numberOfPeople > 2) {
            double firstPersonPrice = unitPrice;
            double additionalPersonPrice = (numberOfPeople - 1) * 50;
            return firstPersonPrice + additionalPersonPrice;
        } else {
            return unitPrice * numberOfPeople;
        }
    }

    @Override
    public String addOrder(String packageId, Order order) {
        Package spaPackage = mongoOperations.findById(packageId, Package.class);
        if (spaPackage == null) {
            return "Package not found";
        }
        if (order == null) {
            return "Order cannot be null";
        }

        PackageSnapShot packageSnapShot = new PackageSnapShot(spaPackage.getId(), spaPackage.getName(),
                spaPackage.getDescription(), spaPackage.getPrice(), spaPackage.getDiscountPercentage());
        order.setPackageSnapShot(packageSnapShot);
        int people = order.getNumberOfPeople();
        double totalPrice = calculatePrice(packageSnapShot, people);
        order.setPrice(totalPrice);
        order.setPackageId(packageId);
        order.setOrderDate(LocalDate.now());
        mongoOperations.save(order);
        return "An confirmation email has been sent to " + order.getEmail();
    }

    @Override
    public String updateOrder(String id, Order order) {
        Order existingOrder = mongoOperations.findById(id, Order.class);
        if (existingOrder == null) {
            return "Order not found";
        }
        existingOrder.setCustomerName(order.getCustomerName());
        existingOrder.setNumberOfPeople(order.getNumberOfPeople());
        double newPrice = calculatePrice(existingOrder.getPackageSnapShot(), order.getNumberOfPeople());
        existingOrder.setPrice(newPrice);
        existingOrder.setOrderDate(LocalDate.now());
        existingOrder.setTelephoneNumber(order.getTelephoneNumber());
        existingOrder.setEmail(order.getEmail());
        mongoOperations.save(existingOrder);
        return "Order updated successfully";
    }

    @Override
    public String deleteOrder(String id) {
        Order existingOrder = mongoOperations.findById(id, Order.class);
        if (existingOrder == null) {
            return "Order not found";
        }
        mongoOperations.remove(existingOrder);
        return "Order deleted successfully";
    }

    @Override
    public Order getOrderById(String id) {
        Order order = mongoOperations.findById(id, Order.class);
        if(order == null){
            return null;
        }
        return order;
    }

    @Override
    public Iterable<Order> getAllOrders() {
        Iterable<Order> orders = mongoOperations.findAll(Order.class);
        if(orders == null){
            return null;
        }
        return orders;
    }

}
