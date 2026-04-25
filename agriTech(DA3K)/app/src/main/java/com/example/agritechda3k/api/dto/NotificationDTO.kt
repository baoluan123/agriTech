package com.example.agritechda3k.api.dto

import java.io.Serializable

data class NotificationDTO(
    val id: Long,
    val title: String,
    val message: String,
    val isRead: Boolean,
    val createdAt: String, // Server trả về chuỗi ISO-8601
    val deviceId: Long? = null
) : Serializable
