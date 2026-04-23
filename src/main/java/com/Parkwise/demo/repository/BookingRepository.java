package com.parkwise.demo.repository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.parkwise.demo.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    List<Booking> findByToTimeBefore(LocalDateTime time);
}
