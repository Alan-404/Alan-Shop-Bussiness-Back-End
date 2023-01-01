package com.alan.shop.services.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.alan.shop.models.User;
import com.alan.shop.repositories.UserRepository;
import com.alan.shop.services.UserService;
import com.alan.shop.utils.Constants;
import com.alan.shop.utils.Generator;
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Generator generator;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
        this.generator = new Generator();
    }
    @Override
    public User addUser(User user){
        user.setId(generator.generateId(Constants.lengthId));
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        user.setModifiedAt(new Timestamp(System.currentTimeMillis()));
        User newUser = this.userRepository.save(user);
        return newUser;
    }

    @Override
    public User getUserByEmail(String email){
        Optional<User> user = this.userRepository.getUserByEmail(email);
        if (user.isPresent() == false){
            return null;
        }
        return user.get();
    }

    @Override
    public User getUserById(String id){
        Optional<User> user = this.userRepository.findById(id);
        if (user.isPresent() == false){
            return null;
        }
        return user.get();
    }

    @Override
    public User editUser(User user){
        try{
            user.setModifiedAt(new Timestamp(System.currentTimeMillis()));
            return this.userRepository.save(user);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }

    }

    @Override
    public List<User> getUsers(){
        try{
            return this.userRepository.findAll();
        }
        catch(Exception exception){
            return null;
        }
    }
}
