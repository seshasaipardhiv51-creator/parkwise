package com.parkwise.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.parkwise.demo.model.User;
import com.parkwise.demo.repository.UserRepository;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

@PostMapping("/login")
public User login(@RequestBody User user) {

    User dbUser = userRepository.findByUsername(user.getUsername());

    if (dbUser != null && passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
        return dbUser; // ✅ RETURN FULL USER (with role)
    }

    return null;
}
    
}