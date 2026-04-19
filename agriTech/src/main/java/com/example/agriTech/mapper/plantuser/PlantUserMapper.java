package com.example.agriTech.mapper.plantuser;

import org.springframework.stereotype.Component;

import com.example.agriTech.dto.plantuser.PlantUserDTO;
import com.example.agriTech.model.PlantUser;

@Component
public class PlantUserMapper {
    public PlantUserDTO toDTO(PlantUser plantUser) {
        if(plantUser == null) {
            return null;
        }
        PlantUserDTO dto = new PlantUserDTO();
        dto.setId(plantUser.getId());
        dto.setCustomName(plantUser.getCustomName());
        dto.setLastWatered(plantUser.getLastWatered());
        dto.setStatus(plantUser.getStatus());

        //
        if(plantUser.getPlant() != null) {
            dto.setPlantId(plantUser.getPlant().getId());
            dto.setPlantName(plantUser.getPlant().getNamePlant());
            dto.setFertilizerInfo(plantUser.getPlant().getFertilizerInfo());
            
            if(plantUser.getPlant().getImages() != null) {
                String thum = plantUser.getPlant().getImages().stream()
                            .filter(img->Boolean.TRUE.equals(img.getIsThumbnail()))
                            .map(img->img.getUrlImg())
                            .findFirst()
                            .orElse(null);
                dto.setImageUrl(thum);
            }
        }

        //
        if(plantUser.getUser() != null) {
            dto.setUserId(plantUser.getUser().getId());
            if(plantUser.getUser().getAccount() != null) {
                dto.setFullName(plantUser.getUser().getAccount().getFullName());
            }
        }

        return dto;
    }
    
}
