package com.example.agritechda3k.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.agritechda3k.model.Auth
@Dao
interface AuthDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // Nếu login lại thì ghi đè user mới
    suspend fun insertAuth(auth: Auth)

    @Query("SELECT*FROM auths LIMIT 1")
    suspend fun getAuth(): Auth?

    @Query("DELETE FROM auths")
    suspend fun clearAuth()
    // Lấy ID của tài khoản đang đăng nhập (giả sử bạn lưu trạng thái isLoggedIn)
    @Query("SELECT id FROM auths")
    suspend fun getLoggedInId(): Long?
}