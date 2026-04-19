package com.example.agritechda3k.api.service

import com.example.agritechda3k.api.dto.PlantUserDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PlantUserApi {
    @GET("myplant/{userId}")
    suspend fun getMyPlantList(
        @Path("userId") userId: Long
    ): Response<List<PlantUserDTO>> // Phải tạo thêm file PlantUserDTO này
    @GET("myplant/detail/{id}")
    suspend fun getDetailMyPlant(@Path("id") id: Long) : Response<PlantUserDTO>
}