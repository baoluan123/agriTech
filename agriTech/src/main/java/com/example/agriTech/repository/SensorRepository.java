package com.example.agriTech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.agriTech.model.Sensor;
@Repository
public interface SensorRepository extends JpaRepository<Sensor,Long> {
    
}
