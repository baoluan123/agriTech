package com.example.agriTech.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.example.agriTech.model.Device;

public interface DeviceRepository extends JpaRepository<Device,Long> {
    Device findByDeviceCode(String deviceCode);
}
