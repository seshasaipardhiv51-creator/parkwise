package com.parkwise.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    private String name;
    private int totalSlots;

    @Column(columnDefinition = "TEXT")
    private String blockedSlots;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getTotalSlots() { return totalSlots; }
    public void setTotalSlots(int totalSlots) { this.totalSlots = totalSlots; }
    public String getBlockedSlots() { return blockedSlots; }
    public void setBlockedSlots(String blockedSlots) { this.blockedSlots = blockedSlots; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
}
