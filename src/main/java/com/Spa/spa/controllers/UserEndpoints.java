package com.Spa.spa.controllers;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.Spa.spa.Services.OrderServices;
import com.Spa.spa.Services.UserServices;
import com.Spa.spa.models.Order;
import com.Spa.spa.models.User;

@RestController
public class UserEndpoints {
    @Autowired
    MongoOperations mongoOperations;
    @Autowired
    UserServices userServices;

    @Autowired
    OrderServices orderServices;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/admin/manage-appointments")
    public ResponseEntity<Iterable<Order>> getAllAppontments() {
        List<Order> orders = mongoOperations.findAll(Order.class);
        if(orders.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/api/signIn")
    public ResponseEntity<String> login(@RequestBody User request ){
        String token = userServices.login(request.getUsername() , request.getPassword());
        if("Invalid username or password".equals(token)){
            return ResponseEntity.status(401).body("Invalid username or password");
        }
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
            .httpOnly(true)
            .secure(false)
            .maxAge(Duration.ofHours(1))
            .sameSite("Lax")
            .build();
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body("Login successful");
    }

}
