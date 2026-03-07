package com.parkwise.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.parkwise.model.Booking;
import com.parkwise.repository.BookingRepository;

@Service
public class BookingService {

    private final BookingRepository repo;

    public BookingService(BookingRepository repo){
        this.repo = repo;
    }

    public Booking saveBooking(Booking booking){
        return repo.save(booking);
    }

    public List<Booking> getAll(){
        return repo.findAll();
    }

    public void removeExpired(){
        List<Booking> expired = repo.findByToTimeBefore(LocalDateTime.now());
        repo.deleteAll(expired);
    }
}