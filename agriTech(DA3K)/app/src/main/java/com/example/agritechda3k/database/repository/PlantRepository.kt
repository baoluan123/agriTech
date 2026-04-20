package com.example.agritechda3k.database.repository

import android.util.Log
import com.example.agritechda3k.api.service.PlantApi
import com.example.agritechda3k.database.dao.PlantDao
import com.example.agritechda3k.model.Plant
import com.example.agritechda3k.mapper.plant.*
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
}