package com.example.agritechda3k.mapper.Notification

import com.example.agritechda3k.api.HT.SensorPoint
import com.example.agritechda3k.api.dto.SensorDTO
import com.example.agritechda3k.model.Sensor

fun SensorDTO.toEntitySensor(plantuserIdKotLin:Long) : Sensor{
    return Sensor(
        id = 0,
        deviceId = plantuserIdKotLin, //ve ban chat la 1 muon khong long vong di cho le
        sensorId = this.sensorId,
        soilMoisture = this.soilMoisture,
        recordedAt = this.recordedAt,
        // Các trường này DTO 3 trường không có thì để null
        humidity = null,
        temperature = null,
        status = null
    )
}
// Mapper quan trọng nhất: Từ DTO sang Point để vẽ biểu đồ luôn
fun SensorDTO.toSensorPoint(): SensorPoint{
    // Gọt "2026-04-28 20:15:00" -> "20:15"
    val displayTime = if (this.recordedAt.length >= 16){
        this.recordedAt.substring(11, 16)
    } else {
        this.recordedAt
    }
    return SensorPoint(
        value = this.soilMoisture.toFloat(),
        timeLabel = displayTime
    )
}