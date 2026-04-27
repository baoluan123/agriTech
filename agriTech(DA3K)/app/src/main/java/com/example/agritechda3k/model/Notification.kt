package com.example.agritechda3k.model

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "notifications")
data class Notification(
    @PrimaryKey val id: Long, // Lấy ID từ Server để không bị trùng
    val userId: Long,
    val plantUserId: Long,
    val title: String,
    val message: String,
    var isRead: Boolean,
    val createdAt: String,
    val deviceCode: String?

)
