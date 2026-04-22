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
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;

import com.Spa.spa.Services.JwtService;
import com.Spa.spa.Services.UserServices;
import com.Spa.spa.models.User;

@SpringBootTest
@ActiveProfiles("test")
public class UserServicesTest {
    MongoOperations mongoOperations;
    UserServices userServices;
    JwtService jwtService;

    @BeforeEach
    public void setUp(){
        mongoOperations = mock(MongoOperations.class);
        jwtService = mock(JwtService.class);
        userServices = new UserServices(mongoOperations, jwtService);
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
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(userName));   
        when(mongoOperations.findOne(query, User.class)).thenReturn(userInput);
        User userSavedResult = userServices.createUser(userInput);
        System.out.println(" ******* the User name from userSavedResult is: ***********  " + userSavedResult.getUsername());
        User userResult = userServices.findByUsername(userName);
        assertNotNull(userResult);
        assert userResult.getUsername().equals(userName);
    }

    @Test
    public void testLogIn_Should_Return_True(){
        String userId = "1212345";
        String userName = "testuser";
        String password = "testpassword";
        String role = "user";
        User userInput= new User(userId , userName, password, role);
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(userName).and("password").is(password));
        when(mongoOperations.findOne(query, User.class)).thenReturn(userInput);
        String loginResult = userServices.login(userName, password);
        System.out.println(" ******* the login result is: ***********  " + loginResult);
        assert loginResult.equals("Login successful");
        assertNotNull(loginResult);
    }

    @Test
    public void testUpdateUser_Should_Return_True(){
        String userId = "1212345";
        String userName = "testuser";
        String password = "testpassword";
        String role = "user";
        User userInput= new User(userId , userName, password, role);
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(userId));
        when(mongoOperations.findOne(query, User.class)).thenReturn(userInput);
        User updatedUserResult = userServices.updateUser(userId, "updatedUser", "updatedPassword", "admin");
        System.out.println(" ******* the updated user name is: ***********  " + updatedUserResult.getUsername());
        assertNotNull(updatedUserResult);
        assert updatedUserResult.getUsername().equals("updatedUser");
        assert updatedUserResult.getPassword().equals("updatedPassword");
        assert updatedUserResult.getRole().equals("admin");
    }

    @Test
    public void testDeleteUser_Should_Return_True(){
        String userId = "1212345";
        String userName = "testuser";
        String password = "testpassword";
        String role = "user";
        User userInput= new User(userId , userName, password, role);
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(userId));
        when(mongoOperations.findOne(query, User.class)).thenReturn(userInput);
        String deleteResult = userServices.deleteUser(userId);
        System.out.println(" ******* the delete result is: ***********  " + deleteResult);
        assert deleteResult.equals("User deleted successfully");
    }

}
