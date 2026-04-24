package com.example.agritechda3k.mapper.plantuser

import com.example.agritechda3k.api.dto.PlantUserDTO
import com.example.agritechda3k.model.PlantUser

fun PlantUserDTO.toEntitys(): PlantUser{
    return PlantUser(
        id = this.id,
        customName = this.customName,
        // Ép LocalDateTime từ Spring (String) về String của Room
        lastWatered = this.lastWatered,
        status = this.status ?: true,
        plantId = this.plantId,
        userId = this.userId,
        // Đống dữ liệu "ké" từ Plant và Account
        plantName = this.plantName,
        imageUrl = this.imageUrl,
        fertilizerInfo = this.fertilizerInfo,
        fullName = this.fullName,
        deviceCode = this.deviceCode
    )
}

// Chuyển cả danh sách (Cái này chính là cái ông gọi trong Repository)
fun List<PlantUserDTO>.toEntityList(): List<PlantUser> {
    return this.map { it.toEntitys() }
}