package com.example.agriTech.mapper.plant;

import org.springframework.stereotype.Component;

import com.example.agriTech.dto.plant.PlantDetailDTO;
import com.example.agriTech.model.Plant;

@Component
public class PlantDetailMapper {
    public PlantDetailDTO toDetailDTO(Plant plant) {
        if(plant == null) {
            return null;
        }
        PlantDetailDTO dto = new PlantDetailDTO();
        dto.setNamePlant(plant.getNamePlant());
        dto.setId(plant.getId());
        dto.setDescriptionPlant(plant.getDescriptionPlant());
        dto.setFertilizerInfo(plant.getFertilizerInfo());
        dto.setIdealHumidity(plant.getIdealHumidity());
        dto.setWaterFrequency(plant.getWaterFrequency());
        if(plant.getImages() != null) {
            String thumbai = plant.getImages().stream()
                            .filter(img->img.getIsThumbnail() != null && img.getIsThumbnail())
                            .map(img->img.getUrlImg())
                            .findFirst()
                            .orElse("default_thumbnail_url");
            dto.setThumbnailUrl(thumbai);                
        }
        return dto;
    }
    
}
