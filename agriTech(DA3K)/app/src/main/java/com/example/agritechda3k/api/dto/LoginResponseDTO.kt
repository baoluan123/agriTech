package com.example.agritechda3k.api.dto

data class LoginResponseDTO(
    val accountId:Long,
    val username:String,
    val fullName:String,
    val role:Int?,
    val message:String,

    val phone: String? = null,
    val address: String? = null,
)
