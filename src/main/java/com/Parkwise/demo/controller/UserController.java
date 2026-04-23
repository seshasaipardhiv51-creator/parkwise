package com.parkwise.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import com.parkwise.demo.model.User;
import com.parkwise.demo.service.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    // ✅ Register manually
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }

    // ✅ Login
    @PostMapping("/login")
    public String login(@RequestBody User user) {

        User validUser = userService.login(user.getUsername(), user.getPassword());

        if (validUser != null) {
            return "SUCCESS";
        }

        return "FAIL";
    }
    @GetMapping("/me")
    public Map<String, Object> getCurrentUser(@AuthenticationPrincipal OAuth2User principal) {
    if (principal == null) return null;
    
    // Spring Security populates this from the Google response
    Map<String, Object> details = new HashMap<>();
    details.put("username", principal.getAttribute("email"));
    details.put("name", principal.getAttribute("name"));
    return details;
}
}