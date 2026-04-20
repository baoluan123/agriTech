package com.example.agriTech.dto.plant;

import lombok.Data;

@Data
public class PlantDetailDTO {
    private Long id;
    private String namePlant;
    private Float idealHumidity;
    private Integer waterFrequency;
    private String fertilizerInfo;
    private String descriptionPlant;
    private String thumbnailUrl; // Vẫn trả về 1 link ảnh duy nhất theo ý bạn

}
