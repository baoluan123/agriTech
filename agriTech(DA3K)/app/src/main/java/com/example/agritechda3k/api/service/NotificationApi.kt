package com.example.agritechda3k.api.service

import com.example.agritechda3k.api.dto.NotificationDTO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface NotificationApi {
    // 1. Lấy lịch sử thông báo theo chậu cây
    @GET("notifications/history/{plantUserId}")
    suspend fun getHistory(
        @Path("plantUserId") plantUserId:Long
    ): Response<List<NotificationDTO>>
    // 2. Đánh dấu đã đọc (Patch cho đúng chuẩn REST)
    @PATCH("notifications/{id}/read")
    suspend fun markAsRead(@Path("id") id:Long) : Response<Unit>
    //Chức năng Subscribe (SSE) không dùng Retrofit
    //    // vì nó là luồng dữ liệu chảy liên tục, mình sẽ dùng OkHttp EventSource riêng.

    // 3. Lấy tổng số tin chưa đọc của 1 User (Cho vòng lặp Polling 10s)
    @GET("notifications/unread-count/{userId}")
    suspend fun getUnreadCount(
        @Path("userId") userId: Long
    ): Response<Int>
    // 4. Đánh dấu tất cả thông báo của User là đã đọc (Khi nhấn nút Đóng Dialog)
    @PATCH("notifications/mark-all-read/{userId}")
    suspend fun markAllRead(
        @Path("userId") userId: Long
    ): Response<Unit>
}