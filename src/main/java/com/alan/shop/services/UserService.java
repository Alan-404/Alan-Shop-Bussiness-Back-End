package com.alan.shop.services;


import java.util.List;

import com.alan.shop.models.User;

public interface UserService {
    public User addUser(User user);
    public User getUserByEmail(String email);
    public User getUserById(String id);
    public User editUser(User user);
    public List<User> getUsers();
}
