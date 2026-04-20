package com.example.agriTech.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.agriTech.dto.plant.PlantDetailDTO;
import com.example.agriTech.dto.plant.PlantListDTO;
import com.example.agriTech.mapper.plant.PlantDetailMapper;
import com.example.agriTech.mapper.plant.PlantListMapper;
import com.example.agriTech.model.Plant;
import com.example.agriTech.repository.PlantRepository;

@Service
public class PlantService {
    private final PlantRepository plantRepository;
    private final PlantListMapper plantListMapper;
    private final PlantDetailMapper plantDetailMapper;
    public PlantService(PlantRepository plantRepository,PlantListMapper plantListMapper,PlantDetailMapper plantDetailMapper){
        this.plantRepository = plantRepository;
        this.plantListMapper = plantListMapper;
        this.plantDetailMapper = plantDetailMapper;
    }

    public List<PlantListDTO> getAllPlant() {
        List<PlantListDTO> plantList = this.plantRepository.findAll()
                                        .stream()
                                        .map(plant -> this.plantListMapper.toHomeDTO(plant)) //map(this.plantListMapper::toHomeDTO)
                                        .collect(Collectors.toList());
        return plantList;
    }


    public PlantDetailDTO getDetailPlant(Long id){
        Plant plant = this.plantRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy cây với ID: " + id));
        PlantDetailDTO plantDTO = this.plantDetailMapper.toDetailDTO(plant);
        return plantDTO;
    }

    
}
