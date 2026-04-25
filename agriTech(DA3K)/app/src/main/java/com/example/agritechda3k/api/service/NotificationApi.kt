package com.example.agritechda3k.api.service

import com.example.agritechda3k.api.dto.NotificationDTO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface NotificationApi {
    // API đếm số chưa đọc xem lịch sử từng cây coi lai****
    @GET("notifications/plant-user/{id}/unread-count")
    fun getUnreadCount(@Path("id") plantUserId:Long): Call<Long>
    // API lấy list lịch sử
    @GET("notifications/plant-user/{id}")
    fun getAllNotifications(@Path("id") plantUserId: Long): Call<List<NotificationDTO>>
    // 3. API đánh dấu đã đọc (Để tắt cảnh báo real-time sau khi user đã xem)
    @PATCH("api/notifications/{id}/read")
    fun markAsRead(@Path("id") notificationId: Long): Call<ResponseBody>

    // Thêm hàm này
    @GET("notifications/user/{userId}/unread-count")
    fun getTotalUnreadCount(@Path("userId") userId: Long): Call<Long>
}