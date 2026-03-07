package com.parkwise.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    private String location;
    private String slot;
    private String carNumber;
    private String phoneNumber;
    private LocalDateTime fromTime;
    private LocalDateTime toTime;
}