package com.example.agritechda3k.database.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.agritechda3k.api.SSEClient
import com.example.agritechda3k.api.service.NotificationApi
import com.example.agritechda3k.database.dao.NotificationDao
import com.example.agritechda3k.mapper.Notification.toEntityssList
import com.example.agritechda3k.model.Notification
import kotlinx.coroutines.flow.Flow

class NotificationRepository(
    private val api: NotificationApi,
    private val dao: NotificationDao,
    private val sseClient: SSEClient
) {
    // 1. Lấy lịch sử từ Room để hiện lên UI (Dùng Flow để tự cập nhật khi có tin mới)
    fun getHistory(plantUserId: Long): Flow<List<Notification>> {
        return dao.getHistoryByPlant(plantUserId)
    }
    // 2. Gọi API lấy lịch sử từ Server và lưu vào Room (Sync dữ liệu)
    suspend fun refreshHistory(plantUserId: Long) {
        try {
            val response = api.getHistory(plantUserId)
            if(response.isSuccessful) {
                response.body()?.let {
                    dtoList->
                    // Dùng Mapper của ông để biến DTO thành Entity/Model Room
                    val entity = dtoList.toEntityssList()
                    // Lưu vào Room (cái này sẽ làm Flow ở trên tự phát tín hiệu mới cho UI)
                    dao.insertAll(entity)
                }
            }
        }catch (e: Exception) {
            Log.e("Repo", "Lỗi refresh lịch sử: ${e.message}")
        }
    }
    // 3. Đánh dấu đã đọc (Vừa báo Server, vừa cập nhật Room)
    suspend fun markAsRead(notificationId: Long) {
        try {
            val response = api.markAsRead(notificationId)
            if(response.isSuccessful) {
                dao.markAsRead(notificationId)
            }
        }catch (e: Exception) {
            Log.e("Repo", "Lỗi markAsRead: ${e.message}")
        }
    }
    // 4. Quản lý Real-time (SSE)
    fun startRealtimeNotifications(UserId:Long) {
        sseClient.startListening(UserId)
    }
    fun stopRealtimeNotifications() {
        sseClient.stopListening()
    }
    // 5. Check TỔNG số tin chưa đọc (Cho cái vòng lặp 10s ở MainActivity)
    suspend fun getTotalUnreadCount(userId: Long): Int {
        return try {
            val response = api.getUnreadCount(userId) // Gọi API
            if (response.isSuccessful) response.body() ?: 0 else 0
        } catch (e: Exception) {
            Log.e("Repo", "Lỗi lấy unread count: ${e.message}")
            0
        }
    }
    // 6. Đánh dấu đọc hết cho 1 User (Để dập cái Dialog cảnh báo)
    suspend fun markAllRead(userId: Long) {
        try {
            val response = api.markAllRead(userId)
            if (response.isSuccessful) {
                dao.markAllAsReadForUser(userId) // Nhớ thêm hàm này vào DAO nữa nhé
            }
        } catch (e: Exception) {
            Log.e("Repo", "Lỗi markAllRead: ${e.message}")
        }
    }
}