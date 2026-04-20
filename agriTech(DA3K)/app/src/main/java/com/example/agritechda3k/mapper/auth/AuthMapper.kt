package com.example.agritechda3k.mapper.auth

import com.example.agritechda3k.api.dto.LoginResponseDTO
import com.example.agritechda3k.model.Auth

fun LoginResponseDTO.toEntityAuth(): Auth{
    return Auth(
        id = this.accountId,
        username = this.username,
        fullName = this.fullName,
        role = this.role ?: 0,
        address = this.address,
        phone = this.phone,
        levelAdmin = null
    )
}