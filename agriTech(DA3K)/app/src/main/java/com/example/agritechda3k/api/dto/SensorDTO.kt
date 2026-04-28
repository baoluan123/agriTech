package com.example.agritechda3k.api.dto

data class SensorDTO(

    val sensorId: String?,    // Để biết dữ liệu của cảm biến nào
    val soilMoisture: Int,    // Giá trị chính để vẽ biểu đồ
    val recordedAt: String    // Thời gian để làm nhãn trục X
)
