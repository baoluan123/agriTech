package com.example.agriTech.mapper.plant;

import org.springframework.stereotype.Component;

import com.example.agriTech.dto.plant.PlantListDTO;
import com.example.agriTech.model.Plant;

@Component
public class PlantListMapper {

    public PlantListDTO toHomeDTO(Plant plant) {
        if(plant == null) {
            return null;
        }
        PlantListDTO dto = new PlantListDTO();
        dto.setId(plant.getId());
        dto.setNamePlant(plant.getNamePlant());
        dto.setDescriptionPlant(plant.getDescriptionPlant());
        // Xử lý logic lọc ảnh: Lấy ảnh đầu tiên có status = true
        if(plant.getImages() != null) {
          String thumbnail = plant.getImages().stream()
                    .filter(img -> img.getIsThumbnail() != null && img.getIsThumbnail()) // Lọc ảnh có status true
                    .map(img -> img.getUrlImg()) // Lấy đường dẫn ảnh
                    .findFirst() // Lấy thằng đầu tiên thỏa mãn
                    .orElse("default_thumbnail_url"); // Nếu không có ảnh nào true thì lấy ảnh mặc định

                    dto.setThumbnailUrl(thumbnail);
        }
       
        return dto;
    }
    
}

 // Xử lý khóa ngoại: Chỉ lấy ID của User, không lấy nguyên Object User
        // Đây chính là chỗ giúp ông không bao giờ bị dính lỗi vòng lặp logic nữa!
        // if (plant.getUser() != null) {
        //     dto.setOwnerId(plant.getUser().getId());
        // }
