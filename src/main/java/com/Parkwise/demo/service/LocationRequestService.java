package com.parkwise.demo.service;
import com.parkwise.demo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parkwise.demo.model.*;
import com.parkwise.demo.repository.*;

import java.util.List;

@Service
public class LocationRequestService {

    @Autowired private LocationRequestRepository repo;
    @Autowired private LocationRepository locationRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private EmailService emailService;
    @Autowired private PasswordEncoder passwordEncoder;

    public List<LocationRequest> getAll() { return repo.findAll(); }

    public LocationRequest submit(LocationRequest req) {
        req.setStatus("PENDING");
        return repo.save(req);
    }

    @Transactional
    public LocationRequest approve(Long id) {
        LocationRequest req = repo.findById(id).orElse(null);

        if (req != null) {
            req.setStatus("APPROVED");
            repo.save(req);

            String cleanName = req.getLocationName().toLowerCase().replaceAll("\\s+", "");
            String generatedUser = cleanName + "-" + req.getSlots();
            String firstName = req.getName().split("\\s+")[0].toLowerCase();
            String generatedPass = firstName + "@" + req.getSlots();

            if (userRepository.findByUsername(generatedUser) == null) {
                User user = new User();
                user.setUsername(generatedUser);
                user.setPassword(passwordEncoder.encode(generatedPass));
                user.setRole("LOCAL_ADMIN");
                userRepository.save(user);
            }

            Location newLoc = new Location();
            newLoc.setCity(req.getCity());
            newLoc.setName(req.getLocationName());
            newLoc.setTotalSlots(req.getSlots());
            newLoc.setBlockedSlots(""); 
            locationRepository.save(newLoc);

            String msg = "Credentials for " + req.getLocationName() + ":\nUser: " + generatedUser + "\nPass: " + generatedPass;
            try {
                emailService.sendEmail(req.getEmail(), "ParkWise Approved Don't share this credentials anywhere", msg);
            } catch (Exception e) {
                System.err.println("Email failed: " + e.getMessage());
            }
        }
        return req;
    }

    public LocationRequest reject(Long id) {
        LocationRequest req = repo.findById(id).orElse(null);
        if (req != null) {
            req.setStatus("REJECTED");
            repo.save(req);
            try {
                emailService.sendEmail(req.getEmail(), "ParkWise Rejected", "❌ Your request for " + req.getLocationName() + " was rejected.");
            } catch (Exception e) {
                System.err.println("Email failed: " + e.getMessage());
            }
        }
        return req;
    }

    @Transactional
    public void approveClosure(Long requestId) {
        LocationRequest req = repo.findById(requestId).orElse(null);
        if (req != null) {
            String cleanName = req.getLocationName().toLowerCase().replaceAll("\\s+", "");
            String generatedUser = cleanName + "-" + req.getSlots();

            User user = userRepository.findByUsername(generatedUser);
            if (user != null) {
                userRepository.delete(user);
            }

            req.setStatus("CLOSED");
            repo.save(req);

            String message = "⚠️ Notice: The closure of " + req.getLocationName() + " is approved. Your account has been deleted.";
            emailService.sendEmail(req.getEmail(), "ParkWise Account Deactivated", message);
        }
    }
}