package com.example.agritechda3k.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.agritechda3k.model.Notification
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    // 1. Lưu thông báo mới (Dùng cho cả SSE và API History)
    // Dùng REPLACE để nếu Server gửi tin cập nhật trùng ID thì nó tự đè lên
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: Notification)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notifications: List<Notification>)

    // 2. Lấy lịch sử theo chậu cây (Dùng Flow để UI tự động cập nhật khi có tin mới)
    @Query("SELECT * FROM notifications WHERE plantUserId = :plantUserId ORDER BY createdAt DESC")
    fun getHistoryByPlant(plantUserId: Long): Flow<List<Notification>>

    // 3. Đánh dấu đã đọc dưới máy
    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Long)

    // 4. Xóa thông báo cũ (nếu ông muốn dọn dẹp bộ nhớ)
    @Query("DELETE FROM notifications WHERE plantUserId = :plantUserId")
    suspend fun deleteByPlant(plantUserId: Long)
    // Đánh dấu tất cả thông báo của 1 user là đã đọc trong Room
    @Query("UPDATE notifications SET isRead = 1 WHERE userId = :userId")
    suspend fun markAllAsReadForUser(userId: Long)


}
////them thang nay sau thang activity read-time
//@Query("SELECT * FROM notifications WHERE plantUserId = :plantId ORDER BY createdAt DESC")
//fun getNotificationsByPlant(plantId: Long): LiveData<List<Notification>>