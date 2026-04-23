package com.Spa.spa.controllers;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.Spa.spa.Services.OrderServices;
import com.Spa.spa.models.Order;

@RestController
public class OrderEndpoints {
    @Autowired
    OrderServices orderServices;
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/admin/manage-appointments")
    public ResponseEntity <Iterable<Order>> getAllOrder(){
        List<Order> orders = orderServices.getAllOrders();
        if(orders.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(orders);
    } 

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/admin/view-last-30-days-appointments")
    public ResponseEntity<Iterable<Order>> viewTheLastMonthAppointments(){
        List<Order> orders = orderServices.getLastMonthOrders();

        if(orders == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(orders);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/admin/view-last-7-days-appointments")
    public ResponseEntity<Iterable<Order>> viewTheLastWeekAppointments(){
        Iterable<Order> orders = orderServices.getLastWeekOrders();
        if(orders == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(orders);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/admin/spa/Opentimes")
    public ResponseEntity<String> getSpaClosedTimes(){
        boolean isClosed = orderServices.isSpaHouseClosed();
        if(isClosed){
            return ResponseEntity.ok("Spa house closed");
        }
        return ResponseEntity.ok("Spa house is Open");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/admin/spa/SpaStatus")
    public ResponseEntity<String> getSpaStatus(){
        long isSpaFullbooked = orderServices.isfullBooked();
        if(isSpaFullbooked == 20){
            return ResponseEntity.ok("The Spa house are fullbooked "+ isSpaFullbooked);
        }
        return ResponseEntity.ok("Status is good "+ isSpaFullbooked);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/admin/spa/monthly-overview")
    public ResponseEntity<String> getMonthlyOverView(){
        long monthlyOverview = orderServices.lastMonthOverView();
        if(monthlyOverview == 0){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("The Last month bookings Overview: " + monthlyOverview);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/admin/spa/weekly-overview")
    public ResponseEntity<String> getWeeklyOverView(){
        long weeklyOverView = orderServices.lastWeekOverView();
        if(weeklyOverView == 0){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok("The Last week bookings Overview : " + weeklyOverView);
    }

    @PostMapping("/api/customers/book-appointment/{packageId}")
    public ResponseEntity<Map<String, Object>> createAnOrder(@PathVariable String packageId, @RequestBody Order order){
        Order newOrder = orderServices.addOrder(packageId, order);
        if(newOrder == null){
            return ResponseEntity.badRequest().build();
        }
        Map <String, Object> response = Map.of(
            "id" , newOrder.getId(),
            "number of people", newOrder.getNumberOfPeople(),
            "Total Price" , newOrder.getTotalPrice(),
            "email" , newOrder.getEmail(),
            "Package name", newOrder.getPackageSnapShot().getName()
        );
        URI location = URI.create("/api/customers/book-appointment"+ newOrder.getPackageId() + newOrder.getCustomerName());
        return ResponseEntity.created(location).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/customers/book-appointment/{orderId}")
    public ResponseEntity<Map<String, Object>> updateAnOrder(@PathVariable String orderId, @RequestBody Order order){
        Order updateOrder = orderServices.updateOrder(orderId, order, order.getPackageName());
        if(updateOrder == null){
            return ResponseEntity.badRequest().build();
        }

        Map<String, Object> response = Map.of(
            "id" , updateOrder.getId(),
            "number of people", updateOrder.getNumberOfPeople(),
            "Total Price" , updateOrder.getTotalPrice(),
            "email" , updateOrder.getEmail(),
            "Package name", updateOrder.getPackageSnapShot().getName()
        );
        return ResponseEntity.accepted().body(response);
    }

    @DeleteMapping("/api/customers/book-appointment/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable String orderId){
        String  confirmation = orderServices.deleteOrder(orderId);
        if(confirmation.isBlank()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(confirmation);
    }
}
