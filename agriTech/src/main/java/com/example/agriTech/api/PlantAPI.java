package com.example.agriTech.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.agriTech.dto.plant.PlantDetailDTO;
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
    @GetMapping("/detail/{id}")
    public ResponseEntity<PlantDetailDTO> getPlantDetai(@PathVariable Long id) {
        try{
            PlantDetailDTO plant = this.plantService.getDetailPlant(id);
            return ResponseEntity.ok(plant);
        }catch(Exception e) {
            // Nếu lỗi (không tìm thấy ID), trả về 404 hoặc 400
        // Vì Kotlin không cho null, nên nếu lỗi ông phải báo lỗi HTTP Code rõ ràng
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
