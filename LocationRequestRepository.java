package com.parkwise.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.parkwise.demo.model.LocationRequest;

public interface LocationRequestRepository extends JpaRepository<LocationRequest, Long> {
}