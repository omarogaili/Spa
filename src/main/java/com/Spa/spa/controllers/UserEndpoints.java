package com.Spa.spa.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Spa.spa.Services.OrderServices;
import com.Spa.spa.Services.UserServices;
import com.Spa.spa.models.Order;

@RestController
public class UserEndpoints {
    @Autowired
    MongoOperations mongoOperations;
    @Autowired
    UserServices userServices;

    @Autowired
    OrderServices orderServices;

    @GetMapping("/api/admin/manage-appointments")
    public ResponseEntity<Iterable<Order>> getAllAppontments() {
        List<Order> orders = mongoOperations.findAll(Order.class);
        if(orders.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orders);

    }

}
