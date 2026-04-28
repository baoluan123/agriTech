package com.example.agritechda3k.api.HT

data class SensorPoint(
    val value: Float,     // Giá trị độ ẩm đất để vẽ cao thấp
    val timeLabel: String // Giờ:Phút (HH:mm) để hiện dưới trục X
)
