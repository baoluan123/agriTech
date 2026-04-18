package com.example.agritechda3k.api.dto

data class PlantListDTO(
    val id:Long,
    val namePlant:String,
    val descriptionPlant:String,
    val thumbnailUrl: String // Link ảnh từ Spring
)
