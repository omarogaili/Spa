package com.Spa.spa.Services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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

    public Double calculatePrice(PackageSnapShot spaPackage, int numberOfPeople, double standardPrice) {
        double unitPrice = standardPrice + spaPackage.getPrice();
        double totalPrice = 0;
        if (numberOfPeople > 1) {
            for(int i= 0; i < numberOfPeople; i++){
                totalPrice += unitPrice - (i * 50);
            }
            return totalPrice;
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
        double totalPrice = calculatePrice(packageSnapShot, people, order.getStandardPrice());
        order.setTotalPrice(totalPrice);
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
        existingOrder.setStandardPrice(order.getStandardPrice());
        double newPrice = calculatePrice(existingOrder.getPackageSnapShot(), order.getNumberOfPeople(), order.getStandardPrice());
        existingOrder.setTotalPrice(newPrice);
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
        return mongoOperations.findAll(Order.class);
    }

    @Override
    @SuppressWarnings("all")
    public Iterable<Order> getLastMonthOrders(){
        try{
            LocalDate lastMonth = LocalDate.now().minusMonths(1);
            Query query = new Query();
            query.addCriteria(Criteria.where("orderDate").gte(lastMonth));
            return mongoOperations.find(query, Order.class);

        } catch(Exception e){
            throw new RuntimeException("failed", e);
        }
    }

    @Override
    public Iterable<Order> getLastWeekOrders(){
        try{
            LocalDate lastWeek = LocalDate.now().minusWeeks(1);
            Query query = new Query();
            query.addCriteria(Criteria.where("orderDate").gte(lastWeek));
            return mongoOperations.find(query, Order.class);
        }catch(Exception e){
            throw new RuntimeException("failed", e);
        }
    }

    @Override
    public boolean isfullBooked (){
        LocalDate currentDay = LocalDate.now();
        LocalDateTime startOfTheDay= currentDay.atStartOfDay();
        LocalDateTime endOfTheDay = currentDay.plusDays(1).atStartOfDay();
        Query query = new Query();
        query.addCriteria(Criteria.where("orderDate").gte(startOfTheDay).lt(endOfTheDay));
        long currentDayBookings = mongoOperations.count(query,Order.class);
        return currentDayBookings >= 20;
    }

    @Override
    public boolean isSpaHouseClosed (){
        LocalDate today = LocalDate.now();
        DayOfWeek todaysName = today.getDayOfWeek();
        return todaysName == DayOfWeek.MONDAY || todaysName == DayOfWeek.SATURDAY || todaysName == DayOfWeek.SUNDAY;
    }

    @Override
    public boolean isSpaPackageFullBooked(PackageSnapShot snapShot){
        LocalDate currentDay = LocalDate.now();
        LocalDateTime startOfTheDay = currentDay.atStartOfDay();
        LocalDateTime endOfTheDay = currentDay.plusDays(1).atStartOfDay();

        Query qr = new Query();
        qr.addCriteria(Criteria.where("orderDate").gte(startOfTheDay).lt(endOfTheDay).and("packageName").is(snapShot.getName()));
        long specialPackageBookings = mongoOperations.count(qr,Order.class);
        return specialPackageBookings >= 5;
    }
}
