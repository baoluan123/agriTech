package com.example.agriTech.dto.plantuser;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
@Data
public class PlantUserDTO {
    private Long id; // ID của plants_users
    private String customName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastWatered;
    private Boolean status;
    // Thuộc tính từ bảng Plant
    private Long plantId;
    private String plantName;
    private String imageUrl;
    private String fertilizerInfo;

    // Thuộc tính từ bảng Account/User
    private String FullName;
    private Long userId;
    private String deviceCode;

    
}
