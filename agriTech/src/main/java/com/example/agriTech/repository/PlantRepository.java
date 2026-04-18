package com.example.agriTech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.agriTech.model.Plant;

@Repository
public interface  PlantRepository extends JpaRepository<Plant,Long> {
    
}
