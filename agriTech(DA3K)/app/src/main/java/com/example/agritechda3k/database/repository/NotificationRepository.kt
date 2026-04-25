package com.example.agritechda3k.database.repository

import androidx.lifecycle.LiveData
import com.example.agritechda3k.api.service.NotificationApi
import com.example.agritechda3k.database.dao.NotificationDao
import com.example.agritechda3k.model.Notification

class NotificationRepository(
    private val api: NotificationApi,
    private val dao: NotificationDao
) {
    // 1. Lấy số lượng chưa đọc từ API (Phục vụ cái cửa sổ nhỏ Real-time) *** coi lai
    suspend fun fetchUnreadCount(plantUserId:Long):Long? {
        return try {
            val response = api.getUnreadCount(plantUserId).execute()
            if(response.isSuccessful) response.body() else 0
        }catch (e: Exception) {
            null
        }
    }

    // 2. Lấy toàn bộ lịch sử và lưu vào máy (Để xem offline)
    suspend fun refreshHistory(plantUserId:Long) {
        try {
            val response = api.getAllNotifications(plantUserId).execute()
            if(response.isSuccessful) {
                //tao file mapping sau
                val entity = response.body()?.map {
                        dto ->
                    Notification(dto.id, dto.title, dto.message, dto.isRead, dto.createdAt, plantUserId)
                }
                if(entity != null) {
                    dao.insertNotifications(entity)
                }
            }
        }catch (e: Exception) {
            // Xử lý lỗi mạng ở đây
        }

    }
    // 3. Đánh dấu đã đọc (Vừa báo lên Server, vừa cập nhật ở máy)
    suspend fun markAsRead(notificationId: Long) {
        try {
            // Phải thêm .execute() để nó thực sự gửi request lên Server
            api.markAsRead(notificationId).execute()
        } catch (e: Exception) {
            // Nếu lỗi mạng thì vẫn cập nhật local để user đỡ khó chịu,
            // nhưng server sẽ chưa update được
        }
        dao.markAsRead(notificationId)
    }

    //them ham nay
    suspend fun fetchTotalUnread(userId: Long): Long? {
        return try {
            val response = api.getTotalUnreadCount(userId).execute()
            if (response.isSuccessful) response.body() else 0
        } catch (e: Exception) { null }
    }

    //thenm thang nay sau activity
    // Không có suspend nhé, vì LiveData bản thân nó đã chạy bất đồng bộ rồi
    fun getNotificationsFromRoom(plantUserId: Long): LiveData<List<Notification>> {
        return dao.getNotificationsByPlant(plantUserId)
    }
}