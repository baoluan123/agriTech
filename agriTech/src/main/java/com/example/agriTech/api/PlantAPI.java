package com.example.agriTech.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.agriTech.dto.plant.PlantListDTO;
import com.example.agriTech.service.PlantService;

@RestController
@RequestMapping("/api/plants")
public class PlantAPI {
    private final PlantService plantService;
    public PlantAPI(PlantService plantService){
        this.plantService = plantService;
    }
    @GetMapping
    public List<PlantListDTO> getPlantList() {
        return this.plantService.getAllPlant();
    }
}
