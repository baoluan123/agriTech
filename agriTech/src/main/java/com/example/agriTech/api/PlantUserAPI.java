package com.example.agriTech.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.agriTech.dto.plantuser.PlantUserDTO;
import com.example.agriTech.service.PlantUserService;

@RestController
@RequestMapping("/api/myplant")
public class PlantUserAPI {
    private final PlantUserService plantUserService;

    public PlantUserAPI(PlantUserService plantUserService) {
        this.plantUserService = plantUserService;
    }
    @GetMapping("/detail/{id}")
    public ResponseEntity<PlantUserDTO> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(plantUserService.getMyPlantDetail(id));
    }
    @GetMapping("/{userId}")
public ResponseEntity<List<PlantUserDTO>> getMyPlants(@PathVariable Long userId) {
    return ResponseEntity.ok(plantUserService.getMyPlantList(userId));
}

}
