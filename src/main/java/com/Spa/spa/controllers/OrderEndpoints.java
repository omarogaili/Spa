package com.Spa.spa.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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
        Iterable<Order> orders = orderServices.getAllOrders();
        if(orders == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(orders);
    } 

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/admin/view-last-30-days-appointments")
    public ResponseEntity<Iterable<Order>> viewTheLastMonthAppointments(){
        Iterable<Order> orders = orderServices.getLastMonthOrders();

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

    


}
