package com.example.agritechda3k.mapper.plant

import com.example.agritechda3k.api.dto.PlantDetailsDTO
import com.example.agritechda3k.model.Plant

fun PlantDetailsDTO.toEntity() : Plant {
    return Plant(
        id = this.id,
        namePlant = this.namePlant,
        idealHumidity = this.idealHumidity,
        waterFrequency = this.waterFrequency,
        fertilizerInfo = this.fertilizerInfo,
        descriptionPlant = this.descriptionPlant,
        thumbnailUrl = this.thumbnailUrl
    )
}