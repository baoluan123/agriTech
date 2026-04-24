package com.example.agritechda3k.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "plant_user",
    foreignKeys = [
        ForeignKey(
            entity = Plant::class,
            parentColumns = ["id"],
            childColumns = ["plantId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["plantId"])] // Đánh index cho khóa ngoại để truy vấn nhanh
)
data class PlantUser(
    @PrimaryKey val id: Long,
    val customName: String?,
    val lastWatered: String?,
    val status: Boolean,
    val plantId: Long, // ID tham chiếu tới bảng Plant
    val userId: Long,
    // Lưu thêm mấy cái "gọt sẵn" để hiện ở List cho nhanh
    val plantName: String?,
    val imageUrl: String?,
    val fertilizerInfo:String?,
    val fullName:String?,
    val deviceCode: String?
)
