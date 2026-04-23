package com.parkwise.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.parkwise.demo.model.Booking;
import com.parkwise.demo.service.BookingService;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/bookings")

public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public Booking book(@RequestBody Booking booking) {
        return bookingService.saveBooking(booking);
    }

    @GetMapping
    public List<Booking> getAll() {
        return bookingService.getAllBookings();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookingService.deleteBooking(id);
    }
}
