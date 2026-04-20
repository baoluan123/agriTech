package com.example.agritechda3k.database.repository

import android.util.Log
import com.example.agritechda3k.api.service.PlantUserApi
import com.example.agritechda3k.database.dao.PlantUserDao
import com.example.agritechda3k.model.PlantUser
import com.example.agritechda3k.mapper.plantuser.toEntityList

import kotlinx.coroutines.flow.Flow

class PlantUserRepository(private val plantUserDao: PlantUserDao, private val plantUserApi: PlantUserApi) {
    // 1. Dữ liệu "sống" cho màn hình My Garden (Quan sát trực tiếp từ Room)
    val allMyPlants: Flow<List<PlantUser>> = plantUserDao.getAllMyPlants()
    // 2. Hàm đồng bộ dữ liệu từ Spring Boot về máy
    suspend fun loadMyPlants() {
        try {
            val userId:Long = plantUserDao.getAuthId()

            val response = plantUserApi.getMyPlantList(userId)

            if (response.isSuccessful) {
                // Sử dụng Mapper để chuyển List DTO -> List Entity
                response.body()?.toEntityList()?.let { entities ->
                    // Xóa dữ liệu cũ (tùy chọn) và thêm dữ liệu mới nhất từ Server vào Room
                    // myPlantDao.deleteAll() // Nếu ông muốn reset data mỗi lần load
                    plantUserDao.insertAll(entities)
                    Log.d("USER_REPO", "Đã đồng bộ ${entities.size} cây của User $userId")
                }
            } else {
                Log.e("USER_REPO", "Lỗi API: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            // Xử lý lỗi kết nối, mất mạng...
            Log.e("USER_REPO", "Lỗi kết nối Server: ${e.message}")
        }
    }

    // 3. Lấy 1 cây duy nhất từ Room để hiện màn hình Detail
    // Vì data đã được "gọt" sẵn 4 bảng rồi, nên lấy từ Room ra là đủ hết info
    fun getMyPlantById(id: Long): Flow<PlantUser?> {
        return plantUserDao.getMyPlantById(id)
    }

}