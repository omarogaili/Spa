package com.Spa.spa;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;

import com.Spa.spa.Services.UserServices;
import com.Spa.spa.models.User;

@SpringBootTest
public class UserServicesTest {
    MongoOperations mongoOperations;
    UserServices userServices = new UserServices(mongoOperations);
    @Test
    public void testCreateUser() {
        String username = "testuser";
        String password = "testpassword";
        String role = "user";

        User user = new User(null, username, password, role);
        user = userServices.createUser(user);

        assert user != null;
        assert mongoOperations.findById(user.getId(), User.class) != null;
        assert user.getUsername().equals(username);
        assert user.getPassword().equals(password);
        assert user.getRole().equals(role);
    }

    @Test
    public void testCreateUserNull() {
        try {
            userServices.createUser(null);
            assert false; // Should not reach this line
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("User cannot be null");
        }
    }

}
