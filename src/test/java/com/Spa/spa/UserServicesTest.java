package com.Spa.spa;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        mongoOperations = mock(MongoOperations.class);
        jwtService = mock(JwtService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userServices = new UserServices(mongoOperations, jwtService, passwordEncoder);
    }

    @Test
    public void testCreateUser() {
        String username = "testuser";
        String password = "testpassword";
        String role = "user";
        String hashedPassword = "$2a$10$fakeHashValueForTest";

        User userInput = new User(null, username, password, role);
        User saved = new User(new ObjectId().toString(), username, password, role);
        when(passwordEncoder.encode(password)).thenReturn(hashedPassword);
        when(mongoOperations.save(any(User.class))).thenReturn(saved);
        when(mongoOperations.findById(saved.getId(), User.class)).thenReturn(saved);
        User userResult = userServices.createUser(userInput);

        assertNotNull(userResult);
        assert userResult.getUsername().equals(username);
        assert userResult.getPassword().equals(hashedPassword);
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
    public void testFindUserByName_Should_Return_True() {
        String userId = "1212345";
        String userName = "testuser";
        String password = "testpassword";
        String role = "user";
        User userInput = new User(userId, userName, password, role);
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(userName));
        when(mongoOperations.findOne(query, User.class)).thenReturn(userInput);
        User userSavedResult = userServices.createUser(userInput);
        System.out.println(
                " ******* the User name from userSavedResult is: ***********  " + userSavedResult.getUsername());
        User userResult = userServices.findByUsername(userName);
        assertNotNull(userResult);
        assert userResult.getUsername().equals(userName);
    }

    @Test
    public void testLogIn_Should_Return_True() {
        String userId = "1212345";
        String userName = "testuser";
        String rawPassword = "testpassword";
        String hashedPassword = "$2a$10$fakeHashValueForTest";
        String role = "ADMIN";
        String expectedToken = "test-jwt-token";

        User userFromDb = new User(userId, userName, hashedPassword, role);

        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(userName));
        when(mongoOperations.findOne(query, User.class)).thenReturn(userFromDb);

        when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(true);
        when(jwtService.generateToken(userName, role)).thenReturn(expectedToken);

        String loginResult = userServices.login(userName, rawPassword);

        assertEquals(expectedToken, loginResult);
    }

    @Test
    public void testUpdateUser_Should_Return_True() {
        String userId = "1212345";
        String oldUserName = "testuser";
        String oldPassword = "testpassword";
        String oldRole = "user";

        String newUserName = "updatedUser";
        String newRawPassword = "updatedPassword";
        String newRole = "admin";
        String expectedHashedPassword = "$2a$10$fakeHashValueForTest";

        User existingUser = new User(userId, oldUserName, oldPassword, oldRole);

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(userId));

        when(mongoOperations.findOne(query, User.class)).thenReturn(existingUser);
        when(passwordEncoder.encode(newRawPassword)).thenReturn(expectedHashedPassword);

        User updatedUserResult = userServices.updateUser(userId, newUserName, newRawPassword, newRole);

        assertNotNull(updatedUserResult);
        assertEquals(newUserName, updatedUserResult.getUsername());
        assertEquals(expectedHashedPassword, updatedUserResult.getPassword());
        assertEquals(newRole, updatedUserResult.getRole());
    }

    @Test
    public void testDeleteUser_Should_Return_True() {
        String userId = "1212345";
        String userName = "testuser";
        String password = "testpassword";
        String role = "user";
        User userInput = new User(userId, userName, password, role);
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(userId));
        when(mongoOperations.findOne(query, User.class)).thenReturn(userInput);
        String deleteResult = userServices.deleteUser(userId);
        System.out.println(" ******* the delete result is: ***********  " + deleteResult);
        assert deleteResult.equals("User deleted successfully");
    }

}
