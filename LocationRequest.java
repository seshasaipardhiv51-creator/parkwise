package com.parkwise.demo.model;

import jakarta.persistence.*;

@Entity
public class LocationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone;
    private String email;
    private String city;
    private String locationName;
    private int slots;

    private String status; // PENDING / APPROVED / REJECTED

    // ✅ GETTERS & SETTERS

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }

    public int getSlots() { return slots; }
    public void setSlots(int slots) { this.slots = slots; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}