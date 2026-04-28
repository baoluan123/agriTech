package com.example.agritechda3k.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sensor_data")
data class Sensor(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val deviceId: Long,
    val sensorId: String?,
    val soilMoisture: Int, // Chốt thằng này để vẽ biểu đồ
    val humidity: Double?,

    val temperature: Double?,

    val status: String?,

    val recordedAt: String // Lưu dạng String "yyyy-MM-dd HH:mm:ss" để dễ mapping
)
