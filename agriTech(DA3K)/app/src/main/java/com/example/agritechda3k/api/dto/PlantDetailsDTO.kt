package com.example.agritechda3k.api.dto

data class PlantDetailsDTO(
    val id:Long,
    val namePlant:String,
    val idealHumidity: Float?,
    val waterFrequency: Int?,
    val fertilizerInfo: String?,
    val descriptionPlant: String?,
    val thumbnailUrl: String? // Link ảnh từ Spring
)
