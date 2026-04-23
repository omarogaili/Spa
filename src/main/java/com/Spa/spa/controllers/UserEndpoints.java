package com.Spa.spa.controllers;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.Spa.spa.Services.OrderServices;
import com.Spa.spa.Services.UserServices;
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
    @GetMapping("/api/admin/manage-employees/allusers")
    public ResponseEntity<Iterable<User>> getAllAppontments() {
        List<User> users = userServices.getAllUsers();
        if(users.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users);
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/admin/manage-employees/{username}")
    public ResponseEntity<Map<String, String>> getUserByUserName(@PathVariable String username){
        User result = userServices.findByUsername(username);
        if(result == null){
            return ResponseEntity.notFound()
            .build();
        }

        Map<String, String> response = Map.of(
            "id" , result.getId(),
            "userName" , result.getUsername(),
            "role" , result.getRole()
        );
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("api/add/admin/manage-employees")
    public ResponseEntity<Map<String, String>> addNewUser (@RequestBody User user){
        User response = userServices.createUser(user);
        if(response == null){
            return ResponseEntity.badRequest().build();
        }
        Map <String, String > result = Map.of(
            "id" , response.getId(),
            "username" , response.getUsername(),
            "role" , response.getRole()
        );
        URI location = URI.create("/api/admin/manage-employees/" + response.getUsername());
        return ResponseEntity.created(location).body(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/admin/users/{username}")
    public ResponseEntity<Map<String, String>> updateUser(@PathVariable String username ,@RequestBody User user){
        User updatedUser = userServices.updateUser(username, user.getPassword(), user.getRole());
        if(updatedUser == null) return ResponseEntity.notFound().build();
        Map<String, String> response = Map.of(
            "id", updatedUser.getId(),
            "username", updatedUser.getUsername(),
            "role", updatedUser.getRole()
        );
        return ResponseEntity.status(201).body(response);
    }

    
}
