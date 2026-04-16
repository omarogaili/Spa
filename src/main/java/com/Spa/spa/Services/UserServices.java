package com.Spa.spa.Services;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.Spa.spa.models.User;

@Service
public class UserServices implements IUserServices {
    private final MongoOperations mongoOperations;

    public UserServices(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public User createUser(User user) {
        try {
            if (user == null) {
                throw new IllegalArgumentException("User cannot be null");
            }
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

        return null;
    }

    @Override
    public User register(String username, String password, String role) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String deleteUser(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public User updateUser(String id, String username, String password, String role) {
        // TODO Auto-generated method stub
        return null;
    }

}
