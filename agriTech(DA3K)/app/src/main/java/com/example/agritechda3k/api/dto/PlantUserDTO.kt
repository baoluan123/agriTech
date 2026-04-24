package com.example.agritechda3k.api.dto

data class PlantUserDTO(
    val id: Long,
    val customName: String?,
    val lastWatered: String?, // Bên Spring gọt về String rồi thì ở đây để String
    val status: Boolean?,
    val plantId: Long,
    val plantName: String?,
    val imageUrl: String?,
    val fertilizerInfo: String?,
    val fullName: String?, // Nếu bên Spring ông đặt là FullName thì ở đây phải là FullName
    val userId: Long,
    val deviceCode:String
)
