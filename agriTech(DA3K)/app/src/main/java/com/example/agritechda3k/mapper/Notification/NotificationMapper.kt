package com.example.agritechda3k.mapper.Notification

import com.example.agritechda3k.api.dto.NotificationDTO
import com.example.agritechda3k.model.Notification

fun NotificationDTO.toEntityss() : Notification{
    return Notification(
        id = this.id,
        userId = this.userId,
        plantUserId = this.plantUserId,
        title = this.title,
        message = this.message,
        isRead = this.isRead,
        createdAt = this.createdAt,
        deviceCode = this.deviceCode
    )
}

fun List<NotificationDTO>.toEntityssList(): List<Notification> {
    return this.map { it.toEntityss() }
}