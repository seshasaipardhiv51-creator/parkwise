package com.parkwise.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.parkwise.demo.model.HardwareLog;
import java.util.List;

public interface HardwareLogRepository extends JpaRepository<HardwareLog, Long> {
    HardwareLog findFirstByHardwareIdAndSlotAndStatusOrderByIdDesc(String hardwareId, String slot, String status);
    List<HardwareLog> findByLocationNameAndStatus(String locationName, String status);
    List<HardwareLog> findAllByOrderByIdDesc();
}