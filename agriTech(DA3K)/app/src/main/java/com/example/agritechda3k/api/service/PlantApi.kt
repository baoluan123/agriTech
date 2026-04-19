package com.example.agritechda3k.api.service

import com.example.agritechda3k.api.dto.PlantListDTO
import retrofit2.Response
import retrofit2.http.GET

interface PlantApi {
    @GET("plants")
    suspend fun getPlantList(): Response<List<PlantListDTO>>
    // Lấy chi tiết một loài cây cụ thể
//    @GET("api/plants/{id}")
//    suspend fun getPlantById(@Path("id") id: Long): Response<PlantDTO>
}