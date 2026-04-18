package com.example.agritechda3k.mapper.plant

import com.example.agritechda3k.api.dto.PlantListDTO
import com.example.agritechda3k.model.Plant

fun PlantListDTO.toEntity(): Plant {
    return Plant(
        id = this.id,
        namePlant = this.namePlant,
        thumbnailUrl = this.thumbnailUrl,
        descriptionPlant = this.descriptionPlant
        // Thêm các trường khác nếu Model Plant của ông có yêu cầu
    )
}

fun List<PlantListDTO>.toEntityList(): List<Plant> {
    return this.map { it.toEntity() }
}