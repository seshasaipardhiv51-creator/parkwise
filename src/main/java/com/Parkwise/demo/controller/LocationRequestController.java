package com.parkwise.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parkwise.demo.model.LocationRequest;
import com.parkwise.demo.service.LocationRequestService;

@RestController
@RequestMapping("/location")
@CrossOrigin(origins = "*") 
public class LocationRequestController {

    @Autowired
    private LocationRequestService service;

    // Fetches pending requests for the table
    @GetMapping("/all")
    public List<LocationRequest> getAllRequests() {
        return service.getAll().stream()
                .filter(req -> "PENDING".equals(req.getStatus()))
                .collect(Collectors.toList());
    }

    @PostMapping("/request")
    public LocationRequest submitRequest(@RequestBody LocationRequest request) {
        return service.submit(request);
    }

    @PostMapping("/approve/{id}")
    public LocationRequest approveRequest(@PathVariable Long id) {
        return service.approve(id);
    }

    @PostMapping("/reject/{id}")
    public LocationRequest rejectRequest(@PathVariable Long id) {
        return service.reject(id);
    }
}