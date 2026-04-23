package com.parkwise.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.parkwise.demo.model.HardwareLog;
import com.parkwise.demo.repository.HardwareLogRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/hardware")
@CrossOrigin(origins = "*")
public class HardwareController {

    @Autowired
    private HardwareLogRepository hardwareLogRepository;

    private Map<String, String> hardwareRegistry = new HashMap<>();

    @PostMapping("/register")
    public void registerHardware(@RequestBody Map<String, String> payload) {
        hardwareRegistry.put(payload.get("hardwareId"), payload.get("locationName"));
    }

    @PostMapping("/update")
    public void receiveSensorData(@RequestBody Map<String, String> payload) {
        String hardwareId = payload.get("hardwareId");
        String slot = payload.get("slot");
        String status = payload.get("status");
        String location = hardwareRegistry.getOrDefault(hardwareId, "Unregistered");

        if ("OCCUPIED".equals(status)) {
            HardwareLog log = new HardwareLog();
            log.setHardwareId(hardwareId);
            log.setLocationName(location);
            log.setSlot(slot);
            log.setStatus("OCCUPIED");
            log.setEntryTime(LocalDateTime.now());
            hardwareLogRepository.save(log);
        } else if ("EMPTY".equals(status)) {
            HardwareLog log = hardwareLogRepository.findFirstByHardwareIdAndSlotAndStatusOrderByIdDesc(hardwareId, slot, "OCCUPIED");
            if (log != null) {
                log.setStatus("COMPLETED");
                log.setExitTime(LocalDateTime.now());
                log.setDurationMinutes(ChronoUnit.MINUTES.between(log.getEntryTime(), log.getExitTime()));
                hardwareLogRepository.save(log);
            }
        }
    }

    @GetMapping("/live")
    public List<HardwareLog> getLiveStatus(@RequestParam String locationName) {
        return hardwareLogRepository.findByLocationNameAndStatus(locationName, "OCCUPIED");
    }

    @GetMapping("/logs")
    public List<HardwareLog> getAllLogs() {
        return hardwareLogRepository.findAllByOrderByIdDesc();
    }
}