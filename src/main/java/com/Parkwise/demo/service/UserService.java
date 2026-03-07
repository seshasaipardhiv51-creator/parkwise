package com.parkwise.service;

import org.springframework.stereotype.Service;

import com.parkwise.model.User;
import com.parkwise.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo){
        this.repo = repo;
    }

    public User findByUsername(String username){
        return repo.findByUsername(username).orElse(null);
    }
}