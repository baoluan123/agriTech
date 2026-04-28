package com.example.agritechda3k.api.service

import com.example.agritechda3k.api.dto.SensorDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SensorApi {
    @GET("sensor/history/{deviceId}")
    suspend fun getSensorHistory(@Path("deviceId") deviceId: Long, @Path("limit") limit: Int): Response<List<SensorDTO>>
}