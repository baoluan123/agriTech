package com.example.agritechda3k.api.dto

//import java.io.Serializable

data class NotificationDTO(
    val id: Long,               // ID để Patch lên server báo "đã đọc"
    val userId: Long,           // Dùng để logic nội bộ hoặc lọc tin (nếu cần)
    val plantUserId: Long,      // ID quan trọng để click vào tin nhắn là nhảy tới đúng cây
    val title: String,
    val message: String,
    val isRead: Boolean,        // Boolean trong Kotlin tự hiểu true/false của Java
    val createdAt: String,      // String từ "yyyy-MM-dd HH:mm:ss" bên Spring gửi qua
    val deviceCode: String?     // Có thể null nếu tin nhắn chung, để String? cho an toàn
)
