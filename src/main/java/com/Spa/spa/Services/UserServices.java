package com.Spa.spa.Services;


import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Spa.spa.models.User;

@Service
public class UserServices implements IUserServices {
    private final MongoOperations mongoOperations;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public UserServices(MongoOperations mongoOperations, JwtService jwtService, PasswordEncoder passwordEncoder ) {
        this.mongoOperations = mongoOperations;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(User user) {
        try {
            if (user == null) {
                throw new IllegalArgumentException("User cannot be null");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            mongoOperations.save(user);
            return new User(user.getId(), user.getUsername(), user.getPassword(), user.getRole());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    @Override
    public User findByUsername(String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        return mongoOperations.findOne(query, User.class);
    }

    @Override
    public String login(String username, String password) {
        User user = findByUsername(username);
        if(user == null) return "Invalid username or password";
        boolean passwordMatch = passwordEncoder.matches(password, user.getPassword());
        if(!passwordMatch) return "Invalid username or password";
        String token= jwtService.generateToken(username, user.getRole());
        return token;
    }


    @Override
    public String deleteUser(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        mongoOperations.remove(query, User.class);
        return "User deleted successfully";
    }

    @Override
    public User updateUser(String id, String username, String password, String role) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        User user = mongoOperations.findOne(query, User.class);
        if (user != null) {
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(role);
            mongoOperations.save(user);
            return user;
        } else {
            return null;
        }
    }

}
