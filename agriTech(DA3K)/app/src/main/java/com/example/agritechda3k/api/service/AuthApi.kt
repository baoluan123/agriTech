package com.example.agritechda3k.api.service

import com.example.agritechda3k.api.dto.LoginRequestDTO
import com.example.agritechda3k.api.dto.LoginResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDTO): Response<LoginResponseDTO>
}