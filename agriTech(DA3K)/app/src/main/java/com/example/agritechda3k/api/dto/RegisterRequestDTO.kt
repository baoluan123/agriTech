package com.example.agritechda3k.api.dto

data class RegisterRequestDTO(
    val username:String,
    val password:String,
    val role:Int,
    val fullName:String,
    val address:String,
    val phone:String,
    val levelAdmin:Int?
)
