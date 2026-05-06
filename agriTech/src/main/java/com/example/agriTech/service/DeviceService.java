package com.example.agriTech.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.agriTech.model.Device;
import com.example.agriTech.repository.DeviceRepository;

@Service
public class DeviceService {
    public final DeviceRepository deviceRepository;
    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public List<Device> getAllDevice() { 
        List<Device> devices = this.deviceRepository.findAll();
        return devices;
    }
}
