package com.Spa.spa.Services;

import com.Spa.spa.models.User;

public interface IUserServices {
    public User createUser(User user);
    public User findByUsername(String username);
    public String login(String username, String password);
    public String deleteUser(String id);
    public User updateUser(String id, String username, String password, String role);
}
