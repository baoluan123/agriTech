package com.example.agritechda3k.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auths")
data class Auth(
    @PrimaryKey
    val id:Long,
    val username:String,
    val fullName:String,
    val role: Int?,
    val address:String?,
    val phone:String?,
    val levelAdmin:Int?
)
