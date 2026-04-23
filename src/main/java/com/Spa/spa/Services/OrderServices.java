package com.Spa.spa.Services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

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
            for (int i = 0; i < numberOfPeople; i++) {
                totalPrice += unitPrice - (i * 50);
            }
            return totalPrice;
        } else {
            return unitPrice * numberOfPeople;
        }
    }

    @Override
    public Order addOrder(String packageId, Order order) {
        Package spaPackage = mongoOperations.findById(packageId, Package.class);
        if (spaPackage == null) {
            return null;
        }
        if (order == null) {
            return null;
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
        return order;
    }

    @Override
    public Order updateOrder(String id, Order order, String packageName) {
        Order existingOrder = mongoOperations.findById(id, Order.class);
        if (existingOrder == null || order == null) {
            return null;
        }


        Query qr = new Query();
        qr.addCriteria(Criteria.where("name").is(packageName));
        Package spaPackage = mongoOperations.findOne(qr, Package.class);
        if (spaPackage == null) {
            return null;
        }

        PackageSnapShot packageSnapShot = new PackageSnapShot(
                spaPackage.getId(),
                spaPackage.getName(),
                spaPackage.getDescription(),
                spaPackage.getPrice(),
                spaPackage.getDiscountPercentage());

        existingOrder.setCustomerName(order.getCustomerName());
        existingOrder.setNumberOfPeople(order.getNumberOfPeople());
        existingOrder.setStandardPrice(order.getStandardPrice());
        existingOrder.setTelephoneNumber(order.getTelephoneNumber());
        existingOrder.setEmail(order.getEmail());
        existingOrder.setOrderDate(LocalDate.now());

        existingOrder.setPackageId(spaPackage.getId());
        existingOrder.setPackageName(spaPackage.getName());
        existingOrder.setPackageSnapShot(packageSnapShot);

        double newPrice = calculatePrice(
                packageSnapShot,
                existingOrder.getNumberOfPeople(),
                existingOrder.getStandardPrice());

        existingOrder.setTotalPrice(newPrice);
        mongoOperations.save(existingOrder);
        return existingOrder;
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
        if (order == null) {
            return null;
        }
        return order;
    }

    @Override
    public List<Order> getAllOrders() {
        return mongoOperations.findAll(Order.class);
    }

    @Override
    @SuppressWarnings("all")
    public List<Order> getLastMonthOrders() {
        try {
            LocalDate lastMonth = LocalDate.now().minusMonths(1);
            Query query = new Query();
            query.addCriteria(Criteria.where("orderDate").gte(lastMonth));
            return mongoOperations.find(query, Order.class);

        } catch (Exception e) {
            throw new RuntimeException("failed", e);
        }
    }

    @Override
    public List<Order> getLastWeekOrders() {
        try {
            LocalDate lastWeek = LocalDate.now().minusWeeks(1);
            Query query = new Query();
            query.addCriteria(Criteria.where("orderDate").gte(lastWeek));
            return mongoOperations.find(query, Order.class);
        } catch (Exception e) {
            throw new RuntimeException("failed", e);
        }
    }

    @Override
    public long isfullBooked() {
        LocalDate currentDay = LocalDate.now();
        LocalDateTime startOfTheDay = currentDay.atStartOfDay();
        LocalDateTime endOfTheDay = currentDay.plusDays(1).atStartOfDay();
        Query query = new Query();
        query.addCriteria(Criteria.where("orderDate").gte(startOfTheDay).lt(endOfTheDay));
        return mongoOperations.count(query, Order.class);
    }

    @Override
    public long lastMonthOverView() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfTheMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfTheMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        Query qr = new Query();
        qr.addCriteria(Criteria.where("orderDate").gte(firstDayOfTheMonth).lt(lastDayOfTheMonth));
        return mongoOperations.count(qr, Order.class);
    }

    @Override
    public long lastWeekOverView() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfTheWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate lastDayOfTheWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        Query qr = new Query();
        qr.addCriteria(Criteria.where("orderDate").gte(firstDayOfTheWeek).lt(lastDayOfTheWeek));
        return mongoOperations.count(qr, Order.class);
    }

    @Override
    public boolean isSpaHouseClosed() {
        LocalDate today = LocalDate.now();
        DayOfWeek todaysName = today.getDayOfWeek();
        return todaysName == DayOfWeek.MONDAY || todaysName == DayOfWeek.SATURDAY || todaysName == DayOfWeek.SUNDAY;
    }

    @Override
    public boolean isSpaPackageFullBooked(PackageSnapShot snapShot) {
        LocalDate currentDay = LocalDate.now();
        LocalDateTime startOfTheDay = currentDay.atStartOfDay();
        LocalDateTime endOfTheDay = currentDay.plusDays(1).atStartOfDay();

        Query qr = new Query();
        qr.addCriteria(Criteria.where("orderDate").gte(startOfTheDay).lt(endOfTheDay).and("packageName")
                .is(snapShot.getName()));
        long specialPackageBookings = mongoOperations.count(qr, Order.class);
        return specialPackageBookings >= 5;
    }
}
