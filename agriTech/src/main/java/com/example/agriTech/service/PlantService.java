package com.example.agriTech.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.agriTech.dto.plant.PlantListDTO;
import com.example.agriTech.mapper.plant.PlantListMapper;

import com.example.agriTech.repository.PlantRepository;

@Service
public class PlantService {
    private final PlantRepository plantRepository;
    private final PlantListMapper plantListMapper;
    public PlantService(PlantRepository plantRepository,PlantListMapper plantListMapper){
        this.plantRepository = plantRepository;
        this.plantListMapper = plantListMapper;
    }

    public List<PlantListDTO> getAllPlant() {
        List<PlantListDTO> plantList = this.plantRepository.findAll()
                                        .stream()
                                        .map(plant -> this.plantListMapper.toHomeDTO(plant)) //map(this.plantListMapper::toHomeDTO)
                                        .collect(Collectors.toList());
        return plantList;
    }

    
}
