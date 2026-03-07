package com.parkwise.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parkwise.model.Booking;
import com.parkwise.service.BookingService;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service){
        this.service = service;
    }

    @PostMapping
    public Booking create(@RequestBody Booking booking){
        return service.saveBooking(booking);
    }

    @GetMapping
    public List<Booking> getAll(){
        return service.getAll();
    }
}