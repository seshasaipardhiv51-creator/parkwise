package com.parkwise.demo.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.parkwise.demo.model.User;
import com.parkwise.demo.repository.UserRepository;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole("USER");
        }
        return userRepository.save(user);
    }
    public User createUser(String email, String password, String role) {
        User existing = userRepository.findByUsername(email);
        if (existing != null) {
            return existing; 
        }
        User user = new User();
        user.setUsername(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return userRepository.save(user);
    }
    public User login(String username, String password) {

        User dbUser = userRepository.findByUsername(username);

        if (dbUser != null && passwordEncoder.matches(password, dbUser.getPassword())) {
            return dbUser; 
        }
        return null;
    }
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
