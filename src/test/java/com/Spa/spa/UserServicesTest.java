package com.Spa.spa;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;

import com.Spa.spa.Services.UserServices;
import com.Spa.spa.models.User;

@SpringBootTest
public class UserServicesTest {
    MongoOperations mongoOperations;
    UserServices userServices;

    @BeforeEach
    public void setUp(){
        mongoOperations = mock(MongoOperations.class);
        userServices = new UserServices(mongoOperations);
    }
    @Test
    public void testCreateUser() {
        String username = "testuser";
        String password = "testpassword";
        String role = "user";

        User userInput = new User(null, username, password, role);
        User saved = new User(new ObjectId().toString(), username, password, role);
        when(mongoOperations.save(any(User.class))).thenReturn(saved);
        when(mongoOperations.findById(saved.getId(), User.class)).thenReturn(saved);
        User userResult = userServices.createUser(userInput);

        assertNotNull(userResult);
        assert userResult.getUsername().equals(username);
        assert userResult.getPassword().equals(password);
        assert userResult.getRole().equals(role);
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

    @Test
    public void testFindUserByName_Should_Return_True(){
        String userId = "1212345";
        String userName = "testuser";
        String password = "testpassword";
        String role = "user";
        User userInput= new User(userId , userName, password, role);
        User savedUser = new User(userId , userName, password, role);
        when(mongoOperations.save(any(User.class))).thenReturn(savedUser);
        when(mongoOperations.findById(savedUser.getId(), User.class)).thenReturn(savedUser);
        User userSavedResult = userServices.createUser(userInput);
        User userResult = userServices.findByUsername(userSavedResult.getUsername());
        System.out.println("the User name is:" + userResult.getUsername());
        assertNotNull(userResult);
        assert userResult.getUsername().equals(userName);
    }

}
