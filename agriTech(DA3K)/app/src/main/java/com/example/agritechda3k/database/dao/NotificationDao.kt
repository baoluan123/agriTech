package com.example.agritechda3k.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.agritechda3k.model.Notification

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications WHERE plantUserId = :plantUserId ORDER BY createdAt DESC")
    fun getAllNotifications(plantUserId: Long): LiveData<List<Notification>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<Notification>)
    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Long)
    @Query("SELECT COUNT(*) FROM notifications WHERE plantUserId = :plantUserId AND isRead = 0")
    fun getUnreadCount(plantUserId: Long): LiveData<Int>

    //them thang nay sau thang activity read-time
    @Query("SELECT * FROM notifications WHERE plantUserId = :plantId ORDER BY createdAt DESC")
    fun getNotificationsByPlant(plantId: Long): LiveData<List<Notification>>
}