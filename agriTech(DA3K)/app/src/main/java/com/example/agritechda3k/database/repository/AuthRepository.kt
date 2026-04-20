package com.example.agritechda3k.database.repository

import com.example.agritechda3k.api.dto.LoginRequestDTO
import com.example.agritechda3k.api.service.AuthApi
import com.example.agritechda3k.database.dao.AuthDao
import com.example.agritechda3k.mapper.auth.toEntityAuth

class AuthRepository(private val api: AuthApi,private val dao: AuthDao) {
    suspend fun login(username:String,password:String): Result<String> {
        return  try {
            val response = api.login(LoginRequestDTO(username,password))
            if(response.isSuccessful && response.body() != null){
                val loginResponse = response.body()!!
                if(loginResponse.message?.contains("thành công") == true){
                    val authEntity = loginResponse.toEntityAuth()
                    dao.insertAuth(authEntity)
                    Result.success(loginResponse.message ?: "Đăng nhập thành công")
                } else {
                    Result.failure(Exception("Phản hồi từ server trống"))
                }

            } else {
                //hứng cái ResponseEntity.badRequest().body(e.getMessage())
                val errorMsg = response.errorBody()?.string() ?: "Lỗi đăng nhập"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            // Hứng các lỗi kết nối, Timeout...
            // Lỗi mạng, Timeout, lỗi Parse JSON...
            Result.failure(Exception("Không thể kết nối đến máy chủ: ${e.message}"))
        }

    }
}