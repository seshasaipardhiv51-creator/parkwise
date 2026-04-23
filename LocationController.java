package com.parkwise.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.parkwise.demo.model.Location;
import com.parkwise.demo.model.User;
import com.parkwise.demo.repository.LocationRepository;
import com.parkwise.demo.repository.UserRepository;
import com.parkwise.demo.service.EmailService;

import java.util.Map;

@RestController
@RequestMapping("/location")
@CrossOrigin(origins = "*")
public class LocationController {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    // Fetches slots for the Local Admin Maintenance view
    @GetMapping("/details")
    public Location getLocationDetails(@RequestParam String name) {
        // findFirstByName prevents 500 errors if duplicate locations exist
        return locationRepository.findFirstByName(name);
    }

    // Saves the "Yellow" slots you click in the dashboard
    @PostMapping("/update-maintenance")
    public Location updateMaintenance(@RequestBody Location updatedLoc) {
        Location existing = locationRepository.findFirstByName(updatedLoc.getName());
        if (existing != null) {
            existing.setBlockedSlots(updatedLoc.getBlockedSlots());
            return locationRepository.save(existing);
        }
        return null;
    }

    // Handles updating the name and emailing the new credentials
    @PostMapping("/rename")
    public void renameLocation(@RequestBody Map<String, String> payload) {
        String oldName = payload.get("oldName");
        String newName = payload.get("newName");
        String adminUser = payload.get("adminUser");
        String email = payload.get("email");
        String slots = payload.get("slots");

        // 1. Update Location in Database
        Location loc = locationRepository.findFirstByName(oldName);
        if (loc != null) {
            loc.setName(newName);
            locationRepository.save(loc);
        }

        // 2. Generate New Credentials
        String cleanNewName = newName.toLowerCase().replaceAll("\\s+", "");
        String newUsername = cleanNewName + "-" + slots;
        String newPassword = cleanNewName + "@" + slots;

        // 3. Update User Credentials in Database
        User user = userRepository.findByUsername(adminUser);
        if (user != null) {
            user.setUsername(newUsername);
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }

        // 4. Send the Email
        String msg = "Your parking location has been successfully renamed to: " + newName + 
                     "\n\nHere are your NEW login credentials:\nUsername: " + newUsername + 
                     "\nPassword: " + newPassword;
        try {
            emailService.sendEmail(email, "ParkWise - Location Renamed", msg);
        } catch (Exception e) {
            System.err.println("Failed to send rename email: " + e.getMessage());
        }
    }

    // Handles the Danger Zone closure request log
    @PostMapping("/request-closure")
    public void requestClosure(@RequestBody Map<String, String> payload) {
        System.out.println("Closure Request received for: " + payload.get("locationName"));
    }

    // Trashes the user and sends closure email
    @PostMapping("/approve-closure")
    public void approveClosureBackend(@RequestBody Map<String, String> payload) {
        String adminUser = payload.get("adminUser");
        String locName = payload.get("locationName");
        
        User user = userRepository.findByUsername(adminUser);
        if (user != null) {
            userRepository.delete(user);
        }
        
        try {
            String msg = "Termination Notice: The closure of " + locName + " has been approved. Your credentials have been permanently destroyed.";
            // If you have the specific email, pass it here. Using a placeholder for now.
            emailService.sendEmail("partner@example.com", "ParkWise - Account Deactivated", msg);
        } catch (Exception e) {
            System.err.println("Email failed: " + e.getMessage());
        }
    }
}