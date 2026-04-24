package com.example.agriTech.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.agriTech.model.Sensor;

public interface SensorRepository extends JpaRepository<Sensor,Long> {
    
}
