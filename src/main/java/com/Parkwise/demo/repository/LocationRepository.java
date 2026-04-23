package com.parkwise.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.parkwise.demo.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Location findFirstByName(String name);
}