package com.example.agritechda3k.database.repository

import android.util.Log
import com.example.agritechda3k.api.dto.PlantUserDTO
import com.example.agritechda3k.api.service.PlantApi
import com.example.agritechda3k.database.dao.PlantDao
import com.example.agritechda3k.model.Plant
import com.example.agritechda3k.mapper.plant.*
import com.example.agritechda3k.mapper.plantuser.*
import com.example.agritechda3k.model.PlantUser
import kotlinx.coroutines.flow.Flow

class PlantRepository(private val plantDao: PlantDao, private val plantApi: PlantApi) {
    // 1. Luồng dữ liệu "sống" từ Room (UI luôn quan sát cái này)
    val allPlant: Flow<List<Plant>> = plantDao.getAllPlant()


    suspend fun loadData() {
        try {
            val response = plantApi.getPlantList() // Lấy từ API
            // Dùng hàm mình vừa viết ở Mapper
            if (response.isSuccessful) {
                // Dùng let để check null an toàn
                response.body()?.toEntityList()?.let { entities ->
                    // Lúc này 'entities' chắc chắn không null, truyền vào thoải mái
                    plantDao.addPlant(entities)
                    Log.d("PLANT_REPO", "Đã cập nhật ${entities.size} cây vào Room")
                }
            }
        }catch (e: Exception) {
            Log.e("PLANT_REPO", "Lỗi đồng bộ: ${e.message}")
        }
    }
    suspend fun detailData(id:Long):Result<Plant> {
       return try {
            val response = plantApi.getPlantDetail(id)
            if(response.isSuccessful) {
                val dto = response.body()
                if(dto != null) {
                    val entity = dto.toEntity()
                    plantDao.addPlantDetail(entity)
                    Log.d("PLANT_REPO", "Đã cập nhật $entity vào Room")
                    Result.success(entity)
                } else {
                    Result.failure(Exception("Dữ liệu chi tiết trống"))
                }
            } else {
                val error = response.errorBody()?.string() ?: "Lỗi server"
                Result.failure(Exception(error))
            }
        }catch (e: Exception) {
            Log.e("PLANT_REPO", "Lỗi đồng bộ: ${e.message}")
            Result.failure(e)
        }
    }
    suspend fun addPlantToGarden(plantId:Long,deviceCode:String,lastWatered:String):Result<PlantUser> {
        return try {
            val authId:Long = plantDao.getAuth()
            val requestDTO = PlantUserDTO(
                id = 0, // serve tự sinh
                customName = null, // Hoặc truyền tên nếu muốn
                lastWatered = lastWatered,
                status = true, //*
                plantId = plantId,
                userId = authId,
                deviceCode = deviceCode,
                plantName = null, imageUrl = null, fertilizerInfo = null, fullName = null // Mấy cái này Server sẽ trả về sau
            )
            // 3. NHIỆM VỤ 1: Gửi sang Spring Boot
            val response = plantApi.savePlantUser(requestDTO)
            if (response.isSuccessful && response.body() != null) {
                val savedDTO = response.body()!!

                // 4. MAPPING: Chuyển DTO đầy đủ (có tên cây, ảnh) từ Server về Entity
                val entity = savedDTO.toEntitys()

                // 5. NHIỆM VỤ 2: Lưu vào SQLite
                plantDao.insertPlantUser(entity)

                Result.success(entity)
            } else {
                Result.failure(Exception("Lỗi Server: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("REPO_ERROR", "Lỗi addPlantToGarden: ${e.message}")
            Result.failure(e)
        }
    }
}